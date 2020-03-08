package pres.auxiliary.work.n.tcase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pres.auxiliary.work.testcase.change.Tab;
import pres.auxiliary.work.testcase.writecase.InputType;

/**
 * <p><b>文件名：</b>InformationCase.java</p>
 * <p><b>用途：</b>用于输出与页面新增或编辑信息相关的用例</p>
 * <p><b>编码时间：</b>2020年3月5日上午8:30:12</p>
 * <p><b>修改时间：</b>2020年3月5日上午8:30:12</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 12
 */
public class InformationCase extends Case {
	private final String BUTTON_NAME = "按钮名称";
	
	/**
	 * 通过测试用例模板库的xml配置文件来构造InformationCase对象
	 * @param configXmlFile 用例模板库的xml文件对象
	 */
	public InformationCase(File configXmlFile) {
		super(configXmlFile);
	}
	
	/**
	 * 该方法用于生成针对文本框的测试用例
	 * 
	 * @param name
	 *            文本框的名称
	 * @param isMust
	 *            是否必填
	 * @param isRepeat
	 *            是否可重复
	 * @param inputConfine
	 *            输入限制
	 * @param lengthConfine
	 *            输入长度限制
	 * @param numConfine
	 *            数字大小限制
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addTextboxCase(String name, boolean isMust, boolean isRepeat, char[] inputConfine, int[] lengthConfine,
			int[] numConfine) throws IOException {
		//用于存储步骤
		ArrayList<String> step = new ArrayList<>();
		//用于存储预期
		ArrayList<String> except = new ArrayList<>();
		
		// 存储方法名
		String methodName = "addTextboxCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 用于存储步骤数
		int step = 1;

		// 添加为空的步骤及预期
		// st.append(step + ".不填写或只输入空格，点击“" + getButtonName() + "”按钮\r\n");
		l.add(1);
		// 判断文本框是否必填，并添加相应的信息（必填时，信息为空，则添加错误的预期）
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		// 添加填写特殊字符
		// st.append(step + ".填写特殊字符或HTML代码，点击“" + getButtonName() + "”按钮\r\n");
		l.add(2);
		// 判断文本框是否包含特殊字符的限制，若能输入特殊字符，则添加正确的用例，反之则添加失败的用例
		if (inputConfine != null) {
			// 用于判断inputConfine是否存在特殊字符的限制
			boolean isSPE = false;
			// 循环，遍历inputConfine
			for (char c : inputConfine) {
				// 判断当前元素是否为Input.SPE，若是，则说明文本框允许输入输入特殊字符，则将isSPE设为true，并结束循环
				if (Character.compare(c, InputType.SPE) == 0) {
					isSPE = true;
					break;
				}
			}

			// 判断isSPE，为true则添加正确的用例，为false则添加失败的用例
			if (isSPE) {
				ex.append(step++ + "." + successExpectation.toString() + "\r\n");
			}
			else {
				ex.append(step++ + "." + failExpectation.toString() + "\r\n");
			}
		} else {
			// 如果没有输入限制，则默认可以输入特殊字符
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		// 添加输入限制的步骤，没有限制则不添加
		// 判断输入限制是否为null，若不为null，则添加输入限制步骤
		if (inputConfine != null) {
			String[] s = inputConfineStep(inputConfine);
			textMap.put("charGroup", s[0]);
			l.add(3);
			// st.append(step + ".输入非" + s[0] + "字符，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + s[1] + "\r\n");
		}

		// 添加长度限制的步骤，没有则不添加
		if (lengthConfine != null) {
			String[] s = lengthConfineStep(lengthConfine, step, l);
			// st.append(s[0]);
			ex.append(s[0]);
			step = Integer.valueOf(s[1]);
		}

		// 添加数字大小限制的步骤，没有则不添加
		if (numConfine != null) {
			String[] s = numConfine(numConfine, step, l);
			// st.append(s[0]);
			ex.append(s[0]);
			step = Integer.valueOf(s[1]);
		}

		if (!isRepeat) {
			l.add(12);
			// st.append(step + ".填写一个已存在的" + name + "信息，点击“" + getButtonName() +
			// "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}
	
}
