package pres.auxiliary.selenium.event;

import java.util.UUID;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import pres.auxiliary.selenium.xml.ByType;

/**
 * <p><b>文件名：</b>JsEvent.java</p>
 * <p><b>用途：</b>提供通过Javascript来修改或者获取页面元素内容的方法</p>
 * <p><b>编码时间：</b>2018年12月2日 下午12:51:19</p>
 * <p><b>修改时间：</b>2019年10月7日下午4:54:52</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class JsEvent extends AbstractEvent {
	/**
	 * 用于使用js
	 */
	private JavascriptExecutor js;

	/**
	 * 构造对象
	 * @param driver 页面WebDriver对象
	 */
	public JsEvent(WebDriver driver) {
		super(driver);
		js = (JavascriptExecutor) getDriver();
	}

	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并获取元素的属性值，若属性不存在时，则返回空串。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为元素属性的值，可参见{@link EventResultEnum}类。
	 * 
	 * @param name          控件的名称或xpath与css定位方式
	 * @param attributeName 元素属性名
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取
	 */
	public Event getAttribute(String name, String attributeName) {
		try {
			Event.setValue(EventResultEnum.STRING.setValue((String) (js.executeScript("return " + getScript(name) + ".getAttribute('" + attributeName + "')"))));
		} catch ( WebDriverException e ) {
			//当当前元素无此属性时，则返回空
			Event.setValue(EventResultEnum.STRING.setValue(""));
		}
		
		return Event.newInstance(getDriver());
	}

	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并设置元素的属性值或者添加属性。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为元素属性的原值，可参见{@link EventResultEnum}类。
	 * 
	 * @param name          控件的名称或xpath与css定位方式
	 * @param attributeName 元素属性名
	 * @param value         需要设置的属性值
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取
	 */
	public Event setAttribute(String name, String attributeName, String value) {
		// 存储脚本
		String script = getScript(name);
		// 获取原属性中的值
		String oldValue = getAttribute(name, attributeName).getStringValve();
		
		//判断传入的对象是否为null，为null则执行删除命令
		if ( value != null ) {
			// 拼接脚本
			script += (".setAttribute('" + attributeName + "','" + value + "');");
		} else {
			// 拼接脚本
			script += (".removeAttribute('" + attributeName + "');");
		}
		
		// 执行代码
		js.executeScript(script);
		
		Event.setValue(EventResultEnum.STRING.setValue(oldValue));
					
		return Event.newInstance(getDriver());
	}

	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并向元素下添加一个新的元素，并返回其元素的查找xpath。注意，通过该方法创建的元素会自带
	 * 一个名为“temp_attribute”的属性，其值为一个uuid，以方便查找元素。
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为新节点的xpath，可参见{@link EventResultEnum}类。
	 * @param name 控件的名称或xpath与css定位方式
	 * @param elementName 待添加元素的标签名称
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取
	 */
	public Event addElement(String name, String elementName) {
		// 获取并将其作为
		String script = "var oldElement = " + getScript(name) + ";";
		// 拼接添加元素的代码
		script += "var newElement = document.createElement('" + elementName + "');";
		// 给新的元素添加一个属性，并将其值设为UUID，使其可被搜索得到
		String uuid = UUID.randomUUID().toString();
		script += "newElement.setAttribute('temp_attribute', '" + uuid + "');";
		// 向指定位置添加节点
		script += "oldElement.appendChild(newElement);";

		// 执行代码
		js.executeScript(script);

		Event.setValue(EventResultEnum.STRING.setValue("//" + elementName + "[@temp_attribute=\"" + uuid + "\"]"));
		
		return Event.newInstance(getDriver());
	}
	
	/**
	 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件名称对应的定位方式，或直接传入xpath与css定位方式，
	 * 根据定位方式在页面查找元素并删除该元素。
	 * 本操作返回的枚举值是{@link EventResultEnum#VOID}，可参见{@link EventResultEnum}类。
	 * @param name 控件的名称或xpath与css定位方式
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取
	 */
	public Event deleteElement(String name) {
		//获取节点
		String script = "var deleteNode = " + getScript(name) + ";";
		//获取节点的父节点
		script += "var parentNode = deleteNode.parentNode;";
		//通过父节点来删除子节点
		script += "parentNode.removeChild(deleteNode)";
		
		// 执行代码
		js.executeScript(script);
		
		return Event.newInstance(getDriver());
	}
	
	/**
	 * 用于执行已经写好的js脚本
	 * 本操作返回的枚举值是{@link EventResultEnum#VOID}，可参见{@link EventResultEnum}类。
	 * @param script js脚本
	 */
	public Event runScript(String script) {
		// 执行代码
		js.executeScript(script);
		return Event.newInstance(getDriver());
	}

	/**
	 * 用于识别传入的控件模型，并将定位的代码的直接返回
	 * 
	 * @param text 元素名称或元素的定位方式
	 * @return 返回页面元素WebElement对象
	 */
	private String getScript(String text) {
		// 存储脚本
		String script = "";

		String[] element = getElementPosition(text).split("=");
		// 判断定位方式，若定位方式为css，则按照querySelector()方式进行选择，其他的方式均按照xpath来获取
		if (ByType.XPATH.getValue().equalsIgnoreCase(element[0])) {
			script = "document.evaluate('" + element[1]
					+ "', document, null, XPathResult.ANY_TYPE, null).iterateNext()";
		} else if (ByType.CSS.getValue().equalsIgnoreCase(element[0])) {
			script = "document.querySelector(" + element[1] + ")";
		} else {
			script = "document.evaluate('//*[@" + element[0] + "=\"" + element[1]
					+ "\"]', document, null, XPathResult.ANY_TYPE, null).iterateNext()";
		}

		// 返回页面元素对象
		return script;
	}

}
