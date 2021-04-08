package com.auxiliary.tool.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * <p>
 * <b>文件名：</b>WindowsCmdUtil.java
 * </p>
 * <p>
 * <b>用途：</b> 定义Windows操作系统下调用dos命令的工具
 * </p>
 * <p>
 * <b>编码时间：</b>2021年4月2日下午6:34:55
 * </p>
 * <p>
 * <b>修改时间：</b>2021年4月2日下午6:34:55
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class WindowsCmdUtil {
	/**
	 * 私有构造
	 */
	private WindowsCmdUtil() {
	}

	/**
	 * 用于执行dos命令，并返回执行的输出
	 * 
	 * @param cmd dos命令
	 * @return 执行成功的输出
	 * @throws ActionException 未指定命令或命令执行出错时抛出的异常
	 */
	public static String action(String cmd) {
		return action(cmd, true);
	}

	/**
	 * 用于执行dos命令，并根据搜索条件，返回执行的输出
	 * <p>
	 * <b>注意：</b>执行异常时，其返回信息也对关键词进行搜索
	 * </p>
	 * 
	 * @param cmd    dos命令
	 * @param isFull 是否需要包含所有关键词
	 * @param keys   关键词组
	 * @return 指定条件的执行成功输出
	 * @throws ActionException 未指定命令或命令执行出错时抛出的异常
	 */
	public static String action(String cmd, boolean isFull, String... keys) {
		Optional.ofNullable(cmd).filter(c -> !c.isEmpty()).orElseThrow(() -> new ActionException("需要指定执行命令"));

		Process result;
		try {
			result = Runtime.getRuntime().exec(cmd);
		} catch (IOException e1) {
			throw new ActionException("无法执行命令：" + cmd, e1);
		}

		// 获取命令的执行结果
		String resultText = getResult(result.getInputStream(), isFull,
				Optional.ofNullable(keys).orElse(new String[] {}));
		// 若获取到的执行结果为空，则输出执行异常结果
		return resultText.isEmpty()
				? getResult(result.getErrorStream(), isFull, Optional.ofNullable(keys).orElse(new String[] {}))
				: resultText;
	}

	/**
	 * 用于对命令的结果进行处理，根据关键词，存储包含关键词的输出
	 * 
	 * @param is     流
	 * @param isFull 是否包含所有关键
	 * @param keys   关键词组
	 * @return 包含关键词的输出，多行输出以“\n”分隔
	 */
	private static String getResult(InputStream is, boolean isFull, String... keys) {
		StringJoiner resultText = new StringJoiner("\n");

		String text = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			// 循环，读取所有的返回
			while ((text = br.readLine()) != null) {
				boolean isResult = true;

				// 与所有的关键词进行判断
				for (String key : keys) {
					// 根据是否需要全包含关键词来存储数据
					if (isFull) {
						// 若需要全判断，且文本包含关键词，则设置存储结果为true，并继续判断下一个关键词；
						// 反之，设置结果为false，并结束关键词判断
						if (text.contains(key)) {
							isResult = true;
						} else {
							isResult = false;
							break;
						}
					} else {
						// 若不需要全判断，且文本包含关键词，则设置存储结果为true，并结束关键词判断；
						// 反之，设置结果为false，并继续判断下一个关键词
						if (text.contains(key)) {
							isResult = true;
							break;
						} else {
							isResult = false;
						}
					}
				}

				if (isResult) {
					resultText.add(text);
				}
			}
		} catch (Exception e) {
			return "";
		}

		return resultText.toString();
	}

	/**
	 * <p>
	 * <b>文件名：</b>WindowsCmdUtil.java
	 * </p>
	 * <p>
	 * <b>用途：</b> 定义执行命令失败时抛出的异常
	 * </p>
	 * <p>
	 * <b>编码时间：</b>2021年4月2日下午7:27:08
	 * </p>
	 * <p>
	 * <b>修改时间：</b>2021年4月2日下午7:27:08
	 * </p>
	 * 
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	static class ActionException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ActionException() {
			super();
		}

		public ActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public ActionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ActionException(String message) {
			super(message);
		}

		public ActionException(Throwable cause) {
			super(cause);
		}

	}
}
