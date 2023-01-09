package com.auxiliary.testcase.templet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.common.DisposeCodeUtils;

/**
 * <p>
 * <b>文件名：CaseData.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 提供测试用例数据的写入和返回方法
 * </p>
 * <p>
 * <b>编码时间：2023年1月4日 下午2:24:26
 * </p>
 * <p>
 * <b>修改时间：2023年1月4日 下午2:24:26
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public class CaseData {
    /**
     * 定义json中内容集合的字段名
     */
    private final String JSON_CONTENT = "content";

    /**
     * 用于存储测试用例的内容json
     */
    private JSONObject caseJson = new JSONObject();

    /**
     * 该方法用于向指定的字段中插入指定的内容，下标支持反序遍历
     * 
     * @param field       字段名称
     * @param insertIndex 插入的下标
     * @param contents    插入的内容字符串数组
     * @return 类本身
     * @since autest 4.0.0
     */
    public CaseData addContent(String field, int insertIndex, String... contents) {
        return addContent(field, insertIndex, Arrays.asList(contents));
    }

    /**
     * 该方法用于向指定的字段中插入指定的内容，下标支持反序遍历
     * 
     * @param field       字段名称
     * @param insertIndex 插入的下标
     * @param contentList 插入的内容字符串集合
     * @return 类本身
     * @since autest 4.0.0
     */
    public CaseData addContent(String field, int insertIndex, List<String> contentList) {
        // 判断字段与内容数组是否包含数据，若为null或为空，则不进行操作
        if (field == null || field.isEmpty()) {
            return this;
        }
        if (contentList == null || !contentList.isEmpty()) {
            return this;
        }

        // 获取内容组
        JSONArray contentListJson = initFieldJson(field).getJSONArray(JSON_CONTENT);

        // 处理需要插入的下标数据，使其能正确进行插入
        if (contentListJson.isEmpty()) {
            insertIndex = 0;
        } else {
            insertIndex = DisposeCodeUtils.customizedIndex2ArrayIndex(insertIndex, 1, contentListJson.size() + 1, 1,
                    true, false, false, false);
        }

        // 插入指定的数据
        contentListJson.addAll(insertIndex, contentList);

        return this;
    }

    /**
     * 该方法用于对指定字段中的内容进行返回，若字段不存在，则返回空集合
     * 
     * @param field 字段
     * @return 字段对应的内容集合
     * @since autest 4.0.0
     */
    public List<String> getContent(String field) {
        // 定义需要返回的字符串集合
        ArrayList<String> contentList = new ArrayList<>();

        // 若用例json中包含该字段，则获取该字段的jsonArray，之后逐一设置数据
        if (caseJson.containsKey(field)) {
            JSONArray contentListJson = caseJson.getJSONArray(field);
            // 将字段中的内容添加至内容集合中
            for (int index = 0; index < contentListJson.size(); index++) {
                contentList.add(contentListJson.getString(index));
            }
        }

        return contentList;
    }

    /**
     * 该方法用于对用例json中的内容进行初始化，并返回初始化json类对象
     * 
     * @param field 字段名称
     * @return 字段对应的json类对象
     * @since autest 4.0.0
     */
    private JSONObject initFieldJson(String field) {
        // 判断字段内容是否存在，不存在，则创建并初始化字段
        if (!caseJson.containsKey(field)) {
            // 创建字段json
            caseJson.put(field, new JSONObject());
            JSONObject fieldJson = caseJson.getJSONObject(field);

            // 创建json中所需要包含的字段
            fieldJson.put(JSON_CONTENT, new JSONArray());

            return fieldJson;
        } else {
            return caseJson.getJSONObject(field);
        }
    }

    @Override
    public String toString() {
        return caseJson.toJSONString();
    }
}
