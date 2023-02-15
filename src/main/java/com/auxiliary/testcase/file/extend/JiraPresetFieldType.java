package com.auxiliary.testcase.file.extend;

import com.auxiliary.testcase.templet.LabelType;

/**
 * <p>
 * <b>文件名：PresetJiraTempletField.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：2023年2月14日 上午8:22:50
 * </p>
 * <p>
 * <b>修改时间：2023年2月14日 上午8:22:50
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.0.0
 */
public class JiraPresetFieldType {
    /**
     * 模板目的字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_OBJECTIVE = "objective";
    /**
     * 模板状态字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_STATUS = "status";
    /**
     * 模板项目字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_COMPONENT = "component";
    /**
     * 模板设计者字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_OWNER = "owner";
    /**
     * 模板关联需求字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_ISSUES = "issues";
    /**
     * 模板用例标题字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_TITLE = LabelType.TITLE.getName();
    /**
     * 用例前置条件字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_PRECONDITION = LabelType.PRECONDITION.getName();
    /**
     * 模板步骤字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_STEP = LabelType.STEP.getName();
    /**
     * 模板预期字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_EXCEPT = LabelType.EXCEPT.getName();
    /**
     * 模板模块字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_MODULE = "module";
    /**
     * 模板优先级字段
     * 
     * @since autest 4.0.0
     */
    public static final String FIELD_RANK = LabelType.RANK.getName();

    /**
     * 模板用例标题表头名称
     */
    public static final String TITLE_TITLE = "Name";
    /**
     * 模板用例目的表头名称
     */
    public static final String TITLE_OBJECTIVE = "Objective";
    /**
     * 模板前置条件表头名称
     */
    public static final String TITLE_PRECONDITION = "Precondition";
    /**
     * 模板步骤表头名称
     */
    public static final String TITLE_STEP = "Test Script (Step-by-Step) - Step";
    /**
     * 模板预期表头名称
     */
    public static final String TITLE_EXCEPT = "Test Script (Step-by-Step) - Expected Result";
    /**
     * 模板模块表头名称
     */
    public static final String TITLE_MODULE = "Folder";
    /**
     * 模板用例状态表头名称
     */
    public static final String TITLE_STATUS = "Status";
    /**
     * 模板优先级表头名称
     */
    public static final String TITLE_RANK = "Priority";
    /**
     * 模板项目表头名称
     */
    public static final String TITLE_COMPONENT = "Component";
    /**
     * 模板设计者表头名称
     */
    public static final String TITLE_OWNER = "Owner";
    /**
     * 模板关联需求表头名称
     */
    public static final String TITLE_ISSUES = "Coverage (Issues)";

    /**
     * 高优先级内容
     */
    public static final String RANK_HIGH = "HIGH";
    /**
     * 普通优先级内容
     */
    public static final String RANK_NORMAL = "NORMAL";
    /**
     * 低优先级内容
     */
    public static final String RANK_LOW = "LOW";

    /**
     * 通过用例状态
     */
    public static final String STATUS_APPROVED = "APPROVED";
    /**
     * 草稿用例状态
     */
    public static final String STATUS_DRAFT = "DRAFT";
    /**
     * 拒绝用例状态
     */
    public static final String STATUS_DEPRECATED = "DEPRECATED";
}
