package test.javase;

import pres.auxiliary.tool.string.CarLicecenType;
import pres.auxiliary.tool.string.PresetString;
import pres.auxiliary.tool.string.RandomString;
import pres.auxiliary.tool.string.StringMode;

public class TestPresetStr {
	public static void main(String[] args) {
		System.out.println("民用车牌：" + PresetString.carLicence());
		System.out.println("警用车牌：" + PresetString.carLicence(CarLicecenType.POLICE));
		System.out.println("使馆车牌：" + PresetString.carLicence(CarLicecenType.ELCHEE));
		System.out.println("新能源汽车车牌：" + PresetString.carLicence(CarLicecenType.ENERGY));
		System.out.println("身份证：" + PresetString.IdentityCard());
		System.out.println("姓名：" + PresetString.name());
		System.out.println("手机号码：139" + new RandomString(StringMode.NUM).toString(8));
	}
}
