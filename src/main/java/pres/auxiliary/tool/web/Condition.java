package pres.auxiliary.tool.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;

/**
 * <p>
 * <b>文件名：</b>Condition.java
 * </p>
 * <p>
 * <b>用途：</b>用于页面统计中记录需要读取的列的定位方式 ，以及其统计的约束条件，并提供数据对比方法<br>
 * <b>注意：</b>
 * <ol>
 * <li>数字和日期的边界限制，可以用中括号"[]"和小括号"()"表示，中括号表可等于，小括号表示不能等于</li>
 * <li>数字和日期的分段限制，用英文逗号","隔开，逗号后可以有空格也可以没有空格，但逗号前不能包含空格</li>
 * <li>日期类型按照[小日期,大日期)或者(小日期,大日期]的形式传入，日期的格式应为yyyy-mm-dd</li>
 * <li>数字类型按照[小数字,大数字)或者(小数字,大数字]的形式传入</li>
 * </ol>
 * </p>
 * <p>
 * <b>编码时间：</b>2019年7月28日下午2:13:59
 * </p>
 * <p>
 * <b>修改时间：</b>2019年7月31日下午7:03:59
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class Condition {

	/**
	 * 用于存储元素定位方式
	 */
	private String element;
	/**
	 * 用于存储约束条件，当约束的类型为数字和日期时，此时存储的数据将是前后值是否允许包含的条件
	 */
	private ArrayList<String> constraints = new ArrayList<String>();
	/**
	 * 约束类型
	 */
	private ConstraintType type;

	/**
	 * 用于存储元素的标题
	 */
	private String title;

	/**
	 * 用于通过列表元素定位方式获取到列表的WebElementEventList对象
	 */
	private DataListEvent listEvent;

	/**
	 * 定义日期约束类型的传入格式
	 */
	private final String REGEX_DATE = "[\\[\\(]\\d{4}[-\\.年\\\\\\/][01]?\\d[-\\.月\\\\\\/][0123]?\\d日?"
			+ "( [012]?\\d[:时][0123456]?\\d分?([:分][0123456]?\\d秒?)?)?, "
			+ "*\\d{4}[-\\.年\\\\][01]?\\d[-\\.月\\\\][0123]?\\d日?"
			+ "( [012]?\\d[:时][0123456]?\\d分?([:分][0123456]?\\d秒?)?)?[\\]\\)]";

	/**
	 * 定义数字约束类型传入格式
	 */
	private final String REGEX_NUMBER = "[\\[\\(]\\d+(.\\d+)?, *\\d+(.\\d+)?[\\]\\)]";

	/**
	 * 定义日期的格式约束
	 */
	private final String YEAR_TYPE = "^[-\\+]?[\\d]*$";

	/**
	 * 日期和数字大小约束之间的分隔符号
	 */
	private final String DATA_SEVERANCE = ";";

	/**
	 * 定义边界相等的条件
	 */
	private final String LIMIT_EQUALS = "1";

	/**
	 * 定义边界不等的条件
	 */
	private final String LIMIT_UN_EQUALS = "0";

	/**
	 * 该构造用于当元素需要做约束限制进行统计时，可以使用该构造
	 * 
	 * @param title       元素标题
	 * @param element     元素的定位方式
	 * @param type        统计的约束类型，在{@link ConstraintType}枚举类中选择相应的约束类型
	 * @param constraints 约束条件
	 */
	public Condition(String title, String element, ConstraintType type, String... constraints) {
		super();
		this.element = element;
		this.type = type;
		this.title = title;

		// 根据约束类型设置约束条件
		if (constraints != null && constraints.length != 0) {
			switch (this.type) {
			case STRING:
				setStringConstraint(constraints);
				break;
			case DATE:
				setDateConstraint(constraints);
				break;
			case NUMBER:
				setNumberConstraint(constraints);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 该构造用于当元素不需要任何限制进行统计时，可使用该构造
	 * 
	 * @param title       元素标题
	 * @param element     元素的定位方式
	 */
	public Condition(String title, String element) {
		super();
		this.element = element;
		this.title = title;
	}

	/**
	 * 该方法用于返回元素的标题
	 * 
	 * @return 元素的标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 该方法用于返回元素的定位方式
	 * 
	 * @return 元素的定位方式
	 */
	public String getElement() {
		return element;
	}

	/**
	 * 该方法用于返回传入的元素定位方式在页面上获取到的WebElementEventList对象
	 * 
	 * @param driver 页面的WebDriver对象
	 * @return 返回列表的WebElementEventList对象
	 */
	public DataListEvent getListEvent(WebDriver driver) {
		if (listEvent == null) {
			listEvent = new DataListEvent(driver);
			listEvent.add(element);
		}

		return listEvent;
	}
	
	/**
	 * 该方法用于返回条件约束组中是否存在约束，若存在约束，则返回true，反之，返回false
	 * @return 返回是否存在约束条件
	 */
	public boolean isConstraint() {
		if (constraints.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 该方法用于判断传入的数据是否满足约束条件。若传入的数据与约束条件的类型不符合，则默认返回false
	 * 
	 * @param data 待判断的数据
	 * @return 是否符合约束条件
	 */
	public boolean isConformToConstraint(String data) {
		// 根据约束类型设置约束条件
		if (constraints != null && constraints.size() != 0) {
			switch (this.type) {
			case STRING:
				return compareString(data);
			case DATE:
				return compareDate(data);
			case NUMBER:
				return compareNumber(data);
			default:
				return true;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * 判断数字类型数据
	 * 
	 * @param data 数据
	 * @return 是否符合要求
	 */
	private boolean compareNumber(String data) {
		// 由于指向返回结果
		boolean a = false;
		// 转换传入的数据，若抛出转换异常，则直接返回a值
		double num = 0;

		try {
			num = Double.valueOf(data);
		} catch (Exception e) {
			return a;
		}

		// 循环，读取所有的约束条件，将数据逐个和约束条件进行大小的对比
		for (String constraint : constraints) {

			// 由于约束条件中包含大小约束条件和边界的限制，故需要按照规则进行切分
			String[] ss = constraint.split(DATA_SEVERANCE);

			// 转换最大最小值
			double minNum = Double.valueOf(ss[0]);
			double maxNum = Double.valueOf(ss[2]);

//			System.out.println("----------------------------");
//			System.out.println("约束最小值：" + minNum);
//			System.out.println("约束最大值：" + maxNum);
//			System.out.println("待测元素：" + num);

			// 对比最小值，若待测数据小于约束条件的最小值，不满足约束条件，可直接结束循环，节约时间
			if (LIMIT_EQUALS.equals(ss[1]) && LIMIT_EQUALS.equals(ss[3])) {
				a = num >= minNum && num <= maxNum;
			} else if (LIMIT_UN_EQUALS.equals(ss[1]) && LIMIT_EQUALS.equals(ss[3])) {
				a = num > minNum && num <= maxNum;
			} else if (LIMIT_EQUALS.equals(ss[1]) && LIMIT_UN_EQUALS.equals(ss[3])) {
				a = num >= minNum && num < maxNum;
			} else {
				a = num > minNum && num < maxNum;
			}
			if (a) {
				break;
			}
		}

		return a;
	}

	/**
	 * 判断日期类型
	 * 
	 * @param data 待测数据
	 * @return 是否符合约束
	 */
	private boolean compareDate(String data) {
		// 由于指向返回结果
		boolean a = false;
		// 转换传入的数据，若抛出转换异常，则直接返回a值
		Long time = 0L;
		try {
			time = new SimpleDateFormat(getDateFormat(data)).parse(data).getTime();
		} catch (Exception e) {
			return a;
		}

		// 循环，读取所有的约束条件，将数据逐个和约束条件进行大小的对比
		for (String constraint : constraints) {

			// 由于约束条件中包含大小约束条件和边界的限制，故需要按照规则进行切分
			String[] ss = constraint.split(DATA_SEVERANCE);

			// 转换最大最小值
			double minTime = Double.valueOf(ss[0]);
			double maxTime = Double.valueOf(ss[2]);

//			System.out.println("----------------------------");
//			System.out.println("约束最小值：" + minTime);
//			System.out.println("约束最大值：" + maxTime);
//			System.out.println("待测元素：" + time);

			// 对比最小值，若待测数据小于约束条件的最小值，不满足约束条件，可直接结束循环，节约时间
			if (LIMIT_EQUALS.equals(ss[1]) && LIMIT_EQUALS.equals(ss[3])) {
				a = time >= minTime && time <= maxTime;
			} else if (LIMIT_UN_EQUALS.equals(ss[1]) && LIMIT_EQUALS.equals(ss[3])) {
				a = time > minTime && time <= maxTime;
			} else if (LIMIT_EQUALS.equals(ss[1]) && LIMIT_UN_EQUALS.equals(ss[3])) {
				a = time >= minTime && time < maxTime;
			} else {
				a = time > minTime && time < maxTime;
			}
			if (a) {
				break;
			}
		}

		return a;
	}

	/**
	 * 判断字符串类型
	 * 
	 * @param data 待测数据
	 * @return 是否符合约束
	 */
	private boolean compareString(String data) {
		// 循环，读取所有的约束条件，将数据逐个和约束条件进行大小的对比
		for (String constraint : constraints) {
			// 判断数据是否与约束条件相同，相同，则返回true
			if (constraint.equals(data)) {
				return true;
			}
		}
		// 若数据与所有的约束都不符合，则返回false
		return false;
	}

	/**
	 * 该方法用于设置数字类型的约束条件
	 * 
	 * @param constraints 约束条件组
	 */
	private void setNumberConstraint(String[] constraints) {
		// 循环，逐个读取约束条件
		for (String constraint : constraints) {
			// 判断传入的约束条件是否符合约束条件的传入规则，若不符合规则，则抛出IncorrectConditionException异常
			if (!Pattern.compile(REGEX_NUMBER).matcher(constraint).matches()) {
				throw new IncorrectConditionException("约束条件“" + constraint + "”不符合数字约束的传入规则");
			}

			// 定义存储约束条件的字符串
			String dateConstraint = "";

			// 按照逗号切分字符串
			String[] c = constraint.split("\\,");

			// 将约束时段的边界提取出来
			StringBuilder cmin = new StringBuilder(c[0].trim());
			StringBuilder cmax = new StringBuilder(c[1].trim());

			// 存储边界值的判定条件（移除该字符串则时会返回该字符串）
			String minSymbox = '[' == cmin.charAt(0) ? LIMIT_EQUALS : LIMIT_UN_EQUALS;
			String maxSymbox = ']' == cmax.charAt(cmax.length() - 1) ? LIMIT_EQUALS : LIMIT_UN_EQUALS;

			// 转换数字
			double minNum = 0;
			double maxNum = 0;
			try {
				minNum = Double.valueOf(cmin.delete(0, 1).toString());
			} catch (NumberFormatException e) {
				throw new IncorrectConditionException("约束条件“" + cmin + "”不符合数字约束的传入规则");
			}

			try {
				maxNum = Double.valueOf(cmax.delete(cmax.length() - 1, cmax.length()).toString());
			} catch (NumberFormatException e) {
				throw new IncorrectConditionException("约束条件“" + cmax + "”不符合数字约束的传入规则");
			}

			// 判断时间戳的大小，若两个时间戳大小相反，则反向存储
			if (minNum > maxNum) {
				dateConstraint += (maxNum + DATA_SEVERANCE + maxSymbox + DATA_SEVERANCE + minNum + DATA_SEVERANCE
						+ minSymbox);
			} else {
				dateConstraint += (minNum + DATA_SEVERANCE + minSymbox + DATA_SEVERANCE + maxNum + DATA_SEVERANCE
						+ maxSymbox);
			}

			this.constraints.add(dateConstraint);
		}

	}

	/**
	 * 该方用于设置时间类型的约束条件
	 * 
	 * @param constraints 约束条件组
	 */
	private void setDateConstraint(String[] constraints) {
		// 循环，逐个读取约束条件
		for (String constraint : constraints) {
			// 判断传入的约束条件是否符合约束条件的传入规则，若不符合规则，则抛出IncorrectConditionException异常
			if (!Pattern.compile(REGEX_DATE).matcher(constraint).matches()) {
				throw new IncorrectConditionException("约束条件“" + constraint + "”不符合日期约束传入的规则");
			}

			// 定义存储约束条件的字符串
			String dateConstraint = "";

			// 按照逗号切分字符串
			String[] c = constraint.split("\\,");
			// 将约束时段的边界提取出来
			StringBuilder cmin = new StringBuilder(c[0].trim());
			StringBuilder cmax = new StringBuilder(c[1].trim());

			// 存储边界值的判定条件（移除该字符串则时会返回该字符串）
			String minSymbox = '[' == cmin.charAt(0) ? LIMIT_EQUALS : LIMIT_UN_EQUALS;
			String maxSymbox = ']' == cmax.charAt(cmax.length() - 1) ? LIMIT_EQUALS : LIMIT_UN_EQUALS;

			// 将该日期转换成时间戳，拼接至字符串中
			long minTime = 0L;
			long maxTime = 0L;
			try {
				minTime = new SimpleDateFormat(getDateFormat(cmin.delete(0, 1).toString())).parse(cmin.toString())
						.getTime();
			} catch (ParseException e) {
				throw new IncorrectConditionException("约束条件“" + cmin + "”不符合日期约束传入的规则");
			}

			try {
				maxTime = new SimpleDateFormat(getDateFormat(cmax.delete(cmax.length() - 1, cmax.length()).toString()))
						.parse(cmax.toString()).getTime();
			} catch (ParseException e) {
				throw new IncorrectConditionException("约束条件“" + cmax + "”不符合日期约束传入的规则");
			}

			// 判断时间戳的大小，若两个时间戳大小相反，则反向存储
			if (minTime > maxTime) {
				dateConstraint += (maxTime + ";" + maxSymbox + ";" + minTime + ";" + minSymbox);
			} else {
				dateConstraint += (minTime + ";" + minSymbox + ";" + maxTime + ";" + maxSymbox);
			}

			this.constraints.add(dateConstraint);
		}
	}

	/**
	 * 该方法用于设置字符串类型的约束条件
	 * 
	 * @param constraints 约束条件组
	 */
	private void setStringConstraint(String[] constraints) {
		// 存储整个约束条件
		this.constraints.addAll(Arrays.asList(constraints));
	}

	/**
	 * 识别传入的时间格式
	 * 
	 * @param time 时间
	 * @return 时间格式
	 */
	private String getDateFormat(String time) {
		boolean year = false;
		Pattern pattern = Pattern.compile(YEAR_TYPE);
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
		return sb.toString();
	}
}
