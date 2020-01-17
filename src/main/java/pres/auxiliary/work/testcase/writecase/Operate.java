package pres.auxiliary.work.testcase.writecase;

import java.io.File;
import java.io.IOException;

public class Operate extends Case {
	/**
	 * 在创建了模版后可调用该构造方法，该构造会检测模版类中存储的模板文件保存路径及文件名，若模板文件保存路径及文件名的其中一项为空，
	 * 则抛出UndefinedDirectoryException异常，此时请使用带参构造
	 */
	public Operate() {
		super();
	}

	/**
	 * 用于指定模板文件对象
	 * 
	 * @param excel
	 *            模板文件对象
	 * @throws IOException
	 */
	public Operate(File excel) throws IOException {
		super(excel);
	}

	/**
	 * 该方法用于生成编辑信息的测试用例
	 * @param name
	 */
	public void addEditCase(String name) {
		
	}
	
	/**
	 * 用于生成预览信息的测试用例
	 */
	public void addPreviewCase() {
		
	}
}
