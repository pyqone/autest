package pres.auxiliary.directory.processor.directoryprocessor.impprocessorinterface;

import pres.auxiliary.directory.processor.directoryprocessor.processorchain.ProcessorChain;
import pres.auxiliary.directory.processor.directoryprocessor.processorinterface.ProcessorInterface;

/**
 * 该处理器用于判断路径末尾是否含有分隔符“\\”，若不包含则添加上
 * @author 彭宇琦
 * @version Ver1.0
 */
public class AddSeparatorProcessor implements ProcessorInterface {

	@Override
	public StringBuilder doProcessor(StringBuilder path, ProcessorChain chain) {
		// 判断字符串的最后一位是否为含分隔符，若不为分隔符则说明路径末尾没有分隔符，则说明文件路径的末尾不含“\\”，需要再添加
		if (path.lastIndexOf("\\") != (path.length() - 1)) {
			path.append("\\");
		}

		chain.doProcessor(path, chain);
		// 返回文件路径
		return path;
	}

	@Override
	public String toString() {
		return "AddSeparatorProcessor";
	}
}
