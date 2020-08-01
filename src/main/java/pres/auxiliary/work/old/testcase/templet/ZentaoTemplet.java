package pres.auxiliary.work.old.testcase.templet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 该类用于向指定的路径下生成用于上传至禅道上的测试用例模版，目前实现已基本模拟从禅道专业版上导出的
 * 模版文件，用户从禅道开源版导出csv的模块数据后，通过类中存储的方法，可实现向模块项与需求项中添加数据有效性。
 * 
 * @author 彭宇琦
 * @version Ver2.4
 * @since POI 3.10
 */
public class ZentaoTemplet {
	// 用于存储用例模版存放路径
	private static StringBuilder savePath = new StringBuilder("");
	// 用于存储Excel文件的名称
	private static StringBuilder fileName = new StringBuilder("");
	// 用于存储模版中存在的字段
	//private static ArrayList<String> field = new ArrayList<>();
	//用于设置是否覆盖原文本
	private static boolean cover = false;
	
	//指向配置文件的位置
	private final static String CONFIG_FILE_PATH = "ConfigurationFiles/CaseConfigurationFile/CaseTemplet.xml";

	/**
	 * 该方法用于以字符串形式返回用例文件保存路径
	 * 
	 * @return 测试用例文件的保存路径
	 */
	public static String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 该方法用于设置测试用例文件保存的位置，可传入相对路径，也可传入绝对路径，
	 * 若传入的路径不符合windows下文件夹名称的命名规则时，则抛出IncorrectDirectoryException异常
	 * 
	 * @param savePath
	 *            传入的测试结果文件保存路径
	 * @throws IncorrectDirectoryException
	 *             传入路径不合法时抛出的异常
	 */
	public static void setSavePath(String savePath) {
		// 将传入的路径封装成StringBuilder，以便格式化
		StringBuilder sb = new StringBuilder(savePath);
		// 格式化传入的路径
		MakeDirectory.formatPath(sb);

		// 判断传入的路径是否符合windows下对文件夹名称命名的规则，如果不符合则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isPath(sb.toString())) {
			throw new IncorrectDirectoryException(
					"不合理的文件夹路径，文件路径：" + sb.toString());
		}

		// 将通过判断的sb赋给savePath属性
		ZentaoTemplet.savePath = sb;
	}

	/**
	 * 以字符串的形式返回测试用例文件的名称
	 * 
	 * @return 测试用例文件的名称
	 */
	public static String getFileName() {
		return fileName.toString();
	}

	/**
	 * 该方法用于设置测试用例文件的文件名称，若传入的文件名不符合windows下文件的命名规则，
	 * 则抛出IncorrectDirectoryException异常
	 * 
	 * @param fileName
	 *            指定的测试结果文件名称
	 * @throws IncorrectDirectoryException
	 *             文件命名不正确时抛出的异常
	 */
	public static void setFileName(String fileName) {
		// 判断传入的测试结果文件名称是否符合windows下的命名规则，若不符合，则抛出IncorrectDirectoryException异常
		if (!MakeDirectory.isFileName(fileName)) {
			throw new IncorrectDirectoryException("不合理的文件名称，文件名称：" + fileName);
		}

		// 通过判断后，则清空fileName存储的信息并将新的文件名称放入fileName种属性中
		ZentaoTemplet.fileName.delete(0, ZentaoTemplet.fileName.length());
		ZentaoTemplet.fileName.append(fileName);
	}

	/**
	 * 该方法用于返回模板文件的文件对象
	 * 
	 * @return 模板文件对象
	 */
	public static File getTempletFile() {
		// 判断模版文件保存路径与模板文件名是否定义，若已定义，则返回模板的文件对象，若未定义，则抛出异常
		if (!ZentaoTemplet.savePath.equals("")
				&& !ZentaoTemplet.fileName.equals("")) {
			return new File(getSavePath() + getFileName() + ".xlsx");
		} else {
			throw new IncorrectDirectoryException("模板保存路径或模板文件名未定义");
		}
	}
	
	/**
	 * 用于设置是否允许覆盖文件，当设置为false时，在创建测试用例模板时会判断文件是否存在（文件同名），若存在，则抛出异常；若设置为true，
	 * 则不做重复文件判断，直接覆盖。在类中，默认为false。
	 * @param cover 是否覆盖文件
	 */
	public static void setCoverFile(boolean cover) {
		ZentaoTemplet.cover = cover;
	}

	/**
	 * 该方法用于向指定的文件路径下写入禅道的测试用例模版
	 */
	public static void create() {
		// 判断测试用例文件保存路径是否定义，若未定义，则给予其默认值
		if (savePath.toString().equals("")) {
			savePath.append("C:\\AutoTest\\Case\\");
		}

		// 测试用例文件名是否定义，若未定义，则给予其
		if (fileName.toString().equals("")) {
			fileName.append("NewCase");
		}

		// 创建文件夹
		File f = new File(savePath.toString());
		if (!f.exists()) {
			f.mkdirs();
		}
		// 拼接存储路径以及文件名称
		f = new File(savePath.toString() + (fileName.toString() + ".xlsx"));

		// 判断文件是否存在，存在则抛出IncorrectDirectoryException异常
		if (f.exists() && !cover) {
			throw new IncorrectDirectoryException(
					"文件名存在：" + fileName.toString() + ".xlsx");
		}

		// 通过判断后向文件中写入模版
		writeTemplet(f);
	}

	/**
	 * 该方法用于读取从禅道出的禅道模版中的模块数据，并将其内容存储在模版中，以添加关于模块的数据有效性
	 * 
	 * @param moduleList
	 *            导出的禅道模版文件名称
	 * @param sheetName
	 *            模版文件中需要读取的工作博名称
	 * @see #readModlueData(File)
	 * @throws IOException
	 */
	public static void readModlueData(File moduleList) throws IOException {
		// 用于读取Excel模版文件的流
		FileInputStream fip;
		// 若抛出文件不存在的异常时，则直接返回
		try {
			fip = new FileInputStream(moduleList);
		} catch (FileNotFoundException e) {
			return;
		}

		// 创建存储模块信息的xml文件的根节点
		Element root = DocumentHelper.createElement("data");
		// 创建xml文件到内存
		Document dom = DocumentHelper.createDocument(root);

		// 指向模版文件
		XSSFWorkbook xw = new XSSFWorkbook(fip);

		// 读取生成的模版
		fip = new FileInputStream(
				new File(getSavePath() + getFileName() + ".xlsx"));
		// 指向生成的测试用例模版
		XSSFWorkbook temxw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 读取第一个工作博
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));
		// 获取“系统数据”工作簿
		XSSFSheet temxs;
		// 判断“系统数据”工作簿是否存在，不存在则创建该工作簿
		if ((temxs = temxw.getSheet("系统数据")) == null) {
			temxs = temxw.createSheet("系统数据");
		}

		// 循环，获取模版中所有的系统数据
		for (int i = 2; i < xs.getLastRowNum() + 1; i++) {
			// 由于模版是从第三行开始编写的数据，故从第三行开始获取
			XSSFRow xr = xs.getRow(i);
			// 存储从单元格中获取到的数据
			String cellValue = xr.getCell(0).toString();

			// 在新的工作簿中写入数据
			XSSFRow temxr;
			// 判断行是否已被创建，若未被创建，则向工作簿中创建行
			if ((temxr = temxs.getRow(i - 2)) == null) {
				temxr = temxs.createRow(i - 2);
			}
			// 写入模块数据
			temxr.createCell(1).setCellValue(cellValue);

			// 将数据按照“/”进行分割，存储得到的数组
			String[] modules = cellValue.split("\\/");

			// 循环，将得到的模块转换为查找元素的xpath路径
			StringBuilder xpath = new StringBuilder("/data");
			for (int j = 1; j < modules.length; j++) {
				// 判断循环是否到最后一次，若循环到最后一次，则进行元素的添加
				if (j == modules.length - 1) {
					// 获取到待添加模块的父节点
					Element e = (Element) dom
							.selectSingleNode(xpath.toString());
					// 链式编程，添加元素标签
					e.addElement("module")
							// 添加元素的name属性，即模块的名称
							.addAttribute("name",
									modules[j].substring(0,
											modules[j].indexOf("(")))
							// 添加元素的id属性，即在禅道中的标识
							.addAttribute("id",
									modules[j].substring(
											modules[j].indexOf("(") + 2,
											modules[j].indexOf(")")));
					// 结束循环
					break;
				}
				// 将路径加上当前获取到的元素
				xpath.append("/" + "module[@name='" + modules[j] + "']");
			}
		}

		// 使用dom4j的漂亮格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置xml文件的编码方式
		format.setEncoding("GBK");
		// 写入xml
		XMLWriter xmlWriter = new XMLWriter(
				new FileOutputStream(savePath.toString() + ("ModuleData.xml")),
				format);

		xmlWriter.write(dom);
		xmlWriter.close();

		// 定义输出流
		FileOutputStream fop = new FileOutputStream(
				new File(getSavePath() + getFileName() + ".xlsx"));
		// 将temxw的内容写入到模版中
		temxw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
		temxw.close();
	}

	/**
	 * 该方法用于读取从禅道导出的需求文件，并将其内容存储在模版中，以添加关于需求的数据有效性
	 * 
	 * @param storyList
	 *            需求文件
	 * @throws IOException
	 */
	public static void readStoryData(File storyList) throws IOException {
		// 用于读取Excel模版文件的流
		FileInputStream fip;
		// 若抛出文件不存在的异常时，则直接返回
		try {
			fip = new FileInputStream(storyList);
		} catch (FileNotFoundException e) {
			return;
		}

		// 创建存储模块信息的xml文件的根节点
		Element root = DocumentHelper.createElement("data");
		// 创建xml文件到内存
		Document dom = DocumentHelper.createDocument(root);

		// 指向模版文件
		XSSFWorkbook xw = new XSSFWorkbook(fip);

		// 读取生成的模版
		fip = new FileInputStream(
				new File(getSavePath() + getFileName() + ".xlsx"));
		// 指向生成的测试用例模版
		XSSFWorkbook temxw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 读取第一个工作博
		// XSSFSheet xs = xw.getSheet(sheetName);
		//XSSFSheet xs = xw.getSheetAt(0);
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));
		// 获取“系统数据”工作簿
		XSSFSheet temxs;
		// 判断“系统数据”工作簿是否存在，不存在则创建该工作簿
		if ((temxs = temxw.getSheet("系统数据")) == null) {
			temxs = temxw.createSheet("系统数据");
		}

		// 循环，读取需求文件中存储的数据
		for (int i = 1; i < xs.getLastRowNum() + 1; i++) {
			// 由于模版是从第二行开始编写的数据，故从第二行开始获取
			XSSFRow xr = xs.getRow(i);
			// 存储从单元格中获取到的需求名称数据
			StringBuilder name = new StringBuilder(
					xr.getCell(5).getStringCellValue());
			// 获取编号数据
			int id = (int) xr.getCell(0).getNumericCellValue();

			// 将数据添加到xml中
			root.addElement("story").addText(name.toString()).addAttribute("id",
					String.valueOf(id));

			// 拼接字符串，使其能存储在模版文件中
			name.append("(#").append(id).append(")");
			// 在新的工作簿中写入数据
			XSSFRow temxr;
			// 判断行是否已被创建，若未被创建，则向工作簿中创建行
			if ((temxr = temxs.getRow(i - 1)) == null) {
				temxr = temxs.createRow(i - 1);
			}
			// 创建单元格，用于录入数据
			temxr.createCell(0).setCellValue(name.toString());
		}

		// 使用dom4j的漂亮格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置xml文件的编码方式
		format.setEncoding("GBK");
		// 写入xml
		XMLWriter xmlWriter = new XMLWriter(
				new FileOutputStream(savePath.toString() + ("StoryData.xml")),
				format);

		xmlWriter.write(dom);
		xmlWriter.close();

		// 定义输出流
		FileOutputStream fop = new FileOutputStream(
				new File(getSavePath() + getFileName() + ".xlsx"));
		// 将temxw的内容写入到模版中
		temxw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
		temxw.close();
	}

	/**
	 * 该方法用于向模版中添加模块的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setModuleDataValidation() throws IOException {
		int id = ZentaoExcel.CASE_COLUMN_MODULE.getValue();
		// 用于读取文件
		FileInputStream fip = new FileInputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 创建XSSFWorkbook对象，用于向Excel文件中写入模版
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 创建指向工作簿的对象，并命名为“用例”
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));
		// 加载下拉列表内容，由于Excel对序列有字数的限制，无法添加大量的数据进入序列，所以需要使用以下的方法实现
		// XSSFDataValidationConstraint constraint = new
		// XSSFDataValidationConstraint(
		// new String[]{"=$A$1:$A$12"});

		// 计算存储模块的列的行数
		XSSFSheet dataxs = xw.getSheet("系统数据");
		int row = dataxs.getLastRowNum();
		// 循环，判断该行是否能获取到数据，如果能，则证明需求从该行获取结束
		while (dataxs.getRow(row).getCell(id) == null) {
			row--;
		}

		// 创建公式约束
		DataValidationConstraint constraint = new XSSFDataValidationHelper(xs)
				.createFormulaListConstraint(
						"=系统数据!$B$1:$B$" + String.valueOf(row + 1));

		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(1,
				xs.getLastRowNum(), id, id);

		// 数据有效性对象
		DataValidation d = new XSSFDataValidationHelper(xs)
				.createValidation(constraint, regions);
		xs.addValidationData(d);

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
	}

	/**
	 * 该方法用于向模版中添加需求的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setStoryDataValidation() throws IOException {
		int id = ZentaoExcel.CASE_COLUMN_STORY.getValue();
		// 用于读取文件
		FileInputStream fip = new FileInputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 创建XSSFWorkbook对象，用于向Excel文件中写入模版
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 创建指向工作簿的对象，并命名为“用例”
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));

		// 计算存储需求的列的行数
		XSSFSheet dataxs = xw.getSheet("系统数据");
		int row = dataxs.getLastRowNum();
		// 循环，判断该行是否能获取到数据，如果能，则证明需求从该行获取结束
		while (dataxs.getRow(row).getCell(id) == null) {
			row--;
		}

		// 创建公式约束
		DataValidationConstraint constraint = new XSSFDataValidationHelper(xs)
				.createFormulaListConstraint(
						"=系统数据!$A$1:$A$" + String.valueOf(row + 1));

		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(1,
				xs.getLastRowNum(), id, id);

		// 数据有效性对象
		DataValidation d = new XSSFDataValidationHelper(xs)
				.createValidation(constraint, regions);
		xs.addValidationData(d);

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
	}

	/**
	 * 该方法用于添加优先级的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setRankDataValidation() throws IOException {
		int id = ZentaoExcel.CASE_COLUMN_RANK.getValue();
		// 用于读取文件
		FileInputStream fip = new FileInputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 创建XSSFWorkbook对象，用于向Excel文件中写入模版
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 创建指向工作簿的对象，并命名为“用例”
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));
		// 创建优先级的数据有效性
		XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(
				new String[] { "3", "1", "2", "4" });
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(1,
				xs.getLastRowNum(), id, id);
		// 数据有效性对象
		DataValidation d = new XSSFDataValidationHelper(xs)
				.createValidation(constraint, regions);
		// 添加数据有效性
		xs.addValidationData(d);

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
	}

	/**
	 * 该方法用于添加用例状态的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setStateDataValidation() throws IOException {
		int id = ZentaoExcel.CASE_COLUMN_STATE.getValue();
		// 用于读取文件
		FileInputStream fip = new FileInputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 创建XSSFWorkbook对象，用于向Excel文件中写入模版
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 创建指向工作簿的对象，并命名为“用例”
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));

		// 添加用例状态的数据有效性
		XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(
				new String[] { "待评审", "正常", "被阻塞", "研究中" });
		CellRangeAddressList regions = new CellRangeAddressList(1,
				xs.getLastRowNum(), id, id);
		DataValidation d = new XSSFDataValidationHelper(xs)
				.createValidation(constraint, regions);
		xs.addValidationData(d);

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
	}

	/**
	 * 该方法用于添加适用阶段的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setStageDataValidation() throws IOException {
		int id = ZentaoExcel.CASE_COLUMN_STAGE.getValue();
		// 用于读取文件
		FileInputStream fip = new FileInputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 创建XSSFWorkbook对象，用于向Excel文件中写入模版
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 创建指向工作簿的对象，并命名为“用例”
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));

		// 添加用例状态的数据有效性
		XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(
				new String[] { "单元测试阶段", "功能测试阶段", "集成测试阶段", "系统测试阶段", "冒烟测试阶段",
						"版本验证阶段" });
		CellRangeAddressList regions = new CellRangeAddressList(1,
				xs.getLastRowNum(), id, id);
		DataValidation d = new XSSFDataValidationHelper(xs)
				.createValidation(constraint, regions);
		xs.addValidationData(d);

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
	}

	/**
	 * 该方法用于添加用例类型的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setTypeDataValidation() throws IOException {
		int id = ZentaoExcel.CASE_COLUMN_TYPE.getValue();
		// 用于读取文件
		FileInputStream fip = new FileInputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 创建XSSFWorkbook对象，用于向Excel文件中写入模版
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 创建指向工作簿的对象，并命名为“用例”
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));

		// 添加用例状态的数据有效性
		XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(
				new String[] { "功能测试", "性能测试", "配置相关", "安装部署", "安全相关", "接口测试",
						"其他" });
		CellRangeAddressList regions = new CellRangeAddressList(1,
				xs.getLastRowNum(), id, id);
		DataValidation d = new XSSFDataValidationHelper(xs)
				.createValidation(constraint, regions);
		xs.addValidationData(d);

		// 向excel中写入数据
		FileOutputStream fop = new FileOutputStream(
				getSavePath() + getFileName() + ".xlsx");
		// 写入模版
		xw.write(fop);
		// 关闭流
		fop.close();

		// 关闭表格文件
		xw.close();
	}

	/**
	 * 该方法用于设置所有类型的数据有效性
	 * 
	 * @throws IOException
	 */
	public static void setAllDataValidation() throws IOException {
		// 添加模块信息的数据有效性，若抛出异常，则说明未定义模块信息的数据有效性，则不添加
		try {
			setModuleDataValidation();
		} catch (NullPointerException e) {
		}
		// 添加需求信息的数据有效性，若抛出异常，则说明未定义模块信息的数据有效性，则不添加
		try {
			setStoryDataValidation();
		} catch (NullPointerException e) {
		}
		// 添加优先级的数据有效性
		setRankDataValidation();
		// 添加用例状态的数据有效性
		setStateDataValidation();
		// 添加适用阶段的数据
		setStageDataValidation();
		// 添加用例类型的数据有效性
		setTypeDataValidation();
	}

	/**
	 * 该方法用于对测试用例文件进行处理，以便于上传至禅道
	 * 
	 * @param caseFile
	 *            测试用例文件
	 * @throws IOException
	 */
	public static void disposeCaseFile(File caseFile) throws IOException {
		// 定义输入流，用于读取模版文件
		FileInputStream fip = new FileInputStream(caseFile);
		// 通过输入流，使XSSFWorkbook对象指向模版文件
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();
		// 获取名为用例的工作簿
		XSSFSheet xs = xw.getSheet(ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));

		// 循环，重头遍历所有的行，
		for (int i = 1; i < xs.getLastRowNum() + 1; i++) {
			// 读取测试步骤一列的数据
			XSSFCell xcs = xs.getRow(i).getCell(ZentaoExcel.CASE_COLUMN_STEP.getValue());

			// 存储获取到的步骤文字
			StringBuilder sbs = new StringBuilder(xcs.getStringCellValue());

			// 对文本内容拆分后，判断其数组是否只包含一个数据，若只包含一个数据，则添加序号“2.”
			if (sbs.toString().split("\\n").length == 1
					&& !sbs.toString().equals("")) {
				// 确认步骤只存在一条时，再读取预期信息，以加快获取速度
				XSSFCell xce = xs.getRow(i).getCell(ZentaoExcel.CASE_COLUMN_EXPECT.getValue());
				StringBuilder sbe = new StringBuilder(xce.getStringCellValue());

				sbs.append("\r\n2.");
				sbe.append("\r\n2.");

				// 重写表格中的内容
				xcs.setCellValue(sbs.toString());
				xce.setCellValue(sbe.toString());
			}
		}

		FileOutputStream fop = new FileOutputStream(caseFile);
		// 写入excel文件
		xw.write(fop);
		// 关闭流
		fop.close();
		xw.close();
	}

	/**
	 * 该方法用于向模版中写入数据
	 * 
	 * @param f
	 */
	private static void writeTemplet(File f) {
		// 创建excel文件
		XSSFWorkbook xw = new XSSFWorkbook();

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
						(short) (Double.valueOf(((Element) (columnList.get(j))).attributeValue("wide")) * 256));

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
	 * 用于写入单元格信息
	 */
	/*
	private XSSFCell writeCell(XSSFRow xr, Excel value, String context) {
		XSSFCell xc = xr.createCell(Integer.valueOf(value.getValue()));
		// 创建并写入操作系统信息
		xc.setCellValue(context);
		xc.setCellStyle(textStyle(xr.getSheet().getWorkbook(), value.getElement().attributeValue("align")));

		return xc;
	}
	*/
	
	/**
	 * 创建文本样式，其按照传入的参数进行选定
	 */
	/*
	private XSSFCellStyle textStyle(XSSFWorkbook xw, String align) {
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
	*/

	/**
	 * 该方法用于设置项目的样式
	 */
	/*
	private static XSSFCellStyle style(XSSFWorkbook xw) {
		XSSFCellStyle xs = xw.createCellStyle();
		// 设置内容的水平居中对齐:HorizontalAlignment.CENTER
		// 设置内容的水平左对齐:HorizontalAlignment.LEFT
		// 设置内容的水平右对齐:HorizontalAlignment.RIGHT
		xs.setAlignment(HorizontalAlignment.CENTER);
		// 设置内容的垂直居中：VerticalAlignment.CENTER
		// 设置内容的底端对齐：VerticalAlignment.BOTTOM
		// 设置内容的顶端对齐：VerticalAlignment.TOP
		xs.setVerticalAlignment(VerticalAlignment.CENTER);

		// 设置单元格自动换行
		xs.setWrapText(true);
		// 设置字体的样式
		xs.setFont(font(xw));
		return xs;
	}
	*/

	/**
	 * 该方法用于设置字体
	 */
	/*
	private static XSSFFont font(XSSFWorkbook xw) {
		XSSFFont xf = xw.createFont();
		// 设置字体名称
		xf.setFontName("宋体");
		// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
		xf.setFontHeightInPoints((short) 12);
		// 设置字体加粗
		xf.setBold(true);
		return xf;
	}
	*/
}
