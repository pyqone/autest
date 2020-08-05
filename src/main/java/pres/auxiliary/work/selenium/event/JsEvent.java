package pres.auxiliary.work.selenium.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pres.auxiliary.work.selenium.element.Element;
import pres.auxiliary.work.selenium.element.ElementType;

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
 * <b>修改时间：</b>2020年5月17日 下午5:21:44
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class JsEvent extends AbstractEvent {
	/**
	 * 用于使用js
	 */
	private JavascriptExecutor js;

	/**
	 * 构造对象
	 * 
	 * @param driver 页面WebDriver对象
	 */
	public JsEvent(WebDriver driver) {
		super(driver);
		js = (JavascriptExecutor) this.driver;
	}

	/**
	 * 获取元素的指定的属性值，若属性不存在时，则返回空串。
	 * 
	 * @param element       元素
	 * @param attributeName 属性名
	 * @return 元素对应属性的内容
	 */
	public String getAttribute(Element element, String attributeName) {
		// 获取对应元素的内容
		String text = (String) (js.executeScript("return arguments[0].getAttribute('" + attributeName + "');",
				wait.until(driver -> {
					try {
						return element.getWebElement();
					} catch (StaleElementReferenceException e) {
						element.findElement();
						return null;
					}
				})));
		// 返回对应属性的内容，若传入的属性不存在，则返回空串
		return (text == null) ? "" : text;
	}

	/**
	 * 设置元素的属性值，并返回属性的原值，若设置的属性名不存在时，则在该元素上增加相应的属性
	 * 
	 * @param element       元素
	 * @param attributeName 需要设置的属性名
	 * @param value         需要设置的属性值
	 * @return 属性的原值
	 */
	public String putAttribute(Element element, String attributeName, String value) {
		// 获取原属性中的值
		String oldValue = getAttribute(element, attributeName);

		// 执行代码
		js.executeScript("arguments[0].setAttribute('" + attributeName + "','" + value + "');", wait.until(driver -> {
				try {
					return element.getWebElement();
				} catch (StaleElementReferenceException e) {
					element.findElement();
					return null;
				}
			}));
		return oldValue;
	}

	/**
	 * 在指定的元素下方添加一个元素，元素将带一个名为temp_attribute的属性，其属性的值为一个uuid，并且添加成功后
	 * 方法返回该元素定位xpath，格式为“//标签名[@temp_attribute='uuid']”
	 * 
	 * @param element     元素
	 * @param elementName 新元素（标签）的名称
	 * @return 新增元素的定位方式
	 */
	public String addElement(Element element, String elementName) {
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
					element.findElement();
					return null;
				}
			}));

		return "//" + elementName + "[@temp_attribute='" + uuid + "']";
	}

	/**
	 * 根据json中的属性及元素名称信息，在指定元素下添加一个元素，元素将带一个
	 * 名为temp_attribute的属性，其属性的值为一个uuid，并且添加成功后
	 * 方法返回该元素定位xpath，格式为“//标签名[@temp_attribute='uuid']”。
	 * 传入的json格式可以参照{@link #deleteElement(WebElement)}方法中 返回的json形式
	 * 
	 * @param element     元素
	 * @param elementJson 新元素（标签）的信息
	 * @return 新增元素的定位方式
	 */
	public String addElement(Element element, JSONObject elementJson) {
		// 添加元素
		String elementName = elementJson.getString("tagname");

		// 获取新添加元素的xpath
		String xpath = addElement(element, elementName);
		// 查找新添加的元素（由于是新添加的元素，肯定能查找到，故无需编写等待）
		Element newElement = new Element(driver, "TeamElement", ElementType.COMMON_ELEMENT);
		List<By> byList = new ArrayList<>();
		byList.add(By.xpath(xpath));
		newElement.setByList(byList);

		// 获取元素的所有属性
		JSONArray attributes = elementJson.getJSONArray("attributes");
		// 遍历属性信息，向元素中添加属性
		for (int i = 0; i < attributes.size(); i++) {
			JSONObject attJson = attributes.getJSONObject(i);
			putAttribute(newElement, attJson.getString("name"), attJson.getString("value"));
		}

		return xpath;
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
	 * @param element 元素
	 * @return 元素的信息
	 */
	public JSONObject deleteElement(Element element) {
		//获取元素信息
		JSONObject json = getElementInfromation(wait.until(driver -> {
				try {
					return element.getWebElement();
				} catch (StaleElementReferenceException e) {
					element.findElement();
					return null;
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

		return json;
	}

	/**
	 * 用于执行已经写好的js脚本
	 * 
	 * @param script js脚本
	 * @return 执行结果
	 */
	public Object runScript(String script) {
		// 执行代码
		return js.executeScript(script);
	}

	/**
	 * 用于执行已经写好的js脚本
	 * 
	 * @param script js脚本
	 * @param args   传入的参数
	 * @return 执行结果
	 */
	public Object runScript(String script, Object... args) {
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
	 * @param element 元素
	 * @return 元素属性的信息，以json的形式返回
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
