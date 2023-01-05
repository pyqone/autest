package com.auxiliary.testcase.templet;

import java.util.Arrays;

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
        // 判断json中是否包含相应的字段内容，若未包含指定的字段，则为字段添加相应的数组json
        if (!caseJson.containsKey(field)) {
            caseJson.put(field, new JSONArray());
        }
        JSONArray contentListJson = caseJson.getJSONArray(field);
        
        // 处理需要插入的下标数据，使其能正确进行插入
        if (contentListJson.isEmpty()) {
            insertIndex = 0;
        } else {
            insertIndex = DisposeCodeUtils.customizedIndex2ArrayIndex(insertIndex, 1, contentListJson.size() + 1, 1,
                    true, false, false, false);
        }

        // 插入指定的数据
        contentListJson.addAll(insertIndex, Arrays.asList(contents));
        
        return this;
    }

    @Override
    public String toString() {
        return caseJson.toJSONString();
    }
}
