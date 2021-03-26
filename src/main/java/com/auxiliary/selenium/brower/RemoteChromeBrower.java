package com.auxiliary.selenium.brower;

import java.net.URL;

import org.openqa.selenium.remote.BrowserType;

import com.auxiliary.selenium.page.Page;

/**
 * <p><b>文件名：</b>RemoteChromeBrower.java</p>
 * <p><b>用途：</b>
 * 连接或唤起指定远程服务器地址上的谷歌浏览器
 * </p>
 * <p><b>编码时间：</b>2021年3月26日下午7:33:22</p>
 * <p><b>修改时间：</b>2021年3月26日下午7:33:22</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class RemoteChromeBrower extends RemoteBrower {
	/**
	 * 构造对象
	 * 
	 * @param serverUrl   服务地址{@link URL}对象
	 * @param url         待测站点
	 * @param pageName    页面名称
	 */
	public RemoteChromeBrower(URL serverUrl, String url, String pageName) {
		super(serverUrl, BrowserType.CHROME, url, pageName);
	}

	/**
	 * 构造对象
	 * 
	 * @param serverUrl   服务地址{@link URL}对象
	 * @param page        页面{@link Page}类对象
	 */
	public RemoteChromeBrower(URL serverUrl, Page page) {
		super(serverUrl, BrowserType.CHROME, page);
	}

}
