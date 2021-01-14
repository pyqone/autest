package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomWord {
	/**
	 * 用于存储需要随机返回的词语
	 */
	private ArrayList<String> returnWordList = new ArrayList<>();
	/**
	 * 用于存储默认返回的词语
	 */
	private ArrayList<String> conditionWordList = new ArrayList<>();
	
	/**
	 * 指向生成的词语是否能重复
	 */
	private boolean isRepeat = true;
	
	/**
	 * 用于向无需条件亦可直接返回的词语组中添加词语
	 * @param words 词语组
	 */
	public void addRetuenWord(String...words) {
		returnWordList.addAll(Arrays.asList(words));
	}
	
	/**
	 * 用于向无需条件亦可直接返回的词语组中添加词语
	 * @param wordList 词语集合
	 */
	public void addRetuenWord(ArrayList<String> wordList) {
		returnWordList.addAll(wordList);
	}
	
	/**
	 * 用于向符合条件后返回的词语组中添加词语
	 * @param words 词语组
	 */
	public void addConditionWord(String...words) {
		conditionWordList.addAll(Arrays.asList(words));
	}
	
	/**
	 * 用于向符合条件后返回的词语组中添加词语
	 * @param wordList 词语集合
	 */
	public void addConditionWord(ArrayList<String> wordList) {
		conditionWordList.addAll(wordList);
	}
	
	/**
	 * 用于设置词语是否可以重复生成
	 * @param isRepeat 词语是否可重复
	 */
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	/**
	 * 用于随机生成指定随机个数的词语
	 * @param minLength 最少词语个数
	 * @param maxLength 最多词语个数
	 * @return 生成的词语集合
	 */
	public ArrayList<String> toWord(int minLength, int maxLength) {
		return toWord(getLength(minLength, maxLength));
	}
	
	/**
	 * 用于随机生成指定个数的词语
	 * @param length 输出的词语个数
	 * @return 生成的词语集合
	 */
	public ArrayList<String> toWord(int length) {
		//克隆returnWordList，便于对不能生成重复词语的情况下，移除集合中的元素，达到快速返回的目的
		@SuppressWarnings("unchecked")
		ArrayList<String> wordList = (ArrayList<String>) returnWordList.clone();
		
		//存储生成的词语
		ArrayList<String> randomWordList = new ArrayList<>();
		//循环，生成相应的词语
		for (int i = 0; i < length && wordList.size() > 0; i++) {
			//获取随机下标
			int index = getRandomIndex(wordList);
			//存储下标对应的词语
			randomWordList.add(wordList.get(index));
			//若生成的词语不可重复，则直接移除wordList中对应的词语
			if (!isRepeat) {
				wordList.remove(index);
			}
		}
		
		return randomWordList;
	}
	
	/**
	 * 用于根据词语是否允许重复，返回实际的最大生成词语个数
	 * @param maxLength 设置的最大词语生成个数
	 * @return 实际最大词语生成个数
	 */
	private int getLength(int minLength, int maxLength) {
		//若词语不允许重复生成且minLength大于returnWordList.size()，则将minLength改为returnWordList.size()
		minLength = (!isRepeat && minLength > returnWordList.size()) ? returnWordList.size() : minLength;
		//若词语不允许重复生成且maxLength大于returnWordList.size()，则将maxLength改为returnWordList.size()
		maxLength = (!isRepeat && maxLength > returnWordList.size()) ? returnWordList.size() : maxLength;
		
		//若两个数字一致，则返回任意一个数字
		if (minLength == maxLength) {
			return maxLength;
		}
		
		//为避免最小长度与最大长度传参相反，故此处进行一个调换
		int max = Math.max(minLength, maxLength);
		int min = Math.min(minLength, maxLength);
		
		//返回指定长度下的一个随机长度
		return (new Random().nextInt(max - min + 1) + min);
	}

	/**
	 * 根据指定的集合，返回随机的一个下标
	 * @param wordList 词语集合
	 * @return 集合元素的随机下标
	 */
	private int getRandomIndex(ArrayList<String> wordList) {
		//根据条件获取集合的长度
		int maxLength = wordList.size();
		//生成最大长度范围内的随机数
		return new Random().nextInt(maxLength);
		
	}
}
