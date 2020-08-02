package pres.auxiliary.work.selenium.event;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;
import pres.auxiliary.work.selenium.element.old.CommonBy;
import pres.auxiliary.work.selenium.event.EventProxy.ActionType;

public class EventProxyTest {
	EventProxy<ClickEvent> clickProxy;
	EventProxy<TextEvent> inputProxy;
	ChromeBrower chrome;
	CommonBy by;
	
	@BeforeClass
	public void init() {
		chrome = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/83.0.4103.39/chromedriver.exe"));
		chrome.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		clickProxy = new EventProxy<>(new ClickEvent(chrome.getDriver()));
		inputProxy = new EventProxy<>(new TextEvent(chrome.getDriver()));
		by = new CommonBy(chrome);
	}
	
	@AfterClass
	public void showResult() {
		ClickEvent click = clickProxy.getProxyInstance();
		click.doubleClick(by.getElement("//*[text()='登录']"));
	}
	
	@Test
	public void addAcionTest() {
		TextEvent textEvent = new TextEvent(chrome.getDriver());
		textEvent.input(by.getElement("//*[@name='account']"), "admin");
		textEvent.input(by.getElement("//*[@name='password']"), "1111111");
		
		inputProxy.addAcion(ActionType.FUNCTION_BEFORE, ".*input.*", (info) -> {
			inputProxy.getProxyInstance().clear(info.getElement().get(0));
		});
		
		clickProxy.addAcion(ActionType.ELEMENT_BEFORE, ".*登录.*", (info) -> {
			TextEvent text = inputProxy.getProxyInstance();
			text.input(by.getElement("//*[@name='account']"), "admin");
			text.input(by.getElement("//*[@name='password']"), "1111111");
		});
		
		clickProxy.addAcion(ActionType.ELEMENT_AFTER, ".*登录.*", (info) -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			by.alertAccept();
		});
	}
}
