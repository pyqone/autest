package test.javase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;

public class TestArrayList {

	public static void main(String[] args) {
		ArrayList<String> al = new ArrayList<String>();
		int count = 10000000;
		
		System.out.println("开始添加" + count + "个随机内容");
		Date start = new Date();
		for (int i = 0; i < count; i++) {
			al.add(new RandomString(StringMode.CH).toString(10, 30));
		}
		System.out.println("添加完毕，耗时：" + (new Date().getTime() - start.getTime()));
		System.out.println("随机抽取10个内容");
		start = new Date();
		for (int i = 0; i < 10; i++) {
			System.out.println("第" + (i + 1) + "个内容：" + al.get(new Random().nextInt(10000)));
		}
		System.out.println("抽取完毕，耗时：" + (new Date().getTime() - start.getTime()));
	}

}
