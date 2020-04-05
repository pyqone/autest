package pres.auxiliary.work.old.testcase.change;

import java.io.File;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pres.auxiliary.work.old.testcase.exception.ExistentContentException;

/**
 * 该类用于编写测试用例的标题
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class Title extends CaseAttribute implements OperationContent {
	private static Title t = null;

	/**
	 * 该构造用于在创建一条新的测试用例时使用的构造，并定义是否允许覆盖
	 * 
	 * @param excel
	 *            测试用例文件的文件对象
	 * @param isReplace
	 *            是否允许覆盖表格内容
	 */
	private Title(File excel, boolean isReplace) {
		setExcel(excel);
		CaseAttribute.isReplace = isReplace;
	}

	/**
	 * 该构造用于指向测试用例文件，并指定需要修改或添加的测试用例在文件中的实际位置
	 * 
	 * @param excel
	 *            测试用例文件的
	 * @param rowNum
	 *            文件中需要修改的测试用例位置
	 * @param isReplace
	 *            是否允许覆盖表格内容
	 */
	private Title(File excel, int rowNum, boolean isReplace) {
		setExcel(excel);
		CaseAttribute.rowNum = rowNum;
		CaseAttribute.isReplace = isReplace;
	}

	/**
	 * 该方法用于构造Title对象
	 * 
	 * @param excel
	 *            测试用例文件的文件对象
	 * @param isReplace
	 *            是否允许覆盖表格内容
	 * @return Title对象
	 */
	public static Title newInstence(File excel, boolean isReplace) {
		if (excel.equals(CaseAttribute.excel)) {
			t = new Title(excel, isReplace);
		}

		// 定位到标题那一列
		cellNum = 2;

		return t;
	}

	/**
	 * 该方法用于构造Title对象
	 * 
	 * @param excel
	 *            测试用例文件的
	 * @param rowNum
	 *            文件中需要修改的测试用例位置
	 * @param isReplace
	 *            是否允许覆盖表格内容
	 * @return Title对象
	 */
	public static Title newInstence(File excel, int rowNum, boolean isReplace) {
		if (excel != CaseAttribute.excel) {
			t = new Title(excel, rowNum, isReplace);
		}

		// 定位到标题那一列
		cellNum = 2;

		return t;
	}

	@Override
	public String getPreviousContent() {
		return content.toString();
	}

	@Override
	public Title write(String content) throws IOException {
		// 定位到测试用例所在的工作簿位置
		XSSFWorkbook xw = before();
		// 判断rowNum是是否存储过数字，未存储过数字时，则获取工作簿的最后一行加上2（表示实际位置）
		if (rowNum <= 0) {
			rowNum = xw.getSheetAt(0).getLastRowNum() + 2;
		}
		XSSFRow xr = xw.getSheetAt(0).getRow(rowNum - 1);

		// 判断单元格是否被创建，若被创建则进一步判断是否允许覆盖，若不允许覆盖，则抛出异常
		if (xr.getCell(cellNum) != null && !isReplace) {
			throw new ExistentContentException("单元格已被创建，且不能覆盖已有的内容");
		}

		// 保存单元格中原有的内容
		OperationContent.content.delete(0, OperationContent.content.length());
		OperationContent.content.append(valueOf(xr.getCell(cellNum)));

		// 写入需要添加的内容
		xr.getCell(cellNum).setCellValue(content);
		after(xw);

		return this;
	}

	@Override
	public Title clear() throws IOException {
		// 定位到测试用例所在的工作簿位置
		XSSFWorkbook xw = before();
		// 判断rowNum是是否存储过数字，未存储过数字时，则获取工作簿的最后一行加上2（表示实际位置）
		if (rowNum <= 0) {
			rowNum = xw.getSheetAt(0).getLastRowNum() + 2;
		}
		XSSFRow xr = xw.getSheetAt(0).getRow(rowNum - 1);

		// 判断单元格是否被创建，若被创建则直接结束，若已被创建，则设置单元格内容为空
		if (xr.getCell(cellNum) != null) {
			// 保存单元格中原有的内容
			OperationContent.content.delete(0, OperationContent.content.length());
			OperationContent.content.append(valueOf(xr.getCell(cellNum)));
			//替换单元格中的内容为空
			xr.getCell(cellNum).setCellValue("");
		}

		// 写入需要添加的内容
		after(xw);

		return this;
	}

	@Override
	public Title replace(String findText, String replaceText)
			throws IOException {

		return this;
	}

	@Override
	public Title delete(String findText) throws IOException {
		// TODO Auto-generated method stub
		return this;
	}
}
