package com.auxiliary.appium.event;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.auxiliary.appium.brower.AbstractCellphoneBrower;
import com.auxiliary.appium.brower.AndroidBrower;
import com.auxiliary.selenium.element.Element;
import com.auxiliary.selenium.event.ClickEvent;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

/**
 * <p><b>文件名：</b>AndroidClickEvent.java</p>
 * <p><b>用途：</b>
 * 用于在安卓手机上运行的自动化脚本相关的点击事件。
 * <p>
 * 该事件是对点击事件{@link ClickEvent}的一个补充，主要添加与安卓手机相关的按键点击操作。
 * </p>
 * </p>
 * <p><b>编码时间：</b>2021年4月23日下午8:46:38</p>
 * <p><b>修改时间：</b>2021年4月23日下午8:46:38</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 */
public class AndroidClickEvent extends ClickEvent {
	KeyEvent keyEvent = new KeyEvent();
	AndroidDriver<AndroidElement> driver;
	
	/**
	 * 构造对象
	 * 
	 * @param brower 浏览器{@link AbstractCellphoneBrower}类对象
	 */
	public AndroidClickEvent(AndroidBrower brower) {
		super(brower);
		driver = brower.getAndroidDriver();
	}

	/**
	 * 由于app端不存在右键的感念，故该方法等同于调用{@link #click(Element)}方法
	 * @param element {@link Element}对象
	 * @throws TimeoutException 元素无法操作时抛出的异常 
	 * @throws NoSuchElementException 元素不存在或下标不正确时抛出的异常 
	 */
	@Override
	public void rightClick(Element element) {
		super.click(element);
	}
	
	/**
	 * 用于点击安卓手机上的按键
	 * @param key 安卓按键{@link AndroidKey}枚举
	 */
	public void clickKey(AndroidKey androidKey) {
		keyEvent.withKey(androidKey);
		driver.pressKey(keyEvent);
	}
}
