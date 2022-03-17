package com.auxiliary.testcase.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import com.auxiliary.testcase.scene.Flowcharting.FlowchartNode;
import com.auxiliary.testcase.scene.Flowcharting.LineEntry;

/**
 * <p>
 * <b>文件名：SceneTestCase.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 分解{@link Flowcharting}中绘制的流程图，得到从起始节点至各个结束节点的子流程，作为场景法生成的测试用例
 * </p>
 * <p>
 * <b>编码时间：2022年3月17日 下午6:02:11
 * </p>
 * <p>
 * <b>修改时间：2022年3月17日 下午6:02:11
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public class SceneTestCase {
    /**
     * 定义流程图文本间的分割符号
     */
    private final String FLOWCHART_TEXT_SEPARATOR_SYMBOL = "、";
    /**
     * 定义流程图文本循环的流程符号
     */
    private final String FLOWCHART_TEXT_LOOP_SYMBOL = "...";

    /**
     * 流程图大图
     */
    private Flowcharting flowchart;
    /**
     * 流程图大图的Mermaid文本
     */
    private String flowText;
    /**
     * 流程图大图的边数
     */
    private int lineNumber;
    /**
     * 流程图大图的节点数
     */
    private int nodeNumber;
    /**
     * 流程图大图的孤立节点数
     */
    private int insularNodeNumber;

    /**
     * 存放通过总流程图分解得到的各个子场景流程图
     */
    private ArrayList<Flowcharting> childFlowchartList = new ArrayList<>();
    /**
     * 存储分解流程图后的文本说明
     */
    private ArrayList<String> childFlowchartTextList = new ArrayList<>();

    /**
     * 构造对象，并初始化当前的总流程图
     * 
     * @param flowcharting 流程图类对象
     */
    public SceneTestCase(Flowcharting flowcharting) {
        this.flowchart = flowcharting.clone();

        // 拆分流程图，获取所有的子流程图
        analyseFlowchart(this.flowchart.getStartNode(), new ArrayList<>());

        // 获取流程图的基本信息
        flowText = this.flowchart.getFlowchartMermaidText(FlowchartDirectionType.TD);
        lineNumber = this.flowchart.getLineNumber();
        nodeNumber = this.flowchart.getNodeNumber();
        insularNodeNumber = this.flowchart.getInsularNodeNumber();
    }

    /**
     * 该方法用于返回通过大流程图，计算得到覆盖所有场景最少的用例数
     * <p>
     * <b>注意：</b>其计算方法借鉴在白盒测试中的圈复杂度的计算公式：
     * <p style=" text-align:center;">
     * <b><i>V(G) = 边 - 节点数 + 孤立节点数</i></b>
     * </p>
     * 该值亦可用在计算最少用例数上，但该值仅供参考，在调用{@link #getSceneFlowchart()}方法后，返回的场景数会大于等于该值，可参考该值，酌情取舍场景用例
     * </p>
     * 
     * @return 覆盖所有场景最少用例数
     * @since autest 3.2.0
     */
    public int getTestCaseNumber() {
        return lineNumber - nodeNumber + insularNodeNumber;
    }

    /**
     * 该方法用于返回通过大流程图分解的各个子流程图Mermaid文本
     * <p>
     * 该方法返回的流程图个数可能会与实际计算的圈复杂度存在一定的偏差，其取决于判定节点串联的个数，可根据计算值酌情筛选
     * </p>
     * 
     * @return 子流程图集合
     * @since autest 3.2.0
     */
    public List<Flowcharting> getSceneFlowchart() {
        return new ArrayList<>(childFlowchartList);
    }

    /**
     * 该方法用于返回通过大流程图分解的各个子流程图的说明文本
     * <p>
     * 流程图文本通过拼接节点名称，将其组成为说明被分解场景的节点走向，每个节点名称间，用顿号相隔
     * </p>
     * 
     * @return 子流程图的说明文本
     * @since autest 3.2.0
     */
    public List<String> getSceneFlowchartText() {
        return new ArrayList<>(childFlowchartTextList);
    }

    /**
     * 该方法用于返回初始化流程图的Mermaid文本，等同于调用{@link Flowcharting#getFlowchartMermaidText(FlowchartDirectionType)}，流程图以竖行绘制
     * 
     * @return 初始化流程图的Mermaid文本
     * @since autest 3.2.0
     */
    public String getFlowchartText() {
        return flowText;
    }

    /**
     * 该方法用于以Markdown形式返回通过分析流程图得到的场景测试用例文本
     * <p>
     * 该方法返回的内容不可制定，仅为一个内容返回的参考，可通过类中的其他方法，自行定义格式
     * </p>
     * 
     * @return 场景测试用例文本
     * @since autest 3.2.0
     */
    public String getTestCaseMarkdownText() {
        // 存储用例数
        StringJoiner caseText = new StringJoiner("\r\n");
        caseText.add("# 用例数");
        caseText.add(String.format("* 理论用例数 = %d - %d + %d = %d", lineNumber, nodeNumber, insularNodeNumber,
                getTestCaseNumber()));
        caseText.add("");
        caseText.add(String.format("* 实际用例数 ：%d", childFlowchartList.size()));
        
        // 存储流程大图
        caseText.add("# 总流程图");
        caseText.add(flowText);

        // 存储流程图
        caseText.add("# 场景用例");
        for (int index = 0; index < childFlowchartList.size(); index++) {
            caseText.add(String.format("- [ ] **第%d条流程：** %s", (index + 1), childFlowchartTextList.get(index)));
            caseText.add(childFlowchartList.get(index).getFlowchartMermaidText(FlowchartDirectionType.TD));
        }
        
        return caseText.toString();
    }

    /**
     * 该方法用于根据大流程图，对其逐一分解为子流程图，存储至类中
     * 
     * @param nodeName 起始节点名称
     * @param nodeList 节点名称集合
     * @since autest 3.2.0
     */
    private void analyseFlowchart(String nodeName, ArrayList<String> nodeList) {
        // 通过总流程图中的节点集合，获取节点类对象
        FlowchartNode node = flowchart.getNode(nodeName);
        // 存储当前节点
        nodeList.add(nodeName);

        // 判断节点是否存在下级节点，若无下级节点，则将节点集合中的节点组成
        if (node.getChildNodeMap().isEmpty()) {
            childFlowchartList.add(appendFlowchart(nodeList));
            childFlowchartTextList.add(appendFlowchartText(nodeList));

            return;
        }

        // 遍历当前节点的所有子节点
        node.getChildNodeMap().forEach((key, value) -> {
            // 判断当前子节点名称是否与自身相同，若相同，则不进行下一步操作
            if (!Objects.equals(nodeName, key)) {
                // 判断当前节点是否已经存在
                if (!nodeList.contains(key)) {
                    // 拼接子节点与其孙节点之间的关系
                    analyseFlowchart(key, new ArrayList<>(nodeList));
                } else {
                    nodeList.add(key);
                    childFlowchartList.add(appendFlowchart(nodeList));
                    childFlowchartTextList.add(appendFlowchartText(nodeList) + FLOWCHART_TEXT_LOOP_SYMBOL);
                    
                    nodeList.remove(nodeList.size() - 1);
                }
            }
        });
    }

    /**
     * 该方法用于将节点名称集合，转换为一套流程图，返回对应的流程图类对象
     * 
     * @param nodeList 节点名称集合
     * @return 节点集合对应的流程图类对象
     * @since autest 3.2.0
     */
    private Flowcharting appendFlowchart(ArrayList<String> nodeList) {
        // 获取当前流程的起始节点，并构造新的流程图类进行存储
        FlowchartNode startNode = flowchart.getNode(nodeList.get(0));
        Flowcharting flow = new Flowcharting(startNode.getNodeName(), startNode.getNodeText());

        // 循环，从第二个点开始，将集合的节点存储至新流程图中
        for (int index = 1; index < nodeList.size(); index++) {
            // 获取当前节点
            FlowchartNode nowNode = flowchart.getNode(nodeList.get(index));
            // 获取当前节点的父节点及与当前节点的连接方式
            FlowchartNode parentNode = flowchart.getNode(nodeList.get(index - 1));
            LineEntry parentLineEntry = parentNode.getChildNodeLineEntry(nowNode.getNodeName());

            // 在新流程图中，使当前节点连接至父层节点
            flow.addNode(nowNode.getNodeName(), nowNode.getNodeText(), nowNode.getGraph())
                    .addParentNode(parentNode.getNodeName(), parentLineEntry.getKey(), parentLineEntry.getValue());
        }

        return flow;
    }

    /**
     * 该方法用于拼接流程图文本
     * 
     * @param nodeList节点名称集合
     * @return 流程图文本
     * @since autest 3.2.0
     */
    private String appendFlowchartText(ArrayList<String> nodeList) {
        StringJoiner text = new StringJoiner(FLOWCHART_TEXT_SEPARATOR_SYMBOL);
        nodeList.forEach(text::add);
        return text.toString();
    }
}
