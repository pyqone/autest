package com.auxiliary.testcase.templet;

/**
 * <p>
 * <b>文件名：PresetTempletContentType.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：2023年2月1日 上午11:08:06
 * </p>
 * <p>
 * <b>修改时间：2023年2月1日 上午11:08:06
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 
 */
public enum PresetCaseTempletContentType {
    /**
     * 标题
     */
    TITLE("title"),
    /**
     * 步骤
     */
    STEP("step"),
    /**
     * 预期
     */
    EXCEPT("except"),
    /**
     * 优先级
     */
    RANK("rank"),
    /**
     * 关键词
     */
    KEY("key"),
    /**
     * 前置条件
     */
    PRECONDITION("precondition");

    /**
     * 枚举表示的内容对应的写入字段
     */
    private String fieldName;

    /**
     * 初始化枚举对应的字段
     * 
     * @param fieldName 写入文件中的字段
     */
    private PresetCaseTempletContentType(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 该方法用于返回枚举对应的内容在用例信息类对象中的字段
     * 
     * @return 枚举对应的内容在用例信息类对象中的字段
     * @since autest 4.0.0
     */
    public String getFieldName() {
        return fieldName;
    }
}
