package com.auxiliary.tool.common.enums;

/**
 * <p>
 * <b>文件名：MathematicsSymbolType.java</b>
 * </p>
 * <p>
 * <b>用途：</b>
 * </p>
 * <p>
 * <b>编码时间：2022年7月7日 上午8:30:28
 * </p>
 * <p>
 * <b>修改时间：2022年7月7日 上午8:30:28
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 
 */
public enum MathematicsSymbolType {
    /**
     * 加
     */
    ADD("+", "加"),
    /**
     * 减
     */
    SUBTRACT("-", "减"),
    /**
     * 乘
     */
    MULTIPLY("*", "乘"),
    /**
     * 除
     */
    DIVIDE("/", "除"),
    /**
     * 大于
     */
    GREATER(">", "大于"),
    /**
     * 大于等于
     */
    GREATER_AND_EQUALS(">=", "大于等于"),
    /**
     * 小于
     */
    LESS("<", "小于"),
    /**
     * 小于等于
     */
    LESS_AND_EQUALS("<=", "小于等于"),
    /**
     * 等于
     */
    EQUALS("==", "等于"),
    /**
     * 不等于
     */
    NOT_EQUALS("!=", "不等于");

    /**
     * 存储符号内容
     */
    private String symbol;
    /**
     * 存储符号名称
     */
    private String name;

    /**
     * 初始化数学符号及名称
     * 
     * @param symbol 数学符号
     * @param name   符号名称
     */
    private MathematicsSymbolType(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    /**
     * 该方法用于返回枚举表示的数学符号
     * 
     * @return 数学符号
     * @since autest 3.5.0
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 该方法用于返回枚举表示的符号名称
     *
     * @return 符号名称
     * @since autest 3.5.0
     */
    public String getName() {
        return name;
    }
}
