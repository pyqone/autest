package com.auxiliary.testcase.scene;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import com.auxiliary.testcase.TestCaseException;

/**
 * <p>
 * <b>文件名：</b>Flowcharting.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 用于定义流程图的各个节点与其之间的连线关系，生成基于Mermaid语法的流程图文本，该内容可写入到支持Mermaid的Markdown编辑器中，
 * 或通过<a href=
 * 'https://mermaid-js.github.io/mermaid-live-editor/edit/'>Mermaid在线编辑器</a>生成相应的流程图
 * </p>
 * <p>
 * <b>注意：</b>每次添加节点后，会以默认的线型连接上一次添加的节点，若不希望连接上一次节点，可调用{@link FlowchartNode#addParentNode(String...)}等方法修改相应的父层或子层
 * </p>
 * <p>
 * <b>编码时间：</b>2022年3月4日 上午7:51:20
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月4日 上午7:51:20
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
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
     * 最后一层节点名称
     */
    private String lastNode = "";

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

        // 设置起始节点和最后一层节点名称
        startNodeName = nodeName;
        lastNode = nodeName;
    }

    /**
     * 该方法用于添加一个自定义图形的节点元素
     * <p>
     * <b>注意：</b>自定义的图形元素不被当做特殊元素处理，例如，使用{@link NodeGraphType#DIAMOND}枚举，其不会被当成判定节点，只作为普通节点看待
     * </p>
     *
     * @param nodeName      节点名称
     * @param nodeText      节点文本
     * @param nodeGraphType 节点图形样式
     * @return 节点类对象
     * @since autest 3.2.0
     * @throws TestCaseException 节点名称为空时抛出的异常
     */
    public FlowchartNode addNode(String nodeName, String nodeText, NodeGraphType nodeGraphType) {
        // 存储节点
        FlowchartNode node = new FlowchartNode(nodeName, nodeText, nodeGraphType);
        nodeMap.put(nodeName, node);

        // 判断当前节点名称是否与最后一层节点名称相同，若不相同，则将最后一层节点名称作为其虚拟父层节点，并设置当前节点为最后一层节点
        if (!Objects.equals(lastNode, nodeName) && !lastNode.isEmpty()) {
            // 设置节点的默认父层节点
            node.addParentNode(lastNode);

            // 设置节点连接虚拟节点的信息
            node.virtualParentNode = lastNode;
            lastNode = nodeName;
        }

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
        // 定义文本的主体形式，若未定义方向，则默认为TD
        StringJoiner text = new StringJoiner("\n", String.format("```Mermaid\nflowchart %s\n",
                Optional.ofNullable(flowchartDirectionType).orElse(FlowchartDirectionType.TD)), "\n```");

        // 添加节点信息
        text.add("\t%% 节点信息");
        nodeMap.forEach((key, value) -> text.add("\t" + value.getNodeText()));
        // 添加节点关系信息
        text.add("\n\t%% 节点间关系信息");
        StringJoiner relationText = new StringJoiner("\n");
        appendNodeRelationMermaidText(startNodeName, relationText, new HashSet<>());
        text.add(relationText.toString());

        return text.toString();
    }

    // TODO 添加返回孤立节点与边数的方法
    // TODO 添加移除节点的方法，考虑移除后，各个节点间的连接关系
    // TODO 添加返回指定的节点方法

    /**
     * 该方法用于处理节点之间的连接关系，并将其转换为相应的文本进行，添加至指定的字符串中
     *
     * @param nodeName         节点名称
     * @param text             文本
     * @param childNodeNameSet 子节点集合，用于当节点已被存储时，则不再重复调用方法，避免死递归
     * @since autest 3.2.0
     */
    private void appendNodeRelationMermaidText(String nodeName, StringJoiner text, HashSet<String> childNodeNameSet) {
        // 获取节点
        FlowchartNode node = nodeMap.get(nodeName);

        // 判断节点是否存在下级节点，若无下级节点，则拼接当前节点后，再次向下判断子节点；若无子节点，则返回拼接的文本
        if (node.childNodeMap.isEmpty()) {
            return;
        }

        node.childNodeMap.forEach((key, value) -> {
            // 拼接节点间关系
            String relationText = String.format("\t%s %s %s", nodeName, value, key);
            // 判断该内容是否已经存在于text中，若不存在则拼接节点与当前子节点的关系
            if (!text.toString().contains(relationText)) {
                text.add(relationText);
            }

            // 判断当前子节点名称是否与自身相同，若不相同，再递归调用自身，对子节点进行判定
            if (!Objects.equals(nodeName, key) && !childNodeNameSet.contains(key)) {
                childNodeNameSet.add(key);
                // 拼接子节点与其孙节点之间的关系
                appendNodeRelationMermaidText(key, text, childNodeNameSet);
            }
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
         * 定义文本换行符
         */
        private static final String LINE_SWITCH = "<br>";
        /**
         * 定义节点文本换行字数
         */
        private final int LINE_SWITCH_INDEX = 4;

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
        private NodeGraphType graph;

        /**
         * 子节点连线集合
         */
        private HashMap<String, String> childNodeMap = new HashMap<>(16);

        /**
         * 虚拟父层节点
         */
        private String virtualParentNode = "";

        /**
         * 初始化节点的内容及图形样式
         * <p>
         * <b>注意：</b>节点名称不能为空，否则抛出异常；节点内容为null时，则默认与名称内容一致；节点的图形为null时，默认采用{@link NodeGraphType#ROUNDED_RECTANGLE}
         * </p>
         *
         * @param nodeName      节点名称
         * @param nodeText      节点内容
         * @param nodeGraphType 图形枚举
         * @since autest 3.2.0
         * @throws TestCaseException 节点名称为空时抛出的异常
         */
        public FlowchartNode(String nodeName, String nodeText, NodeGraphType nodeGraphType) {
            super();
            this.nodeName = Optional.ofNullable(nodeName).filter(name -> !name.isEmpty())
                    .orElseThrow(() -> new TestCaseException("节点名称不能为空"));
            setNodeText(nodeText);
            setNodeGraphType(nodeGraphType);
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
            // 添加父层节点
            putNode(parentNodeName, lineType, lineText, true);

            // 判断当前是否存在虚拟节点，且当前添加的节点名称非自身节点名称
            if (!virtualParentNode.isEmpty() && !Objects.equals(parentNodeName, nodeName)) {
                // 判断当前虚拟节点名称与添加的父层节点名称是否一致，不一致，则移除虚拟节点
                if (!Objects.equals(parentNodeName, virtualParentNode)) {
                    removeParentNode(virtualParentNode);
                }
                // 将虚拟节点置空，表示当前节点不需要连接虚拟节点
                virtualParentNode = "";
            }

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
         * @throws TestCaseException 节点不存在或子节点为起始节点时抛出的异常
         */
        public FlowchartNode addChildNode(String childNodeName, LineType lineType, String lineText) {
            // 判断当前节点是否为起始节点，若为起始节点，则排除异常
            if (Objects.equals(childNodeName, startNodeName)) {
                throw new TestCaseException("子节点不能是起始节点：" + startNodeName);
            }

            putNode(childNodeName, lineType, lineText, false);
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
        public FlowchartNode addChildNode(String childNodeName, LineType lineType) {
            return addChildNode(childNodeName, lineType, "");
        }

        /**
         * 该方法用于以普通的箭头线型和无连线内容的形式，为元素添加元素的子层节点
         *
         * @param childNodeNames 子层节点名称组
         * @return 类本身
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        public FlowchartNode addChildNode(String... childNodeNames) {
            Optional.ofNullable(childNodeNames).filter(arr -> arr.length != 0).map(Arrays::asList)
                    .ifPresent(list -> list.forEach(name -> addChildNode(name, LineType.ARROWS)));

            return this;
        }

        /**
         * 该方法用于移除已添加的父层节点
         *
         * @param nodeName 父层节点名称
         * @return 类本身
         * @since autest 3.2.0
         */
        public FlowchartNode removeParentNode(String parentNodeName) {
            if (nodeMap.containsKey(parentNodeName)) {
                nodeMap.get(parentNodeName).childNodeMap.remove(nodeName);
            }

            return this;
        }

        /**
         * 该方法用于移除已添加的子层节点
         *
         * @param nodeName 子层节点名称
         * @return 类本身
         * @since autest 3.2.0
         */
        public FlowchartNode removeChildNode(String childNodeName) {
            childNodeMap.remove(childNodeName);
            return this;
        }

        /**
         * 该方法用于设置节点的文本内容
         * <p>
         * 节点内容将按照每4个字符，添加一个换行符&lt;br&gt;进行处理，以避免节点图形过大；当节点内容为空或为null事，则将节点名称作为节点内容
         * </p>
         *
         * @param text 节点内容
         * @return 类本身
         * @since autest 3.2.0
         */
        public FlowchartNode setNodeText(String text) {
            nodeText = Optional.ofNullable(text).map(this::disposeNodeText).orElse(disposeNodeText(nodeName));
            return this;
        }

        /**
         * 该方法用于设置节点的图形样式
         * <p>
         * 若传入的节点为null，则默认使用{@link NodeGraphType#ROUNDED_RECTANGLE}
         * </p>
         *
         * @param nodeGraphType 节点图形样式
         * @return 类本身
         * @since autest 3.2.0
         */
        public FlowchartNode setNodeGraphType(NodeGraphType nodeGraphType) {
            graph = Optional.ofNullable(nodeGraphType).orElse(NodeGraphType.ROUNDED_RECTANGLE);
            return this;
        }

        /**
         * 该方法用于返回mermaid语法中，节点的表示文本
         *
         * @return 节点的表示文本
         * @since autest 3.2.0
         */
        public String getNodeText() {
            return nodeName + String.format(graph.getSign(), nodeText);
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
            return Optional.ofNullable(lineType).orElse(LineType.ARROWS).getLine() + Optional.ofNullable(lineText)
                    .filter(text -> !text.isEmpty()).map(text -> "|" + text + "|").orElse("");
        }

        /**
         * 该方法用于对节点内的文本进行处理，当字数超过{@link #LINE_SWITCH_INDEX}限制的字数使，则在其后添加一个{@link #LINE_SWITCH}符号
         *
         * @param text 需要处理的文本
         * @return 处理后的文本
         * @since autest 3.2.0
         */
        private String disposeNodeText(String text) {
            StringBuilder sb = new StringBuilder(text);
            int endIndex = LINE_SWITCH_INDEX;

            // 循环，为每“LINE_SWITCH_INDEX”个字后添加一个换行符
            while (sb.length() > endIndex) {
                sb.insert(endIndex, LINE_SWITCH);
                endIndex += (LINE_SWITCH.length() + LINE_SWITCH_INDEX);
            }

            return sb.toString();
        }

        /**
         * 该方法用于在指定的元素map集合中，添加相应的元素信息
         *
         * @param nodeName 需要添加的节点名称
         * @param lineType 节点间连接线型枚举
         * @param lineText 节点间连线上的文本内容
         * @since autest 3.2.0
         * @throws TestCaseException 节点不存在时抛出的异常
         */
        private void putNode(String nodeName, LineType lineType, String lineText, boolean isParent) {
            // 判断节点是否存在
            if (!nodeMap.containsKey(nodeName)) {
                throw new TestCaseException("不存在的流程节点：" + nodeName);
            }

            HashMap<String, String> map = null;
            if (isParent) {
                map = nodeMap.get(nodeName).childNodeMap;
                nodeName = this.nodeName;
            } else {
                map = childNodeMap;
            }

            // 在指定的节点中，添加数据
            map.put(nodeName, disposeLineText(lineType, lineText));
        }
    }
}
