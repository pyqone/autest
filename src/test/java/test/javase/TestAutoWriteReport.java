package test.javase;

import pres.auxiliary.report.ui.TestReportUI;

public class TestAutoWriteReport {
	public static void main(String[] args) {
		//TestReport tr = new TestReport();
		//System.out.println(tr.AutoWriteReport(true, true));
		//TestReportMainFrame.Main();
		TestReportUI.open();
		//tr.compressReportFile(new File("C:\\AutoTest\\Report\\隆安OA扶贫项目Ver0.4版本测试报告"));
		//System.out.println("The End");
//		System.out.println(new File("E:\\Work\\1.项目测试\\隆安OA项目\\扶贫后台管理系统\\V0.2\\隆安OA扶贫项目Ver0.2版本测试报告").getParent());
//		System.out.println(new File("E:\\Work\\1.项目测试\\隆安OA项目\\扶贫后台管理系统\\V0.2\\隆安OA扶贫项目Ver0.2版本测试报告").getName());
		/*
		//网络不好时使用
		tr.createReport(bugListFile, testDay, person, range);
		tr.compressReportFile(new File("C:\\AutoTest\\Report\\隆安OA扶贫项目Ver0.4版本测试报告"));
		tr.sandMail(tr.getMailContent(), "隆安OA扶贫项目", "隆安OA扶贫项目Ver0.4版本测试报告", new File("C:\\AutoTest\\Report\\隆安OA扶贫项目Ver0.4版本测试报告.zip"));
		*/
		
		/*
		String[][] ts = tr.getMailToAndCc("那坡县");
		
		for (String[] s : ts) {
			for (String ss : s) {
				System.out.println(ss);
			}
		}
		*/
		
		//System.out.println(s());
		//System.out.println(new Random().nextDouble());
		/*
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
		*/
	}
}
