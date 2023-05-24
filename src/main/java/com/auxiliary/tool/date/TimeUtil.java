package com.auxiliary.tool.date;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * <p>
 * <b>文件名：</b>TimeUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 提供对{@link Time}类进行扩展的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年1月22日下午7:52:10
 * </p>
 * <p>
 * <b>修改时间：</b>2022年3月21日 下午5:28:28
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class TimeUtil {
    /**
     * 私有构造，避免创建对象
     * 
     * @since autest 2.0.0
     */
    private TimeUtil() {
    }

    /**
     * 用于随机生成一个起始时间与结束时间范围内的一个时间
     * <p>
     * 方法可通过传入的单位来限制随机产生的时间最小的变动单位，例如： <code><pre>
     * Time startTime = Time.parse("2020-12-20 00:00:00");
     * Time endTime = Time.parse("2020-12-22 00:00:00");
     * 
     * TimeUtil.randomTime(startTime, endTime, TimeUnit.SECONDS)
     * TimeUtil.randomTime(startTime, endTime, TimeUnit.DAYS)
     * </pre></code> 则在以上代码中，第一次调用方法可能得到一个“2020-12-21 17:31:22”的时间或其他的可能，但第二次调用方法
     * 仅得到“2020-12-20 00:00:00”或“2020-12-21 00:00:00”两种结果
     * </p>
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>在生成随机时间的范围中，允许生成起始时间，但不会生成结束时间</li>
     * <li>在随机时间中，不变的时间与起始时间一致。例如，最小的变动单位为“日”，则在生成的时间中，其时间的 时、分、秒的数值都与起始时间保持一致</li>
     * <li>若起始时间大于结束时间，则会自动对时间进行调换；若起始时间与结束时间一致，则直接返回起始时间</li>
     * </ol>
     * </p>
     * 
     * @param startTime 随机时间的起始时间
     * @param endTime   随机时间的结束时间
     * @param timeUnit  随机时间最小变动单位
     * @return 随机的时间
     * @since autest 2.0.0
     */
    public static Time randomTime(Time startTime, Time endTime, TimeUnit timeUnit) {
        // 判断传入的对象是否为空
        if (startTime == null || endTime == null || timeUnit == null) {
            throw new IncorrectConditionException(String
                    .format("必须指定时间范围与最小变动单位：[startTime:%s, endTime:%s, timeUnit:%s]", startTime, endTime, timeUnit));
        }

        // 若开始时间大于结束时间，则交换两个时间；若一致，则直接返回起始时间
        int compareResult = startTime.compareTo(endTime);
        if (compareResult > 0) {
            Time tempTime = startTime;
            startTime = endTime;
            endTime = tempTime;
        } else if (compareResult == 0) {
            return startTime;
        } else {
        }

        // 获取开始时间与结束时间的毫秒值，之后通过指定的单位对毫秒值进行转换
        long startTime2Unit = startTime.getMilliSecond() / timeUnit.getToMillisNum();
        // 若结束时间与起始时间相同，则在结束时间的毫秒值上加上1
        long endTime2Unit = endTime.getMilliSecond() / timeUnit.getToMillisNum();

        // 判断当前转换单位后，得到的结果是否一致，若结果一致，则仍无需计算随机时间
        Time randomTime;
        if (startTime2Unit != endTime2Unit) {
            // 生成随机的经转换的数值，并乘以单位乘积，转换成毫秒值后，再将毫秒值转换为时间
            randomTime = Time.parse((new Random().longs(startTime2Unit, endTime2Unit).findAny().orElse(startTime2Unit))
                    * timeUnit.getToMillisNum());

            // 为保证精度，则根据最小变动的单位，将起始时间的下级单位的数值赋给生成的时间
            LocalDateTime startTimeLocal = startTime.getLocalDateTime();
            switch (timeUnit) {
            case YEAR:
                randomTime.setTime(startTimeLocal.getMonthValue(), TimeUnit.MONTH);
            case MONTH:
                // 若当前月份无开始时间指向的日期（一般为本月最后一天），则将日期改为当月的最后一天
                try {
                    randomTime.setTime(startTimeLocal.getDayOfMonth(), TimeUnit.DAY);
                } catch (IncorrectConditionException e) {
                    randomTime = Time.parse(InitTimeUtil.lastDayOfMonth(false, randomTime.getLocalDateTime()));
                }
            case WEEK:
            case DAY:
                randomTime.setTime(startTimeLocal.getHour(), TimeUnit.HOUR);
            case HOUR:
                randomTime.setTime(startTimeLocal.getMinute(), TimeUnit.MINUTE);
            case MINUTE:
                randomTime.setTime(startTimeLocal.getSecond(), TimeUnit.SECOND);
            case SECOND:
            default:
                break;
            }
        } else {
            randomTime = startTime;
        }

        // 根据毫秒值转换为Time对象
        return randomTime;
    }

    /**
     * 用于以当前时间和指定的时间为随机时间范围，生成一个随机的时间
     * <p>
     * 若当前时间大于指定的时间，则将当前时间作为结束时间进行处理； 若当前时间小于指定的时间，则将当前时间作为起始时间进行处理。
     * 详细参数说明可参考{@link #randomTime(Time, Time, TimeUnit)}
     * </p>
     * 
     * @param time     指定的时间
     * @param timeUnit 随机时间最小变动单位
     * @return 随机的时间
     * @since autest 2.0.0
     */
    public static Time randomTime(Time time, TimeUnit timeUnit) {
        return randomTime(Time.parse(), time, timeUnit);
    }

    /**
     * 用于计算指定时间与待比较时间之间差值，并以{@link Duration}的形式对计算结果进行返回
     * 
     * @param time        指定时间
     * @param compareTime 待比较时间
     * @return 两时间的差值
     * @since autest 2.0.0
     */
    public static Duration timeDifference(Time time, Time compareTime) {
        // 判断传入的对象是否为空
        if (time == null || compareTime == null) {
            throw new IncorrectConditionException(
                    String.format("必须指定时间范围与最小变动单位：[time:%s, compareTime:%s]", time, compareTime));
        }

        return Duration.between(time.getLocalDateTime(), compareTime.getLocalDateTime());
    }

    /**
     * 该方法用于将指定的与一组时间进行比较，返回待比较时间在时间组中的位置下标
     * <p>
     * 下标从0开始计算，0表示待比较时间在时间组的第一位。
     * </p>
     * <p>
     * <b>注意：</b>方法不对时间组进行排序，当待比较时间为null时，则返回-1
     * </p>
     * 
     * @param time         待比较时间
     * @param compareTimes 时间组
     * @return 待比较时间在时间组中的位置
     * @since autest 3.2.0
     */
    public static int compareTimes(Time time, Time... compareTimes) {
        return compareTimes(time, Arrays.asList(compareTimes));
    }

    /**
     * 该方法用于将指定的与一组时间进行比较，返回待比较时间在时间组中的位置下标
     * <p>
     * 下标从0开始计算，0表示待比较时间在时间组的第一位。
     * </p>
     * <p>
     * <b>注意：</b>方法不对时间组进行排序，当待比较时间为null时，则返回-1
     * </p>
     * 
     * @param time         待比较时间
     * @param compareTimes 时间组
     * @return 待比较时间在时间组中的位置
     * @since autest 3.2.0
     */
    public static int compareTimes(Time time, Collection<Time> compareTimes) {
        // 判断待比较的时间对象是否为null，为null，则返回-1
        if (time == null) {
            return -1;
        }

        // 判断比较的时间组是否为空，为空，则返回0
        if (compareTimes == null || compareTimes.size() == 0) {
            return 0;
        }

        // 循环， 对比所有的时间，当待比较的时间小于被比较的时间时，则返回被比较时间所在的下标
        int index = 0;
        for (Time compareTime : compareTimes) {
            if (time.compareTo(compareTime) < 0) {
                break;
            }

            index++;
        }
        return index;
    }
}
