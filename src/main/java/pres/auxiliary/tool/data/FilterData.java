package pres.auxiliary.tool.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilterData {
	/**
	 * 根据关键词，对一组文本进行筛选。通过boolean类型的传参来控制筛选的关键词的且或关系，
	 * 当其为true时，则表示文本需要同时满足所有关键词时才能被返回；当其为false时，则表示只要
	 * 满足其中一个关键词即可被返回。
	 * @param words 文本组
	 * @param judgeFull 是否需要所有关键词全部满足
	 * @param keys 关键词组
	 * @return 筛选后相应的文本组
	 */
	public static String[] filterText(String[] words, boolean judgeFull, String...keys) {
		//转换words数组为List集合
		List<String> wordList = Arrays.asList(words);
		
		//调用重载的方法对词语进行筛选
		wordList = filterText(wordList, judgeFull, keys);
		
		//将集合再转换为数组
		return wordList.toArray(new String[wordList.size()]);
	}
	
	/**
	 * 根据关键词，对一组文本进行筛选。通过boolean类型的传参来控制筛选的关键词的且或关系，
	 * 当其为true时，则表示文本需要同时满足所有关键词时才能被返回；当其为false时，则表示只要
	 * 满足其中一个关键词即可被返回。
	 * @param wordList 文本组
	 * @param judgeFull 是否需要所有关键词全部满足
	 * @param keys 关键词组
	 * @return 筛选后相应的文本组
	 */
	public static List<String> filterText(List<String> wordList, boolean judgeFull, String...keys) {
		return wordList.stream().filter(word -> judgeText(word, judgeFull, keys)).collect(Collectors.toList());
	}
	
	/**
	 * 用于对文本是否包含关键词进行判断
	 * @param text 文本
	 * @param keyFull 是否需要所有关键词全部满足
	 * @param keys 关键词
	 * @return 判断结果
	 */
	private static boolean judgeText(String text, boolean judgeFull, String... keys) {
		//遍历所有的关键词
		for (String key : keys) {
			//判断文本是否包含关键词
			if (text.indexOf(key) > -1) {
				//若文本包含关键词，且keyFull为false，则表示无需判断所有的关键词，但文本包含关键词，可直接结束循环
				if (!judgeFull) {
					return true;
				}
			} else {
				//若文本不包含关键词，且keyFull为true，则表示需要判断所有的关键词，但文本未包含关键词，可直接结束循环
				if (judgeFull) {
					return false;
				}
			}
		}
		
		//若整个循环结束后，方法仍未结束，则只存在以下两种情况：
		//1.keyFull为false，且整个循环下来，文本均为包含关键词，则此时返回false
		//2.keyFull为true，且整个循环下来，文本均包含关键词，则此时返回true
		//根据以上结果，可以得出，循环结束后仍未结束方法的，则可以直接返回keyFull
		return judgeFull;
	}
}
