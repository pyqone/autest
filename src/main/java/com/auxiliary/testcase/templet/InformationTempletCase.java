package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.auxiliary.tool.common.Entry;

/**
 * <p>
 * <b>文件名：InformationTempletCase.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：2023年1月19日 上午9:56:13
 * </p>
 * <p>
 * <b>修改时间：2023年1月19日 上午9:56:13
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 
 */
public class InformationTempletCase<T extends InformationTempletCase<T>> extends PresetCaseTemplet<T> {
    /**
     * 构造对象，并设置模板所在的位置
     * 
     * @param xmlTempletFile 用例模板文件类对象
     */
    public InformationTempletCase(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    /**
     * 构造对象，设置包内用例模板所在的默认位置
     */
    public InformationTempletCase() {
        super(new File(InformationCase.class.getClassLoader()
                .getResource(DEFAULT_TEMPLET_FOLDER + "AddInformation.xml").getFile()));
    }

    public List<CaseData> addWholeInformationCase() {
        List<CaseData> caseDataList = new ArrayList<>();

        if (!isStepIndependentCase) {
            caseDataList.add(getCaseData(new CaseData(this), Arrays.asList(
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" }),
                    new Entry<>(InformationTempletCaseName.GROUP_TEXTBOX_BASIC_CASE,
                            new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT,
                            new String[] { "1", "2", "3", "4", "1", "2", "3", "4" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "2" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1", "2", "3" }))));
        } else {
            caseDataList.add(getCaseData(new CaseData(this), Arrays.asList(
                    new Entry<>(InformationTempletCaseName.GROUP_TEXTBOX_BASIC_CASE, new String[] { "1" }),
                    new Entry<>(InformationTempletCaseName.GROUP_TEXTBOX_BASIC_CASE, new String[] { "1" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "2" }),
                    new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1", "2", "3" }))));
        }

        return caseDataList;
    }

    /**
     * <p>
     * <b>文件名：InformationTempletCase.java</b>
     * </p>
     * <p>
     * <b>用途：</b>用于标记当前用例模板中存在的用例组名称
     * </p>
     * <p>
     * <b>编码时间：2023年1月31日 上午8:14:52
     * </p>
     * <p>
     * <b>修改时间：2023年1月31日 上午8:14:52
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.0.0
     */
    public class InformationTempletCaseName {
        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String GROUP_TEXTBOX_BASIC_CASE = "textboxBasicCase";
    }
}
