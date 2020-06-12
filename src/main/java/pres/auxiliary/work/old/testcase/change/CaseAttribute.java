package pres.auxiliary.work.old.testcase.change;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pres.auxiliary.work.old.testcase.exception.IncorrectCaseFileException;

public abstract class CaseAttribute {
	// 用于指向测试用例文件
	protected static File excel;
	// 用于设置是否允许覆盖已有的文字，默认为不允许
	protected static boolean isReplace = false;
	//用于指向测试用例文件中需要添加或修改的测试用例的实际位置
	protected static int rowNum = 0;
	//用于存储单元格所在的列数
	protected static int cellNum = 0;

	/**
	 * 该方法用于设置需要添加或修改的测试用例在文件中的实际位置
	 * @param rowNum 需要添加或修改的测试用例在文件中的实际位置
	 */
	public void setRowNum(int rowNum) {
		CaseAttribute.rowNum = rowNum;
	}

	/**
	 * 用于设置是否允许覆盖已有的文字，true时则表示可以覆盖，false表示不可以覆盖
	 */
	public void isReplace(boolean isReplace) {
		CaseAttribute.isReplace = isReplace;
	}

	/**
	 * 用于指向测试用例文件
	 * @param excelFile 测试用例文件的文件对象
	 */
	public void setExcel(File excel) {
		if ( excel == null ) {
			throw new IncorrectCaseFileException("未定义测试用例文件");
		}
		
		CaseAttribute.excel = excel;
	}
	
	/**
	 * 该方法用于创建一个XSSFWorkbook对象
	 * 
	 * @return
	 * @throws IOException
	 */
	protected XSSFWorkbook before() throws IOException {
		FileInputStream fip = new FileInputStream(excel);
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		fip.close();
		
		return xw;
	}

	/**
	 * 该方法用于将修改后的表格写入测试用例文件中
	 * 
	 * @param xw
	 * @throws IOException
	 */
	protected void after(XSSFWorkbook xw) throws IOException {
		FileOutputStream fop = new FileOutputStream(excel);
		xw.write(fop);
		fop.close();
	}
	
	/**
	 * 该方法用于读取并通过字符串的形式返回单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	protected String valueOf(XSSFCell cell) {
		String s;
		
		if (cell.getCellTypeEnum().equals(CellType.STRING) ) {
			s = cell.getStringCellValue();
		}

		else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
			if (DateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = DateUtil.getJavaDate(d);
				s = new SimpleDateFormat("yyyy-MM-dd").format(date);
			} else {
				s = String.valueOf((int) cell.getNumericCellValue());
			}
		} else if (cell.getCellTypeEnum().equals(CellType.BLANK)) {
			s = "";
		} else if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
			s = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellTypeEnum().equals(CellType.ERROR)) {
			s = "";
		} else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
			s = cell.getCellFormula().toString();
		} else {
			s = null;
		}

		return s;
	}

	//TODO 模板问题暂缓，计划直接写将模版存储在XML中，并根据不同的类传值写入的文件不同
	/**
	 * 该方法用于返回存储在类的中的模版列表
	 * 
	 * @return 存储在类的中的模版列表
	 */
	/*
	public ArrayList<StringBuilder> getTemplates() {
		return templates;
	}
	*/

	/**
	 * 该方法用于向类中写入模版
	 * 
	 * @param templates
	 *            需要写入的模版
	 * @see #setTemplate(List)
	 */
	/*
	public void setTemplate(String... templates) {
		for (String template : templates) {
			this.templates.add(new StringBuilder(template));
		}
	}
	*/

	/**
	 * 该方法用于向类中写入模版
	 * 
	 * @param templates
	 *            需要写入的模版
	 * @see #setTemplate(String...)
	 */
	/*
	public void setTemplate(List<StringBuilder> templates) {
		this.templates.addAll(templates);
	}
	*/

	/**
	 *  该方法用于将模版存储在xml文件中
	 */
	/*
	public void templateToXml() {
		
	}
	*/
}
