package com.auxiliary.tool.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.auxiliary.AuxiliaryToolsException;
import com.auxiliary.tool.common.enums.MathematicsSymbolType;
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
 * <b>修改时间：2022年12月12日 上午8:49:01
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

    /**
     * 该方法用于对自定义的下标进行处理，返回可用的数组下标的方法
     * <p>
     * 处理下标的规则如下：
     * <ol>
     * <li>当下标绝对值在最小下标与最大下标之间，且下标为正数时，则直接返回传入下标与数组下标的差值（下面简称真实下标）</li>
     * <li>当下标绝对值在最小下标与最大下标之间，下标为负数，且不允许反序遍历时，则按照小于最小值处理</li>
     * <li>当下标绝对值在最小下标与最大下标之间，下标为负数，且允许反序遍历时，则返回反序遍历后得到的真实下标</li>
     * <li>当下标绝对值大于最大值，且下标为正数时，则根据大值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最大下标对应的真实下标</li>
     * <li>当下标绝对值大于最大值，且下标为负数时，则根据小值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最小下标对应的真实下标</li>
     * <li>当下标绝对值小于最小值，且下标为正数时，则根据小值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最小下标对应的真实下标</li>
     * <li>当下标绝对值小于最小值，下标为负数，且不允许反序遍历时，则根据小值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最小下标对应的真实下标</li>
     * <li>当下标绝对值小于最小值，下标为负数，且允许反序遍历时，则根据大值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最大下标对应的真实下标</li>
     * </ol>
     * </p>
     * <p>
     * <b>注意：</b>最大、最小或下标差值小于等于0，且最小下标不能小于差值，否则将抛出异常
     * </p>
     * 
     * @param index                   待处理下标
     * @param minIndex                自定义最小下标
     * @param maxIndex                自定义最大下标
     * @param arrayIndexDiff          自定义下标与数组下标的差值
     * @param isReverseOrderTraversal 是否允许反序遍历
     * @param isMinEmptyIndexRandom   小于最小下标时是否返回随机下标
     * @param isMaxEmptyIndexRandom   大于最大下标时是否返回随机下标
     * @param isThrowException        超出下标界限时是否抛出异常
     * @return 程序可处理的数组下标
     * @since autest 3.5.0
     * @throws DisposeNumberException 非正常下标或最大、最小、下标差值传入有误时抛出的异常
     */
    public static int customizedIndex2ArrayIndex(int index, int minIndex, int maxIndex, int arrayIndexDiff,
            boolean isReverseOrderTraversal, boolean isMinEmptyIndexRandom, boolean isMaxEmptyIndexRandom,
            boolean isThrowException) {
        // 对下标进行初步判断
        DisposeNumberException.compareNumber("最大下标", maxIndex, "最小下标", minIndex, MathematicsSymbolType.LESS);
        DisposeNumberException.compareNumber("最大下标", maxIndex, "", 0, MathematicsSymbolType.LESS_AND_EQUALS);
        DisposeNumberException.compareNumber("最小下标", minIndex, "", 0, MathematicsSymbolType.LESS_AND_EQUALS);
        DisposeNumberException.compareNumber("数组差值", arrayIndexDiff, "", 0, MathematicsSymbolType.LESS_AND_EQUALS);
        DisposeNumberException.compareNumber("最小下标", minIndex, "数组差值", arrayIndexDiff,
                MathematicsSymbolType.LESS);

        // 存储计算后的下标
        int calcIndex = minIndex;

        // 计算随机数字
        int randomNumber = new Random().nextInt(maxIndex - minIndex + 1) + minIndex;
        // 若允许反序遍历，则计算下标的绝对值， 并存储当前参数是否为负数
        boolean isMinusIndex = index < 0;
        int absIndex = isReverseOrderTraversal ? Math.abs(index) : index;

        // 判断下标绝对值小于最大小标且大于最小小标的情况
        if (absIndex <= maxIndex && absIndex >= minIndex) {
            // 判断指定下标是否为非负数
            if (!isMinusIndex) {
                // 若下标为非负数，则根据下标是否为数组实际下标，对当前下标进行处理，并返回
                calcIndex = index;
            } else {
                calcIndex = maxIndex + index + minIndex;
            }
        } else if (absIndex > maxIndex) { // 判断下标绝对值大于最大下标的情况
            // 判断是否需要抛出异常
            if (isThrowException) {
                throw new DisposeNumberException(String.format("指定下标“%d”的绝对值大于最大可用下标“%d”", index, maxIndex));
            }
            calcIndex = isMaxEmptyIndexRandom ? randomNumber : maxIndex;
        } else { // 判断下标绝对值小于最小下标的情况
            // 判断是否需要抛出异常
            if (isThrowException) {
                throw new DisposeNumberException(String.format("指定下标“%d”的绝对值小于最小可用下标“%d”", index, minIndex));
            }

            // 判断当前下标是否为负数，并判断是否允许反序遍历
            // 说明，此处存在以下情况：
            // 1. 下标为正数，则不考虑是否允许反序遍历，直接按照最小值返回
            // 2. 下标为负数，且不允许反序遍历，则按照最小值返回
            // 3. 下标为负数，且允许反序遍历，则按照最大值返回
            if (!isMinusIndex || !isReverseOrderTraversal) {
                // 若为非负数，则判断是否最小值随机，需要随机，则返回随机数；反之，返回最小下标
                calcIndex = isMinEmptyIndexRandom ? randomNumber : minIndex;
            } else {
                // 若为负数，则判断是否最大值随机，则返回随机数；反之，返回最大下标
                calcIndex = isMaxEmptyIndexRandom ? randomNumber : maxIndex;
            }
        }

        return calcIndex - arrayIndexDiff;
    }

    /**
     * 该方法用于对数组下标进行处理的方式
     * <p>
     * 处理下标的规则如下：
     * <ol>
     * <li>当下标在最小下标与最大下标之间时，则直接返回传入下标</li>
     * <li>当下标大于最大值时，则根据大值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最大下标对应的真实下标</li>
     * <li>当下标小于最小值时，则根据小值是否随机返回，允许随机，则返回随机的真实下标；否则，返回最小下标对应的真实下标</li>
     * </ol>
     * </p>
     * <p>
     * <b>注意：</b>最大小于等于0，或最小值小于0，或最大值小于最小值时，将抛出异常
     * </p>
     * 
     * @param index                 指定下标
     * @param minIndex              最小下标
     * @param maxIndex              最大下标
     * @param isMinEmptyIndexRandom 是否最小值随机
     * @param isMaxEmptyIndexRandom 是否最大值随机
     * @param isThrowException      是否抛出异常
     * @return 处理后的下标
     * @since autest 3.5.0
     */
    public static int disposeArrayIndex(int index, int minIndex, int maxIndex, boolean isMinEmptyIndexRandom,
            boolean isMaxEmptyIndexRandom, boolean isThrowException) {
        // 对最大、最小下标进行简单的判断
        DisposeNumberException.compareNumber("最大下标", maxIndex, "最小下标", minIndex, MathematicsSymbolType.LESS);
        DisposeNumberException.compareNumber("最大下标", maxIndex, "", 0, MathematicsSymbolType.LESS_AND_EQUALS);
        DisposeNumberException.compareNumber("最小下标", minIndex, "", 0, MathematicsSymbolType.LESS);

        int randomNum = new Random().nextInt(maxIndex - minIndex + 1) + minIndex;

        // 判断下标在0之间的情况
        if (index >= minIndex && index <= maxIndex) {
            return index;
        } else if (index > maxIndex) { // 判断大于最大下标的情况
            if (isThrowException) {
                throw new DisposeNumberException(String.format("指定下标“%d”超出数组最大下标“%d”", index, maxIndex));
            } else {
                return isMaxEmptyIndexRandom ? randomNum : maxIndex;
            }
        } else { // 判断小于0的情况
            if (isThrowException) {
                throw new DisposeNumberException(String.format("指定下标“%d”小于数组最小下标“%d”", index, minIndex));
            } else {
                return isMinEmptyIndexRandom ? randomNum : minIndex;
            }
        }
    }

    /**
     * 该方法用于查找符号的在字符串中的下标值
     * 
     * @param text                  查找字符串
     * @param findStr               待查找字符
     * @param transferredMeaningStr 转义字符
     * @return 待查找字符在字符串中的位置
     * @since autest 3.5.0
     */
    public static int calcExtendStrIndex(String text, String findStr, String transferredMeaningStr) {
        // 进行字符串的判断
        if (text == null || text.isEmpty()) {
            throw new AuxiliaryToolsException("未指定待查找字符串");
        }
        if (findStr == null || findStr.isEmpty()) {
            throw new AuxiliaryToolsException("未指定待查找符号");
        }
        if (transferredMeaningStr == null || transferredMeaningStr.isEmpty()) {
            throw new AuxiliaryToolsException("未指定转义字符");
        }

        // 获取待查找符号在字符串中的下标
        int index = text.indexOf(findStr);
        // 定义累计下标值
        int sumIndex = 0;

        // 循环，直到查找下标小于0为止
        while (index >= 0) {
            // 判断当前下标是否在第一位，若为第一位，则直接返回累计下标
            if (index == 0) {
                return sumIndex;
            }
            // 判断当前下标前一个字符是否为转义字符，若不为转义字符，则返回累计下标加上获取到的下标
            if (!Objects.equals(String.valueOf(text.charAt(index - 1)), transferredMeaningStr)) {
                return sumIndex + index;
            }

            // 若未返回下标，则从当前获取到的下标处，截取字符串，形成新的判断字符串
            text = text.substring(index + findStr.length());
            // 累计下标值，“-1”表示由于获取的为下标，故再加上长度时，需要减去1才能得到下标；“+1”表示每次获取下标值时，都从0开始计算，故累计时需要加上1
            sumIndex += ((index + findStr.length() - 1) + 1);
            // 获取新串中查找字符串的位置
            index = text.indexOf(findStr);
        }

        // 若通过循环仍未返回下标值，则返回-1，表示不存在需要查找的内容
        return -1;
    }

    /**
     * 该方法用于对文本中的占位符进行替换的方法
     * 
     * @param startSign     占位符起始标识
     * @param endSign       占位符结束标识
     * @param text          待替换的文本
     * @param replaceKeyMap 待替换的占位符及相应的词语
     * @return 替换后的文本
     * @since autest 3.6.0
     */
    public static String replacePlaceholder(String startSign, String endSign, String text,
            Map<String, String> replaceKeyMap) {
        // 判断需要才处理的内容是否为空或是否包含公式标志，若存在，则返回content
        String startSignRegex = disposeRegexSpecialSymbol(startSign);
        String endSignRegex = disposeRegexSpecialSymbol(endSign);
        String placeholder = startSignRegex + ".+?" + endSignRegex;
        if (text == null || text.isEmpty()) {
            return text;
        }

        // 通过函数标志对文本中的函数或方法进行提取
        Matcher m = Pattern.compile(placeholder).matcher(text);
        while (m.find()) {
            // 去除标记符号，获取关键词
            String signKey = m.group();
            String key = signKey.substring((signKey.indexOf(startSign) + startSign.length()),
                    signKey.lastIndexOf(endSign));

            // 判断关键词是否为已存储的词语
            if (replaceKeyMap.containsKey(key)) {
                text = text.replaceAll(DisposeCodeUtils.disposeRegexSpecialSymbol(signKey), replaceKeyMap.get(key));
                continue;
            }
        }

        return text;
    }

    /**
     * 该方法用于获取指定占位符中的内容
     * 
     * @param startSign 占位符开始标志
     * @param endSign   占位符结束标志
     * @param keyRegex  占位符中的关键词正则表达式
     * @param text      查找的文本
     * @return 占位符中的内容
     * @since autest 3.6.0
     */
    public static String extractPlaceholderContent(String startSign, String endSign, String keyRegex,
            String text) {
        String placeholder = disposeRegexSpecialSymbol(startSign) + keyRegex + disposeRegexSpecialSymbol(endSign);

        // 通过函数标志对文本中的函数或方法进行提取
        Matcher m = Pattern.compile(placeholder).matcher(text);
        if (m.find()) {
            // 获取关键词
            String key = m.group();
            // 去除标记符号并进行存储
            return key.substring((key.indexOf(startSign) + startSign.length()), key.lastIndexOf(endSign));
        }

        return "";
    }

    /**
     * 该方法用于对枚举文本所有内容转换为大写字母处理，并最终将其转换为指定的枚举类
     * <p>
     * 若枚举转换失败，则根据指定的参数来判断是否抛出异常，若不抛出异常，则返回null
     * </p>
     * 
     * @param <T>              枚举类对象
     * @param enumClass        枚举类{@link Class}对象
     * @param typeText         枚举文本
     * @param mapper           文本特殊处理方式
     * @param isThrowException 是否需要抛出异常
     * @return 文本转换后的枚举类对象
     * @since autest 3.7.0
     */
    public static <T> T disposeEnumTypeText(Class<T> enumClass, String typeText, Function<String, String> mapper,
            boolean isThrowException) {
        return disposeEnumTypeText(enumClass, typeText, mapper, isThrowException, (short) 1);
    }

    /**
     * 该方法用于对枚举文本根据指定转换方式进行转换处理，并最终将其转换为指定的枚举类
     * <p>
     * 若枚举转换失败，则根据指定的参数来判断是否抛出异常，若不抛出异常，则返回null；文本转换的方式可通过“changeTypeText”参数控制：
     * <ol>
     * <li>当传入“-1”时，则将枚举文本转换为小写</li>
     * <li>当传入“1”时，则将枚举文本转换为大写</li>
     * <li>当传入其他数值时，则不进行转换</li>
     * </ol>
     * </p>
     * 
     * @param <T>              枚举类对象
     * @param enumClass        枚举类{@link Class}对象
     * @param typeText         枚举文本
     * @param mapper           文本特殊处理方式
     * @param isThrowException 是否需要抛出异常
     * @param changeTypeText   文本转换方式
     * @return 文本转换后的枚举类对象
     * @since autest 3.8.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T disposeEnumTypeText(Class<T> enumClass, String typeText, Function<String, String> mapper,
            boolean isThrowException, short changeTypeText) {
        // 存放报错信息
        String exceptionMessgae = "";

        // 对枚举文本进行处理
        Optional<String> typeTextOptional = Optional.ofNullable(typeText).filter(text -> !typeText.isEmpty())
                .map(mapper::apply).map(text -> {
                    // 根据转换方式参数，对元素文本进行转换
                    switch (changeTypeText) {
                    case -1:
                        return text.toLowerCase();
                    case 1:
                        return text.toUpperCase();
                    case 0:
                    default:
                        return text;
                    }
                });
        if (typeTextOptional.isPresent()) {
            // 获取枚举类的所有枚举
            Object[] enmus = enumClass.getEnumConstants();
            // 若枚举为空，则不进行处理
            if (enmus != null && enmus.length != 0) {
                try {
                    // 获取“valueOf”方法
                    Method method = enumClass.getMethod("valueOf", String.class);
                    try {
                        // 反射调用方法，将处理后的文本转换为枚举类对象
                        return (T) (method.invoke(enmus[0], typeTextOptional.get()));
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        exceptionMessgae = String.format("文本“%s”无法转换为“%s”枚举类", typeText, enumClass.getName());
                    }
                } catch (NoSuchMethodException | SecurityException e) {
                    exceptionMessgae = String.format("类“%s”不存在“valueOf(String)”方法", enumClass.getName());
                }
            } else {
                exceptionMessgae = String.format("类“%s”不存在枚举内容", enumClass.getName());
            }
        } else {
            exceptionMessgae = "未指定枚举文本或处理后枚举文本为空： " + typeText;
        }

        // 处理后，若未返回相应的枚举类对象，则根据参数抛出异常或者返回null
        if (isThrowException) {
            throw new IllegalArgumentException(exceptionMessgae);
        } else {
            return null;
        }
    }
}
