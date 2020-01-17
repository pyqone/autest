package pres.auxiliary.selenium.event;

import org.openqa.selenium.WebDriver;

import pres.auxiliary.selenium.event.inter.CommenElementClearInter;
import pres.auxiliary.selenium.event.inter.CommenElementClickInter;
import pres.auxiliary.selenium.event.inter.CommenElementGetTextInter;
import pres.auxiliary.selenium.event.inter.CommenElementInputInter;
import pres.auxiliary.selenium.event.inter.CommenElementJudgeTextInter;
import pres.auxiliary.selenium.event.inter.CommenElementSelectInter;

/**
 * <p>
 * <b>文件名：</b>Event.java
 * </p>
 * <p>
 * <b>用途：</b> 该类提供各个事件类的入口，同时，所有的事件方法的返回值保存在类中，通过{@link #getStringValve()}或
 * {@link #getBooleanValue()}方法，对返回值进行返回。类中提供基本的事件，包括鼠标左键单击事件、输入事件、获取文本事件
 * 和清空事件以及选择下拉框事件。在该类允许直接切换窗体及iframe和对alert的操作。
 * </p>
 * <p>
 * <b>编码时间：</b>2019年8月28日下午5:01:33
 * </p>
 * <p>
 * <b>修改时间：</b>2019年9月25日下午3:03:33
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class Event extends AbstractEvent implements CommenElementClickInter, CommenElementClearInter,
		CommenElementGetTextInter, CommenElementInputInter, CommenElementJudgeTextInter, CommenElementSelectInter {
	/**
	 * 用于单例模式的设计
	 */
	private static Event event = null;

	/**
	 * 存储可返回的TextEvent类对象
	 */
	private TextEvent textEvent;
	/**
	 * 存储可返回的ClickEvent类对象
	 */
	private ClickEvent clickEvent;
	/**
	 * 存储可返回的SelectEvent类对象
	 */
	private SelectEvent selectEvent;
	/**
	 * 存储可返回的JudgeEvent类对象
	 */
	private JudgeEvent judgeEvent;
	
	/**
	 * 存储可返回的JsEvent类对象
	 */
	private JsEvent jsEvent;
	
	/**
	 * 用于存储各个事件的返回值
	 */
	private static EventResultEnum value;

	// 私有所有的构造，保证类对象唯一
	/**
	 * 传入WebDriver对象，构造Event类对象
	 * 
	 * @param driver WebDriver对象
	 */
	private Event(WebDriver driver) {
		super(driver);
	}

	/**
	 * 用于通过WebDriver对象来创建Event类对象，当已创建过Event类对象时，则返回上次创建的Event类对象
	 * 
	 * @param driver WebDriver对象
	 * @return Event类对象
	 */
	public static Event newInstance(WebDriver driver) {
		// 判断对象是否存储，若不存在，则进行构造
		// 若对象存在，则判断其当前WebDriver对象是否与传入的WebDriver对象相同，若不相同，则重新构造
		return event == null ? (event = new Event(driver)) : (event.getDriver() == driver ? event : (event = new Event(driver)));
	}

	/**
	 * 用于返回一个TextEvent（文本事件）类对象
	 * 
	 * @return TextEvent类对象
	 */
	public TextEvent getTextEvent() {
		// 先判断是否存在对象，若不存在，则创建，
		return textEvent == null ? new TextEvent(getDriver()) : textEvent;
	}
	
	/**
	 * 用于返回一个ClickEvent（点击事件）类对象
	 * 
	 * @return ClickEvent类对象
	 */
	public ClickEvent getClickEvent() {
		// 先判断是否存在对象，若不存在，则创建，
		return clickEvent == null ? new ClickEvent(getDriver()) : clickEvent;
	}
	
	/**
	 * 用于返回一个SelectEvent（下拉框选择事件）类对象
	 * 
	 * @return SelectEvent类对象
	 */
	public SelectEvent getSelectEvent() {
		// 先判断是否存在对象，若不存在，则创建，
		return selectEvent == null ? new SelectEvent(getDriver()) : selectEvent;
	}
	
	/**
	 * 用于返回一个JudgeEvent（文本判断事件）类对象
	 * 
	 * @return JudgeEvent类对象
	 */
	public JudgeEvent getJudgeEvent() {
		// 先判断是否存在对象，若不存在，则创建，
		return judgeEvent == null ? new JudgeEvent(getDriver()) : judgeEvent;
	}
	
	/**
	 * 用于返回一个JsEvent（Javascript事件）类对象
	 * 
	 * @return JsEvent类对象
	 */
	public JsEvent getJsEvent() {
		// 先判断是否存在对象，若不存在，则创建，
		return jsEvent == null ? new JsEvent(getDriver()) : jsEvent;
	}
	
	/**
	 * 用于设置事件类方法的返回值
	 * 
	 * @param value 进行操作后得到的返回值
	 */
	public static void setValue(EventResultEnum value) {
		Event.value = value;
	}

	/**
	 * 用于返回事件类方法的返回值
	 * 
	 * @return 事件类方法的返回值
	 */
	public static String getStringValve() {
		return value == null ? "" : value.getValue();
	}

	/**
	 * 用于当事件类方法的返回值为BOOLEAN_FALSE或BOOLEAN_TRUE类型时将返回值由字符串转为boolean类型返回
	 * 
	 * @return 事件类方法的返回值
	 */
	public static boolean getBooleanValue() {
		switch (value) {
		case BOOLEAN_FALSE:
			return false;
		case BOOLEAN_TRUE:
			return true;
		default:
			throw new IllegalArgumentException("该返回值类型不是boolean类型: " + value);
		}
	}

	@Override
	public Event select(String name, int option) {
		return getSelectEvent().select(name, option);
	}

	@Override
	public Event judgeText(String name, boolean keyFull, String key) {
		return getJudgeEvent().judgeText(name, keyFull, key);
	}

	@Override
	public Event input(String name, String text) {
		return getTextEvent().input(name, text);
	}

	@Override
	public Event getText(String name) {
		return getTextEvent().getText(name);
	}

	@Override
	public Event clear(String name) {
		return getTextEvent().clear(name);
	}

	@Override
	public Event click(String name) {
		return getClickEvent().click(name);
	}
}
