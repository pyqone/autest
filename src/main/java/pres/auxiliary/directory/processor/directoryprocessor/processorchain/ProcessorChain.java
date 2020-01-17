package pres.auxiliary.directory.processor.directoryprocessor.processorchain;

import java.util.ArrayList;
import java.util.List;

import pres.auxiliary.directory.processor.directoryprocessor.processorchain.ProcessorChain;
import pres.auxiliary.directory.processor.directoryprocessor.processorinterface.ProcessorInterface;


public class ProcessorChain implements ProcessorInterface {
	//用于存储处理方法的类对象
	private List<ProcessorInterface> processorChain = new ArrayList<>();
	//用于记录调用的处理器在processorChain中的下标
	private int index = -1;
	
	/**
	 * 该构造方法不做任何处理
	 */
	public ProcessorChain() {
	}
	
	/**
	 * 该构造方法用于向处理链中添加一条或多条处理器
	 * @param processor 待添加的处理器
	 */
	public ProcessorChain(ProcessorInterface... processor) {
		//循环，向集合中添加处理器
		for ( ProcessorInterface p : processor ) {
			add(p);
		}
	}

	/**
	 * 该方法用于向处理器链末尾添加一个处理器
	 * @param processor 待添加的处理器
	 * @return 返回添加状态
	 */
	public boolean add(ProcessorInterface processor) {
		return processorChain.add(processor);
	}
	
	/**
	 * 该方法用于向处理器链指定位置添加一个处理器
	 * @param index 指定的处理器链位置
	 * @param processor 待添加的处理器
	 */
	public void add(int index, ProcessorInterface processor) {
		processorChain.add(index, processor);
	}
	
	/**
	 * 该方法用于向处理器链末尾添加一组处理器
	 * @param processors 待添加的处理器组
	 * @return 返回添加状态
	 */
	public boolean addAll(List<ProcessorInterface> processors) {
		return processorChain.addAll(processors);
	}

	/**
	 * 该方法用于向处理器链指定位置添加一组处理器
	 * @param index 指定的处理器链位置
	 * @param processors 待添加的处理器组
	 * @return 返回添加状态
	 */
	public boolean addAll(int index, List<ProcessorInterface> processors) {
		return processorChain.addAll(index, processors);
	}
	
	/**
	 * 该方法用于移除指定位置的处理器
	 * @param index 指定的位置
	 * @return 返回被移除的处理器
	 */
	public ProcessorInterface remove(int index) {
		return processorChain.remove(index);
	}
	
	/**
	 * 该方法用于移除指定的处理器
	 * @param processor 指定的处理器
	 * @return 返回被移除的处理器
	 */
	public boolean remove(ProcessorInterface processor) {
		return processorChain.remove(processor);
	}
	
	/**
	 * 该方法用于返回指定位置的处理器
	 * @param index 指定的处理器位置
	 * @return 返回对应位置的处理器
	 */
	public ProcessorInterface get(int index) {
		return processorChain.get(index);
	}
	
	/**
	 * 该方法用于替换指定位置的处理器
	 * @param index 指定的处理器位置
	 * @param processor 待替换的处理器
	 * @return 返回原处理器
	 */
	public ProcessorInterface set(int index, ProcessorInterface processor) {
		return processorChain.set(index, processor);
	}
	
	/**
	 * 该方法用于返回处理链的长度
	 * @return 处理链的长度
	 */
	public int size() {
		return processorChain.size();
	}
	
	/**
	 * 该方法通过传入的处理器名称来查找处理链中是否存在该处理器，存在则返回对应
	 * 的下标，不存在则返回-1（调用该方法时需要处理器重写的toString()方法是输出类名的）
	 * @param processorName 待搜索的处理器名称
	 * @return 对应的下标或者-1
	 */
	public int indexOf(String processorName) {
		//循环遍历数组
		for (int i = 0; i < processorChain.size(); i++) {
			//判断每一个元素的toSting()方法得到的字符串是否存在与传入的参数相同（忽略大小写），若有相同则返回相应的下标
			if ( processorChain.get(i).toString().equalsIgnoreCase(processorName) ) {
				return i;
			}
		}
		//循环结束则表示未找到对应的处理器，则返回-1
		return -1;
	}
	
	/**
	 * 该方法通过传入处理器类对象来来查找处理链中是否存在该处理器，存在则返回对应的下标，不存在则返回-1
	 * @param processor 传入的处理器类对象
	 * @return 对应的下标或者-1
	 */
	public int indexOf(ProcessorInterface processor) {
		return processorChain.indexOf(processor);
	}
	
	/**
	 * 用于对所有的处理器进行处理
	 * @param 传入的文件路径
	 * @return 返回处理后的路径
	 */
	@Override
	public StringBuilder doProcessor(StringBuilder path, ProcessorChain chain) {
		//下标向前移动
		index++;
		if ( index == chain.size() ) {
			return null;
		}
		//调用处理器的doProcessor()对象
		processorChain.get(index).doProcessor(path, chain);
		//返回处理后的文件路径
		return path;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("ProcessorChain [ ");
		for ( ProcessorInterface p : processorChain ) {
			s.append(p.toString());
			s.append(" ");
		}
		s.append("]");
		
		return s.toString();
	}
}
