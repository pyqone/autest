package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

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
	 * @param carLicecenType 车牌的样式
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

		// 省份
		int[] province = { 13, 14, 15, 21, 22, 23, 31, 32, 33, 34, 35, 36, 37, 41, 42, 43, 44, 45, 46, 50, 51, 52, 53,
				54, 61, 62, 63, 64, 65, 71, 81, 82, 91 };
		// 城市
		String[] city = { "01", "02", "03" };
		// 区县
		String[] county = { "01", "02", "03", "04", "05" };
		// 生日年份前两位
		int[] year = { 19 };

		// 加权数
		int[] factors = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		// 校验位
		String[] parity = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

		// 随机添加一个省份
		sb.append(province[new Random().nextInt(province.length)]);
		// 随机添加一个城市
		sb.append(city[new Random().nextInt(city.length)]);
		// 随机添加一个区县
		sb.append(county[new Random().nextInt(county.length)]);
		// 随机生日年份
		sb.append(year[new Random().nextInt(year.length)]).append(rs.toString(2));
		// 随机生日月份及日子
		sb.append("0" + rs.toString(1)).append("0" + rs.toString(1));
		// 随机三位数
		sb.append(rs.toString(3));

		// 计算加权数
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			int code = Integer.valueOf(sb.substring(i, i + 1));
			int factor = Integer.valueOf(factors[i]);
			sum += (code * factor);
		}
		// 根据加权数添加校验位
		sb.append(parity[(sum % 11)]);

		return sb.toString();
	}

	/**
	 * 生成姓名
	 *
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
	 *
	 * @return 手机号码
	 * @deprecated 已使用{@link #mobleNumber(MobleNumberType)}方法代替
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
	 *
	 * @param mobleNumberType 运营商号段枚举
	 * @return 相应运营商的随机号码
	 */
	public static String mobleNumber(MobleNumberType mobleNumberType) {
		RandomString rs = new RandomString(StringMode.NUM);
		ArrayList<String> regexList = mobleNumberType.getRegex();

		// 根据运营商号码开头规则，随机选择一个号段
		String regex = regexList.get(new Random().nextInt(regexList.size()));
		// 再根据号段的长度，生成相应位数的尾号
		return regex + rs.toString(11 - regex.length());
	}

	/**
	 * 用于生成一个指定范围内的数字，并根据条件进行前位数补0
	 * <p>
	 * 例如，生成的数字为1，传入的最大值为12，并设置需要补0，则最终返回的结果为：01
	 * </p>
	 *
	 * @param minNum    范围最小值
	 * @param maxNum    范围最大值
	 * @param isZeroize 是否需要高位补0
	 * @return 生成的随机数字
	 */
	public static String randomNumber(int minNum, int maxNum, boolean isZeroize) {
		// 若最大值比最小值小，则调换两个值
		if (maxNum < minNum) {
			int tempNum = minNum;
			minNum = maxNum;
			maxNum = tempNum;
		}

		// 生成随机数字
		int randomNum = new Random().nextInt(maxNum - minNum + 1) + minNum;

		// 判断是否需要补0，若需要补0，则根据最大值的位数对生成的随机数字补0
		if (isZeroize) {
			// 获取最小值与最大值的位数（由于存在负数的情况，故不能直接单纯从最大值判断位数）
			int minLength = String.valueOf(Math.abs(minNum)).length();
			int maxLength = String.valueOf(Math.abs(maxNum)).length();
			int length = Math.max(minLength, maxLength);

			// 计算补0的个数
			int zeroLength = length - String.valueOf(Math.abs(randomNum)).length();
			String zeroText = "";
			for (int i = 0; i < zeroLength; i++) {
				zeroText += "0";
			}

			// 判断生成的随机数字是否小于0，若小于0，则在负号后面补0
			return String.format("%s%s%d", randomNum < 0 ? "-" : "", zeroText, Math.abs(randomNum));
		} else {
			return String.valueOf(randomNum);
		}
	}

	/**
	 * 用于生成指定范围内的随机的小数
	 * <p>
	 * 该方法可根据传入的小数的位数，自动判断并生成相应位数的随机小数。
	 * </p>
	 * <p>
	 * <b>注意：</b>由于java中的double在传入整数或者最后一位是0的小数时，默认会将其转换为一位或
	 * 去0的处理，导致最终生成的精度不正确，为解决这一问题，故将传参使用字符串的形式代替
	 * </p>
	 * @param minNumText 最小值文本
	 * @param maxNumText 最大值文本
	 * @return 生成的随机小数文本
	 */
	public static String randomDecimals(String minNumText, String maxNumText) {
		// 将数值转换为小数
		double maxNum = Double.valueOf(maxNumText);
		double minNum = Double.valueOf(minNumText);

		// 判断两个数字是否都为整数
		if (!minNumText.contains(".") && !maxNumText.contains(".")) {
			return randomNumber(Integer.valueOf(minNumText), Integer.valueOf(maxNumText), false);
		}

		// 若最大值比最小值小，则调换两个值
		if (maxNum < minNum) {
			double tempNum = minNum;
			minNum = maxNum;
			maxNum = tempNum;
		}

		// 获取最小值与最大值小数点后的位数，计算最大位数
		int length = Math.max(minNumText.substring(minNumText.indexOf(".") + 1).length(),
				maxNumText.substring(maxNumText.indexOf(".") + 1).length());
		// 计算精确位
		String zeroText = "0.";
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				zeroText += "1";
			} else {
				zeroText += "0";
			}
		}

		// 生成随机数字
		double randomNum = RandomUtils.nextDouble(0, maxNum - minNum + Double.valueOf(zeroText)) + minNum;

		// 判断是否需要补0，若需要补0，则根据最大值的位数对生成的随机数字补0
		String randomNumText = String.valueOf(randomNum);


		// 判断生成的随机数字是否小于0，若小于0，则在负号后面补0
		return randomNumText.substring(0, randomNumText.indexOf(".") + length + 1);
	}

    /**
     * 该方法用于根据指定的起始字符与结束字符，通过在{@link StringMode}中找到其字符对应的模型，之后按照顺序，根据指定的步长，截取模型对应的文本内容进行返回
     * <ul>
     * 根据字符查找模型的机制为，将字符与{@link StringMode}枚举中的所有模型进行对比，直到找到字符所在的模型为止，则存在以下几种情况：
     * <li>获取到的起始模型与结束模型相同，且均能找到模型时，此时存在以下3种情况：
     * <ol>
     * <li>当起始字符与结束字符相同时，则返回该字符</li>
     * <li>当起始字符所在位置小于结束字符所在位置时，则按照模型顺序截取相应段落的字符串，例如，传入{@code createOrderlyText("b", "d", 1)}，则返回“bcd”</li>
     * <li>当起始字符所在位置大于结束字符所在位置时，则将模型反序，再按照顺序截取相应段落的字符串，例如，传入{@code createOrderlyText("d", "b", 1)}，则返回“dcb”</li>
     * </ol>
     * </li>
     * <li>获取到的起始模型与结束模型不相同，且均能找到模型时，则截取起始字符所在模型从起始字符开始到模型末尾，再截取结束字符所在模型的开头到结束字符，将其拼接，例如，传入{@code createOrderlyText("w", "3", 1)}，则返回“wxyz123”</li>
     * <li>当起始字符未找对应模型时，则获取模型字符串的第一位到结束下标的字符串，例如，传入{@code createOrderlyText(";", "c", 1)}，则返回“abc”</li>
     * <li>当结束字符未找对应模型时，则获取模型字符串的起始字符到末尾的字符串，例如，传入{@code createOrderlyText("x", ";", 1)}，则返回“xyz”</li>
     * </ul>
     * <p>
     * 步长表示在截取到的字符串中，再取相应间隔的字符再次组成字符串，例如，传入{@code createOrderlyText("1", "9", 2)}，则返回“13579”
     * </p>
     * 
     * @param startChar 起始字符
     * @param endChar   结束字符
     * @param step      步长
     * @return 通过查找模型根据步长拼接的字符串
     * @since autest 3.5.0
     */
    public static String createOrderlyText(String startChar, String endChar, int step) {
        // 获取开始与结束字符串模型
        StringMode startMode = StringMode.judgeMode(startChar);
        StringMode endMode = StringMode.judgeMode(endChar);

        // 获取开始与结束字符在模型中的位置
        int startModeIndex = Optional.ofNullable(startMode).map(mode -> mode.getSeed().indexOf(startChar)).orElse(-1);
        int endModeIndex = Optional.ofNullable(endMode).map(mode -> mode.getSeed().indexOf(endChar)).orElse(-1);

        // 存储截取后的字符串
        String modeText = "";

        // 若两模型相同，且均不为null时（下标为-1），则根据模型所在的位置，截取相应的字符串
        if (startMode == endMode && startModeIndex != -1) {
            String seed = startMode.getSeed();
            // 判断下标存在以下三种情况：
            // 1 起始下标等于结束下标时，则存储字符串模型中该下标对应的字符
            // 2 起始下标小于结束下标时，则获取字符串模型中从起始下标到结束下标的字符串
            // 3 起始下标大于结束下标时，则将字符串模型反序，再获取反序后的模型中从起始下标到结束下标的字符串
            if (startModeIndex == endModeIndex) {
                modeText = String.valueOf(seed.charAt(startModeIndex));
            } else if (startModeIndex < endModeIndex) {
                modeText = seed.substring(startModeIndex, endModeIndex + 1);
            } else {
                StringBuilder reverseSeed = new StringBuilder(seed).reverse();
                endModeIndex = reverseSeed.indexOf(endChar);
                startModeIndex = reverseSeed.indexOf(startChar);

                modeText = new StringBuilder(seed).reverse().substring(startModeIndex, endModeIndex + 1);
            }
        } else {
            // 若两模型不相同，则获取起始模型从起始下标（如果存在），直至起始模型结尾；获取结束模型开始，直至结束下标（如果存在）
            if (startModeIndex != -1) {
                modeText = startMode.getSeed().substring(startModeIndex);
            }
            if (endModeIndex != -1) {
                modeText += endMode.getSeed().substring(0, endModeIndex + 1);
            }
        }
        
        // 若组合的模型文本长度小于2或者步长小于2，则直接返回模型文本
        if (modeText.length() < 2 || step < 2) {
            return modeText;
        }

        // 根据步长，对模型文本进行分解，得到最终返回的模型文本
        StringBuilder returnText = new StringBuilder();
        for (int i = 0; i < modeText.length(); i += step) {
            returnText.append(modeText.charAt(i));
        }

        return returnText.toString();
	}
}
