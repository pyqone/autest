package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.auxiliary.testcase.file.IncorrectFileException;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：</b>Case.java
 * </p>
 * <p>
 * <b>用途：</b>定义测试用例模板类能返回的基本字段，提供其相应的get与set方法，但该方法不允许包外调用
 * </p>
 * <p>
 * <b>编码时间：</b>2020年3月3日下午8:07:23
 * </p>
 * <p>
 * <b>修改时间：</b>2020年3月4日 07:39:23
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
 * @deprecated 用例生成类已重构，可参考{@link AbstractCaseTemplet}及其相应的子类，原始用例生成类将于4.1.0或后续版本中删除
 */
@Deprecated
public abstract class Case {
    /**
     * 用于指向用例标签中的name属性
     */
    public static final String ATTRIBUTE_NAME = "name";
    /**
     * 用于指向用例标签中的value属性
     */
    public static final String ATTRIBUTE_VALUE = "value";
    /**
     * 用于指向用例标签中的id属性
     */
    public static final String ATTRIBUTE_ID = "id";

    /**
     * 用于标记获取标签下所有的文本
     */
    protected final String ALL = "-1:getAllText";

    /**
     * 用于存储需要替换的词语的开始标记
     */
    protected final String START_SIGN = "*{";
    /**
     * 用于存储需要替换的词语的结束标记
     */
    protected final String END_SIGN = "}*";

    /**
     * 用于存储传入到正则表达式中的开始标记
     */
    protected final String START_SIGN_REGIX = "\\*\\{";

    /**
     * 用于指向测试用例xml文件的Document对象
     */
    protected Document configXml;

    /**
     * 存储xml文件中其需要替换的词语
     */
    protected HashMap<String, String> wordMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 存储字段的文本内容
     */
    protected HashMap<String, ArrayList<String>> fieldTextMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 根据用例xml文件来构造Case类
     *
     * @param configXmlFile xml配置文件
     * @throws IncorrectFileException 文件格式或路径不正确时抛出的异常
     */
    public Case(File configXmlFile) {
        // 判断传入的configurationFile是否为一个文件类对象，若非文件类对象，则抛出异常
        try {
            configXml = new SAXReader().read(configXmlFile);
        } catch (DocumentException e) {
            throw new IncorrectFileException("用例xml文件有误");
        }

        // 查找并存储替换的词语
        saveWord();
        // 保存字段的词语
        saveField();
    }

    /**
     * 用于设置需要替换的词语
     *
     * @param word 测试用例xml库中需要替换的词语
     * @param text 被替换的词语
     */
    public void setReplaceWord(String word, String text) {
        // 判断该词语是否存在于textMap中，若不存在，则抛出异常
        if (!wordMap.containsKey(word)) {
            throw new IncorrectFileException("未找到需要替换的词语：" + word);
        }
        // 存储替换的词语
        wordMap.put(word, text);
    }

    /**
     * 返回字段内容
     *
     * @return 字段内容
     */
    public HashMap<String, ArrayList<String>> getFieldTextMap() {
        return fieldTextMap;
    }

    /**
     * 用于替换文本中需要替换的单词，返回替换后的文本
     *
     * @param text 需要替换的文本
     * @return 替换后的文本
     */
    protected String replaceText(String text) {
        StringBuilder sb = new StringBuilder(text);
        // 存储替换符的位置
        int index = 0;
        // 循环，替换content中所有需要替换的信息
        while ((index = sb.indexOf(START_SIGN)) != -1) {
            // 存储待替换的变量名
            String var = "";
            try {
                var = sb.substring(index + START_SIGN.length(), sb.indexOf(END_SIGN));
            } catch (StringIndexOutOfBoundsException e) {
                throw new CaseContentException("词语替换错误，无效的标记字符：" + text);
            }
            // 替换该变量名
            sb.replace(index, sb.indexOf(END_SIGN) + END_SIGN.length(), wordMap.get(var));
        }

        return sb.toString();
    }

    /**
     * 用于获取用例xml中对应用例的标签内的文本，并返回替换词语后的文本
     *
     * @param caseName  用例名称
     * @param labelType 标签枚举{@link LabelType}
     * @param id        对应标签的id属性
     * @return 标签中存储的文本，并进行处理
     */
    protected String getLabelText(String caseName, LabelType labelType, String id) {
        // 返回处理替换的单词后相应的文本
        return getLabelText(caseName, labelType.getName(), id);
    }

    /**
     * 用于获取用例xml中对应用例的标签内的文本，并返回替换词语后的文本
     *
     * @param caseName  用例名称
     * @param labelName 标签名称
     * @param id        对应标签的id属性
     * @return 标签中存储的文本，并进行处理
     */
    protected String getLabelText(String caseName, String labelName, String id) {
        // 拼接xpath，规则"//case[@name='caseName']//标签名称[@id='id']"
        String xpath = "//" + LabelType.CASE.getName() + "[@" + ATTRIBUTE_NAME + "='" + caseName + "']//" + labelName
                + "[@" + ATTRIBUTE_ID + "='" + id + "']";

        // 获取相应的文本内容
        Element textElement = (Element) (configXml.selectSingleNode(xpath));
        // 判断获取的内容是否为空，为空则跑出异常

        // 判断集合是否存在元素，若不存在元素，则抛出异常
        if (textElement == null) {
            throw new LabelNotFoundException("用例集“" + caseName + "”中不存在id为“" + id + "”的“" + labelName + "”标签");
        }

        // 返回处理替换的单词后相应的文本
        return replaceText(textElement.attributeValue(ATTRIBUTE_VALUE));

    }

    /**
     * 用于获取用例xml中对应用例的标签内所有的文本，并返回替换词语后的文本
     *
     * @param caseName  用例名称
     * @param labelType 标签枚举
     * @return 标签中存储的文本，并进行处理
     */
    protected ArrayList<String> getAllLabelText(String caseName, LabelType labelType) {
        // 拼接xpath，规则"//case[@name='caseName']//标签名称[@id='id']"
        String xpath = "//" + LabelType.CASE.getName() + "[@" + ATTRIBUTE_NAME + "='" + caseName + "']//"
                + labelType.getName();


        // 存储节点中的value属性内的文本
        ArrayList<String> texts = new ArrayList<>();
        // 获取所有的节点
        configXml.selectNodes(xpath).stream().map(e -> (Element) e).map(e -> e.attributeValue(ATTRIBUTE_VALUE))
                .map(this::replaceText).forEach(texts::add);
        ;

        return texts;
    }

    /**
     * 用于获取并存储需要替换的词语
     */
    private void saveWord() {
        // 获取xml中包含value的元素，并将其中包含需要替换的词语存储至wordMap
        configXml.selectNodes("//*[@" + ATTRIBUTE_VALUE + "]").stream().map(e -> (Element) e)
                // 获取元素的value属性，将其转换为文本对象
                .map(e -> e.attributeValue(ATTRIBUTE_VALUE))
                // 筛选包含*{的文本
                .filter(e -> e.indexOf(START_SIGN) > -1).forEach(e -> {
                    // 对文本按照*{切割，并筛选包含}*的文本
                    Arrays.asList(e.split(START_SIGN_REGIX)).stream().filter(s -> s.indexOf(END_SIGN) > -1)
                            .forEach(s -> {
                                // 将需要存储的替换词语存入textMap中
                                wordMap.put(s.substring(0, s.indexOf(END_SIGN)), "");
                            });
                });
    }

    /**
     * 用于保存xml文件中的字段
     */
    protected void saveField() {
        // 获取case标签下所有的标签，存储至fieldTextMap，以初始化所有的字段名称
        configXml.getRootElement().elements("case").forEach(caseElement -> {
            caseElement.elements().forEach(labelElement -> {
                // 去掉末尾的s
                String name = labelElement.getName();
                fieldTextMap.put(name.substring(0, name.length() - 1), new ArrayList<String>());
            });
        });
    }

    /**
     * 用于添加一行文本
     *
     * @param labelType 标签名称（枚举）
     * @param text      相应内容
     */
    protected void addFieldText(LabelType labelType, String text) {
        fieldTextMap.get(labelType.getName()).add(text);
    }

    /**
     * 用于添加多行文本
     *
     * @param labelName 标签名称
     * @param texts     相应内容
     */
    protected void addFieldText(String labelName, List<String> texts) {
        fieldTextMap.get(labelName).addAll(texts);
    }

    /**
     * 用于添加一行文本
     *
     * @param labelName 标签名称
     * @param text      相应内容
     */
    protected void addFieldText(String labelName, String text) {
        fieldTextMap.get(labelName).add(text);
    }

    /**
     * 用于添加多行文本
     *
     * @param label 标签名称（枚举）
     * @param texts 相应内容
     */
    protected void addFieldText(LabelType label, List<String> texts) {
        fieldTextMap.get(label.getName()).addAll(texts);
    }

    /**
     * 用于清空字段的内容，以避免存储上一次输入的用例
     */
    protected void clearFieldText() {
        fieldTextMap.forEach((key, value) -> {
            fieldTextMap.get(key).clear();
        });
    }

    /**
     * 由于添加与参数相关的数据时需要将关联的字段（如步骤及结果）都添加至其中， 若后期关联字段增加，则代码量将是成倍的增加，故将关联的内容提取出来，在
     * 外部进行添加，之后修改关联字段时只需修改该方法即可。若传入-1，则表示 获取xml中该标签下的所有的信息<br>
     * 参数表：
     * <ol>
     * <li>步骤</li>
     * <li>预期</li>
     * </ol>
     *
     * @param caseName 读取的用例名称
     * @param ids      id参数串
     */
    protected void relevanceAddData(String caseName, String... ids) {
        // 添加步骤
        if (ids[0].equals(ALL)) {
            addFieldText(LabelType.STEP, getAllLabelText(caseName, LabelType.STEP));
        } else {
            addFieldText(LabelType.STEP, getLabelText(caseName, LabelType.STEP, ids[0]));
        }

        // 添加预期
        if (ids[1].equals(ALL)) {
            addFieldText(LabelType.EXPECT, getAllLabelText(caseName, LabelType.EXPECT));
        } else {
            addFieldText(LabelType.EXPECT, getLabelText(caseName, LabelType.EXPECT, ids[1]));
        }
    }
}
