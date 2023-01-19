package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dom4j.Element;

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
     * 定义包内用例模板文件所在的路径
     */
    protected final static String DEFAULT_TEMPLET_FOLDER = "com/auxiliary/testcase/templet/";

    /**
     * 根标签（cases）
     */
    protected final String LABEL_CASES = "cases";
    /**
     * 用例组（group）标签
     */
    protected final String LABEL_GROUP = "group";
    /**
     * 优先级（rank）标签
     */
    protected final String LABEL_RANK = "rank";
    /**
     * 关键词（key）标签
     */
    protected final String LABEL_KEY = "key";
    /**
     * 前置条件（precondition）标签
     */
    protected final String LABEL_PRECONDITION = "precondition";
    /**
     * 标题（title）标签
     */
    protected final String LABEL_TITLE = "title";
    /**
     * 步骤标（step）签
     */
    protected final String LABEL_STEP = "step";
    /**
     * 详细步骤（stepdetail）标签
     */
    protected final String LABEL_STEPDETAIL = "stepdetail";
    /**
     * 预期（except）标签
     */
    protected final String LABEL_EXCEPT = "except";
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

    /**
     * 该方法用于返回用例组中的指定id的标题
     * 
     * @param groupName 用例组名称
     * @param id        用例id
     * @return 指定的标题内容集合
     * @since autest 4.0.0
     */
    public List<String> title(String groupName, String id) {
        return getAttributeValue(getXpathFormat(groupName, LABEL_TITLE, id, ""), ATT_VALUE);
    }

    /**
     * 该方法用于根据指定的xptah内容，查找并返回元素指定属性下的内容集合
     * 
     * @param xptah     查找元素的xpath
     * @param attribute 元素对应的属性
     * @return 元素指定属性的内容集合
     * @since autest 4.0.0
     */
    protected List<String> getAttributeValue(String xptah, String attribute) {
        // 根据指定的xptah查找元素，并返回相应元素的属性内容
        return Optional.ofNullable(xptah).map(configXml::selectNodes).map(nodeList -> {
            return nodeList.stream().map(node -> (Element) node)
                    .map(element -> Optional.ofNullable(element.attributeValue(attribute)).orElse(""))
                    .collect(Collectors.toList());
        }).orElseGet(() -> new ArrayList<>());
    }

    /**
     * 该方法用于生成查找元素的xpath信息
     * 
     * @param groupName  用例组名称
     * @param labelName  标签名称
     * @param id         标签id
     * @param lowerLabel 下级元素标签
     * @return 格式化后的xpath
     * @since autest 4.0.0
     */
    protected String getXpathFormat(String groupName, String labelName, String id, String lowerLabel) {
        return String.format("//%s[@%s='%s']/%s[@id='%s']%s", LABEL_GROUP, ATT_NAME, groupName, labelName, id,
                (lowerLabel.isEmpty() ? "" : ("/" + lowerLabel)));
    }
}
