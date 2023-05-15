package com.auxiliary.tool.common;

import java.util.HashMap;

import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：Placeholder.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义占位符替换的方法，以及对占位符指定，以方便占位符替换方法的统一管理
 * </p>
 * <p>
 * <b>编码时间：2023年5月15日 上午10:20:47
 * </p>
 * <p>
 * <b>修改时间：2023年5月15日 上午10:20:47
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public class Placeholder {
    /**
     * 占位符起始标志
     * 
     * @since autest 4.2.0
     */
    private String startSign = "";
    /**
     * 占位符结束标志
     * 
     * @since autest 4.2.0
     */
    private String endSign = "";

    /**
     * 存储需要替换的占位符以及占位符相关的替换公式
     */
    private HashMap<String, DataFunction> replaceWordMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 构造对象，初始化占位符的起始与结束标志
     * 
     * @param startSign 占位符起始标志
     * @param endSign   占位符结束标志
     * 
     * @since autest 4.2.0
     */
    public Placeholder(String startSign, String endSign) {
        setPlaceholderSign(startSign, endSign);
    }

    /**
     * 该方法用于设置占位符的起始与结束标志
     * 
     * @param startSign 占位符起始标志
     * @param endSign   占位符结束标志
     * @return 类本身
     * @since autest 4.2.0
     */
    public Placeholder setPlaceholderSign(String startSign, String endSign) {
        // 将占位符前后标志转换为不被正则转义的内容
        this.startSign = DisposeCodeUtils.disposeRegexSpecialSymbol(startSign);
        this.endSign = DisposeCodeUtils.disposeRegexSpecialSymbol(endSign);

        return this;
    }

    /**
     * 用于添加待替换的词语及相应的替换方法
     * <p>
     * 该方法允许添加待替换词语的处理方式，在写入用例时，若指定的待替换内容符合此方法指定的正则时，则会使用存储的替换方式，
     * 对词语进行替换。例如，占位符前后标志均为“#”，则： <code><pre>
     * {@literal @}Test
     * public void addReplaceWordTest_DataDriverFunction() {
     *  // 定义词语匹配规则和处理方式，当匹配到正则后，将获取“随机：”后的字母
     *  // 若字母为“N”，则随机生成两位数字字符串
     *  // 若字母为“Y”，则随机生成两位中文字符串
     *  test.addReplaceWord(new DataDriverFunction("随机：[NC]", text -&gt; {
     *      return "N".equals(text.split("：")[1]) ? RandomString.randomString(2, 2, StringMode.NUM)
     *              : RandomString.randomString(2, 2, StringMode.CH);
     *  }));
     *
     *  // 随机生成两位数字
     *  System.out.println(test.getContent("内容：#随机：N#"));
     *  // 随机生成两位中文
     *  System.out.println(test.getContent("内容：#随机：C#"));
     * }
     * </pre></code>
     * </p>
     * <p>
     * 部分定义方法可调用工具类{@link Functions}类获取
     * </p>
     *
     * @param functions 替换词语使用的函数
     */
    public Placeholder addReplaceWord(DataDriverFunction functions) {
        if (functions != null) {
            replaceWordMap.put(functions.getRegex(), functions.getFunction());
        }

        return this;
    }
}
