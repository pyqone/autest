package pres.auxiliary.tool.date;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.ibm.icu.text.SimpleDateFormat;

import pres.auxiliary.tool.web.IncorrectConditionException;

/**
 * <p>
 * <b>文件名：</b>DateToMs.java
 * </p>
 * <p>
 * <b>用途：</b>提供对时间进行转换相关的工具
 * </p>
 * <p>
 * <b>编码时间：</b>2019年12月2日下午5:15:55
 * </p>
 * <p>
 * <b>修改时间：</b>2019年12月2日下午5:15:55
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class Time {
	/**
	 * 用于指向指定的时间
	 */
	private Date date;

	/**
	 * 用于设置的时间，以保证在增加或减少时间后，能还原回初始设置的时间
	 */
	private Date oldDate;

	/**
	 * 用于存储日期的格式，默认格式为yyyy-MM-dd HH:mm:ss
	 */
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 定义日期约束类型的传入格式
	 */
	private final String REGEX_DATE = "(\\d{4}[-\\.年\\\\\\/][01]?\\d[-\\.月\\\\\\/][0123]?\\d日?)?"
			+ "( ?[012]?\\d[:时][0123456]?\\d分?([:分][0123456]?\\d秒?)?)?";

	/**
	 * 构造当前时间
	 */
	public Time() {
		setNowTime();
	}

	/**
	 * 通过Date类对象进行构造
	 * 
	 * @param date Date类对象
	 */
	public Time(Date date) {
		setTime(date);
	}

	/**
	 * 通过时间戳进行构造
	 * 
	 * @param ms 时间戳（毫秒值）
	 */
	public Time(long ms) {
		setTime(ms);
	}

	/**
	 * 通过格式化过的时间进行构造
	 * 
	 * @param formatTime 已格式化的时间
	 * @throws IncorrectConditionException 时间转换错误时抛出的异常
	 */
	public Time(String formatTime) {
		setTime(formatTime);
	}

	/**
	 * 用于Date类对象设置指定的时间
	 * 
	 * @param date Date类对象
	 */
	public void setTime(Date date) {
		this.date = date;
		oldDate = this.date;
	}

	/**
	 * 用于根据毫秒数设置指定的时间
	 * 
	 * @param ms 时间戳（毫秒值）
	 */
	public void setTime(long ms) {
		date = new Date(ms);
		oldDate = this.date;
	}

	/**
	 * 用于根据已格式化的时间设置指定的时间
	 * 
	 * @param formatTime 已格式化的时间
	 * @throws IncorrectConditionException 时间转换错误时抛出的异常
	 */
	public void setTime(String formatTime) {
		if (formatTime.matches(REGEX_DATE)) {
			try {
				date = new SimpleDateFormat(getDateFormat(formatTime)).parse(formatTime);
				oldDate = this.date;
			} catch (ParseException e) {
			}
		} else {
			throw new IncorrectConditionException("时间“" + formatTime + "”不符合格式的规则");
		}
	}

	/**
	 * 用于将时间设置为当前时间
	 */
	public void setNowTime() {
		date = new Date();
		oldDate = this.date;
	}

	/**
	 * 用于返回Date类对象
	 * 
	 * @return Date类对象
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * 用于返回Calendar类对象
	 * @return Calendar类对象
	 */
	public Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		return c;
	}

	/**
	 * 用于返回设置的时间的时间戳
	 * 
	 * @return 时间戳
	 */
	public long getTime() {
		return date.getTime();
	}

	/**
	 * 用于返回设置时间的格式化后的时间，若通过{@link #Time(String)}构造
	 * 或{@link #setTime(String)}方法创建的时间，则按照原格式进行返回，若 通过其他方法 创建的时间，则按照默认的“yyyy-MM-dd
	 * HH:mm:ss”格式进行返回
	 * 
	 * @return 格式化后的时间
	 */
	public String getFormatTime() {
		return new SimpleDateFormat(dateFormat).format(date);
	}

	/**
	 * 根据指定的格式，格式化设置的时间并进行返回，若格式化失败，则按照默 认的“yyyy-MM-dd HH:mm:ss”格式进行返回。
	 * 亦可向该方法中传入时间格式的模板，通过识别模板得到日期的格式，但作为模板的日期也必须满足时间格式。例如：<br>
	 * date = 1575387800000L;<br>
	 * String getFormatTime("yyyy-MM-dd HH:mm:ss");//输出：2019-12-03 23:43:20<br>
	 * String getFormatTime("2019/12/04 03:03:20");//输出：2019/12/03 23:43:20<br>
	 * 
	 * 
	 * @param pattern 指定的格式
	 * @return 格式化后的时间
	 */
	public String getFormatTime(String pattern) {
		if (pattern.matches(REGEX_DATE)) {
			return new SimpleDateFormat(getDateFormat(pattern)).format(date);
		} else {
			try {
				return new SimpleDateFormat(pattern).format(date);
			} catch (IllegalArgumentException e) {
				return new SimpleDateFormat(dateFormat).format(date);
			}
		}
	}

	/**
	 * 用于还原最后一次设置的时间
	 */
	public void initTime() {
		date = oldDate;
	}

	/**
	 * <p>
	 * 用于根据传入的增减时间的规则对时间进行增减，传入需要修改的时间单位，
	 * 根据单位前的数值对单位进行增减。例如：需要对当前设置的时间增加1年3个月又5天并较少2小时30分钟45秒，
	 * 此时可以传入“1年3月5日-2时-30分-45秒”，亦可以传入“1y3m5d-2h-30min-45s”。
	 * </p>
	 * <p>
	 * 注意：
	 * 	<ol>
	 * 		<li>单位必须准确，允许传入以下单位：
	 * 			<ul>
	 * 				<li>年单位：年、y、Y</li>
	 * 				<li>月单位：月、m、M</li>
	 * 				<li>周单位：周、w、W</li>
	 * 				<li>日单位：日、d、D</li>
	 * 				<li>小时单位：时、h、H</li>
	 * 				<li>分钟单位：分、min、MIN</li>
	 * 				<li>秒单位：秒、s、S</li>
	 * 			</ul>
	 * 		</li>
	 * 		<li>允许传入小数，但年、月传入小数时，按照自然年及自然月计算，即1年365天，不考虑闰年；
	 * 			1月30天，不考虑大月与二月。例如，传入5.3y2.5h则，5.3年转换为5.3 * 365天计算，
	 * 			2.5小时将转化为增加2.5 * 60分钟计算。建议不要在年、月中传入小数，否则可能导致计算失真
	 * 		</li>
	 * 	</ol>
	 * </p>
	 * 
	 * 
	 * @param regex 时间规则
	 */
	public void addTime(String regex) {
		//去空格
		regex = regex.replaceAll(" ", "");
		
		// 初始化Calendar类，用于修改日期
		Calendar cTime = Calendar.getInstance();
		cTime.setTime(date);
		
		//封装传入的修改时间规则，以便于删除已修改的规则
		StringBuilder time = new StringBuilder(regex);
		//用于指向规则中单位的位置
		int index = -1;

		// 判断是否包含分钟，由于分钟比较特殊，可能会与月份重复，故放在第一位判断；
		//若存在小数点，则按照毫秒进行转换
		if ((index = time.indexOf("分")) > -1 || (index = time.indexOf("min")) > -1
				|| (index = time.indexOf("MIN")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = (int) (Double.valueOf(subTime.substring(numIndex)) * 60.0 * 1000.0);
			//cTime增加相应日期
			cTime.add(Calendar.MILLISECOND, addTime);

			//删除已修改的单位以及其数值，由于分钟单位可能为三位，故需要根据具体传入的单位来定义
			time = time.delete(numIndex,
					time.substring(index, time.length()).indexOf("分") == 0 ? index + 1 : index + 3);
		}

		//判断是否包含年份，若存在小数点，则忽略
		if ((index = time.indexOf("年")) > -1 || (index = time.indexOf("y")) > -1 || (index = time.indexOf("Y")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = 0;
			String yearNum = subTime.substring(numIndex);
			try {
				//若传入的年份是整数，则直接按照整数转换，并对年份增加相应的数值
				addTime = Integer.valueOf(yearNum);
				cTime.add(Calendar.YEAR, addTime);
			}catch (NumberFormatException e) {
				//若年份包含小数，则按照小数点进行切分，先对年份的整数部分进行增加
				String[] num = yearNum.split("\\.");
				addTime = Integer.valueOf(num[0]);
				cTime.add(Calendar.YEAR, addTime);
				
				//之后对小数部分拼接“0.”，转换为double，之后将其转换成天数
				int remainder = (int) (Double.valueOf((addTime > 0 ? "0.": "-0.") + num[1]) * 365.0);
				cTime.add(Calendar.DATE, remainder);
			}
			
			//删除已修改的单位以及其数值
			time = time.delete(numIndex, index + 1);
		}

		//判断是否包含月份，若存在小数点，则忽略
		if ((index = time.indexOf("月")) > -1 || (index = time.indexOf("m")) > -1 || (index = time.indexOf("M")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = 0;
			String monthNum = subTime.substring(numIndex);
			try {
				//若传入的年份是整数，则直接按照整数转换，并对年份增加相应的数值
				addTime = Integer.valueOf(monthNum);
				cTime.add(Calendar.MONTH, addTime);
			}catch (NumberFormatException e) {
				//若年份包含小数，则按照小数点进行切分，先对年份的整数部分进行增加
				String[] num = monthNum.split("\\.");
				addTime = Integer.valueOf(num[0]);
				cTime.add(Calendar.MONTH, addTime);
				
				//之后对小数部分拼接“0.”，转换为double，之后将其转换成天数
				addTime = (int) (Double.valueOf((addTime > 0 ? "0.": "-0.") + num[1]) * 30.0);
				cTime.add(Calendar.DATE, addTime);
			}

			//删除已修改的单位以及其数值
			time = time.delete(numIndex, index + 1);
		}
		
		//判断是否包含月份，若存在小数点，则忽略
		if ((index = time.indexOf("周")) > -1 || (index = time.indexOf("w")) > -1 || (index = time.indexOf("W")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = (int) (Double.valueOf(subTime.substring(numIndex)) * 7.0);
			//cTime增加相应日期
			cTime.add(Calendar.DATE, addTime);

			//删除已修改的单位以及其数值
			time = time.delete(numIndex, index + 1);
		}

		//判断是否包含天数，若存在小数点，则忽略
		if ((index = time.indexOf("日")) > -1 || (index = time.indexOf("d")) > -1 || (index = time.indexOf("D")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = (int) (Double.valueOf(subTime.substring(numIndex)) * 24.0 * 60.0 * 60.0 * 1000.0);
			//cTime增加相应日期
			cTime.add(Calendar.MILLISECOND, addTime);

			//删除已修改的单位以及其数值
			time = time.delete(numIndex, index + 1);
		}

		//判断是否包含小时，若存在小数点，则按照毫秒进行转换
		if ((index = time.indexOf("时")) > -1 || (index = time.indexOf("h")) > -1 || (index = time.indexOf("H")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = (int) (Double.valueOf(subTime.substring(numIndex)) * 60.0 * 60.0 * 1000.0);
			//cTime增加相应日期
			cTime.add(Calendar.MILLISECOND, addTime);

			//删除已修改的单位以及其数值
			time = time.delete(numIndex, index + 1);
		}

		//判断是否包含秒，若存在小数点，则按照毫秒进行转换
		if ((index = time.indexOf("秒")) > -1 || (index = time.indexOf("s")) > -1 || (index = time.indexOf("S")) > -1) {
			//存储待判断单位前的字符串
			String subTime = time.substring(0, index);
			//存储待判断单位前数值位置
			int numIndex = getIndex(time.substring(0, index));

			//转换数值
			int addTime = (int) (Double.valueOf(subTime.substring(numIndex)) * 1000.0);
			//cTime增加相应日期
			cTime.add(Calendar.MILLISECOND, addTime);

			//删除已修改的单位以及其数值
			time = time.delete(numIndex, index + 1);
		}
		
		//将转换后的时间存储至date中
		date = cTime.getTime();
	}

	/**
	 * 用于判断相应单位前的数字在整个字符串中所存在的位置
	 * @param text 传入的除当前判断单位前的字符串，例如有规则5H31s，判断秒数，则传入5H31
	 * @return 返回待判断单位前的数值，如5H31s，判断秒数，则返回2
	 */
	private static int getIndex(String text) {
		// 定义规则
		String regex = "(\\.\\d+)|(-?\\d+(\\.\\d+)?)";

		// 如果其本身符合正则，则返回0
		if (text.matches(regex)) {
			return 0;
		}

		// 若本身不符合正则，则从后向前对字符串逐个增加，直到找到下一个单位为止，返回其在字符串中相应的位置
		for (int i = 1; i < text.length(); i++) {
			int index = text.length() - i;
			// 若切分到的字符串不再符合正则，即此时已找到下一个单位，则返回其下标+1
			// 例如，有字符串5H31s，传入到方法中的字符串将为5H31
			// 逐个累加字符串时，将读取到H31，此时正则返回false，则记录其下标+1，即为3的位置
			if (!text.substring(index).matches(regex)) {
				return index + 1;
			}
		}
		
		//若判断失败，则返回-1，理论上不存在该返回
		return -1;
	}

	/**
	 * 识别传入的时间格式
	 * 
	 * @param time 时间
	 * @return 时间格式
	 */
	private String getDateFormat(String time) {
		boolean year = false;
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		if (pattern.matcher(time.substring(0, 4)).matches()) {
			year = true;
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		if (!year) {
			if (time.contains("月") || time.contains("-") || time.contains("/")) {
				if (Character.isDigit(time.charAt(0))) {
					index = 1;
				}
			} else {
				index = 3;
			}
		}
		for (int i = 0; i < time.length(); i++) {
			char chr = time.charAt(i);
			if (Character.isDigit(chr)) {
				if (index == 0) {
					sb.append("y");
				}
				if (index == 1) {
					sb.append("M");
				}
				if (index == 2) {
					sb.append("d");
				}
				if (index == 3) {
					sb.append("H");
				}
				if (index == 4) {
					sb.append("m");
				}
				if (index == 5) {
					sb.append("s");
				}
				if (index == 6) {
					sb.append("S");
				}
			} else {
				if (i > 0) {
					char lastChar = time.charAt(i - 1);
					if (Character.isDigit(lastChar)) {
						index++;
					}
				}
				sb.append(chr);
			}
		}

		// 存储转换后的格式
		dateFormat = sb.toString();
		return sb.toString();
	}
}
