package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            CaseData caseData = new CaseData(this);

            // 读取标题信息
            caseData.addContent(PresetCaseDataField.KEY_TITLE, -1,
                    title(InformationTempletCaseName.GROUP_COMMON_CONTENT, "1"));
            // 读取
        }

        return caseDataList;
    }

    public class InformationTempletCaseName {
        public static final String GROUP_COMMON_CONTENT = "commonContent";
        public static final String GROUP_TEXTBOX_BASIC_CASE = "textboxBasicCase";
    }
}
