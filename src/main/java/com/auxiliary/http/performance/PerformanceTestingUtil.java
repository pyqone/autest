package com.auxiliary.http.performance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.auxiliary.http.ExtractResponse;
import com.auxiliary.http.InterfaceInfo;
import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：PerformanceTestingUtil.java</b>
 * </p>
 * <p>
 * <b>用途：提供根据接口信息，生成性能测试相关脚本的工具</b>
 * </p>
 * <p>
 * <b>编码时间：2022年8月17日 下午4:04:41
 * </p>
 * <p>
 * <b>修改时间：2022年8月17日 下午4:04:41
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.6.0
 */
public class PerformanceTestingUtil {
    private static File file = new File("src/main/resources/com/auxiliary/http/PerformanceTestingScriptTemplet.xml");

//    public static String interfaceInfo2LRScrpt(InterfaceInfo inter) {
//        // 获取接口路径，作为接口名称
//        String interPath = inter.getPath();
//        // 获取接口的请求方式
//        RequestType requestType = inter.getRequestType();
//        // 获取接口的请求体，并对请求体进行分别处理
//        Entry<MessageType, Object> bodyEntry = inter.getBodyContent();
//        String encType = bodyEntry.getKey().toMessageTypeString();
//        Object body = bodyEntry.getValue();
//        if (body)
//    }

    public static String getMethodLRScript(InterfaceInfo inter, int beforeTabCount) throws DocumentException {
        Document dom = new SAXReader().read(file);
        String getText = dom.selectSingleNode("//*[@name='getMethod']").getText().trim();
        
        // 定义需要替换的词语集合
        HashMap<String, String> replaceKeyMap = new HashMap<>();

        // 添加前置制表符
        StringBuilder beforeTab = new StringBuilder();
        IntStream.range(0, beforeTabCount).mapToObj(index -> "\t").forEach(beforeTab::append);
        replaceKeyMap.put("beforeTab", beforeTab.toString());
        replaceKeyMap.put("tab", "\t");
        replaceKeyMap.put("line", "\r\n");
        
        // 替换接口路径关键词
        replaceKeyMap.put("path", inter.getPath());
        // 替换接口url
        replaceKeyMap.put("url", inter.toUrlString());

        // 替换请求头数据，若不存在请求头数据，则存储空串进行替换
        String headerKeyContent = DisposeCodeUtils.extractPlaceholderContent("@{", "}", "header.+", getText);
        // 将原文中的请求头占位符改为不带模板的占位符，并存储相关的替换数据
        getText = getText.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(headerKeyContent), "header");
        if (!inter.isEmptyHeader()) {
            // 获取请求头数据
            Map<String, String> headerMap = inter.getRequestHeaderMap();
            // 获取请求头模板
            String headerScript = headerKeyContent.substring(headerKeyContent.indexOf(" ") + 1);

            // 拼接请求头
            StringJoiner headerReplaceText = new StringJoiner("\r\n");
            headerMap.forEach((key, value) -> {
                replaceKeyMap.put("headerName", key);
                replaceKeyMap.put("headerValue", value);
                headerReplaceText
                        .add(DisposeCodeUtils.replacePlaceholder("@{", "}", headerScript, replaceKeyMap));
            });

            replaceKeyMap.put("header", headerReplaceText.toString());
        } else {
            replaceKeyMap.put("header", "");
        }

        // 替换cookie数据，若不存在请求头数据，则存储空串进行替换
        String cookieKeyContent = DisposeCodeUtils.extractPlaceholderContent("@{", "}", "cookie.+", getText);
        // 将原文中的请求头占位符改为不带模板的占位符，并存储相关的替换数据
        getText = getText.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(cookieKeyContent), "cookie");
        if (!inter.isEmptyCookie()) {
            // 获取请求头数据
            Map<String, String> cookieMap = inter.getCookieMap();
            // 获取请求头模板
            String cookieScript = cookieKeyContent.substring(cookieKeyContent.indexOf(" ") + 1);

            // 拼接请求头
            StringJoiner cookieReplaceText = new StringJoiner("\r\n");
            // 替换接口主机地址
            replaceKeyMap.put("host", inter.getHost());
            cookieMap.forEach((key, value) -> {
                replaceKeyMap.put("cookieName", key);
                replaceKeyMap.put("cookieValue", value);
                cookieReplaceText.add(DisposeCodeUtils.replacePlaceholder("@{", "}", cookieScript, replaceKeyMap));
            });

            replaceKeyMap.put("cookie", cookieReplaceText.toString());
        } else {
            replaceKeyMap.put("cookie", "");
        }
        
        Set<String> extractJsonSet = inter.getExtractRuleJson();
        StringJoiner extractText = new StringJoiner("\r\n");
        // 替换cookie数据，若不存在请求头数据，则存储空串进行替换
        String extractKeyContent = DisposeCodeUtils.extractPlaceholderContent("@{", "}", "extract.+", getText);
        // 将原文中的请求头占位符改为不带模板的占位符，并存储相关的替换数据
        getText = getText.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(extractKeyContent), "extract");
        String extractScript = extractKeyContent.substring(extractKeyContent.indexOf(" ") + 1);
        extractJsonSet.stream().map(JSONObject::parseObject).forEach(json -> {
            replaceKeyMap.put("extractParamName", json.getString(ExtractResponse.JSON_EXTRACT_SAVE_NAME));
            replaceKeyMap.put("extractLb", json.getString(ExtractResponse.JSON_EXTRACT_LB));
            replaceKeyMap.put("extractRb", json.getString(ExtractResponse.JSON_EXTRACT_RB));
            replaceKeyMap.put("extractSearch", json.getString(ExtractResponse.JSON_EXTRACT_SEARCH));
            extractText.add(DisposeCodeUtils.replacePlaceholder("@{", "}", extractScript, replaceKeyMap));
        });
        replaceKeyMap.put("extract", extractText.toString());

        return DisposeCodeUtils.replacePlaceholder("@{", "}", getText, replaceKeyMap);
    }

}
