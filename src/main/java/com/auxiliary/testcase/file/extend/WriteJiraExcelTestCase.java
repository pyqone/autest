package com.auxiliary.testcase.file.extend;

import java.io.File;
import java.util.Arrays;

import org.dom4j.Document;

import com.auxiliary.testcase.file.WriteExcelTestCase;
import com.auxiliary.testcase.templet.AbstractPresetCaseTemplet;
import com.auxiliary.testcase.templet.LabelType;
import com.auxiliary.tool.common.enums.OrderedListSignType;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.excel.ExcelFileTemplet;

/**
 * <p>
 * <b>文件名：</b>WriteJiraExcelTestCase.java
 * </p>
 * <p>
 * <b>用途：</b> 用于对上传jira测试用例模板，通过该类构造的用例文件，在使用测试用例
 * 模板类写入用例时可以不用指定相应的字段关系。该类中包含部分个性的方法，以方便编写 测试用例
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月19日下午3:30:43
 * </p>
 * <p>
 * <b>修改时间：</b>2023年2月15日 上午8:23:26
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.4.0
 */
public class WriteJiraExcelTestCase extends WriteExcelTestCase<WriteJiraExcelTestCase> {
    public static final String DEFAULT_CASE_NAME = "测试用例";

    /**
     * 用例标题目的
     * 
     * @deprecated 该属性已由{@link JiraPresetFieldType#FIELD_OBJECTIVE}属性代替，将在4.1.0或后续版本中删除
     */
    @Deprecated
    public static final String CASE_OBJECTIVE = "objective";
    /**
     * 用例标题状态
     * 
     * @deprecated 该属性已由{@link JiraPresetFieldType#FIELD_STATUS}属性代替，将在4.1.0或后续版本中删除
     */
    @Deprecated
    public static final String CASE_STATUS = "status";
    /**
     * 用例标题项目
     * 
     * @deprecated 该属性已由{@link JiraPresetFieldType#FIELD_COMPONENT}属性代替，将在4.1.0或后续版本中删除
     */
    @Deprecated
    public static final String CASE_COMPONENT = "component";
    /**
     * 用例标题设计者
     * 
     * @deprecated 该属性已由{@link JiraPresetFieldType#FIELD_OWNER}属性代替，将在4.1.0或后续版本中删除
     */
    @Deprecated
    public static final String CASE_OWNER = "owner";
    /**
     * 用例标题关联需求
     * 
     * @deprecated 该属性已由{@link JiraPresetFieldType#FIELD_ISSUES}属性代替，将在4.1.0或后续版本中删除
     */
    @Deprecated
    public static final String CASE_ISSUES = "issues";

    /**
     * 通过模板配置xml文件对文件写入类进行构造
     * <p>
     * 通过该方法构造的写入类为包含模板的写入类，可直接按照字段编写文件内容
     * </p>
     * 
     * @param templetXml 模板配置文件
     * @param saveFile   文件保存路径
     * @since autest 2.4.0
     */
    public WriteJiraExcelTestCase(Document templetXml, File saveFile) {
        super(templetXml, saveFile);
    }

    /**
     * 构造用例写入类，使用默认模板
     * <p>
     * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
     * </p>
     * 
     * @since autest 2.4.0
     */
    public WriteJiraExcelTestCase() {
        super(DEFAULT_CASE_NAME, WriteJiraExcelTestCase.getJiraCaseExcelTemplet());
    }

    /**
     * 构造用例写入类，使用默认模板并重新设置文件保存路径
     * <p>
     * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
     * </p>
     * 
     * @param saveFile 保存路径文件类对象
     * @since autest 2.4.0
     */
    public WriteJiraExcelTestCase(File saveFile) {
        this();
        // 重新设置保存路径
        dataMap.get(DEFAULT_CASE_NAME).getTemplet().setSaveFile(saveFile);
    }

    /**
     * 构造用例写入类，并重新设置模板
     * 
     * @param templet 模板类对象
     * @since autest 2.4.0
     */
    public WriteJiraExcelTestCase(FileTemplet templet) {
        this();
        // 重新设置模板
        addTemplet(DEFAULT_CASE_NAME, templet);
    }

    @Override
    protected void initField() {
        // 关联用例字段
        relevanceCase(LabelType.STEP.getName(), JiraPresetFieldType.FIELD_STEP);
        relevanceCase(LabelType.EXPECT.getName(), JiraPresetFieldType.FIELD_EXCEPT);
        relevanceCase(LabelType.TITLE.getName(), JiraPresetFieldType.FIELD_TITLE);
        relevanceCase(LabelType.PRECONDITION.getName(), JiraPresetFieldType.FIELD_PRECONDITION);
        relevanceCase(LabelType.RANK.getName(), JiraPresetFieldType.FIELD_RANK);

        // 关联关键词替换字段
        setReplactWord(AbstractPresetCaseTemplet.RANK_1, JiraPresetFieldType.RANK_HIGH);
        setReplactWord(AbstractPresetCaseTemplet.RANK_2, JiraPresetFieldType.RANK_NORMAL);
        setReplactWord(AbstractPresetCaseTemplet.RANK_3, JiraPresetFieldType.RANK_LOW);
        setReplactWord(AbstractPresetCaseTemplet.RANK_4, JiraPresetFieldType.RANK_LOW);
    }

    /**
     * 用于添加测试用例目的
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     * 
     * @param objective 用例目的
     * @return 类本身
     * @since autest 2.4.0
     * @deprecated 该方法已无意义，为保证模板可扩展性，可通过调用
     *             <code>addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_OBJECTIVE), objective)</code>方法进行代替
     */
    @Deprecated
    public WriteJiraExcelTestCase addObjective(String objective) {
        addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_OBJECTIVE), objective);
        return this;
    }

    /**
     * 用于添加测试用例状态
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     * 
     * @param status 用例状态文本
     * @return 类本身
     * @since autest 2.4.0
     * @deprecated 该方法已无意义，为保证模板可扩展性，可通过调用
     *             <code>addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_STATUS), status)</code>方法进行代替
     */
    @Deprecated
    public WriteJiraExcelTestCase addStatus(String status) {
        addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_STATUS), status);
        return this;
    }

    /**
     * 用于添加测试用例模块
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     * 
     * @param component 用例模块文本
     * @return 类本身
     * @since autest 2.4.0
     * @deprecated 该方法已无意义，为保证模板可扩展性，可通过调用
     *             <code>addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_COMPONENT), component)</code>方法进行代替
     */
    @Deprecated
    public WriteJiraExcelTestCase addComponent(String component) {
        addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_COMPONENT), component);
        return this;
    }

    /**
     * 用于添加测试用例关联需求
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     * 
     * @param issues 用例关联需求文本
     * @return 类本身
     * @since autest 2.4.0
     * @deprecated 该方法已无意义，为保证模板可扩展性，可通过调用
     *             <code>addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_ISSUES), issues)</code>方法进行代替
     */
    @Deprecated
    public WriteJiraExcelTestCase addIssues(String issues) {
        addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_ISSUES), issues);
        return this;
    }

    /**
     * 用于添加测试用例设计者
     * <p>
     * <b>注意：</b>多次调用该方法时，会覆盖前一次写入的内容
     * </p>
     * 
     * @param owner 用例设计者
     * @return 类本身
     * @since autest 2.4.0
     * @deprecated 该方法已无意义，为保证模板可扩展性，可通过调用
     *             <code>addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_OWNER), owner)</code>方法进行代替
     */
    @Deprecated
    public WriteJiraExcelTestCase addOwner(String owner) {
        addContent(caseFieldMap.get(JiraPresetFieldType.FIELD_OWNER), owner);
        return this;
    }

    /**
     * 用于生成默认的jira测试用例模板类对象
     * 
     * @return jira测试用例模板类对象
     * @since autest 2.4.0
     */
    public static ExcelFileTemplet getJiraCaseExcelTemplet() {
        ExcelFileTemplet jiraTemplet = new ExcelFileTemplet(new File("result/测试用例.xlsx"));

        // 设置模板全局内容
        jiraTemplet.setFreeze(1, 3).setFiltrate(true);

        // 设置模板字段内容
        jiraTemplet.addField(JiraPresetFieldType.FIELD_TITLE);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_TITLE, JiraPresetFieldType.TITLE_TITLE);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_OBJECTIVE);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_OBJECTIVE, JiraPresetFieldType.TITLE_OBJECTIVE);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_PRECONDITION);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_PRECONDITION, JiraPresetFieldType.TITLE_PRECONDITION);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_STEP);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_STEP, JiraPresetFieldType.TITLE_STEP);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_EXCEPT);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_EXCEPT, JiraPresetFieldType.TITLE_EXCEPT);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_MODULE);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_MODULE, JiraPresetFieldType.TITLE_MODULE);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_STATUS);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_STATUS, JiraPresetFieldType.TITLE_STATUS);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_RANK);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_RANK, JiraPresetFieldType.TITLE_RANK);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_COMPONENT);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_COMPONENT, JiraPresetFieldType.TITLE_COMPONENT);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_OWNER);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_OWNER, JiraPresetFieldType.TITLE_OWNER);

        jiraTemplet.addField(JiraPresetFieldType.FIELD_ISSUES);
        jiraTemplet.addTitle(JiraPresetFieldType.FIELD_ISSUES, JiraPresetFieldType.TITLE_ISSUES);

        // 设置字段垂直居中对齐
        jiraTemplet.setAlignment(AlignmentType.VERTICAL_CENTER);
        // 设置字段左对齐
        jiraTemplet.setAlignment(AlignmentType.HORIZONTAL_LEFT, JiraPresetFieldType.FIELD_OBJECTIVE,
                JiraPresetFieldType.FIELD_PRECONDITION, JiraPresetFieldType.FIELD_STEP,
                JiraPresetFieldType.FIELD_EXCEPT, JiraPresetFieldType.FIELD_TITLE);
        // 设置字段居中水平对齐
        jiraTemplet.setAlignment(AlignmentType.HORIZONTAL_MIDDLE, JiraPresetFieldType.FIELD_MODULE,
                JiraPresetFieldType.FIELD_STATUS, JiraPresetFieldType.FIELD_RANK, JiraPresetFieldType.FIELD_COMPONENT,
                JiraPresetFieldType.FIELD_OWNER, JiraPresetFieldType.FIELD_ISSUES);

        // 设置字段宽度
        jiraTemplet.setWide(18.25, JiraPresetFieldType.FIELD_OBJECTIVE, JiraPresetFieldType.FIELD_PRECONDITION);
        jiraTemplet.setWide(45.75, JiraPresetFieldType.FIELD_STEP, JiraPresetFieldType.FIELD_EXCEPT);
        jiraTemplet.setWide(20, JiraPresetFieldType.FIELD_MODULE, JiraPresetFieldType.FIELD_ISSUES);
        jiraTemplet.setWide(10, JiraPresetFieldType.FIELD_STATUS, JiraPresetFieldType.FIELD_RANK,
                JiraPresetFieldType.FIELD_COMPONENT, JiraPresetFieldType.FIELD_OWNER);
        jiraTemplet.setWide(10, JiraPresetFieldType.FIELD_STATUS, JiraPresetFieldType.FIELD_RANK,
                JiraPresetFieldType.FIELD_COMPONENT, JiraPresetFieldType.FIELD_OWNER);
        jiraTemplet.setWide(30.88, JiraPresetFieldType.FIELD_TITLE);

        // 设置需要编号的字段
        jiraTemplet.setOrderedListSign(OrderedListSignType.ARABIC_NUM, JiraPresetFieldType.FIELD_OBJECTIVE,
                JiraPresetFieldType.FIELD_PRECONDITION, JiraPresetFieldType.FIELD_STEP,
                JiraPresetFieldType.FIELD_EXCEPT);
        // 设置需要换行的字段
        jiraTemplet.setContentBranch(1, JiraPresetFieldType.FIELD_STEP, JiraPresetFieldType.FIELD_EXCEPT);

        // 添加数据有效性
        jiraTemplet.addDataOption(JiraPresetFieldType.FIELD_RANK, Arrays.asList(JiraPresetFieldType.RANK_HIGH,
                JiraPresetFieldType.RANK_NORMAL, JiraPresetFieldType.RANK_LOW));
        jiraTemplet.addDataOption(JiraPresetFieldType.FIELD_STATUS, Arrays.asList(JiraPresetFieldType.STATUS_APPROVED,
                JiraPresetFieldType.STATUS_DRAFT, JiraPresetFieldType.STATUS_DEPRECATED));

        return jiraTemplet;
    }
}
