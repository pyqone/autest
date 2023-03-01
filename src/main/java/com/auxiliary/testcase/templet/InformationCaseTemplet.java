package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.auxiliary.testcase.TestCaseException;
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
 * @since autest 4.0.0
 */
public class InformationCaseTemplet extends AbstractPresetCaseTemplet {
    /**
     * 构造对象，并指定读取的模板xml文件
     * 
     * @param xmlTempletFile 用例模板文件类对象
     */
    public InformationCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    /**
     * 构造对象，并读取默认的模板xml文件
     */
    public InformationCaseTemplet() {
        try {
            configXml = new SAXReader().read(InformationCaseTemplet.class.getClassLoader()
                    .getResourceAsStream(DEFAULT_TEMPLET_FOLDER + "AddInformation.xml"));
        } catch (DocumentException e) {
            throw new TestCaseException("jar包异常，无法读取模板xml文件", e);
        }
    }

    public List<CaseData> addWholeInformationCase() {
        Map<LabelType, List<Entry<String, String[]>>> allContentMap = new HashMap<>(
                ConstType.DEFAULT_MAP_SIZE);
        allContentMap.put(LabelType.TITLE,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" })));
        allContentMap.put(LabelType.STEP,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_TEXTBOX_BASIC_CASE,
                        new String[] { "1", "2", "3", "4", "5", "6", "7", "8" })));
        allContentMap.put(LabelType.EXCEPT,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT,
                        new String[] { "1", "2", "3", "4", "1", "2", "3", "4" })));
        allContentMap.put(LabelType.RANK,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" })));
        allContentMap.put(LabelType.KEY,
                Arrays.asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1" })));
        allContentMap.put(LabelType.PRECONDITION, Arrays
                .asList(new Entry<>(InformationTempletCaseName.GROUP_COMMON_CONTENT, new String[] { "1", "2", "3" })));

        addReplaceWord("保存按钮名称", "保存个人信息");
        addReplaceWord("成功预期前文", "这是一段成功的预期前文呀！");

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
