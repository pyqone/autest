package pres.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Arrays;

public enum MobleNumberType {
	/**
	 * 中国电信运营商号码段
	 */
	CHINA_TELECOM(133, 1349, 149, 153, 162, 1700, 1701, 1702, 173, 17400, 17401, 17402, 17403, 17404, 17405, 177, 180, 181, 189, 191, 193, 199), 
	/**
	 * 中国移动运营商号码段
	 */
	CHINA_MOBILE(1340, 1341, 1342, 1343, 1344, 1345, 1346, 135, 136, 137, 138, 139, 147, 148, 150, 151, 152, 1570, 1571, 1572, 1573, 1574, 1575, 1576, 1577, 1578, 1579, 158, 159, 165, 1703, 1705, 1706, 172, 178, 182, 183, 184, 187, 188, 198), 
	/**
	 * 中国联通运营商号码段
	 */
	CHINA_UNICOM(130, 131, 132, 145, 146, 155, 156, 166, 167, 1704, 1707, 1708, 1709, 171, 175, 176, 185, 186);
	
	/**
	 * 用于表示当前运营商号码段正则表达式
	 */
	ArrayList<String> numberList = new ArrayList<>();

	/**
	 * 用于初始化数据
	 * @param nums 号码段
	 */
	private MobleNumberType(int...nums) {
		//添加数据
		Arrays.stream(nums).forEach(num -> {
			numberList.add(String.valueOf(num));
		});
	}

	/**
	 * 用于返回运营商号码段
	 * @return 号码段
	 */
	public ArrayList<String> getRegex() {
		return numberList;
	}
}
