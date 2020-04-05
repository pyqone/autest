package pres.auxiliary.work.old.testcase.change;

import java.util.ArrayList;

/**
 * 该接口用于设置有多条内容需要写入时使用的的便捷方法
 * @author 彭宇琦
 * @version Ver1.0
 */
interface OperationContents {
	/**
	 * 用于存储单元格中的多条内容
	 */
	ArrayList<StringBuilder> content = new ArrayList<>();
	
	/**
	 * 该方法用于将需要设置多条的容写入到单元格中，调用该方法用可不需要自行添加序号及换行
	 * @param contents 需要在单元格中填写的多条内容
	 */
	public void write(String... contents);
}
