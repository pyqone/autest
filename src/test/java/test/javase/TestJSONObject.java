package test.javase;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

public class TestJSONObject {
	public static void main(String[] args) {
		JSONObject methodJson1 = new JSONObject();
		methodJson1.put("name", "测试方法1");
		methodJson1.put("bug", 5);
		methodJson1.put("step", 7);
		methodJson1.put("isBugMethod", false);
		
		JSONObject methodJson2 = new JSONObject();
		methodJson2.put("name", "测试方法2");
		methodJson2.put("bug", 1);
		methodJson2.put("step", 3);
		methodJson2.put("isBugMethod", true);
		
		JSONObject methodJson3 = new JSONObject();
		methodJson3.put("name", "测试方法3");
		methodJson3.put("bug", 4);
		methodJson3.put("step", 10);
		methodJson3.put("isBugMethod", false);
		
		JSONObject testClassJson1 = new JSONObject();
		testClassJson1.put("name", "测试类1");
		testClassJson1.put("method", Arrays.asList(methodJson1.toString(), methodJson2.toString()));
		
		JSONObject testClassJson2 = new JSONObject();
		testClassJson2.put("name", "测试类2");
		testClassJson2.put("method", Arrays.asList(methodJson3.toString()));
		
		JSONObject moduleJson = new JSONObject();
		moduleJson.put("name", "测试模块");
		moduleJson.put("testClass", Arrays.asList(testClassJson1.toString(), testClassJson2.toString()));
		
		//System.out.println(moduleJson);
		
		System.out.println("模块信息输出：");
		JSONObject outputModuleJson = JSONObject.parseObject(moduleJson.toString());
		System.out.println(outputModuleJson.get("name"));
		outputModuleJson.getJSONArray("testClass").forEach(System.out :: println);
		System.out.println("-".repeat(20));
		
		System.out.println("测试类输出：");
		outputModuleJson.getJSONArray("testClass").forEach(testClassJson -> {
			JSONObject outputTestClassJson = JSONObject.parseObject(testClassJson.toString());
			System.out.println(outputTestClassJson.get("name"));
			outputTestClassJson.getJSONArray("method").forEach(System.out :: println);
		});
		System.out.println("-".repeat(20));
		
		System.out.println("测试方法输出：");
		outputModuleJson.getJSONArray("testClass").forEach(testClassJson -> {
			JSONObject outputTestClassJson = JSONObject.parseObject(testClassJson.toString());
			outputTestClassJson.getJSONArray("method").forEach(methodJson -> {
				JSONObject outputMethodJson = JSONObject.parseObject(methodJson.toString());
				System.out.println(outputMethodJson.get("name"));
				System.out.println(outputMethodJson.get("step"));
				System.out.println(outputMethodJson.get("bug"));
				System.out.println(outputMethodJson.get("isBugMethod"));
			});
		});
		System.out.println("-".repeat(20));
	}
}
