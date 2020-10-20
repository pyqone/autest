package pres.auxiliary.work.selenium.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.work.selenium.brower.AbstractBrower;
import pres.auxiliary.work.selenium.element.AbstractBy.Element;

/**
 * <p>	
 * <b>文件名：</b>JsEvent.java
 * </p>
 * <p>
 * <b>用途：</b>用提供通过Javascript来修改或者获取页面元素内容的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2018年12月2日 下午12:51:19
 * </p>
 * <p>
 * <b>修改时间：</b>2020年10月18日 下午3:52:44
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 8
 */
public class JsEvent extends AbstractEvent {
	/**
	 * 用于使用js
	 */
	private JavascriptExecutor js;

	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractBrower}类对象
	 */
	public JsEvent(AbstractBrower brower) {
		super(brower);
	}

	/**
	 * 获取元素的指定的属性值，若属性不存在时，则返回空串。
	 * 
	 * @param element {@link Element}对象
	 * @param attributeName 属性名
	 * @return 元素对应属性的内容
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String getAttribute(Element element, String attributeName) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		// 获取对应元素的内容
		String text = (String) (js.executeScript("return arguments[0].getAttribute('" + attributeName + "');",
				wait.until(driver -> {
					try {
						return element.getWebElement();
					} catch (StaleElementReferenceException e) {
						element.againFindElement();
						throw e;
					}
				})));
		
		logText = "获取“" + element.getElementData().getName() + "”元素的" + attributeName + "属性的属性值";
		resultText = (text == null) ? "" : text;
		
		return resultText;
	}

	/**
	 * 设置元素的属性值，并返回属性的原值，若设置的属性名不存在时，则在该元素上增加相应的属性
	 * 
	 * @param element       {@link Element}对象
	 * @param attributeName 需要设置的属性名
	 * @param value         需要设置的属性值
	 * @return 属性的原值
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String putAttribute(Element element, String attributeName, String value) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		// 获取原属性中的值
		resultText = getAttribute(element, attributeName);

		// 执行代码
		js.executeScript("arguments[0].setAttribute('" + attributeName + "','" + value + "');", wait.until(driver -> {
				try {
					return element.getWebElement();
				} catch (StaleElementReferenceException e) {
					element.againFindElement();
					throw e;
				}
			}));
		
		logText = "设置“" + element.getElementData().getName() + "”元素的" + attributeName + "属性的属性值";
		return resultText;
	}

	/**
	 * 在指定的元素下方添加一个元素，元素将带一个名为temp_attribute的属性，其属性的值为一个uuid，并且添加成功后
	 * 方法返回该元素定位xpath，格式为“//标签名[@temp_attribute='uuid']”
	 * 
	 * @param element     {@link Element}对象
	 * @param elementName 新元素（标签）的名称
	 * @return 新增元素的定位方式
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	public String addElement(Element element, String elementName) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		// 获取并将其作为
		String script = "var oldElement = arguments[0];";
		// 拼接添加元素的代码
		script += "var newElement = document.createElement('" + elementName + "');";
		// 给新的元素添加一个属性，并将其值设为UUID，使其可被搜索得到
		String uuid = UUID.randomUUID().toString();
		script += "newElement.setAttribute('temp_attribute', '" + uuid + "');";
		// 向指定位置添加节点
		script += "oldElement.appendChild(newElement);";

		// 执行代码
		js.executeScript(script, wait.until(driver -> {
				try {
					return element.getWebElement();
				} catch (StaleElementReferenceException e) {
					element.againFindElement();
					throw e;
				}
			}));

		logText = "在“" + element.getElementData().getName() + "”元素下添加“" + elementName + "”元素";
		resultText = "//" + elementName + "[@temp_attribute='" + uuid + "']";
		
		return resultText;
	}

	/**
	 * 删除指定的元素，并以json的格式返回被删除的元素信息，其形式为：
	 * 
	 * <pre>
	 * {
	 *     "tagname":元素标签名称
	 *     "attributes":[
	 *         {
	 *             "name":属性1名称
	 *             "value":属性1值
	 *         }, 
	 *         {
	 *             "name":属性1名称
	 *             "value":属性1值
	 *         },
	 *         ...
	 *     ]
	 * }
	 * </pre>
	 * 
	 * @param element {@link Element}对象
	 * @return 被删除的元素信息
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 */
	public JSONObject deleteElement(Element element) {
		//由于在until()方法中无法直接抛出元素不存在的异常，故此处直接调用返回元素的方法，让元素不存在的异常抛出
		element.getWebElement();
		
		//获取元素信息
		JSONObject json = getElementInfromation(wait.until(driver -> {
				try {
					return element.getWebElement();
				} catch (StaleElementReferenceException e) {
					element.againFindElement();
					throw e;
				}
			}));

		// 获取节点
		String script = "var deleteNode = arguments[0];";
		// 获取节点的父节点
		script += "var parentNode = deleteNode.parentNode;";
		// 通过父节点来删除子节点
		script += "parentNode.removeChild(deleteNode)";

		// 执行代码，由于在获取元素信息时已经对元素的过期进行了判断，故此处无需在做判断
		js.executeScript(script, element.getWebElement());
		
		logText = "删除“" + element.getElementData().getName() + "”元素";
		resultText = json.toJSONString();
		
		return json;
	}

	/**
	 * 用于执行已经写好的js脚本
	 * 
	 * @param script js脚本
	 * @param args   传入的参数
	 * @return 执行结果
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 */
	public Object runScript(String script, Object... args) {
		logText = "执行脚本：" + script;
		resultText = "";
		
		// 执行代码
		return js.executeScript(script, args);
	}

	/**
	 * 获取元素所有的属性信息，以json的形式返回。格式为：
	 * 
	 * <pre>
	 * {
	 *     "tagname":元素标签名称
	 *     "attributes":[
	 *         {
	 *             "name":属性1名称
	 *             "value":属性1值
	 *         }, 
	 *         {
	 *             "name":属性1名称
	 *             "value":属性1值
	 *         },
	 *         ...
	 *     ]
	 * }
	 * </pre>
	 * 
	 * @param element {@link Element}对象
	 * @return 元素属性的信息，以json的形式返回
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 */
	@SuppressWarnings("unchecked")
	private JSONObject getElementInfromation(WebElement element) {
		JSONObject elementJson = new JSONObject();

		// 添加元素的标签名称
		elementJson.put("tagname", element.getTagName());

		JSONArray attArray = new JSONArray();
		// 获取元素的所有属性信息
		((ArrayList<Object>) js.executeScript("return arguments[0].attributes;", element)).stream().
		// 将属性信息变为字符串的形式
				map(obj -> obj.toString()).
				// 删除无用的属性信息，并将属性信息存储至一个json中进行返回
				map(text -> {
					// 将属性按照“, ”进行切分，得到每一个信息
					String[] atts = text.split("\\,\\ ");

					JSONObject json = new JSONObject();
					// 过滤属性信息
					Arrays.stream(atts).filter(att -> {
						String key = att.split("=")[0];
						return "name".equals(key) || "value".equals(key);
					}).
					// 将过滤后的信息存储至json中（最后只剩下name和value属性）
					forEach(att -> {
						String[] kv = att.split("=");
						json.put(kv[0], kv[1].indexOf("}") > -1 ? kv[1].substring(0, kv[1].length() - 1) : kv[1]);
					});

					return json;
				}).
				// 将每一个json存储至attArray中
				forEach(json -> {
					attArray.add(json);
				});

		// 存储属性
		elementJson.put("attributes", attArray);

		return elementJson;
	}
}
