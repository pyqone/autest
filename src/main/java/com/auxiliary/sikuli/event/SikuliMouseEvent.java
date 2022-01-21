package com.auxiliary.sikuli.event;

import java.util.Optional;

import com.auxiliary.sikuli.element.FindSikuliElement;
import com.auxiliary.sikuli.element.SikuliElement;
import com.auxiliary.sikuli.element.TimeoutException;

/**
 * <p>
 * <b>文件名：</b>SikuliMouseEvent.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 封装sikuli工具的鼠标事件，可设置{@link FindSikuliElement}类对象，用以元素名称来进行操作
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月21日 上午8:03:49
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月21日 上午8:03:49
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class SikuliMouseEvent extends SikuliAbstractEvent {
    /**
     * 构造对象
     * @since autest 3.0.0
     */
    public SikuliMouseEvent() {
        super();
    }

    /**
     * 该方法用于鼠标左键单击指定的元素
     *
     * @param element 元素类对象
     * @since autest 3.0.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void click(SikuliElement element) {
        String eventName = "鼠标左键单击";

        actionOperate(eventName, element.getElement(), match -> {
            // 根据点击事件的返回值是否为0判断操作是否成功
            if (match.click() != 0) {
                return "";
            } else {
                return null;
            }
        });

        recordLog(String.format("%s%s元素", eventName, element.getName()), 0);
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，使用鼠标左键单击目标元素
     *
     * @param elementName 元素名称
     * @param index 多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param keys 外链词语
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException 元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void click(String elementName, int index, String...keys) {
        if (Optional.ofNullable(find).isPresent()) {
            click(find.findElement(elementName, index, keys));
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，使用鼠标左键单击目标元素
     *
     * @param elementName 元素名称
     * @param keys 外链词语
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException 元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void click(String elementName, String...keys) {
        click(elementName, 1, keys);
    }

    /**
     * 该方法用于鼠标左键双击指定的元素
     *
     * @param element 元素类对象
     * @since autest 3.0.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void doubleClick(SikuliElement element) {
        String eventName = "鼠标左键双击";

        actionOperate(eventName, element.getElement(), match -> {
            // 根据点击事件的返回值是否为0判断操作是否成功
            if (match.doubleClick() != 0) {
                return "";
            } else {
                return null;
            }
        });

        recordLog(String.format("%S%s元素", eventName, element.getName()), 0);
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，使用鼠标左键双击目标元素
     *
     * @param elementName 元素名称
     * @param index 多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param keys 外链词语
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException 元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void doubleClick(String elementName, int index, String...keys) {
        if (Optional.ofNullable(find).isPresent()) {
            doubleClick(find.findElement(elementName, index, keys));
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，使用鼠标左键双击目标元素
     *
     * @param elementName 元素名称
     * @param keys 外链词语
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException 元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void doubleClick(String elementName, String...keys) {
        doubleClick(elementName, 1, keys);
    }

    /**
     * 该方法用于鼠标右键单击指定的元素
     *
     * @param element 元素类对象
     * @since autest 3.0.0
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void rightClick(SikuliElement element) {
        String eventName = "鼠标右键单击";

        actionOperate(eventName, element.getElement(), match -> {
            // 根据点击事件的返回值是否为0判断操作是否成功
            if (match.rightClick() != 0) {
                return "";
            } else {
                return null;
            }
        });

        recordLog(String.format("%s%s元素", eventName, element.getName()), 0);
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，使用鼠标右键单击目标元素
     *
     * @param elementName 元素名称
     * @param index 多元素时的下标，允许传入负数，参考{@link FindSikuliElement#findElement(String, int, String...)}方法
     * @param keys 外链词语
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException 元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void rightClick(String elementName, int index, String...keys) {
        if (Optional.ofNullable(find).isPresent()) {
            rightClick(find.findElement(elementName, index, keys));
        } else {
            throw new ElementOperateException("未指定元素查找类对象，无法通过元素名称查找指定的元素");
        }
    }

    /**
     * 该方法用于根据元素信息，通过指定的元素查找类，查找到目标元素后，使用鼠标右键单击目标元素
     *
     * @param elementName 元素名称
     * @param keys 外链词语
     * @since autest 3.0.0
     * @throws ElementOperateException 未指定元素查找类时，抛出的异常
     * @throws TimeoutException 元素查找超时时，抛出的异常
     * @throws OperateTimeoutException 操作超时时，抛出的异常
     */
    public void rightClick(String elementName, String...keys) {
        rightClick(elementName, 1, keys);
    }
}
