package com.auxiliary.testcase.scene;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.StringJoiner;

import com.auxiliary.testcase.TestCaseException;

public class Flowcharting {
    /**
     * 用于存储已添加的节点
     */
    private HashMap<String, FlowchartNode> nodeMap = new HashMap<>(16);
    /**
     * 起始节点的名称
     */
    private String startNodeName = "";

    /**
     * 构造对象，初始化起始节点
     *
     * @param nodeName 起始节点名称
     * @param nodeText 起始节点文本
     * @since autest 3.2.0
     */
    public Flowcharting(String nodeName, String nodeText) {
        // 添加起始节点
        addNode(nodeName, nodeText, NodeGraphType.ELLIPSE);
        startNodeName = nodeName;
    }

    /**
     * 该方法用于添加一个自定义图形的节点元素
     * <p>
     * <b>注意：</b>自定义的图形元素不被当做特殊元素处理，例如，使用{@link NodeGraphType#DIAMOND}枚举，其不会被当成判定节点，只作为普通节点看待
     * </p>
     *
     * @param nodeName 节点名称
     * @param nodeText 节点文本
     * @param grapg    节点图形样式
     * @return 节点类对象
     * @since autest 3.2.0
     * @throws TestCaseException 节点名称为空时抛出的异常
     */
    public FlowchartNode addNode(String nodeName, String nodeText, NodeGraphType grapg) {
        FlowchartNode node = new FlowchartNode(nodeName, nodeText, grapg);
        nodeMap.put(nodeName, node);
        return node;
    }

    /**
     * 该方法用于添加判断节点
     *
     * @param nodeName 节点名称
     * @param nodeText 节点内文本
     * @return 节点类对象
     * @since autest 3.2.0
     * @throws TestCaseException 节点名称为空时抛出的异常
     */
    public FlowchartNode addJudgeNode(String nodeName, String nodeText) {
        return addNode(nodeName, nodeText, NodeGraphType.DIAMOND);
    }

    /**
     * 该方法用于普通流程节点
     *
     * @param nodeName 节点名称
     * @param nodeText 节点内文本
     * @return 节点类对象
     * @since autest 3.2.0
     * @throws TestCaseException 节点名称为空时抛出的异常
     */
    public FlowchartNode addFlowNode(String nodeName, String nodeText) {
        return addNode(nodeName, nodeText, NodeGraphType.ROUNDED_RECTANGLE);
    }

    /**
     * 该方法用于结束节点
     *
     * @param nodeName 节点名称
     * @param nodeText 节点内文本
     * @return 节点类对象
     * @since autest 3.2.0
     * @throws TestCaseException 节点名称为空时抛出的异常
     */
    public FlowchartNode addEndNode(String nodeName, String nodeText) {
        return addNode(nodeName, nodeText, NodeGraphType.ELLIPSE);
    }

    /**
     * 该方法用于页面引用节点
     *
     * @param nodeName 节点名称
     * @param nodeText 节点内文本
     * @return 节点类对象
     * @since autest 3.2.0
     * @throws TestCaseException 节点名称为空时抛出的异常
     */
    public FlowchartNode addPageReferenceNode(String nodeName, String nodeText) {
        return addNode(nodeName, nodeText, NodeGraphType.CIRCLE);
    }

    /**
     * 该方法用于返回在Markdown中，Mermaid语法绘制流程图的文本
     *
     * @param flowchartDirectionType 流程图方向枚举
     * @return 流程图的文本
     * @since autest 3.2.0
     */
    public String getFlowchartMermaidText(FlowchartDirectionType flowchartDirectionType) {
        // 定义文本的主体形式
        StringJoiner text = new StringJoiner("\n", String.format("``Mermaid\ngraph %s\n", flowchartDirectionType),
                "\n```");

        // 添加节点信息
        text.add("\t%% 节点信息");
        nodeMap.forEach((key, value) -> text.add("\t" + value.getNodeText()));
        // 添加节点关系信息
        text.add("\n\t%% 节点间关系信息");
        StringJoiner relationText = new StringJoiner("\n");
        getNodeRelationMermaidText(startNodeName, relationText);
        text.add(relationText.toString());

        return text.toString();
    }

    /**
     * 该方法用于处理节点之间的连接关系，并将其转换为相应的文本进行，添加至指定的字符串中
     *
     * @param nodeName 节点名称
     * @param text     文本
     * @since autest 3.2.0
     */
    private void getNodeRelationMermaidText(String nodeName, StringJoiner text) {
        // 获取节点
        FlowchartNode node = nodeMap.get(nodeName);
        // 判断节点是否存在下级节点，若无下级节点，则拼接当前节点后，再次向下判断子节点；若无子节点，则返回拼接的文本
        if (node.childNodeMap.isEmpty()) {
            return;
        }

        node.childNodeMap.forEach((key, value) -> {
            // 拼接节点间关系
            String relationText = String.format("\t%s%s%s", nodeName, value, key);
            // 判断该内容是否已经存在于text中，若不存在则拼接节点与当前子节点的关系
            if (!text.toString().contains(relationText)) {
                text.add(relationText);
            }
            // 拼接子节点与其孙节点之间的关系
            getNodeRelationMermaidText(key, text);
        });
    }

    /**
     * <p>
     * <b>文件名：</b>Flowcharting.java
     * </p>
     * <p>
     * <b>用途：</b> 存储流程图中每个节点的信息
     * </p>
     * <p>
     * <b>编码时间：</b>2022年3月1日 上午8:04:27
     * </p>
     * <p>
     * <b>修改时间：</b>2022年3月1日 上午8:04:27
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 3.2.0
     */
    public class FlowchartNode {
        /**
         * 节点名称
         */
        private String nodeName = "";
        /**
         * 节点内容
         */
        private String nodeText = "";
        /**
         * 当前节点图形
         */
        private NodeGraphType grapg;
        /**
         * 子节点连线集合
         */
        private HashMap<String, String> childNodeMap = new HashMap<>(16);

        /**
         * 初始化节点的内容及图形样式
         * <p>
         * <b>注意：</b>节点名称不能为空，否则抛出异常；节点内容为null时，则默认与名称内容一致；节点的图形为null时，默认采用{@link NodeGraphType#ROUNDED_RECTANGLE}
         * </p>
         *
         * @param nodeName 节点名称
         * @param nodeText 节点内容
         * @param grapg    图形枚举
         * @since autest 3.2.0
         * @throws TestCaseException 节点名称为空时抛出的异常
         */
        public FlowchartNode(String nodeName, String nodeText, NodeGraphType grapg) {
            super();
            this.nodeName = Optional.ofNullable(nodeName).filter(name -> !name.isEmpty())
                    .orElseThrow(() -> new TestCaseException("节点名称不能为空"));
            this.nodeText = Optional.ofNullable(nodeText).orElse(nodeName);
            this.grapg = Optional.ofNullable(grapg).orElse(NodeGraphType.ROUNDED_RECTANGLE);
        }

        /**
         * 该方法用于添加元素的父层节点
         *
         * @param nodeName 父层节点名称
         * @param lineType 节点间连接线型枚举
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        public FlowchartNode addParentNode(String parentNodeName, LineType lineType) {
            return addParentNode(parentNodeName, lineType, "");
        }

        /**
         * 该方法用于添加元素的父层节点
         *
         * @param nodeName 父层节点名称
         * @param lineType 节点间连接线型枚举
         * @param lineText 节点间连线上的文本内容
         * @return 类本身
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        public FlowchartNode addParentNode(String parentNodeName, LineType lineType, String lineText) {
            putNode(nodeMap.get(parentNodeName).childNodeMap, parentNodeName, lineType, lineText);
            return this;
        }

        /**
         * 该方法用于以普通的箭头线型和无连线内容的形式，为元素添加元素的父层节点
         *
         * @param parentNodeNames 父层节点名称
         * @return 类本身
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        public FlowchartNode addParentNode(String... parentNodeNames) {
            Optional.ofNullable(parentNodeNames).filter(arr -> arr.length != 0).map(Arrays::asList)
                    .ifPresent(list -> list.forEach(name -> addParentNode(name, LineType.ARROWS)));

            return this;
        }

        /**
         * 该方法用于添加一个已存在的子层节点，以便于循环流程的编写
         *
         * @param childNodeName 子节点名称
         * @param lineType      节点间连接线型枚举
         * @return 类本身
         * @since autest 3.2.0
         * @throws TestCaseException 父层节点不存在时抛出的异常
         */
        public FlowchartNode addExistingChildNode(String childNodeName, LineType lineType, String lineText) {
            putNode(childNodeMap, childNodeName, lineType, lineText);
            return this;
        }

        /**
         * 该方法用于添加一个已存在的子层节点，以便于循环流程的编写
         *
         * @param nodeName 父层节点名称
         * @param lineType 节点间连接线型枚举
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        public FlowchartNode addExistingChildNode(String childNodeName, LineType lineType) {
            return addExistingChildNode(childNodeName, lineType, "");
        }

        /**
         * 该方法用于以普通的箭头线型和无连线内容的形式，为元素添加元素的子层节点
         *
         * @param childNodeNames 子层节点名称组
         * @return 类本身
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        public FlowchartNode addExistingChildNode(String... childNodeNames) {
            Optional.ofNullable(childNodeNames).filter(arr -> arr.length != 0).map(Arrays::asList)
                    .ifPresent(list -> list.forEach(name -> addExistingChildNode(name, LineType.ARROWS)));

            return this;
        }

        /**
         * 该方法用于返回mermaid语法中，节点的表示文本
         *
         * @return 节点的表示文本
         * @since autest 3.2.0
         */
        public String getNodeText() {
            return nodeName + String.format(grapg.getSign(), nodeText);
        }

        /**
         * 该方法用于生成节点间连线的文本
         *
         * @param lineType 节点间连接线型枚举
         * @param lineText 节点间连线上的文本内容
         * @return 节点间连线的文本
         * @since autest 3.2.0
         */
        private String disposeLineText(LineType lineType, String lineText) {
            return lineType.getLine() + Optional.ofNullable(lineText).filter(text -> !text.isEmpty())
                    .map(text -> "|" + text + "|").orElse("");
        }

        /**
         * 该方法用于在指定的元素map集合中，添加相应的元素信息
         *
         * @param map 元素map集合
         * @param nodeName 需要添加的节点名称
         * @param lineType 节点间连接线型枚举
         * @param lineText 节点间连线上的文本内容
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        private void putNode(HashMap<String, String> map, String nodeName, LineType lineType, String lineText) {
            // 判断节点是否存在
            if (!nodeMap.containsKey(nodeName)) {
                throw new TestCaseException("不存在的流程节点：" + nodeName);
            }

            // 在指定的节点中，添加数据
            map.put(nodeName, disposeLineText(lineType, lineText));
        }
    }
}
