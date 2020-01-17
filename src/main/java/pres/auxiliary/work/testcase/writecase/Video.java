package pres.auxiliary.work.testcase.writecase;

import java.io.File;
import java.io.IOException;

import pres.auxiliary.work.testcase.change.Tab;

/**
 * FileName: Video.java
 * 
 * 用于生成与视频播放相关的测试用例
 * 
 * @author 彭宇琦
 * @Deta 2018年6月13日 上午9:14:30
 * @version ver1.0
 */
public class Video extends Case {

	private String videoType = "视频";

	public Video() {
		super();
	}

	public Video(File excel) throws IOException {
		super(excel);
	}

	/**
	 * 用于返回当前设置的视频类型的名称
	 * 
	 * @return
	 */
	public String getVideoType() {
		return videoType;
	}

	/**
	 * 用于设置当前视频类型的名称
	 * 
	 * @param videoType
	 */
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	/**
	 * 该方法用于生成视频播放相关的测试用例
	 * 
	 * @param isSpace
	 *            是否允许按下空格
	 * @return
	 * @throws IOException
	 */
	public Tab addPlayVideoCase(boolean isSpace) throws IOException {
		// 存储方法名
		String methodName = "addPlayVideoCase";
		// 存储变量
		textMap.put("videoType", videoType);

		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		// 添加步骤与预期
		String[] contents = null;
		// 判断是否有过期时间
		if (isSpace) {
			contents = getStep(methodName, -1);
		} else {
			contents = getStep(methodName, 1, 2, 3, 4, 6);
		}

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank,
				precondition);
	}

	/**
	 * 用于生成对视频进行截图的测试用例
	 * 
	 * @return
	 * @throws IOException
	 */
	public Tab addVideoScreenshotCase() throws IOException {
		// 存储方法名
		String methodName = "addVideoScreenshotCase";
		// 存储变量
		textMap.put("videoType", videoType);

		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		// 添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank,
				precondition);
	}

	/**
	 * 用于生成对视频快进、快退的测试用例
	 * 
	 * @param isDirection
	 *            是否允许按下键盘的方向键
	 * @param isInputSec
	 *            是否允许修改秒数
	 * @return
	 * @throws IOException
	 */
	public Tab addVideoAdvanceCase(boolean isDirection, boolean isInputSec) throws IOException {
		// 存储方法名
		String methodName = "addVideoAdvanceCase";
		// 存储变量
		textMap.put("videoType", videoType);

		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		// 判断是否允许按下键盘来快进快退
		// 添加步骤与预期
		String[] contents = null;
		if (isDirection && isInputSec) {
			contents = getStep(methodName, -1);
		} else if (isDirection && !isInputSec) {
			contents = getStep(methodName, 1, 2, 3, 4, 5, 6);
		} else if (!isDirection && isInputSec) {
			contents = getStep(methodName, 1, 2, 3, 4, 5, 7, 8, 9, 10);
		} else {
			contents = getStep(methodName, 1, 2, 3, 4, 5);
		}

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank,
				precondition);

	}

	/**
	 * 用于生成视频快放、慢放的测试用例
	 * 
	 * @param isSelect
	 *            是否允许用户选择倍率
	 * @return
	 * @throws IOException
	 */
	public Tab addVideoSpeedCase(boolean isSelect) throws IOException {
		// 存储方法名
		String methodName = "addVideoSpeedCase";
		// 存储变量
		textMap.put("videoType", videoType);

		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		// 添加步骤与预期
		String[] contents = null;
		if (isSelect) {
			contents = getStep(methodName, -1);
		} else {
			contents = getStep(methodName, 1, 2, 3);
		}

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank,
				precondition);
	}

	/**
	 * 用于生成对视频进度条操作的测试用例
	 * 
	 * @return
	 * @throws IOException
	 */
	public Tab addVideoProgressBarCase() throws IOException {
		// 存储方法名
		String methodName = "addVideoProgressBarCase";
		// 存储变量
		textMap.put("videoType", videoType);

		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		// 添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank,
				precondition);
	}

	/**
	 * 用于生成视频全屏播放和退出全屏播放的操作测试用例
	 * @return
	 * @throws IOException
	 */
	public Tab addFullScreenPlayCase() throws IOException {
		// 存储方法名
		String methodName = "addFullScreenPlayCase";
		// 存储变量
		textMap.put("videoType", videoType);

		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		// 添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank,
				precondition);
	}
}
