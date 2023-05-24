package com.auxiliary.tool.common;

import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;

/**
 * <p>
 * <b>文件名：AddPlaceholder.java</b>
 * </p>
 * <p>
 * <b>用途：</b>定义使用占位符工具的类必须实现的方法
 * </p>
 * <p>
 * <b>编码时间：2023年5月23日 下午3:09:39
 * </p>
 * <p>
 * <b>修改时间：2023年5月23日 下午3:09:39
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public interface AddPlaceholder {
    /**
     * 该方法用于添加被替换的词语以及替换的内容
     * 
     * @param word        需要替换的内容
     * @param replaceWord 替换后的内容
     * @since autest 4.2.0
     */
    public void addReplaceWord(String word, String replaceWord);

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
     * }
     * </pre></code>
     * </p>
     * <p>
     * 部分定义方法可调用工具类{@link Functions}类获取，以其中一个方法为例，其传参方法为如下： <code><pre>
     * {@link DataDriverFunction} driverFunction = {@link Functions#randomCarId()};
     * addReplaceFunction({@link DataDriverFunction#getRegex()}, {@link DataDriverFunction#getFunction()});
     * </pre></code>
     * </p>
     * <p>
     * 
     * @param regex    需要替换的内容正则表达式
     * @param function 替换词语使用的函数
     * @since autest 4.2.0
     */
    public void addReplaceFunction(String regex, DataFunction function);
}
