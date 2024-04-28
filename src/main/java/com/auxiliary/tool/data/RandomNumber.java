package com.auxiliary.tool.data;

import java.util.Random;

/**
 * <p>
 * <b>文件名：RandomNumber.java</b>
 * </p>
 * <p>
 * <b>用途：</b>提供简化生成随机数字的工具，通过该工具，可快速生成特定随机数字
 * </p>
 * <p>
 * <b>编码时间：2024年4月28日 上午8:18:17
 * </p>
 * <p>
 * <b>修改时间：2024年4月28日 上午8:18:17
 * </p>
 *
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 5.0.0
 */
public class RandomNumber {
    /**
     * 用于生成随机数字
     *
     * @since autest 5.0.0
     */
    private static Random random = new Random();

    /**
     * 该方法用于生成指定范围内的随机整数，该整数在int的范围内，可以为负数
     * <p>
     * <b>注意：</b>生成的随机数字的范围包含起始值和结束值
     * </p>
     *
     * @param startNum 起始数值
     * @param endNum   结束数值
     * @return 指定范围内容的随机整数
     * @since autest 5.0.0
     */
    public static int randomIntegerNumber(int startNum, int endNum) {
        return randomIntegerNumber(startNum, endNum, 0);
    }

    /**
     * 该方法用于生成指定范围内特定间隔下的随机整数，该整数在int的范围内，可以为负数
     * <p>
     * 例如，指定生成随机数生成的范围为1~10，若间隔为1，则表示在1~10范围内，从起始值开始，每间隔一个数字取一个值，即实际的生成的随机数范围为1、3、5、7、9
     * </p>
     * <p>
     * <b>注意：</b>生成的随机数字的范围包含起始值和结束值
     * </p>
     *
     * @param startNum 起始数值
     * @param endNum   结束数值
     * @param step     间隔数值
     * @return 指定范围内容的随机整数
     * @since autest 5.0.0
     */
    public static int randomIntegerNumber(int startNum, int endNum, int step) {
        // 若起始值与结束值相等，则直接返回起始值
        if (startNum == endNum) {
            return startNum;
        }

        // 判断起始值是否大于结束值
        if (startNum > endNum) {
            int temp = startNum;
            startNum = endNum;
            endNum = temp;
        }

        // 计算起始值与结束值的差值（样本个数）
        long diff = Long.valueOf(endNum) - Long.valueOf(startNum) + 1;
        // 判断当前是否存在步长，若存在步长数据，则计算除去步长内的数字后的样本个数
        if (step > 0 && step < diff) {
            diff = diff / (step + 1) + (diff % (step + 1) == 0 ? 0 : 1);
        }

        // 若差值超出整形最大值（超过整形正数最大值后，继续增加，其值将变为负的最大值），则对数据进行分组
        if (diff <= Integer.MAX_VALUE) {
            // 根据差值，生成一个随机随机值，该随机值即为随机数字所在的起始与结束值区间内的下标
            int randomNum = random.nextInt((int) diff);
            // 返回随机数
            return startNum + (randomNum * (step > 0 ? (step + 1) : 1));
        } else {
            return (int) randomLongNumber(startNum, endNum, step);
        }
    }

    /**
     * 该方法用于生成指定范围内的随机long类型整数，可以为负数
     * <p>
     * <b>注意：</b>生成的随机数字的范围包含起始值和结束值，起始值与结束值差值不可超过{@link Long#MAX_VALUE}，否则会抛出异常
     * </p>
     *
     * @param startNum 起始值
     * @param endNum   结束值
     * @return 范围的随机long类型整数
     * @since autest 5.0.0
     * @throws IllegalDataException 当起始值与结束值差值超过{@link Long#MAX_VALUE}时，抛出的异常
     */
    public static long randomLongNumber(long startNum, long endNum) {
        return randomLongNumber(startNum, endNum, 0);
    }

    /**
     * 该方法用于生成指定范围内特定间隔下的随机long类型整数，可以为负数
     * <p>
     * 例如，指定生成随机数生成的范围为1~10，若间隔为1，则表示在1~10范围内，从起始值开始，每间隔一个数字取一个值，即实际的生成的随机数范围为1、3、5、7、9
     * </p>
     * <p>
     * <b>注意：</b>生成的随机数字的范围包含起始值和结束值，起始值与结束值差值不可超过{@link Long#MAX_VALUE}，否则会抛出异常
     * </p>
     *
     * @param startNum 起始值
     * @param endNum   结束值
     * @param step     间隔数值
     * @return 范围的随机long类型整数
     * @since autest 5.0.0
     * @throws IllegalDataException 当起始值与结束值差值超过{@link Long#MAX_VALUE}时，抛出的异常
     */
    public static long randomLongNumber(long startNum, long endNum, long step) {
        // 若起始值与结束值相等，则直接返回起始值
        if (startNum == endNum) {
            return startNum;
        }

        // 判断起始值是否大于结束值
        if (startNum > endNum) {
            long temp = startNum;
            startNum = endNum;
            endNum = temp;
        }

        // 计算起始值与结束值的差值（样本个数）
        long diff = Long.valueOf(endNum) - Long.valueOf(startNum) + 1;
        // 判断当前是否存在步长，若存在步长数据，则计算除去步长内的数字后的样本个数
        if (step > 0 && step < diff) {
            diff = diff / (step + 1) + (diff % (step + 1) == 0 ? 0 : 1);
        }

        if (diff <= 0) {
            throw new IllegalDataException("当前起始值与结束值差值超出long类型最大值，请重新设置起始值与结束值");
        }

        // 生成指定范围的 long 类型随机值并加上区间下限
        long randomNum = Math.round(random.nextDouble() * diff) * (step + 1) + startNum;
        // 由于小数位计算精度问题，最终计算结果可能会大于最大值，故需要进行判断，当生成值大于最大值时，则返回最大值
        return randomNum > endNum ? endNum : randomNum;
    }

    /**
     * 该方法用于生成指定范围内的随机小数，并且根据指定的小数位数进行四舍五入。若小数位小于0，则表示不进行四舍五入处理
     *
     * @param startNum      起始数值
     * @param endNum        结束数值
     * @param decimalPlaces 保留小数位数
     * @return 随机小数
     * @since autest 5.0.0
     */
    public static double randomDoubleNumber(double startNum, double endNum, int decimalPlaces) {
        // 若当前保留小数位为整数，则直接调用随机整数方法
        if (decimalPlaces == 0) {
            return randomIntegerNumber((int) startNum, (int) endNum) * 1.0;
        }

        // 若起始值与结束值相等，则直接返回起始值
        if (startNum == endNum) {
            return startNum;
        }

        // 判断起始值是否大于结束值
        if (startNum > endNum) {
            double temp = startNum;
            startNum = endNum;
            endNum = temp;
        }

        // 生成随机小数
        double randomNum = random.nextDouble() * (endNum - startNum) + startNum;
        // 判断当前是否需要保留小数位
        if (decimalPlaces > 0) {
            // 对随机数进行小数位四舍五入处理
            randomNum = Math.round(randomNum * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
        }
        return randomNum;
    }

    /**
     * 该方法用于生成指定范围内的随机奇数，可为负数
     * <p>
     * <b>注意：</b>起始值与结束值可为偶数，但不能全为偶数
     * </p>
     *
     * @param startNum 起始值
     * @param endNum   结束值
     * @return 指定范围内的随机奇数
     * @since autest 5.0.0
     * @throws IllegalDataException 当且仅当起始值与结束值都为偶数时，抛出的异常
     */
    public static int randomOddIntegerNumber(int startNum, int endNum) {
        // 若起始值等于结束值，且两个值均为偶数，则抛出异常
        if (startNum == endNum && startNum % 2 == 0) {
            throw new IllegalDataException("当前起始值与结束值相等且为偶数，无法生成奇数");
        }
        // 若起始值非奇数，则将起始值加1
        if (startNum % 2 == 0) {
            startNum += 1;
        }

        return randomIntegerNumber(startNum, endNum, 1);
    }

    /**
     * 该方法用于生成指定范围内的随机偶数，可为负数
     * <p>
     * <b>注意：</b>起始值与结束值可为奇数，但不能全为奇数
     * </p>
     *
     * @param startNum 起始值
     * @param endNum   结束值
     * @return 指定范围内的随机偶数
     * @since autest 5.0.0
     * @throws IllegalDataException 当且仅当起始值与结束值都为奇数时，抛出的异常
     */
    public static int randomEvenIntegerNumber(int startNum, int endNum) {
        // 若起始值等于结束值，且两个值均为奇数，则抛出异常
        if (startNum == endNum && startNum % 2 == 1) {
            throw new IllegalDataException("当前起始值与结束值相等且为奇数，无法生成偶数");
        }
        // 若起始值非偶数，则将起始值加1
        if (Math.abs(startNum) % 2 == 1) {
            startNum += 1;
        }

        return randomIntegerNumber(startNum, endNum, 1);
    }
}
