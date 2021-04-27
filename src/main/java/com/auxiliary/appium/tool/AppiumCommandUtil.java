package com.auxiliary.appium.tool;

import java.io.File;
import java.util.Optional;

import com.auxiliary.tool.data.IllegalDataException;
import com.auxiliary.tool.data.WindowsCmdUtil;

/**
 * <p>
 * <b>文件名：</b>AppiumCommandUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 定义在执行app自动化前，需要在dos下执行的常用命令。若“adb”和“aapt”命令未被添加至系统变量中，
 * 则可分别通过{@link #setAdbFolder(File)}、{@link #setAaptFolder(File)}方法来设置命令所在路径
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月8日下午7:04:55
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月25日上午10:17:19
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 */
public class AppiumCommandUtil {
	/**
	 * 用于切分当前返回结果的属性内容的标识符
	 */
	private static final String SPLIT_VALUE = "' ";
	/**
	 * 用于切分当前属性值的标识符
	 */
	private static final String SPLIT_CONTENT = "'";

	/**
	 * 指向aapt命令所在路径
	 */
	private static File aaptFolder;
	/**
	 * 指向adb命令所在路径
	 */
	private static File adbFolder;

	/**
	 * 工具类，私有构造
	 */
	private AppiumCommandUtil() {
	}

	/**
	 * 用于设置aapt命令所在路径的File对象
	 * <p>
	 * <b>注意：</b>命令所在路径文件对象必须将可执行文件也包含至路径中，否则读取命令将存在异常。
	 * 例如，命令所在路径为“D:\test\aapt.exe”，则传入的File对象必须为：{@code new File("D:\\test\\aapt.exe")}
	 * </p>
	 * 
	 * @param aaptFolder aapt命令所在路径
	 */
	public static void setAaptFolder(File aaptFolder) {
		AppiumCommandUtil.aaptFolder = aaptFolder;
	}

	/**
	 * 用于设置adb命令所在路径的File对象
	 * <p>
	 * <b>注意：</b>命令所在路径文件对象必须将可执行文件也包含至路径中，否则读取命令将存在异常。
	 * 例如，命令所在路径为“D:\test\adb.exe”，则传入的File对象必须为：{@code new File("D:\\test\\adb.exe")}
	 * </p>
	 * 
	 * @param adbFolder adb命令所在路径
	 */
	public static void setAdbFolder(File adbFolder) {
		AppiumCommandUtil.adbFolder = adbFolder;
	}

	/**
	 * 用于获取app的包名，若包名获取失败，则返回空串
	 * 
	 * @param appFile app包所在路径
	 * @return 包名
	 */
	public static String getAppPackageName(File appFile) {
		if (appFile == null) {
			return "";
		}

		String key = "package: name";

		// 拼接命令
		String cmd = String.format("%s dump badging %s", aaptCommand(), appFile.getAbsoluteFile());
		// 执行，获取包名所在行
		String resultText = WindowsCmdUtil.action(cmd, true, key);

		// 由于单行获取到多个属性，故需要按相应的属性切分符进行切分，得到目标属性后，返回目标属性的值
		for (String text : resultText.split(SPLIT_VALUE)) {
			if (text.contains(key)) {
				// 对返回结果进行裁剪，获取相应的内容
				return text.substring(text.indexOf(SPLIT_CONTENT) + SPLIT_CONTENT.length());
			}
		}

		return "";
	}

	/**
	 * 用于获取app的启动类名
	 * <p>
	 * <b>注意：</b>由于一个app可有多个启动类，故可根据关键词进一步对启动类名进行搜索。若搜索后仍存在多个启动类名，则将返回第一个启动类名
	 * </p>
	 * 
	 * @param appFile app包所在路径
	 * @param keys    关键词组
	 * @return 启动类名
	 */
	public static String getAppActivity(File appFile, String... keys) {
		if (appFile == null) {
			return "";
		}

		String key = "launchable-activity: name";

		// 拼接命令
		String cmd = String.format("%s dump badging %s", aaptCommand(), appFile.getAbsoluteFile());

		// 将关键词组与需要查找的关键词进行合并，若keys为null，则返回一个空数组
		String[] searchKey = Optional.ofNullable(keys).orElse(new String[] {});
		String[] newKeys = new String[searchKey.length + 1];
		for (int i = 0; i < searchKey.length; i++) {
			newKeys[i] = searchKey[i];
		}
		newKeys[searchKey.length] = key;

		// 执行，获取包名所在行
		String resultText = WindowsCmdUtil.action(cmd, true, newKeys);

		// 由于单行获取到多个属性，故需要按相应的属性切分符进行切分，得到目标属性后，返回目标属性的值
		for (String text : resultText.split(SPLIT_VALUE)) {
			if (text.contains(key)) {
				// 对返回结果进行裁剪，获取相应的内容
				return text.substring(text.indexOf(SPLIT_CONTENT) + SPLIT_CONTENT.length());
			}
		}

		return "";
	}

	/**
	 * 用于启动指定端口的adb服务
	 * 
	 * @param port 端口号
	 * @throws IllegalDataException 端口号不合法时抛出的异常
	 */
	public static void startAdbServer(int port) {
		// 判断端口号是否合法
		if (port < 0 || port > 65535) {
			throw new IllegalDataException("端口号范围只能是0～65535之间：" + port);
		}

		WindowsCmdUtil.action(String.format("%s -P %d start-server", adbCommand(), port));
	}

	/**
	 * 用于启动默认端口（5037端口）的adb服务
	 */
	public static void startAdbServer() {
		startAdbServer(5037);
	}

	/**
	 * 用于停止指定端口的adb服务
	 * 
	 * @param port 端口号
	 * @throws IllegalDataException 端口号不合法时抛出的异常
	 */
	public static void killAdbServer(int port) {
		// 判断端口号是否合法
		if (port < 0 || port > 65535) {
			throw new IllegalDataException("端口号范围只能是0～65535之间：" + port);
		}
		WindowsCmdUtil.action(String.format("%s -P %d kill-server", adbCommand(), port));
	}

	/**
	 * 用于停止默认端口（5037端口）的adb服务
	 */
	public static void killAdbServer() {
		killAdbServer(5037);
	}

	/**
	 * 用于通过指定端口的adb服务，连接指定的设备名称
	 * 
	 * @param port       端口号
	 * @param deviceName 设备名称
	 * @throws IllegalDataException 未指定设备名称或端口号不合法时抛出的异常
	 */
	public static boolean connectDevice(int port, String deviceName) {
		// 判断端口号是否合法
		if (port < 0 || port > 65535) {
			throw new IllegalDataException("端口号范围只能是0～65535之间：" + port);
		}

		// 连接设备
		WindowsCmdUtil.action(String.format("%s -P %d connect %s", adbCommand(), port, Optional.ofNullable(deviceName)
				.filter(t -> !t.isEmpty()).orElseThrow(() -> new IllegalDataException("未指定设备名称"))));

		// 获取当前端口连接的设备，根据连接状态返回是否成功连接设备
		return !WindowsCmdUtil.action(String.format("%s -P %d devices", adbCommand(), port), true, deviceName, "device")
				.isEmpty();
	}

	/**
	 * 用于通过默认端口（5037端口）的adb服务，连接指定的设备名称
	 * 
	 * @param deviceName 设备名称
	 * @throws IllegalDataException 未指定设备名称时抛出的异常
	 */
	public static boolean connectDevice(String deviceName) {
		return connectDevice(5037, deviceName);
	}

	/**
	 * 用于通过指定端口的adb服务，断开指定设备名称的连接
	 * 
	 * @param port       端口号
	 * @param deviceName 设备名称
	 * @throws IllegalDataException 未指定设备名称或端口号不合法时抛出的异常
	 */
	public static boolean disconnectDevice(int port, String deviceName) {
		// 判断端口号是否合法
		if (port < 0 || port > 65535) {
			throw new IllegalDataException("端口号范围只能是0～65535之间：" + port);
		}

		// 连接设备
		WindowsCmdUtil
				.action(String.format("%s -P %d disconnect %s", adbCommand(), port, Optional.ofNullable(deviceName)
						.filter(t -> !t.isEmpty()).orElseThrow(() -> new IllegalDataException("未指定设备名称"))));

		// 获取当前端口连接的设备，根据连接状态返回是否成功连接设备
		return WindowsCmdUtil.action(String.format("%s -P %d devices", adbCommand(), port), true, deviceName).isEmpty();
	}

	/**
	 * 用于通过默认端口（5037端口）的adb服务，断开指定设备名称的连接
	 * 
	 * @param deviceName 设备名称
	 * @throws IllegalDataException 未指定设备名称时抛出的异常
	 */
	public static boolean disconnectDevice(String deviceName) {
		return disconnectDevice(5037, deviceName);
	}

	/**
	 * 用于通过指定端口的adb服务，安装指定路径下的app文件
	 * <p>
	 * <b>注意：</b>需要保证指定的端口上存在设备，否则命令会一直处于等待状态，直到连上设备为止
	 * </p>
	 * 
	 * @param port    端口号
	 * @param isGrant 安装后是否授权所有的权限
	 * @param appFile app所在路径
	 * @return 是否安装成功
	 */
	public static boolean installApp(int port, boolean isGrant, File appFile) {
		// 判断端口号是否合法
		if (port < 0 || port > 65535) {
			throw new IllegalDataException("端口号范围只能是0～65535之间：" + port);
		}

		// 拼接并执行命令
		String result = WindowsCmdUtil.action(String.format("%s -P %d install -r -t -d %s %s", adbCommand(), port,
				(isGrant ? "-g" : ""), Optional.ofNullable(appFile).filter(File::isFile)
						.orElseThrow(() -> new IllegalDataException("错误的App应用路径：" + appFile))));

		// 返回是否安装成功
		return result.contains("Success");
	}

	/**
	 * 用于通过默认端口（5037端口）的adb服务，安装指定路径下的app文件
	 * 
	 * @param isGrant 安装后是否授权所有的权限
	 * @param appFile app所在路径
	 * @return 是否安装成功
	 */
	public static boolean installApp(boolean isGrant, File appFile) {
		return installApp(5037, isGrant, appFile);
	}

	/**
	 * 用于通过指定端口的adb服务，卸载设备上指定的app
	 * 
	 * @param port           端口
	 * @param appPackageName app包名
	 * @return 是否卸载成功
	 */
	public static boolean uninstallApp(int port, String appPackageName) {
		// 判断端口号是否合法
		if (port < 0 || port > 65535) {
			throw new IllegalDataException("端口号范围只能是0～65535之间：" + port);
		}

		// 拼接并执行命令
		String result = WindowsCmdUtil.action(String.format("%s -P %d uninstall %s", adbCommand(), port,
				Optional.ofNullable(appPackageName).filter(t -> !t.isEmpty())
						.orElseThrow(() -> new IllegalDataException("错误的app包名：" + appPackageName))));

		// 返回是否安装成功
		return result.contains("Success");
	}

	/**
	 * 用于通过指定端口的adb服务，卸载设备上指定的app
	 * 
	 * @param port    端口
	 * @param appFile app所在路径
	 * @return 是否卸载成功
	 */
	public static boolean uninstallApp(int port, File appFile) {
		return uninstallApp(port, getAppPackageName(Optional.ofNullable(appFile).filter(File::isFile)
				.orElseThrow(() -> new IllegalDataException("错误的App应用路径：" + appFile))));
	}

	/**
	 * 用于通过指定端口的adb服务，卸载设备上指定的app
	 * 
	 * @param appPackageName app包名
	 * @return 是否卸载成功
	 */
	public static boolean uninstallApp(String appPackageName) {
		return uninstallApp(5037, appPackageName);
	}

	/**
	 * 用于通过指定端口的adb服务，卸载设备上指定的app
	 * 
	 * @param appFile app所在路径
	 * @return 是否卸载成功
	 */
	public static boolean uninstallApp(File appFile) {
		return uninstallApp(5037, appFile);
	}

	/**
	 * 用于根据是否指定命令所在位置来返回aapt的命令
	 * 
	 * @return aapt的命令
	 */
	private static String aaptCommand() {
		return aaptFolder == null ? "aapt" : aaptFolder.getAbsolutePath();
	}

	/**
	 * 用于根据是否指定命令所在位置来返回aapt的命令
	 * 
	 * @return aapt的命令
	 */
	private static String adbCommand() {
		return adbFolder == null ? "adb" : adbFolder.getAbsolutePath();
	}
}
