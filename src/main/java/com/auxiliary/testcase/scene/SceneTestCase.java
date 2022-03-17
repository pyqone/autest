package com.auxiliary.testcase.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.auxiliary.testcase.scene.Flowcharting.FlowchartNode;
import com.auxiliary.testcase.scene.Flowcharting.LineEntry;

public class SceneTestCase {
    /**
     * 流程图大图
     */
    Flowcharting flowchart;
    /**
     * 流程图大图的Mermaid文本
     */
    String flowText;
    /**
     * 流程图大图的边数
     */
    int lineNumber;
    /**
     * 流程图大图的节点数
     */
    int nodeNumber;
    /**
     * 流程图大图的孤立节点数
     */
    int insularNodeNumber;

    /**
     * 存放通过总流程图分解得到的各个子场景流程图
     */
    ArrayList<Flowcharting> childFlowchartList = new ArrayList<>();

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
     * 该方法用于返回通过大流程图分解的各个子流程图
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
}
