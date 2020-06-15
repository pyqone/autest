package pres.auxiliary.work.selenium.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.directory.operate.MakeDirectory;
import pres.auxiliary.work.selenium.brower.AbstractBrower;

/**
 * <p>
 * <b>文件名：</b>Record.java
 * </p>
 * <p>
 * <b>用途：</b>用于记录自动化测试运行过程中的运行结果，最终输出一个excel文件。在文件中可以结合测
 * 试用例，并且可自动添加超链接，其需要的字段可以在LogTemplet.xml文件中配置。其写入文件中的信息
 * 通过类中的方法调用编写，来写入。若使用包中的Event类，则可以自动写入运行形式时的步骤记录，详细可见
 * {@link pres.auxiliary.selenium.temp.event.Event}
 * </p>
 * <p>
 * <b>编码时间：</b>2018年11月27日 上午10:44:26
 * </p>
 * 
 * @author 彭宇琦
 */
public class ExcelRecord {
	/**
	 * 用于存储文件的保存路径
	 */
	private StringBuilder savePath = new StringBuilder("C:\\AutoTest\\Record\\");
	/**
	 * 用于存储的文件名称
	 */
	private StringBuilder fileName = new StringBuilder("AutoTestRecord");
	/**
	 * 用于存储xml文件位置
	 */
	private final String CONFIG_FILE_PATH = "ConfigurationFiles/SeleniumConfigurationFile/LogConfiguration/LogTemplet.xml";
	/**
	 * 用于标记需要修改颜色的步骤
	 */
	private final String TEXT_START_SIGN = "&{";
	/**
	 * 用于标记需要修改颜色的步骤的初始位置和结束位置
	 */
	private final String TEXT_END_SIGN = "}&";

	/**
	 * 用于记录Bug数量
	 */
	private int bugNum = 0;

	/**
	 * 获取执行者名称，默认获取计算机名
	 */
	private String actionName = System.getenv().get("COMPUTERNAME");
	/**
	 * 用于存储浏览器对象
	 */
	private AbstractBrower brower = null;
	/**
	 * 存储记录开始时间
	 */
	private long startTime;
	/**
	 * 用于存储当前运行类的名称
	 */
	private String className = "";
	/**
	 * 用于存储当前执行的方法的名称
	 */
	private String methodName = "";

	/**
	 * 用于存储待写入的内容
	 */
	private StringBuilder[] contexts = new StringBuilder[13];
	/**
	 * 用于存储写入内容的序号
	 */
	private int[] ids = new int[6];
	/**
	 * 用于记录最后一条步骤，以方便在抛出异常时可直接记录
	 */
	private String lastStep = "";
	/**
	 * 用于记录异常
	 */
	private Exception exception = null;

	/**
	 * 存储正在写入的excel类
	 */
	private XSSFWorkbook xw;
	/**
	 * 用于定义当前指向的行数
	 */
	private XSSFRow xr;

	/**
	 * 用于记录自增序号
	 */
	int[] id = null;

	/**
	 * 用于按默认方式建文件保存位置及文件名称<br/>
	 * 默认位置为：C:\\AutoTest\\Record\\<br/>
	 * 默认文件名为（不带后缀）：TestResults
	 */
	public ExcelRecord() {
	}

	/**
	 * 用于按指定的路径以及默认的文件名保存测试结果文件<br/>
	 * 默认文件名为（不带后缀）：Image<br/>
	 * 注意，传入的文件路径可为相对路径，也可为绝对路径，若路径不符合windows下文件夹名称的名称规则，
	 * 则抛出IncorrectDirectoryException异常
	 * 
	 * @param savePath
	 *            指定的测试结果文件保存路径
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public ExcelRecord(String savePath) {
		this();
		setSavePath(savePath);
	}

	/**
	 * 用于按指定的路径以及指定的文件名保存测试结果文件
	 * 
	 * @param savePath
	 *            指定的测试结果文件保存路径
	 * @param imageName
	 *            指定的测试结果文件文件名
	 * @throws IncorrectDirectoryException
	 *             传入的路径不合法或者文件名不合法时抛出的异常
	 */
	public ExcelRecord(String savePath, String fileName) {
		this(savePath);
		setFileName(fileName);
	}

	/**
	 * 用于设置浏览器对象，以读取其浏览器及操作系统信息
	 * @param brower 浏览器对象
	 */
	public void setBrower(AbstractBrower brower) {
		this.brower = brower;
	}

	/**
	 * 用于返回记录文件存储的位置
	 * 
	 * @return 记录文件存储的位置
	 */
	public String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 该方法用于设置测试结果文件保存的位置，可传入相对路径，也可传入绝对路径，
	 * 若传入的路径不符合windows下文件夹名称的命名规则时，则抛出IncorrectDirectoryException异常
	 * 
	 * @param savePath
	 *            传入的测试结果文件保存路径
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public void setSavePath(String savePath) {
		// 将传入的路径封装成StringBuilder，以便格式化
		StringBuilder sb = new StringBuilder(savePath);
		// 格式化传入的路径
		MakeDirectory.formatPath(sb);

		// 判断传入的路径是否符合windows下对文件夹名称命名的规则，如果不符合则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isPath(sb.toString())) {
			throw new IncorrectDirectoryException("不合理的文件夹路径，文件路径：" + sb.toString());
		}

		// 将通过判断的sb赋给savePath属性
		this.savePath = sb;
	}

	/**
	 * 用于返回测试结果文件的文件名称（不含后缀）
	 * 
	 * @return 测试结果文件的文件名称
	 */
	public String getFileName() {
		return fileName.toString();
	}

	/**
	 * 该方法用于设置测试结果文件的文件名称，若传入的文件名不符合windows下文件的命名规则，
	 * 则抛出IncorrectDirectoryException异常
	 * 
	 * @param fileName
	 *            指定的测试结果文件名称
	 * @throws IncorrectDirectoryException
	 *             文件命名不正确时抛出的异常
	 */
	public void setFileName(String fileName) {
		// 判断传入的测试结果文件名称是否符合windows下的命名规则，若不符合，则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isFileName(fileName)) {
			throw new IncorrectDirectoryException("不合理的文件名称，文件名称：" + fileName);
		}

		// 通过判断后，则清空fileName存储的信息并将新的文件名称放入fileName种属性中
		this.fileName.delete(0, this.fileName.length());
		this.fileName.append(fileName);
	}

	/**
	 * 用于返回执行者的名字，在未进行设置时，则为计算机名称
	 * 
	 * @return 执行者的名字
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * 用于设置执行者的名字
	 * 
	 * @param actionName
	 *            执行者的名字
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * 用于记录异常
	 * 
	 * @param exception
	 *            异常类对象
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * 用于开始记录日志.当自动化测试框架采用JUnit时， 建议将其写在被@Before标记的方法中。
	 * 
	 * @param className
	 *            类名
	 * @param methodName
	 *            方法名
	 *            
	 * @return 类本身
	 */
	public ExcelRecord startRecord(String className, String methodName) {
		// 初始化所有的统计
		for (int i = 0; i < contexts.length; i++) {
			contexts[i] = new StringBuilder("");
		}
		for (int i = 0; i < ids.length; i++) {
			ids[i] = 0;
		}
		exception = null;
		lastStep = "";
		bugNum = 0;

		// 创建文件夹
		File f = new File(getSavePath());
		f.mkdirs();
		// 定位到记录文件
		f = new File(f + "/" + getFileName() + ".xlsx");
		// 判断记录文件是否存在，若不存在，则创建该文件
		if (!f.exists()) {
			createTemplet();
		}

		// 构造ecxel对象
		try {
			FileInputStream fip = new FileInputStream(f);
			xw = new XSSFWorkbook(fip);
			fip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 若id未初始化，则初始化id的数量，使其为xw中sheet的个数
		if (id == null) {
			id = new int[xw.getNumberOfSheets()];
		}

		// 记录开始时间
		startTime = System.currentTimeMillis();
		//记录运行类名及运行方法名
		this.className = className;
		this.methodName = methodName;

		return this;
	}

	/**
	 * 用于方法运行结束后的将运行的信息记录到文本中。当自动化测试框架采用JUnit时， 建议将其写在被@After标记的方法中，并通过一下格式进行调用：<br>
	 * endRecord(this.getClass().getName(), name.getMethodName());
	 * 
	 * @return 类本身
	 */
	public ExcelRecord endRecord() {
		// 获取执行结束时间，并减去开始时间，得到脚本执行时间，并记录到文件中
		double time = (System.currentTimeMillis() - startTime) / 1000.0;

		// 判断类名是否包含“.”
		if (className.indexOf(".") > -1) {
			// 对类名进行切分，并将其最后一个参数写入记录中
			String[] classNames = className.split("\\.");
			// 写入信息至id为run对应的sheet
			writeRunSheet(time, classNames[classNames.length - 1], methodName);
		} else {
			// 写入信息至id为run对应的sheet
			writeRunSheet(time, className, methodName);
		}

		// 将内容写入文件
		try {
			FileOutputStream fop = new FileOutputStream(new File(getSavePath() + getFileName() + ".xlsx"));
			xw.write(fop);
			fop.close();
			xw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 用于添加前置条件
	 * 
	 * @param text
	 * @return
	 */
	public ExcelRecord condition(String text) {
		contexts[0].append((++ids[0]) + "." + text + "\r\n");

		return this;
	}

	/**
	 * 用于添加步骤
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord step(String text) {
		// 记录当前步骤
		lastStep = text;
		contexts[1].append((++ids[1]) + "." + text + "\r\n");

		return this;
	}

	/**
	 * 用于添加执行结果
	 * 
	 * @param text
	 *            文本
	 * @param bug
	 *            是否为Bug
	 * @return 类本身
	 */
	public ExcelRecord result(String text, boolean bug) {
		if (bug) {
			text = TEXT_START_SIGN + text + TEXT_END_SIGN;
			bugNum++;
		}

		return result(text);
	}

	/**
	 * 用于添加执行结果
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord result(String text) {
		contexts[2].append((++ids[2]) + "." + text + "\r\n");
		return this;
	}

	/**
	 * 用于添加备注
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord mark(String text) {
		contexts[3].append((++ids[3]) + "." + text + "\r\n");

		return this;
	}

	/**
	 * 用于添加运行时截图
	 * 
	 * @param file
	 *            图片文件对象
	 * @return 类本身
	 */
	public ExcelRecord runScreenshot(File file) {
		contexts[4].append((++ids[4]) + "." + pathToURI(file.getAbsolutePath()) + "\r\n");

		return this;
	}

	/**
	 * 用于添加异常时截图
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord errorScreenshot(File file) {
		contexts[5].append((++ids[5]) + "." + pathToURI(file.getAbsolutePath()) + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例序号
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseID(String text) {
		contexts[6].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例模块
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseModule(String text) {
		contexts[7].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例标题
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseTitle(String text) {
		contexts[8].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例步骤
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseStep(String text) {
		contexts[9].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例预期
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseExpect(String text) {
		contexts[10].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例前置条件
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseCondition(String text) {
		contexts[11].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于添加测试用例所属流程
	 * 
	 * @param text
	 *            文本
	 * @return 类本身
	 */
	public ExcelRecord caseFlow(String text) {
		contexts[12].append(text + "\r\n");

		return this;
	}

	/**
	 * 用于创建日志文件
	 */
	private void createTemplet() {
		// 创建excel文件
		xw = new XSSFWorkbook();

		// 读取xml文件的信息
		Document d = null;
		try {
			d = new SAXReader().read(new File(CONFIG_FILE_PATH));
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// 读取所有sheet标签
		List<?> sheetList = d.getRootElement().elements("sheet");
		// 循环，创建sheet
		for (int i = 0; i < sheetList.size(); i++) {
			// 获取标签的name属性，并将该属性值作为sheet的名称
			XSSFSheet xs = xw.createSheet(((Element) (sheetList.get(i))).attributeValue("name"));

			// 获取sheet标签下的所有column标签
			List<?> columnList = ((Element) (sheetList.get(i))).elements("column");
			// 创建第一行，用于编写表头
			XSSFRow xr = xs.createRow(0);

			// 循环，创建单元格，并将其赋值
			for (int j = 0; j < columnList.size(); j++) {
				// 创建单元格
				XSSFCell xc = xr.createCell(j);
				// 填写表头
				xc.setCellValue(((Element) (columnList.get(j))).attributeValue("name"));
				// 读取宽度信息并设置单元格的宽度
				xs.setColumnWidth(j,
						(short) (Integer.valueOf(((Element) (columnList.get(j))).attributeValue("wide")) * 256));

				// 创建样式
				XSSFCellStyle xcs = xw.createCellStyle();
				// 设置单元格水平居中
				xcs.setAlignment(HorizontalAlignment.CENTER);
				// 设置单元格垂直居中
				xcs.setVerticalAlignment(VerticalAlignment.CENTER);
				// 创建字体样式
				XSSFFont xf = xw.createFont();
				// 设置字体名称
				xf.setFontName("宋体");
				// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
				xf.setFontHeightInPoints((short) 12);
				// 设置字体加粗
				xf.setBold(true);
				// 设置字体的样式
				xcs.setFont(xf);
				// 设置单元格自动换行
				xcs.setWrapText(true);

				// 设置单元格的样式
				xc.setCellStyle(xcs);
			}

			// 添加筛选按钮
			xs.setAutoFilter(CellRangeAddress.valueOf(
					(String.valueOf((char) (65)) + "1:" + String.valueOf((char) (65 + columnList.size() - 1)) + "1")));

			// 冻结表格
			int z = Integer.valueOf(((Element) (sheetList.get(i))).attributeValue("freeze"));
			xs.createFreezePane(z, 1, z, 1);
		}

		File f = new File(getSavePath() + getFileName() + ".xlsx");

		// 将预设内容写入Excel文件中，异常直接处理，不抛出
		try {
			// 定义输出流，用于向指定的Excel文件中写入内容
			FileOutputStream fop = new FileOutputStream(f);
			// 写入文件
			xw.write(fop);
			// 关闭流
			fop.close();
			xw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文本样式，其按照传入的参数进行选定
	 */
	private XSSFCellStyle textStyle(String align) {
		// 创建样式
		XSSFCellStyle xcs = xw.createCellStyle();
		// 设置单元格水平居中
		if (align.equalsIgnoreCase("center")) {
			xcs.setAlignment(HorizontalAlignment.CENTER);
		} else {
			xcs.setAlignment(HorizontalAlignment.LEFT);
		}
		// 设置单元格垂直居中
		xcs.setVerticalAlignment(VerticalAlignment.CENTER);
		// 创建字体样式
		XSSFFont xf = xw.createFont();
		// 设置字体名称
		xf.setFontName("宋体");
		// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
		xf.setFontHeightInPoints((short) 12);
		// 设置字体的样式
		xcs.setFont(xf);
		// 设置单元格自动换行
		xcs.setWrapText(true);

		// 设置单元格的样式
		return xcs;
	}

	/**
	 * 带超链接时追加其字体样式
	 */
	private void addLinkStyle(XSSFCell xc) {
		// 获取字体信息
		XSSFFont xf = xc.getCellStyle().getFont();
		// 添加下划线
		xf.setUnderline((byte) 1);
		// 设置字体颜色为蓝色
		xf.setColor(IndexedColors.BLUE.getIndex());
	}

	/**
	 * 添加文本的超链接
	 */
	private void addLink(XSSFCell xc, Excel excel, String path) {
		CreationHelper createHelper = xw.getCreationHelper();
		XSSFHyperlink link = null;

		// 获取传入的信息
		String[] s = excel.getElement().attributeValue("link").split(":");
		// 判断其是需要链接至文件中位置还是链接至外部文件，若按照冒号分割能得到两个结果，则说明其是内链，否则为外链
		if (s.length == 2) {
			// 根据拆分的信息读取其需要链接的位置
			Excel sheet = excel.getSheetExcel(s[0]);
			String sheetName = sheet.getElement().attributeValue("name");
			String cellValue = String.valueOf(sheet.getColumnExcel(s[1]).getCell());
			// 添加文件内链接
			link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.DOCUMENT);
			link.setAddress("'" + sheetName + "'!" + cellValue + (xw.getSheet(sheetName).getLastRowNum() + 1));
		} else {
			// 添加本地文件链接
			link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.FILE);
			link.setAddress(path);
		}

		// 在单元格上添加超链接
		xc.setHyperlink(link);
		// 新增单元格的样式
		addLinkStyle(xc);
	}

	/**
	 * 用于写入单元格信息
	 */
	private XSSFCell writeCell(Excel value, String context) {
		return writeCell(xr, value, context);
	}

	/**
	 * 用于写入单元格信息
	 */
	private XSSFCell writeCell(XSSFRow xr, Excel value, String context) {
		XSSFCell xc = xr.createCell(Integer.valueOf(value.getValue()));
		// 创建并写入操作系统信息
		xc.setCellValue(context);
		xc.setCellStyle(textStyle(value.getElement().attributeValue("align")));

		return xc;
	}

	/**
	 * 用于将路径的反斜杠转换为斜杠表示
	 */
	private String pathToURI(String path) {
		StringBuilder uri = new StringBuilder(path);

		// 记录反斜杠的位置
		int i = 0;
		// 循环，替换所有的反斜杠
		while ((i = uri.indexOf("\\")) > -1) {
			uri.replace(i, i + 1, "/");
		}

		return uri.toString();
	}

	/**
	 * 用于标记为BUG的结果
	 */
	private void signText(XSSFCell xc, IndexedColors color) {
		// 获取文本
		StringBuilder text = new StringBuilder(xc.toString());
		// 判断其内容是否为空，或者是否存在标记，若为空或者不存在标记，则直接返回
		if ("".equals(text.toString()) || text.indexOf(TEXT_START_SIGN) == -1) {
			return;
		}

		// 存储需要标记的文本位置
		StringBuilder position = new StringBuilder("");

		// 循环，查找带标记的文本，存储其需要标记的位置，并将标记删除
		while (text.indexOf(TEXT_START_SIGN) > -1) {
			// int strat = text.indexOf(TEXT_START_SIGN) + TEXT_START_SIGN.length();
			// 获取开始标记，注意，此处由于之后会将标记删除，后面的文本会顶替该标记的位置，故不需要加上其标记的长度
			int start = text.indexOf(TEXT_START_SIGN);
			int end = text.indexOf(TEXT_END_SIGN);

			// 删除标记
			text.delete(start, start + TEXT_START_SIGN.length());
			text.delete(end - TEXT_START_SIGN.length(), end + TEXT_END_SIGN.length() - TEXT_START_SIGN.length());

			// 由于标记被删除，所以需要重新定义结束标记
			end = end + TEXT_END_SIGN.length() - TEXT_START_SIGN.length();

			// 存储标记
			position.append(start + ":" + end + "*");

		}
		// 删除多余的分割标记
		position.delete(position.lastIndexOf("*"), position.length());

		// 将新的内容写单元格
		xc.setCellValue(text.toString());

		// 循环，对文档进行标记。由于poi在设置单元格格式后，会覆盖原有的标记，故只能在上循环外重新获取标记位置并进行标记
		for (String sign : position.toString().split("\\*")) {
			// 将文本进行分割，得到需要标记的位置
			String[] pos = sign.split(":");

			int start = Integer.valueOf(pos[0]);
			int end = Integer.valueOf(pos[1]);

			// 创建字体
			XSSFFont xf = xw.createFont();
			// 设置字体名称
			xf.setFontName("宋体");
			// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
			xf.setFontHeightInPoints((short) 12);
			// 设置颜色
			xf.setColor(color.getIndex());
			// 设置文本样式
			xc.getRichStringCellValue().applyFont(start - TEXT_START_SIGN.length(), end - TEXT_START_SIGN.length(), xf);

		}
	}

	/**
	 * 写入id为run的sheet信息
	 * @param time 写入当前时间
	 * @param className 写入类名
	 * @param methodName 写入方法名
	 */
	private void writeRunSheet(double time, String className, String methodName) {
		// 获取第一个sheet的信息
		XSSFSheet xs = xw.getSheet(Excel.SHEET_RUN.getElement().attributeValue("name"));
		// 获取该行最后一个单元格
		xr = xs.createRow(xs.getLastRowNum() + 1);

		// 创建序号一列的单元格，并填写序号信息，及单元格格式
		writeCell(Excel.RUN_COLUMN_ID, String.valueOf(++id[Excel.SHEET_RUN.getValue()]));
		// 创建并填写执行者信息
		writeCell(Excel.RUN_COLUMN_ACTIVE_PERSON, getActionName());
		// 创建并填写执行时间信息（当前时间）
		writeCell(Excel.RUN_COLUMN_ACTIVE_TIME, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		// 若传入了浏览器对象，则记录浏览器及版本
		if (brower != null) {
			writeCell(Excel.RUN_COLUMN_BROWER, brower.getBrowerName());
			writeCell(Excel.RUN_COLUMN_VERSION, brower.getBrowerVersion());
		}

		// 创建并写入操作系统信息
		writeCell(Excel.RUN_COLUMN_SYSTEM, System.getProperties().getProperty("os.name"));

		// 创建并写入操作系统信息
		writeCell(Excel.RUN_COLUMN_USE_TIME, String.valueOf(time));

		// 创建并写入类名
		writeCell(Excel.RUN_COLUMN_CLASS_NAME, className);

		// 创建并写入方法名
		writeCell(Excel.RUN_COLUMN_METHOD_NAME, methodName);

		// 清除包含的多余空格
		for (int i = 0; i < contexts.length; i++) {
			if (contexts[i].lastIndexOf("\r\n") == -1) {
				continue;
			}
			contexts[i].delete(contexts[i].lastIndexOf("\r\n"), contexts[i].length());
		}

		if (!"".equals(contexts[6].toString()) && !"".equals(contexts[8].toString())
				&& !"".equals(contexts[9].toString()) && !"".equals(contexts[10].toString())) {
			XSSFCell caseIDXc = writeCell(Excel.RUN_COLUMN_CASE_ID, contexts[6].toString());
			writeCaseSheet();
			addLink(caseIDXc, Excel.RUN_COLUMN_CASE_ID, null);
		}

		// 写入Bug数量
		writeCell(Excel.RUN_COLUMN_BUG_NUMBER, String.valueOf(bugNum));

		// 写入执行状态信息，若异常类变量为null则表示程序运行结束，则写入正常，否则则写入异常，并添加超链接
		if (exception == null) {
			writeCell(Excel.RUN_COLUMN_STATE, "正常");
		} else {
			XSSFCell runXc = writeCell(Excel.RUN_COLUMN_STATE, "异常");
			// 写入error对应的Sheet
			writeErrorSheet(className, methodName);
			addLink(runXc, Excel.RUN_COLUMN_STATE, null);
		}

		// 写入前置条件信息
		writeCell(Excel.RUN_COLUMN_CONDITION, contexts[0].toString());
		// 写入步骤信息
		writeCell(Excel.RUN_COLUMN_STEP, contexts[1].toString());

		// 写入结果信息
		signText(writeCell(Excel.RUN_COLUMN_RESULT, contexts[2].toString()), IndexedColors.RED);

		// 写入备注信息
		writeCell(Excel.RUN_COLUMN_MARK, contexts[3].toString());

		// 若截图中存储信息，则写入截图信息，并在与截图相关的sheet中添加信息，且添加超链接
		if (!"".equals(contexts[4].toString())) {
			XSSFCell runXc = writeCell(Excel.RUN_COLUMN_SCREENSHOT_POSITION, contexts[4].toString());
			// 写入id为run_screenshot对应的Sheet
			writeRunScreenshotSheet(className, methodName);
			addLink(runXc, Excel.RUN_COLUMN_SCREENSHOT_POSITION, null);
		}
	}

	/**
	 * TODO 写入id为run_screenshot的sheet信息
	 */
	private void writeRunScreenshotSheet(String className, String methodName) {
		// 定位到运行时截图sheet上
		XSSFSheet runScreenXs = xw.getSheet(Excel.SHEET_RUN_SCREENSHOT.getElement().attributeValue("name"));
		// 创建一行
		XSSFRow runScreenXr = runScreenXs.createRow(runScreenXs.getLastRowNum() + 1);
		// 写入运行截图序号
		writeCell(runScreenXr, Excel.RUN_SCREENSHOT_COLUMN_ID,
				String.valueOf(++id[Excel.SHEET_RUN_SCREENSHOT.getValue()]));
		// 写入运行截图类名
		writeCell(runScreenXr, Excel.RUN_SCREENSHOT_COLUMN_CLASS_NAME, className);
		// 写入运行截图方法名
		addLink(writeCell(runScreenXr, Excel.RUN_SCREENSHOT_COLUMN_METHOD_NAME, methodName),
				Excel.RUN_SCREENSHOT_COLUMN_METHOD_NAME, null);

		// 循环，添加所有的截图至单元格
		String[] runContext = contexts[4].toString().split("\r\n");
		for (int i = 0; i < runContext.length; i++) {
			// 创建单元格
			XSSFCell runScreenXc = runScreenXr.createCell(runScreenXr.getLastCellNum());
			// 写入单元格内容
			runScreenXc.setCellValue(runContext[i]);
			// 单元格格式
			runScreenXc.setCellStyle(
					textStyle(Excel.RUN_SCREENSHOT_COLUMN_SCREENSHOT.getElement().attributeValue("align")));
			addLink(runScreenXc, Excel.ERROR_SCREENSHOT_COLUMN_SCREENSHOT,
					runContext[i].substring(runContext[i].indexOf(".") + 1));
			runScreenXs.setColumnWidth(runScreenXr.getLastCellNum() - 1, 25 * 256);
		}
	}

	/**
	 * TODO 写入id为erro的sheet信息
	 */
	private void writeErrorSheet(String className, String methodName) {
		// 定位到异常sheet上
		XSSFSheet errorXs = xw.getSheet(Excel.SHEET_ERROR.getElement().attributeValue("name"));
		// 创建一行
		XSSFRow errorXr = errorXs.createRow(errorXs.getLastRowNum() + 1);
		// 写入异常序号
		writeCell(errorXr, Excel.ERROR_COLUMN_ID, String.valueOf(++id[Excel.SHEET_ERROR.getValue()]));
		// 写入异常类名
		writeCell(errorXr, Excel.ERROR_COLUMN_CLASS_NAME, className);
		// 写入异常方法名，并为其添加超链接
		addLink(writeCell(errorXr, Excel.ERROR_COLUMN_METHOD_NAME, methodName), Excel.ERROR_COLUMN_METHOD_NAME, null);
		// 写入异常错误步骤，即记录的最后一个步骤
		writeCell(errorXr, Excel.ERROR_COLUMN_ERROR_STEP, lastStep);
		// 写入异常类
		writeCell(errorXr, Excel.ERROR_COLUMN_ERROR_CLASS, exception.getClass().getName());
		// 写入异常信息
		writeCell(errorXr, Excel.ERROR_COLUMN_ERROR_INFORMATION, exception.getMessage());
		// 若截图中存储信息，则写入截图信息，并在与截图相关的sheet中添加信息，且添加超链接
		if (!"".equals(contexts[5].toString())) {
			XSSFCell errorXc = writeCell(errorXr, Excel.ERROR_COLUMN_SCREENSHOT_POSITION, contexts[5].toString());
			// 写入id为error_screenshot对应的sheet
			writeErrorScreenshotSheet(className, methodName);
			addLink(errorXc, Excel.ERROR_COLUMN_SCREENSHOT_POSITION, null);
		}

	}

	/**
	 * TODO 写入id为error_screenshot的sheet信息
	 */
	private void writeErrorScreenshotSheet(String className, String methodName) {
		// 定位到运行时截图sheet上
		XSSFSheet errorScreenXs = xw.getSheet(Excel.SHEET_ERROR_SCREENSHOT.getElement().attributeValue("name"));
		// 创建一行
		XSSFRow errorScreenXr = errorScreenXs.createRow(errorScreenXs.getLastRowNum() + 1);
		// 写入运行截图序号
		writeCell(errorScreenXr, Excel.ERROR_SCREENSHOT_COLUMN_ID,
				String.valueOf(++id[Excel.SHEET_ERROR_SCREENSHOT.getValue()]));
		// 写入运行截图类名
		writeCell(errorScreenXr, Excel.ERROR_SCREENSHOT_COLUMN_CLASS_NAME, className);
		// 写入运行截图方法名
		addLink(writeCell(errorScreenXr, Excel.ERROR_SCREENSHOT_COLUMN_METHOD_NAME, methodName),
				Excel.ERROR_SCREENSHOT_COLUMN_METHOD_NAME, null);

		// 循环，添加所有的截图至单元格
		String[] errorContext = contexts[5].toString().split("\r\n");
		for (int i = 0; i < errorContext.length; i++) {
			// 创建单元格
			XSSFCell errorScreenXc = errorScreenXr.createCell(errorScreenXr.getLastCellNum());
			// 写入单元格内容
			errorScreenXc.setCellValue(errorContext[i]);
			// 单元格格式
			errorScreenXc.setCellStyle(
					textStyle(Excel.ERROR_SCREENSHOT_COLUMN_SCREENSHOT.getElement().attributeValue("align")));
			errorScreenXs.setColumnWidth(errorScreenXr.getLastCellNum() - 1, 25 * 256);
			addLink(errorScreenXc, Excel.ERROR_SCREENSHOT_COLUMN_SCREENSHOT,
					errorContext[i].substring(errorContext[i].indexOf(".") + 1));
		}
	}

	/**
	 * TODO 写入信息至id为case的sheet信息
	 */
	private void writeCaseSheet() {
		// 定位到运行时截图sheet上
		XSSFSheet caseScreenXs = xw.getSheet(Excel.SHEET_CASE.getElement().attributeValue("name"));
		// 创建一行
		XSSFRow caseScreenXr = caseScreenXs.createRow(caseScreenXs.getLastRowNum() + 1);
		// 写入测试用例编号
		addLink(writeCell(caseScreenXr, Excel.CASE_COLUMN_CASE_ID, contexts[6].toString()), Excel.CASE_COLUMN_CASE_ID,
				null);
		// 写入测试用例所属模块
		writeCell(caseScreenXr, Excel.CASE_COLUMN_MODULE, contexts[7].toString());
		// 写入测试用例标题
		writeCell(caseScreenXr, Excel.CASE_COLUMN_TITLE, contexts[8].toString());
		// 写入测试用例步骤
		writeCell(caseScreenXr, Excel.CASE_COLUMN_STEP, contexts[9].toString());
		// 写入测试用例预期
		writeCell(caseScreenXr, Excel.CASE_COLUMN_EXPECT, contexts[10].toString());
		// 写入测试用例前置条件
		writeCell(caseScreenXr, Excel.CASE_COLUMN_CONDITION, contexts[11].toString());
		// 写入测试用例所属流程
		writeCell(caseScreenXr, Excel.CASE_COLUMN_FLOW, contexts[12].toString());

		// 添加超链接
	}
	
	/**
	 * <p><b>文件名：</b>Record.java</p>
	 * <p><b>用途：</b>用于对Ecxel运行记录的表格进行定位的枚举类，由于需要定位的行列表格较多，
	 * 不能直接写到类中，故使用枚举来列举所有需要定位的行列</p>
	 * <p><b>编码时间：</b>2018年11月27日 上午10:44:26</p>
	 * <p><b>修改时间：</b>2019年10月23日上午9:14:57</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 12
	 *
	 */
	private enum Excel {
		/**
		 * 指向id为case的sheet
		 */
		SHEET_CASE("/templet/sheet[@id='case']"),
		/**
		 * 指向id为run的sheet
		 */
		SHEET_RUN("/templet/sheet[@id='run']"),
		/**
		 * 指向run_screenshot为run的sheet
		 */
		SHEET_RUN_SCREENSHOT("/templet/sheet[@id='run_screenshot']"),
		/**
		 * 指向error为run的sheet
		 */
		SHEET_ERROR("/templet/sheet[@id='error']"),
		/**
		 * 指向error_screenshot为run的sheet
		 */
		SHEET_ERROR_SCREENSHOT("/templet/sheet[@id='error_screenshot']"),
		/**
		 * 指向id为case的sheet中id为case_id的列
		 */
		CASE_COLUMN_CASE_ID(SHEET_CASE.getXpath() + "/column[@id='case_id']"),
		/**
		 * 指向id为case的sheet中id为module的列
		 */
		CASE_COLUMN_MODULE(SHEET_CASE.getXpath() + "/column[@id='module']"),
		/**
		 * 指向id为case的sheet中id为title的列
		 */
		CASE_COLUMN_TITLE(SHEET_CASE.getXpath() + "/column[@id='title']"),
		/**
		 * 指向id为case的sheet中id为step的列
		 */
		CASE_COLUMN_STEP(SHEET_CASE.getXpath() + "/column[@id='step']"),
		/**
		 * 指向id为case的sheet中id为expect的列
		 */
		CASE_COLUMN_EXPECT(SHEET_CASE.getXpath() + "/column[@id='expect']"),
		/**
		 * 指向id为case的sheet中id为condition的列
		 */
		CASE_COLUMN_CONDITION(SHEET_CASE.getXpath() + "/column[@id='condition']"),
		/**
		 * 指向id为case的sheet中id为flow的列
		 */
		CASE_COLUMN_FLOW(SHEET_CASE.getXpath() + "/column[@id='flow']"),
		/**
		 * 指向id为run的sheet中id为的id列
		 */
		RUN_COLUMN_ID(SHEET_RUN.getXpath() + "/column[@id='id']"),
		/**
		 * 指向id为run的sheet中id为的case_id列
		 */
		RUN_COLUMN_CASE_ID(SHEET_RUN.getXpath() + "/column[@id='case_id']"),
		/**
		 * 指向id为run的sheet中id为class_name的列
		 */
		RUN_COLUMN_CLASS_NAME(SHEET_RUN.getXpath() + "/column[@id='class_name']"),
		/**
		 * 指向id为run的sheet中id为method_name的列
		 */
		RUN_COLUMN_METHOD_NAME(SHEET_RUN.getXpath() + "/column[@id='method_name']"),
		/**
		 * 指向id为run的sheet中id为state的列
		 */
		RUN_COLUMN_STATE(SHEET_RUN.getXpath() + "/column[@id='state']"),
		/**
		 * 指向id为run的sheet中id为step的列
		 */
		RUN_COLUMN_STEP(SHEET_RUN.getXpath() + "/column[@id='step']"),
		/**
		 * 指向id为run的sheet中id为result的列
		 */
		RUN_COLUMN_RESULT(SHEET_RUN.getXpath() + "/column[@id='result']"),
		/**
		 * 指向id为run的sheet中id为condition的列
		 */
		RUN_COLUMN_CONDITION(SHEET_RUN.getXpath() + "/column[@id='condition']"),
		/**
		 * 指向id为run的sheet中id为mark的列
		 */
		RUN_COLUMN_MARK(SHEET_RUN.getXpath() + "/column[@id='mark']"),
		/**
		 * 指向id为run的sheet中id为bug_number的列
		 */
		RUN_COLUMN_BUG_NUMBER(SHEET_RUN.getXpath() + "/column[@id='bug_number']"),
		/**
		 * 指向id为run的sheet中id为screenshot_position的列
		 */
		RUN_COLUMN_SCREENSHOT_POSITION(
				SHEET_RUN.getXpath() + "/column[@id='screenshot_position']"),
		/**
		 * 指向id为run的sheet中id为use_time的列
		 */
		RUN_COLUMN_USE_TIME(SHEET_RUN.getXpath() + "/column[@id='use_time']"),
		/**
		 * 指向id为run的sheet中id为active_person的列
		 */
		RUN_COLUMN_ACTIVE_PERSON(
				SHEET_RUN.getXpath() + "/column[@id='active_person']"),
		/**
		 * 指向id为run的sheet中id为active_time的列
		 */
		RUN_COLUMN_ACTIVE_TIME(SHEET_RUN.getXpath() + "/column[@id='active_time']"),
		/**
		 * 指向id为run的sheet中id为brower的列
		 */
		RUN_COLUMN_BROWER(SHEET_RUN.getXpath() + "/column[@id='brower']"),
		/**
		 * 指向id为run的sheet中id为version的列
		 */
		RUN_COLUMN_VERSION(SHEET_RUN.getXpath() + "/column[@id='version']"),
		/**
		 * 指向id为run的sheet中id为system的列
		 */
		RUN_COLUMN_SYSTEM(SHEET_RUN.getXpath() + "/column[@id='system']"),
		/**
		 * 指向id为run_screenshot的sheet中id为id的列
		 */
		RUN_SCREENSHOT_COLUMN_ID(
				SHEET_RUN_SCREENSHOT.getXpath() + "/column[@id='id']"),
		/**
		 * 指向id为run_screenshot的sheet中id为class_name的列
		 */
		RUN_SCREENSHOT_COLUMN_CLASS_NAME(
				SHEET_RUN_SCREENSHOT.getXpath() + "/column[@id='class_name']"),
		/**
		 * 指向id为run_screenshot的sheet中id为method_name的列
		 */
		RUN_SCREENSHOT_COLUMN_METHOD_NAME(
				SHEET_RUN_SCREENSHOT.getXpath() + "/column[@id='method_name']"),
		/**
		 * 指向id为run_screenshot的sheet中id为screenshot的列
		 */
		RUN_SCREENSHOT_COLUMN_SCREENSHOT(
				SHEET_RUN_SCREENSHOT.getXpath() + "/column[@id='screenshot']"),
		/**
		 * 指向id为error的sheet中id为id的列
		 */
		ERROR_COLUMN_ID(SHEET_ERROR.getXpath() + "/column[@id='id']"),
		/**
		 * 指向id为error的sheet中id为class_name的列
		 */
		ERROR_COLUMN_CLASS_NAME(
				SHEET_ERROR.getXpath() + "/column[@id='class_name']"),
		/**
		 * 指向id为error的sheet中id为method_name的列
		 */
		ERROR_COLUMN_METHOD_NAME(
				SHEET_ERROR.getXpath() + "/column[@id='method_name']"),
		/**
		 * 指向id为error的sheet中id为error_step的列
		 */
		ERROR_COLUMN_ERROR_STEP(
				SHEET_ERROR.getXpath() + "/column[@id='error_step']"),
		/**
		 * 指向id为error的sheet中id为error_class的列
		 */
		ERROR_COLUMN_ERROR_CLASS(
				SHEET_ERROR.getXpath() + "/column[@id='error_class']"),
		/**
		 * 指向id为error的sheet中id为error_information的列
		 */
		ERROR_COLUMN_ERROR_INFORMATION(
				SHEET_ERROR.getXpath() + "/column[@id='error_information']"),
		/**
		 * 指向id为error的sheet中id为screenshot_position的列
		 */
		ERROR_COLUMN_SCREENSHOT_POSITION(
				SHEET_ERROR.getXpath() + "/column[@id='screenshot_position']"),
		/**
		 * 指向id为error_screenshot的sheet中id为id的列
		 */
		ERROR_SCREENSHOT_COLUMN_ID(
				SHEET_ERROR_SCREENSHOT.getXpath() + "/column[@id='id']"),
		/**
		 * 指向id为error_screenshot的sheet中id为class_name的列
		 */
		ERROR_SCREENSHOT_COLUMN_CLASS_NAME(
				SHEET_ERROR_SCREENSHOT.getXpath() + "/column[@id='class_name']"),
		/**
		 * 指向id为error_screenshot的sheet中id为method_name的列
		 */
		ERROR_SCREENSHOT_COLUMN_METHOD_NAME(
				SHEET_ERROR_SCREENSHOT.getXpath() + "/column[@id='method_name']"),
		/**
		 * 指向id为error_screenshot的sheet中id为screenshot的列
		 */
		ERROR_SCREENSHOT_COLUMN_SCREENSHOT(
				SHEET_ERROR_SCREENSHOT.getXpath() + "/column[@id='screenshot']"),;

		// 用于存储xml文件位置
		private final String CONFIG_FILE_PATH = "ConfigurationFiles/SeleniumConfigurationFile/LogConfiguration/LogTemplet.xml";

		// 存储xpath
		private String xpath;
		// 存储节点的标记
		private short type;
		// 存储节点的id属性
		private String id;
		// 用于存储父节点的id
		private String parentId;

		private Excel(String xpath) {
			this.xpath = xpath;

			// 判断其xptah是否含有column，若包含，则表示其为column节点，否则为sheet节点
			if (this.xpath.indexOf("column") > -1) {
				this.type = 1;
			} else {
				this.type = 0;
			}

			// 获取其id属性
			this.id = xpath.substring(xpath.lastIndexOf("@id='") + "@id='".length(),
					xpath.lastIndexOf("'"));

			// 获取该节点的父节点
			if (this.type == 1) {
				this.parentId = this.xpath.substring(
						this.xpath.indexOf("@id='") + "@id='".length(),
						this.xpath.indexOf("']/column"));
			} else {
				this.parentId = "";
			}
		}

		/**
		 * 返回枚举对应的Elemen对象
		 * 
		 * @return Elemen对象
		 */
		public Element getElement() {
			try {
				return (Element) (new SAXReader().read(new File(CONFIG_FILE_PATH))
						.selectSingleNode(xpath));
			} catch (DocumentException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 根据Column元素的id查找元素，并将其枚举常量返回，若id查找不到，则返回null。根据调用的节点不同，其返回方式也有一些不同，
		 * 具体的方式如下：
		 * <ol>
		 * <li>若参数本身为sheet节点（type值为0），则判断每个元素的父节点id是否为当前节点的id，再判断子节点id（元素column节点的id
		 * ）是否为传入的id</li>
		 * <li>若参数本身为column节点（type值为1），则判断每个元素的父节点id是否为当前节点父节点的id，再判断子节点id（
		 * 元素column节点的id）是否为传入的id</li>
		 * </ol>
		 * 
		 * @param sonId
		 *            节点的id
		 * @return id对应的枚举常量
		 */
		public Excel getColumnExcel(String sonId) {
			// 遍历枚举类中的所有枚举常量
			for (Excel excel : Excel.values()) {
				// 返回依据：
				// 1.若参数本身为sheet节点（type值为0），则判断每个元素的父节点id是否为当前节点的id，再判断子节点id（元素column节点的id）是否为传入的id
				// 2.若参数本身为column节点（type值为1），则判断每个元素的父节点id是否为当前节点父节点的id，再判断子节点id（元素column节点的id）是否为传入的id
				// 简单来说，若节点为sheet节点，则直接获取子节点；若节点为colnum节点，则先获取起sheet节点，再获取column节点
				if (type == (short) 0 && excel.parentId.equals(id)
						&& excel.id.equals(sonId)) {
					return excel;
				} else if (type == (short) 1 && excel.parentId.equals(parentId)
						&& excel.id.equals(sonId)) {
					return excel;
				}
			}

			return null;
		}

		/**
		 * 根据传入id来返回sheet节点
		 * 
		 * @param id
		 *            sheet节点id
		 * @return id对应的sheet节点
		 */
		public Excel getSheetExcel(String id) {
			// 遍历枚举类中的所有枚举常量
			for (Excel excel : Excel.values()) {
				// 判断参数是否为sheet节点，再判断其id是否一致，都一致，则返回起枚举常量
				if (excel.type == (short) 0 && excel.id.equals(id)) {
					return excel;
				}
			}

			return null;
		}

		/**
		 * 用于返回当前column节点所在的sheet节点。若传入的是sheet节点，则直接返回本身
		 * 
		 * @return sheet节点
		 */
		/*
		public Excel getSheetExcel() {
			// 判断当前节点是否为sheet节点，若是，则直接返回其本身
			if (type == (short) 0) {
				return this;
			}

			// 遍历枚举类中的所有枚举常量
			for (Excel excel : Excel.values()) {
				// 判断参数是否为sheet节点，再判断其id是否一致，都一致，则返回起枚举常量
				if (excel.type == (short) 0 && excel.id.equals(parentId)) {
					return excel;
				}
			}

			return null;
		}
		*/

		/**
		 * 用于返回元素对应的xpath路径
		 * 
		 * @return xpath路径
		 */
		public String getXpath() {
			return xpath;
		}

		/**
		 * 用于返回元素在xml文件中的位置，以达到确定其在Excel文件中所在的列（元素的下标从0开始）。
		 * 注意，该方法无论是sheet节点还是column节点，其返回的方法都是按照通过父节点来查找子节点的
		 * 方式进行，所以其返回值在不同的节点下返回的结果可能相同。
		 * 
		 * @return 元素位置
		 */
		public int getValue() {
			// 记录其元素的位置
			int value = -1;
			// 获取元素
			Element element = getElement();
			// 获取元素的父节点
			Element fElement = element.getParent();
			// 获取父节点下的所有节点的迭代器
			List<?> elements = fElement.elements();

			// 循环，遍历所有节点，找到与需要查找的元素匹配的元素，返回其位置
			for (int i = 0; i < elements.size(); i++) {
				if (((Element) (elements.get(i))).attributeValue("id")
						.equals(getElement().attributeValue("id"))) {
					value = i;
					break;
				}
			}
			return value;
		}

		/**
		 * 用于返回其元素对应的表格列标记，即在Excel中，第一列的标记为A，以此类推。
		 * 注意，若元素为sheet标签，则同样以其value值加上65的形式，以字符返回。
		 * 
		 * @return 元素在Excel文件中列的标记
		 */
		public char getCell() {
			return (char) (65 + getValue());
		}

		/**
		 * 用于返回节点的id属性
		 * 
		 * @return 节点的id属性
		 */
		/*
		public String getId() {
			return id;
		}
		 */
		/**
		 * 返回父节点的id属性
		 * 
		 * @return 父节点id属性
		 */
		/*
		public String getParentId() {
			return parentId;
		}
		*/
	}

}
