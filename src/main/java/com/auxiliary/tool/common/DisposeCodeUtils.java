package com.auxiliary.tool.common;

import java.util.Random;

import com.auxiliary.AuxiliaryToolsException;
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

    public static int disposeIndex(int index, int maxIndex, int minIndex, boolean isArrayIndex,
            boolean isReverseOrderTraversal, boolean isMinEmptyIndexRandom, boolean isMaxEmptyIndexRandom,
            boolean isThrowException) {
        // 对下标进行初步判断
        if (maxIndex < minIndex) {
            throw new AuxiliaryToolsException(String.format("最大下标“%d”不能小于最小下标“%d”", maxIndex, minIndex));
        }
        if (maxIndex <= 0) {
            throw new AuxiliaryToolsException(String.format("最大下标“%d”不能小于等于0", maxIndex));
        }

        // 计算随机数字
        int randomNumber = new Random().nextInt(maxIndex - minIndex + 1) + minIndex;
        // 计算下标的绝对值， 并存储当前参数是否为负数
        boolean isMinusIndex = index < 0;
        int absIndex = Math.abs(index);
        // 若下标为数组下标，且下标为负数时，则下标绝对值需要+1，以满足反序遍历的需求
        absIndex -= ((isMinusIndex && isArrayIndex) ? 1 : 0);

        // 当下标小于最大小标且大于最小小标时，则返回下标与最小下标的差值
        if (absIndex <= maxIndex && absIndex >= minIndex) {
            // 判断指定下标是否为非负数
            if (!isMinusIndex) {
                // 若下标为非负数，则根据下标是否为数组实际下标，对当前下标进行处理，并返回
                return isArrayIndex ? index : (index - minIndex);
            }

            // 若下标为负数，则判断当前是否允许反序遍历
            if (isReverseOrderTraversal) {

            }
        }

        // 当下标大于最大下标时，则判断是否抛出异常，若不抛出异常，则取最大下标减去最小下标
        if (index > maxIndex) {
            if (isThrowException) {
                throw new AuxiliaryToolsException(String.format("指定下标“%d”大于最大可用下标“%d”", index, maxIndex));
            } else {
                if (isMaxEmptyIndexRandom) {
                    return randomNumber;
                } else {
                    return maxIndex;
                }
            }
        }

        // 当下标小于最小下标，且不允许反序遍历时
        if (index < minIndex && index >= (isReverseOrderTraversal ? 0 : Integer.MIN_VALUE)) {
            // 判断是否需要抛出异常
            if (isThrowException) {
                throw new AuxiliaryToolsException(String.format("指定下标“%d”小于最小可用下标“%d”", index, minIndex));
            } else {
                if (isMaxEmptyIndexRandom) {
                    return randomNumber;
                } else {
                    return maxIndex;
                }
            }
        }

        // 当允许反序遍历且下标小于0时
        if (isReverseOrderTraversal && index < 0) {
            // 获取下标的绝对值
            int absIndex = Math.abs(index);

            // 若绝对值大于介于最小和最大下标之间，则
        }


        return 0;
    }
}
