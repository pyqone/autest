package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

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
public class InformationCaseTemplet<T extends InformationCaseTemplet<T>> extends AbstractPresetCaseTemplet<T> {
    /**
     * 构造对象，并设置模板所在的位置
     * 
     * @param xmlTempletFile 用例模板文件类对象
     */
    public InformationCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    /**
     * 构造对象，设置包内用例模板所在的默认位置
     */
    public InformationCaseTemplet() {
        super(new File(InformationCase.class.getClassLoader()
                .getResource(DEFAULT_TEMPLET_FOLDER + "AddInformation.xml").getFile()));
    }

    public List<CaseData> addWholeInformationCase() {
        Map<PresetCaseTempletContentType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(
                ConstType.DEFAULT_MAP_SIZE);
        allContentMap.put(PresetCaseTempletContentType.TITLE,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" })));
        allContentMap.put(PresetCaseTempletContentType.STEP,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_TEXTBOX_BASIC_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6", "7", "8" })));
        allContentMap.put(PresetCaseTempletContentType.EXCEPT,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT,
                        new String[] { "1", "2", "3", "4", "1", "2", "3", "4" })));
        allContentMap.put(PresetCaseTempletContentType.RANK,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" })));
        allContentMap.put(PresetCaseTempletContentType.KEY,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" })));
        allContentMap.put(PresetCaseTempletContentType.PRECONDITION, Arrays
                .asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1", "2", "3" })));

        return createCaseDataList(this, allContentMap);
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
