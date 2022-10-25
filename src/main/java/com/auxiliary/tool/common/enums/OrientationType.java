package com.auxiliary.tool.common.enums;

/**
 * <p>
 * <b>文件名：DirectionType.java</b>
 * </p>
 * <p>
 * <b>用途：</b>标记上、下、左、右四个朝向的枚举
 * </p>
 * <p>
 * <b>编码时间：2022年10月24日 上午10:38:23
 * </p>
 * <p>
 * <b>修改时间：2022年10月24日 上午10:38:23
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.7.0
 */
public enum OrientationType {
    /**
     * 上
     */
    UP(0),
    /**
     * 下
     */
    DOWN(1),
    /**
     * 左
     */
    LEFT(2),
    /**
     * 右
     */
    RIGHT(3);
    
    /**
     * 编码
     */
    private int code;

    /**
     * 初始化枚举编码
     * 
     * @param code 编码
     * @since autest 3.7.0
     */
    private OrientationType(int code) {
        this.code = code;
    }

    /**
     * 该方法用于返回枚举的编码
     * 
     * @return 枚举编码
     * @since autest 3.7.0
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 该方法用于根据枚举编码，判断与之相同编码的枚举，并进行返回。若未找到相同的枚举，则返回null
     * 
     * @param code 枚举编码
     * @return 相应编码的枚举
     * @since autest 3.7.0
     */
    public OrientationType valueOf(int code) {
        for (OrientationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        return null;
    }
}
