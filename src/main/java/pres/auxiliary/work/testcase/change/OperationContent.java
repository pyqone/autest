package pres.auxiliary.work.testcase.change;

import java.io.IOException;

/**
 * 该接口定义对单步内容进行的操作
 * @author 彭宇琦
 */
interface OperationContent {
	/**
	 * 用于存储单元格中的内容
	 */
	StringBuilder content = new StringBuilder("");
	
	/**
	 * 该方法用于返回单元格被修改前的内容
	 * @return 单元格被修改前的内容
	 */
	public String getPreviousContent();
	
	/**
	 * 该方法用于将需要设置的内容写入到单元格中，该方法检查是否允许覆盖原有内容
	 * @param content 需要在单元格中填写的内容
	 * @throws IOException 
	 */
	public OperationContent write(String content) throws IOException;
	
	/**
	 * 该方法用于清空单元格中的内容，该方法不检查是否允许覆盖原有内容
	 * @return
	 * @throws IOException
	 */
	public OperationContent clear() throws IOException;
	
	/**
	 * 该方法用于替换文本中的内容，该方法不检查是否允许覆盖原有内容
	 * @param findText 需要查找的内容
	 * @param replaceText 待替换的内容
	 * @return
	 * @throws IOException
	 */
	public OperationContent replace(String findText, String replaceText) throws IOException;
	
	/**
	 * 该方法用于删除指定的内容，该方法不检查是否允许覆盖原有内容
	 * @param findText 需要被删除的内容
	 * @return
	 * @throws IOException
	 */
	public OperationContent delete(String findText) throws IOException;
}
