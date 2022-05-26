package com.auxiliary.http;

/**
 * <p>
 * <b>文件名：ResponseContentType.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 定义接口响应内容的格式枚举
 * </p>
 * <p>
 * <b>编码时间：2022年4月12日 上午8:57:02
 * </p>
 * <p>
 * <b>修改时间：2022年4月12日 上午8:57:02
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public enum MessageType {
    /**
     * json类型
     */
    JSON("application/json"),
    /**
     * xml类型
     */
    XML("application/xml"),
    /**
     * html类型
     */
    HTML("text/html"),
    /**
     * 纯文本类型
     */
    RAW("text/plain"),
    /**
     * 表单类型
     */
    FORM_DATA("multipart/form-data"),
    /**
     * 表单上传编码类型
     */
    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    /**
     * 二进制编码类型
     */
    BINARY("application/octet-stream"),
    /**
     * 文件类型
     */
    FILE("application/file"),
    /**
     * 无报文类型
     */
    NONE("none");

    /**
     * 报文类型名称
     */
    private String mediaName;

    /**
     * 初始化报文类型名称
     *
     * @param mediaValue 报文类型名称
     */
    private MessageType(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * 该方法用于返回报文类型名称
     *
     * @return 报文类型名称
     * @since autest 3.3.0
     */
    public String getMediaValue() {
        return mediaName;
    }

    /**
     * 该方法用于比对消息类型名称，返回对应的消息类型枚举
     * 
     * @param typeName 消息类型名称
     * @return 消息类型枚举
     * @since autest 3.3.0
     */
    public static MessageType typeOf(String typeName) {
        for (MessageType type : values()) {
            if (type.mediaName.equals(typeName)) {
                return type;
            }
        }

        return null;
    }
}
