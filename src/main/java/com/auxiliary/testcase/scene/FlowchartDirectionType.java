package com.auxiliary.testcase.scene;

/**
 * <p>
 * <b>文件名：</b>FlowchartDirectionType.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 流程图绘制方向枚举，用于标记流程图节点的排列方向
 * </p>
 * <p>
 * <b>编码时间：</b>2022年3月3日 上午8:57:37
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月3日 上午8:57:37
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public enum FlowchartDirectionType {
    /**
     * 从上到下
     */
    TB,
    /**
     * 从上到下
     */
    TD,
    /**
     * 从下到上
     */
    BT,
    /**
     * 从右到左
     */
    RL,
    /**
     * 从左到右
     */
    LR;
}
