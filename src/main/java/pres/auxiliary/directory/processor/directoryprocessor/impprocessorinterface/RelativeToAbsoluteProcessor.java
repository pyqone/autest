package pres.auxiliary.directory.processor.directoryprocessor.impprocessorinterface;

import pres.auxiliary.directory.processor.directoryprocessor.processorchain.ProcessorChain;
import pres.auxiliary.directory.processor.directoryprocessor.processorinterface.ProcessorInterface;

/**
 * 该处理器用于将相对路径改为绝对路径，判断相对路径的依据是路径中是否包含字符“:”，
 * 相对路径中不可能还含有字符“:”（windows下不允许创建带“:”的字符），故若路径
 * 中含有“:”，则说明路径为绝对路径。<br/><br/>
 * <i><b>注意，使用该处理器时需要保证相对路径的命名正确</b></i>
 * @author 彭宇琦
 * @version Ver1.0
 */
public class RelativeToAbsoluteProcessor implements ProcessorInterface {

	@Override
	public StringBuilder doProcessor(StringBuilder path, ProcessorChain chain) {
		// 判断传入的路径是绝对路径还是相对路径，若是相对路径则在相对路径前加上当前位置
		if (path.indexOf(":") < 0) {
			path.insert(0, (System.getProperty("user.dir") + "\\"));
		}
		
		chain.doProcessor(path, chain);
		return path;
	}

	@Override
	public String toString() {
		return "RelativeToAbsoluteProcessor";
	}
}
