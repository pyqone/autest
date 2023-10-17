package com.auxiliary.http;

import java.util.HashSet;
import java.util.Set;

import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.tool.common.AddPlaceholder;
import com.auxiliary.tool.common.Placeholder;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：EasyRequest.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义请求类工具基本的请求方法
 * </p>
 * <p>
 * <b>编码时间：2023年10月17日 上午8:13:55
 * </p>
 * <p>
 * <b>修改时间：2023年10月17日 上午8:13:55
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.5.0
 */
public abstract class EasyRequest implements AddPlaceholder {
    /**
     * 占位符起始符号
     *
     * @since autest 4.5.0
     */
    protected final String FUNCTION_START_SIGN = "@{";
    /**
     * 占位符结束符号
     *
     * @since autest 4.5.0
     */
    protected final String FUNCTION_END_SIGN = "}";

    /**
     * 占位符标记
     *
     * @since autest 4.5.0
     */
    protected final String FUNCTION_SIGN = FUNCTION_START_SIGN + ".+?" + FUNCTION_END_SIGN;

    /**
     * 存储断言结果
     *
     * @since autest 4.5.0
     */
    protected Set<String> assertResultSet = new HashSet<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储占位符类对象
     *
     * @since autest 4.5.0
     */
    protected Placeholder placeholder = new Placeholder(FUNCTION_START_SIGN, FUNCTION_END_SIGN);

    /**
     * 断言失败是否抛出异常
     *
     * @since autest 4.5.0
     */
    protected boolean isAssertFailThrowException = false;
    /**
     * 定义调用接口前是否自动调用前置操作
     *
     * @since autest 4.5.0
     */
    protected boolean isAutoBeforeOperation = true;

    /**
     * 该方法用于根据接口信息，对接口进行请求，并返回响应内容
     *
     * @param interInfo 接口信息类对象
     * @return 接口响应类
     * @since autest 4.5.0
     */
    public abstract EasyResponse requst(InterfaceInfo interInfo);

    /**
     * 该方法用于添加数据处理函数
     * <p>
     * 可通过lambda添加公式对数据处理的方式，例如，将文本中的存在的"a()"全部替换为文本“test”，则可按如下写法： <code><pre>
     * addFunction(new DataDriverFunction("a\\(\\)", text -&gt; "test"));
     * </pre></code>
     * </p>
     * <p>
     * 可添加{@link Functions}类中预设的函数
     * </p>
     *
     * @param functions 数据处理函数
     * @return 类本身
     * @since autest 4.5.0
     */
    public EasyRequest addFunction(DataDriverFunction functions) {
        if (functions != null) {
            placeholder.addReplaceFunction(functions.getRegex(), functions.getFunction());
        }

        return this;
    }

    /**
     * 该方法用于返回指定待替换关键词的内容
     *
     * @param key 待替换关键词
     * @return 替换内容
     * @since autest 4.5.0
     */
    public String getReplaceKey(String key) {
        return placeholder.replaceWord(key);
    }

    /**
     * 该方法用于设置自动断言失败时，是否需要抛出异常
     *
     * @param isAssertFailThrowException 断言失败是否抛出异常
     * @return 类本身
     * @since autest 4.5.0
     */
    public EasyRequest setAssertFailThrowException(boolean isAssertFailThrowException) {
        this.isAssertFailThrowException = isAssertFailThrowException;
        return this;
    }

    /**
     * 该方法用于设置在请求接口时，是否自动调用接口的前置操作
     *
     * @param isAutoBeforeOperation 是否自动调用接口的前置操作
     * @return 类本身
     * @since autest 4.5.0
     */
    public EasyRequest setAutoBeforeOperation(boolean isAutoBeforeOperation) {
        this.isAutoBeforeOperation = isAutoBeforeOperation;
        return this;
    }

    /**
     * 该方法用于返回请求接口后自动断言的结果集合
     *
     * @return 断言结果集合
     * @since autest 4.5.0
     */
    public Set<String> getAssertResult() {
        return assertResultSet;
    }

    @Override
    public void addReplaceFunction(String regex, DataFunction function) {
        placeholder.addReplaceFunction(regex, function);
    }

    @Override
    public void addReplaceWord(String word, String replaceWord) {
        placeholder.addReplaceWord(word, replaceWord);
    }
}
