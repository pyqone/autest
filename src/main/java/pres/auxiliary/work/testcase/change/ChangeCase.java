package pres.auxiliary.work.testcase.change;

import java.io.File;

import pres.auxiliary.work.testcase.exception.IncorrectCaseFileException;

public abstract class ChangeCase {
	//用于指向测试用例文件
	private static File excel;
	//用于存储指定修改测试用例文件中的行数
	private static int rowNum;
	
	/**
	 * 指定测试用例文件对象
	 * @param excel 测试用例文件对象
	 * @param rowNum 文件中需要修改的测试用例位置（所在表格的实际行数）
	 */
	public ChangeCase(File excel, int rowNum) {
		//判断文件对象是否定义
		if ( excel == null ) {
			throw new IncorrectCaseFileException("未定义测试用例文件");
		}
		
		//判断传入行数是否有意义
		if ( rowNum < 1 ) {
			throw new IncorrectCaseFileException("指定的行数无意义");
		}
		
		ChangeCase.excel = excel;
		//传入的是实际行数，所以要减去1
		ChangeCase.rowNum = rowNum - 1;
	}
	
	/**
	 * 该方法用于编辑标题
	 * @return Title对象
	 */
	public Title changeTitle() {
		return Title.newInstence(excel, rowNum, true);
	}
}
