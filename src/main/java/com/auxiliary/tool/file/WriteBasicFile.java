package com.auxiliary.tool.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataDriverFunction.FunctionExceptional;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;
import com.auxiliary.testcase.templet.LabelNotFoundException;
import com.auxiliary.tool.file.excel.AbstractWriteExcel.Field;

public abstract class WriteBasicFile<T extends WriteBasicFile<T>> {
	protected final String KEY_CONTENT = "content";
	protected final String KEY_TEXT = "text";

	/**
	 * 存储模板文件的构造类
	 */
	protected FileTemplet templet;

	/**
	 * 待替换词语的标记
	 */
	protected final String WORD_SIGN = "#";

	/**
	 * 存储当前需要写入到文件的内容json串
	 */
	protected JSONObject contentJson = new JSONObject();
	/**
	 * 存储单条内容写入到内容json串中的内容
	 */
	protected JSONObject caseJson = new JSONObject();
	/**
	 * 存储字段默认内容
	 */
	protected JSONObject defaultCaseJson = new JSONObject();

	/**
	 * 存储待替换的词语以及被替换的词语
	 */
	protected HashMap<String, DataFunction> replaceWordMap = new HashMap<>(16);

	/**
	 * 构造对象，初始化创建文件的模板
	 * 
	 * @param templet 模板类对象
	 */
	public WriteBasicFile(FileTemplet templet) {
		this.templet = templet;

		contentJson.put(KEY_CONTENT, new JSONArray());
	}

	/**
	 * 用于设置需要被替换的词语
	 * 
	 * @param word        需要替换的词语
	 * @param replactWord 被替换的词语
	 * @throws FunctionExceptional 未指定替换词语或替换内容时抛出的异常
	 */
	public void setReplactWord(String word, String replactWord) {
		setReplactWord(new DataDriverFunction(word, text -> replactWord));
	}

	/**
	 * 用于根据需要替换的词语，设置需要动态写入到文本的内容。添加词语时无需添加特殊字符
	 * <p>
	 * <b>注意：</b>当未传入关键词，或关键词为空，或处理方式为null时，则不存储替换方式
	 * </p>
	 * 
	 * @param word            需要替换的词语
	 * @param replactFunction 替换规则
	 * @throws FunctionExceptional 未指定替换词语或替换内容时抛出的异常
	 * @deprecated 该方法已被{@link #setReplactWord(DataDriverFunction)}方法代替，可将方法改为
	 * {@code setReplactWord(new DataDriverFunction(word, functions))}。该方法将在后续两个版本中删除
	 */
	@Deprecated
	public void setReplactWord(String word, DataFunction functions) {
		if (!Optional.ofNullable(word).filter(t -> !t.isEmpty()).isPresent()) {
			return;
		}

		if (functions == null) {
			return;
		}

		replaceWordMap.put(word, functions);
	}
	
	/**
	 * 用于添加匹配公式与处理方式
	 * <p>
	 * 该方法允许添加待替换词语的处理方式，在写入用例时，若指定的待替换内容符合此方法指定的正则时，则会使用存储的替换方式，
	 * 对词语进行替换。例如：
	 * <code><pre>
	 * {@literal @}Test
	 * public void setReplactWordTest_DataDriverFunction() {
	 * 	// 定义词语匹配规则和处理方式，当匹配到正则后，将获取“随机：”后的字母
	 *	// 若字母为“N”，则随机生成两位数字字符串
	 * 	// 若字母为“Y”，则随机生成两位中文字符串
	 *	test.setReplactWord(new DataDriverFunction("随机：[NC]", text -> {
	 *		return "N".equals(text.split("：")[1]) ? RandomString.randomString(2, 2, StringMode.NUM)
	 *				: RandomString.randomString(2, 2, StringMode.CH);
	 *	}));
	 *	
	 *	// 随机生成两位数字
	 *	test.addContent("title", "内容：#随机：N#").end();
	 *	// 随机生成两位中文
	 *	test.addContent("title", "内容：#随机：C#").end();
	 *	
	 * 	// 控制台输出生成的内容json串
	 *	System.out.println(test.toWriteFileJson());
	 * }
	 * </pre></code>
	 * </p>
	 * <p>
	 * 部分定义方法可调用工具类{@link Functions}类获取
	 * </p>
	 * 
	 * @param functions
	 * @throws FunctionExceptional 未指定替换词语或替换内容时抛出的异常
	 */
	public void setReplactWord(DataDriverFunction functions) {
		if (functions == null) {
			return;
		}
		
		replaceWordMap.put(functions.getRegex(), functions.getFunction());
	}

	/**
	 * 用于向字段中添加默认内容
	 * <p>
	 * 当完成一条用例编写后，若未写入存在默认内容的字段时，则在完成用例后自动将默认内容写入字段中。 
	 * 若字段存在内容，则不会写入默认值
	 * </p>
	 * <p>
	 * <b>注意：</b>若模板中不存在指定的字段，则不写入该默认值
	 * </p>
	 * 
	 * @param field    字段
	 * @param contents 默认内容
	 */
	public void setFieldValue(String field, String... contents) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.contains(field)) {
			return;
		}

		// 若未传值或传入null，则不进行操作
		if (contents == null || contents.length == 0) {
			return;
		}

		// 获取字段指向的用例内容
		JSONArray defaultListJson = Optional.ofNullable(defaultCaseJson.getJSONArray(field)).orElse(new JSONArray());

		// 查找特殊词语，并对词语进行替换，并将内容写入到字段中
		Arrays.stream(contents).map(this::replaceWord).map(text -> {
			JSONObject fieldJson = new JSONObject();
			fieldJson.put(KEY_TEXT, text);

			return fieldJson;
		}).forEach(defaultListJson::add);

		defaultCaseJson.put(field, defaultListJson);
	}

	/**
	 * 通过传入的字段id，将对应的字段内容写入到用例最后的段落中，字段id对应xml配置文件中的单元格标签的id属性。
	 * 若需要使用替换的词语，则需要使用“#XX#”进行标记，如传参：<br>
	 * testing<br>
	 * 需要替换其中的“ing”，则传参：<br>
	 * test#ing#<br>
	 * 添加数据时，其亦可对存在数据有效性的数据进行转换，在传值时，只需要传入相应的字段值即可，例如：<br>
	 * 当字段存在两个数据有效性：“测试1”和“测试2”时，则，可传入addContent(..., "1")（注意，下标从1开始），
	 * 此时，文件中该字段的值将为“测试1”，若传入的值无法转换成数字，则直接填入传入的内容，具体说明可以参见{@link Field#getDataValidation(int)}。
	 * 
	 * @param field    字段id
	 * @param contents 相应字段的内容
	 * @return 类本身，以方便链式编码
	 * @throws LabelNotFoundException 当在sheet标签中查不到相应的单元格id不存在时抛出的异常
	 */
	@SuppressWarnings("unchecked")
	public T addContent(String field, String... contents) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.contains(field)) {
			return (T) this;
		}

		// 若未传值或传入null，则不进行操作
		if (contents == null || contents.length == 0) {
			return (T) this;
		}

		// 获取字段指向的用例内容
		JSONArray contentListJson = Optional.ofNullable(caseJson.getJSONArray(field)).orElse(new JSONArray());

		// 查找特殊词语，并对词语进行替换，并将内容写入到字段中
		Arrays.stream(contents).map(this::replaceWord).map(text -> {
			JSONObject fieldJson = new JSONObject();
			fieldJson.put(KEY_TEXT, text);

			return fieldJson;
		}).forEach(contentListJson::add);

		// 将内容写入到用例数据中
		caseJson.put(field, contentListJson);

		return (T) this;
	}

	/**
	 * 标记完成一条用例数据的编写。
	 * <p>
	 * 调用该方法后，则将数据的缓存写入到文本的缓存内容中，以表示当前段落数据编写完成， 之后再次调用添加数据方法时，则写入一段新的内容。
	 * </p>
	 * 
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T end() {
		// 将默认值附加到字段上
		replenishDefaultContent();

		// 获取内容字段的数组，并将用例写入到文本中
		JSONArray contentListJson = Optional.ofNullable(contentJson.getJSONArray(KEY_CONTENT)).orElse(new JSONArray());
		contentListJson.add(JSONObject.parse(caseJson.toJSONString()));
		contentJson.put(KEY_CONTENT, contentListJson);
		
		caseJson.clear();
		return (T) this;
	}

	/**
	 * 用于返回主体json串
	 * <p>
	 * 在主体json串中，包括文件模板内容、默认值内容以及需要写入到文件中的内容，可通过该json串对文件写入类进行构造
	 * </p>
	 * 
	 * @return 主体json串
	 */
	public String toWriteFileJson() {
		JSONObject mainjson = new JSONObject();
		mainjson.put("templet", JSONObject.parse(templet.getTempletJson()));

		JSONObject dataJson = new JSONObject();
		dataJson.put("default", defaultCaseJson);
		dataJson.put("case", contentJson);

		mainjson.put("data", dataJson);

		return mainjson.toJSONString();
	}
	
	/**
	 * 用于将编写的内容写入到文件中
	 */
	public abstract void write();

	/**
	 * 用于在用例中补充默认的字段内容，若用例存在内容，则不添加默认值
	 */
	protected void replenishDefaultContent() {
		// 遍历defaultCaseJson，判断是否存在caseJson中不存在的字段
		for (String key : defaultCaseJson.keySet()) {
			if (!caseJson.containsKey(key)) {
				caseJson.put(key, defaultCaseJson.getJSONArray(key));
			}
		}
	}

	/**
	 * 用于对当前文本内容中的词语进行提取，并返回替换后的内容
	 * 
	 * @param content 文本内容
	 * @return 替换词语后的文本内容
	 */
	protected String replaceWord(String content) {
		//获取内容中的待替换词语
		ArrayList<String> wordList = getReplaceWord(content);
		
		//循环，遍历待替换词语，并对内容进行替换
		for (String word : wordList) {
			//将词语与每一个规则进行匹配
			for (String key : replaceWordMap.keySet()) {
				// 若传入的内容与正则匹配，则将数据进行处理，并返回处理结果
				if (Pattern.compile(key).matcher(word).matches()) {
					//将待替换的词语进行拼装
					String oldWord = WORD_SIGN + word + WORD_SIGN;
					//获取替换的词语
					String newWord = replaceWordMap.get(key).apply(word);
					//循环，替换所有与oldWord相同的内容
					//由于oldWord可能包含括号等特殊字符，故不能使用replaceAll方法进行替换
					while(content.contains(oldWord)) {
						int startIndex = content.indexOf(oldWord);
						int endIndex = startIndex + oldWord.length();
						content = content.substring(0, startIndex) + newWord + content.substring(endIndex);
					}
				}
			}
		}

		return content;
	}

	/**
	 * 用于获取内容中的所有的带替换符号的词语
	 * 
	 * @param content 文本内容
	 * @return 替换的词语集合
	 */
	protected ArrayList<String> getReplaceWord(String content) {
		ArrayList<String> wordList = new ArrayList<>();
		// 获取当前数据中是否存在符号
		int index = content.indexOf("#");
		// 循环，判断是否存在标记符号
		while (index > -1) {
			// 存在编号，则将内容按第一个符号切分，使其不包括第一个符号
			content = content.substring(index + 1);
			// 再次获取符号所在的编号
			index = content.indexOf("#");

			// 若存在符号，则获取符号后的词语，到当前index位置
			if (index > -1) {
				wordList.add(content.substring(0, index));

				// 再次按照符号切分，并获取index
				content = content.substring(index + 1);
				index = content.indexOf("#");
			}
		}

		return wordList;
	}
}
