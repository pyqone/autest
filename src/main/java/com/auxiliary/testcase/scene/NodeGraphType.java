package com.auxiliary.testcase.scene;

/**
 * <p>
 * <b>文件名：</b>NodeGraphType.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 枚举在流程图中需要使用到的节点图形枚举
 * </p>
 * <p>
 * <b>编码时间：</b>2022年3月1日 上午8:25:59
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月1日 上午8:25:59
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public enum NodeGraphType {
    /**
     * 圆角矩形
     */
    ROUNDED_RECTANGLE("(%s)"),
    /**
     * 椭圆形
     */
    ELLIPSE("([%s])"),
    /**
     * 子程序型
     */
    PROCESS("[[%s]]"),
    /**
     * 圆柱形
     */
    CYLINDER("[(%s)]"),
    /**
     * 圆形
     */
    CIRCLE("((%s))"),
    /**
     * 鱼尾旗形
     */
    STEP(">%s]"),
    /**
     * 菱形
     */
    DIAMOND("{%s}"),
    /**
     * 六边形
     */
    HEXAGON("{{%s}}"),
    /**
     * 平行四边形，顶部尖角朝右
     */
    PARALLELOGRAM_RIGHT("[/%s/]"),
    /**
     * 平行四边形，顶部尖角朝左
     */
    PARALLELOGRAM_LEFT("[\\%s\\]"),
    /**
     * 正梯形
     */
    TRAPEZOID("[/%s\\]"),
    /**
     * 倒置梯形
     */
    TRAPEZOID_INVERTED("[\\%s/]")
    ;

    /**
     * 图形在mermaid语法中的标识
     */
    String sign = "";

    /**
     * 初始化枚举的标识
     * @param sign 标志
     * @since autest 3.2.0
     */
    private NodeGraphType(String sign) {
        this.sign = sign;
    }

    /**
     * 该方法用于返回枚举在mermaid语法中的标识，以格式化字符串的形式，其%s表示图形中的内容
     *
     * @return 枚举在mermaid语法中的标识
     * @since autest 3.2.0
     */
    public String getSign() {
        return sign;
    }

}
