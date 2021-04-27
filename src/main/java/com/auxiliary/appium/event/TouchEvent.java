package com.auxiliary.appium.event;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import com.auxiliary.appium.brower.AbstractCellphoneBrower;
import com.auxiliary.selenium.element.Element;
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
	protected Point centrePoint;

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
		defaultTouch = new TouchAction<>(appDriver);

		// 计算各个特殊坐标的值
		Dimension dim = appDriver.manage().window().getSize();
		hight = dim.getHeight();
		width = dim.getWidth();
		topYNum = hight / SPACE_BETWEEN;
		bottomYNum = hight - topYNum;
		leftXNum = width / SPACE_BETWEEN;
		rigthXNum = width - leftXNum;
		centrePoint = new Point(width / 2, hight / 2);

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
	 * @param y 滑动停止坐标
	 */
	public void upGlide(int y) {
		upGlide(PointOption.point(rigthXNum, y));
	}

	/**
	 * 模拟手指从页面的底部滑动到自定义的坐标上
	 * <p>
	 * 该方法为由屏幕底部1/10处位置作为起始y轴坐标，屏幕右侧1/10处位置作为x轴坐标，向上滑动至屏幕顶部1/10处位置坐标处位置
	 * </p>
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
	 * @param y 滑动停止坐标
	 */
	public void downGlide(int y) {
		downGlide(PointOption.point(rigthXNum, y));
	}

	/**
	 * 模拟手指从页面的顶部滑动到自定义的坐标上
	 * <p>
	 * 该方法为由屏幕顶部1/10处位置作为起始y轴坐标，屏幕右侧1/10处位置作为x轴坐标，向下滑动至屏幕底部1/10处位置坐标处位置
	 * </p>
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
	 * @param points 滑动坐标组
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

		StringJoiner pointText = new StringJoiner("、");
		for (PointOption<?> point : points) {
			Map<String, Object> pointMap = point.build();
			pointText.add(String.format("(%s, %s)", pointMap.get("x"), pointMap.get("y")));
		}

		logText = "单手指依次从以下坐标进行滑动：" + pointText.toString();
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
		// 判断位移值是否小于等于0
		if (pixel <= 0) {
			return;
		}

		// 获取中心点坐标
//		Map<String, Object> coordinateMap = centrePoint.build();
		int x = centrePoint.getX();
		int y = centrePoint.getY();

		// 根据滑动方向，指定滑动的点
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

		// 滑动页面
		actionGlide(PointOption.point(centrePoint), movePoint);

		logText = String.format("由中心点坐标(%d, %d)向%s移动%d个像素", x, y, directionType.getName(), pixel);
	}

	/**
	 * 用于从一个元素的坐标点滑动到另一个元素坐标点上
	 * <p>
	 * <b>注意：</b>
	 * <ol>
	 * <li>该方法仅对原生元素有效，WebView元素获取到的坐标点可能与实际坐标有所偏差。</li>
	 * <li>若未指定开始元素，则以右顶部坐标为起始坐标；若未指定终止滑动元素，则以右底部坐标为终止坐标</li>
	 * </ol>
	 * </p>
	 * 
	 * @param startElement 起始元素
	 * @param endElement   终止滑动元素
	 */
	public void glideToElement(Element startElement, Element endElement) {
		// 获取元素坐标，并设置默认坐标点
		Point startPoint = Optional.ofNullable(startElement).map(e -> e.getWebElement().getLocation())
				.orElse(new Point(rigthXNum, topYNum));
		Point endPoint = Optional.ofNullable(endElement).map(e -> e.getWebElement().getLocation())
				.orElse(new Point(rigthXNum, bottomYNum));

		// 滑动页面
		actionGlide(PointOption.point(startPoint), PointOption.point(endPoint));

		logText = String.format("由起始元素坐标(%d, %d)滑动终止元素坐标(%d, %d)", startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	/**
	 * 用于通过屏幕中心点向左下和右上两个方向滑动，以模拟放大操作
	 */
	public void enlargement() {
		MultiTouchAction touch = new MultiTouchAction(appDriver);

		// 指定滑动坐标点
		Point leftDownPoint = new Point(leftXNum, bottomYNum);
		Point rightUpPoint = new Point(rigthXNum, topYNum);

		// 指定手指以及滑动的点
		TouchAction<?> touch1 = new TouchAction<>(appDriver);
		TouchAction<?> touch2 = new TouchAction<>(appDriver);

		touch1.press(PointOption.point(centrePoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(leftDownPoint)).release();
		touch2.press(PointOption.point(centrePoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(rightUpPoint)).release();

		// 进行操作
		wait.until(driver -> {
			try {
				touch.add(touch1).add(touch2);
				touch.perform();
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}

			return true;
		});

		logText = String.format("由中心点坐标(%d, %d)模拟两根手指同时滑动到屏幕左下角(%d, %d)与屏幕右上角(%d, %d)，模拟放大操作", centrePoint.x,
				centrePoint.y, leftDownPoint.x, leftDownPoint.y, rightUpPoint.x, rightUpPoint.y);
	}

	/**
	 * 用于通过屏幕左下和右上两个方向中心点滑动，以模拟缩小操作
	 */
	public void reduce() {
		MultiTouchAction touch = new MultiTouchAction(appDriver);

		// 指定滑动坐标点
		Point leftDownPoint = new Point(leftXNum, bottomYNum);
		Point rightUpPoint = new Point(rigthXNum, topYNum);

		// 指定手指以及滑动的点
		TouchAction<?> touch1 = new TouchAction<>(appDriver);
		TouchAction<?> touch2 = new TouchAction<>(appDriver);

		touch1.press(PointOption.point(leftDownPoint))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(leftDownPoint)).release();
		touch2.press(PointOption.point(rightUpPoint))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(centrePoint)).release();

		// 进行操作
		wait.until(driver -> {
			try {
				touch.add(touch1).add(touch2);
				touch.perform();
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}

			return true;
		});

		logText = String.format("模拟两根手指同时由屏幕左下角(%d, %d)与屏幕右上角(%d, %d)滑动到中心点坐标(%d, %d)，模拟缩小操作", leftDownPoint.x,
				leftDownPoint.y, rightUpPoint.x, rightUpPoint.y, centrePoint.x, centrePoint.y);
	}

	/**
	 * 用于模拟三指下拉操作
	 */
	public void threeDownGlide() {
		MultiTouchAction touch = new MultiTouchAction(appDriver);

		// 指定三指下拉的起始坐标
		Point leftUpPoint = new Point(leftXNum, topYNum);
		Point centerUpPoint = new Point(centrePoint.x, topYNum);
		Point rightUpPoint = new Point(rigthXNum, topYNum);

		// 指定三指下拉的结束坐标
		Point leftDownPoint = new Point(leftXNum, bottomYNum);
		Point centerDownPoint = new Point(centrePoint.x, bottomYNum);
		Point rightDownPoint = new Point(rigthXNum, bottomYNum);

		// 指定手指以及滑动的点
		TouchAction<?> touch1 = new TouchAction<>(appDriver);
		TouchAction<?> touch2 = new TouchAction<>(appDriver);
		TouchAction<?> touch3 = new TouchAction<>(appDriver);

		touch1.press(PointOption.point(leftUpPoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(leftDownPoint)).release();
		touch2.press(PointOption.point(centerUpPoint))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(centerDownPoint)).release();
		touch3.press(PointOption.point(rightUpPoint))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
				.moveTo(PointOption.point(rightDownPoint)).release();

		// 进行操作
		wait.until(driver -> {
			try {
				touch.add(touch1).add(touch2).add(touch3).perform();
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}

			return true;
		});

		logText = String.format(
				"模拟三根手指同时由屏幕上方左(%d, %d)、中(%d, %d)、右(%d, %d)三个坐标，滑动至屏幕下方左(%d, %d)、中(%d, %d)、右(%d, %d)三个坐标，模拟三指下拉操作",
				leftUpPoint.x, leftUpPoint.y, centerUpPoint.x, centerUpPoint.y, rightUpPoint.x, rightUpPoint.y,
				leftDownPoint.x, leftDownPoint.y, centerDownPoint.x, centerDownPoint.y, rightDownPoint.x,
				rightDownPoint.y);
	}

	/**
	 * 用于通过滑动的形式打开手机的通知栏
	 */
	public void openNotification() {
		Point startPoint = new Point(centrePoint.x, 0);
		Point endPoint = new Point(centrePoint.x, bottomYNum);

		actionGlide(PointOption.point(startPoint), PointOption.point(endPoint));

		logText = String.format("使用单根手指从屏幕中心顶点坐标(%d, %d)滑动到屏幕中心底部坐标(%d, %d)，模拟打开通知栏", startPoint.x, startPoint.y,
				endPoint.x, endPoint.y);
	}

	/**
	 * 用于通过滑动的形式关闭手机的通知栏
	 */
	public void closeNotification() {
		Point startPoint = new Point(centrePoint.x, bottomYNum);
		Point endPoint = new Point(centrePoint.x, 0);

		actionGlide(PointOption.point(startPoint), PointOption.point(endPoint));

		logText = String.format("使用单根手指从屏幕中心底部坐标(%d, %d)滑动到屏幕中心顶部坐标(%d, %d)，模拟关闭通知栏", startPoint.x, startPoint.y,
				endPoint.x, endPoint.y);
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
			try {
				defaultTouch.press(startPoints).waitAction(WaitOptions.waitOptions(Duration.ofMillis(operateTime)))
						.moveTo(endPoints).release().perform();
			} catch (Exception e) {
				return null;
			}

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
		CENTER("中心");

		/**
		 * 标记移动方向的名称
		 */
		String name;

		/**
		 * 初始化枚举名称
		 * 
		 * @param name 枚举名称
		 */
		private DirectionType(String name) {
			this.name = name;
		}

		/**
		 * 返回枚举的名称
		 * 
		 * @return 枚举名称
		 */
		public String getName() {
			return name;
		}
	}
}
