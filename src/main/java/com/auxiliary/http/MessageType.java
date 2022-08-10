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
 * <b>修改时间：2022年8月8日 下午4:10:29
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
     * 格式编码
     */
    private String charset = "";

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
        String mediaName = "";
        String charset = "";
        // 根据传入的内容，将其分解为类型文本及字符串编码类型
        if (typeName.contains(";")) {
            String[] texts = typeName.split(";");
            mediaName = texts[0];
            if (texts[1].contains("=")) {
                charset = texts[1].split("=")[1];
            }
        }

        if (mediaName.isEmpty()) {
            return null;
        }

        // 根据类型文本，判断相应的枚举
        for (MessageType type : values()) {
            if (type.mediaName.equals(mediaName)) {
                if (!charset.isEmpty()) {
                    type.setCharset(charset);
                }
                return type;
            }
        }

        return null;
    }

    /**
     * 该方法用于设置报文类型的编码格式
     * 
     * @param charset 编码格式名称字符串
     * @return 枚举本身
     * @since autest 3.6.0
     */
    public MessageType setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 该方法用于返回报文类型的详细文本内容（包含编码）
     * 
     * @return 报文类型的详细文本内容
     * @since autest 3.6.0
     */
    public String toMessageTypeString() {
        String text = mediaName;
        if (!charset.isEmpty()) {
            text += (";charset=" + charset);
        }

        return text;
    }
}
