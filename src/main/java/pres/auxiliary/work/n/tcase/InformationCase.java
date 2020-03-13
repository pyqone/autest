package pres.auxiliary.work.n.tcase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pres.auxiliary.work.testcase.change.Tab;
import pres.auxiliary.work.testcase.writecase.InputType;

/**
 * <p><b>文件名：</b>InformationCase.java</p>
 * <p><b>用途：</b>用于输出与页面新增或编辑信息相关的用例，类中提供部分模板中会使用到
 * 待替换的词语，可通过类名获取相应的文本，传入{@link #setReplaceWord(String, String)}方法
 * 的第一个参数中</p>
 * <p><b>编码时间：</b>2020年3月5日上午8:30:12</p>
 * <p><b>修改时间：</b>2020年3月5日上午8:30:12</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class InformationCase extends Case {
	/**
	 * 用于标记提交按钮名称
	 */
	public static final String BUTTON_NAME = "按钮名称";
	/**
	 * 用于标记需要添加的信息
	 */
	public static final String ADD_INFORMATION = "信息";
	/**
	 * 用于标记添加成功预期的前文
	 */
	public static final String SUCCESS_EXCEPT_TEXT_START = "成功预期前文";
	/**
	 * 用于标记添加成功预期的后文
	 */
	public static final String SUCCESS_EXCEPT_TEXT_END = "成功预期后文";
	/**
	 * 用于标记添加失败预期前文
	 */
	public static final String FAIL_EXCEPT_TEXT_START = "失败预期前文";
	/**
	 * 用于标记添加失败预期后文
	 */
	public static final String FAIL_EXCEPT_TEXT_END = "失败预期后文";
	
	/**
	 * 通过测试用例模板库的xml配置文件来构造InformationCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public InformationCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	@Override
	public void setReplaceWord(String word, String text) {
		//由于预期前后文是直接在文本上拼接，故为保证语句通顺，则自动为其添加
		//若传入的参数为成功或失败预期的前文时，则在文本后加上逗号
		if (SUCCESS_EXCEPT_TEXT_START.equals(word) || FAIL_EXCEPT_TEXT_START.equals(word)) {
			//判断文本最后一个字符是否为逗号，若不是逗号，则将逗号拼接上
			if (text.lastIndexOf("，") == text.length() - 1) {
				text += ",";
			}
		}
		
		//若传入的参数为成功或失败预期的后文时，则在文本前加上逗号
		if (SUCCESS_EXCEPT_TEXT_END.equals(word) || FAIL_EXCEPT_TEXT_END.equals(word)) {
			//判断文本第一个字符是否为逗号，若不是逗号，则将逗号拼接上
			if (text.indexOf("，") == 0) {
				text = "," + text;
			}
		}
		
		super.setReplaceWord(word, text);
	}
	
	/**
	 * 该方法用于生成正确填写所有信息的用例
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Case addWholeInformationCase() throws IOException {
		// 存储case标签的name属性内容
		String caseName = "addWholeInformationCase";
		
		//存储标题信息
		String title = replaceText(getText(caseName, LabelType.TITLE, 1));
		//存储步骤信息
		ArrayList<String> step = new ArrayList<>();
		
		
		// 存储需要使用的变量
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();
				
		// 清空步骤与预期中存储的信息
		//st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
//		st.append(step + ".正确填写所有的信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(("添加信息完整的" + getInformationName()), new StringBuilder(getStep(methodName, id)[0]), ex, ("信息完整," + getInformationName()), 1,
				getPrecondition());
	}
}
