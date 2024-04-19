package com.auxiliary.tool.data;

import java.util.Random;

public class RandomNumber {
    /**
     * 定义每组的数据量，用于在超出数字最大限制时，对数据分组
     *
     * @since autest 5.0.0
     */
    private final static int GROUP_DATA_NUM = 10000;

    /**
     * 用于生成随机数字
     *
     * @since autest 5.0.0
     */
    private static Random random = new Random();

    /**
     * 该方法用于生成指定范围内的随机整数，该整数在int的范围内，可以为负数
     * <p>
     * <b>注意：</b>生成的随机数字可为起始或结束值
     * </p>
     *
     * @param startNum 起始数值
     * @param endNum   结束数值
     * @return 指定范围内容的随机整数
     * @since autest 5.0.0
     */
    public static int randomInteger(int startNum, int endNum) {
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
        int diff = endNum - startNum + 1;
        // 若差值超出整形最大值（超过整形正数最大值后，继续增加，其值将变为负的最大值），则对数据进行分组
        if (diff > 0) {
            // 根据差值，生成一个随机随机值，该随机值即为随机数字所在的起始与结束值区间内的下标
            int randomNum = random.nextInt(diff);
            // 返回随机数
            return randomNum + startNum;
        } else {
            // 使用长整形重新计算样本个数
            long longDiff = Long.valueOf(endNum) - Long.valueOf(startNum) + 1;
            // 对样本数使用分组的组数进行除余，并记录余数（由于分组数不超过整形最大值，固此处可以强转）
            int remainingDataNum = (int) (longDiff % GROUP_DATA_NUM);
            // 根据样本数，计算分组组数，若存在余数，则表示当前需要多分配一组
            int groupNum = (int) (longDiff / GROUP_DATA_NUM) + (remainingDataNum == 0 ? 0 : 1);
            // 根据特定的分组数量，随机产生一个分组的组数，若余数正好为0，则说明指定的分组数能正好完成对数据的分组，若余数不为0，则表示当前需要多分配一组
            int randomGroupIndex = random.nextInt(groupNum);

            // 根据分组数据，重新计算当前组的起始数字与结束数字
            int groupStartNum = startNum + randomGroupIndex * GROUP_DATA_NUM;
            // 结束数字计算需要判断当前组数是否为最后一组，且是否存在余数
            int groupEndNum = 0;
            if (randomGroupIndex == (groupNum - 1) && remainingDataNum != 0) {
                groupEndNum = groupStartNum + remainingDataNum - 1;
            } else {
                groupEndNum = groupStartNum + GROUP_DATA_NUM - 1;
            }

            return randomInteger(groupStartNum, groupEndNum);
        }
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
    public static double randomDouble(double startNum, double endNum, int decimalPlaces) {
        // 若当前保留小数位为整数，则直接调用随机整数方法
        if (decimalPlaces == 0) {
            return randomInteger((int) startNum, (int) endNum) * 1.0;
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
}
