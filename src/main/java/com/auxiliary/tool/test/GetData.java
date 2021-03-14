package com.auxiliary.tool.test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.auxiliary.selenium.brower.ChromeBrower;
import com.auxiliary.selenium.brower.ChromeBrower.ChromeOptionType;
import com.auxiliary.selenium.element.FindCommonElement;
import com.auxiliary.selenium.element.FindDataListElement;
import com.auxiliary.selenium.event.ClickEvent;
import com.auxiliary.selenium.event.TextEvent;
import com.auxiliary.selenium.location.ByType;
import com.auxiliary.selenium.location.NoFileLocation;

public class GetData {
	final File driverFile = new File("");

	ChromeBrower chrome;

	NoFileLocation mailNl = new NoFileLocation();

	FindCommonElement co;
	FindDataListElement da;

	ClickEvent cl;
	TextEvent te;

	@BeforeSuite
	public void initData() {
		chrome = new ChromeBrower(driverFile);
		chrome.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");

		co = new FindCommonElement(chrome);
		da = new FindDataListElement(chrome);

		cl = new ClickEvent(chrome);
		te = new TextEvent(chrome);

		mailNl.putElementLocation("版本", ByType.XPATH, "//table//*[text()='版本名称']/../td");
		mailNl.putElementLocation("分支", ByType.XPATH, "//table//*[text()='测试分支']/../td");
		mailNl.putElementLocation("分支链接", ByType.XPATH, "//table//*[text()='测试分支']/../td/p/a");
		mailNl.putElementLocation("需求", ByType.XPATH, "//table//*[text()='版本需求内容']/../td");
		mailNl.putElementLocation("SQL", ByType.XPATH, "//table//*[contains(text(),'数据库变更记录')]/../td/p/span/a");
	}

	@AfterSuite
	public void closeBrower() {
		chrome.closeBrower();
	}

	@Test
	public void getMailInformation() {
		co.setReadMode(mailNl, true);
		String versionName = te.getText(co.getElement("版本"));
		String branchName = te.getText(co.getElement("分支"));
		String issueName = te.getText(co.getElement("需求"));

		List<String> branchLinkList = da.find("分支链接").getAllElement().stream().map(e -> te.getAttributeValue(e, "herf"))
				.collect(Collectors.toList());
		List<String> sqlList = da.find("SQL").getAllElement().stream().map(e -> te.getAttributeValue(e, "herf"))
				.collect(Collectors.toList());
	}
}
