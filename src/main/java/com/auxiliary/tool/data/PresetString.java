package com.auxiliary.tool.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.common.enums.SexType;

/**
 * <p>
 * <b>文件名：PresetString.java</b>
 * </p>
 * <p>
 * <b>用途：</b>该工具提供一些常用的随机信息字符串，以及一些常用的生成特殊随机字符串的方法，以便于快速生成需要的随机字符串
 * </p>
 * <p>
 * <b>编码时间：2021年01月12日 上午9:43:09
 * </p>
 * <p>
 * <b>修改时间：2023年6月19日 上午9:43:09
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
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
     * 该方法用于根据指定的参数，生成与参数匹配的随机身份证号
     * <p>
     * 在生成随机身份证时，除性别枚举传入为null则默认为女性身份证外，其余参数在传入错误的值时，均在合理范围内随机生成一个参数，具体规则如下：
     * <ul>
     * <li>行政区划代码：三个行政区划代码若传入空、null或错误的区划代码时，则在现有的参数文件中，随机查询一个行政区划。但需要注意的时，若指定了下级区划代码，
     * 但上级随机的区划正好存在下级的区划代码时，则将生成其对应的区划，例如，当省级传入“32”，市级不传，而区级传入“11”时，若市级代码随机到“01”时，
     * 则将生成区划代码为“320111”的身份证</li>
     * <li>年龄段：传入年龄段后，则将在年龄段中随机生成一个年龄作为生日的年份，若起始年龄为小于1的数，则默认为18岁，若结束年龄小于1，则默认为65岁；
     * 若传入的起始年龄大于结束年龄，则自动将其调换</li>
     * <li>生日日期：月份若传入小于1，或大于12，则随机生成月份；日数若传入数值不合理时，则也在当前月份下随机一个日数</li>
     * <li>性别枚举：若性别枚举为空，则随机产生一个性别数（传入性别枚举时亦是随机产生一个和该性别相应的随机数）</li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>若行政区划为直辖市（例如北京市）或市级下无行政机构（例如新疆高昌区），则传入省份区划代码后，在市级行政区划代码传入4位的代码即可，例如，北京市朝阳区，
     * 则省级代码传入“11”，市级代码传入“0105”，区级代码不用传
     * </p>
     *
     * @param provinceCode 省级行政区划代码
     * @param cityCode     市级行政区划代码
     * @param zoneCode     区、县级行政区划代码
     * @param startAge     随机年龄段起始年龄
     * @param endAge       随机年龄段结束年龄
     * @param moon         生日月份
     * @param day          生日日数
     * @param sexType      性别
     * @return 参数对应的随机身份证号
     * @since autest 4.5.0
     */
    public static String identityCard(String provinceCode, String cityCode, String zoneCode, int startAge, int endAge,
            int moon, int day, SexType sexType) {
        StringBuffer cardText = new StringBuffer();

        // 定义随机数生成对象
        Random random = new Random();

        // 若起始年龄小于等于0，则默认取18岁作为起始年龄
        if (startAge <= 0) {
            startAge = 18;
        }
        // 若结束年龄小于等于0，则默认取65岁作为结束年龄
        if (endAge <= 0) {
            endAge = 65;
        }
        // 若起始年龄大于结束年龄，则对其进行交换
        if (startAge > endAge) {
            int temp = endAge;
            endAge = startAge;
            startAge = temp;
        }
        // 根据指定的起始与结束年龄，随机生成指定的年份
        int age = startAge;
        if (startAge != endAge) {
            age = random.nextInt(endAge - startAge + 1) + startAge;
        }
        int year = LocalDateTime.now().getYear() - age;
        String yearText = String.valueOf(year);

        // 校验月份是否合理，若超出合理范围，则随机生成月份
        String moonText = "";
        if (moon < 1 || moon > 12) {
            moonText = String.format("%02d", (random.nextInt(12 - 1 + 1) + 1));
        } else {
            moonText = String.format("%02d", moon);
        }

        // 根据年份与月份，生成当前月份最大日数
        int maxDayNum = 31;
        // 定义大月月份
        String bigMoon = "01,03,05,07,08,10,12";
        // 若当前月份不在大月月份中，则对最大日数进行修改
        if (!bigMoon.contains(moonText)) {
            // 若当前指定的月份为2月，则判断当前是否为闰年
            if (moon == 2) {
                maxDayNum = (year % 4 == 0 ? 29 : 28);
            } else {
                maxDayNum = 30;
            }
        }
        // 校验日数是否合理，若超出合理范围，则随机生成日数
        String dayText = "";
        if (day < 1 || day > maxDayNum) {
            dayText = String.format("%02d", (random.nextInt(maxDayNum - 1 + 1) + 1));
        } else {
            dayText = String.format("%02d", day);
        }

        // 读取配置文件
        Document configXml = null;
        try {
            configXml = new SAXReader()
                    .read(PresetString.class.getClassLoader()
                            .getResourceAsStream("com/auxiliary/tool/data/AdministrativeCode.xml"));
        } catch (DocumentException e) {
            throw new IllegalDataException("配置文件错误，请检查行政区域代码配置文件是否存在");
        }

        // 定义层级xpath
        String provinceXpath = "//province[@id='%s']";
        String cityXpath = "/city[@id='%s']";
        String zoneXpath = "/zone[@id='%s']";
        // 定义随机对象类对象，并添加省级元素
        RandomObject<Element> randomElement = new RandomObject<>();
        // 查找省级元素是否存在
        Element provinceElement = (Element) configXml.selectSingleNode(String.format(provinceXpath, provinceCode));
        // 若省级元素不存在，则随机获取一个省级节点
        if (provinceElement == null) {
            randomElement.addObject(configXml.getRootElement().elements());
            // 随机生成省级元素，并存储其编码
            provinceElement = randomElement.toObject().get();
            provinceCode = provinceElement.attributeValue("id");
        }

        Element cityElement = (Element) configXml.selectSingleNode(String.format(provinceXpath + cityXpath, provinceCode, cityCode));
        Element cityZoneElement = (Element) configXml.selectSingleNode(String.format(provinceXpath + zoneXpath, provinceCode, cityCode));
        // 若市级元素与区级市元素均不存在，则根据省级元素，产生一个随机的市级或区级市元素
        if (cityElement == null && cityZoneElement == null) {
            randomElement.clear(RandomObject.DEFAULT_NAME);
            randomElement.addObject(provinceElement.elements());
            Element e = randomElement.toObject().get();
            // 对元素的节点名称判断，并存储相应的市级或区级市节点
            if ("city".equals(e.getName())) {
                cityElement = e;
            } else {
                cityZoneElement = e;
            }
            cityCode = e.attributeValue("id");
        }

        // 若市级元素存在，则对区级元素随机提取；若市级元素不存在，则直接将区级代码置空（通过上面的代码判断后，不存在市级节点与区级市节点均为空的情况）
        if (cityElement != null) {
            Element zoneElement = (Element) configXml.selectSingleNode(
                    String.format(provinceXpath + cityXpath + zoneXpath, provinceCode, cityCode, zoneCode));
            if (zoneElement == null) {
                randomElement.clear(RandomObject.DEFAULT_NAME);
                randomElement.addObject(cityElement.elements());
                zoneCode = randomElement.toObject().get().attributeValue("id");
            }
        } else {
            zoneCode = "";
        }

        // 拼接行政区号
        cardText.append(provinceCode + cityCode + zoneCode);
        // 拼接生日
        cardText.append(yearText + moonText + dayText);
        // 拼接两位随机数
        cardText.append(String.format("%02d", random.nextInt(100)));
        // 根据性别，拼接性别随机数，若性别枚举为null，则随机生成尾数
        if (sexType == null) {
            cardText.append(RandomString.randomString(1, 1, StringMode.NUM));
        } else {
            cardText.append(sexType == SexType.MAN ? RandomString.randomString(1, 1, "13579")
                    : RandomString.randomString(1, 1, "24680"));
        }

        // 加权数
        int[] factors = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        // 校验位
        String[] parity = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        // 计算加权数
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int code = Integer.valueOf(cardText.substring(i, i + 1));
            int factor = Integer.valueOf(factors[i]);
            sum += (code * factor);
        }
        // 根据加权数添加校验位
        cardText.append(parity[(sum % 11)]);

        // 返回拼接的身份证号
        return cardText.toString();
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

    /**
     * 文本模板中，需要替换的占位符内容
     *
     * @since autest 4.3.0
     */
    private final static String TEMP_PLACEHOLDER = "%s";

    /**
     * 该方法用于根据指定的模板，和随机字符串的最小、最大生成长度，以及参与随机的随机字符串类对象集合，生成相应的随机字符串
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>在传入最小与最大生成长度后，其会根据随机字符串类对象的默认长度再次进行判断，若随机字符串类对象的总默认最小生成长度大于传入的最小生成长度
     * （或者传入最大生成长度大于随机字符串类对象的总默认最大长度），则最终会以默认的总长度取值区间随机生成一个长度，特别的，
     * 若传入的长度最大值小于总默认最小值（或长度最小值大于总默认长度最大值），则抛出异常</li>
     * <li>模板中替换随随机字符串的占位符为“%s”，其占位符号必须与传入的名称组数量对应，否则将会抛出异常（模板为空时则不进行判断）</li>
     * <li>模板中已存在的字符（除占位符）不被计入生成的随机字符串的长度中，若直接判断最终生成的长度，则可能会超出传入的长度区间，需要在传入时自行扣除相应的内容长度</li>
     * <li>当传入的最小或最大值小于1时，则表示该参数按照所有随机字符串的总默认值取</li>
     * </ol>
     * </p>
     *
     * @param minLength        随机字符串最小生成长度
     * @param maxLength        随机字符串最大生成长度
     * @param templet          模板
     * @param randomStringList 随机字符串类对象集合
     * @return 替换模板后的随机字符串组合
     * @since autest 4.3.0
     * @throws IllegalDataException 当名称组为空、名称未对应随机字符串类对象、名称组数量与模板占位符数量不符、传入的区间在随机字符串默认总区间外时抛出的异常
     */
    public static String createRandomGroupString(int minLength, int maxLength, String templet,
            List<RandomString> randomStringList) {
        // 判断是否传入组名称
        if (randomStringList == null || randomStringList.size() == 0) {
            throw new IllegalDataException("随机字符串组为空，无法生成随机字符串");
        }

        // 处理模板，避免存在传入null的问题
        templet = Optional.ofNullable(templet).orElse("");
        // 若存在模板，则判断模板中的占位符个数
        if (!templet.isEmpty()) {
            // 统计模板中占位符的个数
            int count = DisposeCodeUtils.countOccurrences(templet = Optional.ofNullable(templet).orElse(""),
                    TEMP_PLACEHOLDER);
            // 判断传入的组名称是否足够对其进行替换
            if (count != randomStringList.size()) {
                throw new IllegalDataException(String.format("随机字符串组个数与模板占位符个数不符。组个数：%d；占位符（%s）个数：%d",
                        randomStringList.size(), TEMP_PLACEHOLDER, count));
            }
        }

        // 若最大值小于等于0（无限制最大值），则对其进行转换（最小值无限制无需转换）
        if (maxLength < 1) {
            maxLength = Integer.MAX_VALUE;
        }
        // 若最大值小于最小值，则对两个数字进行转换
        if (maxLength < minLength) {
            int temp = minLength;
            minLength = maxLength;
            maxLength = temp;
        }

        // 存储随机字符串类的默认最小和最大长度
        int totalRandomMinLength = 0;
        int totalRandomMaxLength = 0;

        // 存储转换后的随机字符串
        String[] randomTexts = new String[randomStringList.size()];
        // 存储当前生成的字符串总长度
        int totalRandomStringLength = 0;
        // 存储当前随机字符串数组长度
        int randomStringsLength = randomStringList.size();
        // 存储已存在随机字符串的元素个数
        int exsitTextCount = 0;
        // 循环，转换名称并存储随机字符串类对象，计算随机字符串默认生成的最大、最小字符串长度
        for (int index = 0; index < randomStringsLength; index++) {
            // 获取随机字符串类对象，若名称不存在，则此处会异常
            RandomString rs = randomStringList.get(index);
            totalRandomMinLength += rs.getDefaultMinLength();
            totalRandomMaxLength += rs.getDefaultMaxLength();

            // 判断默认最大值与最小值是否一致，一致则直接生成随机字符串，并存储相应的余量
            if (rs.getDefaultMinLength() == rs.getDefaultMaxLength()) {
                randomTexts[index] = rs.toString();
                totalRandomStringLength += rs.getDefaultMinLength();
                exsitTextCount++;
            }
        }

        // 判断当前存储的随机字符串默认最大、最小默认生成长度是否与实际需要的长度相符
        // 若当前总最小值大于传入的最大值或当前总最大值小于传入的最小值，则抛出异常
        if (totalRandomMinLength > maxLength) {
            throw new IllegalDataException(
                    String.format("所有随机字符串类对象默认最小生成长度（%d）大于传入的随机字符串最大长度（%d）", totalRandomMinLength, maxLength));
        }
        if (totalRandomMaxLength < minLength) {
            throw new IllegalDataException(
                    String.format("所有随机字符串类对象默认最大生成长度（%d）小于传入的随机字符串最小长度（%d）", totalRandomMaxLength, minLength));
        }
        // 若当前的总最小值大于传入的最小值或总最大值小于传入的最大值（包含关系），则转换传入的最大、最小值
        minLength = Math.max(totalRandomMinLength, minLength);
        maxLength = Math.min(totalRandomMaxLength, maxLength);

        // 若当前最大最小值不相等，则生成当前字符串区间内的一个随机长度
        int length = (minLength == maxLength ? maxLength : new Random().nextInt(maxLength - minLength + 1) + minLength);

        // 存储能继续插入随机字符串类对象，值为剩余量（最大量 - 已生成的随机字符串数量）
        List<RandomStringGroupData> exsitZoneGroupList = new ArrayList<>();
        // 存储已存在随机字符串的元素个数

        // 再次遍历并生成每个组的随机字符串
        for (int index = 0; index < randomStringsLength; index++) {
            // 若已生成随机字符串，则跳过当前下标
            if (randomTexts[index] != null) {
                continue;
            }

            // 计算剩余量的平均值，以保证每个随机字符串均能被取到
            // 平均值的计算：(总生成长度 - 已生成的字符串长度) / (随机字符串组总个数 - 已生成字符串的个数)，其中已生成字符串的个数每次循环若不跳过，则其+1
            int avg = (length - totalRandomStringLength) / (randomStringsLength - exsitTextCount);

            // 直接调用结合中的获取方法，可跳过一次空判断（此前已做过一次判断，此处无需再做判断）
            RandomString rs = randomStringList.get(index);

            // 根据平均值，生成对应的长度下的随机字符串
            String randomText = "";
            if (rs.getDefaultMinLength() > avg) {
                randomText = rs.toString(rs.getDefaultMinLength());
            } else if (rs.getDefaultMinLength() <= avg && avg <= rs.getDefaultMaxLength()) {
                randomText = rs.toString(rs.getDefaultMinLength(), avg);
            } else {
                randomText = rs.toString();
            }

            // 存储字符串，并计算总长
            randomTexts[index] = randomText;
            totalRandomStringLength += randomText.length();

            // 计算当前随机字符串是否还有余量，若存在余量，则进行存储
            int diff = rs.getDefaultMaxLength() - randomText.length();
            if (diff > 0) {
                exsitZoneGroupList.add(new RandomStringGroupData(rs, diff, index));
            }

            exsitTextCount++;
        }

        // 判断当前生成的总长度是否满足生成的长度，若不满足，则按照剩余个数继续补足
        while (totalRandomStringLength < length) {
            // 打乱集合顺序，取乱序后的第一个元素（来自ChatGPT的改进建议）
            Collections.shuffle(exsitZoneGroupList);
            RandomStringGroupData group = exsitZoneGroupList.get(0);

            // 根据余量，生成随机字符串，并进行拼接
            String randomText = group.rs.toString(1, length - totalRandomStringLength);
            // 将生成的随机字符串进行拼接
            randomTexts[group.index] += randomText;
            totalRandomStringLength += randomText.length();

            // 重新计算余量，若余量不足，则将其移除exsitZoneGroupList集合
            int diff = group.surplusLength - randomText.length();
            if (diff == 0) {
                exsitZoneGroupList.remove(0);
            } else {
                group.surplusLength = diff;
            }
        }

        // 判断模板是否为空
        if (templet.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String str : randomTexts) {
                sb.append(str);
            }
            return sb.toString();
        } else {
            return String.format(templet, (Object[]) randomTexts);
        }
    }

    /**
     * <p>
     * <b>文件名：PresetString.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义随机字符串组的数据
     * </p>
     * <p>
     * <b>编码时间：2023年6月15日 下午3:16:05
     * </p>
     * <p>
     * <b>修改时间：2023年6月15日 下午3:16:05
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.3.0
     */
    private static class RandomStringGroupData {
        /**
         * 存储组名称
         *
         * @since autest 4.3.0
         */
        public RandomString rs;
        /**
         * 存储当前随机字符串剩余长度
         *
         * @since autest 4.3.0
         */
        public int surplusLength = 0;
        /**
         * 存储当前组在集合中的下标，以便于快速定位
         *
         * @since autest 4.3.0
         */
        public int index = -1;

        /**
         * 构造对象，初始化可随机的组数据
         *
         * @param groupName     组名称
         * @param surplusLength 随机字符串剩余长度
         * @param index         集合中的下标
         * @since autest 4.3.0
         */
        public RandomStringGroupData(RandomString rs, int surplusLength, int index) {
            this.rs = rs;
            this.surplusLength = surplusLength;
            this.index = index;
        }
    }
}
