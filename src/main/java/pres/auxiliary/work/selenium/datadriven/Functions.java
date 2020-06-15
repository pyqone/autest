package pres.auxiliary.work.selenium.datadriven;

import pres.auxiliary.tool.date.Time;
import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;

public class Functions {
	/**
	 * <p>
	 * 定义生成随机字符串函数，用法：<br>
	 * ${rs(字符串种子,生成字符串最短长度,生成字符串最大长度)}
	 * </p>
	 * <p>
	 * <b>参数解释：</b><br>
	 * <ul>
	 * <li>字符串种子：生成随机字符串的范围，该范围可自定义，也可以使用系统定义的特殊种子，包括
	 * NUM（数字）、CAP（大写字母）、LOW（小写字母）、CH（中文）、AREA（区域缩写）、SURNAME（姓氏）；
	 * 使用自定义种子时需要使用双引号标识，即传入"ASD"（表示只在ASD中生成随机字符串）
	 * </li>
	 * <li>生成字符串最短长度：最终生成的随机字符串的最短长度</li>
	 * <li>生成字符串最大长度：<b>该字段可不传入</b>，用于指定最终生成的随机字符串的最大长度</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>示例：</b><br>
	 * ${rs("qwertyuiop", 10, 20)}——表示生成“qwertyuiop”范围内，10~20位（随机）长度的随机字符串<br>
	 * ${rs("qwertyuiop", 10)}——表示生成“qwertyuiop”范围内，10位长度的随机字符串<br>
	 * ${rs(CH,10)}——表示生成中文（1000个不重复的中文）范围内，10位长度的随机字符串<br>
	 * ${rs(num, 10,20)}——表示生成数字（0-9）范围内，10~20位（随机）长度的随机字符串<br>
	 * </p>
	 * <p>
	 * <b>注意：</b>每个参数之间的分隔符号中允许存在一个空格，该空格可写可不写
	 * </p>
	 * @return {@link DataDriverFunction}类对象
	 */
	public static DataDriverFunction randomString() {
		//定义规则
		String regex = "rs\\(((\".+\")|NUM|num|CAP|cap|LOW|low|CH|ch|AREA|area|SURNAME|surname), ?\\d+(, ?\\d+)?\\)";
		return new DataDriverFunction(regex, text -> {
			//获取公式中有效部分
			String str = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
			
			//将公式有效部分按照分隔符号进行切分
			String[] parameters = str.split(", ?");
			//切分的第一个元素为字符串种子
			String seed = parameters[0];
			//第二个元素为随机字符串最小长度
			int startIndex = Integer.valueOf(parameters[1]);
			//判断parameters是否存在第三个元素，若不存在，则将字符串最长长度设置为与最小字符串长度一致
			int endIndex = parameters.length > 2 ? Integer.valueOf(parameters[2]) : startIndex;
			
			//判断字符串种子是否包含双引号，若包含双引号，则去掉双引号后将其转换，若不包含，则
			//表示字符串种子属于特殊种子，按照枚举进行添加
			if (seed.indexOf("\"") > -1) {
				seed = seed.substring(seed.indexOf("\"") + 1, seed.lastIndexOf("\""));
			} else {
				switch (seed.toUpperCase()) {
				case "NUM":
					seed = StringMode.NUM.getSeed();
					break;
				case "CAP":
					seed = StringMode.CAP.getSeed();
					break;
				case "LOW":
					seed = StringMode.LOW.getSeed();
					break;
				case "CH":
					seed = StringMode.CH.getSeed();
					break;
				case "AREA":
					seed = StringMode.AREA.getSeed();
					break;
				case "SURNAME":
					seed = StringMode.SURNAME.getSeed();
					break;
				default:
					break;
				}
			}
			
			//返回生成的随机字符串
			return new RandomString(seed).toString(startIndex, endIndex);
		});
	}
	
	/**
	 * <p>
	 * 定义对指定时间处理函数
	 * </p>
	 * @return 处理后的时间
	 */
	
	public static DataDriverFunction getTime() {
		//定义规则
		String regex = "time\\(((\\d{4}[-\\.年\\\\/][01]?\\d[-\\.月\\\\\\/][0123]?\\d日?"
				+ "( [012]?\\d[:时][0123456]?\\d分?"
				+ "([:分][0123456]?\\d秒?)?)?)|"
				+ "([012]?\\d[:时][0123456]?\\d分?([:分][0123456]?\\d秒?))|"
				+ "(\\d+)), ?(-?\\d+(\\.\\d+)?"
				+ "([ymwdhs]|(min)))+\\)";
		return new DataDriverFunction(regex, text -> {
			//获取公式中有效部分
			String str = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
			//将公式有效部分按照分隔符号进行切分
			String[] parameters = str.split(", ?");
			
			return disposeTime(parameters[0], parameters[1]);
		});
	}
	
	/**
	 * 定义对当前时间处理函数
	 * @return 处理后的时间
	 */
	public static DataDriverFunction getNowTime() {
		//定义规则
		String regex = "time\\(((-?\\d+(\\.\\d+)?([ymwdhs]|(min)))+)?\\)";
		return new DataDriverFunction(regex, text -> {
			//获取公式中有效部分
			String str = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
			
			return disposeTime("", str);
		});
	}
	
	/**
	 * 用于对时间进行处理
	 * @param timeText 设置时间的文本
	 * @param pattern 时间处理规则
	 * @return 返回处理后的时间
	 */
	private static String disposeTime(String timeText, String pattern) {
		Time time = new Time(timeText);
		time.addTime(pattern);
		
		return time.getFormatTime();
	}
}
