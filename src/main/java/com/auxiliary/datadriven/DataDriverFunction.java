package com.auxiliary.datadriven;

import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>DataDriverFunction.java
 * </p>
 * <p>
 * <b>用途：</b> 用于存储数据驱动中处理数据的公式
 * <p>
 * 可使用lambda表达式来简化添加公式的写法，例如，在集合中添加一个重复生成指定个数字符的方法，则可写作如下的形式：
 * 
 * <pre>
 * <code>
 * ArrayList<DataDriverFunction> funList = new ArrayList<>();
 * funList.add(new DataDriverFunction(".*\\d", text -> {
 *      String[] dataTexts = text.split("\\*");
 *      StringBuilder newText = new StringBuilder();
 *      
 *      for (int i = 0; i < Integer.valueOf(dataTexts[1]); i++) {
 *          newText.append(dataTexts[0]);
 *      }
 *      
 *      return newText.toString();
 *  }));
 * </code>
 * </pre>
 * </p>
 * </p>
 * <p>
 * <b>编码时间：</b>2020年6月12日上午8:27:04
 * </p>
 * <p>
 * <b>修改时间：</b>2020年6月12日上午8:27:04
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class DataDriverFunction {
	/**
	 * 用于存储正则表达式
	 */
	private String regex;
	
	/**
	 * 用于存储处理数据的
	 */
	private DataFunction function;
	
	/**
	 * 初始化数据
	 * @param regex 正则表达式
	 * @param function 数据对应的处理方法
	 * @throws FunctionExceptional 未指定公式匹配规则或公式时抛出的异常
	 */
	public DataDriverFunction(String regex, DataFunction function) {
		this.regex = Optional.ofNullable(regex).filter(re -> !re.isEmpty()).orElseThrow(FunctionExceptional::new);
		this.function = Optional.ofNullable(function).orElseThrow(FunctionExceptional::new);
	}

	/**
	 * 返回正则表达式
	 * @return 正则表达式
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * 返回数据处理的方法
	 * @return 数据处理的方法
	 */
	public DataFunction getFunction() {
		return function;
	}
	
	/**
	 * 用于将公式关键词与存储的正则表达式进行匹配，返回匹配的结果
	 * @param key 公式关键词
	 * @return 与正则表达式匹配的结果
	 */
	public boolean matchRegex(String key) {
		return key.matches(regex);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((regex == null) ? 0 : regex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		DataDriverFunction other = (DataDriverFunction) obj;
		if (regex == null) {
			if (other.regex != null) {
                return false;
            }
		} else if (!regex.equals(other.regex)) {
            return false;
        }
		return true;
	}

	@Override
	public String toString() {
		return getRegex();
	}
	
	/**
	 * <p><b>文件名：</b>DataDriverFunction.java</p>
	 * <p><b>用途：</b>
	 * 定义公式指定有误时抛出的异常
	 * </p>
	 * <p><b>编码时间：</b>2021年3月24日上午7:35:35</p>
	 * <p><b>修改时间：</b>2021年3月24日上午7:35:35</p>
	 * @author 彭宇琦
	 * @version Ver1.0
	 * @since JDK 1.8
	 */
	public class FunctionExceptional extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public FunctionExceptional() {
			this("未指定公式名称或公式本体");
		}

		public FunctionExceptional(String message, Throwable cause) {
			super(message, cause);
		}

		public FunctionExceptional(String message) {
			super(message);
		}
	}
}
