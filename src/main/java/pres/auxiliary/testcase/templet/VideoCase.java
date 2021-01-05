package pres.auxiliary.testcase.templet;

import java.io.File;

/**
 * <p><b>文件名：</b>VideoCase.java</p>
 * <p><b>用途：</b>
 * 生成生成与视频播放相关的测试用例
 * </p>
 * <p><b>编码时间：</b>2020年11月13日上午1:13:07</p>
 * <p><b>修改时间：</b>2020年11月13日上午1:13:07</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class VideoCase extends Case {
	/**
	 * 用于标记
	 */
	public static final String VIDEO_TYPE = "视频类型";
	
	/**
	 * 通过测试用例模板库的xml配置文件来构造MapCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public VideoCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 该方法用于生成视频播放相关的测试用例
	 * @param isSpace 是否允许按下空格暂停/播放
	 * @return 类本身
	 */
	public Case playVideoCase(boolean isSpace) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addPlayVideoCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		relevanceAddData(caseName, "1", "1");
		relevanceAddData(caseName, "2", "2");
		relevanceAddData(caseName, "3", "3");
		relevanceAddData(caseName, "4", "4");
		relevanceAddData(caseName, "6", "6");
		if (isSpace) {
			relevanceAddData(caseName, "5", "5");
		}
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 该方法用于生成对视频进行截图相关的测试用例
	 * @return 类本身
	 */
	public Case videoScreenshotCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addVideoScreenshotCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 该方法用于生成视频快进、快退相关的测试用例
	 * @param isDirection 是否允许按下键盘的方向键
	 * @param isInputSec 是否允许修改秒数
	 * @return 类本身
	 */
	public Case videoAdvanceCase(boolean isDirection, boolean isInputSec) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addVideoAdvanceCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		relevanceAddData(caseName, "1", "1");
		relevanceAddData(caseName, "2", "2");
		relevanceAddData(caseName, "3", "3");
		relevanceAddData(caseName, "4", "4");
		relevanceAddData(caseName, "5", "5");
		if (isDirection) {
			relevanceAddData(caseName, "6", "6");
		}
		if (isInputSec) {
			relevanceAddData(caseName, "7", "7");
			relevanceAddData(caseName, "8", "8");
			relevanceAddData(caseName, "9", "9");
			relevanceAddData(caseName, "10", "10");
		}
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 该方法用于生成视频快放、慢放相关的测试用例
	 * @param isSelect 是否允许用户选择倍率
	 * @return 类本身
	 */
	public Case videoSpeedCase(boolean isSelect) {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addVideoSpeedCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		relevanceAddData(caseName, "1", "1");
		relevanceAddData(caseName, "2", "2");
		relevanceAddData(caseName, "3", "3");
		if (isSelect) {
			relevanceAddData(caseName, "4", "4");
		}
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 该方法用于生成对视频进度条操作相关的测试用例
	 * @return 类本身
	 */
	public Case videoProgressBarCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addVideoProgressBarCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
	
	/**
	 * 该方法用于生成视频全屏播放和退出全屏播放操作相关的测试用例
	 * @return 类本身
	 */
	public Case videoFullScreenPlayCase() {
		//清空字段的内容
		clearFieldText();
		// 存储case标签的name属性内容
		String caseName = "addFullScreenPlayCase";
		
		//存储标题信息
		addFieldText(LabelType.TITLE, getLabelText(caseName, LabelType.TITLE, "1"));
		//添加步骤与预期
		relevanceAddData(caseName, ALL, ALL);
		
		//存储前置条件信息
		addFieldText(LabelType.PRECONDITION, getAllLabelText(caseName, LabelType.PRECONDITION));
		//存储关键词信息
		addFieldText(LabelType.KEY, getLabelText(caseName, LabelType.KEY, "1"));
		//存储优先级信息
		addFieldText(LabelType.RANK, getLabelText(caseName, LabelType.RANK, "1"));
		
		return this;
	}
}
