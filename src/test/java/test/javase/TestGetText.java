package test.javase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.browers.FirefoxBrower;

public class TestGetText {
	public static void main(String[] args) {
		FirefoxBrower fb = new FirefoxBrower("http://www.hao123.com");
		WebDriver d = fb.getDriver();
		
		System.out.println(d.findElement(By.xpath("//*[@id=\"menus\"]/li[4]/a")).getText());
	}
}
