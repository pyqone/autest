package com.auxiliary.testcase.templet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dom4j.Element;

import com.auxiliary.tool.common.Entry;
import com.auxiliary.tool.regex.ConstType;

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
public abstract class AbstractPresetCaseTemplet extends AbstractXmlCaseTemplet implements StepDetailTemplet {
    /**
     * 1级优先级
     */
    public final static String RANK_1 = "rank-1";
    /**
     * 2级优先级
     */
    public final static String RANK_2 = "rank-2";
    /**
     * 3级优先级
     */
    public final static String RANK_3 = "rank-3";
    /**
     * 4级优先级
     */
    public final static String RANK_4 = "rank-4";

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
     * 优先级（rank）属性
     */
    protected final String ATT_RANK = "rank";

    /**
     * 存储是否读取详细步骤的内容
     */
    protected boolean isStepDetail = false;
    /**
     * 存储每条步骤是否作为独立的用例
     */
    protected boolean isStepIndependentCase = false;

    /**
     * 构造对象，读取xml文件模板类
     * 
     * @param xmlTempletFile xml模板文件类对象
     */
    public AbstractPresetCaseTemplet(File xmlTempletFile) {
        super(xmlTempletFile);
    }

    protected AbstractPresetCaseTemplet() {
    }

    @Override
    public void setReadStepDetail(boolean isStepDetail, boolean isStepIndependentCase) {
        this.isStepDetail = isStepDetail;
        this.isStepIndependentCase = isStepIndependentCase;
    }

    /**
     * 该方法用于返回用指定例组中的指定id对应的内容
     * 
     * @param LabelType 预设用例模板内容枚举
     * @param groupName 用例组名称
     * @param ids       用例id组
     * @return 获取到的内容集合
     * @since autest 4.0.0
     */
    public List<String> getTempletContent(LabelType LabelType, String groupName,
            String... ids) {
        // 根据相应的模板内容枚举，返回相应的内容
        switch (LabelType) {
        case RANK:
            if (isStepIndependentCase) {
                return getContentList(groupName, LABEL_STEP, "", ATT_RANK, ids);
            } else {
                return getContentList(groupName, LABEL_RANK, "", ATT_VALUE, ids);
            }
        case TITLE:
            if (isStepIndependentCase) {
                return getContentList(groupName, LABEL_STEP, "", ATT_INTENTION, ids);
            } else {
                return getContentList(groupName, LABEL_TITLE, "", ATT_VALUE, ids);
            }
        case STEP:
            return getContentList(groupName, LABEL_STEP, (isStepDetail ? LABEL_STEPDETAIL : ""), ATT_VALUE, ids);
        case STEPDETAIL:
            return getContentList(groupName, LABEL_STEP, LABEL_STEPDETAIL, ATT_VALUE, ids);
        case EXCEPT:
            return getContentList(groupName, LABEL_EXCEPT, "", ATT_VALUE, ids);
        case KEY:
            return getContentList(groupName, LABEL_KEY, "", ATT_VALUE, ids);
        case PRECONDITION:
            return getContentList(groupName, LABEL_PRECONDITION, "", ATT_VALUE, ids);
        default:
            return new ArrayList<>();
        }
    }

    /**
     * 该方法用于读取模板中的指定内容，并将所有需要读取的内容组成集合进行返回
     * 
     * @param groupName  用例组名称
     * @param labelName  读取的标签
     * @param otherXpath 需要拼接的xpath内容
     * @param attName    读取的属性名称
     * @param ids        用例id组
     * @return 内容集合
     * @since autest 4.0.0
     */
    protected List<String> getContentList(String groupName, String labelName, String otherXpath, String attName,
            String... ids) {
        List<String> contentList = new ArrayList<>();
        // 判断传入的id组是否为null
        if (ids == null || (groupName == null || groupName.isEmpty())) {
            return contentList;
        }

        // 遍历所有id
        for (String id : ids) {
            if (id.isEmpty()) {
                continue;
            }

            // 判断步骤是否读取详细步骤
            contentList.addAll(getAttributeValue(getXpathFormat(groupName, labelName, id, otherXpath), attName));
        }

        return contentList;
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

    /**
     * 该方法用于生成测试用例集合
     * 
     * @param templetClass  测试用例模板读取类
     * @param allContentMap 需要读取的测试用例集合
     * @return 生成的测试用例信息集合
     * @since autest 4.0.0
     */
    protected List<CaseData> createCaseDataList(AbstractPresetCaseTemplet templetClass,
            Map<LabelType, List<Entry<String, String[]>>> allContentMap) {
        // 定义需要返回的用例数据集合
        List<CaseData> caseDataList = new ArrayList<>();

        // 判断当前用例是否需要每步作为新的用例处理
        if (!isStepIndependentCase) {
            CaseData caseData = new CaseData(templetClass);
            // 遍历传入的所有用例内容
            for (LabelType LabelType : allContentMap.keySet()) {
                // 获取指定枚举的内容
                List<Entry<String, String[]>> contntList = allContentMap.get(LabelType);
                // 遍历并在用例信息类对象中，添加相应的内容
                for (Entry<String, String[]> contentGroup : contntList) {
                    caseData.addContent(LabelType.getName(), -1,
                            getTempletContent(LabelType, contentGroup.getKey(), contentGroup.getValue()));
                }
            }

            caseDataList.add(caseData);
        } else {
            // 存储所有内容id数组展开后的结果
            Map<LabelType, List<Entry<String, String>>> contentMap = new HashMap<>(
                    ConstType.DEFAULT_MAP_SIZE);
            // 遍历传入的所有用例内容
            for (LabelType LabelType : allContentMap.keySet()) {
                // 判断当前字段是否需要展开，以过滤掉无需展开的字段
                contentMap.put(LabelType, new ArrayList<>());
                // 展开集合中的数组，使其成为独立的用例
                for (Entry<String, String[]> contentGroup : allContentMap.get(LabelType)) {
                    for (String id : contentGroup.getValue()) {
                        contentMap.get(LabelType).add(new Entry<>(contentGroup.getKey(), id));
                    }
                }
            }

            // 读取关键词与前置条件的内容
            Entry<String, String> keyEntry = Optional.ofNullable(contentMap.get(LabelType.KEY))
                    .map(list -> list.get(0)).orElseGet(() -> new Entry<>("", ""));
            Entry<String, String> preconditionEntry = Optional
                    .ofNullable(contentMap.get(LabelType.PRECONDITION)).map(list -> list.get(0))
                    .orElseGet(() -> new Entry<>("", ""));

            // 由于是按照步骤对用例进行拆分，故需要计算步骤中的集合数量
            int stepNum = contentMap.get(LabelType.STEP).size();
            // 按照步骤数量，对各个字段相应位置的内容进行获取
            for (int index = 0; index < stepNum; index++) {
                CaseData caseData = new CaseData(templetClass);
                // 获取步骤相关的内容
                Entry<String, String> stepEntry = contentMap.get(LabelType.STEP).get(index);
                // 添加标题数据
                caseData.addContent(LabelType.TITLE.getName(), -1,
                        getTempletContent(LabelType.TITLE, stepEntry.getKey(), stepEntry.getValue()));
                // 添加步骤
                caseData.addContent(LabelType.STEP.getName(), -1,
                        getTempletContent(LabelType.STEP, stepEntry.getKey(), stepEntry.getValue()));
                // 优先级
                caseData.addContent(LabelType.RANK.getName(), -1,
                        getTempletContent(LabelType.RANK, stepEntry.getKey(), stepEntry.getValue()));
                
                // 添加预期
                Entry<String, String> exceptEntry = contentMap.get(LabelType.EXCEPT).get(index);
                caseData.addContent(LabelType.EXCEPT.getName(), -1,
                        getTempletContent(LabelType.EXCEPT, exceptEntry.getKey(), exceptEntry.getValue()));

                // 添加关键词
                caseData.addContent(LabelType.KEY.getName(), -1,
                        getTempletContent(LabelType.KEY, keyEntry.getKey(), keyEntry.getValue()));
                // 添加前置条件
                caseData.addContent(LabelType.PRECONDITION.getName(), -1, getTempletContent(LabelType.PRECONDITION,
                        preconditionEntry.getKey(),
                                preconditionEntry.getValue()));

                caseDataList.add(caseData);
            }
        }

        return caseDataList;
    }
}
