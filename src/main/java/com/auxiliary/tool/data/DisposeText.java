package com.auxiliary.tool.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>DisposeText.java
 * </p>
 * <p>
 * <b>用途：</b>用于对文件中的文本进行处理，以简化日常工作中对文本的内容的测试
 * </p>
 * <p>
 * <b>编码时间：</b>2019年7月4日 07:08
 * </p>
 * <p>
 * <b>修改时间：</b>2019年7月11日 09:12
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DisposeText {
	/**
	 * 用于将两个词语集合进行比较，并返回待测集合不在目标集合中的元素
	 * 
	 * @param testList   待测试集合
	 * @param targetList 目标集合
	 * @return 待测集合不在目标集合中的元素
	 * @throws IllegalDataException 未指定集合时抛出的异常
	 */
	public static List<String> compareFileWord(List<String> testList, List<String> targetList) {
		// 判断是否传入集合
		Optional.ofNullable(testList).filter(list -> !list.isEmpty())
				.orElseThrow(() -> new IllegalDataException("未指定待测试集合：" + testList));
		Optional.ofNullable(targetList).filter(list -> !list.isEmpty())
				.orElseThrow(() -> new IllegalDataException("未指定目标集合：" + targetList));

		List<String> compareWordList = new ArrayList<>();
		testList.stream()
				// 过滤为null的数据
				.filter(str -> str != null)
				// 过滤不在targetList中的数据
				.filter(str -> !targetList.contains(str))
				// 过滤掉已存在于compareWordList中的数据
				.filter(str -> !compareWordList.contains(str)).forEach(compareWordList::add);

		return compareWordList;
	}

	/**
	 * 该方法用于对文本进行去重，保留不重复的字符
	 * 
	 * @param textFile 存储文本的文件
	 * @return 去重后的文本
	 * @throws IOException
	 */
	public static String textDelDuplication(String text) {
		// 存储文件中的文本
		StringBuilder newText = new StringBuilder();
		// 获取并按字符对字符串进行返回
		Optional.ofNullable(text).orElse("").chars()
				// 将字符转换为字符串
				.mapToObj(ch -> String.valueOf(((char) ch)))
				// 过滤掉已在newText中的数据，并进行存储
				.filter(str -> newText.indexOf(str) < 0).forEach(newText::append);

		return newText.toString();
	}

	/**
	 * 用于对文本按照切分符多次切分，返回最终的切分结果
	 * <p>
	 * <b>注意：</b>切分符必须使用正则表达式的写法，例如按照“.”进行切分，则必须传入“\\.”
	 * </p>
	 * @param text 需要切分的内容
	 * @param regexs 切分符号组
	 * @return 最终切分后的结果
	 */
	public static List<String> splitText(String text, String... regexs) {
		List<String> wordList = new ArrayList<>();
		// 若text存在且不为空，则执行切分
		Optional.ofNullable(text).filter(str -> !str.isEmpty()).ifPresent(str -> {
			wordList.add(str);
			//遍历切分符号集合
			Arrays.stream(Optional.ofNullable(regexs).orElseThrow(() -> new IllegalDataException("未指定切分方式")))
					.forEach(regex -> {
						ArrayList<String> tempWordList = new ArrayList<>();
						//遍历wordList中的所有内容，并对内容一一按照当前的切分符号进行切分
						wordList.stream().map(word -> word.split(regex))
								//将且分后得到的数组转换为集合后并存储
								.map(Arrays::asList).forEach(tempWordList::addAll);
						
						//清空wordList集合，并存储临时结合中的所有元素
						wordList.clear();
						wordList.addAll(tempWordList);
					});
		});

		return wordList;
	}

	/**
	 * 该方法用于对文本中单词进行去重，输出不重复单词
	 * 
	 * @param testFile 待测文件
	 * @return 去重后的单词数组
	 */
	public static List<String> wordDelDuplication(List<String> textList) {
		List<String> wordList = new ArrayList<>();
		Optional.ofNullable(textList).filter(list -> !list.isEmpty()).ifPresent(list -> {
			list.stream().filter(text -> !wordList.contains(text)).forEach(wordList::add);
		});
		
		return wordList;
	}

	/**
	 * 用于统计每个内容在集合中出现的次数
	 * 
	 * @param wordList 需要统计的集合
	 * @return 统计结果
	 */
	public static LinkedHashMap<String, Integer> statistics(List<String> wordList) {
		LinkedHashMap<String, Integer> result = new LinkedHashMap<>(16);

		// 存储不重复的单词，并存储其在文中出现的次数
		Optional.ofNullable(wordList).filter(list -> !list.isEmpty()).ifPresent(list -> {
			list.forEach(element -> {
				// 判断map是否已经存在该词语，若存在，则将key对应的value加上1
				if (result.containsKey(element)) {
					result.put(element, result.get(element) + 1);
				} else {
					result.put(element, 1);
				}
			});
		});

		return result;
	}
}
