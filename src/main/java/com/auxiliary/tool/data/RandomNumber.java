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
            // 得到差值后，使用如下公式后，可得到随机数字：随机数 = 结束值 - (差值 - 随机值)
            return endNum - (diff - randomNum);
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
            int gropuStartNum = startNum + randomGroupIndex * GROUP_DATA_NUM;
            // 结束数字计算需要判断当前组数是否为最后一组，且是否存在余数
            int gropuEndNum = 0;
            if (randomGroupIndex == (groupNum - 1) && remainingDataNum != 0) {
                gropuEndNum = gropuStartNum + remainingDataNum - 1;
            } else {
                gropuEndNum = gropuStartNum + GROUP_DATA_NUM - 1;
            }

            return randomInteger(gropuStartNum, gropuEndNum);
        }
    }

    /**
     * 该方法用于生成指定数字以上的随机数字，该数字不超过整形数字的正数上限。例如，传入数字“5”，则生成大于等于5且小于等于{@link Integer#MAX_VALUE}的整形数字
     *
     * @param num 数字下限
     * @return 大于等于数字下限的随机整形数字
     * @since autest 5.0.0
     */
    public static int randomAboveInteger(int num) {
        return randomInteger(num, Integer.MAX_VALUE);
    }

    /**
     * 该方法用于生成指定数字以下的随机数字，该数字不超过整形数字的负数上限。例如，传入数字“5”，则生成小于等于5且大于等于{@link Integer#MAX_VALUE}的整形数字
     *
     * @param num 数字下限
     * @return 大于等于数字下限的随机整形数字
     * @since autest 5.0.0
     */
    public static int randomBelowInteger(int num) {
        return randomInteger(num, Integer.MAX_VALUE);
    }
}
