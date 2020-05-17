package test.selenium.js;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.work.selenium.brower.ChromeBrower;
import pres.auxiliary.work.selenium.brower.ChromeBrower.ChromeOptionType;

public class TestJavaScript {
	@Test
	public void getElementAttribute() {
		ChromeBrower cb = new ChromeBrower(new File("Resource/BrowersDriver/Chrom/80.0.3987.163/chromedriver.exe"));
		cb.addConfig(ChromeOptionType.CONTRAL_OPEN_BROWER, "127.0.0.1:9222");
		JavascriptExecutor js = (JavascriptExecutor) cb.getDriver();
		
		WebElement element = cb.getDriver().findElement(By.xpath("//*[@id='kw']"));
		JSONObject elemrntJson = new JSONObject();
		elemrntJson.put("tagname", element.getTagName());
		
		JSONArray attArray = new JSONArray();
		((ArrayList<Object>) js.executeScript("return arguments[0].attributes;", element)).stream().
			map(obj -> obj.toString()).map(text -> {
				String[] atts = text.split("\\,\\ ");
				
				JSONObject json = new JSONObject();
				Arrays.stream(atts).filter(att -> {
					String key = att.split("=")[0];
					return "name".equals(key) || "value".equals(key);
				}).forEach(att -> {
					String[] kv = att.split("=");
					json.put(kv[0], kv[1].indexOf("}") > -1 ? kv[1].substring(0, kv[1].length() - 1) : kv[1]);
				});
				
				return json;
			}).forEach(json -> {
				attArray.add(json);
			});
		
		elemrntJson.put("att", attArray);
		
		System.out.println(elemrntJson);
		
		cb.getDriver().quit();
	}
}
