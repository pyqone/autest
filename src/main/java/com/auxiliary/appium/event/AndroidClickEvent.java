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
		click(element);
	}
	
	/**
	 * 用于点击安卓手机上的按键
	 * <p>
	 * 由于{@link AndroidKey}枚举中的值过多，查找不方便，可使用{@link AndroidCommondKey}枚举，
	 * 其列举出了常用的按键，通过{@link AndroidCommondKey#getKey()}方法输出指定枚举
	 * </p>
	 * <p>
	 * <b>注意：</b>目前方法只支持发送单一按键，尚未找到组合按键的解决方案
	 * </p>
	 * @param key 安卓按键{@link AndroidKey}枚举
	 */
	public void clickKey(AndroidKey androidKey) {
		keyEvent.withKey(androidKey);
		driver.pressKey(keyEvent);
	}
	
	/**
	 * 用于长按安卓手机上的按键
	 * <p>
	 * 由于{@link AndroidKey}枚举中的值过多，查找不方便，可使用{@link AndroidCommondKey}枚举，
	 * 其列举出了常用的按键，通过{@link AndroidCommondKey#getKey()}方法输出指定枚举
	 * </p>
	 * <p>
	 * <b>注意：</b>目前方法只支持发送单一按键，尚未找到组合按键的解决方案
	 * </p>
	 * @param key 安卓按键{@link AndroidKey}枚举
	 */
	public void longPressKey(AndroidKey androidKey) {
		keyEvent.withKey(androidKey);
		driver.pressKey(keyEvent);
	}
	
	/**
	 * <p><b>文件名：</b>AndroidClickEvent.java</p>
	 * <p><b>用途：</b>
	 * 枚举安卓手机常用的物理按键
	 * </p>
	 * <p><b>编码时间：</b>2021年4月24日下午6:27:57</p>
	 * <p><b>修改时间：</b>2021年4月24日下午6:27:57</p>
	 * @author 
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum AndroidCommondKey {
		/**
		 * home键（桌面键）
		 */
		HOME(AndroidKey.HOME), 
		/**
		 * 任务键
		 */
		MENU(AndroidKey.MENU), 
		/**
		 * 返回键
		 */
		BACK(AndroidKey.BACK), 
		/**
		 * 电源键
		 */
		POWER(AndroidKey.POWER), 
		/**
		 * 音量上键
		 */
		VOLUME_UP(AndroidKey.VOLUME_UP), 
		/**
		 * 音量下键
		 */
		VOLUME_DOWN(AndroidKey.VOLUME_DOWN), 
		/**
		 * 静音键
		 */
		VOLUME_MUTE(AndroidKey.VOLUME_MUTE), 
		/**
		 * 增加亮度键
		 */
		BRIGHTNESS_UP(AndroidKey.BRIGHTNESS_UP), 
		/**
		 * 降低亮度键
		 */
		BRIGHTNESS_DOWN(AndroidKey.BRIGHTNESS_DOWN)
		;
		/**
		 * 枚举对应的{@link AndroidKey}
		 */
		AndroidKey key;

		/**
		 * 初始化枚举值
		 * @param key {@link AndroidKey}枚举
		 */
		private AndroidCommondKey(AndroidKey key) {
			this.key = key;
		}

		/**
		 * 返回枚举与{@link AndroidKey}枚举的映射
		 * @return {@link AndroidKey}枚举
		 */
		public AndroidKey getKey() {
			return key;
		}
	}
}
