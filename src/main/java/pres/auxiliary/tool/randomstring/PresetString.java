package pres.auxiliary.tool.randomstring;

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
	 * @param type
	 *            车牌的样式
	 * @return 生成的车牌号字符串
	 */
	public static String carLicence(CarLicecenType type) {
		String s = "";

		// 判断车牌的样式
		switch (type) {
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
	public static String IdentityCard() {
		String s = "45030119";

		rs.clear();
		rs.addMode(StringMode.NUM);
		s += rs.toString(2);

		Random r = new Random();

		// 生成月
		int i = (1 + r.nextInt(11));
		// 判断生成的随机字符串是否小于10，小于10则向s中补全一个0
		if (i < 10) {
			s += "0";
		}
		s += i;

		// 生成日
		i = (1 + r.nextInt(27));
		// 判断生成的随机字符串是否小于10，小于10则向s中补全一个0
		if (i < 10) {
			s += "0";
		}
		s += i;

		s += rs.toString(4);

		return s;
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
	 */
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

}
