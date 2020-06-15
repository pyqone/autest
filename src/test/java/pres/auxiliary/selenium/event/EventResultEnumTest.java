package pres.auxiliary.selenium.event;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * <p><b>文件名：</b>EventResultEnumTest.java</p>
 * <p><b>用途：</b>用于测试枚举类EventResultEnum</p>
 * <p><b>编码时间：</b>2019年8月31日下午2:01:30</p>
 * <p><b>修改时间：</b>2019年8月31日下午2:01:30</p>
 * @author 
 * @version Ver1.0
 * @since JDK 12
 *
 */
public class EventResultEnumTest {
	/**
	 * 分隔运行方法
	 * @param method 方法类对象
	 */
	@BeforeMethod
	public void start(Method method) {
		System.out.println("正在运行" + method.getName() + "方法");
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 分隔运行的方法
	 */
	@AfterMethod
	public void end() {
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 用于测试getValue()方法
	 */
	@Test
	public void testGetValue() {
		System.out.println(EventResultEnum.BOOLEAN_FALSE.getValue());
		System.out.println(EventResultEnum.BOOLEAN_TRUE.getValue());
		System.out.println(EventResultEnum.NUMBER.getValue());
		System.out.println(EventResultEnum.STRING.getValue());
		System.out.println(EventResultEnum.VOID.getValue());
	}
	
	/**
	 * 用于测试setValue()方法
	 */
	@Test
	public void testSetValue() {
		System.out.println(EventResultEnum.BOOLEAN_FALSE.setValue("hehe").getValue());
		System.out.println(EventResultEnum.BOOLEAN_TRUE.setValue("gege").getValue());
		System.out.println(EventResultEnum.NUMBER.setValue("xixi").getValue());
		System.out.println(EventResultEnum.STRING.setValue("haha").getValue());
		System.out.println(EventResultEnum.VOID.setValue("lolo").getValue());
	}
	
	/**
	 * 用于测试setValue()方法，修改数字为正整数
	 */
	@Test
	public void testSetValue_NumberIsPositiveInt() {
		EventResultEnum.NUMBER.setValue("1234");
		System.out.println(EventResultEnum.NUMBER.getValue());
	}
	
	/**
	 * 用于测试setValue()方法，修改数字为负整数
	 */
	@Test
	public void testSetValue_NumberIsNegativeInt() {
		EventResultEnum.NUMBER.setValue("-1234");
		System.out.println(EventResultEnum.NUMBER.getValue());
	}
	
	/**
	 * 用于测试setValue()方法，修改数字为正实数
	 */
	@Test
	public void testSetValue_NumberIsPositiveFloat() {
		EventResultEnum.NUMBER.setValue("12.2265");
		System.out.println(EventResultEnum.NUMBER.setValue("12.2265").getValue());
		EventResultEnum.NUMBER.setValue("0.2265");
		System.out.println(EventResultEnum.NUMBER.getValue());
		EventResultEnum.NUMBER.setValue(".2265");
		System.out.println(EventResultEnum.NUMBER.getValue());
	}
	
	/**
	 * 用于测试setValue()方法，修改数字为负实数
	 */
	@Test
	public void testSetValue_NumberIsNegativeFloat() {
		EventResultEnum.NUMBER.setValue("-12.2265");
		System.out.println(EventResultEnum.NUMBER.getValue());
		EventResultEnum.NUMBER.setValue("-0.2265");
		System.out.println(EventResultEnum.NUMBER.getValue());
		EventResultEnum.NUMBER.setValue("-.2265");
		System.out.println(EventResultEnum.NUMBER.getValue());
	}
	
	/**
	 * 用于测试setValue()方法，修改数字为负实数
	 */
	@Test
	public void testSetValue_NotNumber() {
		EventResultEnum.NUMBER.setValue("hahahhaha");
		System.out.println(EventResultEnum.NUMBER.getValue());
	}
}
