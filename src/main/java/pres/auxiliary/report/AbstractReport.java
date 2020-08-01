package pres.auxiliary.report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.opencsv.CSVReader;

/**
 * 该类用于定义生成测试报告类共用的方法
 * 
 * @author 彭宇琦
 */
// TODO 添加文件不存在时的限制
public abstract class AbstractReport implements ReportInter {
	/**
	 * 用于存储生成的测试报告存储的位置
	 */
	private StringBuilder savePath = new StringBuilder("C:\\AutoTest\\Report\\");
	/**
	 * 用于存储测试报告的文件名
	 */
	private StringBuilder fileName = new StringBuilder("");
	/**
	 * 用于判断是否允许打开文件夹，默认打开
	 */
	private boolean isOpenFolder = true;

	/**
	 * 用于存储各版本Bug的信息
	 */
	private File verBugNumber = new File(
			"ConfigurationFiles/ReportConfigurationFile/PreviousVerisionBugNumber.xml");

	/**
	 * 用于存储当前报告的项目名称
	 */
	private String projectedName = "";

	/**
	 * 用于存储从禅道上导出的BUG列表文件
	 */
	protected File bugListFile;

	/**
	 * 用于存储Bug汇总表中的所有信息
	 */
	private List<String[]> bugList = null;
	
	/**
	 * 用于存储严重BUG的信息
	 */
	private List<BugInformation> bugInformations = new ArrayList<>();
	
	private static Pattern PATTERN = Pattern.compile("\\$\\{(.+?)\\}",Pattern.CASE_INSENSITIVE);
	
	/**
	 * 用于返回严重BUG的信息
	 * @return 严重BUG的信息列表
	 */
	public List<BugInformation> getBugInformations() {
		return bugInformations;
	}

	/**
	 * 该方法返回测试报告的文件名称
	 * 
	 * @return 测试报告的文件名称
	 */
	public String getFileName() {
		return fileName.toString();
	}

	/**
	 * 该方法用于设置测试报告文件名称
	 * 
	 * @param fileName
	 *            测试报告的文件名称
	 */
	public void setFileName(String fileName) {
		// 判断传入的测试结果文件名称是否符合windows下的命名规则，若不符合，则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isFileName(fileName)) {
			throw new IncorrectDirectoryException("不合理的文件名称，文件名称：" + fileName);
		}

		// 通过判断后，则清空xmlName存储的信息并将新的文件名称放入xmlName种属性中
		this.fileName.delete(0, this.fileName.length());
		this.fileName.append(fileName);
	}

	/**
	 * 该方法用于返回测试报告文件存放的位置
	 * 
	 * @return 测试报告文件存放的位置
	 */
	public String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 该方法用于重新设置测试报告文件存放的路径，并将Document对象指向新的XML文件
	 * 
	 * @param savePath
	 *            测试报告的存放路径
	 */
	public void setSavePath(String savePath) {
		// 将传入的参数进行格式化
		StringBuilder sb = new StringBuilder(savePath);
		sb = MakeDirectory.formatPath(sb);

		// 判断格式化后的文件路径格式是否符合windonws下文件路径的命名规则,不符合则抛出异常
		if (!MakeDirectory.isPath(sb.toString())) {
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径："
					+ sb.toString());
		}

		// 将文件路径设置入属性中
		this.savePath = sb;
	}

	/**
	 * 用于返回是否允许直接打开文件夹
	 * 
	 * @return
	 */
	public boolean isOpenFolder() {
		return isOpenFolder;
	}

	/**
	 * 用于设置是否直接打开文件夹
	 * 
	 * @param isOpenFolder
	 */
	public void setOpenFolder(boolean isOpenFolder) {
		this.isOpenFolder = isOpenFolder;
	}

	/**
	 * 该方法用于打印测试报告，需要结合jacob包，以及配置好jacob-1.17-xxx.dll文件
	 * 
	 * @param report
	 *            测试报告文件对象
	 * @param printName
	 *            打印机的名称
	 */
	public void printReport(File report, String printName) {
		// 获取并存储待打印文件的绝对路径
		String path = report.getAbsolutePath();

		// 初始化COM线程
		ComThread.InitSTA();
		// 使用Jacob创建 ActiveX部件对象：
		ActiveXComponent word = new ActiveXComponent("Word.Application");

		// 打开Word文档
		Dispatch doc = null;
		// 这里Visible是控制文档打开后是可见还是不可见，若是静默打印，那么第三个参数就设为false
		Dispatch.put(word, "Visible", new Variant(false));
		// 第二个参数中跟着的字符串中是打印机的名称
		word.setProperty("ActivePrinter", new Variant(printName));
		Dispatch docs = word.getProperty("Documents").toDispatch();
		doc = Dispatch.call(docs, "Open", path).toDispatch();

		Dispatch.call(doc, "PrintOut");// 打印
		if (doc != null) {
			Dispatch.call(doc, "Close", new Variant(0));
		}
		// 释放资源
		ComThread.Release();
	}

	/**
	 * 该方法用于读取测试报告模板
	 * 
	 * @param xd
	 *            指向word文档的XWPFDocument对象
	 * @param params
	 *            存储替换本的容器
	 * @throws IOException
	 */
	protected void readeTemplet(XWPFDocument xd, Map<String, String> params)
			throws IOException {
		// 用于获取所有word上所有的表格
		Iterator<XWPFTable> iterator = xd.getTablesIterator();
		// 用于获取单独的表格
		XWPFTable table;
		// 用于存储获取到表格上所有的行
		List<XWPFTableRow> rows;
		// 用于存储表格上所有的列
		List<XWPFTableCell> cells;
		// 用于获取到列中的所有段落
		List<XWPFParagraph> paras;

		// 循环，获取文档中所有的表格
		while (iterator.hasNext()) {
			// 获取文档中的表格
			table = iterator.next();
			// 获取表格中的所有行
			rows = table.getRows();

			// 循环用于获取行中所有的行
			for (int i = 0; i < rows.size(); i++) {
				cells = rows.get(i).getTableCells();

				// 用于获取列中所有的列
				for (int j = 0; j < cells.size(); j++) {
					paras = cells.get(j).getParagraphs();

					// 判断获取的行数是否为第一行，若是第一行，则需要设计其样式
					if (i != 0) {
						// 判断文本是否
						if (i == 9) {
							// 分别查找段落中待替换的文本
							for (int k = 0; k < paras.size(); k++) {
								replaceContent(paras.get(k), params);
							}
						} else {
							// 分别查找段落中待替换的文本
							for (int k = 0; k < paras.size(); k++) {
								replaceTempet(paras.get(k), params);
							}
						}
					} else {
						// 分别查找段落中待替换的文本
						for (int k = 0; k < paras.size(); k++) {
							replaceTitleTempet(paras.get(k), params);
						}
					}
				}
			}
		}

		// 定义文件，用于创建存储生成的模版的文件夹
		File f = new File(savePath.toString() + fileName.toString());
		if (!f.exists()) {
			f.mkdirs();
		}

		FileOutputStream fop = new FileOutputStream(f
				+ ("\\" + fileName.toString() + ".docx"));
		xd.write(fop);
		fop.close();
	}

	/**
	 * 该方法用于替换模板中的其他文本
	 * 
	 * @param xp
	 * @param params
	 */
	private void replaceTempet(XWPFParagraph xp, Map<String, String> params) {
		// 用于匹配替换的字符
		Matcher matcher;
		// 用于存储获取到的文字和替换后的文字
		String s = xp.getText();

		// 通过正则进行匹配
		matcher = matcher(s);

		// 判断获取到的段落文字是否能匹配到正则
		if (matcher.find()) {
			// 循环，对s中的文字进行修改
			while ((matcher = matcher(s)).find()) {
				s = matcher.replaceFirst(String.valueOf(params.get(matcher
						.group(1))));
			}

			// 获取到段落中所有的XWPFSRun对象，并通过循环将其全部删除
			List<XWPFRun> xrs = xp.getRuns();
			for (int i = 0; i < xrs.size(); i++) {
				xp.removeRun(i);
				i--;
			}

			// 设置新的样式以及文字
			// xp.createRun().setText(s);
			// 判断s中的内容是否包含换行符，如果包含，则需要对段落进行分段处理
			if (s.indexOf("\n") > -1) {
				// 分割字符串
				String[] ss = s.split("\\n");
				for (int i = 0; i < ss.length; i++) {
					XWPFRun run = xp.createRun();// 对某个段落设置格式
					run.setText(ss[i].trim());

					// 判断当前的位置是否是最后一段，若是，则不加上换行
					if (i != ss.length - 1) {
						run.addBreak();// 换行
					}
				}
			} else {
				XWPFRun run = xp.createRun();// 对某个段落设置格式
				run.setText(s);
			}
		}
	}

	/**
	 * 方法用于替换模板中的内容文本
	 * 
	 * @param xp
	 * @param params
	 */
	private void replaceContent(XWPFParagraph xp, Map<String, String> params) {
		// 用于匹配替换的字符
		Matcher matcher;
		// 用于存储获取到的文字和替换后的文字
		String s = xp.getText();

		// 通过正则进行匹配
		matcher = matcher(s);

		// 判断获取到的段落文字是否能匹配到正则
		if (matcher.find()) {
			// 循环，对s中的文字进行修改
			while ((matcher = matcher(s)).find()) {
				s = matcher.replaceFirst(String.valueOf(params.get(matcher
						.group(1))));
			}

			// 获取到段落中所有的XWPFSRun对象，并通过循环将其全部删除
			List<XWPFRun> xrs = xp.getRuns();
			for (int i = 0; i < xrs.size(); i++) {
				xp.removeRun(i);
				i--;
			}

			// 设置新的样式以及文字
			// xp.createRun().setText(s);
			// 判断s中的内容是否包含换行符，如果包含，则需要对段落进行分段处理
			if (s.indexOf("\n") > -1) {
				// 分割字符串
				String[] ss = s.split("\\n");
				for (int i = 0; i < ss.length; i++) {
					XWPFRun run = xp.createRun();// 对某个段落设置格式
					run.addTab();// 段落前的Tab（等价于在段落前添加一个\t）
					run.setText(ss[i].trim());

					// 判断当前的位置是否是最后一段，若是，则不加上换行
					if (i != ss.length - 1) {
						// 换行
						run.addBreak();
					}
				}
			} else {
				XWPFRun run = xp.createRun();// 对某个段落设置格式
				run.addTab();// 段落前的Tab（等价于在段落前添加一个\t）
				run.setText(s);
			}
		}
	}

	/**
	 * 该方法用于替换模板中标题文本
	 * 
	 * @param xp
	 * @param params
	 */
	private void replaceTitleTempet(XWPFParagraph xp, Map<String, String> params) {
		// 用于匹配替换的字符
		Matcher matcher;
		// 用于存储获取到的文字和替换后的文字
		String s = xp.getText();

		// 通过正则进行匹配
		matcher = matcher(s);

		// 判断获取到的段落文字是否能匹配到正则
		if (matcher.find()) {
			// 循环，对s中的文字进行修改
			while ((matcher = matcher(s)).find()) {
				s = matcher.replaceFirst(String.valueOf(params.get(matcher
						.group(1))));
			}

			// 获取到段落中所有的XWPFSRun对象，并通过循环将其全部删除
			List<XWPFRun> xrs = xp.getRuns();
			for (int i = 0; i < xrs.size(); i++) {
				xp.removeRun(i);
				i--;
			}

			// 设置新的样式及文本
			XWPFRun xr = xp.createRun();
			// 设置字体大小(22磅为小二字体)
			xr.setFontSize(22);
			// 判断是否需要创建样式，若需要则创建，不需要则直接获取，之后重新设置
			CTRPr rpr = xr.getCTR().isSetRPr() ? xr.getCTR().getRPr() : xr
					.getCTR().addNewRPr();
			CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr
					.addNewRFonts();
			// 设置英文与数字的字体
			fonts.setAscii("Time New Roman");
			// 设置中文的字体
			fonts.setEastAsia("黑体");
			// 设置希腊文的字体
			fonts.setHAnsi("Time New Roman");
			// 设置文本
			xr.setText(s);
		}
	}

	/**
	 * 该方法用于匹配文本中的待替换的文本
	 * 
	 * @param str
	 * @return
	 */
	private Matcher matcher(String str) {
		Matcher matcher = PATTERN.matcher(str);
		return matcher;
	}

	@Override
	public String[] readFile(File bugListFile, int testDay) throws IOException {
		// 用于存储测试结束时间，即当前的时间
		Date d = new Date();
		// 存储当前时间（测试结束时间）
		String endTime = new SimpleDateFormat("yyyyMMdd").format(d);

		// 设置测试开始时间，调用Calendar类进行处理
		Calendar calendar = Calendar.getInstance();
		// 设置当前时间
		calendar.setTime(d);
		// 设置当前的天数减去测试时间，得到测试开始时间
		calendar.add(Calendar.DAY_OF_MONTH, (1 - testDay));
		// 将得到的时间转换为Date进行返回
		d = calendar.getTime();
		String startTime = new SimpleDateFormat("yyyyMMdd").format(d);

		return readFile(bugListFile, startTime, endTime);
	}

	/**
	 * 该方法用于读取Bug汇总表，其返回的数组存储的信息如下表所示：<br/>
	 * <table>
	 * <tr>
	 * <td>s[0]:项目名称</td>
	 * <td>s[1]:当前版本</td>
	 * <td>s[2]:1级BUG数量</td>
	 * <td>s[3]:2级BUG数量</td>
	 * </tr>
	 * <tr>
	 * <td>s[4]:3级BUG数量</td>
	 * <td>s[5]:4级BUG数量</td>
	 * <td>s[6]:激活1级BUG数量</td>
	 * <td>s[7]:激活2级BUG数量</td>
	 * </tr>
	 * <tr>
	 * <td>s[8]:激活3级BUG数量</td>
	 * <td>s[9]:激活4级BUG数量</td>
	 * <td>s[10]:未解决的1级BUG数量</td>
	 * <td>s[11]:未解决的2级BUG数量</td>
	 * </tr>
	 * <tr>
	 * <td>s[12]:延期解决1级BUG数量</td>
	 * <td>s[13]:延期解决2级BUG数量</td>
	 * </tr>
	 * </table>
	 * 
	 * @param bugListFile
	 *            BUG汇总表
	 * @param startTime
	 *            测试开始时间（字符串）
	 * @param endTime
	 *            测试结束时间（字符串）
	 * @return Bug汇总表中提取的信息
	 * @throws IOException
	 */
	public String[] readFile(File bugListFile, String startTime, String endTime)
			throws IOException {
		// this.bugListFile = bugListFile;

		// TODO 新增字段时需要修改数组
		/*
		 * 数组字段说明 s[0]:项目名称 s[1]:当前版本 s[2]:1级BUG数量 s[3]:2级BUG数量 s[4]:3级BUG数量
		 * s[5]:4级BUG数量 s[6]:激活1级BUG数量 s[7]:激活2级BUG数量 s[8]:激活3级BUG数量
		 * s[9]:激活4级BUG数量 s[10]:未解决1级BUG数量 s[11]:未解决2级BUG数量
		 */
		// 用于存储获取到的字符串组
		String[] temps = null;

		// 判断文件是否已经存在，不存在则读取Bug汇总表，并将其转换成List数组存入属性中
		if (this.bugListFile == null || !this.bugListFile.equals(bugListFile)) {
			fileContentToList(bugListFile);
		}

		temps = readVersionInformation(1);

		return readFile(bugListFile, temps[0].trim(), temps[1].trim(), startTime, endTime);
	}

	public String[] readFile(File bugListFile, String projected,
			String version, String startTime, String endTime)
			throws IOException {
		// TODO 新增字段时需要修改数组
		// 用于存储在Bug列表中读取到的信息
		String[] s = new String[14];

		// 判断文件是否已经存在，不存在则读取Bug汇总表，并将其转换成List数组存入属性中
		if (this.bugListFile == null || !this.bugListFile.equals(bugListFile)) {
			fileContentToList(bugListFile);
		}

		// 存储项目名称
		projectedName = projected.trim();
		
		// 存储项目及版本号
		s[0] = projected.trim();
		s[1] = version.trim();
		
		// 用于计算一级Bug的数量
		int one = 0;
		// 用于计算二级Bug的数量
		int two = 0;
		// 用于计算三级Bug的数量
		int three = 0;
		// 用于计算四级Bug的数量
		int four = 0;

		// 用于计算激活一级Bug的数量
		int activeOne = 0;
		// 用于计算激活二级Bug的数量
		int activeTwo = 0;
		// 用于计算激活三级Bug的数量
		int activeThree = 0;
		// 用于计算激活四级Bug的数量
		int activeFour = 0;

		// 用于计算往期版本中延期的一级Bug的数量
		int delayOne = 0;
		// 用于计算往期版本中延期的二级Bug的数量
		int delayTwo = 0;
		//用于计算未解决的一级Bug的数量
		int noSulveOne = 0;
		//用于计算未解决的二级Bug的数量
		int noSulveTwo = 0;
		
		// 读取BUG的总数
		int bugNumber = bugList.size();

		// 循环，读取文件中所有Bug（读取时下标从1开始，表示从第二行开始读）
		for (int i = 1; i < bugNumber; i++) {
			// 判断读取的BUG是否为新提交的BUG
			if (isNewBug(i, s[0], s[1], readDataInformation(i), startTime, endTime) == 1) {
				// 判断读取到的Bug等级，其相应等级的数量加1
				switch (readBugLever(i)) {
				case "1":
					one++;
					//添加严重BUG的信息至列表中
					bugInformations.add(new BugInformation(readBugID(i), readBugTitle(i), 1));
					break;

				case "2":
					two++;
					//添加严重BUG的信息至列表中
					bugInformations.add(new BugInformation(readBugID(i), readBugTitle(i), 2));
					break;

				case "3":
					three++;
					break;

				case "4":
					four++;
					break;
				}

				// 继续循环，获取下一行的时间进行判断
				continue;
			}

			// 若BUG不是新提交的BUG，则判断BUG是否为激活的BUG，依据则是判断版本号与激活次数是否大于1且版本为当前版本
			if (isActiveBug(i, s[0], s[1])) {
				// 判断读取到的Bug等级，其相应等级的数量加1
				switch (readBugLever(i)) {
				case "1":
					activeOne++;
					break;

				case "2":
					activeTwo++;
					break;

				case "3":
					activeThree++;
					break;

				case "4":
					activeFour++;
					break;
				}

				continue;
			}

			// 判断BUG是否为往期版本未解决的严重BUG
			if (isOldBug(i) == 1) {
				// 判断读取到的Bug等级，其相应等级的数量加1
				switch (readBugLever(i)) {
				case "1":
					noSulveOne++;
					break;

				case "2":
					noSulveTwo++;
					break;
				}

				continue;
			}
			
			// 判断BUG是否为往期版本延期解决的严重BUG
			if (isOldBug(i) == 2) {
				// 判断读取到的Bug等级，其相应等级的数量加1
				switch (readBugLever(i)) {
				case "1":
					delayOne++;
					break;

				case "2":
					delayTwo++;
					break;
				}

				continue;
			}
		}

		// 存储不同等级的Bug数量
		s[2] = String.valueOf(one);
		s[3] = String.valueOf(two);
		s[4] = String.valueOf(three);
		s[5] = String.valueOf(four);
		s[6] = String.valueOf(activeOne);
		s[7] = String.valueOf(activeTwo);
		s[8] = String.valueOf(activeThree);
		s[9] = String.valueOf(activeFour);
		s[10] = String.valueOf(noSulveOne);
		s[11] = String.valueOf(noSulveTwo);
		s[12] = String.valueOf(delayOne);
		s[13] = String.valueOf(delayTwo);

		// 添加当前版本BUG数量至XML文件中
		writeVersionBugNumber(s, (startTime + "-" + endTime));

		// 用于创建测试报告的存储文件夹，已便于存储测试报告与处理后的Bug汇总表
		File f = new File(savePath.toString() + s[0] + "Ver" + s[1] + "版本测试报告");
		// 判断文件夹是否被创建，未被创建则创建文件夹
		if (!f.exists()) {
			f.mkdirs();
		}

		// 将Bug汇总表写入到文件夹中
		FileOutputStream fop = new FileOutputStream(new File(f
				+ ("\\附：" + s[0] + "Ver" + s[1]) + "版本Bug汇总表.xlsx"));
		createAccessory().write(fop);
		fop.close();

		return s;
	}

	/**
	 * 该方法用于改进从禅道中导出的Bug汇总表
	 * 
	 * @throws IOException
	 */
	public XSSFWorkbook createAccessory() throws IOException {
		// 用于创建excel文件对象
		XSSFWorkbook newxw = new XSSFWorkbook();
		// 创建新的Bug汇总表模版
		XSSFSheet newxs = createTemplet(newxw);
		// 创建统计各个版本bug的sheet
		XSSFSheet verBugXs = createVerBugTemplet(newxw);
		// 读取存储bug数量的xml文件
		Document dom = null;
		try {
			dom = new SAXReader().read(verBugNumber);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// 读取Bug汇总表，并将其转换成List数组存入属性中
		if (bugList == null) {
			fileContentToList(bugListFile);
		}

		// 获取BUG的总条数
		int rowNum = bugList.size();

		// 读取原汇总表中的Bug标题中的取值，复制到新的汇总表中
		for (int i = 1; i < rowNum; i++) {
			// 用于创建新汇总表中的行
			XSSFRow newxr = newxs.createRow(i);
			XSSFCell newxc;
			String s = null;

			// 复制Bug编号的内容
			s = bugList.get(i)[BugFilePosition.ID];
			// 在新的模版中创建第i行第1列
			newxc = newxr.createCell(0);
			// 将s中的内容放入单元格中
			newxc.setCellValue(s);
			// 设置单元格的样式
			newxc.setCellStyle(style3(newxw));

			// 复制所属产品的内容
			s = bugList.get(i)[BugFilePosition.PRODUCT];
			newxc = newxr.createCell(1);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 通过标识符复制所属模块的内容
			s = bugList.get(i)[BugFilePosition.MODULE];
			newxc = newxr.createCell(2);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 通过标识符复制所属项目的内容
			s = bugList.get(i)[BugFilePosition.PROJECT];
			newxc = newxr.createCell(3);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制Bug标题的内容
			s = bugList.get(i)[BugFilePosition.TITLE];
			newxc = newxr.createCell(4);
			newxc.setCellValue(s);
			newxc.setCellStyle(style2(newxw));

			// 复制严重程度的内容
			s = bugList.get(i)[BugFilePosition.SIGNIFICANCE];
			newxc = newxr.createCell(5);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制Bug类型的内容
			s = bugList.get(i)[BugFilePosition.TYPE];
			newxc = newxr.createCell(6);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制重现步骤的内容
			s = bugList.get(i)[BugFilePosition.STEP];
			newxc = newxr.createCell(7);
			newxc.setCellValue(s);
			newxc.setCellStyle(style2(newxw));

			// 复制激活次数的内容
			s = bugList.get(i)[BugFilePosition.ACTIVE_COUNT];
			newxc = newxr.createCell(8);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制由谁创建的内容
			s = bugList.get(i)[BugFilePosition.CREATE_PERSON];
			newxc = newxr.createCell(9);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制创建时间的内容
			s = bugList.get(i)[BugFilePosition.CREATE_TIME];
			newxc = newxr.createCell(10);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制影响版本的内容
			s = bugList.get(i)[BugFilePosition.VERSION];
			newxc = newxr.createCell(11);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));

			// 复制指派给的内容
			s = bugList.get(i)[BugFilePosition.ASSIGN_PERSON];
			newxc = newxr.createCell(12);
			newxc.setCellValue(s);
			newxc.setCellStyle(style3(newxw));
		}
		
		// 添加各版本的BUG统计
		Element e = (Element) (dom.selectSingleNode("bug/projected[@name='"
				+ projectedName + "']"));
		// 读取所有的version标签
		@SuppressWarnings("unchecked")
		List<Element> verEs = e.elements();
		// 循环，将Bug记录信息写入文件中
		for (int i = 0; i < verEs.size(); i++) {
			// 用于创建新汇总表中的行
			XSSFRow verBugXr = verBugXs.createRow(i + 1);
			XSSFCell verBugXc;

			// 存储版本信息
			verBugXc = verBugXr.createCell(0);
			verBugXc.setCellValue(verEs.get(i).attributeValue("version"));
			verBugXc.setCellStyle(style3(newxw));

			// 存储该版本一级Bug的数量
			verBugXc = verBugXr.createCell(1);
			verBugXc.setCellValue(verEs.get(i).element("one")
					.attributeValue("number"));
			verBugXc.setCellStyle(style3(newxw));

			// 存储该版本二级Bug的数量
			verBugXc = verBugXr.createCell(2);
			verBugXc.setCellValue(verEs.get(i).element("two")
					.attributeValue("number"));
			verBugXc.setCellStyle(style3(newxw));

			// 存储该版本三级Bug的数量
			verBugXc = verBugXr.createCell(3);
			verBugXc.setCellValue(verEs.get(i).element("three")
					.attributeValue("number"));
			verBugXc.setCellStyle(style3(newxw));

			// 存储该版本四级Bug的数量
			verBugXc = verBugXr.createCell(4);
			verBugXc.setCellValue(verEs.get(i).element("four")
					.attributeValue("number"));
			verBugXc.setCellStyle(style3(newxw));
		}

		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		XSSFDrawing patriarch = verBugXs.createDrawingPatriarch();
		// 八个参数，前四个表示图片离起始单元格和结束单元格边缘的位置，
		// 后四个表示起始和结束单元格的位置，如下表示从第7列到第13列，从第1行到第16行,需要注意excel起始位置是0
		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 6,
				(short) 1, (short) 14, (short) 16);
		// anchor.setAnchorType(3);
		// 插入图片
		patriarch.createPicture(anchor, newxw.addPicture(plotBugCurve(verBugXs)
				.toByteArray(), Workbook.PICTURE_TYPE_PNG));

		// 返回新的模版对象
		return newxw;
	}

	/**
	 * 用于创建Bug数量曲线的方法，其需要传入一个工作表对象，其工作表中，数据的格式如下：<br/>
	 * <table border="0">
	 * <tr>
	 * <td>版本</td>
	 * <td>一级Bug数量</td>
	 * <td>二级Bug数量</td>
	 * <td>三级Bug数量</td>
	 * <td>四级Bug数量</td>
	 * </tr>
	 * <tr>
	 * <td>V0.1</td>
	 * <td>0</td>
	 * <td>2</td>
	 * <td>5</td>
	 * <td>3</td>
	 * </tr>
	 * <tr>
	 * <td>···</td>
	 * <td>···</td>
	 * <td>···</td>
	 * <td>···</td>
	 * <td>···</td>
	 * </tr>
	 * </table>
	 * 
	 * @param xs
	 *            工作表sheet对象（poi）
	 * @return
	 */
	public ByteArrayOutputStream plotBugCurve(XSSFSheet xs) {
		// 用于输出图片的字节流
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

		// 用于存储从sheet中读取到的数据，之后将用其作图
		// 存储版本信息
		List<String> versionList = new ArrayList<String>();
		// 存储Bug信息
		List<Integer> oneList = new ArrayList<Integer>();
		List<Integer> twoList = new ArrayList<Integer>();
		List<Integer> threeList = new ArrayList<Integer>();
		List<Integer> fourList = new ArrayList<Integer>();

		// 读取sheet中的数据
		for (int i = 1; i < xs.getLastRowNum() + 1; i++) {
			// 读取版本信息
			versionList.add("V" + xs.getRow(i).getCell(0).getStringCellValue());
			// 读取Bug数量（按照getNumericCellValue方法获取的数据为double，需要强转为int）
			oneList.add(Integer.valueOf(xs.getRow(i).getCell(1)
					.getStringCellValue()));
			twoList.add(Integer.valueOf(xs.getRow(i).getCell(2)
					.getStringCellValue()));
			threeList.add(Integer.valueOf(xs.getRow(i).getCell(3)
					.getStringCellValue()));
			fourList.add(Integer.valueOf(xs.getRow(i).getCell(4)
					.getStringCellValue()));
		}

		// 反序数组，以便于作图
		/*
		 * Collections.reverse(versionList); Collections.reverse(oneList);
		 * Collections.reverse(twoList); Collections.reverse(threeList);
		 * Collections.reverse(fourList);
		 */

		// 添加数据入图表
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < versionList.size(); i++) {
			String version = versionList.get(i);
			dataset.addValue(oneList.get(i), "一级Bug数量", version);
			dataset.addValue(twoList.get(i), "二级Bug数量", version);
			dataset.addValue(threeList.get(i), "三级Bug数量", version);
			dataset.addValue(fourList.get(i), "四级Bug数量", version);
		}
		JFreeChart chart = ChartFactory.createLineChart("Bug数量曲线", "版本", "数量",
				dataset, PlotOrientation.VERTICAL, true, true, true);

		// 设置图例字体
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 10));

		// 设置标题字体
		chart.setTitle(new TextTitle(chart.getTitle().getText(), new Font("黑体",
				Font.BOLD, 12)));

		// 图形的绘制结构对象
		CategoryPlot plot = chart.getCategoryPlot();

		// 获取显示线条的对象
		LineAndShapeRenderer lasp = (LineAndShapeRenderer) plot.getRenderer();

		// 设置拐点是否可见/是否显示拐点
		lasp.setBaseShapesVisible(true);

		// 设置拐点不同用不同的形状
		lasp.setDrawOutlines(true);

		// 设置线条是否被显示填充颜色
		lasp.setUseFillPaint(false);

		// 设置折点的大小
		lasp.setSeriesOutlineStroke(0, new BasicStroke(0.025F));
		lasp.setSeriesOutlineStroke(1, new BasicStroke(0.05F));

		// 设置网格线
		plot.setDomainGridlinePaint(Color.gray);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setRangeGridlinesVisible(true);

		// x轴
		CategoryAxis domainAxis = plot.getDomainAxis();
		// 设置x轴不显示，即让x轴和数据区重合
		domainAxis.setAxisLineVisible(false);
		// x轴标题
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 10));
		// x轴数据倾斜
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(0.95D));
		// X轴坐标上数值字体
		domainAxis.setTickLabelFont(new Font("黑体", Font.BOLD, 10));

		// 设置Y轴间隔
		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
		numAxis.setTickUnit(new NumberTickUnit(50));
		// y轴
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 10));

		// 设置y轴不显示，即和数据区重合
		rangeAxis.setAxisLineVisible(false);

		// y轴坐标上数值字体
		rangeAxis.setTickLabelFont(new Font("黑体", Font.BOLD, 10));
		rangeAxis.setFixedDimension(0);
		CategoryPlot cp = chart.getCategoryPlot();

		// 背景色设置
		cp.setBackgroundPaint(Color.WHITE);
		cp.setRangeGridlinePaint(Color.GRAY);

		try {
			ChartUtilities.writeChartAsPNG(byteArrayOut, chart, 400, 200);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteArrayOut;
	}

	/**
	 * 该方法用于创建一个Bug汇总表的附件模版
	 * 
	 * @return Excel表对象
	 */
	private XSSFSheet createTemplet(XSSFWorkbook xw) {
		// 创建工作簿
		XSSFSheet xs = xw.createSheet("Bug");
		// 创建表格的第一行，并设置高度
		XSSFRow xr = xs.createRow(0);
		xs.getRow(0).setHeight((short) (26.00 * 20));

		// 创建Bug编号项目及其样式
		// 创建第一列
		XSSFCell xc = xr.createCell(0);
		// 创建该列的值
		xc.setCellValue("Bug编号");
		// 创建该列的样式
		xc.setCellStyle(style1(xw));
		// 创建该列的宽度
		xs.setColumnWidth(0, (short) (5.7 * 256));

		// 创建所属产品及其样式
		xc = xr.createCell(1);
		xc.setCellValue("所属产品");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(1, (short) (9.5 * 256));

		// 创建所属模块及其样式
		xc = xr.createCell(2);
		xc.setCellValue("所属模块");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(2, (short) (9.5 * 256));

		// 创建所属项目及其样式
		xc = xr.createCell(3);
		xc.setCellValue("所属项目");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(3, (short) (9.5 * 256));

		// 创建Bug标题及其样式
		xc = xr.createCell(4);
		xc.setCellValue("Bug标题");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(4, (short) (33.13 * 256));

		// 创建严重程度及其样式
		xc = xr.createCell(5);
		xc.setCellValue("严重程度");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(5, (short) (5.7 * 256));

		// 创建Bug类型及其样式
		xc = xr.createCell(6);
		xc.setCellValue("Bug类型");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(6, (short) (9.5 * 256));

		// 创建重现步骤及其样式
		xc = xr.createCell(7);
		xc.setCellValue("重现步骤");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(7, (short) (39.00 * 256));

		// 创建激活次数及其样式
		xc = xr.createCell(8);
		xc.setCellValue("激活次数");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(8, (short) (5.7 * 256));

		// 创建由谁创建及其样式
		xc = xr.createCell(9);
		xc.setCellValue("由谁创建");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(9, (short) (7.50 * 256));

		// 创建创建日期及其样式
		xc = xr.createCell(10);
		xc.setCellValue("创建日期");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(10, (short) (11.00 * 256));

		// 创建影响版本及其样式
		xc = xr.createCell(11);
		xc.setCellValue("影响版本");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(11, (short) (12.38 * 256));

		// 创建指派给及其样式
		xc = xr.createCell(12);
		xc.setCellValue("指派给");
		xc.setCellStyle(style1(xw));
		xs.setColumnWidth(12, (short) (7.50 * 256));

		// 添加筛选按钮，筛选按钮的添加是按表格上的标识添加的，所以需要对列数进行转换
		// 例如，要添加前三列的搜索，则写为：xs.setAutoFilter(CellRangeAddress.valueOf("A1:C1"));
		xs.setAutoFilter(CellRangeAddress.valueOf((String.valueOf((char) (65))
				+ "1:" + String.valueOf((char) (65 + 13 - 1)) + "1")));

		// 冻结单元格
		xs.createFreezePane(0, 1, 1, 1);
				
		return xs;
	}

	/**
	 * 用于创建各个版本Bug统计
	 * 
	 * @param xw
	 * @return
	 */
	private XSSFSheet createVerBugTemplet(XSSFWorkbook xw) {
		// 创建工作簿
		XSSFSheet xs = xw.createSheet("各个版本Bug数量统计");
		// 创建表格的第一行
		XSSFRow xr = xs.createRow(0);

		// 创建Bug编号项目及其样式
		XSSFCell xc = xr.createCell(0);
		xc.setCellValue("版本");
		xc.setCellStyle(style3(xw));
		xs.setColumnWidth(0, (short) (12.00 * 256));

		xc = xr.createCell(1);
		xc.setCellValue("一级Bug数量");
		xc.setCellStyle(style3(xw));
		xs.setColumnWidth(1, (short) (12.00 * 256));

		xc = xr.createCell(2);
		xc.setCellValue("二级Bug数量");
		xc.setCellStyle(style3(xw));
		xs.setColumnWidth(2, (short) (12.00 * 256));

		xc = xr.createCell(3);
		xc.setCellValue("三级Bug数量");
		xc.setCellStyle(style3(xw));
		xs.setColumnWidth(3, (short) (12.00 * 256));

		xc = xr.createCell(4);
		xc.setCellValue("四级Bug数量");
		xc.setCellStyle(style3(xw));
		xs.setColumnWidth(4, (short) (12.00 * 256));

		// 添加筛选按钮，筛选按钮的添加是按表格上的标识添加的，所以需要对列数进行转换
		// 例如，要添加前三列的搜索，则写为：xs.setAutoFilter(CellRangeAddress.valueOf("A1:C1"));
		xs.setAutoFilter(CellRangeAddress.valueOf("A1:A1"));

		return xs;
	}

	/**
	 * 该方法用于设置水平居中、垂直居中、字体加粗的样式
	 */
	private XSSFCellStyle style1(XSSFWorkbook xw) {
		XSSFCellStyle xcs = xw.createCellStyle();
		// 设置内容的水平居中对齐
		xcs.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容的垂直居中对齐
		xcs.setVerticalAlignment(VerticalAlignment.CENTER);
		// 设置单元格自动换行
		xcs.setWrapText(true);
		// 设置字体的样式
		xcs.setFont(font1(xw));
		return xcs;
	}

	/**
	 * 该方法用于设置水平左对齐、垂直居中的样式
	 */
	private XSSFCellStyle style2(XSSFWorkbook xw) {
		XSSFCellStyle xcs = xw.createCellStyle();
		// 设置内容的水平居中对齐
		xcs.setAlignment(HorizontalAlignment.LEFT);
		// 设置内容的垂直居中对齐
		xcs.setVerticalAlignment(VerticalAlignment.CENTER);
		// 设置单元格自动换行
		xcs.setWrapText(true);
		// 设置字体的样式
		xcs.setFont(font2(xw));
		return xcs;
	}

	/**
	 * 该方法用于设置水平居中、垂直居中的样式
	 */
	private XSSFCellStyle style3(XSSFWorkbook xw) {
		XSSFCellStyle xcs = xw.createCellStyle();
		// 设置内容的水平居中对齐
		xcs.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容的垂直居中对齐
		xcs.setVerticalAlignment(VerticalAlignment.CENTER);
		// 设置单元格自动换行
		xcs.setWrapText(true);
		// 设置字体的样式
		xcs.setFont(font2(xw));
		return xcs;
	}

	/**
	 * 该方法用于设置加粗的字体
	 */
	private XSSFFont font1(XSSFWorkbook xw) {
		XSSFFont xf = xw.createFont();
		// 设置字体名称
		xf.setFontName("宋体");
		// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
		xf.setFontHeightInPoints((short) 12);
		// 设置字体加粗
		xf.setBold(true);
		return xf;
	}

	/**
	 * 该方法用于设置普通的字体
	 */
	private XSSFFont font2(XSSFWorkbook xw) {
		XSSFFont xf = xw.createFont();
		// 设置字体名称
		xf.setFontName("宋体");
		// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
		xf.setFontHeightInPoints((short) 12);
		return xf;
	}

	/**
	 * 该方法用于读取并通过字符串的形式返回单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	//
	// private String valueOf(XSSFCell cell) {
	// String s;
	//
	// if (cell.getCellTypeEnum().equals(CellType.STRING)) {
	// s = cell.getStringCellValue();
	// }
	//
	// else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
	// if (HSSFDateUtil.isCellDateFormatted(cell)) {
	// double d = cell.getNumericCellValue();
	// Date date = HSSFDateUtil.getJavaDate(d);
	// s = new SimpleDateFormat("yyyy-MM-dd").format(date);
	// } else {
	// s = String.valueOf((int) cell.getNumericCellValue());
	// }
	// } else if (cell.getCellTypeEnum().equals(CellType.BLANK)) {
	// s = "";
	// } else if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
	// s = String.valueOf(cell.getBooleanCellValue());
	// } else if (cell.getCellTypeEnum().equals(CellType.ERROR)) {
	// s = "";
	// } else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
	// s = cell.getCellFormula().toString();
	// } else {
	// s = null;
	// }
	//
	// return s;
	// }

	/**
	 * 用于识别文件的格式并将其转换成数组形式存储在类属性bugList中
	 * 
	 * @param bugListFile
	 * @throws IOException
	 */
	private void fileContentToList(File bugListFile) throws IOException {
		this.bugListFile = bugListFile;

		// TODO 添加标识符，用以判断传入的文件是csv文件还是xlsx文件
		if (this.bugListFile.getName().indexOf(".xlsx") > -1) {
			bugList = new ArrayList<>();

			// 读取导出的bug列表文件
			FileInputStream fip = new FileInputStream(this.bugListFile);
			// 通过XSSFWorkbook对表格文件进行操作
			XSSFWorkbook xw = new XSSFWorkbook(fip);
			// 关闭流
			fip.close();

			// 读取工作簿（禅道导出的数据在未改动的情况下工作簿的名称为Bug）
			XSSFSheet xs = xw.getSheetAt(0);

			// 循环，将整个表格转换成List<String[]>的形式
			// 读取所有行
			int cellNum = 0;// 用于存储列的总数
			for (int i = 0; i < xs.getLastRowNum() + 1; i++) {
				XSSFRow xr = xs.getRow(i);
				// 判断是否是第一行数据，由于每行最后一列直接获取可能导致获取到的数据不统一，但第一行的所有列是最全的（标题列），所以按照第一行进行获取所有列
				if (i == 0) {
					cellNum = xr.getLastCellNum();
				}
				// 定义存储列数据的数组
				String[] s = new String[cellNum];

				// 读取每行的所有列
				for (int j = 0; j < cellNum; j++) {
					// 存储单元格中的内容
					// 当读取到日期时，需要更正日期格式
					if (j == 20) {
						// 由于专业版与开源版读取到的数据类型有些偏差，故需要通过读取数据时是否报错来判断禅道的版本，当抛出IllegalStateException异常时，则禅道使用的是开源版
						try {
							// 专业版的读法
							s[j] = xs.getRow(i).getCell(j).getStringCellValue();
						} catch (IllegalStateException e) {
							// 开源版的读法
							s[j] = new SimpleDateFormat("yyyyMMdd")
									.format(DateUtil.getJavaDate(xs.getRow(i)
											.getCell(j).getNumericCellValue()));//
						}
					} else {
						// 若不是日期，则按照正常的方式读取，若读到空单元格时会出现空指针异常，此时需要将该行进行处理，将其存储为空
						try {
							s[j] = xr.getCell(j).toString();
						} catch (NullPointerException e) {
							s[j] = "";
						}
					}
				}
				// 存储一行单元格中的内容
				bugList.add(s);
			}

			xw.close();
		} else if (this.bugListFile.getName().indexOf(".csv") > -1) {
			CSVReader csvReader = new CSVReader(
					new FileReader(this.bugListFile));
			bugList = csvReader.readAll();
			csvReader.close();

		} else if (this.bugListFile.getName().indexOf(".xls") > -1) {
			bugList = new ArrayList<>();

			// 读取导出的bug列表文件
			FileInputStream fip = new FileInputStream(this.bugListFile);
			// 通过XSSFWorkbook对表格文件进行操作
			HSSFWorkbook hw = new HSSFWorkbook(fip);
			// 关闭流
			fip.close();

			// 读取工作簿（禅道导出的数据在未改动的情况下工作簿的名称为Bug）
			HSSFSheet hs = hw.getSheetAt(0);

			// 循环，将整个表格转换成List<String[]>的形式
			// 读取所有行
			int cellNum = 0;// 用于存储列的总数
			for (int i = 0; i < hs.getLastRowNum() + 1; i++) {
				HSSFRow hr = hs.getRow(i);
				// 判断是否是第一行数据，由于每行最后一列直接获取可能导致获取到的数据不统一，但第一行的所有列是最全的（标题列），所以按照第一行进行获取所有列
				if (i == 0) {
					cellNum = hr.getLastCellNum();
				}
				// 定义存储列数据的数组
				String[] s = new String[cellNum];

				// 读取每行的所有列
				for (int j = 0; j < cellNum; j++) {
					// 存储单元格中的内容
					// 当读取到日期时，需要更正日期格式
					if (j == 20) {
						// 由于专业版与开源版读取到的数据类型有些偏差，故需要通过读取数据时是否报错来判断禅道的版本，当抛出IllegalStateException异常时，则禅道使用的是开源版
						try {
							// 专业版的读法
							s[j] = hs.getRow(i).getCell(j).getStringCellValue();
						} catch (IllegalStateException e) {
							// 开源版的读法
							s[j] = new SimpleDateFormat("yyyyMMdd")
									.format(DateUtil.getJavaDate(hs.getRow(i)
											.getCell(j).getNumericCellValue()));//
						}
					} else {
						// 若不是日期，则按照正常的方式读取，若读到空单元格时会出现空指针异常，此时需要将该行进行处理，将其存储为空
						try {
							s[j] = hr.getCell(j).toString();
						} catch (NullPointerException e) {
							s[j] = "";
						}
					}
				}
				// 存储一行单元格中的内容
				bugList.add(s);
			}

			hw.close();
		} else {
			throw new IncompatibleFileLayoutException("不兼容的文件格式："
					+ bugListFile.getName());
		}
	}

	/**
	 * 用于向PreviousVerisionBugNumber.xml中添加当前版本的BUG数量
	 * 
	 * @param s
	 *            从BUG汇总表中记录的数据
	 * @param startTime
	 * @param endTime
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void writeVersionBugNumber(String[] s, String time)
			throws UnsupportedEncodingException, IOException {
		// 设置统计当前版本BUG至XML文件上
		Document dom = null;
		
		String judge_BUG = "bug";
		String judge_PROJECTED = "projected";
		
		try {
			dom = new SAXReader().read(verBugNumber);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		// 获取根节点
		Element element = dom.getRootElement();

		// 读取所有的项目标签，判断当前项目是否已存入XML中，若已存在，则获取其标签，若不存在，则创建项目标签
		@SuppressWarnings("unchecked")
		List<Element> es = element.elements("projected");
		for (Element e : es) {
			// 判断当前元素的name属性是否为当前的项目名称，是则直接获取当前元素，并结束循环
			if (e.attributeValue("name").equals(s[0])) {
				element = e;
				break;
			}
		}

		// 判断变量element的标签名称是否仍为根节点名称，若是，则说明当前项目未创建过标签，则直接创建项目标签及版本标签
		if (judge_BUG.equals(element.getName())) {
			element = element.addElement("projected");
			element.addAttribute("name", s[0]);

			// 创建版本，并将element指向版本标签
			element = element.addElement("version");
			element.addAttribute("version", s[1]);
			element.addAttribute("time", time);

			// 创建记录BUG数量的标签
			element.addElement("one").addAttribute("number", "");
			element.addElement("two").addAttribute("number", "");
			element.addElement("three").addAttribute("number", "");
			element.addElement("four").addAttribute("number", "");
		} else {
			// 如果查找到项目元素，则再查找当前版本是否存在，方法同查找项目标签
			@SuppressWarnings("unchecked")
			List<Element> vs = element.elements("version");
			for (Element v : vs) {
				if (v.attributeValue("version").equals(s[1])) {
					element = v;
					break;
				}
			}

			// 判断element的标签名称是否仍为projected，若是，则说明循环后未获取到相应的版本，则创建
			if (judge_PROJECTED.equals(element.getName())) {
				// 创建版本，并将element指向版本标签
				element = element.addElement("version");
				element.addAttribute("version", s[1]);
				element.addAttribute("time", time);

				// 创建记录BUG数量的标签
				element.addElement("one").addAttribute("number", "");
				element.addElement("two").addAttribute("number", "");
				element.addElement("three").addAttribute("number", "");
				element.addElement("four").addAttribute("number", "");
			} else {
				// 如果标签存在，则修改标签的时间信息
				element.attribute("time").setValue(time);
			}

		}

		// 获取当前项目对应版本的标签（xpath方式搜索）
		element = (Element) (dom.selectSingleNode("/bug/projected[@name=\""
				+ s[0] + "\"]/version[@version=\"" + s[1] + "\"]"));

		// 将其值添加入xml文件中
		element.element("one").attribute("number").setValue(s[2]);
		element.element("two").attribute("number").setValue(s[3]);
		element.element("three").attribute("number").setValue(s[4]);
		element.element("four").attribute("number").setValue(s[5]);

		// 使用dom4j的漂亮格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置xml文件的编码方式
		format.setEncoding("GBK");
		// 写入xml
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(verBugNumber),
				format);

		xmlWriter.write(dom);
		xmlWriter.close();

		// 用于创建测试报告的存储文件夹，已便于存储测试报告与处理后的Bug汇总表
		File f = new File(savePath.toString() + s[0] + "Ver" + s[1] + "版本测试报告");
		// 判断文件夹是否被创建，未被创建则创建文件夹
		if (!f.exists()) {
			f.mkdirs();
		}

		// 将Bug汇总表写入到文件夹中
		FileOutputStream fop = new FileOutputStream(new File(f
				+ ("\\附：" + s[0] + "Ver" + s[1]) + "版本Bug汇总表.xlsx"));
		createAccessory().write(fop);
		fop.close();
	}

	/**
	 * 用于获取Bug的版本信息，注意，需要根据标识符sign来传入文件类型，若sign为xlsx，则传入xs，bugList传入null；若为csv，
	 * 则xs传入null。
	 * 
	 * @param sign
	 * @param xs
	 * @param bugList
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String[] readVersionInformation(int i)
			throws UnsupportedEncodingException, IOException {
		StringBuilder temp = new StringBuilder();
		String[] temps;
		String[] s = new String[2];

		// 定位到模版中的影响版本中（导出的数据中第22列，即V那一列为影响版本，那里可以读取到测试的项目名称及版本号）
		temp.append(this.bugList.get(i)[BugFilePosition.VERSION]);
		
		// 判断版本号的分隔是否为Ver，之后按照版本号特定的字符进行分割，并存储获取到的数据到s中
		if (temp.indexOf("Ver") > -1) {
			temps = temp.toString().split("Ver");
		} else if(temp.indexOf("V") > -1)  {
			temps = temp.toString().split("V");
		} else {
			throw new IncorrectBugVersionException("列表第" + i + "条数据版本号不正确");
		}

		// 存储测试项目名称
		s[0] = temps[0].trim();

		// 存储版本号，由于获取到的元素中还包含一个禅道的标识符，其格式为“(#xx)”，此时可以查找“(#”来将字符串进行删除
		s[1] = new StringBuilder(temps[1]).delete(temps[1].indexOf("(#"),
				temps[1].length()).toString().trim();

		return s;
	}

	/**
	 * 读取时间信息
	 * 
	 * @param i
	 * @return
	 */
	private String readDataInformation(int i) {
		StringBuilder temp = new StringBuilder("");
		// 判断新增BUG数的方法，先判断创建日期是否为当天，再判断其影响版本是否为当前版本，最后再判断其激活次数是否大于0
		// 通过标识符读取日期信息
		// 先保留该行注释，以便于以后开发专业版时有参考
		// if (sign.equals("xlsx")) {
		// // 由于第一个提交的Bug时间会大于或等于测试开始时间，此时程序会读到表格的最后一行，若继续向下读
		// // 会造成获取不到值的情况，此时会抛出空指针异常，此处接收抛出的异常，之后结束整个循环
		// // 2018-6-23 备注： 我忘记当时的情况了，不敢改动这一段的代码···
		// try {
		// // 定位到模版中的创建时间中（导出的数据中第21列，即U那一列为创建时间）
		// //
		// 由于专业版与开源版读取到的数据类型有些偏差，故需要通过读取数据时是否报错来判断禅道的版本，当抛出IllegalStateException异常时，则禅道使用的是开源版
		// try {
		// temp.append(xs.getRow(i).getCell(20).getStringCellValue());// 专业版的读法
		// } catch (IllegalStateException e) {
		// temp.append(new SimpleDateFormat("yyyyMMdd")
		// .format(DateUtil.getJavaDate(xs.getRow(i).getCell(20).getNumericCellValue())));//
		// 开源版的读法
		// }
		// } catch (NullPointerException e) {
		// }
		// } else if (sign.equals("csv")) {
		// // 读取bugList的i行20列
		// temp.append(bugList.get(i)[20]);
		// }

		// 读取日期信息
		temp.append(bugList.get(i)[BugFilePosition.CREATE_TIME]);
		int j;
		// 删除获取到的文字中存在的分隔符
		while ((j = temp.indexOf("/")) > -1 || (j = temp.indexOf("-")) > -1) {
			temp.delete(j, j + 1);
		}

		return temp.toString();
	}

	/**
	 * 用于获取BUG的等级
	 */
	private String readBugLever(int i) {
		/*
		 * // 通过标识符判断BUG等级 // 读取严重程度的取值，并将相应的Bug等级的数量+1(严重程度在第9行，即I行) String
		 * bugLv = null; if (sign.equals("xlsx")) { //
		 * 获取Bug的等级,由于专业版与开源版读取到的数据类型有些偏差，故需要通过读取数据时是否报错来判断禅道的版本，
		 * 当抛出IllegalStateException异常时，则禅道使用的是开源版 try { bugLv =
		 * xs.getRow(i).getCell(8).getStringCellValue();// 专业版的读法 } catch
		 * (IllegalStateException e) { bugLv = String.valueOf((int)
		 * xs.getRow(i).getCell(8).getNumericCellValue());// 开源版的读法 } } else if
		 * (sign.equals("csv")) { bugLv = bugList.get(i)[8]; }
		 */

		// 读取Bug等级
		String bugLv = bugList.get(i)[BugFilePosition.SIGNIFICANCE];
		if (bugLv.indexOf(".") > -1) {
			bugLv = bugLv.substring(0, bugLv.indexOf("."));
		}
		return bugLv;
	}
	
	private String readBugTitle(int i) {
		return bugList.get(i)[BugFilePosition.TITLE];
	}
	
	private String readBugID(int i) {
		return bugList.get(i)[BugFilePosition.ID];
	}

	/**
	 * 用于判断查询的BUG是否为新发现的BUG<br/>
	 * 三个状态： <br/>
	 * 1表示当前Bug的创建时间在给定的时间以内 <br/>
	 * 0表示当前Bug的创建时间大于给定的时间<br/>
	 * -1表示当前Bug的创建时间小于给定的时间
	 */
	private int isNewBug(int i, String projected, String version, String temp,
			String startTime, String endTime) {
		// boolean is = false;
		// 三个状态：
		// 1表示当前Bug的创建时间在给定的时间以内
		// 0表示当前Bug的创建时间大于给定的时间
		// -1表示当前Bug的创建时间小于给定的时间
		int is = 0;

		// 读取当前BUG的影响版本
		String[] str1 = null;
		try {
			str1 = readVersionInformation(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 读取激活次数
		int count = (int) (Double
				.valueOf(bugList.get(i)[BugFilePosition.ACTIVE_COUNT])
				.doubleValue());

		// 将日期转换为数字进行比较，若得到的获取到的时间介于开始时间与结束时间之间时，则获取该Bug的等级
		// 读取日期信息 
		if (Integer.valueOf(temp) >= Integer.valueOf(startTime)
				&& Integer.valueOf(temp) <= Integer.valueOf(endTime)) {
			// 判断BUG的影响版本是否为当前版本（即判断影响版本是否与第一个版本一致）
			if (str1[0].equals(projected) && str1[1].equals(version)) {
				// 判断激活次数是否为0
				if (count == 0) {
					// 若激活次数为0且BUG的创建时间在测试时间范围内，则返回1
					is = 1;
				}
			}
		} else if (Integer.valueOf(temp) > Integer.valueOf(endTime)) {
			// 若BUG的创建时间在测试时间大于测试结束时间，则返回0
			is = 0;
		} else {
			is = -1;
		}
		
		return is;
	}

	/**
	 * 判断是否为激活的BUG
	 */
	private boolean isActiveBug(int i, String projected, String version) {
		boolean is = false;
		// 读取严重程度的取值，并将相应的Bug等级的数量+1(严重程度在第9行，即I行)
		// 获取Bug的等级,由于专业版与开源版读取到的数据类型有些偏差，故需要通过读取数据时是否报错来判断禅道的版本，当抛出IllegalStateException异常时，则禅道使用的是开源版
		// 若BUG不是新提交的BUG，则判断BUG是否为激活的BUG，依据则是判断版本号与激活次数是否大于1且版本为当前版本
		int count = (int) (Double
				.valueOf(bugList.get(i)[BugFilePosition.ACTIVE_COUNT])
				.doubleValue());
		// 判断激活次数是否大于0
		if (count > 0) {
			// 符对比影响版本
			String[] str1 = null;
			try {
				str1 = readVersionInformation(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 判断BUG的影响版本是否为当前版本（即判断影响版本是否与第一个版本一致）
			if (str1[0].equals(projected) && str1[1].equals(version)) {
				is = true;
			}
		}

		return is;
	}

	/**
	 * 统计往期的版本BUG，返回值有三个状态：
	 * 0表示往期非严重的BUG
	 * 1表示往期未解决的严重BUG
	 * 2表示往期延期解决的严重BUG
	 */
	private int isOldBug(int i) {
		// 如果读取到的BUG既不是新提交的BUG，也不是激活的BUG，则判断该BUG是否为严重BUG（1、2级）且是否是未解决的BUG
		// 读取解决方案那一列
		if ( bugList.get(i)[BugFilePosition.SOULVE_WAY].toString().equals("") ) {
			return 1;
		} else if (bugList.get(i)[BugFilePosition.SOULVE_WAY].toString().indexOf("延期") > -1) {
			return 2;
		} else {
			return 0;
		}
	}
	/*
	 * 
	 * private String valueOf(XSSFCell cell) { String s;
	 * 
	 * cell.getCellTypeEnum() == CellType.;
	 * 
	 * //方法过期 if (cell.getCellType() == Cell.CELL_TYPE_STRING) { s =
	 * cell.getStringCellValue(); }
	 * 
	 * else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) { if
	 * (HSSFDateUtil.isCellDateFormatted(cell)) { double d =
	 * cell.getNumericCellValue(); Date date = HSSFDateUtil.getJavaDate(d); s =
	 * new SimpleDateFormat("yyyy-MM-dd").format(date); } else { s =
	 * String.valueOf((int) cell.getNumericCellValue()); } } else if
	 * (cell.getCellType() == Cell.CELL_TYPE_BLANK) { s = ""; } else if
	 * (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) { s =
	 * String.valueOf(cell.getBooleanCellValue()); } else if (cell.getCellType()
	 * == Cell.CELL_TYPE_ERROR) { s = ""; } else if (cell.getCellType() ==
	 * Cell.CELL_TYPE_FORMULA) { s = cell.getCellFormula().toString(); } else {
	 * s = null; }
	 * 
	 * return s; }
	 */
	
	/**
	 * 用于记录BUG的编号、标题和等级信息
	 * @author 彭宇琦
	 */
	public class BugInformation {
		public String bugID;
		public String bugTitle;
		public int bugLv;
		public BugInformation(String bugID, String bugTitle, int bugLv) {
			super();
			if ( bugID.indexOf(".") > -1 ) {
				this.bugID = bugID.substring(0, bugID.indexOf("."));
			}
			this.bugTitle = bugTitle;
			this.bugLv = bugLv;
		}
	}
}
