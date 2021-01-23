package com.auxiliary.tool.date;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>InitTimeUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 提供特殊的更改日期/时间的方法，
 * </p>
 * <p>
 * <b>编码时间：</b>2021年1月21日上午8:15:45
 * </p>
 * <p>
 * <b>修改时间：</b>2021年1月21日上午8:15:45
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class InitTimeUtil {
	private InitTimeUtil(){
	}
	
	/**
	 * 更改日期为指定日期下当年的第一天
	 * <p>
	 * 例如，指定时间为：2019-12-21 15:21:10，则调用方法后，将返回时间为：2019-01-01 00:00:00
	 * </p>
	 * <p>
	 * <b>注意：</b>若日期未传入（null），则默认为当天的日期
	 * </p>
	 * 
	 * @param date 指定的日期
	 * @return 更改的日期
	 */
	public static LocalDateTime firstDayOfYear(LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		return LocalDateTime.of(date.getYear(), 1, 1, 0, 0, 0);
	}

	/**
	 * 更改日期为指定日期下当年的最后第一天
	 * <p>
	 * 方法允许指定是否将时间也指向最后的日期，例如：
	 * <ol>
	 * <li>指定时间为：2019-12-21 15:21:10，设置时间指向最后一刻，则返回时间为：2019-12-31 23:59:59</li>
	 * <li>指定时间为：2019-12-21 15:21:10，不设置时间，则返回时间为：2019-12-31 15:21:10</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>注意：</b>若日期未传入（null），则默认为当天的日期
	 * </p>
	 * 
	 * @param date 指定的日期
	 * @return 更改的日期
	 */
	public static LocalDateTime lastDayOfYear(boolean isLastTime, LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		return LocalDateTime.of(date.getYear(), 12, 31, isLastTime ? 23 : date.getHour(),
				isLastTime ? 59 : date.getMinute(), isLastTime ? 59 : date.getSecond());
	}

	/**
	 * 更改日期为指定日期下当月的第一天
	 * <p>
	 * 例如，指定时间为：2019-12-21 15:21:10，则调用方法后，将返回时间为：2019-12-01 00:00:00
	 * </p>
	 * <p>
	 * <b>注意：</b>若日期未传入（null），则默认为当天的日期
	 * </p>
	 * 
	 * @return 更改的日期
	 */
	public static LocalDateTime firstDayOfMonth(LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		return LocalDateTime.of(date.getYear(), date.getMonth(), 1, 0, 0, 0);
	}

	/**
	 * 更改日期为指定日期下当月的最后一天
	 * <p>
	 * 方法允许指定是否将时间也指向最后的日期，例如：
	 * <ol>
	 * <li>指定时间为：2019-12-21 15:21:10，设置时间指向最后一刻，则返回时间为：2019-12-31 23:59:59</li>
	 * <li>指定时间为：2019-12-21 15:21:10，不设置时间，则返回时间为：2019-12-31 15:21:10</li>
	 * </ol>
	 * </p>
	 * <p>
	 * <b>注意：</b>若日期未传入（null），则默认为当天的日期
	 * </p>
	 * 
	 * @return 更改的日期
	 */
	public static LocalDateTime lastDayOfMonth(boolean isLastTime, LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		// 设置月份为当前月份的下一月的第一天
		// 当月份为12月时，使用月份数值加1则会抛出异常，故在异常时设置月份为下一年的第一天即可
		try {
			date = LocalDateTime.of(date.getYear(), date.getMonthValue() + 1, 1, isLastTime ? 23 : date.getHour(),
					isLastTime ? 59 : date.getMinute(), isLastTime ? 59 : date.getSecond());
		} catch (Exception e) {
			date = LocalDateTime.of(date.getYear() + 1, 1, 1, isLastTime ? 23 : date.getHour(),
					isLastTime ? 59 : date.getMinute(), isLastTime ? 59 : date.getSecond());
		}

		// 将日期减去一天，即可得到本月的最后一天日期
		return date.minusDays(1);
	}

	/**
	 * 更改日期为指定日期下当天的0点
	 * <p>
	 * 例如，指定时间为：2019-12-21 15:21:10，则调用方法后，将返回时间为：2019-12-21 00:00:00
	 * </p>
	 * <p>
	 * <b>注意：</b>若日期未传入（null），则默认为当天的日期
	 * </p>
	 * 
	 * @return 更改的日期
	 */
	public static LocalDateTime startTimeOfDay(LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
	}
	
	/**
	 * 更改日期为指定日期下当天的最后一刻
	 * <p>
	 * 例如，指定时间为：2019-12-21 15:21:10，则调用方法后，将返回时间为：2019-12-21 23:59:59
	 * </p>
	 * <p>
	 * <b>注意：</b>若日期未传入（null），则默认为当天的日期
	 * </p>
	 * 
	 * @return 更改的日期
	 */
	public static LocalDateTime lastTimeOfDay(LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23, 59, 59);
	}

	/**
	 * 更改日期为指定日期下当天的指定的小时数的起始时间
	 * <p>
	 * 例如，指定时间为：2019-12-21 15:21:10，则调用方法后，将返回时间为：2019-12-21 15:00:00
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>若日期未传入（null），则默认为当天的日期</li>
	 * <li>若传入的小时数大于23或者小于0，则将小时数初始化为指定时间的小时数</li>
	 * </ol>
	 * </p>
	 * 
	 * @param hour 指定的小时数
	 * @return 更改的日期
	 */
	public static LocalDateTime startHour(int hour, LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		if (hour > 23 || hour < 0) {
			hour = date.getHour();
		}
		return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), hour, 0, 0);
	}

	/**
	 * 更改日期为指定日期下当天的指定的分钟数的起始时间
	 * <p>
	 * 例如，指定时间为：2019-12-21 15:21:10，则调用方法后，将返回时间为：2019-12-21 15:21:00
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>若日期未传入（null），则默认为当天的日期</li>
	 * <li>若传入的分钟数大于59或者小于0，则将数值初始化为指定时间的分钟数</li>
	 * </ol>
	 * </p>
	 * 
	 * @param hour 指定的分钟数
	 * @return 更改的日期
	 */
	public static LocalDateTime startMinute(int minute, LocalDateTime date) {
		date = Optional.ofNullable(date).orElse(LocalDateTime.now());
		if (minute > 59 || minute < 0) {
			minute = date.getMinute();
		}
		return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), date.getHour(), minute, 0);
	}
}
