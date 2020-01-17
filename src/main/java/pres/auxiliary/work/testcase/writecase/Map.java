package pres.auxiliary.work.testcase.writecase;

import java.io.File;
import java.io.IOException;

import pres.auxiliary.work.testcase.change.Tab;

public class Map extends Case {
	/**
	 * 在创建了模版后可调用该构造方法，该构造会检测模版类中存储的模板文件保存路径及文件名，若模板文件保存路径及文件名的其中一项为空，
	 * 则抛出UndefinedDirectoryException异常，此时请使用带参构造
	 */
	public Map() {
		super();
	}

	/**
	 * 用于指定模板文件对象
	 * 
	 * @param excel
	 *            模板文件对象
	 * @throws IOException
	 */
	public Map(File excel) throws IOException {
		super(excel);
	}

	/**
	 * 用于编写地图测距的测试用例
	 * 
	 * @return
	 * @throws IOException
	 */
	public Tab addRangeFindingCase() throws IOException {
		//存储方法名
		String methodName = "addRangeFindingCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 用于生成地图上单一定位点的测试用例
	 * 
	 * @param pointName
	 *            定位点的名称
	 * @return
	 * @throws IOException
	 */
	public Tab addMapPointCase(String pointName) throws IOException {
		//存储方法名
		String methodName = "addMapPointCase";
		//存储变量信息
		textMap.put("pointName", pointName);
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 用于添加对地图定位点搜索的测试用例
	 * 
	 * @param condition
	 *            搜索条件
	 * @param name
	 *            搜索的信息
	 * @return
	 * @throws IOException
	 */
	public Tab addMapSearchInformationCase(String condition, String name)
			throws IOException {
		//存储方法名
		String methodName = "addMapSearchInformationCase";
		//存储变量信息
		textMap.put("condition", condition);
		textMap.put("name", name);
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 用于添加与车辆轨迹回放相关的测试用例
	 * @return
	 * @throws IOException
	 */
	public Tab addCarLocusPlaybackCase() throws IOException {
		//存储方法名
		String methodName = "addCarLocusPlaybackCase";
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
	
	/**
	 * 该方法用于生成显示车辆轨迹的测试用例
	 * @param name 轨迹名称
	 * @param names 轨迹名称，该变量为可变参数，与name的作用一致，可不传入参数
	 * @return
	 * @throws IOException
	 */
	public Tab addShowLocusCase(String name, String... names) throws IOException {
		//存储方法名
		String methodName = "addShowLocusCase";
		
		String pointType = name + "、";
		for ( String s : names ) {
			pointType += (s + "、");
		}
		textMap.put("pointType", pointType.substring(0, pointType.lastIndexOf("、")));
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = getStep(methodName, -1);

		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}
}
