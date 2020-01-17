package pres.auxiliary.directory.processor.directoryprocessor.impprocessorinterface;

import pres.auxiliary.directory.processor.directoryprocessor.processorchain.ProcessorChain;
import pres.auxiliary.directory.processor.directoryprocessor.processorinterface.ProcessorInterface;

/**
 * 该类用于对传入的文件路径进行文件夹分隔符格式化的处理，处理的方法是将所有的分隔符“/”改为“\\”
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class FormatSeparatorProcessor implements ProcessorInterface {

	@Override
	public StringBuilder doProcessor(StringBuilder path, ProcessorChain chain) {
		// 用于存储循环查询字符串得到的下标
		int i = 0;

		// 循环，查询path中的“/”，并将“/”字符改为“\\”
		while (true) {
			// 判断字符串查询到的字符串是否为“/”，若为“/”则修改
			if ((i = path.indexOf("/")) > -1) {
				// 删除当前字符
				path.delete(i, i + 1);
				// 将字符“\\”插入到删除的位置，由于需要转译，故“\\”在存储时应为“\\\\”
				path.insert(i, "\\");
				// 继续循环
				continue;
			}
			// 若未找到字符“/”，则结束循环
			break;
		}
		
		chain.doProcessor(path, chain);

		// 返回格式化后的路径
		return path;
	}

	@Override
	public String toString() {
		return "FormatSeparatorProcessor";
	}

	
}
