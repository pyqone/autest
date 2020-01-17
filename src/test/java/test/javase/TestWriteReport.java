package test.javase;

import java.io.IOException;

import org.dom4j.DocumentException;

import pres.auxiliary.report.ui.TestReportMainFrame;


public class TestWriteReport {
	public static void main(String[] args) throws IOException, DocumentException, InterruptedException {
		// 创建测试报告类
		//TestReport r = new TestReport();
		//r.AutoWriteReport(true, true);
		// r.setSavePath("E:\\");
		// 测试报告属性作为参数，传入createReport()中
		//r.createReport(new File("E:\\1 - 副本.csv"), 2, "彭宇琦、李健梅、赵莉宽", "页面所有功能及APP任务、消息提醒功能");
		// System.out.println(r.getMailContent());
		TestReportMainFrame.Main();
		
		/*
		File f = new File("E:\\1.csv");
		TestReport r = new TestReport();
		String[] ss = r.readFile(f, 4);
		
		for ( String s : ss ) {
			System.out.println(s);
		}
		*/
		System.out.println("The end");
	}
}
