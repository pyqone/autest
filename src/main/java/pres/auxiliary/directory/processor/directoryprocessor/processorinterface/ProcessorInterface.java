package pres.auxiliary.directory.processor.directoryprocessor.processorinterface;

import pres.auxiliary.directory.processor.directoryprocessor.processorchain.ProcessorChain;

/**
 * 该接口定义处理传入的文件路径的方法
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public interface ProcessorInterface {
	/**
	 * 该方法用于处理传入的文件路径
	 * 
	 * @param path
	 *            指定的文件路径
	 * @return 返回处理后的文件路径
	 */
	public abstract StringBuilder doProcessor(StringBuilder path, ProcessorChain chain);
	
	/**
	 * 重写toString()方法，用于输出类名
	 * */
	@Override
	public abstract String toString();
}
