package test.javase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestOpenBrowers {
	public static void main(String[] args) {
		//FirefoxBrower fb = new FirefoxBrower("C:\\Program Files (x86)\\firefox46\\firefox.exe", "http://www.hao123.com");
		//WebDriver d = fb.getDriver();
		//By by = By.xpath("/html/body/");
		//System.out.println(by.toString());
		ChromeBrower cb = new ChromeBrower("E:\\chromedriver.exe", "http://www.hao123.com");
		WebDriver d = cb.getDriver();
		System.out.println("The End");
	}
}
