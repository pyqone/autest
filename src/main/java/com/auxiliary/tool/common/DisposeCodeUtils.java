package com.auxiliary.tool.common;

import com.auxiliary.tool.regex.RegexType;

/**
 * <p>
 * <b>文件名：DisposeCodeUtils.java</b>
 * </p>
 * <p>
 * <b>用途：</b> 集合在编码时需要对文本等数据时经常用到的方法，通过该类可统一对所有相关的方法进行统一维护
 * </p>
 * <p>
 * <b>编码时间：2022年5月17日 下午3:59:33
 * </p>
 * <p>
 * <b>修改时间：2022年5月17日 下午3:59:33
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class DisposeCodeUtils {
    /**
     * 该方法用于对文本进行去正则特殊符号处理，使文本整体能在正则判断中不被转义
     * <p>
     * 例如，存在一段文本“test()”，若直接放入{@link String#matches(String)}中时，文本中的“()”会被识别为正则
     * 中的特殊符号，导致返回结果不符合预期；通过该方法处理后，字符串将变为“test\\(\\)”此时便可直接将整体当做判断为本进行判断
     * </p>
     *
     * @param text 待处理文本
     * @return 处理后的文本
     * @since autest 3.3.0
     */
    public static String disposeRegexSpecialSymbol(String text) {
        StringBuilder newText = new StringBuilder();
        // 对当前字符串逐字遍历
        for (int i = 0; i < text.length(); i++) {
            String charStr = text.substring(i, i + 1);
            // 若遍历的字符为正则中需要转义的特殊字符，则对该内容进行转义
            if (charStr.matches(RegexType.REGEX_ESC.getRegex())) {
                newText.append("\\" + charStr);
            } else {
                newText.append(charStr);
            }
        }

        return newText.toString();
    }
}
