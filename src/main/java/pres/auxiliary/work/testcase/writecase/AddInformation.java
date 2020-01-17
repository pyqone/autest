package pres.auxiliary.work.testcase.writecase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pres.auxiliary.work.testcase.change.Tab;
import pres.auxiliary.work.testcase.exception.IncompleteInformationException;

/**
 * 该类用于生成预设的新增信息相关的测试用例
 * 
 * @author 彭宇琦
 * @version Ver1.5
 */
public class AddInformation extends Case {
	/** 用于存储新增成功时的预期 */
	private StringBuilder successExpectation = new StringBuilder();

	/** 用于存储新增失败时的预期 */
	private StringBuilder failExpectation = new StringBuilder();

	/** 用于存储按钮的名称 */
	private StringBuilder buttonName = new StringBuilder();

	/** 用于存储新增的是什么信息 */
	private StringBuilder informationName = new StringBuilder();

	/** 用于存储前置条件 */
	private StringBuilder precondition = new StringBuilder();

	/** 用于存储测试用例的步骤 */
	private StringBuilder st = new StringBuilder();

	/** 用于存储测试用例的预期 */
	private StringBuilder ex = new StringBuilder();

	/** 调用输入限制 */
	public InputType INPUT;

	/** 调用文件格式限制 */
	public FileType FILE;

	/** 无边界的判断 */
	public final int NUM_NAN = Integer.MIN_VALUE;

	/**
	 * 在创建了模版后可调用该构造方法，该构造会检测模版类中存储的模板文件保存路径及文件名，若模板文件保存路径及文件名的其中一项为空，
	 * 则抛出UndefinedDirectoryException异常，此时请使用带参构造
	 */
	public AddInformation() {
		super();
	}

	/**
	 * 用于指定模板文件对象
	 * 
	 * @param excel
	 *            模板文件对象
	 * @throws IOException
	 */
	public AddInformation(File excel) throws IOException {
		super(excel);
	}

	/**
	 * 该方法用于返回存储的前置条件信息
	 * 
	 * @return 存储的前置条件
	 */
	public String getPrecondition() {
		return precondition.toString();
	}

	/**
	 * 该方法用于整体设置前置条件
	 * 
	 * @param precondition
	 *            待设置的前置条件
	 */
	public void setPrecondition(String precondition) {
		this.precondition.delete(0, this.precondition.length());
		this.precondition.append(precondition);
	}

	/**
	 * 该方法可分步写入前置条件，写入多条时可不带序号以及换行符，如：<br/>
	 * setPrecondition("已在添加信息界面", "所有信息均正确填写");<br/>
	 * 则调用getPrecondition()方法后，输出：<br/>
	 * 1.已在添加信息界面<br/>
	 * 2.所有信息均正确填写<br/>
	 * 
	 * @param preconditions
	 *            写入的多条前置条件
	 * @see #setPrecondition(String)
	 */
	public void setPrecondition(String... preconditions) {
		// 清除原存储的信息
		this.precondition.delete(0, this.precondition.length());
		// 循环，分解preconditions，并拼接前置条件，存储precondition中
		for (int i = 0; i < preconditions.length; i++) {
			// 添加序号
			precondition.append((i + 1) + ".");
			// 添加传入的条件
			precondition.append(preconditions[i]);
			// 判断是否正在添加最后一个条件，若不是，则添加空行(\r\n)
			if (i != preconditions.length - 1) {
				precondition.append("\r\n");
			}
		}
	}

	/**
	 * 该方法用于返回存储的新增信息成功时的预期
	 * 
	 * @return 新增信息成功时的预期
	 */
	public String getSuccessExpectation() {
		return successExpectation.toString();
	}

	/**
	 * 该方法用于设置新增信息成功时的预期
	 * 
	 * @param successExpectation
	 *            待设置的新增信息成功时的预期
	 */
	public void setSuccessExpectation(String successExpectation) {
		this.successExpectation.delete(0, this.successExpectation.length());
		this.successExpectation.append(successExpectation);
	}

	/**
	 * 该方法用于返回存储的新增信息失败时的预期
	 * 
	 * @return 新增信息失败时的预期
	 */
	public String getFailExpectation() {
		return failExpectation.toString();
	}

	/**
	 * 该方法用于设置新增信息失败时的预期
	 * 
	 * @param failExpectation
	 *            待设置的新增信息失败时的预期
	 */
	public void setFailExpectation(String failExpectation) {
		this.failExpectation.delete(0, this.failExpectation.length());
		this.failExpectation.append(failExpectation);
	}

	/**
	 * 该方法用于返回提交表单按钮的名称
	 * 
	 * @return 提交表单按钮的名称
	 */
	public String getButtonName() {
		return buttonName.toString();
	}

	/**
	 * 该方法用于设置提交表单按钮的名称，如“提交按钮”，则传入“提交”
	 * 
	 * @param buttonName
	 *            提交表单按钮的名称
	 */
	public void setButtonName(String buttonName) {
		this.buttonName.delete(0, this.buttonName.length());
		this.buttonName.append(buttonName);
	}

	/**
	 * 该方法用于返回新增信息的名称，如“新增不同车牌的车辆资源”，则返回“车辆资源”
	 * 
	 * @return 新增信息的名称
	 */
	public String getInformationName() {
		return informationName.toString();
	}

	/**
	 * 该方法用于设置新增信息的名称，如“新增不同车牌的车辆资源”，则传入“车辆资源”
	 * 
	 * @param informationName
	 *            新增信息的名称
	 */
	public void setInformationName(String informationName) {
		this.informationName.delete(0, this.informationName.length());
		this.informationName.append(informationName);
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

	/**
	 * 该方法用于生成下拉框的测试用例
	 * 
	 * @param name
	 *            控件的名称
	 * @param isMust
	 *            是否必填
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addSelectboxCase(String name, boolean isMust) throws IOException {
		// 存储方法名
		String methodName = "addSelectboxCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".不进行选择或选择空选项，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(2);
		// st.append(step + ".选择选项中的第一项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(3);
		st.append(step + ".选择选项中的最后一项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成单选按钮的测试用例
	 * 
	 * @param name
	 *            控件的名称
	 * @param isMust
	 *            是否必填
	 * @throws IOException
	 */
	public Tab addRadioButtonCase(String name, boolean isMust) throws IOException {
		// 存储方法名
		String methodName = "addRadioButtonCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".从第一个选项开始，将所有的选项依次点击\r\n");
		ex.append(step++ + ".不会出现有两个或以上的选项被选中的情况\r\n");

		l.add(2);
		// st.append(step + ".不进行选择，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(3);
		// st.append(step + ".选择选项中的第一项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(4);
		// st.append(step + ".选择选项中的最后一项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成多选按钮的测试用例
	 * 
	 * @param name
	 *            控件的名称
	 * @param isMust
	 *            是否必填
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addCheckboxCase(String name, boolean isMust) throws IOException {
		// 存储方法名
		String methodName = "addCheckboxCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".从第一个选项开始，依次点击所有的选项\r\n");
		ex.append(step++ + ".所有的选项均被勾选\r\n");

		l.add(2);
		// st.append(step + ".再次依次点击所有的选项\r\n");
		ex.append(step++ + ".所有的选项均被取消勾选\r\n");

		l.add(3);
		// st.append(step + ".不进行选择，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(4);
		// st.append(step + ".选择选项中的第一项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		l.add(5);
		// st.append(step + ".选择选项中的最后一项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		l.add(6);
		// st.append(step + ".选择其中多个选项，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成与日期相关的测试用例
	 * 
	 * @param name
	 *            控件的名称
	 * @param isMust
	 *            是否必填
	 * @param isInput
	 *            是否可输入
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addDateCase(String name, boolean isMust, boolean isInput) throws IOException {
		// 存储方法名
		String methodName = "addDateCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".不选择时间，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(2);
		// st.append(step + ".选择当前的时间，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(3);
		// st.append(step + ".选择时间后清空时间，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		// 添加是否可以手动输入日期的用例
		if (isInput) {
			l.add(4);
			st.append(step + ".手动输入正确格式的日期，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");

			l.add(5);
			st.append(step + ".手动输入非正确格式的日期，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成针对类似开始时间的测试用例
	 * 
	 * @param name
	 *            控件名称
	 * @param isMust
	 *            是否必填
	 * @param isInput
	 *            是否可输入
	 * @param endDateName
	 *            结束时间的控件名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addStartDateCase(String name, boolean isMust, boolean isInput, String endDateName) throws IOException {
		// 存储方法名
		String methodName = "addStartDateCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		textMap.put("endDateName", endDateName);
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".不选择时间，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(2);
		// st.append(step + ".选择当前的时间，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(3);
		// st.append(step + ".选择时间后清空时间，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		// 添加可以手动输入日期的用例
		if (isInput) {
			l.add(4);
			// st.append(step + ".手动输入正确格式的日期，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");

			l.add(5);
			// st.append(step + ".手动输入非正确格式的日期，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");

			// 添加有结束日期的用例，当日期可输入时，步骤包括“输入或选择···”
			l.add(6);
			// st.append(step + ".输入或选择大于" + endDateName + "的日期，点击“" + getButtonName() +
			// "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		} else {
			// 当日期不可输入时，步骤只为“选择···”
			l.add(7);
			// st.append(step + ".选择大于" + endDateName + "的日期，点击“" + getButtonName() +
			// "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成针对类似结束时间的测试用例
	 * 
	 * @param name
	 *            控件名称
	 * @param isMust
	 *            是否必填
	 * @param isInput
	 *            是否可输入
	 * @param startDateName
	 *            开始时间控件名称
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addEndDateCase(String name, boolean isMust, boolean isInput, String startDateName) throws IOException {
		// 存储方法名
		String methodName = "addEndDateCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		textMap.put("startDateName", startDateName);
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".不选择时间，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(2);
		// st.append(step + ".选择当前的时间，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "，且新增信息的" + name + "为选择的时间\r\n");

		l.add(3);
		// st.append(step + ".选择时间后清空时间，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "，且新增信息的" + name + "为选择的时间\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "，且新增信息的" + name + "为选择的时间\r\n");
		}

		// 添加可以手动输入日期的用例
		if (isInput) {
			l.add(4);
			// st.append(step + ".手动输入正确格式的日期，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + successExpectation.toString() + "，且新增信息的" + name + "为输入的时间\r\n");

			l.add(5);
			// st.append(step + ".手动输入非正确格式的日期，点击“" + getButtonName() + "”按钮");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");

			// 添加有结束日期的用例，当日期可输入时，步骤包括“输入或选择···”
			l.add(6);
			// st.append(step + ".输入或选择小于" + startDateName + "的日期，点击“" + getButtonName() +
			// "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		} else {
			// 当日期不可输入时，步骤只为“选择···”
			l.add(7);
			// st.append(step + ".选择小于" + startDateName + "的日期，点击“" + getButtonName() +
			// "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成针对手机号码的测试用例
	 * 
	 * @param name
	 *            控件的名称
	 * @param isMust
	 *            是否必填
	 * @param isRepeat
	 *            是否可重复
	 * @param phone
	 *            话机类型
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addPhoneCase(String name, boolean isMust, boolean isRepeat, PhoneType phone) throws IOException {
		// 存储方法名
		String methodName = "addPhoneCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
		// st.append(step + ".不进行输入，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(2);
		// st.append(step + ".输入非数字字符，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		// 判断号码的类型，并生成对应的测试用例
		if (phone.equals(PhoneType.MOBLE)) {
			l.add(3);
			// st.append(step + ".输入大于11位的数字，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");

			l.add(4);
			// st.append(step + ".输入小于11位的数字，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			l.add(5);
			// st.append(step + ".输入大于7位的数字，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");

			l.add(6);
			// st.append(step + ".输入小于7位的数字，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		l.add(7);
		// st.append(step + ".输入不符合规则但长度符合规则的数字，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		// 添加优先级信息
		int rank = 2;
		// 判断控件是否必填，若必填，则用例为1级
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成针对身份证号的测试用例
	 * 
	 * @param name
	 *            控件的名称
	 * @param isMust
	 *            是否必填
	 * @param isRepeat
	 *            是否可重复
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addIDCardCase(String name, boolean isMust, boolean isRepeat) throws IOException {
		// 存储方法名
		String methodName = "addIDCardCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 清空步骤与预期中存储的信息
		// st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
//		st.append(step + ".不进行输入，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}

		l.add(2);
//		st.append(step + ".输入非数字字符，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		l.add(3);
//		st.append(step + ".输入15位的证件信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(4);
//		st.append(step + ".输入18位的证件信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(5);
//		st.append(step + ".输入末尾带“X”或“x”的证件信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

		l.add(6);
//		st.append(step + ".输入大于18位的数字，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		l.add(7);
//		st.append(step + ".输入小于18位但大于15位的数字，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		l.add(8);
//		st.append(step + ".输入小于15位的数字，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		l.add(9);
//		st.append(step + ".输入不符合证件规则但长度符合规则的数字（如123456789012345678），点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		if (!isRepeat) {
			l.add(10);
//			st.append(step + ".输入存在的证件信息，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		// 定义优先级
		int rank = 2;
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成上传图片的测试用例
	 * 
	 * @param name
	 *            控件名称
	 * @param isMust
	 *            是否必填
	 * @param isFileRepeat
	 *            是否允许重复上传同一文件
	 * @param isPhotograph
	 *            是否允许拍照
	 * @param isUpload
	 *            是否允许直接上传
	 * @param isSizeConfine
	 *            是否有大小限制
	 * @param fileConfine
	 *            格式限制
	 * @param imageNumConfine
	 *            图片张数限制
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addUploadImageCase(String name, boolean isMust, boolean isFileRepeat, boolean isPhotograph,
			boolean isUpload, boolean isSizeConfine, char[] fileConfine, int[] imageNumConfine) throws IOException {
		// 存储方法名
		String methodName = "addUploadImageCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();

		// 配置步骤的文字描述
		String text;
		if (isPhotograph && isUpload) {
			text = "拍照或上传";
		} else if (!isPhotograph && isUpload) {
			text = "上传";
		} else if (isPhotograph && !isUpload) {
			text = "拍照上传";
		} else {
			// 注意，若既不能拍照，也不能上传图片，则该用例无效
			return null;
		}
		textMap.put("text", text);

		// 清空步骤与预期中存储的信息
		//st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
//		st.append(step + ".不" + text + "图片，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}
		l.add(2);
//		st.append(step + ".上传图片后点击图片\r\n");
		ex.append(step++ + ".图片能进行预览\r\n");
		l.add(3);
//		st.append(step + "." + text + "图片后删除图片再" + text + "图片，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "，且新增信息显示的图片是第二次" + text + "的图片\r\n");
		l.add(4);
//		st.append(step + "." + text + "同一张图片，点击“" + getButtonName() + "”按钮\r\n");
		if (isFileRepeat) {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}
		if (isPhotograph) {
			l.add(5);
//			st.append(step + ".拍照后再取消拍照\r\n");
			ex.append(step++ + ".界面上不显示任何图片\r\n");
		}
		if (isSizeConfine) {
			l.add(6);
//			st.append(step + "." + text + "超过限制大小的图片，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			l.add(7);
//			st.append(step + "." + text + "较大的图片（如超过3M的图片），点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + ".在等待提交过程中有提示或不能进行其他操作，且" + successExpectation.toString() + "\r\n");
		}

		if (fileConfine != null) {
			// 执行添加文件格式限制的方法，存储返回值
			String[] s = fileConfine(fileConfine);
			textMap.put("fileGroup", s[0]);
			l.add(8);
//			st.append(step + ".上传非" + s[0] + "格式的文件，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + s[1]);
		} else {
			l.add(9);
//			st.append(step + "." + text + "上传非图片格式的文件，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}

		if (imageNumConfine != null) {
			String[] s = fileNumConfine(imageNumConfine, step, text, "图片", l);
//			st.append(s[0]);
			ex.append(s[0]);
			step = Integer.valueOf(s[1]);
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, 1);
	}

	/**
	 * 该方法用于生成上传文件的测试用例
	 * 
	 * @param name
	 *            控件名称
	 * @param isMust
	 *            是否必填
	 * @param isFileRepeat
	 *            是否允许重复
	 * @param isSizeConfine
	 *            是否有大小限制
	 * @param fileConfine
	 *            格式限制
	 * @param fileNumConfine
	 *            文件个数限制
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addUploadFileCase(String name, boolean isMust, boolean isFileRepeat, boolean isSizeConfine,
			char[] fileConfine, int[] fileNumConfine) throws IOException {
		// 存储方法名
		String methodName = "addUploadFileCase";
		// 存储需要使用的变量
		textMap.put("name", name);
		textMap.put("buttonName", getButtonName());
		textMap.put("text", "上传");
		// 用于存储读取测试用例的id号
		ArrayList<Integer> l = new ArrayList<>();
				
		// 清空步骤与预期中存储的信息
		//st.delete(0, st.length());
		ex.delete(0, ex.length());

		// 用于存储步骤数
		int step = 1;

		// 添加步骤
		l.add(1);
//		st.append(step + ".不上传文件，点击“" + getButtonName() + "”按钮\r\n");
		if (isMust) {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		}
		l.add(2);
//		st.append(step + ".上传文件后删除文件再上传，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "，且新增信息显示的文件是第二次上传的文件\r\n");
		l.add(3);
//		st.append(step + ".上传同一个文件，点击“" + getButtonName() + "”按钮\r\n");
		if (isFileRepeat) {
			ex.append(step++ + "." + successExpectation.toString() + "\r\n");
		} else {
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		}
		if (isSizeConfine) {
			l.add(4);
//			st.append(step + ".上传超过限制大小的文件，点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + "." + failExpectation.toString() + "\r\n");
		} else {
			l.add(5);
//			st.append(step + ".上传较大的文件（如超过50M的文件），点击“" + getButtonName() + "”按钮\r\n");
			ex.append(step++ + ".在等待提交过程中有提示或不能进行其他操作，且" + successExpectation.toString() + "\r\n");
		}

		if (fileConfine != null) {
			// 执行添加文件格式限制的方法，存储返回值
			String[] s = fileConfine(fileConfine);
			textMap.put("fileGroup", s[0]);
			l.add(6);
//			st.append(step + ".上传非" + s[0]);
			ex.append(step++ + "." + s[1]);
		}
		if (fileNumConfine != null) {
			String[] s = fileNumConfine(fileNumConfine, step, "上传", "文件", l);
//			st.append(s[0]);
			ex.append(s[0]);
			step = Integer.valueOf(s[1]);
		}

		// 定义优先级
		int rank = 2;
		if (isMust) {
			rank = 1;
		}

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(name, new StringBuilder(getStep(methodName, id)[0]), ex, rank);
	}

	/**
	 * 该方法用于生成正确填写所有信息的用例，一般用于用例开头
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addWholeInformationCase() throws IOException {
		// 存储方法名
		String methodName = "addWholeInformationCase";
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

	/**
	 * 该方法用于生成只填写部分信息的用例，一般用于第二条用例
	 * 
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab addUnWholeInformationCase() throws IOException {
		// 存储方法名
		String methodName = "addUnWholeInformationCase";
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
		l.add(-1);
//		st.append(step + ".不填写任何信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

//		st.append(step + ".只填写所有的必填项信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + successExpectation.toString() + "\r\n");

//		st.append(step + ".只填写所有的非必填项信息，点击“" + getButtonName() + "”按钮\r\n");
		ex.append(step++ + "." + failExpectation.toString() + "\r\n");

		// 将ArrayList转换成数组
		int[] id = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			id[i] = l.get(i);
		}
		return after(("添加信息不完整的" + getInformationName()), new StringBuilder(getStep(methodName, id)[0]), ex, ("不信息完整," + getInformationName()), 2,
				getPrecondition());
	}

	private String[] fileConfine(char[] fileConfine) {
		// 拼接输入限制
		String s = String.valueOf(fileConfine);
		// 判断是否包含了所有的限制，若限制都包含，则直接返回空（不需要添加步骤及预期）
		if (s.indexOf(InputType.CH) > -1 && s.indexOf(InputType.EN) > -1 && s.indexOf(InputType.NUM) > -1
				&& s.indexOf(InputType.SPE) > -1) {
			return new String[] { "", "" };
		}
		// 用于存储输入限制的步骤
		StringBuilder sb = new StringBuilder();
		if (s.indexOf(FileType.JPG) > -1) {
			sb.append("jpg、");
		}
		if (s.indexOf(FileType.GIF) > -1) {
			sb.append("gif、");
		}
		if (s.indexOf(FileType.PNG) > -1) {
			sb.append("png、");
		}
		if (s.indexOf(FileType.BMP) > -1) {
			sb.append("bmp、");
		}
		if (s.indexOf(FileType.DOC) > -1) {
			sb.append("doc、");
		}
		if (s.indexOf(FileType.DOCX) > -1) {
			sb.append("docx、");
		}
		if (s.indexOf(FileType.TXT) > -1) {
			sb.append("txt、");
		}
		if (s.indexOf(FileType.XLS) > -1) {
			sb.append("xls、");
		}
		if (s.indexOf(FileType.XLSX) > -1) {
			sb.append("xlsx、");
		}

		// 删除多余的信息
		sb.delete(sb.lastIndexOf("、"), sb.lastIndexOf("、") + 1);

		// 返回步骤及失败的预期
		return new String[] { sb.toString(), (failExpectation.toString() + "\r\n") };
	}

	/**
	 * 该方法用于生成图片张数的限制的用例步骤
	 * 
	 * @param numConfine
	 * @param stepNum
	 * @return
	 */
	private String[] fileNumConfine(int[] fileNumConfine, int stepNum, String text, String mode, ArrayList<Integer> l) {
		// 用于存储长度限制的步骤
//		StringBuilder step = new StringBuilder();
		StringBuilder expectation = new StringBuilder();
		int[] lc = { NUM_NAN, NUM_NAN };
		// 判断参数是否只有一个，则将参数存入lc的第二个元素中
		if (fileNumConfine.length == 1) {
			lc[1] = fileNumConfine[0];
			lc[0] = fileNumConfine[0];
		} else {
			// 通过前判断后，则将lengthConfine中的前两个参数放入lc中
			if (fileNumConfine[0] != 0) {
				lc[0] = fileNumConfine[0];
			}
			if (fileNumConfine[1] != 0) {
				lc[1] = fileNumConfine[1];
			}
			// 判断存入的第一个参数是否小于第二个参数，且第二个参数不是NAN时（由于NAN是int中最小的数，所以此处要判断），调换两参数
			if (lc[0] > lc[1] && lc[1] != NUM_NAN) {
				int tem = lc[0];
				lc[0] = lc[1];
				lc[1] = tem;
			}
		}

		// 判断传入的mode是图片还是文件，若是图片，改单位为张，文件则为个
		String s = "个";
		if (mode.equals("图片")) {
			s = "张";
		}
		
		textMap.put("fileMin", String.valueOf(lc[0]));
		textMap.put("fileMax", String.valueOf(lc[1]));
		textMap.put("s", s);
		textMap.put("mode", mode);
		
		// 判断两参数是否相等，若相等，则生成相应的步骤后返回
		if (lc[0] == lc[1] && lc[0] != NUM_NAN) {
			if ( mode.equals("图片") ) {
				l.add(10);
			} else {
				l.add(7);
			}
//			step.append(stepNum + "." + text + "小于" + lc[0] + s + mode + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			if ( mode.equals("图片") ) {
				l.add(12);
			} else {
				l.add(9);
			}
//			step.append(stepNum + "." + text + "大于" + lc[0] + s + mode + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			return new String[] { expectation.toString(), String.valueOf(stepNum) };
		}
		// 判断参数第一个参数是否为NAN，若不为，则说明有下限，则添加步骤
		if (lc[0] != NUM_NAN) {
			if ( mode.equals("图片") ) {
				l.add(10);
			} else {
				l.add(7);
			}
//			step.append(stepNum + "." + text + "小于" + lc[0] + s + mode + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			if ( mode.equals("图片") ) {
				l.add(11);
			} else {
				l.add(8);
			}
//			step.append(stepNum + "." + text + lc[0] + s + mode + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + successExpectation.toString() + "\r\n");
		}
		// 判断参数第二个参数是否为NAN，若不为，则说明有上限，则添加步骤
		if (lc[1] != NUM_NAN) {
			if ( mode.equals("图片") ) {
				l.add(12);
			} else {
				l.add(9);
			}
//			step.append(stepNum + "." + text + "大于" + lc[1] + s + mode + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			if ( mode.equals("图片") ) {
				l.add(13);
			} else {
				l.add(10);
			}
//			step.append(stepNum + "." + text + lc[1] + s + mode + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + successExpectation.toString() + "\r\n");
		}

		return new String[] { expectation.toString(), String.valueOf(stepNum) };
	}

	/**
	 * 该方法用于生成数字大小限制的用例步骤
	 * 
	 * @param numConfine
	 * @param stepNum
	 * @return
	 */
	private String[] numConfine(int[] numConfine, int stepNum, ArrayList<Integer> l) {
		// 用于存储长度限制的步骤
		// StringBuilder step = new StringBuilder();
		StringBuilder expectation = new StringBuilder();
		// 处理长度限制，处理机制为：
		// 1.若只传入一个参数（假设为10），则默认不限制下限大小，等同于传入{NAN, 10}，即最长只能输入10个字符，但没有下限
		// 2.若传入的参数多于2个，则只接受前两个参数，即传入{1, 2, 3}，则处理为{1, 2}
		// 3.若传入的参数大小相反，则对调两个参数，即传入{2, 1}，则处理为{1, 2}
		// 用于存储处理后的限制组，初始化为无限制
		int[] lc = { NUM_NAN, NUM_NAN };
		// 判断参数是否只有一个，则将参数存入lc的第二个元素中
		if (numConfine.length == 1) {
			lc[1] = numConfine[0];
			lc[0] = numConfine[0];
		} else {
			// 通过前判断后，则将lengthConfine中的前两个参数放入lc中
			lc[0] = numConfine[0];
			lc[1] = numConfine[1];
			// 判断存入的第一个参数是否小于第二个参数，且第二个参数不是NAN时（由于NAN是int中最小的数，所以此处要判断），调换两参数
			if (lc[0] > lc[1] && lc[1] != NUM_NAN) {
				int tem = lc[0];
				lc[0] = lc[1];
				lc[1] = tem;
			}
		}

		// 存储最大最小值
		textMap.put("numberMin", String.valueOf(lc[0]));
		textMap.put("numberMax", String.valueOf(lc[1]));

		// 判断两参数是否相等，若相等，则生成相应的步骤后返回
		if (lc[0] == lc[1] && lc[0] != NUM_NAN) {
			l.add(8);
			// step.append(stepNum + ".输入小于" + lc[0] + "的数字，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			l.add(10);
			// step.append(stepNum + ".输入大于" + lc[0] + "的数字，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			return new String[] { expectation.toString(), String.valueOf(stepNum) };
		}
		// 判断参数第一个参数是否为NAN，若不为，则说明有下限，则添加步骤
		if (lc[0] != NUM_NAN) {
			l.add(8);
			// step.append(stepNum + ".输入小于" + lc[0] + "的数字，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			l.add(9);
			// step.append(stepNum + ".输入" + lc[0] + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + successExpectation.toString() + "\r\n");
		}
		// 判断参数第二个参数是否为NAN，若不为，则说明有上限，则添加步骤
		if (lc[1] != NUM_NAN) {
			l.add(10);
			// step.append(stepNum + ".输入大于" + lc[1] + "的数字，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			l.add(11);
			// step.append(stepNum + ".输入" + lc[1] + "，点击“" + getButtonName() + "”按钮\r\n");
			expectation.append(stepNum++ + "." + successExpectation.toString() + "\r\n");
		}

		return new String[] { expectation.toString(), String.valueOf(stepNum) };
	}

	/**
	 * 用于生成有长度限制的文本框
	 * 
	 * @param lengthConfine
	 * @param stepNum
	 * @return
	 */
	private String[] lengthConfineStep(int[] lengthConfine, int stepNum, ArrayList<Integer> l) {
		// 用于存储长度限制的步骤
		// StringBuilder step = new StringBuilder();
		// 用于存储预期
		StringBuilder expectation = new StringBuilder();
		// 处理长度限制，处理机制为：
		// 1.若只传入一个参数（假设为10），则默认不限制下限长度，等同于传入{NAN, 10}，即最长只能输入10个字符，但没有下限
		// 2.若传入的参数多于2个，则只接受前两个参数，即传入{1, 2, 3}，则处理为{1, 2}
		// 3.若传入的参数大小相反，则对调两个参数，即传入{2, 1}，则处理为{1, 2}
		// 用于存储处理后的限制组，初始化为无限制
		int[] lc = { NUM_NAN, NUM_NAN };
		// 判断参数是否只有一个，则将参数存入lc的第二个元素中
		if (lengthConfine.length == 1) {
			lc[1] = lengthConfine[0];
			lc[0] = lengthConfine[0];
		} else {
			// 通过前判断后，则将lengthConfine中的前两个参数放入lc中
			lc[0] = lengthConfine[0];
			lc[1] = lengthConfine[1];
			// 判断存入的第一个参数是否小于第二个参数，且第二个参数不是NAN时（由于NAN是int中最小的数，所以此处要判断），调换两参数
			if (lc[0] > lc[1] && lc[1] != NUM_NAN) {
				int tem = lc[0];
				lc[0] = lc[1];
				lc[1] = tem;
			}
		}

		// 存储最大最小值
		textMap.put("lengthMin", String.valueOf(lc[0]));
		textMap.put("lengthMax", String.valueOf(lc[1]));

		// 判断两参数是否相等，若相等，则生成相应的步骤后返回
		if (lc[0] == lc[1] && lc[0] != NUM_NAN) {
			l.add(4);
			// step.append(stepNum + ".输入小于" + lc[0] + "个字符，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			l.add(6);
			// step.append(stepNum + ".输入大于" + lc[0] + "个字符，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			return new String[] { expectation.toString(), String.valueOf(stepNum) };
		}
		// 判断参数第一个参数是否为NAN，若不为，则说明有下限，则添加步骤
		if (lc[0] != NUM_NAN) {
			l.add(4);
			// step.append(stepNum + ".输入小于" + lc[0] + "个字符，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			l.add(5);
			// step.append(stepNum + ".输入" + lc[0] + "个字符，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + successExpectation.toString() + "\r\n");
		}
		// 判断参数第二个参数是否为NAN，若不为，则说明有上限，则添加步骤
		if (lc[1] != NUM_NAN) {
			l.add(6);
			// step.append(stepNum + ".输入大于" + lc[1] + "个字符，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + failExpectation.toString() + "\r\n");

			l.add(7);
			// step.append(stepNum + ".输入" + lc[1] + "个字符，点击“" + getButtonName() +
			// "”按钮\r\n");
			expectation.append(stepNum++ + "." + successExpectation.toString() + "\r\n");
		}

		return new String[] { expectation.toString(), String.valueOf(stepNum) };
	}

	/**
	 * 该方法用于添加输入限制的步骤
	 * 
	 * @param inputConfine
	 * @return
	 */
	private String[] inputConfineStep(char[] inputConfine) {
		// 拼接输入限制
		String s = String.valueOf(inputConfine);
		// 判断是否包含了所有的限制，若限制都包含，则直接返回空（不需要添加步骤及预期）
		if (s.indexOf(InputType.CH) > -1 && s.indexOf(InputType.EN) > -1 && s.indexOf(InputType.NUM) > -1
				&& s.indexOf(InputType.SPE) > -1) {
			return new String[] { "", "" };
		}

		// 用于存储输入限制的步骤
		StringBuilder sb = new StringBuilder();
		if (s.indexOf(InputType.NUM) > -1) {
			sb.append("数字、");
		}
		if (s.indexOf(InputType.EN) > -1) {
			sb.append("英文、");
		}
		if (s.indexOf(InputType.CH) > -1) {
			sb.append("中文、");
		}
		if (s.indexOf(InputType.SPE) > -1) {
			sb.append("特殊字符、");
		}

		// 删除多余的信息
		sb.delete(sb.lastIndexOf("、"), sb.lastIndexOf("、") + 1);

		// 返回步骤及失败的预期
		return new String[] { sb.toString(), failExpectation.toString() };
	}

	/**
	 * 封装各个方法结束前必须使用的代码
	 * 
	 * @param name
	 *            控件名称
	 * @param st
	 *            用例步骤
	 * @param ex
	 *            用例预期
	 * @param rank
	 *            优先级
	 * @throws IOException
	 */
	private Tab after(String name, StringBuilder st, StringBuilder ex, int rank) throws IOException {
		// 判断新增信息的名称是否为空，若为空，则抛出IncompleteInformationException异常
		if (informationName.toString().equals("")) {
			throw new IncompleteInformationException("新增信息的名称不能为空");
		}
		// 判断提交表单按钮的名称是否为空，若为空，则抛出IncompleteInformationException异常
		if (buttonName.toString().equals("")) {
			throw new IncompleteInformationException("提交表单按钮的名称不能为空");
		}

		return after(("新增不同" + name + "的" + getInformationName()), st, ex, ("新增," + name), rank, getPrecondition());
	}
}
