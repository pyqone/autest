package test.javase;

import java.util.HashMap;

import org.testng.annotations.Test;

public class TestMap {
	HashMap<String, String> map = new HashMap<String, String>(16);
	
	/**
	 * 用于测试map移除不存在的元素
	 */
	@Test
	public void removeTest() {
		System.out.println(map.remove("hehe"));
	}
}	
