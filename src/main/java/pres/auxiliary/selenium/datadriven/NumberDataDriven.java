package pres.auxiliary.selenium.datadriven;

public class NumberDataDriven extends DataDriven {
//	/**
//	 * 无参构造，不做任何操作，请使用{@link #setDataFile(File)}方法定义数据驱动文件
//	 */
//	public NumberDataDriven() {
//		super();
//	}
//
//	/**
//	 * 用于指定数据驱动文件
//	 * 
//	 * @param dataFile
//	 *            数据驱动文件对象
//	 */
//	public NumberDataDriven(File dataFile) {
//		super(dataFile);
//	}
//
//	@Override
//	public File createDataFile(String saveFileName, int minSize, int maxSize, int length) throws IOException {
//		//用于存储生成的字符
//		StringBuilder sb = new StringBuilder("");
//		// 循环，向文件中添加随机数字
//		for (int i = 0; i < length; i++) {
//			// 添加随机数字
//			sb.append(new RandomString(StringMode.NUM).toString(minSize, maxSize));
//			sb.append("\r\n");
//		}
//		//删除最后一个换行符
//		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());
//		
//		// 返回数据驱动文件
//		return createDataFile(saveFileName, sb);
//	}
//
//	@Override
//	public File createDataFile(String saveFileName, int size, int length) throws IOException {
//		//用于存储生成的字符
//		StringBuilder sb = new StringBuilder("");
//		// 循环，向文件中添加随机数字
//		for (int i = 0; i < length; i++) {
//			// 添加随机数字
//			sb.append(new RandomString(StringMode.NUM).toString(size));
//			sb.append("\r\n");
//		}
//		//删除最后一个换行符
//		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());
//		
//		// 返回数据驱动文件
//		return createDataFile(saveFileName, sb);
//	}
//	
//	/**
//	 * 该方法用于从数字1开始，每次增加1步长来生成自然顺序的数字，并写入文件中
//	 * 
//	 * @param saveFileName
//	 *            数据驱动文件的文件名，若为空或为null，则默认为NewDataDriven（文件的后缀为.txt）
//	 * @param length
//	 *            数据生成个数
//	 * @return 生成的数据驱动文件对象
//	 * @throws IOException
//	 * 
//	 * @see #createDataFile(String, int, int)
//	 * @see #createDataFile(String, int, int, int)
//	 */
//	public File createContinuesNumberDataFile(String saveFileName, int length) throws IOException {
//		//用于存储生成的字符
//		StringBuilder sb = new StringBuilder("");
//		// 循环，向文件中添加随机数字
//		for (int i = 1; i < length + 1; i++) {
//			// 添加随机数字
//			sb.append("" + i);
//			sb.append("\r\n");
//		}
//		//删除最后一个换行符
//		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());
//		
//		// 返回数据驱动文件
//		return createDataFile(saveFileName, sb);
//	}
//
//	/**
//	 * 该方法用于根据起始数字，通过增加一定的步长来生成自然顺序的数字，并写入文件中
//	 * 
//	 * @param saveFileName
//	 *            数据驱动的文件名
//	 * @param startNum
//	 *            起始数字
//	 * @param step
//	 *            步长
//	 * @param length
//	 *            生成数字的个数
//	 * @return 数据驱动文件对象
//	 * @throws IOException
//	 */
//	public File createContinuesNumberDataFile(String saveFileName, int startNum, int step, int length)
//			throws IOException {
//		//用于存储生成的字符
//		StringBuilder sb = new StringBuilder("");
//		// 循环，向文件中添加随机数字
//		for (int i = 0; i < length; i++) {
//			// 添加随机数字
//			sb.append("" + startNum);
//			sb.append("\r\n");
//			
//			// 数字增加一步
//			startNum += step;
//		}
//		//删除最后一个换行符
//		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());
//		
//		// 返回数据驱动文件
//		return createDataFile(saveFileName, sb);
//	}
//
//	/**
//	 * 该方法用于向文件中添加身份证信息
//	 * 
//	 * @param saveFileName
//	 *            数据驱动的文件名
//	 * @param length
//	 *            生成身份证的个数
//	 * @return 数据驱动文件对象
//	 * @throws IOException
//	 */
//	public File createIDCardDataFile(String saveFileName, int length) throws IOException {
//		//用于存储生成的字符
//		StringBuilder sb = new StringBuilder("");
//		// 循环，向文件中添加随机数字
//		for (int i = 0; i < length; i++) {
//			// 添加随机数字
//			sb.append(PresetString.IdentityCard());
//			sb.append("\r\n");
//		}
//		//删除最后一个换行符
//		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());
//		
//		// 返回数据驱动文件
//		return createDataFile(saveFileName, sb);
//	}
//	
//	/**
//	 * 该方法用于向文件中添加手机号码信息
//	 * 
//	 * @param saveFileName
//	 *            数据驱动的文件名
//	 * @param length
//	 *            生成手机号码的个数
//	 * @return 数据驱动文件对象
//	 * @throws IOException
//	 */
//	public File createPhoneDataFile(String saveFileName, int length) throws IOException {
//		//用于存储生成的字符
//		StringBuilder sb = new StringBuilder("");
//		// 循环，向文件中添加随机数字
//		for (int i = 0; i < length; i++) {
//			// 添加随机数字
//			sb.append("139" + new RandomString(StringMode.NUM).toString(8));
//			sb.append("\r\n");
//		}
//		//删除最后一个换行符
//		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());
//		
//		// 返回数据驱动文件
//		return createDataFile(saveFileName, sb);
//	}
}
