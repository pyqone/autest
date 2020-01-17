package pres.auxiliary.selenium.event.inter;

import java.io.File;

import pres.auxiliary.selenium.event.Event;
import pres.auxiliary.selenium.event.EventResultEnum;
import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;

/**
 * <p><b>文件名：</b>TextEventInter.java</p>
 * <p><b>用途：</b>该接口集合普通元素的所有文本相关的事件，可在此处扩展新的方法</p>
 * <p><b>编码时间：</b>2019年9月6日上午8:52:33</p>
 * <p><b>修改时间：</b>2019年10月31日下午7:25:40</p>
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 12
 *
 */
public interface TextEventInter extends CommenElementClearInter, CommenElementGetAttributeValueInter,
		CommenElementGetTextInter, CommenElementInputInter {
	/**
	 * <p>
	 * 通过图片识别的方法，根据codeName查找验证码图片元素位置，识别页面中的验证码，并将结果填入textName对应
	 * 的文本框中。注意，该方法识别验证码成功的概率不高，在数字+英文的验证码模式下，经常将数字识别为英文。
	 * </p>
	 * <p>
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为输入到控件的内容，可参见{@link EventResultEnum}类。
	 * </p>
	 * 
	 * @param textName 文本框控件的名称或xpath与css定位方式
	 * @param codeName 验证码图片控件的名称或xpath与css定位方式
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event codeInput(String textName, String codeName);
	
	/**
	 * <p>
	 * 该方法用于将一个指定的整数随机填写到传入的控件组中<br>
	 * <b><i>建议在指定值远远大于控件的数量时再使用该方法，否则将会出现不可预期的问题</i></b>
	 * </p>
	 * <p>
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为输入到控件的内容，
	 * 由于涉及到多个文本框，故其返回值有多个，将以“值1,值2,值3...”的形式进行存储，可参见{@link EventResultEnum}类。
	 * </p>
	 * @param num 指定的整数
	 * @param names 控件的名称或xpath与css定位方式，可传入多个控件名称
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event avgIntergeInput(int num, String...names);
	
	/**
	 * 该方法通过指定的随机字符串长度与指定的随机字符串模型枚举（{@link StringMode}枚举类），向控件中随机输入字符串操作。<br>
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为输入到控件的内容，可参见{@link EventResultEnum}类。
	 * @param name 控件的名称或xpath与css定位方式，可传入多个控件名称
	 * @param minLength 字符串最小长度，设为小于等于0的数值时则默认为1
	 * @param maxLength 字符串最大长度，设为小于等于0的数值时则默认为1，若需要指定字符串的输入长度，可设置minLength数值与maxLength一致
	 * @param modes {@link StringMode}枚举，指定字符串输入的类型，可传入多种模型，参见{@link RandomString#RandomString(StringMode...)}
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event randomInput(String name, int minLength, int maxLength, StringMode... modes);
	
	/**
	 * 该方法通过指定的随机字符串长度与指定的随机字符串模型，向控件中随机输入字符串操作。<br>
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为输入到控件的内容，可参见{@link EventResultEnum}类。
	 * @param name 控件的名称或xpath与css定位方式，可传入多个控件名称
	 * @param minLength 字符串最小长度，设为小于等于0的数值时则默认为1
	 * @param maxLength 字符串最大长度，设为小于等于0的数值时则默认为1，若需要指定字符串的输入长度，可设置minLength数值与maxLength一致
	 * @param mode 可用的随机字符串抽取范围，参见{@link RandomString#RandomString(String)}
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event randomInput(String name, int minLength, int maxLength, String mode);
	/**
	 * 该方法用于向控件中上传指定的文件。<br>
	 * 本操作返回的枚举值是{@link EventResultEnum#STRING}，其枚举的value值为文件在本地中的绝对路径，可参见{@link EventResultEnum}类。
	 * @param name 控件的名称或xpath与css定位方式，可传入多个控件名称
	 * @param updataFile 需要上传到控件中的文件
	 * @return {@link Event}类对象，可通过{@link Event#getStringValve()}方法获取操作的返回值
	 */
	Event updataFile(String name, File updataFile);
}
