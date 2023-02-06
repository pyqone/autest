package com.auxiliary.testcase.templet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.file.WriteTempletFile;

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
    private final String JSON_CONTENT = WriteTempletFile.KEY_CONTENT;

    /**
     * 用于存储测试用例的内容json
     */
    private JSONObject caseJson = new JSONObject();

    /**
     * 用于存储当前生成用例的模板类对象，以方便调取模板类对象中部分所需的功能
     */
    private AbstractCaseTemplet<?> caseTemplet;

    /**
     * 构造对象，并设置当前生成用例的模板类对象
     * <p>
     * 在写入用例时需要读取模板类中的内容，若通过用例模板子类生成的测试用例数据，建议使用该构造，在构造对象时使用如下形式进行构造
     * 
     * <pre>
     * <code>CaseData caseData = new CaseData(this)</code>
     * </pre>
     * </p>
     * <p>
     * 若不存在模板，可传入null进行构造
     * </p>
     * 
     * @param caseTemplet 用例模板类对象
     */
    public CaseData(AbstractCaseTemplet<?> caseTemplet) {
        this.caseTemplet = caseTemplet;
    }

    /**
     * 该方法用于返回用例模板类对象
     * 
     * @return 用例模板类对象
     * @since autest 4.0.0
     */
    public AbstractCaseTemplet<?> getCaseTemplet() {
        return caseTemplet;
    }

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
        return addContent(field, insertIndex,
                Arrays.asList(Optional.ofNullable(contents).orElseGet(() -> new String[] {})));
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
        if (contentList == null || contentList.isEmpty()) {
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
     * 该方法用于将测试用例类对象下下指定字段的内容拼接至当前测试用例类对象相应字段下
     * <p>
     * <b>注意：</b>若未指定字段，则默认将所有的字段内容拼接至当前用例类对象下
     * </p>
     * 
     * @param caseDate 已生成的测试用例类对象
     * @param fields   指定的字段组
     * @return 当前用例类对象
     * @since autest 4.0.0
     */
    public CaseData addContent(CaseData caseDate, String... fields) {
        // 判断两个参数是否为null
        if (fields == null) {
            return this;
        }
        Optional<CaseData> caseDataOpt = Optional.ofNullable(caseDate);
        if (!caseDataOpt.isPresent()) {
            return this;
        }

        // 遍历传入的caseData类对象中的所有字段
        for (String field : fields) {
            // 获取待添加用例类的字段内容，并将其转换为字符串集合
            List<String> contentList = caseDataOpt.map(data -> data.caseJson.getJSONObject(field))
                    .map(json -> json.getJSONArray(JSON_CONTENT)).map(jsonArr -> jsonArr.toJavaList(String.class))
                    .orElseGet(() -> new ArrayList<>());
            
            addContent(field, -1, contentList);
        }
        return this;
    }

    // TODO 添加移除、替换内容的方法

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
