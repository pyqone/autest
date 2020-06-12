package pres.auxiliary.work.old.testcase.change;

import java.io.File;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 该方法扩充了对测试用例进行标记方法
 * 
 * @author 彭宇琦
 * @version V1.0
 * @since JDK1.7
 * @since POI3.10
 */
public class CaseTab extends Tab {
	// 用于构造对象
	private static CaseTab c = null;

	/**
	 * 私有构造，禁止直接创建类对象
	 * */
	private CaseTab(File caseFile) {
		// TODO CaseTab(File caseFile)
		super(caseFile);
	}

	/**
	 * 该方法用于构造CaseTab对象
	 * 
	 * @param caseFile
	 *            测试用例文件对象
	 * @return CaseTab对象
	 */
	public static CaseTab newInstence(File caseFile) {
		// TODO newInstence(File caseFile)
		// 判断类中的属性c是否已被构造，若未被构造则进行构造对象操作，若已被构造则将传入的文件对象赋给属性中
		if (c == null) {
			c = new CaseTab(caseFile);
		} else {
			Tab.caseFile = caseFile;
		}

		return c;
	}

	/**
	 * 该方法用于向指定行的第三列（即用例标题那一列）中设置一个注释
	 * 
	 * @param rowNum
	 *            行数
	 * @param content
	 *            注释的内容
	 * @return CaseTab对象
	 * @throws IOException
	 */
	public CaseTab setTab(int rowNum, String content) throws IOException {
		// TODO setTab(int rowNum, String content)
		return (CaseTab) setTab(before(), rowNum, 2, content);
	}

	/**
	 * 该方法用于向指定单元格中设置一个注释，需要传入制定行中对应的列名称
	 * 
	 * @param rowNum
	 *            指定的行
	 * @param tableName
	 *            列的名称
	 * @param content
	 *            标注的内容
	 * @return
	 * @throws IOException
	 */
	public CaseTab setTab(int rowNum, String tableName, String content)
			throws IOException {

		// TODO setTab(int rowNum, String tableName, String content)
		int cellNum = 2;
		if (tableName != null) {
			if ("所属模块".equals(tableName)) {
				cellNum = 0;
			} else if ("相关需求".equals(tableName)) {
				cellNum = 1;
			} else if ("用例标题".equals(tableName)) {
				cellNum = 2;
			} else if ("步骤".equals(tableName)) {
				cellNum = 3;
			} else if ("预期".equals(tableName)) {
				cellNum = 4;
			} else if ("关键词".equals(tableName)) {
				cellNum = 5;
			} else if ("用例类型".equals(tableName)) {
				cellNum = 6;
			} else if ("优先级".equals(tableName)) {
				cellNum = 7;
			} else if ("用例状态".equals(tableName)) {
				cellNum = 8;
			} else if ("适用阶段".equals(tableName)) {
				cellNum = 9;
			} else if ("前置条件".equals(tableName)) {
				cellNum = 10;
			}
		}

		return setTab(rowNum, cellNum, content);
	}

	/**
	 * 该方法用于向指定行与指定的列中设置一个注释
	 * 
	 * @param rowNum
	 *            行数
	 * @param cellNum
	 *            列数
	 * @param content
	 *            注释的内容
	 * @return CaseTab对象
	 * @throws IOException
	 */
	public CaseTab setTab(int rowNum, int cellNum, String content)
			throws IOException {
		// TODO setTab(int rowNum, int cellNum, String content)
		return (CaseTab) setTab(before(), rowNum, cellNum, content);
	}

	/**
	 * 该方法用于向指定的单元格中的字体设置颜色标注，需要传入制定行中对应的列名称
	 * 
	 * @param rowNum
	 *            行数
	 * @param tableName
	 *            列名称
	 * @param color
	 *            颜色代码，可在类中选择
	 * @return CaseTab对象
	 * @throws IOException
	 */
	public CaseTab setTableColorTab(int rowNum, String tableName, short color)
			throws IOException {
		//TODO setTableColorTab(int rowNum, String tableName, short color)
		//给表格列数一个默认值，即默认标记指定行的用例标题那一列
		int cellNum = 2;
		if (tableName != null) {
			if ("所属模块".equals(tableName)) {
				cellNum = 0;
			} else if ("相关需求".equals(tableName)) {
				cellNum = 1;
			} else if ("用例标题".equals(tableName)) {
				cellNum = 2;
			} else if ("步骤".equals(tableName)) {
				cellNum = 3;
			} else if ("预期".equals(tableName)) {
				cellNum = 4;
			} else if ("关键词".equals(tableName)) {
				cellNum = 5;
			} else if ("用例类型".equals(tableName)) {
				cellNum = 6;
			} else if ("优先级".equals(tableName)) {
				cellNum = 7;
			} else if ("用例状态".equals(tableName)) {
				cellNum = 8;
			} else if ("适用阶段".equals(tableName)) {
				cellNum = 9;
			} else if ("前置条件".equals(tableName)) {
				cellNum = 10;
			}
		}

		setTableColorTab(rowNum, cellNum, color);

		return c;
	}

	/**
	 * 该方法用于向指定的单元格中的字体设置颜色标注
	 * 
	 * @param rowNum
	 *            行数
	 * @param cellNum
	 *            列数
	 * @param color
	 *            颜色代码，可在类中选择
	 * @return CaseTab对象
	 * @throws IOException
	 */
	public CaseTab setTableColorTab(int rowNum, int cellNum, short color)
			throws IOException {
		//TODO setTableColorTab(int rowNum, int cellNum, short color)
		// 获取工作表
		XSSFWorkbook xw = before();
		XSSFSheet xs = xw.getSheetAt(0);

		// 设置单元格内字体颜色
		setColorTab(xs.getRow(rowNum).getCell(cellNum), color);

		after(xw);

		return c;
	}
}
