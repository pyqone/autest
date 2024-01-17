package com.auxiliary.http;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * <b>用途：</b>用于对Socket请求方式的响应报文进行解析，并提供相应的报文处理方法
 * </p>
 * <p>
 * <b>编码时间：2023年12月08日 08:31:20
 * </p>
 * <p>
 * <b>修改时间：2023年12月08日 08:31:20
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.5.0
 */
public class EasySockecResponse extends EasyResponse {
    /**
     * 存储当前报文请求体长度标识数组，用于返回完整报文时，拼接至主体报文前
     *
     * @since autest 4.5.0
     */
    protected byte[] responseBodyLengthBytes;
    /**
     * 存储流中返回的所有的字节内容
     *
     * @since autest 4.5.0
     */
    protected List<byte[]> responseBodyByteList;
    /**
     * 存储当前响应体的字节长度
     *
     * @since autest 4.5.0
     */
    protected int bodyLength = 0;
    /**
     * 存储当前响应体的格式
     *
     * @since autest 4.5.0
     */
    protected Set<MessageType> messageTypeSet = new HashSet<>();

    /**
     * 构造对象
     *
     * @param responseBodyLengthBytes 报文返回的长度标识字节数组
     * @param responseBodyByteList    报文主体字节数组集合（允许最后一个元素中包含重复的字节）
     * @param bodyLength              报文的主体实际长度
     * @param interInfo               接口信息类对象
     * @since autest 4.5.0
     */
    protected EasySockecResponse(byte[] responseBodyLengthBytes, List<byte[]> responseBodyByteList, int bodyLength,
            InterfaceInfo interInfo) {
        // 获取长度标识字节数组，若传入为null，则设置为空数组
        this.responseBodyLengthBytes = Optional.ofNullable(responseBodyLengthBytes).orElseGet(() -> new byte[] {});
        // 根据响应报文字节位数，初始化响应报文字节数组
        this.responseBodyByteList.addAll(responseBodyByteList);
        // 记录接口实际请求信息
        requestInterInfo = interInfo;
        // 设置默认的响应字符集编码
        charsetName = interInfo.getResponseCharsetname();
    }

    /**
     * 该方法用于将类中的{@link #responseBodyByteList}属性转换为一个完整的byte数组
     *
     * @return 响应体完整的byte数组
     * @since autest 4.5.0
     */
    protected byte[] conversionByteList() {
        // 存储完整的报文字节数组
        byte[] responseBodyBytes = new byte[bodyLength];
        // 存储当前最大处理的字节长度
        int disposeMaxLength = 0;
        // 存储当前需要存储至responseBodyBytes中的下标
        int disposeIndex = 0;

        // 转换响应报文主体，循环处理报文集合中的内容，直到最后一个报文字节组之前
        for (byte[] bytes : responseBodyByteList) {
            // 定义当前最后数组最后需要读取的位置
            int endIndex = bytes.length;
            // 根据当前数组的长度，计算当前需要添加至responseBodyBytes数组中的内容
            // 若已处理的字节长度加上当前数组的长度小于响应体总长度，则根据当前数组长度将数组内容添加至responseBodyBytes中
            // 若超出响应体总长度，则按照当前数组的长度，减去多出的差值，以确认需要读取的位置
            disposeMaxLength += bytes.length;
            if (disposeMaxLength > bodyLength) {
                endIndex = bytes.length - (disposeMaxLength - bodyLength);
            }

            // 循环，根据结束下标，将每个字节内容一一存储至responseBodyBytes中
            for (int index = 0; index < endIndex; index++) {
                responseBodyBytes[disposeIndex++] = bytes[index];
            }
        }

        return responseBodyBytes;
    }

    @Override
    public String getResponseBodyText() {
        try {
            return new String(conversionByteList(), charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new SocketResponseException("当前请求体字节数组或字符集编码异常，无法转换");
        }
    }

    /**
     * 该方法用于返回响应主体部分字节数组
     *
     * @return 响应主体部分字节数组
     * @since autest 4.5.0
     */
    public byte[] getResponseBodyBytes() {
        return conversionByteList();
    }

    /**
     * 该方法用于返回当前相应内容字节长度的字节数组，组合在{@link #getResponseBodyBytes()}方法返回的响应内容前，可组成响应体完整的报文，
     * 若当前响应内容中不包含字节长度内容，则返回空数组
     *
     * @return 字节长度响应体数组
     * @since autest 4.5.0
     */
    public byte[] getResponseLengthBytes() {
        return responseBodyLengthBytes;
    }

    /**
     * 该方法用于返回响应主体内容的报文字节长度，即整体报文长度减去{@link #getResponseLengthBytes()}方法返回的数组长度
     *
     * @return 报文主体长度
     * @since autest 4.5.0
     */
    public int getResponseBodyLength() {
        return bodyLength;
    }

    /**
     * 该方法用于返回接口响应的所有部分的字节长度，若接口不存在定义主体字节长度的内容，则返回的长度与{@link #getResponseBodyLength()}方法返回的长度一致
     *
     * @return 完整响应报文的字节长度
     * @since autest 4.5.0
     */
    public int getResponseLength() {
        return responseBodyLengthBytes.length + bodyLength;
    }

    /**
     * 该方法用于设置当前响应体的内容格式，每次调用时会覆盖上次设置的内容
     *
     * @param messageTypes 消息格式枚举
     * @since autest 4.5.0
     */
    public void setMessageType(MessageType... messageTypes) {
        // 清空已存储的消息格式
        messageTypeSet.clear();
        // 将数组中的内容，添加至集合中
        for (MessageType messageType : messageTypes) {
            if (messageType != null) {
                messageTypeSet.add(messageType);
            }
        }
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。方法中各个参数的解释，可参考{@link #extractKey(SearchType, String, String, String, String, int)}方法
     * </p>
     *
     * @param assertRegex   断言规则
     * @param paramName     搜索变量
     * @param xpath         提取内容xpath
     * @param leftBoundary  断言内容左边界
     * @param rightBoundary 断言内容右边界
     * @param index         指定下标内容
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, String paramName, String xpath,
            String leftBoundary, String rightBoundary, int index) {
        return extractKey(paramName, xpath, leftBoundary, rightBoundary, index).matches(assertRegex);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言 “(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(SearchType, String, String, int)}方法
     * </p>
     *
     * @param assertRegex   断言规则
     * @param leftBoundary  断言内容左边界
     * @param rightBoundary 断言内容右边界
     * @param index         指定下标内容
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, String leftBoundary, String rightBoundary,
            int index) {
        return assertResponse(assertRegex, leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(SearchType, String, String)}方法
     * </p>
     *
     * @param assertRegex 断言规则
     * @param paramName   搜索变量
     * @param xpath       提取内容xpath
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, String paramName, String xpath) {
        return assertResponse(assertRegex, paramName, xpath, "", "", 0);
    }

    /**
     * 该方法用于对接口响应报文的指定部分内容进行断言
     * <p>
     * <b>注意：</b>断言规则为正则表达式，若断言非正则表达式的内容，需要自行进行转义，例如断言“(”符号，则需要传入“\\(”。
     * 方法中各个参数的解释，可参考{@link #extractKey(SearchType, String)}方法
     * </p>
     *
     * @param assertRegex 断言规则
     * @param paramName   搜索变量
     * @return 断言结果
     * @since autest 3.3.0
     */
    public boolean assertResponse(String assertRegex, String paramName) {
        return assertResponse(assertRegex, paramName, "");
    }

    /**
     * 该方法用于通过指定的条件对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}类型时，其xpath参数不生效，判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为{@link MessageType#XML}或{@link MessageType#HTML}类型时，则优先判断xpath参数，若其为空时，则再判断paramName参数，其判断方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则xpath与paramName参数均不生效</li>
     * </ol>
     * 通过以上方式提取后，若传入了左右边界内容，则继续按照左右边界的方式，再次对已提取的内容进行提取：
     * <ol>
     * <li>若同时未指定左右边界，则不进行边界内容提取</li>
     * <li>当边界提取到多条数据时，则根据指定的index进行提取，其下标从1开始计算（1为第一条元素），若值小于1，则获取第一条数据；若值大于提取到的数据集合数量，则返回最后一条数据</li>
     * <li>左右边界允许为正则表达式</li>
     * </ol>
     * </p>
     *
     * @param paramName     提取内容参数名
     * @param xpath         提取内容xpath
     * @param leftBoundary  提取内容左边界
     * @param rightBoundary 提取内容右边界
     * @param index         边界提取到多条内容时指定的获取下标
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(String paramName, String xpath, String leftBoundary, String rightBoundary, int index) {
        // 需要提词的内容
        String value = "";
        // 提词内容的格式集合，由于在父类中已对null进行处理，故此处无需进行初始化
        return extractKey(value, messageTypeSet, paramName, xpath, leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于通过指定的边界条件对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>若同时未指定左右边界，则不进行边界内容提取</li>
     * <li>当边界提取到多条数据时，则根据指定的index进行提取，其下标从1开始计算（1为第一条元素），若值小于1，则获取第一条数据；若值大于提取到的数据集合数量，则返回最后一条数据</li>
     * <li>左右边界允许为正则表达式</li>
     * </ol>
     * </p>
     *
     * @param leftBoundary  提取内容左边界
     * @param rightBoundary 提取内容右边界
     * @param index         边界提取到多条内容时指定的获取下标
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(String leftBoundary, String rightBoundary, int index) {
        return extractKey("", "", leftBoundary, rightBoundary, index);
    }

    /**
     * 该方法用于通过指定的搜索参数对接口响应报文指定内容进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}类型时，其xpath参数不生效，判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为{@link MessageType#XML}或{@link MessageType#HTML}类型时，则优先判断xpath参数，若其为空时，则再判断paramName参数，其判断方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则xpath与paramName参数均不生效</li>
     * </ol>
     * </p>
     *
     * @param paramName  提取内容参数名
     * @param xpath      提取内容xpath
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(String paramName, String xpath) {
        return extractKey(paramName, xpath, "", "", 0);
    }

    /**
     * 该方法用于通过指定的搜索参数对响应体进行提取，返回提取到的内容
     * <p>
     * 提取规则如下：
     * <ol>
     * <li>当响应体为{@link MessageType#JSON}或{@link MessageType#XML}或{@link MessageType#HTML}类型时，其判断paramName参数的方式与{@link SearchType#HEADER}类似</li>
     * <li>当响应体为其他类型时，则paramName参数均不生效</li>
     * </ol>
     * </p>
     *
     * @param paramName 提取内容参数名
     * @return 对响应体提取到的内容
     * @since autest 3.3.0
     */
    public String extractKey(String paramName) {
        return extractKey(paramName, "", "", "", 0);
    }
}
