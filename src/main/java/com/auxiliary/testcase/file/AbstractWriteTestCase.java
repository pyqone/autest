package com.auxiliary.testcase.file;

import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auxiliary.testcase.templet.Case;

@SuppressWarnings("unchecked")
public abstract class AbstractWriteTestCase<T extends AbstractWriteTestCase<T>> {
	/**
	 * 存储生成的测试用例内容
	 */
	JSONObject caseJson = new JSONObject();
	/**
	 * 存储当前节点的用例
	 */
	JSONObject nodeJson = new JSONObject();;

	/**
	 * 用于初始化数据
	 */
	public AbstractWriteTestCase() {
		caseJson.put("caseList", new JSONArray());
	}

	/**
	 * 用于写入测试用例标题
	 * <p>
	 * <b>注意：</b>该方法只能写入一次，多次调用会覆盖上一次存储的内容
	 * </p>
	 * 
	 * @param titleText 标题内容
	 * @return 类本身
	 */
	public T title(String titleText) {
		nodeJson.put("title", Optional.ofNullable(titleText).filter(t -> !t.isEmpty())
				.orElseThrow(() -> new IncorrectContentException("未指定标题内容")));
		return (T) this;
	}

	/**
	 * 用于写入测试用例标题
	 * 
	 * @param stepTexts 标题内容组
	 * @return 类本身
	 */
	public abstract T step(String... stepTexts);

	/**
	 * 用于写入测试用例预期
	 * 
	 * @param exceptTexts 预期内容组
	 * @return
	 */
	public abstract T except(String... exceptTexts);

	/**
	 * 用于写入测试用例优先级
	 * 
	 * @param rank 优先级内容
	 * @return 类本身
	 */
	public abstract T rank(int rank);

	/**
	 * 用于写入测试用例前置条件
	 * 
	 * @param contitionTexts 前置条件内容组
	 * @return 类本身
	 */
	public abstract T precondition(String... contitionTexts);

	/**
	 * 用于写入模板中的测试用例
	 * 
	 * @param testCase 模板类
	 * @return 类背身
	 */
	public abstract T testCase(Case testCase);

	/**
	 * 测试用例编写结束标志，用于标记完成当前用例的编写
	 * 
	 * @return 类本身
	 */
	public T end() {
		caseJson.getJSONArray("case").add(JSONObject.parse(nodeJson.toJSONString()));
		nodeJson = new JSONObject();
		return (T) this;
	}

	/**
	 * 用于返回生成的测试用例内容
	 * 
	 * @return 测试用例内容
	 */
	public String getCase() {
		return caseJson.toJSONString();
	}
}
