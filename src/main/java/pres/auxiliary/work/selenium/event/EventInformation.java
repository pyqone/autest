package pres.auxiliary.work.selenium.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import pres.auxiliary.work.selenium.element.Element_Old;

/**
 * <p><b>文件名：</b>EventInformation.java</p>
 * <p><b>用途：</b>
 * 用于存储事件的信息
 * </p>
 * <p><b>编码时间：</b>2020年7月10日下午3:47:58</p>
 * <p><b>修改时间：</b>2020年7月10日下午3:47:58</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class EventInformation {
	/**
	 * 存储事件操作的方法
	 */
	private Method method;
	/**
	 * 传入到事件的参数
	 */
	private Object[] args;
	/**
	 * 存储传入到方法中的元素类对象，由args进行分离
	 */
	private ArrayList<Element_Old> elementList = new ArrayList<>();
	
	/**
	 * 构造对象
	 * @param method 事件方法的{@link Method}类对象
	 * @param args 传入事件方法的参数
	 */
	public EventInformation(Method method, Object[] args) {
		super();
		this.method = method;
		this.args = args;
		//提取Element类对象
		toElement();
	}

	/**
	 * 返回事件操作方法类对象
	 * @return 事件的{@link Method}类对象
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 用于返回传入事件方法的参数
	 * @return 传入事件方法的参数
	 */
	public Object[] getParam() {
		return args;
	}
	
	/**
	 * 用于返回传入到方法中{@link Element_Old}类对象集合
	 * @return {@link Element_Old}类对象集合
	 */
	public ArrayList<Element_Old> getElement() {
		return elementList;
	}
	
	/**
	 * 用于将args中为Element类对象的参数进行转换并存储至elementList中
	 */
	private void toElement() {
		Arrays.stream(args).forEach(arg -> {
			if (arg instanceof Element_Old) {
				elementList.add((Element_Old) arg);
			}
		});
	}
}
