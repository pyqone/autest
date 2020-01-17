package test.selenium.cookies;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import pres.auxiliary.selenium.browers.FirefoxBrower;
import pres.auxiliary.selenium.event.Event;

/**
 * <p>
 * <b>文件名：</b>TestCookies.java
 * </p>
 * <p>
 * <b>用途：</b>用于实验selenium结合Cooikes
 * </p>
 * <p>
 * <b>编码时间：</b>2018年11月28日 下午2:55:20
 * </p>
 * 
 * @author 彭宇琦
 */
public class TestCookies {
//	static JavascriptExecutor js;
//	static FirefoxBrower fb;
//	static Event e;

	@BeforeClass
	public static void init() {
		// fb = new FirefoxBrower("",
		// "http://www.runoob.com/try/try.php?filename=tryjs_write_over");
		// js = (JavascriptExecutor) (fb.getDriver());

		// e = new Event(fb.getDriver());
	}

	@AfterClass
	public static void close() {
		//fb.close();
	}

	@After
	public void sleep() throws Exception {
		//Thread.sleep(2000);
	}

	@Test
	public void test1() throws Exception {
		FirefoxDriver d = new FirefoxDriver();
		d.manage().addCookie(new Cookie("ASP.NET_SessionId", "5zl4d3p1bug5zj5ftr5itwac"));
		d.manage().addCookie(new Cookie("currentmoduleId", "undefined"));
		d.manage().addCookie(new Cookie("Learun_LoginUserKey_2016_V6.1", "13C2C1A702AE2DE0196A2308525E16C697135B38495FD82D4124C6928A9AD55D5875EA6D6CE157897555A4BC2B925A1C153272E08ED46D711972D7BCF1FFC103B9D3E528AB516BA5E0E348F60D46184704E7A41113A9B4227F2669C043136EEC63A9F5D5A9BAE12095EBFE06899A85D8C91F3C372D4FF14DA6A447CA1D8E911E1A2094A6B5B1DE26B80EEE9B773C67890EC9E5D1412461607463F8797601858990EAF8B9E53DDD3EF2AD3AC7DF5017BF54C479D0402C2B010034BB4EC6FE68D158F12FEB2E0652870F107748FABC3E00857298C9ED31343FA1D4CD575776BE33CBC65D77440CC800A55F66A3117563856C5DC7CB8EE560F4886BAD85C830801F030DF26E94719A267C5A06F55E215087666273CDE674F7D667516D4C9931C6B4E0D936AFCAB944EF9D5F46DF9430A93C1D53E896D6B78CB69289A7F67E451E0449F5E95A799AB2A55A03F76E16CB9A8F6F99F1454B1864F5B75DF4356ABD33042FA6C00D84DF04F33427671FE1A743C3417D2E415687F72A92774AF2FE9CBC1CBCAD8FED19D3C5460BC119EFF64966C3F6952704A720F1416F52BF99984854DE9C5A6C650E9B39E7BE8D8F8F7B591464A8989597471ED19901159CAF5B374D792D6D7D5943FDFFD7D70740801A2256FFCDECA2428212A03D36164FFCBE7C3BBD7719E71A8FBBB6F268672111450E83D8054BA510BF8D6FA0A0C13757BA31FF3AE98C310B70B3B0C621501F827DACD391141639E1CF3E8E479F7C5A3202E49F6F30A87C3B3D083FBF1926F28E2F58A5B8189633A87BF50498865A587091467003FE848A321564CFB89F31A85A781150DF6763984EACEBAD2A5FC6EE2992D5D13B2DE78E77464A72B3213DC73AAEEA7DA9CA1CCFC56C8A0338E24E5259B406B3F40FD15664B2D5BBDA2BCAC9DBAF7ABC67C8ABAAEED4685447C2E74BDE9D430650EED878FA0A8BD1181BD57EB3E0939B98B807A57FCBB033BA0F12760A5B8852CBC49AB2504EB08D3A8B719EDFA2ADF498"));
		d.get("http://218.21.102.9:999/Home/AdminDefault");
		//System.out.println(d.manage().getCookies());
		//Thread.sleep(10000);
		//d.findElement(By.xpath("//*[@id=\"btnlogin\"]")).click();
		//Thread.sleep(5000);
		//System.out.println(d.manage().getCookies());
	}
}
