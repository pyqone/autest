package pres.auxiliary.work.selenium.element;

import java.util.function.Consumer;

import org.openqa.selenium.WebDriver;

/**
 * <p><b>文件名：</b>ExceptionAction.java</p>
 * <p><b>用途：用于针对元素获取失败时，执行的方法</b>
 * </p>
 * <p><b>编码时间：</b>2020年8月7日下午8:05:57</p>
 * <p><b>修改时间：</b>2020年8月7日下午8:05:57</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public interface ExceptionAction extends Consumer<WebDriver> {
}
