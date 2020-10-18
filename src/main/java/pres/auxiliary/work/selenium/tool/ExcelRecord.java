package pres.auxiliary.work.selenium.tool;

import java.io.File;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.tool.date.Time;
import pres.auxiliary.tool.file.MarkColorsType;
import pres.auxiliary.tool.file.excel.AbstractWriteExcel;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p><b>文件名：</b>ExcelRecord.java</p>
 * <p><b>用途：</b>
 * 用于记录自动化测试的运行过程，可记录用例步骤、结果、浏览器信息等，并在记录失败时提供自动截图。
 * </p>
 * <p><b>编码时间：</b>2020年8月12日下午2:16:55</p>
 * <p><b>修改时间：</b>2020年8月12日下午2:16:55</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class ExcelRecord extends AbstractWriteExcel<ExcelRecord> {
	/**
	 * 用于记录当前出现BUG的结果位置
	 */
	private ArrayList<Integer> resultBugList = new ArrayList<>();
	/**
	 * 用于指向当前记录的运行截图文件
	 */
	private File runScreenhotFile = null;
	/**
	 * 用于指向当前记录的异常截图文件
	 */
	private File errorScreenhotFile = null;
	/**
	 * 用于指向当前是否存在异常
	 */
	boolean isError = false;
	/**
	 * 用于记录开始执行的时间
	 */
	private long startTime = 0L;
	
	/**
	 * 根据xml配置文件的{@link Document}类对象进行构造
	 * @param configDocument xml配置文件的{@link Document}类对象
	 * @param tempFile excel文件对象
	 */
	public ExcelRecord(Document configDocument, File tempFile) {
		super(configDocument, tempFile);
	}

	/**
	 * 根据xml配置文件对象进行构造
	 * @param configFile xml配置文件对象
	 * @param tempFile excel文件对象
	 * @throws DocumentException 当读取xml配置文件对象失败时抛出的异常
	 */
	public ExcelRecord(File configFile, File tempFile) throws DocumentException {
		super(configFile, tempFile);
	}
	
	/**
	 * 用于添加浏览器信息。该方法不能单独调用，必须配合其他
	 * 其他方法一起使用，否则在生成文件时，其写入的内容不会被填写到文件中
	 * @param brower 继承自{@link AbstractBrower}类的浏览器类对象
	 * @return 类本身
	 */
	public ExcelRecord setBrowerInformation(AbstractBrower brower) {
		//获取浏览器信息，将其转换为JSONObject对象
		JSONObject json = JSON.parseObject(brower.getAllInformation());
		switchSheet("运行记录")
			.addContent("brower", json.get("浏览器名称").toString())
			.addContent("version", json.get("浏览器版本").toString())
			.addContent("system", json.get("操作系统版本").toString());
		
		return this;
	}
	
	/**
	 * 用于添加执行者。该方法不能单独调用，必须配合的其他其他方法一起使用，
	 * 否则在生成文件时，其写入的内容不会被填写到文件中
	 * @param actionName 执行者
	 * @return 类本身
	 */
	public ExcelRecord setActionName(String actionName) {
		switchSheet("运行记录").setFieldValue("active_person", actionName);
		
		return this;
	}
	
	/**
	 * 用于添加运行类名以及运行方法名。多次调用时会清空上一次记录的内容
	 * @param className 类名
	 * @param methodName 方法名
	 * @return 类本身
	 */
	public ExcelRecord runMethod(String className, String methodName) {
		setFieldValue("运行记录", "class_name", className);
		setFieldValue("运行记录", "method_name", methodName);
		setFieldValue("错误记录", "class_name", className);
		setFieldValue("错误记录", "method_name", methodName);
		return this;
	}
	
	/**
	 * 用于记录执行开始时间，记录结束时，会写入相应的结束时间
	 */
	public ExcelRecord reckonByTime() {
		//若设定当前状态为开始记录状态，则存储当前的时间戳
		startTime = System.currentTimeMillis();
		
		return this;
	}
	
	/**
	 * 用于添加实际运行步骤，支持传入多条，每条数据将自动编号以及换行
	 * 
	 * @param text 实际运行步骤
	 * @return 类本身
	 */
	public ExcelRecord runStep(String... text) {
		return switchSheet("运行记录").addContent("step", text);
	}
	
	/**
	 * 用于添加执行结果，根据传入的Bug判定来记录当前执行的Bug数量。该方法可调用多次，
	 * 将多个结果，结果将自动编号。
	 * 
	 * @param text 结果
	 * @param bug 标记是否为Bug
	 * @return 类本身
	 */
	public ExcelRecord runResult(String text, boolean bug) {
		//若当前结果为BUG，则记录相应的编号
		if (bug) {
			//编号可根据相应sheet中字段的内容数量获取到当前出现BUG的内容位置
			resultBugList.add(fieldMap.get("运行记录").get("result").content.size());
		}

		return switchSheet("运行记录").addContent("result", text);
	}
	
	/**
	 * 用于添加运行备注
	 * 
	 * @param text 备注文本
	 * @return 类本身
	 */
	public ExcelRecord runMark(String text) {
		return switchSheet("运行记录").addContent("mark", text);
	}
	
	/**
	 * 用于添加运行时截图
	 * 
	 * @param screenshotFile 截图文件对象
	 * @return 类本身
	 */
	public ExcelRecord runScreenshot(File screenshotFile) {
		//记录运行截图
		runScreenhotFile = screenshotFile;
		
		return switchSheet("运行记录")
				.addContent("screenshot_position", screenshotFile.getPath());
	}
	
	/**
	 * 用于添加运行记录编号
	 * @param text 编号文本
	 * @return 类本身
	 */
	public ExcelRecord runId(String text) {
		return switchSheet("运行记录")
				.addContent("id", text);
	}
	
	/**
	 * 用于添加异常步骤的信息，传入异常类后，将会自动记录异常步骤
	 * @param exception 异常类
	 * @return 类本身
	 */
	public ExcelRecord exception(Exception exception) {
		isError = true;
		return switchSheet("错误记录")
				.addContent("id", fieldMap.get("运行记录").get("id").content.size() == 0 ? "" : fieldMap.get("运行记录").get("id").getContent(0))
				.addContent("class_name", fieldMap.get("运行记录").get("class_name").content.size() == 0 ? "" : fieldMap.get("运行记录").get("class_name").getContent(0))
				.addContent("method_name", fieldMap.get("运行记录").get("method_name").content.size() == 0 ? "" : fieldMap.get("运行记录").get("method_name").getContent(0))
				.addContent("error_step", fieldMap.get("运行记录").get("step").content.size() == 0 ? "" : fieldMap.get("运行记录").get("step").getContent(-1))
				.addContent("error_class", exception.getClass().getName())
				.addContent("error_information", exception.getMessage());
	}
	
	/**
	 * 用于添加异常步骤信息，并记录截图，可参见{@link #exception(Exception)}
	 * @param exception 异常类
	 * @param screenshotFile 截图文件对象
	 * @return 类本身
	 */
	public ExcelRecord exception(Exception exception, File screenshotFile) {
		errorScreenhotFile = screenshotFile;
		return exception(exception)
				.switchSheet("错误记录")
				.addContent("screenshot_position", screenshotFile.getPath());
	}
	
	/**
	 * 用于添加测试用例的前置条件，支持传入多条，每条数据将自动编号以及换行
	 * 
	 * @param text 前置条件
	 * @return 类本身
	 */
	public ExcelRecord caseCondition(String... text) {
		return switchSheet("测试用例").addContent("condition", text);
	}
	
	/**
	 * 用于添加测试用例的用例编号，不支持传入多个用例编号，每次调用方法后将覆盖之前传入的内容
	 * @param text 用例编号
	 * @return 类本身
	 */
	public ExcelRecord caseId(String text) {
		return switchSheet("测试用例")
				.clearContent("case_id")
				.addContent("case_id", text)
				.switchSheet("运行记录")
				.clearContent("case_id")
				.addContent("case_id", text);
	}
	
	/**
	 * 用于添加测试用例的标题，不支持传入多个标题，每次调用方法后将覆盖之前传入的内容
	 * @param text 标题
	 * @return 类本身
	 */
	public ExcelRecord caseTitle(String text) {
		return switchSheet("测试用例")
				.clearContent("title")
				.addContent("title", text);
	}
	
	/**
	 * 用于添加测试用例的步骤，支持传入多条，每条数据将自动编号以及换行
	 * @param text 标题
	 * @return 类本身
	 */
	public ExcelRecord caseStep(String... text) {
		return switchSheet("测试用例")
				.addContent("step", text);
		
	}
	
	/**
	 * 用于添加测试用例的预期，支持传入多条，每条数据将自动编号以及换行
	 * @param text 标题
	 * @return 类本身
	 */
	public ExcelRecord caseExpect(String... text) {
		return switchSheet("测试用例")
				.addContent("expect", text);
	}
	
	@Override
	public AbstractWriteExcel<ExcelRecord>.FieldMark end() {
		//记录运行状态，Bug数量，当前时间以及运行时间
		switchSheet("运行记录")
			.addContent("state", isError ? "2" : "1")
			.addContent("bug_number", String.valueOf(resultBugList.size()))
			.addContent("active_time", new Time().getFormatTime())
			.addContent("use_time", startTime == 0L ? "" : (String.valueOf((System.currentTimeMillis() - startTime) / 1000.0) + "s"));
		
		//获取父类的end方法，结束当前的记录
		FieldMark fieldMark = super.end();
		
		//判断是否存在BUG，并标记相应的结果文本为红色
		resultBugList.forEach(bugIndex -> {
			fieldMark.changeTextColor("result", bugIndex, MarkColorsType.RED);
		});
		
		//若存在异常，则将运行状态文本标记为红色，并添加相应的超链接
		if (isError) {
			//修改文本为红色
			fieldMark.changeTextColor("运行记录", "state", 0, MarkColorsType.RED);
			
			//添加运行记录与错误记录的内链关系
			fieldMark.fieldLink("运行记录", "state", getDocumentLinkPath("错误记录|id|-1"));
			fieldMark.fieldLink("错误记录", "method_name", getDocumentLinkPath("运行记录|method_name|-1"));
			//若存在异常截图文件，则添加本地文件链接
			if (errorScreenhotFile != null) {
				fieldMark.fieldLink("错误记录", "screenshot_position", getScreenshotPath(errorScreenhotFile));
			}
		}
		
		//若需要进行超链接，则添加相应的超链接
		if (runScreenhotFile != null) {
			fieldMark.fieldLink("运行记录", "screenshot_position", getScreenshotPath(runScreenhotFile));
		}
		
		//获取测试用例中相应的元素
		Element caseElement = fieldMark.getCaseElement("测试用例");
		Element runElement = fieldMark.getCaseElement("运行记录");
		//若能获取到元素，则添加运行记录与测试用例之间的关联
		if (caseElement != null && runElement != null) {
			fieldMark.fieldLink("测试用例", "case_id", getDocumentLinkPath("运行记录|case_id|-1"));
			fieldMark.fieldLink("运行记录", "case_id", getDocumentLinkPath("测试用例|case_id|-1"));
		}
		
		
		//初始化数据
		initDta();
		return fieldMark;
	}
	
	/**
	 * 用于初始化所有需要用作记录的数据
	 */
	private void initDta() {
		resultBugList.clear();
		runScreenhotFile = null;
		errorScreenhotFile = null;
		startTime = 0L;
		isError = false;
	}
	
	/**
	 * 将xml文件中编写的文档内部超链接内容进行转换
	 * @param linkContent 链接内容
	 * @return 转换后的在poi使用的超链接代码
	 */
	private String getDocumentLinkPath(String linkContent) {
		String[] linkContents = linkContent.split("\\|");
		int length = linkContents.length;
		
		//获取超链接的sheet名称
		String linkSheetName = linkContents[0];
		//根据sheet名称以及字段id，获取该字段在sheet中的列数字下标，并将数字下标转换为英文下标
		String linkColumnIndex = num2CharIndex(getColumnNumIndex(linkSheetName, linkContents[1]));
		
		String linkRowIndex = "";
		//获取当前表格的最后一行元素个数
		
		int lastIndex = ((Element) (contentXml.selectSingleNode("//sheet[@name='" + linkSheetName + "']"))).elements().size();
		//判断当前linkContents是否存在链接行数（即第三个元素）且链接的文本为数字
		//若符合规则，则将linkRowIndex设置为当前编写的内容
		//若不符合规则，则将linkRowIndex设置为当前sheet的最后一行
		//由于存在标题行，调用getPoiIndex()方法得到的数值需要向下平移一位才能得到真实的数据
		if (length > 2) {
			linkRowIndex = String.valueOf(getPoiIndex(lastIndex + 1, Integer.valueOf(linkContents[2])) + 1);
		} else {
			linkRowIndex = String.valueOf(lastIndex + 1);
		}
		
		//返回文档链接的内容
		return "'" + linkSheetName + "'!" + linkColumnIndex + linkRowIndex;
	}
	
	/**
	 * 用于返回需要链接的截图文件路径，若截图文件在tempFile下，则返回相应的相对路径；
	 * 若不在其下，则返回截图文件的绝对路径
	 * @param screenshotFile 截图文件对象
	 * @return 截图文件路径
	 */
	private String getScreenshotPath(File screenshotFile) {
		//获取excel文件以及截图文件存储路径
		String excelFolderPath = tempFile.getParentFile().getAbsolutePath();
		String screenhotPath = screenshotFile.getAbsolutePath();
		
		//判断当前截图文件的路径是否在excel文件路径下，若存在其下，则返回相应的相对路径；
		//若不存在，则返回截图文件的绝对路径
		if (screenhotPath.indexOf(excelFolderPath) == -1) {
			return screenhotPath;
		} else {
			return "." + screenhotPath.substring(excelFolderPath.length()).replaceAll("\\\\", "/");
		}
	}
}
