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
    LINE("-"),
    /**
     * 箭头连线
     */
    ARROWS(">"),
    /**
     * 圆头连线
     */
    CIRCLE("o"),
    /**
     * 星形头连线
     */
    STARLIKE("x")
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
     * 该方法用于返回节点间枚举对应形状默认线型的连线
     *
     * @param isBidirection 是否需要反向
     * @return 枚举对应形状的默认线型连线
     * @since autest 3.2.0
     */
    public String getNormalLine(boolean isBidirection) {
        String text = "--" + shape;
        if (isBidirection) {
            return disposeBidirectionText(this, text);
        } else {
            return text;
        }
    }

    /**
     * 该方法用于返回节点间枚举对应形状默认线型的连线
     *
     * @return 枚举对应形状的默认线型连线
     * @since autest 3.2.0
     */
    public String getNormalLine() {
        return getNormalLine(false);
    }

    /**
     * 该方法用于返回节点间枚举对应形状加粗线型的连线
     *
     * @param isBidirection 是否需要反向
     * @return 枚举对应形状的加粗线型连线
     * @since autest 3.2.0
     */
    public String getBoldLine(boolean isBidirection) {
        String text = "==" + shape;
        if (isBidirection) {
            return disposeBidirectionText(this, text);
        } else {
            return text;
        }
    }

    /**
     * 该方法用于返回节点间枚举对应形状加粗线型的连线
     *
     * @return 枚举对应形状的加粗线型连线
     * @since autest 3.2.0
     */
    public String getBoldLine() {
        return getBoldLine(false);
    }

    /**
     * 该方法用于返回节点间枚举对应形状虚线线型的连线
     *
     * @param isBidirection 是否需要反向
     * @return 枚举对应形状的虚线线型连线
     * @since autest 3.2.0
     */
    public String getDashedLine(boolean isBidirection) {
        String text = "-.-" + shape;
        if (isBidirection) {
            return disposeBidirectionText(this, text);
        } else {
            return text;
        }
    }

    /**
     * 该方法用于返回节点间枚举对应形状虚线线型的连线
     *
     * @return 枚举对应形状的虚线线型连线
     * @since autest 3.2.0
     */
    public String getDashedLine() {
        return getDashedLine(false);
    }

    /**
     * 该方法用于对当箭头需要反向时，进行处理相应的线型文本
     *
     * @param type 枚举
     * @param LineText 线型文本
     * @return 处理后的文本
     * @since autest 3.2.0
     */
    private String disposeBidirectionText(LineType type, String LineText) {
        switch (type) {
        case LINE:
            return LineText;
        case ARROWS:
            return "<" + LineText;
        default:
            return type.shape + LineText;
        }
    }
}
