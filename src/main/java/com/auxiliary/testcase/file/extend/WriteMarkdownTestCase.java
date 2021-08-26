package com.auxiliary.testcase.file.extend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.file.BasicTsetCase;
import com.auxiliary.testcase.file.RelevanceTestCaseTemplet;
import com.auxiliary.testcase.templet.Case;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteFileException;
import com.auxiliary.tool.file.WriteSingleTempletFile;
import com.auxiliary.tool.file.WriteTempletFile;

/**
 * <p>
 * <b>文件名：</b>WriteMarkdownTestCase.java
 * </p>
 * <p>
 * <b>用途：</b> 用于生成markdown类型的用例，目前该类属于实验阶段
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月29日下午8:19:19
 * </p>
 * <p>
 * <b>修改时间：</b>2021年6月29日下午8:19:19
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class WriteMarkdownTestCase extends WriteSingleTempletFile<WriteMarkdownTestCase>
		implements BasicTsetCase<WriteMarkdownTestCase>, RelevanceTestCaseTemplet<WriteMarkdownTestCase> {
	/**
	 * 用于存储测试用例与测试用例模板字段之间的关联
	 */
	protected HashMap<String, String> caseFieldMap = new HashMap<>();

	/**
	 * 构造用例写入类，使用默认模板
	 * <p>
	 * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
	 * </p>
	 */
	public WriteMarkdownTestCase() {
		super(WriteMarkdownTestCase.getMarkdownTemplet());
		initField();
	}

	/**
	 * 构造用例写入类，使用默认模板并重新设置文件保存路径
	 * <p>
	 * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
	 * </p>
	 * 
	 * @param saveFile 保存路径文件类对象
	 */
	public WriteMarkdownTestCase(File saveFile) {
		this();
		// 重新设置保存路径
		data.getTemplet().setSaveFile(saveFile);
	}

	/**
	 * 构造用例写入类，并重新设置模板
	 * 
	 * @param templet 模板类对象
	 */
	public WriteMarkdownTestCase(FileTemplet templet) {
		super(templet);
		initField();
	}

	/**
	 * 根据已有的写入类对象，构造新的写入类对象，并保存原写入类对象中的模板、内容、字段默认内容以及词语替换内容
	 * <p>
	 * <b>注意：</b>在转换模板时，若模板的name字段为对象，则以默认名称“Sheet + 序号”来命名，并修改其中的name字段值
	 * </p>
	 * 
	 * @param writeTempletFile 文件写入类对象
	 * @throws WriteFileException 文件写入类对象为空时，抛出的异常
	 */
	public WriteMarkdownTestCase(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
		initField();
	}

	/**
	 * 用于初始化字段的链接
	 */
	protected void initField() {
		getField().forEach(field -> caseFieldMap.put(field, field));
	}

	@Override
	protected void contentWriteTemplet(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
		// 获取所有的用例
		JSONArray caseListJson = data.getContentJson().getJSONArray(KEY_CASE);
		ArrayList<String> contentList = new ArrayList<>();
		// 循环，遍历所有需要写入的内容
		for (int index = caseStartIndex; index < caseEndIndex + 1; index++) {
			JSONObject contentJson = caseListJson.getJSONObject(index);

			// 分解模块信息
			List<String> moduleList = Arrays
					.stream(contentJson.getJSONObject(CASE_MODULE).getJSONArray(KEY_DATA).getJSONObject(0)
							.getString(KEY_TEXT).split("\\/"))
					.filter(text -> !text.isEmpty()).collect(Collectors.toList());
			int moduleNum = 0;
			int moduleIndex = -1;

			// 遍历模块信息
			for (; moduleNum < moduleList.size(); moduleNum++) {
				String module = appendSign(moduleNum, moduleList.get(moduleNum));

				if (!contentList.contains(module)) {
					break;
				} else {
					moduleIndex = contentList.indexOf(module);
				}
			}

			moduleIndex++;
			// 判断模块循环结束后，是否存在未添加的模块，存在，则向下添加
			if (moduleNum < moduleList.size()) {
				for (; moduleNum < moduleList.size(); moduleNum++, moduleIndex++) {
					contentList.add(moduleIndex, appendSign(moduleNum, moduleList.get(moduleNum)));
				}
			}

			int signCount = moduleList.size();
			contentList.add(moduleIndex++, appendSign(signCount++,
					contentJson.getJSONObject(CASE_TITLE).getJSONArray(KEY_DATA).getJSONObject(0).getString(KEY_TEXT)));

			JSONArray stepListJson = contentJson.getJSONObject(CASE_STEP).getJSONArray(KEY_DATA);
			JSONArray exceptListJson = contentJson.getJSONObject(CASE_EXCEPT).getJSONArray(KEY_DATA);
			int contentLength = stepListJson.size() < exceptListJson.size() ? exceptListJson.size()
					: stepListJson.size();

			for (int contentIndex = 0; contentIndex < contentLength; contentIndex++) {
				String stepText = (contentIndex > stepListJson.size() ? ""
						: stepListJson.getJSONObject(contentIndex).getString(KEY_TEXT));
				String exceptText = (contentIndex > exceptListJson.size() ? ""
						: exceptListJson.getJSONObject(contentIndex).getString(KEY_TEXT));

				contentList.add(moduleIndex, appendSign(signCount, String.format("%s&%s", stepText, exceptText)));
			}
		}

		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString())))) {
			for (String text : contentList) {
				bw.write(text);
				bw.newLine();
			}
		} catch (Exception e) {
			throw new WriteFileException("文件写入异常", e);
		}
	}

	private String appendSign(int signLength, String text) {
		StringBuilder sign = new StringBuilder("#");
		for (int count = 0; count < signLength; count++) {
			sign.append("#");
		}

		return String.format("%s %s", sign.toString(), text);
	}

	@Override
	protected void createTempletFile(FileTemplet templet) {
		File tempFile = new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString());

		File floderFile = tempFile.getParentFile();
		if (floderFile.exists()) {
			floderFile.mkdirs();
		}
	}

	public static FileTemplet getMarkdownTemplet() {
		FileTemplet templet = new FileTemplet(new File("result/Markdown测试用例.md"));

		templet.addField(CASE_TITLE);
		templet.addField(CASE_STEP);
		templet.addField(CASE_EXCEPT);
		templet.addField(CASE_MODULE);

		return templet;
	}

	@Override
	public WriteMarkdownTestCase addStep(String... stepTexts) {
		addContent(caseFieldMap.get(CASE_STEP), stepTexts);
		return this;
	}

	@Override
	public WriteMarkdownTestCase addTitle(String titleText) {
		addContent(caseFieldMap.get(CASE_TITLE), titleText);
		return this;
	}

	@Override
	public WriteMarkdownTestCase addExcept(String... exceptTexts) {
		addContent(caseFieldMap.get(CASE_EXCEPT), exceptTexts);
		return this;
	}

	@Override
	public WriteMarkdownTestCase addStepAndExcept(String step, String except) {
		addStep(step);
		return addExcept(except);
	}

	@Override
	public WriteMarkdownTestCase addModule(String module) {
		addContent(caseFieldMap.get(CASE_MODULE), module);
		return this;
	}

	@Override
	public WriteMarkdownTestCase addPrecondition(String... preconditions) {
		addContent(caseFieldMap.get(CASE_PRECONDITION), preconditions);
		return this;
	}

	@Override
	public WriteMarkdownTestCase addPriority(String priority) {
		addContent(caseFieldMap.get(CASE_RANK), priority);
		return this;
	}

	@Override
	public void relevanceCase(String caseField, String templetField) {
		caseFieldMap.put(caseField, templetField);
	}

	@Override
	public WriteMarkdownTestCase addCase(Case testCase) {
		// 获取用例内容
		HashMap<String, ArrayList<String>> labelMap = testCase.getFieldTextMap();

		// 遍历当前测试用例模板字段中的内容，将内容写入到相应的文件模板中
		labelMap.forEach((field, content) -> {
			addContent(caseFieldMap.get(field), labelMap.get(field).toArray(new String[] {}));
		});

		return this;
	}
}
