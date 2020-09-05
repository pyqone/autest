package pres.auxiliary.tool.file.excel;

/**
 * <p><b>文件名：</b>ReplactFunction.java</p>
 * <p><b>用途：</b>
 * 用于根据替换词语，设置相应的替换方法，并在读取到该词语时执行设置方法的接口
 * </p>
 * <p><b>编码时间：</b>2020年9月3日上午8:24:58</p>
 * <p><b>修改时间：</b>2020年9月3日上午8:24:58</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public interface ReplactFunction {
	/**
	 * 根据替换的词语，以及在该位置上的原始内容，对词语所在位置内容进行替换
	 * @param replactWord 需要替换的词语
	 * @return 被替换的内容
	 */
	public String replact(String replactWord);
}
