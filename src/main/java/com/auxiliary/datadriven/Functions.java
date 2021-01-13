package com.auxiliary.datadriven;

import com.auxiliary.tool.data.CarLicecenType;
import com.auxiliary.tool.data.MobleNumberType;
import com.auxiliary.tool.data.PresetString;
import com.auxiliary.tool.data.RandomString;
import com.auxiliary.tool.data.StringMode;
import com.auxiliary.tool.date.Time;

/**
 * <p><b>文件名：</b>Functions.java</p>
 * <p><b>用途：</b>
 * 提供部分默认的函数，在编写数据驱动时，可直接调用相应的方法
 * </p>
 * <p><b>编码时间：</b>2020年6月16日下午7:25:36</p>
 * <p><b>修改时间：</b>2020年6月16日下午7:25:36</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class Functions {
	/**
	 * 定义生成随机字符串函数
	 * <p>
	 * <b>用法：</b>
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
	 * 定义对指定时间处理的函数
	 * <p>
	 * <b>用法：</b>${time(时间, 增减时间方式)}
	 * </p>
	 * <p>
	 * <b>参数解释：</b>
	 * <ul>
	 * 	<li>时间：指定的时间，可传入年、月、日、时、分、秒，允许只传入“年、月、日”，也允许只传入“时、分、秒”。
	 * 当只传入“年、月、日”时，表示当前日期的0时0分0秒；当只传入“时、分、秒”时，表示当天日期的时间；当传入
	 * 完整的时间时，其“年、月、日”与“时、分、秒”之间用空格隔开。<br>
	 * “年、月、日”之间，允许使用的分隔符有：年（或月或日）、“/”、“\”、“.”和“-”<br>
	 * “时、分、秒”之间，允许使用的分隔符有：时（或分或秒）、“:”</li>
	 * 	<li>增减时间方式：指定对时间进行增减的方式，使用英文单位表示需要操作的内容，包括：
	 * y（年）、m（月）、w（周）、d（日）、h（小时）、s（秒）、min（分钟）。不带符号表示增加时间，带“-”符号
	 * 表示减少时间，允许传入小数，允许使用连续式，每个单位之间没有顺序，允许一个单位传入多个（如“9d-2d-5m8y-3.66min”）</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>示例：</b>
	 * <ul>
	 * <li>${time(2020-06-08 11:20:12, 1d)}——表示生成“2020-06-08 11:20:12”增加1天的时间</li>
	 * <li>${time(2020年06月08日 11时20分12秒, -1d2.3y)}——表示生成“2020年06月08日 11时20分12秒”减少一天并增加2.3年的时间</li>
	 * <li>${time(2020-06-08,1d)}——表示生成“2020-06-08 00:00:00”增加1天的时间</li>
	 * <li>${time(11:20:12, 1h)}——表示生成“当天11:20:12”增加1天的时间</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>注意：</b>计算小数点的日期时，可能存在精度丢失，计算跨度越大，精度丢失越多
	 * </p>
	 * @return {@link DataDriverFunction}类对象
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
	 * 定义对当前时间处理的函数
	 * <p>
	 * <b>用法：</b>${time(增减时间方式)}
	 * </p>
	 * <p>
	 * <b>参数解释：</b>
	 * <ul>
	 * <li>增减时间方式：指定对时间进行增减的方式，使用英文单位表示需要操作的内容，包括：
	 * y（年）、m（月）、w（周）、d（日）、h（小时）、s（秒）、min（分钟）。不带符号表示增加时间，带“-”符号
	 * 表示减少时间，允许传入小数，允许使用连续式，每个单位之间没有顺序，允许一个单位传入多个（如“9d-2d-5m8y-3.66min”）。
	 * 该参数允许不传，当不传入该参数时返回为当前时间
	 * </li>
	 * </ul>
	 * <p>
	 * <b>示例：</b>
	 * <ul>
	 * 	<li>${time(1d)}——表示生成当前时间增加1天的时间</li>
	 * 	<li>${time(-1d2.3y)}——表示生成当前时间减少一天并增加2.3年的时间</li>
	 * 	<li>${time()}——表示生成当前时间</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>注意：</b>计算小数点的日期时，可能存在精度丢失，计算跨度越大，精度丢失越多
	 * </p>
	 * @return {@link DataDriverFunction}类对象
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
	
	/**
	 * 定义标记当前元素为空元素
	 * <p>
	 * 用法：${ }（花括号内存在一个空格）
	 * </p>
	 * @return {@link DataDriverFunction}类对象
	 */
	public static DataDriverFunction addEmpty() {
		//定义规则
		String regex = " ";
		return new DataDriverFunction(regex, text -> {
			return "";
		});
	}
	
	/**
	 * 定义生成随机18位长度的身份证信息函数
	 * <p>
	 * 用法：${idcard}或${身份证}
	 * </p>
	 * @return {@link DataDriverFunction}类对象
	 */
	public static DataDriverFunction randomIdCard() {
		String regex = "(idcard)|(身份证)";
		return new DataDriverFunction(regex, text -> PresetString.identityCard());
	}
	
	/**
	 * 定义生成随机手机号码函数
	 * <p>
	 * 用法：${moble.运营商简写或名称}或${手机号码.运营商简写或名称}
	 * </p>
	 * <p>
	 * <b>参数解释：</b>
	 * <ul>
	 * <li>运营商简写或名称：表示当前随机哪个运营商的手机号码段，支持中国移动（yd或移动）、
	 * 中国联通（lt或联通）、中国电信（dx或电信）
	 * </li>
	 * </ul>
	 * <p>
	 * <b>示例：</b>
	 * <ul>
	 * 	<li>${moble}或${mo}或${手机}或${手机号码}——表示生成中国移动号码段的随机手机号码</li>
	 * 	<li>${moble.联通}或${mo.联通}或${手机.联通}或${手机号码.联通}——表示生成中国联通号码段的随机手机号码</li>
	 * 	<li>${moble.dx}或${mo.dx}或${手机.dx}或${手机号码.dx}——表示生成中国电信号码段的随机手机号码</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ul>
	 * 	<li>“moble”可简写为“mo”，“手机号码”可简写为“手机”</li>
	 * 	<li>省略运营商简写或名称则默认为生成中国移动随机手机号码</li>
	 * </ul>
	 * </p>
	 * @return {@link DataDriverFunction}类对象
	 */
	public static DataDriverFunction randomMoble() {
		String regex = "((mo(ble)?)|(手机(号码)?))(\\.(((dx)|(电信))|((yd)|(移动))|((lt)|(联通))))?";
		return new DataDriverFunction(regex, text -> {
			int index = text.indexOf(".");
			//若切分得到的字符串数组只有一个元素，则表示字符串不包含后缀，按照默认的参数进行返回
			if (index > -1) {
				//若存在后缀，则按照后缀的参数对手机号码进行返回
				String type = text.toLowerCase().substring(index + 1, text.length());
				switch(type) {
				case "dx":
				case "电信":
					return PresetString.mobleNumber(MobleNumberType.CHINA_TELECOM);
				case "lt":
				case "联通":
					return PresetString.mobleNumber(MobleNumberType.CHINA_UNICOM);
				case "yd":
				case "移动":
				default:
					return PresetString.mobleNumber(MobleNumberType.CHINA_MOBILE);
				}
			} else {
				return PresetString.mobleNumber(MobleNumberType.CHINA_MOBILE);
			} 
		});
	}
	
	/**
	 * 定义生成随机姓名函数
	 * <p>
	 * 用法：${name}或${姓名}
	 * </p>
	 * @return {@link DataDriverFunction}类对象
	 */
	public static DataDriverFunction randomName() {
		String regex = "(name)|(姓名)";
		return new DataDriverFunction(regex, text -> PresetString.name());
	}
	
	/**
	 * 定义生成随机随机车牌号码函数
	 * <p>
	 * 用法：${car.车牌简写或名称}或${车牌.车牌简写或名称}
	 * </p>
	 * <p>
	 * <b>参数解释：</b>
	 * <ul>
	 * <li>车牌简写或名称：表示当前随机产生哪一类车牌，支持民用车牌（ci或民用）、警车车牌（po或警车）
	 * 、大使馆车牌（el或使馆）、新能源车牌（en或新能源）
	 * </li>
	 * </ul>
	 * <p>
	 * <b>示例：</b>
	 * <ul>
	 * 	<li>${car}或${车牌}——表示生成民用车牌</li>
	 * 	<li>${car.po}或${车牌.po}——表示生成警车车牌</li>
	 * 	<li>${car.新能源}或${车牌.新能源}——表示生成新能源车牌</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>注意：</b>省略车牌简写或名称则默认为生成民用车牌
	 * </p>
	 * @return {@link DataDriverFunction}类对象
	 */
	public static DataDriverFunction randomCarId() {
		String regex = "((car)|(车牌))(\\.(((ci)|(民用))|((po)|(警车))|((el)|(使馆))|((en)|(新能源))))?";
		return new DataDriverFunction(regex, text -> {
			int index = text.indexOf(".");
			//若切分得到的字符串数组只有一个元素，则表示字符串不包含后缀，按照默认的参数进行返回
			if (index > -1) {
				//若存在后缀，则按照后缀的参数对手机号码进行返回
				String type = text.toLowerCase().substring(index + 1, text.length());
				switch(type) {
				case "en":
				case "新能源":
					return PresetString.carLicence(CarLicecenType.ENERGY);
				case "po":
				case "警车":
					return PresetString.carLicence(CarLicecenType.POLICE);
				case "el":
				case "使馆":
					return PresetString.carLicence(CarLicecenType.ELCHEE);
				case "ci":
				case "民用":
				default:
					return PresetString.carLicence(CarLicecenType.CIVIL);
					
				}
			} else {
				return PresetString.carLicence(CarLicecenType.CIVIL);
			} 
		});
	}
}
