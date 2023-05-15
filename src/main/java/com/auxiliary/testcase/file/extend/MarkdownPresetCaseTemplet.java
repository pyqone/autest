package com.auxiliary.testcase.file.extend;

import java.io.File;

import com.auxiliary.testcase.file.WriteMarkdownTestCase;
import com.auxiliary.testcase.templet.AbstractPresetCaseTemplet;
import com.auxiliary.testcase.templet.LabelType;
import com.auxiliary.tool.date.Time;
import com.auxiliary.tool.file.FileTemplet;

/**
 * <p>
 * <b>文件名：MarkdownPresetCaseTemplet.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：2023年5月4日 下午5:06:26
 * </p>
 * <p>
 * <b>修改时间：2023年5月4日 下午5:06:26
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public abstract class MarkdownPresetCaseTemplet<T extends WriteMarkdownTestCase<T>> extends WriteMarkdownTestCase<T> {
    /**
     * 节点内容样式
     */
    protected final String CONTENT_FORMAT = "%s：%s";

    /**
     * 模板中的name属性
     */
    protected static final String TEMPLET_ATTRI_NAME = "name";

    /**
     * 构造用例写入类，使用默认模板
     * <p>
     * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
     * </p>
     */
    public MarkdownPresetCaseTemplet() {
        super(MarkdownPresetCaseTemplet.getMarkdownTemplet());
    }

    /**
     * 构造用例写入类，使用默认模板并重新设置文件保存路径
     * <p>
     * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
     * </p>
     * 
     * @param saveFile 保存路径文件类对象
     */
    public MarkdownPresetCaseTemplet(File saveFile) {
        this();
        // 重新设置保存路径
        data.getTemplet().setSaveFile(saveFile);
    }

    /**
     * 构造用例写入类，并重新设置模板
     * 
     * @param templet 模板类对象
     */
    public MarkdownPresetCaseTemplet(FileTemplet templet) {
        super(templet);
        initField();
    }

    @Override
    protected void initField() {
        // 关联用例字段
        relevanceCase(LabelType.STEP.getName(), MarkdownPresetFieldType.FIELD_STEP);
        relevanceCase(LabelType.EXPECT.getName(), MarkdownPresetFieldType.FIELD_EXPECT);
        relevanceCase(LabelType.TITLE.getName(), MarkdownPresetFieldType.FIELD_TITLE);
        relevanceCase(LabelType.PRECONDITION.getName(), MarkdownPresetFieldType.FIELD_PRECONDITION);
        relevanceCase(LabelType.RANK.getName(), MarkdownPresetFieldType.FIELD_RANK);
        relevanceCase(LabelType.KEY.getName(), MarkdownPresetFieldType.FIELD_KEY);

        // 关联关键词替换字段
        setReplactWord(AbstractPresetCaseTemplet.RANK_1, MarkdownPresetFieldType.RANK_1);
        setReplactWord(AbstractPresetCaseTemplet.RANK_2, MarkdownPresetFieldType.RANK_2);
        setReplactWord(AbstractPresetCaseTemplet.RANK_3, MarkdownPresetFieldType.RANK_3);
        setReplactWord(AbstractPresetCaseTemplet.RANK_4, MarkdownPresetFieldType.RANK_4);
    }

    /**
     * 该方法用于生成默认的模板类对象
     * 
     * @return 默认模板类对象
     * @since autest 4.2.0
     */
    public static FileTemplet getMarkdownTemplet() {
        FileTemplet templet = new FileTemplet(
                new File(String.format("result/Markdown测试用例-%d.md", Time.parse().getMilliSecond())));

        templet.addField(MarkdownPresetFieldType.FIELD_CREATE_DATE);
        templet.addField(MarkdownPresetFieldType.FIELD_CREATE_PERSON);
        templet.addField(MarkdownPresetFieldType.FIELD_EXPECT);
        templet.addField(MarkdownPresetFieldType.FIELD_KEY);
        templet.addField(MarkdownPresetFieldType.FIELD_MODULE);
        templet.addField(MarkdownPresetFieldType.FIELD_STEP);
        templet.addField(MarkdownPresetFieldType.FIELD_TITLE);
        templet.addField(MarkdownPresetFieldType.FIELD_RANK);

        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_CREATE_DATE, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_CREATE_DATE);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_CREATE_PERSON, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_CREATE_PERSON);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_EXPECT, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_EXPECT);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_KEY, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_KEY);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_MODULE, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_MODULE);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_STEP, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_STEP);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_TITLE, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_TITLE);
        templet.addFieldAttribute(MarkdownPresetFieldType.FIELD_RANK, TEMPLET_ATTRI_NAME,
                MarkdownPresetFieldType.TITLE_RANK);

        return templet;
    }
}
