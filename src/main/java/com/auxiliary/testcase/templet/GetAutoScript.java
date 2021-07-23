package com.auxiliary.testcase.templet;

/**
 * <p><b>文件名：</b>GetAutoScript.java</p>
 * <p><b>用途：</b>
 * 定义能生成自动化测试脚本的测试用例模板类所需要实现的方法。约定返回的json为如下格式：
 * </p>
 * <code><pre>
 * {
 *     "templetType": 1,
 *     "element": [
 *         "XXXX",
 *         "XXXX",
 *         "XXXX"
 *     ],
 *     "case": [
 *         {
 *             "caseTitleText": "XXXX",
 *             "step": [
 *                 {
 *                     "record": {
 *                         "methodName": "step_1",
 *                         "caseStepText": "XXXX",
 *                         "caseExceptText": "XXXX"
 *                     },
 *                     "operateStep": [
 *                         {
 *                             "elementName": "XXXX",
 *                             "index":0, 
 *                             "elementType":0, 
 *                             "operate": "click",
 *                             "input": ""
 *                         }
 *                     ]
 *                 }
 *             ]
 *         }
 *     ]
 * }
 * </pre></code>
 * <p><b>编码时间：</b>2021年7月7日上午8:09:26</p>
 * <p><b>修改时间：</b>2021年7月7日上午8:09:26</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public interface GetAutoScript {
	/**
	 * 字段存储当前需要使用的元素，Array
	 */
	String KEY_ELEMENT = "element";
	/**
	 * 字段存储用例，Array
	 */
	String KEY_CASE = "case";
	/**
	 * 字段存储用例标题名称，String
	 */
	String KEY_CASE_TITLE_TEXT = "caseTitleText";
	/**
	 * 字段存储当前用例的步骤，Array
	 */
	String KEY_STEP = "step";
	/**
	 * 字段存储当前步骤所需要记录的内容，Object
	 */
	String KEY_RECORD = "record";
	/**
	 * 字段存储当前步骤的名称，String
	 */
	String KEY_METHOD_NAME = "methodName";
	/**
	 * 字段存储当前用例的步骤文本，String
	 */
	String KEY_CASE_STEP_TEXT = "caseStepText";
	/**
	 * 字段存储当前用例的预期文本，String
	 */
	String KEY_CASE_EXCEPT_TEXT = "caseExceptText";
	/**
	 * 字段存储当前用例的操作步骤，Array
	 */
	String KEY_OPERATE_STEP = "operateStep";
	/**
	 * 字段存储当前操作元素的名称，String
	 */
	String KEY_ELEMENT_NAME = "elementName";
	/**
	 * 存储当前操作元素的下标，int
	 */
	String KEY_INDEX = "index";
	/**
	 * 存错当前操作元素的元素类型，int
	 */
	String KEY_ELEMENT_TYPE = "elementType";
	/**
	 * 字段存储当前操作的类型，int
	 */
	String KEY_OPERATE = "operate";
	/**
	 * 字段存储当前操作的输入内容，String
	 */
	String KEY_INPUT = "input";
	/**
	 * 存储当前操作的输出的内容至某个变量，String
	 */
	String KEY_OUTPUT = "output";
	
	/**
	 * 用于获取所有元素的标志
	 */
	String ELEMENT_INDEX_All_SIGN = "all";
	/**
	 * 用于分隔元素下标的标志
	 */
	String ELEMENT_INDEX_SPLIT_SIGN = ",";
	
	/**
	 * 用于以json的形式返回当前测试用例模板中存储的内容
	 * @return 测试用例模板操作json
	 */
	String getAutoScriptJson();
}
