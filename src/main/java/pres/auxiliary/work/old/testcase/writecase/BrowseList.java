package pres.auxiliary.work.old.testcase.writecase;

import java.io.File;
import java.io.IOException;

import pres.auxiliary.work.old.testcase.change.Tab;

/**
 * 该类用于生成预设的浏览列表相关的测试用例
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class BrowseList extends Case {
	/**
	 * 在创建了模版后可调用该构造方法，该构造会检测模版类中存储的模板文件保存路径及文件名，若模板文件保存路径及文件名的其中一项为空，
	 * 则抛出UndefinedDirectoryException异常，此时请使用带参构造
	 */
	public BrowseList() {
		super();
		
	}

	/**
	 * 用于指定模板文件对象
	 * 
	 * @param excel
	 *            模板文件对象
	 * @throws IOException
	 */
	public BrowseList(File excel) throws IOException {
		super(excel);
	}

	/**
	 * 该方法用于生成app上浏览列表的测试用例
	 * 
	 * @param name
	 *            列表的名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addAppBrowseListCase(String name) throws IOException {
		//存储方法名
		String methodName = "addAppBrowseListCase";
		//存储变量信息
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
	 * 该方法用于生成web上列表的测试用例
	 * 
	 * @param name
	 *            列表的名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addWebBrowseListCase(String name) throws IOException {
		//存储方法名
		String methodName = "addWebBrowseListCase";
		//存储变量信息
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
	 * 该方法用于添加输入条件对列表进行搜索的测试用例
	 * 
	 * @param condition
	 *            搜索的条件
	 * @param information
	 *            被搜索信息的名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addInputSearchCase(String condition, String information)
			throws IOException {
		//存储方法名
		String methodName = "addInputSearchCase";
		//存储变量信息
		textMap.put("condition", condition);
		textMap.put("information", information);
		
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
	 * 该方法用于添加选择条件对列表进行搜索的测试用例
	 * 
	 * @param condition
	 *            搜索的条件
	 * @param information
	 *            被搜索信息的名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addSelectSearchCase(String condition, String information)
			throws IOException {
		//存储方法名
		String methodName = "addSelectSearchCase";
		//存储变量信息
		textMap.put("condition", condition);
		textMap.put("information", information);
		
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
	 * 该方法用于添加通过时间或时间段对列表进行搜索的测试用例
	 * 
	 * @param condition
	 *            搜索的条件名称
	 * @param isTimeSlot
	 *            是否为时间段
	 * @param information
	 *            被搜索信息的名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addDateSearchCase(String condition, boolean isTimeSlot,
			String information) throws IOException {
		//存储方法名
		String methodName = "addDateSearchCase";
		//存储变量信息
		textMap.put("condition", condition);
		textMap.put("information", information);
		textMap.put("s", isTimeSlot ? "时间段" : "时间");
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = null;
		if ( !isTimeSlot ) {
			contents = getStep(methodName, 1, 2, 3);
		} else {
			contents = getStep(methodName, -1);
		}
		
		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 该方法用于生成对列表排序的测试用例
	 * 
	 * @param information
	 *            列表的名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addListSortCase(String condition, String information) throws IOException {
		//存储方法名
		String methodName = "addListSortCase";
		//存储变量信息
		textMap.put("information", information);
		textMap.put("condition", condition);
		
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
	 * 该方法用于添加导出信息的测试用例
	 * 
	 * @param information
	 *            列表的名称
	 * @param isCheck
	 *            是否可以勾选列表上的内容
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addExportListCase(String information, boolean isCheck)
			throws IOException {
		//存储方法名
		String methodName = "addExportListCase";
		//存储变量信息
		textMap.put("information", information);
		
		// 添加标题信息
		String title = getContent(methodName, "title");
		// 添加关键词
		String keyword = getContent(methodName, "keyword");
		// 添加优先级
		int rank = Integer.valueOf(getContent(methodName, "rank"));
		// 添加前置条件
		String precondition = getContent(methodName, "preconditions");
		//添加步骤与预期
		String[] contents = null;
		if (isCheck) {
			contents = getStep(methodName, 1, 5);
		} else {
			contents = getStep(methodName, 2, 3, 4, 5);
		}
		
		return after(title, new StringBuilder(contents[0]), new StringBuilder(contents[1]), keyword, rank, precondition);
	}

	/**
	 * 该方法用于添加导入信息的测试用例
	 * 
	 * @param information
	 *            需要导入的信息名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addImportListCase(String information) throws IOException {
		//存储方法名
		String methodName = "addImportListCase";
		//存储变量信息
		textMap.put("information", information);
		
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
	 * 该方法用于生成重置搜索功能的测试用例
	 * @return
	 * @throws IOException
	 */
	public Tab addResetSearchCase() throws IOException {
		//存储方法名
		String methodName = "addResetSearchCase";
		
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
	 * 该方法用于生成切换列表单页数据显示量的用例
	 * @return
	 * @throws IOException
	 */
	public Tab addSwitchListShowDataCase() throws IOException {
		//存储方法名
		String methodName = "addSwitchListShowDataCase";
		
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
