package com.auxiliary.tool.common.enums;

/**
 * <p>
 * <b>文件名：OrderedSignType.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义能转换的有序序号标志的类型
 * </p>
 * <p>
 * <b>编码时间：2023年5月9日 上午8:35:00
 * </p>
 * <p>
 * <b>修改时间：2023年5月9日 上午8:35:00
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public enum OrderedSignType {
    /**
     * 阿拉伯数字
     * 
     * @since autest 4.2.0
     */
    ARABIC_NUM(0),
    /**
     * 小写罗马数字
     * 
     * @since autest 4.2.0
     */
    ROMAN_NUM_LOWER(1),
    /**
     * 大写罗马数字
     * 
     * @since autest 4.2.0
     */
    ROMAN_NUM_UPPER(2),
    /**
     * 小写英文字母
     * 
     * @since autest 4.2.0
     */
    ENGLISH_LOWER(3),
    /**
     * 大写英文字母
     * 
     * @since autest 4.2.0
     */
    ENGLISH_UPPER(4);

    /**
     * 编码
     */
    private int code;

    /**
     * 初始化枚举编码
     * 
     * @param code 编码
     * @since autest 4.2.0
     */
    private OrderedSignType(int code) {
        this.code = code;
    }

    /**
     * 该方法用于返回枚举的编码
     * 
     * @return 枚举编码
     * @since autest 4.2.0
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 该方法用于根据枚举编码，判断与之相同编码的枚举，并进行返回。若未找到相同的枚举，则返回null
     * 
     * @param code 枚举编码
     * @return 相应编码的枚举
     * @since autest 4.2.0
     */
    public OrderedSignType valueOf(int code) {
        for (OrderedSignType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        return null;
    }
}
