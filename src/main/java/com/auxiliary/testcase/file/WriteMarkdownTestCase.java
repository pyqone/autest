package com.auxiliary.testcase.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.auxiliary.testcase.templet.CaseData;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteSingleTempletFile;

/**
 * <p>
 * <b>文件名：</b>WriteMarkdownTestCase.java
 * </p>
 * <p>
 * <b>用途：</b> 用于生成markdown类型的用例
 * </p>
 * <p>
 * <b>编码时间：</b>2021年6月29日下午8:19:19
 * </p>
 * <p>
 * <b>修改时间：</b>2023年4月28日 上午10:30:52
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver2.0
 * @since JDK 1.8
 * @since autest 4.2.0
 */
public abstract class WriteMarkdownTestCase<T extends WriteMarkdownTestCase<T>> extends WriteSingleTempletFile<T>
        implements RelevanceTestCaseTemplet<T> {
	/**
	 * 用于存储测试用例与测试用例模板字段之间的关联
	 */
	protected HashMap<String, String> caseFieldMap = new HashMap<>();

	/**
     * 构造用例写入类，并重新设置模板
     * 
     * @param templet 模板类对象
     * @since autest 4.2.0
     */
	public WriteMarkdownTestCase(FileTemplet templet) {
		super(templet);
		initField();
	}

    /**
     * 该方法用于模板与
     * 
     * @since autest 4.2.0
     */
    protected abstract void initField();

    /**
     * 该方法用于拼接markdown语法的层级关系标志
     * 
     * @param signType   标志
     * @param signLength 需要添加的标志个数
     * @param text       用例内容
     * @return 添加了标志的用例内容
     * @since autest 4.2.0
     */
    protected String appendSign(String signType, int signLength, String text) {
        StringBuilder sign = new StringBuilder(signType);
		for (int count = 0; count < signLength; count++) {
            sign.append(signType);
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

    @Override
    public void relevanceCase(String caseField, String templetField) {
        caseFieldMap.put(templetField, caseField);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T addCase(CaseData caseData) {
        // 遍历当前测试用例模板字段中的内容，将内容写入到相应的文件模板中
        disposeWriteFieldsContent(caseData.getFields(), () -> {
            caseFieldMap.forEach((key, value) -> {
                List<String> contentList = caseData.getContent(value);
                if (!contentList.isEmpty()) {
                    addContent(key, caseData.getCaseTemplet().getReplaceWordMap(),
                            contentList.toArray(new String[] {}));
                }
            });
        });
        return (T) this;
    }
}
