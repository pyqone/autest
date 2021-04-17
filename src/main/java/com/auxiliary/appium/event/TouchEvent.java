package com.auxiliary.appium.event;

import java.time.Duration;
import java.util.Optional;

import org.openqa.selenium.Dimension;

import com.auxiliary.appium.brower.AbstractCellphoneBrower;
import com.auxiliary.selenium.event.AbstractEvent;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

/**
 * <p><b>文件名：</b>TouchEvent.java</p>
 * <p><b>用途：</b>提供app自动化测试中触摸相关的事件操作。</p>
 * <p><b>编码时间：</b>2021年4月16日 下午4:28:56</p>
 * <p><b>修改时间：</b>2021年4月16日 下午4:28:56</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class TouchEvent extends AbstractEvent {
	/**
	 * 指定坐标的间距
	 */
	protected final int SPACE_BETWEEN = 10;

	/**
	 * 存储转换后的app驱动类对象
	 */
	protected AppiumDriver<?> appDriver;
	/**
	 * 存储设备的高度
	 */
	protected int hight;
	/**
	 * 存储设备宽度
	 */
	protected int width;
	/**
	 * 存储底部y轴坐标
	 */
	protected int bottomYNum;
	/**
	 * 存储顶部y轴坐标
	 */
	protected int topYNum;
	/**
	 * 存储左边x轴坐标
	 */
	protected int leftXNum;
	/**
	 * 存储右边x轴坐标
	 */
	protected int rigthXNum;

	/**
	 * 多手指执行类对象
	 */
	protected MultiTouchAction touch;
	/**
	 * 默认的手指滑动事件
	 */
	protected TouchAction<?> defaultTouch;
	/**
	 * 存储完成滑动事件的时间，默认1秒
	 */
	protected long operateTime = 1000;

	/**
	 * 构造对象
	 * 
	 * @param brower 手机浏览器{@link AbstractCellphoneBrower}类对象
	 */
	public TouchEvent(AbstractCellphoneBrower brower) {
		super(brower);

		appDriver = (AppiumDriver<?>) brower.getDriver();
		touch = new MultiTouchAction(appDriver);
		defaultTouch = new TouchAction<>(appDriver);

		// 计算各个特殊坐标的值
		Dimension dim = appDriver.manage().window().getSize();
		hight = dim.getHeight();
		width = dim.getWidth();
		topYNum = hight / SPACE_BETWEEN;
		bottomYNum = hight - topYNum;
		leftXNum = width / SPACE_BETWEEN;
		rigthXNum = width - leftXNum;
	}

	/**
	 * 用于设置完成滑动操作的时间，单位为毫秒
	 * 
	 * @param operateTime 完成滑动操作的时间
	 */
	public void setOperateTime(long operateTime) {
		this.operateTime = operateTime;
	}

	/**
	 * 模拟手指从页面的底部滑动到自定义的坐标上
	 * <p>
	 * 由于手机可能包含虚拟按键，若以最低的坐标作为起始坐标，可能导致页面滑动无效，故此处的底部坐标特指整个屏幕自下而上1/10处位置作为起始坐标。
	 * </p>
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>当滑动停止坐标小于底部坐标时，则滑动无效</li>
	 * <li>当未指定结束坐标时，则y轴坐标按0计算</li>
	 * <li>底部起始坐标的x轴坐标为传入坐标的x轴坐标，当该值为0时，可能导致页面无法滑动</li>
	 * </ol>
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void slideUp(PointOption<?> pointOption) {
		@SuppressWarnings("unchecked")
		PointOption<?> point = Optional.ofNullable(pointOption).orElse(PointOption.point(width, 0));

		// 获取传入的坐标的x作为起始的x坐标
		int bottomXPoint = (Integer) point.build().get("x");

		// 判断当前结束坐标是否大于等于底部坐标，若是，则不做操作
		Integer stopYPoint = (Integer) point.build().get("y");
		if (stopYPoint >= bottomYNum) {
			return;
		}

		// 使用默认手指进行滑动
		defaultTouch.press(PointOption.point(bottomXPoint, bottomYNum))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime))).moveTo(point).release().perform();
	}

	/**
	 * 模拟手指从页面的底部滑动到自定义的y轴坐标上
	 * <p>
	 * 由于手机可能包含虚拟按键，若以最低的坐标作为起始坐标，可能导致页面滑动无效，故此处的底部坐标特指整个屏幕自下而上1/10处位置。x轴坐标取屏幕自右向左1/10处位置
	 * </p>
	 * <p>
	 * <b>注意：</b>当滑动停止坐标小于底部坐标时，则滑动无效</li>
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void slideUp(int y) {
		slideUp(PointOption.point(rigthXNum, y));
	}

	/**
	 * 模拟手指从页面的底部滑动到自定义的坐标上
	 * <p>
	 * 该方法为由屏幕底部1/10处位置作为起始y轴坐标，屏幕右侧1/10处位置作为x轴坐标，向上滑动至屏幕顶底部1/10处位置坐标处位置
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void slideUp() {
		// 计算起始位置的x轴坐标
		slideUp(PointOption.point(rigthXNum, topYNum));
	}
}
