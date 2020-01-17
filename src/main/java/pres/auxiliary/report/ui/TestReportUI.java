package pres.auxiliary.report.ui;

import javax.swing.UIManager;

/**
 * <p><b>文件名：</b>TestReportUI.java</p>
 * <p><b>用途：</b>用于设置界面的样式，并初始化测试报告UI界面</p>
 * <p><b>编码时间：</b>2018年12月19日 上午9:27:20</p>
 * @author 彭宇琦
 */
public class TestReportUI {
	/**
	 * 打开UI界面
	 */
	public static void open() {
		//设置BeautyEye L&F样式
		try{
	        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	        UIManager.put("RootPane.setupButtonVisible", false);
	    }
	    catch(Exception e){
	    }
		
		TestReportMainFrame.Main();
	}
}
