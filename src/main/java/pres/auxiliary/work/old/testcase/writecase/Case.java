package pres.auxiliary.work.old.testcase.writecase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.work.old.testcase.change.CaseTab;
import pres.auxiliary.work.old.testcase.change.Tab;
import pres.auxiliary.work.old.testcase.templet.ZentaoExcel;
import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;

/**
 * 该类定义了所有预设用例都包含的基本信息<br/>
 * <b><i>NOTE:若文件中存在手动添加的标注时，应将标注标在用例标题那一列上，以避免在插入表格时会被程序删除掉</i></b>
 * 
 * @author 彭宇琦
 * @version Ver2.1
 * @since POI3.17
 */
public abstract class Case {
	// 用于存储模版文件存储的位置
	private static StringBuilder savePath = new StringBuilder(
			ZentaoTemplet.getSavePath());
	// 用于存储模版文件存储的位置
	private static StringBuilder fileName = new StringBuilder(
			ZentaoTemplet.getFileName() + ".xlsx");
	// 用于存储所属模块信息
	private static StringBuilder module = new StringBuilder();
	// 用于存储当前测试用例的位置（永远指向被新插入的测试用例的位置）
	private static int insertRowNum = 0;
	
	//TODO 续写会有序号不正确的问题需要解决
	//用于记录模块序号
	private static int moduleId = 0;
	//用于记录用例序号
	private static int caseId = 0;
	
	//用于配置模块及用例条数的最大值位数，例如，编号最大到Test_99_99，则其位数都设置为2；编号最大到Test_999_99，则其模块位数都设置为3，用例位数为2
	private static int ModuleNum = 2;
	private static int CaseNum = 2;
	
	//用于存储模块信息
	private HashMap<String, Integer> moduleMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> caseMap = new HashMap<String, Integer>();
	
	//存储测试用例模板的文件夹位置
	private final String CASE_TEMPLET_FOLDER = "ConfigurationFiles/CaseConfigurationFile/CaseTemplet/";
	//存储读取xml文件的类对象
	Document dom;
	
	//用于存储需要编写的变量信息
	HashMap<String, String> textMap = new HashMap<String, String>();

	/**
	 * 在创建了模版后可调用该构造方法，该构造会检测模版类中存储的模板文件保存路径及文件名，若模板文件保存路径及文件名的其中一项为空，
	 * 则抛出UndefinedDirectoryException异常，此时请使用带参构造
	 */
	public Case() {
		if (getSavePath().equals("") || getFileName().equals("")) {
			throw new UndefinedDirectoryException("未指定excel文件存储路径");
		}
		
		if ( !this.getClass().getSimpleName().equals("PresetCase") && !this.getClass().getSimpleName().equals("MyCase") ) {
			try {
				dom = new SAXReader().read(new File(CASE_TEMPLET_FOLDER + this.getClass().getSimpleName() + ".xml"));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 用于指定模板文件对象
	 * 
	 * @param excel
	 *            模板文件对象
	 * @throws IOException
	 */
	public Case(File excel) throws IOException {
		setExcelFile(excel);
		if ( !this.getClass().getSimpleName().equals("PresetCase") && !this.getClass().getSimpleName().equals("MyCase") ) {
			try {
				dom = new SAXReader().read(new File(CASE_TEMPLET_FOLDER + this.getClass().getSimpleName() + ".xml"));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 该方法用于返回模板文件名
	 * 
	 * @return 文件名
	 */
	public String getFileName() {
		return fileName.toString();
	}

	/**
	 * 该方法用于返回模版存储路径
	 * 
	 * @return 模版存储的路径
	 */
	public String getSavePath() {
		return savePath.toString();
	}

	/**
	 * 该方法用于指定模板文件
	 * 
	 * @param excelFile
	 *            模版文件对象
	 * @throws IOException
	 */
	public void setExcelFile(File excelFile) throws IOException {
		// 读取文件名，存入fileName中
		Case.fileName.delete(0, Case.fileName.length());
		Case.fileName.append(excelFile.getName());

		// 读取存储路径，存入savePath中
		Case.savePath.delete(0, Case.savePath.length());
		Case.savePath.append(excelFile.getParent() + "\\");
	}

	/**
	 * 该方法用于返回测试用例文件的文件对象
	 * 
	 * @return 测试用例文件的文件对象
	 */
	public File getTempletFile() {
		return new File(savePath.toString() + fileName.toString());
	}

	/**
	 * 该方法用于返回存储的所属模块信息
	 * 
	 * @return 存储的所属模块信息
	 */
	public String getModule() {
		return module.toString();
	}

	/**
	 * 该方法用于设置所属模块信息
	 * 
	 * @param module
	 *            待存储的所属模块信息
	 */
	public void setModule(String module) {
		if (Case.module.toString().equals(module)) {
			return;
		}
		
		Case.module.delete(0, Case.module.length());
		Case.module.append(module);
		
		//若模块信息不存在时，则初始化序号
		if ( !moduleMap.containsKey(module) ) {
			moduleId++;
			caseId = 0;
			moduleMap.put(module, moduleId);
			caseMap.put(module, caseId);
		} else {
			moduleId = moduleMap.get(module);
			caseId = caseMap.get(module);
		}

		// 读取xml中的模块数据
		try {
			// 读取模版的数据
			Document dom = new SAXReader()
					.read(new File(savePath.toString() + "ModuleData.xml"));
			// 解析传入的模块信息
			String[] s = module.split("\\/");
			// 定义xpath
			String xpath = "/data";
			// 循环，将模块信息解析成xpath
			for (int i = 1; i < s.length; i++) {
				xpath += ("/module[@name='" + s[i] + "']");
			}

			// 在xml中查找模块信息
			Element e = (Element) dom.selectSingleNode(xpath);
			// 如果查找到该元素，则读取其元素的id属性，向模块中继续添加该模块在禅道中的信息，否则将不做更改
			if (e != null) {
				// 添加在上传禅道时需要填写格式
				Case.module.append("(#");
				// 读取元素的id属性，并该属性的值写入模块信息中
				Case.module.append(e.attributeValue("id"));
				Case.module.append(")");
			} else {
				return;
			}
			// 如果未读到数据，则会抛出DocumentException异常，此时则不更改添加的模块信息
		} catch (DocumentException e) {
			return;
		}
	}
	
	/**
	 * 用于返回模块序号的长度位数，例如位数为2，则表示模块序号为两位数，序号不足两位数的，则前方补0（如，两位数，序号为12时，显示为“12”，序号为9时，显示为“09”）
	 * @return 返回模块序号位数
	 */
	public static int getModuleNum() {
		return ModuleNum;
	}

	/**
	 * 用于设置模块序号的长度位数，例如位数为2，则表示模块序号为两位数，序号不足两位数的，则前方补0（如，两位数，序号为12时，显示为“12”，序号为9时，显示为“09”）
	 * @param moduleNum 需要设置的模块序号位数
	 */
	public static void setModuleNum(int moduleNum) {
		ModuleNum = moduleNum;
	}

	/**
	 * 用于返回用例序号的长度位数，例如位数为2，则表示用例序号为两位数，序号不足两位数的，则前方补0（如，两位数，序号为12时，显示为“12”，序号为9时，显示为“09”）
	 * @return 返回用例序号位数
	 */
	public static int getCaseNum() {
		return CaseNum;
	}

	/**
	 * 用于设置用例序号的长度位数，例如位数为2，则表示用例序号为两位数，序号不足两位数的，则前方补0（如，两位数，序号为12时，显示为“12”，序号为9时，显示为“09”）
	 * @param caseNum 需要设置的用例序号位数
	 */
	public static void setCaseNum(int caseNum) {
		CaseNum = caseNum;
	}
	
	/**
	 * 该方法用于向测试用例文件中插入一条空的测试用例（等价于预留一个位置）
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addEmptyCase() throws IOException {
		return after("", new StringBuilder("\r\n"), new StringBuilder("\r\n"),
				"", 1, "");
	}

	/**
	 * 封装各个方法结束前必须使用的代码
	 * 
	 * @param title
	 *            用例标题
	 * @param step
	 *            用例步骤
	 * @param expectation
	 *            用例预期
	 * @param keyword
	 *            用例关键词
	 * @param rank
	 *            优先级
	 * @param precondition
	 *            前置条件
	 * @throws IOException
	 */
	protected Tab after(String title, StringBuilder step,
			StringBuilder expectation, String keyword, int rank,
			String precondition) throws IOException {
		// 若step、expectation中最后一个是空行（即\r\n），则处理其最后一个空行
		if (step.lastIndexOf("\r\n") == step.length() - 2) {
			step.delete(step.lastIndexOf("\r\n"), step.lastIndexOf("\r\n") + 3);
		}
		if (expectation.lastIndexOf("\r\n") == expectation.length() - 2) {
			expectation.delete(expectation.lastIndexOf("\r\n"),
					expectation.lastIndexOf("\r\n") + 3);
		}

		// 定义输入流，用于读取模版文件
		FileInputStream fip = new FileInputStream(
				new File(getSavePath() + getFileName()));
		// 通过输入流，使XSSFWorkbook对象指向模版文件
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();
		// 获取用例的工作簿
		XSSFSheet xs = xw.getSheet(
				ZentaoExcel.SHEET_CASE.getElement().attributeValue("name"));

		// 判断新加入的测试用例其模块是否与文件中最后一行的模块信息一致，若一致，则直接向下方添加一行，若不是，则判断该模块是否存在与文件中
		XSSFRow xr;
		if (getModule().equals(xs.getRow(insertRowNum)
				.getCell(ZentaoExcel.CASE_COLUMN_MODULE.getValue())
				.getStringCellValue())) {
			// 判断当前caseNum指向的行是否为最后一行，若是最后一行，则直接向下增加测试用例，若不是，则调用插入行的方式将测试用例插入表格中
			if (insertRowNum == xs.getLastRowNum()) {
				xr = xs.createRow(++insertRowNum);
			} else {
				// 在插入表格前需要将caseNum增加1，以保证移动单元格时无误
				++insertRowNum;
				xr = insertRow(xs);
			}
		} else {
			// 用于判断模块信息是否能在文件的表格中查找得到
			boolean isHas = false;
			// 循环，判断文件中存储的模块是否包含正在添加的模块信息
			for (int i = 0; i < xs.getLastRowNum(); i++) {
				// 判断当前行查找的模块信息是否与正在添加的测试用例的模块信息相同，若相同，则将isHas变为true，存储当前的行数，并结束循环
				if (xs.getRow(i)
						.getCell(ZentaoExcel.CASE_COLUMN_MODULE.getValue())
						.getStringCellValue().equals(getModule())) {
					insertRowNum = i;
					isHas = true;
					break;
				}
			}

			// 判断isHas中的信息，若为false，则说明整个文档中未查到该模块信息，则直接在文档表格的最后一行添加测试用例
			if (!isHas) {
				insertRowNum = xs.getLastRowNum();
				xr = xs.createRow(++insertRowNum);
			} else {
				// 如果在文件中查到了模块信息，则需要将该测试用例插入到该模块现有的测试用例的最后一条
				// 循环，判断比该模块的最后一行多一行的行数，即正好与需要添加的模块信息不同的那一行
				for (int i = insertRowNum; i < xs.getLastRowNum(); i++) {
					// 判断当前行查找的模块信息是否与正在添加的测试用例的模块信息相同，若相同，则将isHas变为true，存储当前的行数，并结束循环
					if (!xs.getRow(i)
							.getCell(ZentaoExcel.CASE_COLUMN_MODULE.getValue())
							.getStringCellValue().equals(getModule())) {
						insertRowNum = i;
						break;
					}
				}

				// 插入行
				xr = insertRow(xs);
			}
		}
		
		// 处理用例序号信息
		String id = "Test_";
		//判断模块和用例编号的位数是否达到 设置的位数，位达到则补0
		if ( String.valueOf(moduleId).length() < ModuleNum ) {
			for ( int i = 0; i < ModuleNum - String.valueOf(moduleId).length(); i++ ) {
				id += "0";
			}
		}
		id += (moduleId + "_");
		if ( String.valueOf(++caseId).length() < CaseNum ) {
			for ( int i = 0; i < CaseNum - String.valueOf(caseId).length(); i++ ) {
				id += "0";
			}
		}
		id += (caseId);
		//存储用例编号
		caseMap.put(getModule(), caseId);

		//添加序号信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_CASE_ID, id);
		// 添加所属模块信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_MODULE, getModule());
		// 添加用例标题信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_TITLE, title);
		// 添加用例步骤信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_STEP, step.toString());
		// 添加预期信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_EXPECT, expectation.toString());
		// 添加关键词信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_KEYS, keyword);
		// 添加用例类型信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_TYPE, "功能测试");
		// 添加优先级信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_RANK, String.valueOf(rank));
		// 添加用例状态信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_STATE, "待评审");
		// 添加适用阶段信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_STAGE, "功能测试阶段");
		// 添加前置条件信息
		writeCell(xr, ZentaoExcel.CASE_COLUMN_CONDITION, precondition);

		FileOutputStream fop = new FileOutputStream(
				new File(getSavePath() + getFileName()));
		// 写入excel文件
		xw.write(fop);
		// 关闭流
		fop.close();
		xw.close();

		return CaseTab.newInstence(new File(getSavePath() + getFileName()));
	}

	/**
	 * 用于移动并插入单元格
	 */
	private XSSFRow insertRow(XSSFSheet xs) {
		// 为避免移动后会删除用户添加的注解，应添加处理方式，保证移动表格后不会删除手动添加的标注，若需要保证速度，则可以注释掉对标注的处理
		// 调用Map集合来存储行标以及标注对象
		HashMap<Integer, XSSFComment> map = new HashMap<Integer, XSSFComment>();
		// 循环，判断该行的指定列是否包含标注，若包含标注，则将该行的行标及标注对象存入Map数组中
		// 注意，即使未移动的单元格中有标注也会受到影响，故需要从头开始检测
		for (int i = 0; i < xs.getLastRowNum() + 1; i++) {
			if (xs.getRow(i).getCell(ZentaoExcel.CASE_COLUMN_TITLE.getValue()).getCellComment() != null) {
				map.put(i, xs.getRow(i).getCell(2).getCellComment());
			}
		}

		// 移动单元格
		xs.shiftRows(insertRowNum, xs.getLastRowNum(), 1);
		XSSFRow xr = xs.createRow(insertRowNum);

		// 循环，读取集合中的元素，并获取第(key + 1)行的表格，向该行指定的单元格中添加元素
		for (Entry<Integer, XSSFComment> e : map.entrySet()) {
			// 判断标注是否在未被移动的单元格上，若是，则行数不加1，若不是，则行数加1
			if (e.getKey() < insertRowNum) {
				xs.getRow(e.getKey()).getCell(2).setCellComment(e.getValue());
			}
			else {
				xs.getRow(e.getKey() + 1).getCell(2)
						.setCellComment(e.getValue());
			}
		}

		return xr;
	}

	/**
	 * 该方法用于返回当前测试用例被插入到的位置
	 */
	public static int getInsertRowNum() {
		return insertRowNum;
	}
	
	/**
	 * 用于获取并返回从xml文件中读取到的测试用例信息
	 * @param methodName 方法名
	 * @param nodeName 节点名称
	 * @return 模板中读取到的并加以修改后的文本
	 */
	protected String getContent(String methodName, String nodeName) {
		//获取到相应方法的对应数据
		Element e = (Element)(dom.selectSingleNode("/cases/case[@function='" + methodName + "']/" + nodeName));
		
		//若传入的是读取前置条件的标签，则需要循环读取
		if ( nodeName.equals("preconditions") ) {
			if ( e.elements().size() != 0 ) {
				//用于写入步骤
				int step = 0;
				//用于存储读取的内容
				StringBuilder sb = new StringBuilder();
				List<?> childElemenets = e.elements();
				//循环，添加标签中的内容
				for ( Object childElemenet : childElemenets ) {
					sb.append(++step + ".");
					sb.append(translateContent(((Element)(childElemenet)).attributeValue("value")));
					sb.append("\r\n");
				}
				//删除多余的换行
				sb.delete(sb.lastIndexOf("\r\n"), sb.length());
				
				return sb.toString();
			} else {
				return "";
			}
		}
		
		return translateContent(e.attributeValue("value"));
		
	}
	
	/**
	 * 用于获取并返回从xml文件中读取到的测试用例步骤与预期的信息，注意，返回值中包括步骤和预期的信息，其信息使用“*”符号将其隔开
	 * @param methodName 方法名称
	 * @param ids 需要读取的标题id号
	 * @return 步骤及预期信息（使用“*”符号隔开）
	 */
	protected String[] getStep(String methodName, int... ids) {
		StringBuilder st = new StringBuilder();
		StringBuilder ex = new StringBuilder();
		
		//用于存储步骤及预期前的序号
		int step = 0;
		//判断传入的结果中其下标值是否为-1，为-1则读取所有的步骤和预期标签
		if ( ids.length == 1 && ids[0] == -1 ) {
			//获取steps标签下所有的step标签
			int size = dom.selectNodes("/cases/case[@function='" + methodName + "']/steps/step").size();
			ids = new int[size];
			//循环，存储所有的标签的下标
			for ( int i = 1; i < size + 1; i++ ) {
				ids[i - 1] = i;
			}
		}
		//循环，读取所有需要读取的步骤预期标签
		for ( int id : ids ) {
			//获取到相应方法对应步骤数据
			Element e = (Element)(dom.selectSingleNode("/cases/case[@function='" + methodName + "']/steps/step[@id='" + id + "']"));
			st.append(++step + "." + translateContent(e.attributeValue("value")) + "\r\n");
			
			//获取相应方法对应的预期数据，先判断是否存在该ID的预期信息，若存在则读取，若不存在，则跳过
			if ((e = (Element)(dom.selectSingleNode("/cases/case[@function='" + methodName + "']/expectations/expectation[@id='" + id + "']"))) != null){
				ex.append(step + "." + translateContent(e.attributeValue("value")) + "\r\n");
			}
		}
		
		return new String[]{st.toString(), ex.toString()};
	}
	
	/**
	 * 用于将文本中的变量替换为map中存储的信息
	 */
	private String translateContent(String content) {
		StringBuilder sb = new StringBuilder(content);
		//存储替换符的位置
		int index = 0;
		//循环，替换content中所有需要替换的信息
		while( (index = sb.indexOf("*{")) != -1 ) {
			//存储待替换的变量名
			String var = sb.substring(index + "*{".length(), sb.indexOf("}*"));
			//判断map中是否存储了该变量名
			if ( textMap.containsKey(var) ) {
				sb.replace(index, sb.indexOf("}*") + "}*".length(), textMap.get(var));
			} else {
				sb.replace(index, sb.indexOf("}*") + "}*".length(), "{" + var + "}");
			}
		}
		
		return sb.toString();
	}

	/**
	 * 用于写入单元格信息
	 */
	private XSSFCell writeCell(XSSFRow xr, ZentaoExcel value, String context) {
		XSSFCell xc = xr.createCell(Integer.valueOf(value.getValue()));
		// 创建并写入操作系统信息
		xc.setCellValue(context);
		xc.setCellStyle(textStyle(xr.getSheet().getWorkbook(),
				value.getElement().attributeValue("align")));

		return xc;
	}
	
	/**
	 * 创建文本样式，其按照传入的参数进行选定
	 */
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
}
