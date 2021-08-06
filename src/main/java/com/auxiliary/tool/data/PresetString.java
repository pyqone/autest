package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Random;

/**
 * 该类提供一些已预设好的随机信息，可直接进行使用
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class PresetString {
	private static RandomString rs = new RandomString();

	/**
	 * 该方法用于生成随机的民用车牌号
	 * 
	 * @return 生成的车牌号字符串
	 */
	public static String carLicence() {
		String s = "";

		// 添加地域缩写
		rs.clear();
		rs.addMode(StringMode.AREA);
		s += rs.toString(1);

		// 添加地市一级代码，只添加“ABCDE”五个代码
		rs.clear();
		rs.addMode("ABCDE");
		s += rs.toString(1);

		// 添加后五位编号
		rs.clear();
		rs.addMode(StringMode.CAP, StringMode.NUM, StringMode.NUM, StringMode.NUM, StringMode.NUM, StringMode.NUM,
				StringMode.NUM, StringMode.NUM, StringMode.NUM);
		s += rs.toString(5);

		return s;
	}

	/**
	 * 该方法用于生成一个指定样式的随机车牌
	 * 
	 * @param carLicecenType
	 *            车牌的样式
	 * @return 生成的车牌号字符串
	 */
	public static String carLicence(CarLicecenType carLicecenType) {
		String s = "";

		// 判断车牌的样式
		switch (carLicecenType) {
		case CIVIL: {
			s += carLicence();
			break;
		}

		case POLICE: {
			rs.clear();
			rs.addMode(StringMode.AREA);
			s += rs.toString(1);

			// 添加地市一级代码，只添加“ABCDE”五个代码
			rs.clear();
			rs.addMode("ABCDE");
			s += rs.toString(1);

			rs.clear();
			rs.addMode(StringMode.NUM);
			s += rs.toString(4);
			s += "警";
			break;
		}

		case ELCHEE: {
			s += "使";
			rs.clear();
			rs.addMode(StringMode.NUM);
			s += rs.toString(6);
			break;
		}

		case ENERGY: {
			rs.clear();
			rs.addMode(StringMode.AREA);
			s += rs.toString(1);

			// 添加地市一级代码，只添加“ABCDE”五个代码
			rs.clear();
			rs.addMode("ABCDE");
			s += rs.toString(1);

			rs.clear();
			rs.addMode("DF");
			s += rs.toString(1);

			rs.clear();
			rs.addMode(StringMode.NUM);
			s += rs.toString(5);
			break;
		}
		}

		return s;
	}

	/**
	 * 该方法用于随机生成一个身份证号
	 * 
	 * @return 生成的身份证号码
	 */
	public static String identityCard() {
		StringBuilder sb = new StringBuilder();
		RandomString rs = new RandomString("123456789");
		
		//省份
		int[] province = {13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91};
		//城市
		String[] city = {"01", "02", "03"};
		//区县
		String[] county = {"01", "02", "03", "04", "05"};
		//生日年份前两位
		int[] year = {19};
		
		//加权数
		int[] factors = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		//校验位
		String[] parity = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
		
		//随机添加一个省份
		sb.append(province[new Random().nextInt(province.length)]);
		//随机添加一个城市
		sb.append(city[new Random().nextInt(city.length)]);
		//随机添加一个区县
		sb.append(county[new Random().nextInt(county.length)]);
		//随机生日年份
		sb.append(year[new Random().nextInt(year.length)]).append(rs.toString(2));
		//随机生日月份及日子
		sb.append("0" + rs.toString(1)).append("0" + rs.toString(1));
		//随机三位数
		sb.append(rs.toString(3));
		
		//计算加权数
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			int code = Integer.valueOf(sb.substring(i, i + 1));
			int factor = Integer.valueOf(factors[i]);
			sum += (code * factor);
		}
		//根据加权数添加校验位
		sb.append(parity[(sum % 11)]);
		
		return sb.toString();
	}

	/**
	 * 生成姓名
	 * @return 姓名
	 */
	public static String name() {
		String s;

		rs.clear();
		rs.addMode(StringMode.SURNAME);
		s = rs.toString(1);

		rs.clear();
		rs.addMode(StringMode.CH);
		s += rs.toString(1, 2);

		return s;
	}

	/**
	 * 生成手机号码
	 * @return 手机号码
	 * @deprecated 生成手机号码由{@link #mobleNumber(MobleNumberType)}方法代替
	 */
	@Deprecated
	public static String phoneID(PhoneType phoneType) {
		if (phoneType == PhoneType.MOBLE) {
			return "139" + new RandomString(StringMode.NUM).toString(8);
		} else {
			String s = "077";
			rs.clear();
			rs.addMode("12345");
			s += rs.toString(1);
			
			rs.clear();
			
			rs.addMode("23456789");
			s += rs.toString(7);
			
			return s;
		}
	}
	
	/**
	 * 根据运营商，生成相应运营商号段的随机号码
	 * @param mobleNumberType 运营商号段枚举
	 * @return 相应运营商的随机号码
	 */
	public static String mobleNumber(MobleNumberType mobleNumberType) {
		RandomString rs = new RandomString(StringMode.NUM);
		ArrayList<String> regexList = mobleNumberType.getRegex();
		
		//根据运营商号码开头规则，随机选择一个号段
		String regex = regexList.get(new Random().nextInt(regexList.size()));
		//再根据号段的长度，生成相应位数的尾号
		return regex + rs.toString(11 - regex.length());
	}
}
