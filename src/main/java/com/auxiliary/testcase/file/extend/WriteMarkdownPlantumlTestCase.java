package com.auxiliary.testcase.file.extend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Optional;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteFileException;

/**
 * <p>
 * <b>文件名：WriteMarkdownPlantumlTestCase.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于生成符合PlantUml语法的Markdown形式的测试用例模板，可通过生成文本文件，调用PlantUml依赖提供的方法，生成相应的脑图图片，即调用命令
 * “{@code java -DPLANTUML_LIMIT_SIZE=图片大小 -jar plantUml的jar包所在路径 -charset UTF-8 md文件所在路径}”进行生成。
 * </p>
 * <p>
 * <b>编码时间：2023年4月28日 上午10:51:43
 * </p>
 * <p>
 * <b>修改时间：2023年4月28日 上午10:51:43
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public class WriteMarkdownPlantumlTestCase extends MarkdownPresetCaseTemplet<WriteMarkdownPlantumlTestCase> {
    /**
     * 左节点标记
     */
    private final String SIGN_LEFT = "-";
    /**
     * 右节点标记
     */
    private final String SIGN_RIGHT = "+";

	/**
	 * 构造用例写入类，使用默认模板
	 * <p>
	 * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
	 * </p>
	 */
	public WriteMarkdownPlantumlTestCase() {
        super();
	}

	/**
	 * 构造用例写入类，使用默认模板并重新设置文件保存路径
	 * <p>
	 * 默认的模板可通过{@link WriteJiraExcelTestCase#getJiraCaseExcelTemplet()}方法进行获取
	 * </p>
	 * 
	 * @param saveFile 保存路径文件类对象
	 */
	public WriteMarkdownPlantumlTestCase(File saveFile) {
		this();
		// 重新设置保存路径
		data.getTemplet().setSaveFile(saveFile);
	}

	/**
	 * 构造用例写入类，并重新设置模板
	 * 
	 * @param templet 模板类对象
	 */
	public WriteMarkdownPlantumlTestCase(FileTemplet templet) {
		super(templet);
		initField();
	}

    @Override
    protected void contentWriteTemplet(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
        // 获取md文件的保存路径
        String mdFilePath = templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString();

        // 生成当前用例的markdown内容
        String markdownText = createMarkdownContent(templet, caseStartIndex, caseEndIndex);

        // 生成文本，并进行保存
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(mdFilePath)))) {
            bw.write(markdownText);
        } catch (Exception e) {
            throw new WriteFileException("文件写入异常", e);
        }
    }

	@Override
	protected void createTempletFile(FileTemplet templet) {
		File tempFile = new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString());

		File floderFile = tempFile.getParentFile();
		if (floderFile.exists()) {
			floderFile.mkdirs();
		}
	}

    /**
     * 该方法用于重新生成markdown内容
     * 
     * @param caseStartIndex 需要读取用例的起始下标
     * @param caseEndIndex   需要读取用例的结束下标
     * @since autest 4.2.0
     */
    private String createMarkdownContent(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
        // 存储生成的markdown内容
        StringJoiner markdownContent = new StringJoiner("\r\n", "@startmindmap\r\n", "\r\n@endmindmap");
        // 拼接根节点
        markdownContent.add(appendSign(SIGN_LEFT, 1, "测试用例"));

        // 平衡左右两边的节点分支，故需要计算起始节点与结束节点的中间位置
        int centreNum = caseStartIndex + ((caseEndIndex - caseStartIndex) / 2);

        // 获取所有的用例
        JSONArray caseArrJson = data.getContentJson().getJSONArray(KEY_CASE);
        // 循环，遍历所有需要写入的内容
        for (int index = caseStartIndex; index < caseEndIndex + 1; index++) {
            // 获取用例内容
            JSONObject caseJson = caseArrJson.getJSONObject(index);
            // 定义标志
            String signType = index <= centreNum ? SIGN_LEFT : SIGN_RIGHT;
            
            // 拼接用例的标题
            markdownContent.add(appendSign(signType, 2, String.format(CONTENT_FORMAT,
                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_TITLE, TEMPLET_ATTRI_NAME),
                    Optional.ofNullable(caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_TITLE))
                            .map(json -> json.getJSONArray(KEY_DATA)).map(arrJson -> arrJson.getJSONObject(0))
                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));

            // 判断当前是否存在步骤，存在则拼接用例的步骤与预期模块
            JSONArray stepArrJson = Optional.ofNullable(
                    caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_STEP))
                    .map(json -> json.getJSONArray(KEY_DATA)).orElse(new JSONArray());
            JSONArray expectpArrJson = Optional.ofNullable(
                    caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_EXPECT))
                    .map(json -> json.getJSONArray(KEY_DATA)).orElse(new JSONArray());
            if (!stepArrJson.isEmpty()) {
                markdownContent.add(appendSign(signType, 3, "步骤与预期"));
                for (int stepIndex = 0; stepIndex < stepArrJson.size(); stepIndex++) {
                    // 拼接步骤
                    markdownContent.add(appendSign(signType, 4,
                            String.format(CONTENT_FORMAT,
                                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_STEP,
                                            TEMPLET_ATTRI_NAME),
                                    Optional.ofNullable(stepArrJson.getJSONObject(stepIndex))
                                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));
                    // 拼接预期，若预期不足，则不进行拼接
                    try {
                        markdownContent.add(appendSign(signType, 5,
                                String.format(CONTENT_FORMAT,
                                        templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_EXPECT,
                                                TEMPLET_ATTRI_NAME),
                                        expectpArrJson.getJSONObject(stepIndex).getString(KEY_TEXT))));
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
            }

            // 判断当前是否存在前置条件，存在则拼接用例的前置条件
            JSONArray preconditionArrJson = Optional.ofNullable(
                    caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_PRECONDITION))
                    .map(json -> json.getJSONArray(KEY_DATA)).orElse(new JSONArray());
            if (!preconditionArrJson.isEmpty()) {
                markdownContent.add(appendSign(signType, 3, "前置条件"));
                for (int preconditionIndex = 0; preconditionIndex < preconditionArrJson.size(); preconditionIndex++) {
                    // 拼接步骤
                    markdownContent.add(appendSign(signType, 4,
                            Optional.ofNullable(preconditionArrJson.getJSONObject(preconditionIndex))
                                    .map(json -> json.getString(KEY_TEXT)).orElse("")));
                }
            }

            // 拼接用例的优先级
            markdownContent.add(appendSign(signType, 3, String.format(CONTENT_FORMAT,
                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_RANK, TEMPLET_ATTRI_NAME),
                    Optional.ofNullable(caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_RANK)
                            .getJSONArray(KEY_DATA)).map(arrJson -> arrJson.getJSONObject(0))
                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));

            // 拼接用例的所属模块
            markdownContent.add(appendSign(signType, 3, String.format(CONTENT_FORMAT,
                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_MODULE, TEMPLET_ATTRI_NAME),
                    Optional.ofNullable(caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_MODULE))
                            .map(json -> json.getJSONArray(KEY_DATA)).map(arrJson -> arrJson.getJSONObject(0))
                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));
            // 拼接用例的关键词
            markdownContent.add(appendSign(signType, 3, String.format(CONTENT_FORMAT,
                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_KEY, TEMPLET_ATTRI_NAME),
                    Optional.ofNullable(caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_KEY))
                            .map(json -> json.getJSONArray(KEY_DATA)).map(arrJson -> arrJson.getJSONObject(0))
                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));
            // 拼接用例的创建人
            markdownContent.add(appendSign(signType, 3, String.format(CONTENT_FORMAT,
                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_CREATE_PERSON,
                            TEMPLET_ATTRI_NAME),
                    Optional.ofNullable(caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_CREATE_PERSON))
                            .map(json -> json.getJSONArray(KEY_DATA)).map(arrJson -> arrJson.getJSONObject(0))
                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));
            // 拼接用例的创建时间
            markdownContent.add(appendSign(signType, 3, String.format(CONTENT_FORMAT,
                    templet.getFieldAttribute(MarkdownPresetFieldType.FIELD_CREATE_DATE,
                            TEMPLET_ATTRI_NAME),
                    Optional.ofNullable(caseJson.getJSONObject(MarkdownPresetFieldType.FIELD_CREATE_DATE))
                            .map(json -> json.getJSONArray(KEY_DATA)).map(arrJson -> arrJson.getJSONObject(0))
                            .map(json -> json.getString(KEY_TEXT)).orElse(""))));
        }
        
        return markdownContent.toString();
    }
}
