package test.javase;

import java.io.IOException;
import java.lang.reflect.Parameter;

import pres.auxiliary.work.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.testcase.writecase.InputType;
import pres.auxiliary.work.testcase.writecase.PresetCase;

public class TestAddCase {
	public static void main(String[] args) throws IOException {
		/*
		ZentaoTemplet.setFileName("商品车车辆物流管控平台项目");
		ZentaoTemplet.create();
		
		PresetCase p = new PresetCase();
		p.setModule("/商品车车辆物流管控平台/登录");
		p.getUsername().rightLoginCase();
		p.getUsername().errorLoginCase();
		
		p.setModule("/商品车车辆物流管控平台/百度地图");
		p.getMap().mapPointCase("车辆");
		p.getMap().rangeFindingCase();
		p.getMap().carLocusPlaybackCase();
		p.getBrowseList().addWebBrowseListCase("实时信息");
		p.getBrowseList().addWebBrowseListCase("报警");
		
		p.setModule("/商品车车辆物流管控平台/系统设置/修改密码");
		p.getUsername().alterPasswordCase();
		
		p.setModule("/商品车车辆物流管控平台/系统设置/人工确认报警");
		p.getBrowseList().addWebBrowseListCase("报警信息");
		p.getBrowseList().addSelectSearchCase("车组", "报警信息");
		p.getBrowseList().addSelectSearchCase("车辆", "报警信息");
		p.getBrowseList().addSelectSearchCase("处理状态", "报警信息");
		p.getBrowseList().addDateSearchCase("报警时间", true, "报警信息");
		p.getBrowseList().addExportListCase("报警信息", true);
		
		p.setModule("/商品车车辆物流管控平台/外接设备管理/IC卡查询");
		p.getBrowseList().addWebBrowseListCase("IC卡");
		p.getBrowseList().addInputSearchCase("卡号", "IC卡");
		p.getBrowseList().addDateSearchCase("报警时间", true, "IC卡");
		
		p.setModule("/商品车车辆物流管控平台/外接设备管理/IC卡管理");
		p.getAddInformation().setButtonName("添加");
		p.getAddInformation().setInformationName("IC卡");
		p.getAddInformation().setFailExpectation("IC卡添加失败，并给出相应的提示");
		p.getAddInformation().setSuccessExpectation("IC卡添加成功，并显示在下方的IC卡列表中");
		p.getAddInformation().addWholeInformationCase();
		p.getAddInformation().addUnWholeInformationCase();
		p.getAddInformation().addTextboxCase("卡号", true, false, new char[]{InputType.NUM}, null, null);
		p.getAddInformation().addTextboxCase("密码", true, true, new char[]{InputType.NUM, InputType.EN}, null, null);
		p.getAddInformation().addTextboxCase("确认密码", true, true, new char[]{InputType.NUM, InputType.EN}, null, null);
		p.getAddInformation().addSelectboxCase("IC卡类型", true);
		p.getAddInformation().addTextboxCase("持卡人", true, true, null, null, null);
		p.getAddInformation().addSelectboxCase("模式", true);
		
		p.getOperateInformation().addEditCase("IC卡");
		
		p.getBrowseList().addWebBrowseListCase("IC卡");
		
		p.setModule("/商品车车辆物流管控平台/外接设备管理/里程统计信息");
		p.getBrowseList().addWebBrowseListCase("里程信息");
		p.getBrowseList().addInputSearchCase("卡号", "里程信息");
		p.getBrowseList().addDateSearchCase("统计时间", true, "里程信息");
		p.getBrowseList().addExportListCase("里程信息", true);
		
		p.setModule("/商品车车辆物流管控平台/报表功能/车辆状态查询");
		p.getBrowseList().addWebBrowseListCase("车辆状态");
		p.getBrowseList().addSelectSearchCase("车组", "车辆状态");
		p.getBrowseList().addSelectSearchCase("车辆", "车辆状态");
		p.getBrowseList().addDateSearchCase("不在线时间", false, "车辆状态");
		*/
		
		A a = new A();
		a.show();
		System.out.println("The End");
	}
	
	public TestAddCase() {
		System.out.println(this.getClass().getSimpleName());
	}
}	

class A extends B {
	public void show() {
		method(Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 2].getMethodName());
	}
} 

class B {
	public void method(String s) {
		try {
			Parameter[] pars = this.getClass().getMethod(Thread.currentThread().
					getStackTrace()[Thread.currentThread().getStackTrace().length - 3].getMethodName(), String.class).getParameters();
			for ( Parameter par : pars ) {
				System.out.println(par.getName());
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		System.out.println(s);
	}
}