package com.auxiliary.testcase.templet;

import java.io.File;

/**
 * <p>
 * <b>文件名：PresetCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义预设模板中所包含的字段及方法
 * </p>
 * <p>
 * <b>编码时间：2023年1月18日 上午8:07:16
 * </p>
 * <p>
 * <b>修改时间：2023年1月18日 上午8:07:16
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public abstract class PresetCaseTemplet<T extends PresetCaseTemplet<T>> extends XmlCaseTemplet<T>
        implements StepDetailTemplet {
    /**
     * 根标签（cases）
     */
    protected final String KEY_CASES = "cases";
    /**
     * 用例组（group）标签
     */
    protected final String KEY_GROUP = "group";
    /**
     * 优先级（rank）标签
     */
    protected final String KEY_RANK = "rank";
    /**
     * 关键词（key）标签
     */
    protected final String KEY_KEY = "key";
    /**
     * 前置条件（precondition）标签
     */
    protected final String KEY_PRECONDITION = "precondition";
    /**
     * 标题（title）标签
     */
    protected final String KEY_TITLE = "title";
    /**
     * 步骤标（step）签
     */
    protected final String KEY_STEP = "step";
    /**
     * 详细步骤（stepdetail）标签
     */
    protected final String KEY_STEPDETAIL = "stepdetail";
    /**
     * 预期（except）标签
     */
    protected final String KEY_EXCEPT = "except";
    /**
     * 名称（name）属性
     */
    protected final String ATT_NAME = "name";
    /**
     * 标识（id）属性
     */
    protected final String ATT_ID = "id";
    /**
     * 值（value）属性
     */
    protected final String ATT_VALUE = "value";
    /**
     * 测试意图（intention）属性
     */
    protected final String ATT_INTENTION = "intention";
    /**
     * 步骤（step）属性
     */
    protected final String ATT_STEP = "step";

    /**
     * 存储是否读取详细的步骤
     */
    boolean isStepDetail = false;
    /**
     * 存储每条步骤是否作为独立的用例
     */
    boolean isStepIndependentCase = false;

    /**
     * 构造对象，读取xml文件模板类
     * 
     * @param xmlTempletFile xml模板文件类对象
     */
    public PresetCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    @Override
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase) {
        this.isStepDetail = isStepDetail;
        this.isStepIndependentCase = isStepIndependentCase;
    }
}
