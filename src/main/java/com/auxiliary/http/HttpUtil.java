package com.auxiliary.http;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.file.TextFileReadUtil;

/**
 * <p>
 * <b>文件名：HttpUtil.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口调试常用的工具方法
 * </p>
 * <p>
 * <b>编码时间：2022年8月4日 下午4:53:15
 * </p>
 * <p>
 * <b>修改时间：2022年8月4日 下午4:53:15
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.6.0
 */
public class HttpUtil {
    /**
     * 该方法用于读取Har文件中的接口信息，并将其转换为接口信息类对象的方法
     * 
     * @param harFile       Har文件
     * @param resourceTypes 需要保留的接口类型，不传则保留所有类型接口
     * @return 转换后的接口信息类集合
     * @since autest 3.6.0
     */
    public static List<InterfaceInfo> HarFile2InterfaceInfo(File harFile, ResourceType... resourceTypes) {
        List<InterfaceInfo> interList = new ArrayList<>();
        // 将需要过滤的接口类型数组转换为Set集合
        Set<ResourceType> typeSet = new HashSet<>(Arrays.asList(resourceTypes));

        // 读取HAR文件，并将其转换为JSON类对象
        JSONArray interJosnArray = JSONObject.parseObject(TextFileReadUtil.megerTextToTxt(harFile, false))
                .getJSONObject("log").getJSONArray("entries");
        // 遍历接口Json集合，过滤掉不需要的接口，并将剩余的接口Json信息封装为接口信息类
        for (int index = 0; index < interJosnArray.size(); index++) {
            JSONObject interJson = interJosnArray.getJSONObject(index);
            // 判断接口类型是否为需要过滤的类型
            if (!typeSet.contains(ResourceType.valueOf(interJson.getString("_resourceType").toUpperCase()))
                    && !typeSet.isEmpty()) {
                continue;
            }

            // 获取接口请求Json部分
            JSONObject requestJson = interJson.getJSONObject("request");
            InterfaceInfo info = new InterfaceInfo();

            // 读取接口请求类型
            info.setRequestType(RequestType.valueOf(requestJson.getString("method").toUpperCase()));
            // 读取接口url信息
            info.analysisUrl(requestJson.getString("url"));

            // 添加请求头信息
            JSONArray headerJosnArray = requestJson.getJSONArray("headers");
            for (int headerIndex = 0; headerIndex < headerJosnArray.size(); headerIndex++) {
                JSONObject headerJson = headerJosnArray.getJSONObject(headerIndex);
                info.addRequestHeader(headerJson.getString("name"), headerJson.getString("value"));
            }

            // 添加cookie信息
            JSONArray cookiesJsonArray = requestJson.getJSONArray("cookies");
            for (int cookieIndex = 0; cookieIndex < cookiesJsonArray.size(); cookieIndex++) {
                JSONObject cookiesJson = cookiesJsonArray.getJSONObject(cookieIndex);
                info.addCookie(cookiesJson.getString("name"), cookiesJson.getString("value"));
            }
            
            // 若存在请求体信息，则添加请求体
            JSONObject postDataJson = requestJson.getJSONObject("postData");
            if (postDataJson != null) {
                // 获取请求体内容类型
                String messageTypeText = postDataJson.getString("mimeType");
                String chaersetJudgeText = "charset=";
                MessageType messageType;
                // 判断请求体内容是否包含字符集编码，若存在，则对其进行分离，并根据指定内容设置请求体类型枚举
                if (messageTypeText.contains(chaersetJudgeText)) {
                    messageType = MessageType.typeOf(messageTypeText.substring(0, messageTypeText.indexOf(";")))
                            .setCharset(messageTypeText.substring(
                                    messageTypeText.indexOf(chaersetJudgeText) + chaersetJudgeText.length()));
                } else {
                    messageType = MessageType.typeOf(messageTypeText);
                }

                // 获取请求体，若类型为表单格式，则获取params参数中的内容；若为其他格式，则获取text参数中的内容
                if (messageType == MessageType.X_WWW_FORM_URLENCODED || messageType == MessageType.FORM_DATA) {
                    JSONArray dataListJson = postDataJson.getJSONArray("params");
                    List<Entry<String, Object>> dataList = new ArrayList<>();
                    for (int dataIndex = 0; dataIndex < dataListJson.size(); dataIndex++) {
                        JSONObject dataJson = dataListJson.getJSONObject(dataIndex);
                        dataList.add(
                                new Entry<>(dataJson.getString("name"), dataJson.getString("value")));
                    }

                    info.setFormBody(messageType, dataList);
                } else {
                    info.setBodyContent(messageType, postDataJson.getString("text"));
                }
            }

            interList.add(info);
        }


        return interList;
    }

    /**
     * 该方法用于对X_WWW_FORM_URLENCODED表单类型的请求体进行拼接，转换成相应的表达式
     * 
     * @return 表达式文本
     * @since autest 3.6.0
     */
    public static String formUrlencoded2Extract(List<Entry<String, Object>> dataList) {
        // 判断当前设置的请求体类型是否为X_WWW_FORM_URLENCODED类型
        return Optional.ofNullable(dataList).filter(list -> !list.isEmpty()).map(list -> {
            // 获取表单集合，并将其拼接为表达式
            StringJoiner extract = new StringJoiner("&");
            for (Entry<String, Object> data : dataList) {
                extract.add(String.format("%s=%s", data.getKey(), data.getValue().toString()));
            }

            return extract.toString();
        }).orElse("");
    }
}
