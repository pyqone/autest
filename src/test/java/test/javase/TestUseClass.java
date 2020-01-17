package test.javase;

import java.io.File;

public class TestUseClass {
	public static void main(String[] args) {
		TestUseAClass ta = new TestUseAClass(
				new File("E:\\pyq\\TFS\\品管部工作\\05.日常管理\\个人工作\\彭宇琦\\2018年工作周报\\NNCQ_彭宇琦_第35周个人周报(C899).docx"));
		
		TestUseBClass tb = new TestUseBClass(ta);
		System.out.println(tb.test());
		
		ta.setF(new File("E:\\pyq\\TFS\\品管部工作\\05.日常管理\\个人工作\\彭宇琦\\2018年工作周报\\NNCQ_彭宇琦_第42周个人周报.docx"));
		System.out.println(tb.test());
	}
}

class TestUseAClass {
	public File f;

	public TestUseAClass(File f) {
		super();
		this.f = f;
	}

	public File getF() {
		return f;
	}

	public void setF(File f) {
		this.f = f;
	}
}

class TestUseBClass {
	public TestUseAClass ta;

	public TestUseBClass(TestUseAClass ta) {
		super();
		this.ta = ta;
	}

	public String test() {
		return ta.getF().getName();
	}
}