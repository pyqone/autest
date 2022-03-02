package com.auxiliary.testcase.scene;

/**
 * <p>
 * <b>文件名：</b>LineType.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 枚举流程图中，每个节点间连线使用的线型枚举
 * </p>
 * <p>
 * <b>编码时间：</b>2022年3月1日 下午2:04:57
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月1日 下午2:04:57
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.2.0
 */
public enum LineType {
    /**
     * 直线连线
     */
    LINE("---"),
    /**
     * 加粗直线
     */
    BOLD_LINE("==="),
    /**
     * 直虚线
     */
    DASHED_LINE("-.-"),
    /**
     * 单向箭头连线
     */
    ARROWS("-->"),
    /**
     * 双线箭头连线
     */
    BIDIRECTION_ARROWS("<-->"),
    /**
     * 单向加粗箭头连线
     */
    BOLD_ARROWS("==>"),
    /**
     * 双向加粗箭头连线
     */
    BIDIRECTION_BOLD_ARROWS("<==>"),
    /**
     * 单向虚线箭头连线
     */
    DASHED_ARROWS("-.->"),
    /**
     * 双向虚线箭头连线
     */
    BIDIRECTION_DASHED_ARROWS("<-.->"),
    /**
     * 单向圆头连线
     */
    CIRCLE("--o"),
    /**
     * 双向圆头连线
     */
    BIDIRECTION_CIRCLE("o--o"),
    /**
     * 单向加粗圆头连线
     */
    BOLD_CIRCLE("==o"),
    /**
     * 双向加粗圆头连线
     */
    BIDIRECTION_BOLD_CIRCLE("o==o"),
    /**
     * 单向虚线圆头连线
     */
    DASHED_CIRCLE("-.-o"),
    /**
     * 双向虚线圆头连线
     */
    BIDIRECTION_DASHED_CIRCLE("o-.-o"),
    /**
     * 单向星形头连线
     */
    STARLIKE("--x"),
    /**
     * 双向星形头连线
     */
    BIDIRECTION_STARLIKE("x--x"),
    /**
     * 单向加粗星形头连线
     */
    BOLD_STARLIKE("==x"),
    /**
     * 双向加粗星形头连线
     */
    BIDIRECTION_BOLD_STARLIKE("x==x"),
    /**
     * 单向虚线星形头连线
     */
    DASHED_STARLIKE("-.-x"),
    /**
     * 双向虚线星形头连线
     */
    BIDIRECTION_DASHED_STARLIKE("x-.-x")
    ;

    /**
     * 连线头部的形状
     */
    String shape = "";

    /**
     * 初始化连线头部形状
     * @param shape 形状
     * @since autest 3.2.0
     */
    private LineType(String shape) {
        this.shape = shape;
    }

    /**
     * 该方法用于返回节点间连线的线型
     *
     * @return 节点间连线的线型
     * @since autest 3.2.0
     */
    public String getLine() {
        return shape;
    }
}
