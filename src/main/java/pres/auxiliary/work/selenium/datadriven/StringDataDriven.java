package pres.auxiliary.work.selenium.datadriven;

public class StringDataDriven extends DataDriven {

	/*
	@Override
	public File createDataFile(String saveFileName, int minSize, int maxSize, int length) throws IOException, DataNotFoundException {
		// 用于存储生成的字符
		StringBuilder sb = new StringBuilder("");
		// 循环，向文件中添加随机数字
		for (int i = 0; i < length; i++) {
			// 添加随机字母
			sb.append(new RandomString(StringMode.LOW, StringMode.CAP).toString(minSize, maxSize));
			sb.append("\r\n");
		}
		// 删除最后一个换行符
		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());

		// 返回数据驱动文件
		return createDataFile(saveFileName, sb);
	}

	@Override
	public File createDataFile(String saveFileName, int size, int length) throws IOException {
		// 用于存储生成的字符
		StringBuilder sb = new StringBuilder("");
		// 循环，向文件中添加随机数字
		for (int i = 0; i < length; i++) {
			// 添加随机数字
			sb.append(new RandomString(StringMode.LOW, StringMode.CAP).toString(size));
			sb.append("\r\n");
		}
		// 删除最后一个换行符
		sb.delete(sb.lastIndexOf("\r\n"), sb.lastIndexOf("\r\n") + "\r\n".length());

		// 返回数据驱动文件
		return createDataFile(saveFileName, sb);
	}
	*/
	
}
