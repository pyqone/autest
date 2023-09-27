package com.auxiliary.http;

import java.util.function.Function;

import com.auxiliary.tool.common.DisposeCodeUtils;

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
     * 表单类型，与{@link #FORM_DATA}一致
     *
     * @deprecated 该枚举已无效，将在3.8.0或更高版本中删除
     */
    FD(FORM_DATA.mediaName),
    /**
     * 表单上传编码类型
     */
    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    /**
     * 表单上传编码类型，与{@link #X_WWW_FORM_URLENCODED}一致
     *
     * @deprecated 该枚举已无效，将在3.8.0或更高版本中删除
     */
    FU(X_WWW_FORM_URLENCODED.mediaName),
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

    /**
     * 该方法用于将枚举文本转换为消息枚举
     * <p>
     * 该方法本质上与{@link #valueOf(String)}方法的作用一致，但其中加上了对文本的内容的判空、对文本内容大写转换、
     * 对特殊文本进行处理以及对异常文本进行汉化的操作。可转换的枚举文本如下：
     * </p>
     * <ul>
     * <li>若文本为“text”，则转换为“raw”输出</li>
     * <li>若文本为“fd”，则转换为“form-data”输出</li>
     * <li>若文本为“fu”，则转换为“X-WWW-FORM-URLENCODED”输出</li>
     * </ul>
     *
     * @param type 枚举文本内容
     * @return 转换后的枚举
     * @since autest 3.7.0
     * @throws IllegalArgumentException 当枚举文本为空或不能转换成枚举时抛出的异常
     */
    public static MessageType typeText2Type(String type) {
        return DisposeCodeUtils.disposeEnumTypeText(MessageType.class, type, text -> {
            text = text.toUpperCase();
            switch(text) {
            case "FD":
                text = FORM_DATA.toString();
                break;
            case "FU":
                text = X_WWW_FORM_URLENCODED.toString();
                break;
            case "TEXT":
                text = RAW.toString();
                break;
            default :
                break;
            }

            return text;
        }, true, (short) 0);

    }

    /**
     * 该方法用于将枚举文本转换为消息枚举
     * <p>
     * 该方法与{@link #typeText2Type(String)}方法类似，其可对传入的文本进行自定义的转换，例如欲将传入的"text"转换为"raw"，则可写为
     * <code><pre>
     * MessageType.typeText2MessageType("text", type -&gt; {
     *      if ("text".equals(text)) {
     *          text = "raw";
     *      }
     *
     *      return text;
     * });
     * </pre></code>
     * </p>
     *
     * @param type   枚举文本内容
     * @param mapper 枚举文本的处理方法
     * @return 转换后的枚举
     * @since autest 3.7.0
     * @throws IllegalArgumentException 当枚举文本为空或不能转换成枚举时抛出的异常
     */
    public static MessageType typeText2Type(String type, Function<String, String> mapper) {
        return DisposeCodeUtils.disposeEnumTypeText(MessageType.class, type, mapper, true);
    }
}
