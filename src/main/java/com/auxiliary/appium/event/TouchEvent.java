package com.auxiliary.appium.event;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import org.openqa.selenium.Dimension;

import com.auxiliary.appium.brower.AbstractCellphoneBrower;
import com.auxiliary.selenium.event.AbstractEvent;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

/**
 * <p>
 * <b>文件名：</b>TouchEvent.java
 * </p>
 * <p>
 * <b>用途：</b>提供app自动化测试中触摸相关的事件操作。
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月16日 下午4:28:56
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月16日 下午4:28:56
 * </p>
 * 
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
	 * 存储中心点坐标
	 */
	protected PointOption<?> centrePoint;

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
		centrePoint = PointOption.point(width / 2, hight / 2);

		wait.withMessage("当前无法进行滑动操作");
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
	public void upGlide(PointOption<?> pointOption) {
		@SuppressWarnings("unchecked")
		PointOption<?> point = Optional.ofNullable(pointOption).orElse(PointOption.point(rigthXNum, topYNum));

		// 获取传入的坐标的x作为起始的x坐标
		int bottomXPoint = (Integer) point.build().get("x");

		// 判断当前结束坐标是否大于等于底部坐标，若是，则不做操作
		Integer stopYPoint = (Integer) point.build().get("y");
		if (stopYPoint >= bottomYNum) {
			return;
		}

		// 使用默认手指进行滑动
		actionGlide(PointOption.point(bottomXPoint, bottomYNum), point);

		logText = String.format("单手指从底部坐标(%d, %d)直线滑动至坐标(%s, %d)", bottomXPoint, bottomYNum, point.build().get("x"),
				stopYPoint);
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
	public void upGlide(int y) {
		upGlide(PointOption.point(rigthXNum, y));
	}

	/**
	 * 模拟手指从页面的底部滑动到自定义的坐标上
	 * <p>
	 * 该方法为由屏幕底部1/10处位置作为起始y轴坐标，屏幕右侧1/10处位置作为x轴坐标，向上滑动至屏幕顶部1/10处位置坐标处位置
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void upGlide() {
		// 计算起始位置的x轴坐标
		upGlide(topYNum);
	}

	/**
	 * 模拟手指从页面的顶部滑动到自定义的坐标上
	 * <p>
	 * 该方法所取的顶部坐标点与底部坐标点的获取方法类似，取顶部1/10处作为起始点，向下滑动至目标点。可参考{@link #upGlide(PointOption)}方法进行传参
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void downGlide(PointOption<?> pointOption) {
		@SuppressWarnings("unchecked")
		PointOption<?> point = Optional.ofNullable(pointOption).orElse(PointOption.point(rigthXNum, bottomYNum));

		// 获取传入的坐标的x作为起始的x坐标
		int bottomXPoint = (Integer) point.build().get("x");

		// 判断当前结束坐标是否大于等于底部坐标，若是，则不做操作
		Integer stopYPoint = (Integer) point.build().get("y");
		if (stopYPoint <= topYNum) {
			return;
		}

		// 使用默认手指进行滑动
		actionGlide(PointOption.point(bottomXPoint, topYNum), point);

		logText = String.format("单手指从顶部坐标(%d, %d)直线滑动至坐标(%s, %d)", bottomXPoint, topYNum, point.build().get("x"),
				stopYPoint);
	}

	/**
	 * 模拟手指从页面的顶部滑动到自定义的y轴坐标上
	 * <p>
	 * 该方法所取的顶部坐标点与底部坐标点的获取方法类似，取顶部1/10处作为起始点，向下滑动至目标点。可参考{@link #upGlide(int)}方法进行传参
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void downGlide(int y) {
		downGlide(PointOption.point(rigthXNum, y));
	}

	/**
	 * 模拟手指从页面的顶部滑动到自定义的坐标上
	 * <p>
	 * 该方法为由屏幕顶部1/10处位置作为起始y轴坐标，屏幕右侧1/10处位置作为x轴坐标，向下滑动至屏幕底部1/10处位置坐标处位置
	 * </p>
	 * 
	 * @param pointOption 滑动停止坐标
	 */
	public void downGlide() {
		// 计算起始位置的x轴坐标
		downGlide(bottomYNum);
	}

	/**
	 * 用于根据指定的多个坐标，模拟单手指进行滑动操作
	 * <p>
	 * <b>注意：</b>若指定的坐标点小于2个时，则不进行操作
	 * </p>
	 * 
	 * @param startPoint 起始点坐标
	 * @param endPoint   结束点坐标
	 */
	public void glide(PointOption<?>... points) {
		// 判断是否指定相应数量的坐标点
		if (!Optional.ofNullable(points).filter(p -> p.length > 1).isPresent()) {
			return;
		}

		// 使用默认手指进行滑动
		wait.until(driver -> {
			// 遍历指定的点，并根据点的坐标进行滑动操作
			// 滑动方式为：将上一个点作为下一个点的起始点进行滑动
			IntStream.range(1, points.length).forEach(index -> {
				actionGlide(points[index - 1], points[index]);
			});

			return true;
		});

		for (PointOption<?> point : points) {
			StringJoiner pointText = new StringJoiner("、");
			Map<String, Object> pointMap = point.build();
			pointText.add(String.format("(%s, %s)", pointMap.get("x"), pointMap.get("y")));

			logText = "单手指依次从以下坐标进行滑动：" + pointText.toString();
		}
	}

	/**
	 * 用于从屏幕的中心点向一个指定的方向直线滑动指定的像素
	 * <p>
	 * <b>注意：</b>当指定的方向为{@link DirectionType#CENTER}时，则不做滑动操作
	 * </p>
	 * 
	 * @param directionType 滑动方向枚举
	 * @param pixel         滑动的像素值
	 */
	public void directionGlide(DirectionType directionType, int pixel) {
		//判断位移值是否小于等于0
		if (pixel <= 0) {
			return;
		}
		
		// 获取中心点坐标
		Map<String, Object> coordinateMap = centrePoint.build();
		int x = (Integer) coordinateMap.get("x");
		int y = (Integer) coordinateMap.get("y");
		
		//根据滑动方向，指定滑动的点
		PointOption<?> movePoint;
		switch (Optional.ofNullable(directionType).orElse(DirectionType.CENTER)) {
		case UP:
			movePoint = PointOption.point(x, y - (pixel > y ? y : pixel));
			break;
		case DOWN:
			movePoint = PointOption.point(x, y + (pixel > y ? y : pixel));
			break;
		case LEFT:
			movePoint = PointOption.point(x - (pixel > x ? x : pixel), y);
			break;
		case RIGHT:
			movePoint = PointOption.point(x + (pixel > x ? x : pixel), y);
			break;
		default:
			return;
		}
		
		//滑动页面
		actionGlide(centrePoint, movePoint);
		
		logText = String.format("由中心点坐标(%d, %d)向%s移动%d个像素", x, y, directionType.getName(), pixel);
	}

	/**
	 * 根据坐标点进行滑动
	 * 
	 * @param startPoints 起始坐标点
	 * @param endPoints   结束坐标点
	 */
	private void actionGlide(PointOption<?> startPoints, PointOption<?> endPoints) {
		// 使用默认手指进行滑动
		wait.until(driver -> {
			defaultTouch.press(startPoints).waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
					.moveTo(endPoints).release().perform();

			return true;
		});
	}

	/**
	 * <p>
	 * <b>文件名：</b>TouchEvent.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 定义方向枚举，用于标记滑动的方向
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年4月19日下午2:25:25
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年4月19日下午2:25:25
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public enum DirectionType {
		/**
		 * 向上滑动
		 */
		UP("上"),
		/**
		 * 向下滑动
		 */
		DOWN("下"),
		/**
		 * 向右滑动
		 */
		RIGHT("右"),
		/**
		 * 向左滑动
		 */
		LEFT("左"), 
		/**
		 * 向中心滑动
		 */
		CENTER("中心")
		;
		
		/**
		 * 标记移动方向的名称
		 */
		String name;

		/**
		 * 初始化枚举名称
		 * @param name 枚举名称
		 */
		private DirectionType(String name) {
			this.name = name;
		}

		/**
		 * 返回枚举的名称
		 * @return 枚举名称
		 */
		public String getName() {
			return name;
		}
	}
}
