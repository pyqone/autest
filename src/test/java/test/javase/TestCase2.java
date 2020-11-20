package test.javase;

import java.io.IOException;

import pres.auxiliary.tool.string.RandomString;
import pres.auxiliary.tool.string.StringMode;
import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;

public class TestCase2 {
	public static void main(String[] args) throws IOException {
		String s = "测试" + new RandomString(StringMode.ALL).toString();
		ZentaoTemplet.setSavePath("E:\\");
		ZentaoTemplet.setFileName(s);
		ZentaoTemplet.create();
		
		/*
		AddInformation a = new AddInformation();
		
		a.setModule("测试模块");
		a.setPrecondition("已在新增信息界面", "所有信息均正确填写");
		a.setSuccessExpectation("新增成功");
		a.setFailExpectation("新增失败");
		a.setButtonName("提交");
		a.setInformationName("活动");
		
		a.addWholeInformationCase().setTab("哈哈").setRowColorTab(Tab.BLUE);
		
		System.out.println("Step:");
		System.out.println(a.getStep());
		System.out.println("Expectation:");
		System.out.println(a.getExpectation());
		System.out.println();
		
		a.addUnWholeInformationCase();
		
		System.out.println("Step:");
		System.out.println(a.getStep());
		System.out.println("Expectation:");
		System.out.println(a.getExpectation());
		System.out.println();
		*/
		
		/*
		AddInformation a = new AddInformation();
		BrowseList b = new BrowseList();
		MyCase c = new MyCase();
		
		a.setModule("测试模块");
		a.setPrecondition("已在新增信息界面", "所有信息均正确填写");
		a.setSuccessExpectation("新增成功");
		a.setFailExpectation("新增失败");
		a.setButtonName("提交");
		a.setInformationName("活动");
		
		a.addWholeInformationCase().setTab("哈哈").setRowColorTab(Tab.BLUE);
		a.addUnWholeInformationCase();
		a.addTextboxCase("名称", true, false, null, new int[]{a.NUM_NAN, 10}, null);
		
		//实验自定义
		c.addTitle("新增不同电话号码的人物").
			addStep("我去", "我不去").
			addExpectation("输入失败").
			addKeyword("新增", "电话号码").
			addRank(1).
			addPrecondition("已在新增页面", "所有信息均正确填写").
			end().
			setRowColorTab(Tab.GREEN);
		
		a.addTextboxCase("活动代号", true, false, new char[]{InputType.NUM, InputType.EN}, new int[]{5, a.NUM_NAN}, null);
		a.addSelectboxCase("活动类型", true);
		a.addTextboxCase("参与人数", false, true, new char[]{InputType.NUM}, null, new int[]{10, 50});
		a.addTextboxCase("实到人数", false, true, new char[]{a.INPUT.NUM}, null, new int[]{a.NUM_NAN, 50});
		a.addTextboxCase("活动地点", false, true, null, new int[]{10, 30}, null);
		a.addStartDateCase("活动开始时间", true, true, "true");
		a.addEndDateCase("活动结束时间", true, true, "false");
		a.addDateCase("出发日期", false, true);
		a.addCheckboxCase("出行工具", false);
		a.addRadioButtonCase("主持人性别", false);
		a.addPhoneCase("主持人电话", false, true, PhoneType.MOBLE);
		a.addIDCardCase("主持人身份证号", false, true);
		a.addUploadImageCase("活动剪影", true, false, false, true, false, new char[]{a.FILE.JPG, a.FILE.BMP, a.FILE.PNG}, new int[]{3, a.NUM_NAN});
		a.addUploadFileCase("活动事项及通知", false, false, false, new char[]{a.FILE.DOC, a.FILE.DOCX, a.FILE.XLS, a.FILE.XLSX, a.FILE.TXT}, null);
		
		b.addAppBrowseListCase("活动列表");
		b.addWebBrowseListCase("活动列表");
//		b.addSearchCase("名称", "活动地点", "主持人姓名", "活动代号");
 */
		
		System.out.println("The end");
	}
}
