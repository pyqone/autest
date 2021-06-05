package com.auxiliary.tool.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.datadriven.DataDriverFunction;
import com.auxiliary.datadriven.DataDriverFunction.FunctionExceptional;
import com.auxiliary.datadriven.DataFunction;
import com.auxiliary.datadriven.Functions;

/**
 * <p>
 * <b>文件名：</b>WriteTempletFile.java
 * </p>
 * <p>
 * <b>用途：</b> 快速写入模板型的文件进行的工具。可通过类中提供的词语替换和默认内容的方法，对一类模板型的文件内容（如，测试用例）
 * 进行快速写作，提高工作效率。并且，若存在内容相同，但模板不同的文件，还可通过调用{@link #toWriteFileJson()}方法
 * 以完成各个子类之间的消息互通，达到快速生成多种同一内容，不同存放文件的目的。
 * </p>
 * <p>
 * 在注释中，存在以下名词需要解释：
 * <ul>
 * <li><b>用例</b>：写入到模板文件中的一条数据，类似于一条测试用例</li>
 * <li><b>内容</b>：写入到模板文件中的所有数据，类似于写在测试用例文件中的所有测试用例</li>
 * </ul>
 * </p>
 * <p>
 * <b>编码时间：</b>2021年5月15日上午11:13:26
 * </p>
 * <p>
 * <b>修改时间：</b>2021年5月15日上午11:13:26
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T> 子类
 */
public abstract class WriteTempletFile<T extends WriteTempletFile<T>> {
	public static final String KEY_CONTENT = "content";
	public static final String KEY_TEXT = "text";
	public static final String KEY_DEFAULT = "default";
	public static final String KEY_CASE = "case";
	public static final String KEY_TEMPLET = "templet";
	public static final String KEY_DATA = "data";

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
	 * 指向当前需要编写的用例下标
	 */
	protected int caseIndex = -1;
	/**
	 * 存储需要分页的行数
	 */
	protected int writeRowNum = 0;
	/**
	 * 存储当前已写入的行数
	 */
	protected int nowRowNum = 0;

	/**
	 * 构造对象，初始化创建文件的模板
	 * 
	 * @param templet 模板类对象
	 */
	public WriteTempletFile(FileTemplet templet) {
		this();
		this.templet = templet;
	}

	/**
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * 
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteTempletFile(WriteTempletFile<?> writeTempletFile) {
		// 若传入的文件写入类为空，则抛出异常
		if (writeTempletFile == null) {
			throw new WriteFileException("未指定文件写入类对象，无法进行构造");
		}

		this.templet = writeTempletFile.templet;
		this.contentJson = writeTempletFile.contentJson;
		this.defaultCaseJson = writeTempletFile.defaultCaseJson;
		this.replaceWordMap = writeTempletFile.replaceWordMap;
	}
	
	/**
	 * 无参构造，方便子类进行特殊的构造方法
	 */
	protected WriteTempletFile() {
		contentJson.put(KEY_CASE, new JSONArray());
	}

	/**
	 * 设置需要被替换的词语以及替换的内容
	 * <p>
	 * 在调用{@link #addContent(String, String...)}等方法编写内容时，用“#xx#”来表示待替换的词语，
	 * </p>
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
	 *             {@code setReplactWord(new DataDriverFunction(word, functions))}。该方法将在后续两个版本中删除
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
	 * 该方法允许添加待替换词语的处理方式，在写入用例时，若指定的待替换内容符合此方法指定的正则时，则会使用存储的替换方式， 对词语进行替换。例如：
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
	 * 当完成一条用例编写后，若未写入存在默认内容的字段时，则在完成用例后自动将默认内容写入字段中。 若字段存在内容，则不会写入默认值
	 * </p>
	 * <p>
	 * <b>注意：</b>若模板中不存在指定的字段，则不写入该默认值；每次重新编写字段默认值时，会覆盖上次编写的内容
	 * </p>
	 * 
	 * @param field    字段
	 * @param contents 默认内容
	 */
	public void setFieldValue(String field, String... contents) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.containsField(field)) {
			return;
		}

		// 若未传值或传入null，则不进行操作
		if (contents == null || contents.length == 0) {
			return;
		}

		// 定义新的内容集合
		JSONArray defaultListJson = new JSONArray();

		// 查找特殊词语，并对词语进行替换，并将内容写入到字段中
		Arrays.stream(contents).map(this::replaceWord).map(text -> {
			JSONObject fieldJson = new JSONObject();
			fieldJson.put(KEY_TEXT, text);

			return fieldJson;
		}).forEach(defaultListJson::add);
		
		JSONObject fieldJson = new JSONObject();
		fieldJson.put(KEY_DATA, defaultListJson);
		defaultCaseJson.put(field, fieldJson);
	}

	/**
	 * 用于设置
	 * 
	 * @param writeRowNum
	 */
	public void setWriteRowNum(int writeRowNum) {
		this.writeRowNum = writeRowNum < 0 ? 0 : writeRowNum;
	}

	public void clearFieldValue(String field) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.containsField(field)) {
			return;
		}

		defaultCaseJson.remove(field);
	}

	/**
	 * 用于获取指定下标的用例内容，并对内容进行重写
	 * <p>
	 * 调用该方法后，当前编写的用例将指向为指定下标的用例。若当前正在编写用例，调用该方法后，将覆盖当前编写的用例。
	 * </p>
	 * <p>
	 * <b>注意：</b>若下标为负数或大于当前内容最大个数时，则不获取指定内容。下标从0开始计算，及0表示第一条用例
	 * </p>
	 * 
	 * @param index 用例下标
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T getCase(int index) {
		if (index < 0) {
			return (T) this;
		}

		// 获取用例集合
		JSONArray caseListJson = contentJson.getJSONArray(KEY_CASE);
		// 判断下标指向的集合内容是否存在，不存在，则不进行获取
		if (index >= caseListJson.size()) {
			return (T) this;
		}

		// 设置当前用例下标为指定的下标
		caseIndex = index;
		// 获取下标对应的用例内容
		caseJson = caseListJson.getJSONObject(caseIndex);

		return (T) this;
	}

	/**
	 * 根据传入的字段信息，将指定的内容插入到用例相应字段的最后一行
	 * <p>
	 * 方法允许传入多条内容，每条内容在写入到文件时，均以换行符隔开。
	 * </p>
	 * 
	 * @param field    字段id
	 * @param contents 相应字段的内容
	 * @return 类本身
	 */
	public T addContent(String field, String... contents) {
		return addContent(field, -1, contents);
	}

	/**
	 * 根据传入的字段信息，将指定的内容插入到用例相应字段的指定下标下
	 * <p>
	 * 方法允许传入多条内容，每条内容在写入到文件时，均以换行符隔开。若指定的下标小于0或大于当前内容的最大个数时，则将内容写入到集合最后
	 * </p>
	 * 
	 * @param field    字段id
	 * @param index    指定插入的位置
	 * @param contents 相应字段的内容
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T addContent(String field, int index, String... contents) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.containsField(field)) {
			return (T) this;
		}

		// 若未传值或传入null，则不进行操作
		if (contents == null || contents.length == 0) {
			return (T) this;
		}

		// 获取字段指向的用例内容
		JSONObject fieldJson = Optional.ofNullable(caseJson.getJSONObject(field)).orElse(new JSONObject());
		JSONArray contentListJson = Optional.ofNullable(fieldJson.getJSONArray(KEY_DATA)).orElse(new JSONArray());

		// 查找特殊词语，并对词语进行替换，并将内容写入到字段中
		Arrays.stream(contents).map(this::replaceWord).map(text -> {
			JSONObject fieldTextJson = new JSONObject();
			fieldTextJson.put(KEY_TEXT, text);
			return fieldTextJson;
		}).forEach(json -> {
			// 判断传入的下标是否符合当前内容集合的个数限制，不符合，则将内容写入到最后一行
			if (index < 0 || index >= contentListJson.size()) {
				contentListJson.add(json);
			} else {
				contentListJson.add(index, json);
			}
		});

		// 将内容写入到用例数据中
		fieldJson.put(KEY_DATA, contentListJson);
		caseJson.put(field, fieldJson);

		return (T) this;
	}

	/**
	 * 用于清除用例字段下指定下标段落的内容
	 * 
	 * @param field 字段
	 * @param index 段落下标
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T clearContent(String field, int index) {
		// 判断字段是否存在，若不存在，则不进行操作
		if (!templet.containsField(field)) {
			return (T) this;
		}

		if (index < 0) {
			return (T) this;
		}

		// 获取字段指向的用例内容
		if (caseJson.containsKey(field)) {
			JSONArray contentListJson = caseJson.getJSONObject(field).getJSONArray(KEY_DATA);
			// 判断字段内容是否为空
			if (contentListJson != null && !contentListJson.isEmpty()) {
				// 若指定的下标小于内容集合的个数，则移除相应的内容
				if (index < contentListJson.size()) {
					contentListJson.remove(index);
				}
			}
		}

		return (T) this;
	}

	/**
	 * 用于移除指定下标的一条用例
	 * @param index 用例下标
	 */
	public void clearCase(int index) {
		if (index < 0) {
			return;
		}

		// 获取用例集合
		JSONArray caseListJson = contentJson.getJSONArray(KEY_CASE);
		// 判断下标指向的集合内容是否存在，不存在，则不进行获取
		if (index >= caseListJson.size()) {
			return;
		}

		caseListJson.remove(index);
	}

	/**
	 * 标记完成一条用例数据的编写，并将内容插入到最后一行内容下
	 * <p>
	 * 调用该方法后，则将数据的缓存写入到文本的缓存内容中，以表示当前段落数据编写完成， 之后再次调用添加数据方法时，则写入一段新的内容。
	 * </p>
	 * 
	 * @return 类本身
	 */
	public T end() {
		return end(Integer.MAX_VALUE);
	}

	/**
	 * 标记完成一条用例数据的编写，并将内容插入到指定行内容下
	 * <p>
	 * 调用该方法后，则将数据的缓存写入到文本的缓存内容中，以表示当前段落数据编写完成， 之后再次调用添加数据方法时，则写入一段新的内容。
	 * </p>
	 * <p>
	 * <b>注意：</b>若调用了{@link #getCase(int)}方法覆盖指定行内容的用例时，则传入的下标无效
	 * </p>
	 * 
	 * @param contentIndex 内容下标
	 * @return 类本身
	 */
	@SuppressWarnings("unchecked")
	public T end(int contentIndex) {
		// 将默认值附加到字段上
		replenishDefaultContent();

		// 获取内容字段的数组，并将用例写入到文本中
		JSONArray contentListJson = Optional.ofNullable(contentJson.getJSONArray(KEY_CASE)).orElse(new JSONArray());
		// 将caseJson中存储的内容重新放入到一个json中

		// 判断当前用例下标是否为-1，为-1，则记录到内容中的最后一条；反之，则使用当前内容进行覆盖
		if (caseIndex == -1) {
			// 判断传入的内容下标是否正确，不正确，则对下标进行修正
			// 由于fastjson无法直接存储caseJson（清空caseJson时，其存储的内容也被清除），故先转成字符串后再用json进行解析
			if (contentIndex < 0) {
				contentListJson.add(0, JSONObject.parse(caseJson.toJSONString()));
			} else if (contentIndex >= 0 && contentIndex < contentListJson.size()) {
				contentListJson.add(contentIndex, JSONObject.parse(caseJson.toJSONString()));
			} else {
				contentListJson.add(JSONObject.parse(caseJson.toJSONString()));
			}
		} else {
			contentListJson.set(caseIndex, JSONObject.parse(caseJson.toJSONString()));
		}

		// 将用例集合重新添加至内容json中
		contentJson.put(KEY_CASE, contentListJson);

		// 若当前指定了分行写入文件，则判断当前行数是否需要分行写入文件
		if (writeRowNum != 0 && contentListJson.size() % writeRowNum == 0) {
			write();
			// 指定当前写入的行为当前内容的行数
			nowRowNum = contentListJson.size();
		}

		// 清除用例json中的内容，并指定用例下标为-1
		caseJson.clear();
		caseIndex = -1;

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
		mainjson.put(KEY_DEFAULT, defaultCaseJson);
		mainjson.put(KEY_CONTENT, contentJson);

		return mainjson.toJSONString();
	}

	/**
	 * 用于将编写的所有内容写入到文件中
	 */
	public void write() {
		// 若分页行数不为0，则获取当前行数作为编写的起始行数
		int startIndex = 0;
		if (writeRowNum != 0) {
			startIndex = nowRowNum;
		}

		write(startIndex, -1);
	}

	/**
	 * 用于将编写的部分内容写入到文件中
	 * <p>
	 * 方法支持反序遍历，即指定的下标为负数时，则表示反序遍历用例集，至指定位置的用例。若两个下标一致，则不处理数据
	 * </p>
	 * 
	 * @param caseStartIndex 写入文件开始下标
	 * @param caseEndIndex   写入文件结束下标
	 */
	protected abstract void write(int caseStartIndex, int caseEndIndex);
	
	protected abstract void contentWriteTemplet(FileTemplet templet, int caseStartIndex, int caseEndIndex);

	/**
	 * 用于根据当前实际的模板数量，返回拼接后的模板json内容
	 * <p>
	 * 由于部分写入类可传入多个模板，为方便构造，则需要对所有模板的json进行返回
	 * </p>
	 * 
	 * @return 拼接所有模板json后得到的模板json内容
	 */
	public String toTempletJson() {
		// 转换模板Json，并进行存储
		JSONArray templetJsonList = new JSONArray();
		// 若转换方法返回为null，则构造一个空串
		Optional.ofNullable(getAllTempletJson()).orElse(new ArrayList<String>()).stream().map(JSONObject::parseObject)
				.forEach(templetJsonList::add);

		JSONObject tempJson = new JSONObject();
		tempJson.put(KEY_TEMPLET, templetJsonList);

		return tempJson.toJSONString();
	}

	/**
	 * 将类中存储的模板类转换为模板json串，通过List集合收集后，进行返回
	 * 
	 * @return 返回类中所有的模板json串
	 */
	protected abstract List<String> getAllTempletJson();

	/**
	 * 用于在用例中补充默认的字段内容，若用例存在内容，则不添加默认值
	 */
	protected void replenishDefaultContent() {
		// 遍历defaultCaseJson，判断是否存在caseJson中不存在的字段
		for (String key : defaultCaseJson.keySet()) {
			if (!caseJson.containsKey(key)) {
				caseJson.put(key, defaultCaseJson.getJSONObject(key));
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
		// 获取内容中的待替换词语
		ArrayList<String> wordList = getReplaceWord(content);

		// 循环，遍历待替换词语，并对内容进行替换
		for (String word : wordList) {
			// 将词语与每一个规则进行匹配
			for (String key : replaceWordMap.keySet()) {
				// 若传入的内容与正则匹配，则将数据进行处理，并返回处理结果
				if (Pattern.compile(key).matcher(word).matches()) {
					// 将待替换的词语进行拼装
					String oldWord = WORD_SIGN + word + WORD_SIGN;
					// 获取替换的词语
					String newWord = replaceWordMap.get(key).apply(word);
					// 循环，替换所有与oldWord相同的内容
					// 由于oldWord可能包含括号等特殊字符，故不能使用replaceAll方法进行替换
					while (content.contains(oldWord)) {
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
	
	/**
	 * 用于返回指定下标的文本json
	 * <p>
	 * 获取文本json支持反序遍历，当传入的内容为负数时，则表示反序遍历集合，找到指定下标的内容
	 * </p>
	 * @param field 字段
	 * @param textIndex 文本下标
	 * @return 文本json
	 */
	protected JSONObject getTextJson(String field, int textIndex) {
		if (caseJson.containsKey(field)) {
			JSONArray dataListJson = caseJson.getJSONObject(field).getJSONArray(KEY_DATA);
			if (Optional.ofNullable(dataListJson).filter(arr -> !arr.isEmpty()).isPresent()) {
				return dataListJson.getJSONObject(analysisIndex(dataListJson.size(), textIndex, true));
			}
		}
		
		return null;
	}
	
	/**
	 * 用于解析真实下标内容
	 * <p>
	 * 数字最大为下标允许的最大值，是可以达到的，在解析集合下标时，其最大限制一定为“集合长度 - 1”
	 * </p>
	 * @param maxNum 数字最大限制
	 * @param index 下标
	 * @param isReciprocal 是否允许反向遍历
	 * @return 真实下标
	 */
	protected int analysisIndex(int maxNum, int index, boolean isReciprocal) {
		// 若最大值为0，则直接返回0
		if (maxNum == 0) {
			return 0;
		}
		
		if (index < 0) {
			if (isReciprocal) {
				return Math.abs(index) > maxNum ? 0 : (maxNum + index);
			} else {
				return 0;
			}
		} else {
			return index >= maxNum ? maxNum - 1 : index;
		}
	}
	
	/**
	 * 用于创建模板文件
	 * @return 模板文件对象
	 */
	protected abstract void createTempletFile(FileTemplet templet);
}
