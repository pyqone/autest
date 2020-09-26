package pres.auxiliary.tool.file;

import java.util.HashMap;

import pres.auxiliary.tool.file.excel.ReplactFunction;

public abstract class AbstractWriteFile {
	/**
	 * 用于对待替换词语的标记
	 */
	protected final String WORD_SIGN = "\\#";
	
	/**
	 * 用于存储待替换的词语以及被替换的词语
	 */
	protected HashMap<String, ReplactFunction> replaceWordMap = new HashMap<>(16);
	
	/**
	 * 用于设置需要被替换的词语。添加词语时无需添加特殊字符
	 * 
	 * @param word        需要替换的词语
	 * @param replactWord 被替换的词语
	 */
	public void setReplactWord(String word, String replactWord) {
			setReplactWord(word, (text) -> {
			return replactWord;
		});
	}
	
	/**
	 * 用于根据需要替换的词语，设置需要动态写入到文本的内容。添加词语时无需添加特殊字符
	 * @param word 需要替换的词语
	 * @param replactFunction 替换规则
	 */
	public void setReplactWord(String word, ReplactFunction replactFunction) {
		replaceWordMap.put(word, replactFunction);
	}
	
	/**
	 * 用于对需要进行替换的特殊词语进行替换
	 * @param contents 文本内容
	 */
	protected String[] replaceWord(String[] contents) {
		// 查找特殊词语，并对词语进行替换
		for (String word : replaceWordMap.keySet()) {
			// 查找所有的0内容，并将特殊词语进行替换
			for (int i = 0; i < contents.length; i++) {
				String regex = WORD_SIGN + word + WORD_SIGN;
				contents[i] = contents[i].replaceAll(regex, replaceWordMap.get(word).replact(word));
			}
		}
		
		return contents;
	}
}
