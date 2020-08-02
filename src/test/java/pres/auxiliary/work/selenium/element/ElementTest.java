package pres.auxiliary.work.selenium.element;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;
import pres.auxiliary.work.selenium.element.Element;
import pres.auxiliary.work.selenium.element.ElementType;

public class ElementTest {
	/**
	 * 主窗体元素
	 */
	Element mainFrameElement;
	/**
	 * 工资发放详情窗体元素
	 */
	Element payrollFrameElement;
	/**
	 * 单位名称元素
	 */
	Element comNameElement;
	/**
	 * 专户名称元素
	 */
	Element payrollElement;
	/**
	 * 标题元素
	 */
	Element titleElement;
	
	ChromeBrower cb;
	
	@BeforeClass
	public void init() {
		cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/78.0394.70/chromedriver.exe"));
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		
		ArrayList<By> mainFrameByList = new ArrayList<By>();
		mainFrameByList.add(By.xpath("//iframe[contains(@src,'/Regulatory/admin/index.jsp')]"));
		mainFrameElement = new Element(cb, "主窗体元素", ElementType.COMMON_ELEMENT, 0);
		mainFrameElement.setByList(mainFrameByList);
		
		
		ArrayList<By> payrollFrameByList = new ArrayList<By>();
		payrollFrameByList.add(By.xpath("//iframe[contains(@src,'工资单管理')]"));
		payrollFrameElement = new Element(cb, "工资发放详情窗体元素", ElementType.COMMON_ELEMENT, 0);
		payrollFrameElement.setByList(payrollFrameByList);
		payrollFrameElement.setIframeElement(mainFrameElement);
		
		ArrayList<By> comNameByList = new ArrayList<By>();
		comNameByList.add(By.xpath("//*[@id='listBox']/li[1]/div[1]/p/span[1]"));
		comNameElement = new Element(cb, "单位名称元素", ElementType.COMMON_ELEMENT, 0);
		comNameElement.setByList(comNameByList);
		comNameElement.setIframeElement(payrollFrameElement);
		
		ArrayList<By> payrollByList = new ArrayList<By>();
		payrollByList.add(By.xpath("//*[@class='pay-code']"));
		payrollElement = new Element(cb, "专户名称元素", ElementType.COMMON_ELEMENT, 0);
		payrollElement.setByList(payrollByList);
		payrollElement.setIframeElement(payrollFrameElement);
		
		ArrayList<By> titleByList = new ArrayList<By>();
		titleByList.add(By.xpath("//*[@lay-id='工资单管理']"));
		titleElement = new Element(cb, "标题元素", ElementType.COMMON_ELEMENT, 0);
		titleElement.setByList(titleByList);
		titleElement.setIframeElement(mainFrameElement);
	}
	
	@AfterClass
	public void closeDriver() {
		cb.getDriver().quit();
	}
	
	@Test
	public void getWebElementTest() {
		System.out.println(comNameElement.getWebElement().getText());
		System.out.println("----------------------------------------");
		System.out.println(payrollElement.getWebElement().getText());
		System.out.println("----------------------------------------");
		System.out.println(titleElement.getWebElement().getText());
		System.out.println("----------------------------------------");
		System.out.println(payrollElement.getWebElement().getText());
	}
}
