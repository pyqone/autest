package com.auxiliary.tool.date;

import java.time.temporal.ChronoUnit;

/**
 * <p><b>文件名：</b>TimeUnit.java</p>
 * <p><b>用途：</b>
 * 指定允许使用的时间单位
 * </p>
 * <p><b>编码时间：</b>2021年1月20日上午7:54:09</p>
 * <p><b>修改时间：</b>2021年1月20日上午7:54:09</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public enum TimeUnit {
	/**
	 * 指向计算单位“<b>年</b>”，对应的时间单位为：年、y、Y
	 */
	YEAR("[年yY]", ChronoUnit.YEARS, (365L * 24L * 60L * 60L * 1000L)), 
	/**
	 * 指向计算单位“<b>月</b>”，对应的时间单位为：月、m、M
	 */
	MONTH("[月mM]", ChronoUnit.MONTHS, (30L * 24L * 60L * 60L * 1000L)), 
	/**
	 * 指向计算单位“<b>周</b>”，对应的时间单位为：周、w、W
	 */
	WEEK("[周wW]", ChronoUnit.WEEKS, (7L * 24L * 60L * 60L * 1000L)), 
	/**
	 * 指向计算单位“<b>日</b>”，对应的时间单位为：日、d、D
	 */
	DAY("[日dD]", ChronoUnit.DAYS, (24L * 60L * 60L * 1000L)), 
	/**
	 * 指向计算单位“<b>时</b>”，对应的时间单位为：时、h、H
	 */
	HOUR("[时hH]", ChronoUnit.HOURS, (60L * 60L * 1000L)), 
	/**
	 * 指向计算单位“<b>分</b>”，对应的时间单位为：分、min（所有字母不区分大小写）
	 */
	MINUTE("分|((m|M)(i|I)(n|N))", ChronoUnit.MINUTES, (60L * 1000L)), 
	/**
	 * 指向计算单位“<b>秒</b>”，对应的时间单位为：秒、s、S
	 */
	SECOND("[秒sS]", ChronoUnit.SECONDS, (1000L)), 
	;
	/**
	 * 指定判断当前单位的正则
	 */
	private String unitRegex;
	/**
	 * 存储转换为毫秒值所需的乘积
	 */
	private long toMillisNum;
	/**
	 * 指向当前的单位在{@link ChronoUnit}中的映射
	 */
	private ChronoUnit chronoUnit;
	
	/**
	 * 初始化枚举值
	 * @param unitRegex 单位判断正则
	 * @param chronoUnit {@link ChronoUnit}的映射
	 * @param toMillisNum 转换为毫秒值所需的乘积
	 */
	private TimeUnit(String unitRegex, ChronoUnit chronoUnit, long toMillisNum) {
		this.unitRegex = unitRegex;
		this.toMillisNum = toMillisNum;
		this.chronoUnit = chronoUnit;
	}
	
	/**
	 * 用于返回单位转换为毫秒值所需的乘积
	 * @return 单位转换为毫秒值所需的乘积
	 */
	public long getToMillisNum() {
		return toMillisNum;
	}
	
	/**
	 * 用于返回单位在{@link ChronoUnit}的映射
	 * @return {@link ChronoUnit}的映射
	 */
	public ChronoUnit getChronoUnit() {
		return chronoUnit;
	}
	
	/**
	 * 用于判断传入的单位是否符合当前枚举值
	 * @param unit 单位
	 * @return 是否符合当前枚举
	 */
	public boolean isTimeUnit(String unit) {
		return unit.matches(unitRegex);
	}
}
