package pres.auxiliary.tool.string;

public enum RegionType {
	/**
	 * 北京市
	 */
	BEIJING("北京市", "京", "11"),
	/**
	 * 天津市
	 */
	TIANJIN("天津市", "津", "12"),
	/**
	 * 河北省
	 */
	HEIBEI("河北省", "冀", "13"),
	/**
	 * 山西省
	 */
	SHANXI_JIN("山西省", "晋", "14"), 
	/**
	 * 内蒙古自治区
	 */
	NEIMENGGU("内蒙古自治区", "蒙", "15"), 
	/**
	 * 辽宁省
	 */
	LIAONING("辽宁省", "辽", "21"), 
	/**
	 * 吉林省
	 */
	JILIN("吉林省", "吉", "22"), 
	/**
	 * 黑龙江省
	 */
	HEILONGJIANG("黑龙江省", "黑", "23"), 
	/**
	 * 上海市
	 */
	SHANGHAI("上海市", "沪", "31"), 
	/**
	 * 江苏省
	 */
	JIANGSU("江苏省", "苏", "32"), 
	/**
	 * 浙江省
	 */
	ZHEJIANG("浙江省", "浙", "33"), 
	/**
	 * 安徽省
	 */
	ANHUI("安徽省", "皖", "34"), 
	/**
	 * 福建省
	 */
	FUJIAN("福建省", "闽", "35"), 
	/**
	 * 江西省
	 */
	JIANGXI("江西省", "赣", "36"), 
	/**
	 * 山东省
	 */
	SHANDONG("山东省", "鲁", "37"), 
	/**
	 * 河南省
	 */
	HENAN("河南省", "豫", "41"), 
	/**
	 * 湖北省
	 */
	HUBEI("湖北省", "鄂", "42"), 
	/**
	 * 湖南省
	 */
	HUNAN("湖南省", "湘", "43"), 
	/**
	 * 广东省
	 */
	GUANGDONG("广东省", "粤", "44"), 
	/**
	 * 广西壮族自治区
	 */
	GUANGXI("广西壮族自治区", "桂", "45"), 
	/**
	 * 海南省
	 */
	HAINAN("海南省", "琼", "46"), 
	/**
	 * 重庆市
	 */
	CHONGQING("重庆市", "渝", "50"), 
	/**
	 * 四川省
	 */
	SICHUAN("四川省", "川", "51"), 
	/**
	 * 贵州省
	 */
	GUIZHOU("贵州省", "贵", "52"), 
	/**
	 * 云南省
	 */
	YUNNAN("云南省", "云", "53"), 
	/**
	 * 西藏自治区
	 */
	XIZANG("西藏自治区", "藏", "54"), 
	/**
	 * 陕西省
	 */
	SHANXI_SHAN("陕西省", "陕", "61"), 
	/**
	 * 甘肃省
	 */
	GANSU("甘肃省", "甘", "62"), 
	/**
	 * 青海省
	 */
	QINGHAI("青海省", "青", "63"), 
	/**
	 * 宁夏回族自治区
	 */
	NINGXIA("宁夏回族自治区", "宁", "64"), 
	/**
	 * 新疆维吾尔自治区
	 */
	XINJIANG("新疆维吾尔自治区", "新", "65");
	
	private String name;
	private String abbreviation;
	private String number;
	
	RegionType(String name, String abbreviation, String number) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.number = number;
	}
	
	/**
	 * 用于返回地区名称
	 * @return 地区名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 用于返回地区缩写
	 * @return 地区缩写
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * 用于返回地区编码
	 * @return 地区编码
	 */
	public String getNumber() {
		return number;
	}
	//TODO 部分方法准备写入XML中
}
