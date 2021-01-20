package com.auxiliary.tool.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>Time.java
 * </p>
 * <p>
 * <b>用途：</b>提供对时间进行转换相关的工具
 * </p>
 * <p>
 * <b>编码时间：</b>2019年12月2日下午5:15:55
 * </p>
 * <p>
 * <b>修改时间：</b>2021年1月20日下午12:43:01
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class Time {
	/**
	 * 定义默认时区
	 */
	public static ZoneId defaultZoneId = ZoneId.systemDefault();
	
	/**
	 * 指向初始化时设置的时间
	 */
	private LocalDateTime initTime;
	/**
	 * 指向根据初始化时间计算后得到的时间
	 */
	private LocalDateTime calculateTime;

	/**
	 * 用于存储日期的格式，默认格式为yyyy-MM-dd HH:mm:ss
	 */
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * 定义日期约束类型的传入格式
	 */
	private final static String REGEX_DATE = "(\\D*((\\d{1,2})|(\\d{4}))\\D+\\d{1,2}\\D+\\d{1,2})((\\D+\\d{1,2}){3})?\\D*";

	/**
	 * 私有构造
	 */
	private Time() {
	}
	
	/**
	 * 用于根据{@link Date}类对象初始化时间
	 * 
	 * @param date Date类对象
	 */
	public static Time parse(Date date) {
		return parse(Optional.ofNullable(date).orElse(new Date()).getTime());
	}

	/**
	 * 用于根据毫秒数初始化时间
	 * 
	 * @param ms 时间戳（毫秒值）
	 */
	public static Time parse(long ms) {
		Time time = new Time();
		
		//转换时间戳
		Instant longTime = Instant.ofEpochMilli(ms);
		
		time.initTime = LocalDateTime.ofInstant(longTime, defaultZoneId);
		time.calculateTime =  LocalDateTime.ofInstant(longTime, defaultZoneId);
		
		return time;
	}

	/**
	 * 用于根据已格式化的时间初始化时间
	 * 
	 * @param formatTime 已格式化的时间
	 * @throws IncorrectConditionException 时间转换错误时抛出的异常
	 */
	public static Time parse(String formatTime) {
		// 判断传入的格式化时间是否符合要求，并将其转换为格式化字符串
		return parse(formatTime, 
				Optional.ofNullable(formatTime).filter(text -> !text.isEmpty())
				.filter(text -> text.matches(REGEX_DATE))
				.map(Time::judgeDateFormatText)
				.orElseThrow(() -> new IncorrectConditionException("时间“" + formatTime + "”不符合格式的规则"))
				);
	}
	
	/**
	 * 用于根据格式化的日期/时间，及相应的时间格式，初始化日期/时间
	 * <p>
	 * 该方法允许只传入格式化的日期或者时间，如：
	 * <code><pre>
	 * Time time1 = Time.parse("2020-12-12", "yyyy-MM-dd");//初始化为2020年12月12日的0点
	 * Time time2 = Time.parse("15:15:15", "HH:mm:ss");//初始化为当天的15时15分15秒
	 * </pre></code>
	 * </p>
	 * @param formatTime 格式化的日期/时间
	 * @param formatText 时间格式
	 * @return 初始化的类
	 * @throws DateTimeParseException 日期/时间无法转换时抛出的异常
	 */
	public static Time parse(String formatTime, String formatText) {
		Time time = new Time();
		
		//定义相应的时间格式，并用于解析传入的时间
		dateFormat = DateTimeFormatter.ofPattern(formatText);
		try {
			time.initTime = LocalDateTime.parse(formatTime, dateFormat);
			time.calculateTime = LocalDateTime.parse(formatTime, dateFormat);
		} catch (DateTimeParseException e) {
			if (formatText.matches(".*M+.*")) {
				time.initTime = LocalDate.parse(formatTime, dateFormat).atStartOfDay();
				time.calculateTime = LocalDate.parse(formatTime, dateFormat).atStartOfDay();
			} else {
				time.initTime = LocalTime.parse(formatTime, dateFormat).atDate(LocalDate.now());
				time.calculateTime = LocalTime.parse(formatTime, dateFormat).atDate(LocalDate.now());
			}
		}
		
		return time;
	}

	/**
	 * 用于将时间初始化为当前时间
	 */
	public static Time parse() {
		Time time = new Time();
		
		time.initTime = LocalDateTime.now();
		time.calculateTime = LocalDateTime.now();
		
		return time;
	}

	/**
	 * 设置返回时间的格式，该方法可传入时间格式，亦可向该方法中传入时间格式的模板，
	 * 通过识别模板得到日期的格式，但作为模板的日期也必须满足时间格式。例如：<br>
	 * 
	 * <pre>
	 * <code>
	 * Time time = new Time(1575387800000L);
	 * 
	 * time.setTimeFormat("yyyy年MM月dd日 HH:mm:ss");
	 * getFormatTime();//输出：2019年12月03日 23:43:20
	 * 
	 * time.setTimeFormat("2019/12/04 03:03:20");
	 * getFormatTime();//输出：2019/12/03 23:43:20
	 * </code>
	 * </pre>
	 * 
	 * <p>
	 * <b>注意</b>
	 * <ol>
	 * <li>传入已格式化的时间时，其不会改变当前存储的时间</li>
	 * <li>已格式化的时间中，其分隔符不能包含字母，否则转译将出错（在格式化时间的方法中也不允许存在字母）</li>
	 * </ol>
	 * </p>
	 * 
	 * @param pattern 指定的格式或已格式化的时间
	 */
	public Time setTimeFormat(String pattern) {
		pattern = Optional.ofNullable(pattern).filter(text -> !text.isEmpty())
				.orElseThrow(() -> new IncorrectConditionException("未指定时格式"));
		
		if (pattern.matches(REGEX_DATE)) {
			dateFormat = DateTimeFormatter.ofPattern(judgeDateFormatText(pattern));
		} else {
			dateFormat = DateTimeFormatter.ofPattern(pattern);
		}
		
		return this;
	}

	/**
	 * 用于返回Date类对象
	 * 
	 * @return Date类对象
	 */
	public Date getDate() {
		return Date.from(calculateTime.atZone(defaultZoneId).toInstant());
	}

	/**
	 * 用于返回设置的时间的时间戳
	 * 
	 * @return 时间戳
	 */
	public long getMilliSecond() {
		return calculateTime.atZone(defaultZoneId).toInstant().toEpochMilli();
	}

	/**
	 * 用于返回设置时间的格式化后的时间，若通过{@link #Time(String)}构造
	 * 或{@link #initTime(String)}方法创建的时间，则按照原格式进行返回，若通过其他方法 创建的时间，则按照默认的“yyyy-MM-dd
	 * HH:mm:ss”格式进行返回
	 * 
	 * @return 格式化后的时间
	 */
	public String getFormatTime() {
		return calculateTime.format(dateFormat);
	}

	/**
	 * 用于还原最后一次设置的时间
	 */
	public Time initTime() {
		calculateTime = initTime;
		
		return this;
	}

	/**
	 * 用于根据条件计算日期/时间，方法允许传入小数与负数进行计算
	 * <p>
	 * <b>注意：</b>在计算年、月时，若传入的数值是小数，在转换毫秒值时，其会按照
	 * <ul>
	 * <li>1年 = 365天</li>
	 * <li>1月 = 30天</li>
	 * </ul>
	 * 进行计算，在跨度大的计算中，其会存在精度的丢失
	 * </p>
	 * @param num 日期/时间增减的数量
	 * @param timeUnit 日期计算的单位
	 */
	public Time addTime(double num, TimeUnit timeUnit) {
		calculateTime = calcuLocalTime(Double.valueOf(num), timeUnit, calculateTime);
		return this;
	}
	
	/**
	 * <p>
	 * 用于根据传入的增减时间的规则对时间进行增减，传入需要修改的时间单位，
	 * 根据单位前的数值对单位进行增减。例如：需要对当前设置的时间增加1年3个月又5天并较少2小时30分钟45秒，
	 * 此时可以传入“1年3月5日-2时-30分-45秒”，亦可以传入“1y3m5d-2h-30min-45s”。
	 * </p>
	 * <p>
	 * 注意：
	 * <ol>
	 * <li>单位必须准确，允许传入以下单位：
	 * <ul>
	 * <li>年单位：年、y、Y</li>
	 * <li>月单位：月、m、M</li>
	 * <li>周单位：周、w、W</li>
	 * <li>日单位：日、d、D</li>
	 * <li>小时单位：时、h、H</li>
	 * <li>分钟单位：分、min、MIN</li>
	 * <li>秒单位：秒、s、S</li>
	 * </ul>
	 * </li>
	 * <li>允许传入小数，但年、月传入小数时，按照自然年及自然月计算，即1年365天，不考虑闰年；
	 * 1月30天，不考虑大月与二月。例如，传入5.3y2.5h则，5.3年转换为5.3 * 365天计算， 2.5小时将转化为增加2.5 *
	 * 60分钟计算。建议不要在年、月中传入小数，否则可能导致计算失真</li>
	 * </ol>
	 * </p>
	 * 
	 * 
	 * @param regex 时间规则
	 * @return 返回修改后的时间戳
	 */
	public Time addTime(String calculateTimeText) {
		//将字符串转换为char[]数组
		char[] chars = Optional.ofNullable(calculateTimeText)
				.filter(text -> !text.isEmpty())
				//为保证最后一位能进行计算，在字符串末尾拼接一个“-”符号
				.map(text -> text + "-")
				.map(String::toCharArray)
				.orElseThrow(() -> new IncorrectConditionException("必须指定修改时间的参数"));
		
		//记录当前计算的时间
		LocalDateTime nowTime = calculateTime;
		
		/*
		 * 判断单位思路：
		 * 1.遍历通过calculateTimeText得到的每一个字符
		 * 2.判断当前字符是否为数字：
		 * 	a.若为数字，则判断上一次读取的内容是否为字符：
		 * 		I.若为字符，则表示上一个单位及计算数值已读取完毕，则先对上一次的数值对日期时间进行一次计算
		 * 		II.若为数字，则表示当前正在读取计算的数值，则不进行操作
		 * 	判断结束后，记录isUnit为false，表示当前字符为数字，并拼接到numText中
		 * 	b.若为非数字，则将isUnit设置为true，并拼接计算单位
		 * */
		//遍历所有的字符，区别存储单位与增减的数值
		StringBuilder numText = new StringBuilder();
		StringBuilder unitText = new StringBuilder();
		boolean isUnit = false;
		for (char ch : chars) {
			//判断当前字符是否为数字
			if (Character.isDigit(ch) || ch == '.' || ch == '-') {
				//判断上一次读取的内容是否为字符
				if (isUnit) {
					nowTime = calcuLocalTime(disposeDoubleText(numText.toString()), 
							Arrays.stream(TimeUnit.values())
							.filter(unit -> unit.isTimeUnit(unitText.toString()))
							.findFirst()
							.orElseThrow(() -> new IncorrectConditionException("无法识别的计算公式：" + numText + unitText))
							, nowTime);
					
					numText.delete(0, numText.length());
					unitText.delete(0, unitText.length());
				}
				
				numText.append(ch);
				isUnit = false;
			} else {
				isUnit = true;
				unitText.append(ch);
			}
		}
		
		calculateTime = nowTime;
		return this;
	}
	
	/**
	 * 用于对计算的double数值进行处理，不全小数点前后缺失的内容
	 * @param doubleText 数值文本
	 * @return 转换后的double类型
	 */
	private Double disposeDoubleText(String doubleText) {
		int index = doubleText.indexOf(".");
		if (index == doubleText.length() - 1) {
			return Double.valueOf(doubleText + "0");
		} else if (index == 0) {
			return Double.valueOf("0" + doubleText);
		} else {
			return Double.valueOf(doubleText);
		}
	}
	
	/**
	 * 用于对传入的时间进行计算，并返回计算结果
	 * @param num 计算数值
	 * @param timeUnit 计算单位
	 * @param time 指定的日期
	 * @return 计算后得到的日期
	 */
	private LocalDateTime calcuLocalTime(Double num, TimeUnit timeUnit, LocalDateTime time) {
		//为避免出现数字过大导致计算出错的问题，先计算整数部分，再将小数部分转换为时间戳后，计算毫秒值
		time = time.plus(num.intValue(), timeUnit.getChronoUnit());
		num = num - num.intValue();
		time = time.plus((long)(num * timeUnit.getToMillisNum()), ChronoUnit.MILLIS);
	
		return time;
	}

	/**
	 * 用于识别传入的日期文本，并将日期文本转换为相应的日期格式化字符串
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>日期字符串必须是三位完整的日期（X年X月X日）或时间（X时X分X秒），或者是完整的日期+时间（X年X月X日X时X分X秒）</li>
	 * <li>日期字符串前后允许添加非数字字符</li>
	 * <li>无法识别纯数字的日期格式</li>
	 * </ol>
	 * </p>
	 * @param dateText 日期文本
	 * @return 相应的日期格式化字符串
	 */
	private static String judgeDateFormatText(String dateText) {
		//判断格式化日期时间中是否存在字母
		if (dateText.matches(".*[a-zA-Z]+.*")) {
			throw new IncorrectConditionException("格式化的日期/时间中存在字母：" + dateText);
		}
		
		//将传入的日期文本转换为字符数组
		char[] chars = dateText.toCharArray();
		
		/*
		 * 转换思路：
		 * 1.遍历通过dateText得到的每一个字符
		 * 2.判断当前字符是否为数字：
		 * 	a.若为数字，则记录isSign为false，表示当前字符为数字，并拼接index指向的位数
		 * 	b.若为非数字，则记录isSign为true,表示当前字符为字符，则需要再次判断上一个字符是
		 * 	否也是非数字（即isSign是否本身为false）:
		 * 		I.若上一个字符不为非数字（isSign原为true），则设置index指向的位数加1（即第一次读取到分隔符，
		 * 		表示上一位的日期以存储完毕）
		 * 		II.若上一位为非数字（isSign原为false），则不做改动（即该字符仅为分隔符的一部分）
		 * 	判断结束后，将isSign设置为true，并拼接分隔符
		 * 3.结束循环后，得到一个待转译的中间字符串
		 * 
		 * 举例：传入“2020-12-25 14:12:12”最终会转换为“1111-22-33 44:55:66”
		 * */
		int index = 1;
		boolean isSign = false;
		StringBuilder formatTextBuilder = new StringBuilder();
		for (char ch : chars) {
			if (Character.isDigit(ch)) {
				isSign = false;
				formatTextBuilder.append(index);
			} else {
				if (!isSign) {
					index++;
				}
				
				isSign = true;
				formatTextBuilder.append(ch);
			}
		}
		
		//判断中间字符串最后一位是否为非数字字符，若为非数字字符，表示位数多移动了1位，需要减1后得到真实的位数
		index -= (formatTextBuilder.substring(formatTextBuilder.length() - 1).matches("\\d") ? 0 : 1);
		
		//判断位数，若位数为3，则表示只传入了日期或者时间
		if (index == 3) {
			//若第一位包含4个字符，则按日期转换，否则按时间转换
			if (formatTextBuilder.substring(formatTextBuilder.indexOf("1"), formatTextBuilder.lastIndexOf("1") + 1).length() == 4) {
				return formatTextBuilder.toString()
						.replaceAll("1", "y")
						.replaceAll("2", "M")
						.replaceAll("3", "d");
			} else {
				return formatTextBuilder.toString()
						.replaceAll("1", "H")
						.replaceAll("2", "m")
						.replaceAll("3", "s");
			}
		} else if (index == 6) {
			//若位数为6，表示既传入了日期也传入了时间
			return formatTextBuilder.toString()
					.replaceAll("1", "y")
					.replaceAll("2", "M")
					.replaceAll("3", "d")
					.replaceAll("4", "H")
					.replaceAll("5", "m")
					.replaceAll("6", "s");
		} else {
			throw new IncorrectConditionException("时间“" + dateText + "”不符合格式的规则");
		}
	}
	
	/**
	 * <p><b>文件名：</b>Time.java</p>
	 * <p><b>用途：</b>
	 * 指定允许使用的时间单位
	 * </p>
	 * <p><b>编码时间：</b>2021年1月20日上午7:54:09</p>
	 * <p><b>修改时间：</b>2021年1月20日上午7:54:09</p>
	 * @author 彭宇琦
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
}
