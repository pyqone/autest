package com.auxiliary.tool.file;

import java.util.HashMap;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.tool.file.excel.ExcelFileTemplet;

/**
 * <p>
 * <b>文件名：</b>WriteFileData.java
 * </p>
 * <p>
 * <b>用途：</b> 存储写入文件中的数据
 * </p>
 * <p>
 * <b>编码时间：</b>2021年8月19日下午6:49:44
 * </p>
 * <p>
 * <b>修改时间：</b>2022年2月8日上午8:40:27
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 */
public class WriteFileData {
    /**
     * 存储当前使用的模板
     */
    private FileTemplet templet;
    /**
     * 存储当前需要写入文件的内容
     */
    private JSONObject contentJson = new JSONObject();
    /**
     * 存储当前需要使用的默认内容
     */
    private JSONObject defaultCaseJson = new JSONObject();
    /**
     * 存储模板当前正在编写的内容
     */
    private JSONObject caseJson = new JSONObject();
    /**
     * 存储当前已写入的内容条数
     */
    private int nowCaseNum = 0;
    /**
     * 存储当前模板的名称
     */
    private String tempName = "";
    /**
     * 存储待替换的词语以及被替换的词语
     */
    private HashMap<String, DataFunction> replaceWordMap = new HashMap<>(16);

    /**
     * 初始化相关的数据
     *
     * @param fileTemplet 模板文件对象
     */
    public WriteFileData(FileTemplet fileTemplet) {
        setTemplet(
                Optional.ofNullable(fileTemplet).orElseThrow(() -> new WriteFileException("未指定模板类")).getTempletJson());
        contentJson.put(WriteTempletFile.KEY_CASE, new JSONArray());
    }

    /**
     * 用于移除当前字段的默认内容
     *
     * @param field 字段id
     */
    public void removeFieldDefault(String field) {
        // 判断字段是否存在，若不存在，则不进行操作
        if (!templet.containsField(field)) {
            return;
        }

        defaultCaseJson.remove(field);
    }

    /**
     * 用于根据模板json对当前模板进行设置，调用该方法会覆盖当前的模板
     *
     * @param templetJsonText 模板json
     */
    public void setTemplet(String templetJsonText) {
        this.templet = new FileTemplet(templetJsonText);
        if (templet.containsAttribute(ExcelFileTemplet.KEY_NAME)) {
            tempName = templet.getTempletAttribute(ExcelFileTemplet.KEY_NAME).toString();
        }
    }

    /**
     * 用于根据内容json对当前内容进行设置，调用该方法会覆盖当前的内容
     *
     * @param contentJsonText 内容json
     */
    public void setContentJson(String contentJsonText) {
        this.contentJson = JSONObject.parseObject(contentJsonText);
    }

    /**
     * 用于根据字段默认内容json对当前字段默认内容进行设置，调用该方法会覆盖当前的字段默认内容
     *
     * @param defaultCaseJsonText 字段默认内容json
     */
    public void setDefaultCaseJson(String defaultCaseJsonText) {
        this.defaultCaseJson = JSONObject.parseObject(defaultCaseJsonText);
    }

    /**
     * 设置当前内容的行数
     *
     * @param nowCaseNum 当前内容的行数
     */
    public void setNowCaseNum(int nowCaseNum) {
        this.nowCaseNum = nowCaseNum;
    }

    /**
     * 用于添加待替换的词语及相应的替换方法
     *
     * @param functions 替换词语使用的函数
     */
    public void addReplaceWord(DataDriverFunction functions) {
        if (functions == null) {
            return;
        }

        replaceWordMap.put(functions.getRegex(), functions.getFunction());
    }

    /**
     * 用于返回当前模板类对象
     *
     * @return 模板类对象
     */
    public FileTemplet getTemplet() {
        return templet;
    }

    /**
     * 用于返回当前模板类对象json内容
     *
     * @return 模板类对象json内容
     */
    public String getTempletJsonText() {
        return templet.getTempletJson();
    }

    /**
     * 用于返回当前内容的json类对象
     *
     * @return 当前内容的json类对象
     */
    public JSONObject getContentJson() {
        return contentJson;
    }

    /**
     * 用于返回当前内容的json类内容
     *
     * @return 当前内容的json类内容
     */
    public String getContentJsonText() {
        return contentJson.toJSONString();
    }

    /**
     * 返回当前字段默认内容json类对象
     *
     * @return 字段默认内容json类对象
     */
    public JSONObject getDefaultCaseJson() {
        return defaultCaseJson;
    }

    /**
     * 返回当前字段默认内容json类内容
     *
     * @return 字段默认内容json类内容
     */
    public String getDefaultCaseJsonText() {
        return defaultCaseJson.toJSONString();
    }

    /**
     * 返回模板当前正在编写的内容json类对象
     *
     * @return 模板当前正在编写的内容json类对象
     */
    public JSONObject getCaseJson() {
        return caseJson;
    }

    /**
     * 返回模板当前正在编写的内容json类内容
     *
     * @return 模板当前正在编写的内容json类内容
     */
    public String getCaseJsonText() {
        return caseJson.toJSONString();
    }

    /**
     * 用于对当前写入的内容进行
     *
     * @param caseJson
     */
    public void setCaseJson(JSONObject caseJson) {
        caseJson = JSONObject.parseObject(caseJson.toJSONString());
    }

    /**
     * 用于清除当前的用例内容
     */
    public void clearCaseJson() {
        caseJson.clear();
    }

    /**
     * 返回当前已写入文件的行数
     *
     * @return 已写入文件的行数
     */
    public int getNowCaseNum() {
        return nowCaseNum;
    }

    /**
     * 用于返回当前模板的名称
     *
     * @return 模板名称
     */
    public String getTempName() {
        return tempName;
    }

    /**
     * 用于返回当前模板中需要替换的词语集合
     *
     * @return 替换的词语集合
     */
    public HashMap<String, DataFunction> getReplaceWordMap() {
        return replaceWordMap;
    }

    /**
     * 该方法用于返回当前是否存在需要写入的内容
     *
     * @return 是否存在需要写入的内容
     * @since autest 3.1.0
     */
    public boolean isContentEmpty() {
        return contentJson.getJSONArray(WriteTempletFile.KEY_CASE).isEmpty();
    }

    /**
     * 该方法用于判断当前准备插入的内容中是否包含指定的字段id
     *
     * @param field 字段id
     * @return 准备插入的内容是否包含指定的字段id
     * @since autest 3.1.0
     */
    public boolean caseContainsField(String field) {
        return caseJson.containsKey(field);
    }
}
