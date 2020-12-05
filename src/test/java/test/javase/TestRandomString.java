package test.javase;

import pres.auxiliary.tool.string.RandomString;

public class TestRandomString {
	public static void main(String[] args) {
		RandomString rs =  new RandomString();
		rs.addMode("asdf");
		//System.out.println(rs.getStringSeed());
		//System.out.println(rs.toString(50));
		
		System.out.println(rs.getStringSeed());
		rs.shuffle();
		System.out.println(rs.getStringSeed());
		System.out.println("---------------------");
		
		rs.setRepeat(false);
		rs.setDispose(rs.DISPOSE_IGNORE);
		System.out.println(rs.toString(6));
		System.out.println(rs.getStringSeed());
		System.out.println("---------------------");
		
		/*
		try {
			rs.setDispose(rs.DISPOSE_THROW_EXCEPTION);
			System.out.println(rs.toString(6));
		} catch (LllegalStringLengthException e) {
			e.printStackTrace();
		}
		System.out.println("---------------------");
		*/
		
		System.out.println(rs.getStringSeed());
		rs.setDispose(rs.DISPOSE_REPEAT);
		System.out.println(rs.toString(8));
		System.out.println(rs.getStringSeed());
		System.out.println("---------------------");
		
		System.out.println(rs.toString(3));
		rs.setRepeat(true);
		System.out.println(rs.toString(3));
		System.out.println("---------------------");
		
		rs.setRepeat(false);
		System.out.println(rs.toString(4));
		rs.setRepeat(true);
		System.out.println(rs.toString(4));
		System.out.println("---------------------");
		
		rs.setRepeat(false);
		System.out.println(rs.toString(8));
		rs.setRepeat(true);
		System.out.println(rs.toString(8));
		System.out.println("---------------------");
		/*
		System.out.println("中文名：" + new RandomString(StringMode.CH));
		System.out.println("英文名：" + new RandomString(StringMode.CAP));
		*/
		
		/*
		System.out.println("测试构造方法：");
		
		RandomString rs1 = new RandomString();
		System.out.println("无参构造产生的字符串池为：" + rs1.getStringSeed());
		
		RandomString rs2 = new RandomString(StringMode.NUM);
		System.out.println("传入大写字母模型的构造产生的字符串池为：" + rs2.getStringSeed());
		
		RandomString rs3 = new RandomString("AThis is my RandomString class");
		System.out.println("传入自定义字符串的参构造产生的字符串池为：" + rs3.getStringSeed());
		
		System.out.println("----------------------------------------------");
		System.out.println("测试普通方法：");
		System.out.println("测试getStringSeed()方法：");
		System.out.println(rs2.getStringSeed());
		System.out.println();
		
		System.out.println("测试addMode(StringMode... modes)方法：");
		System.out.println("rs1调用方法前：" + rs1.getStringSeed());
		rs1.addMode(StringMode.LOW);
		System.out.println("rs1添加LOW模型后：" + rs1.getStringSeed());
		System.out.println();
		
		System.out.println("测试addMode(boolean isRepeat, StringMode... modes)方法：");
		System.out.println("rs1调用方法前：" + rs1.getStringSeed());
		rs1.addMode(false, StringMode.LOW);
		System.out.println("rs1再次添加LOW模型（不可重复）后：" + rs1.getStringSeed());
		rs1.addMode(true, StringMode.LOW);
		System.out.println("rs1再次添加LOW模型（可重复）后：" + rs1.getStringSeed());
		System.out.println();
		
		System.out.println("测试addMode(String mode)方法：");
		System.out.println("rs3调用方法前：" + rs3.getStringSeed());
		rs3.addMode(" haha");
		System.out.println("rs3添加LOW模型后：" + rs3.getStringSeed());
		System.out.println();
		
		System.out.println("测试addMode(boolean isRepeat, StringMode... modes)方法：");
		System.out.println("rs3调用方法前：" + rs3.getStringSeed());
		rs3.addMode(false, " xixi");
		System.out.println("rs3再次添加LOW模型（不可重复）后：" + rs3.getStringSeed());
		rs3.addMode(true, " hehe");
		System.out.println("rs3再次添加LOW模型（可重复）后：" + rs3.getStringSeed());
		System.out.println();
		
		System.out.println("测试clear()方法：");
		System.out.println("rs2调用方法前：" + rs2.getStringSeed());
		rs2.clear();
		System.out.println("rs2调用方法后：" + rs2.getStringSeed());
		System.out.println();
		
		System.out.println("测试length()方法：");
		System.out.println("rs1字符串池中的元素共有：" + rs1.length());
		System.out.println();
		
		System.out.println("测试remove(String str)方法：");
		System.out.println("rs3调用方法前：" + rs3.getStringSeed());
		rs3.remove("hehe");
		System.out.println("rs3移除“hehe”后：" + rs3.getStringSeed());
		System.out.println();
		
		System.out.println("测试remove(int StartPos, int EndPos)方法：");
		System.out.println("rs3调用方法前：" + rs3.getStringSeed());
		rs3.remove(5, 8);
		System.out.println("rs3移除5~7位的元素后：" + rs3.getStringSeed());
		System.out.println();
		
		System.out.println("测试remove(int Pos)方法：");
		System.out.println("rs3调用方法前：" + rs3.getStringSeed());
		rs3.remove(0);
		System.out.println("rs3移除第一位的元素后：" + rs3.getStringSeed());
		System.out.println();
		
		System.out.println("测试removeRepetition()方法：");
		System.out.println("rs3调用方法前：" + rs3.getStringSeed());
		rs3.removeRepetition();
		System.out.println("rs3移除第一位的元素后：" + rs3.getStringSeed());
		System.out.println();
		
		System.out.println("测试toString()方法：");
		System.out.println("rs3调用方法：" + rs3.toString());
		System.out.println();
		
		System.out.println("测toString(int stringLength)方法：");
		System.out.println("rs3调用方法：" + rs3.toString(8));
		System.out.println();
		
		System.out.println("测试toString(int stringLengthMin, int stringLengthMax)方法：");
		System.out.println("rs3调用方法：" + rs3.toString(1, 4));
		System.out.println();
		*/
	}
}
