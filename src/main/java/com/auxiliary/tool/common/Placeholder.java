package com.auxiliary.tool.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.tool.regex.ConstType;
import com.auxiliary.tool.regex.RegexType;

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
     * 递归替换的深度
     * 
     * @since autest 4.2.0
     */
    private int replaceDepth = 5;

    /**
     * 存储需要替换的占位符以及占位符相关的替换公式
     */
    private Map<String, DataFunction> replaceFunctionMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);
    /**
     * 存储需要替换的占位符以及占位符相关的替换词语
     */
    private Map<String, String> replaceWordMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

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
     * 该方法用于设置替换占位符的深度（替换次数），避免嵌套替换占位符导致死循环
     * <p>
     * 例如，设置占位符起始与结束标志均为“#”符号，存储占位符“1”的替换内容为“#2#”，存储占位符“2”的替换内容为“#3#”；则当需要替换的内容为“内容：#1#”时：
     * <ol>
     * <li>第一次替换后，文本变为“内容：#2#”</li>
     * <li>第二次替换后，文本变为“内容：#3#”</li>
     * <li>第三次替换后，文本变为“内容：4”</li>
     * </ol>
     * 根据以上替换次数，则说明文本的替换深度为3层（3次替换），若设置深度为2，则替换后的文本将返回“内容：#3#”
     * </p>
     * <p>
     * <b>注意：</b>深度必须设置大于等于0次（0次则表示不替换），若设置的深度为负数，则不会生效，将沿用上一次设置的值（初次设置为负数时，则使用默认次数，默认次数为5次）
     * </ul>
     * </p>
     * 
     * @param count 嵌套替换深度
     * @return 类本身
     * @since autest 4.2.0
     */
    public Placeholder setReplaceDepth(int count) {
        if (count >= 0) {
            replaceDepth = count;
        }

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
     *  // 输出：“内容：53”（两位随机数字）
     *  System.out.println(test.replaceText("内容：#随机：N#"));
     *  // 输出：“内容：谁当”（两位随机中文）
     *  System.out.println(test.replaceText("内容：#随机：C#"));
     * }
     * </pre></code>
     * </p>
     * <p>
     * 部分定义方法可调用工具类{@link Functions}类获取
     * </p>
     * <p>
     * <b>注意：</b>为加快若传入的内容不包含正则相关的内容，则仍按照词语进行存储，其规则可参考枚举类{@link RegexType#REGEX_REGEX}
     * </p>
     *
     * @param functions 替换词语使用的函数
     * @return 类本身
     * @since autest 4.2.0
     */
    public Placeholder addReplaceFunction(DataDriverFunction functions) {
        if (functions == null) {
            return this;
        }

        // 获取正则内容，判断传入的规则中是否包含正则字符，若不包含，则仍按照词语进行存储
        String regex = functions.getRegex();
        if (regex.matches(RegexType.REGEX_REGEX.getRegex())) {
            replaceFunctionMap.put(regex, functions.getFunction());
        } else {
            addReplaceWord(regex, functions.getFunction().apply(regex));
        }

        return this;
    }

    /**
     * 该方法用于添加待替换的词语及相应的替换内容
     * 
     * @param word        待替换的占位符词语
     * @param replaceWord 被替换的内容
     * @return 类本身
     * @since autest 4.2.0
     */
    public Placeholder addReplaceWord(String word, String replaceWord) {
        if (word != null && !word.isEmpty()) {
            replaceWordMap.put(word, replaceWord);
        }

        return this;
    }

    /**
     * 该方法用于对需要替换的词语进行转换，输出该词语对应的替换后的内容
     * <p>
     * <b>注意：</b>传入的词语不能包含占位符的前后标志
     * </p>
     * 
     * @param word 需要替换的词语
     * @return 替换后的内容
     * @since autest 4.2.0
     */
    public String replaceWord(String word) {
        // 替换词语的方法：
        // 1. 先判断当前的词语是否直接可在替换词语集合中找到，即词语替换
        // 2. 若不存在词语替换，则按照正则表达式再次判断，进行替换
        // 词语替换
        if (replaceWordMap.containsKey(word)) {
            return replaceWordMap.get(word);
        }

        // 正则判断替换
        for (String funKey : replaceFunctionMap.keySet()) {
            if (word.matches(funKey)) {
                return replaceFunctionMap.get(funKey).apply(word);
            }
        }

        return word;
    }

    /**
     * 该方法用于对文本中的占位符进行替换
     * 
     * @param text 需要替换内容
     * @return 替换占位符后的内容
     * @since autest 4.2.0
     */
    public String replaceText(String text) {
        // 判断需要才处理的内容是否为空或是否包含公式标志，若存在，则返回content
        String functionSign = String.format("%s.+?%s", startSign, endSign);
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 根据替换深度，循环对词语进行替换；若其内不包含需要替换的内容时，则结束循环
        for (int i = 0; i < replaceDepth && text.matches(String.format(".*%s.*", functionSign)); i++) {
            // 通过函数标志对文本中的函数或方法进行提取
            Matcher m = Pattern.compile(functionSign).matcher(text);
            while (m.find()) {
                // 去除标记符号，获取关键词
                String signKey = m.group();
                String key = signKey.substring(startSign.length(), signKey.length() - signKey.indexOf(endSign));
                // 调用替换词语的方法，对占位符进行替换
                text = text.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(signKey), replaceWord(key));
            }
        }

        return text;
    }
}
