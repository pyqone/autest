package pres.auxiliary.report.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.dom4j.io.SAXReader;

import pres.auxiliary.report.AbstractReport.BugInformation;
import pres.auxiliary.tool.ui.control.AutoNumberJTextArea;

/**
 * FileName: TestReportFrame.java
 * 
 * 该界面是生成测试报告程序的主界面，可在此编辑测试报告的内容并生成测试报告
 * 
 * @author 彭宇琦
 * @Deta 2018年4月26日 下午2:52:49
 * @version ver1.3
 */
public class TestReportFrame extends JFrame {
	private static final long serialVersionUID = 4597332795736199596L;
	private JPanel contentPane;
	private static JTextField showBugFilePath;
	protected static JTextField projectName;
	protected static JTextField Version;
	private JLabel label_2;
	protected static JSpinner startTime;
	private JLabel label_3;
	protected static JSpinner endTime;
	protected static JTextField range;
	protected static JTextField person;
	protected static JTextArea purpose;
	protected static JTextArea gist;
	protected static JSpinner activeOneBug;
	protected static JSpinner activeTwoBug;
	protected static JSpinner activeThreeBug;
	protected static JSpinner activeFourBug;
	protected static JSpinner newOneBug;
	protected static JSpinner newTwoBug;
	protected static JSpinner newThreeBug;
	protected static JSpinner newFourBug;
	protected static JCheckBox isPass;
	protected static JTextArea content;

	static TestReportFrame frame = new TestReportFrame();
	private JButton printReport;
	
	/**
	 * Launch the application.
	 */
	static void Main() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					frame.setVisible(true);
					// 设置窗体显示在屏幕中心
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public TestReportFrame() {
		setResizable(false);
		setTitle("\u6D4B\u8BD5\u62A5\u544A\u751F\u6210\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 634, 791);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Bug\u5217\u8868\u6587\u4EF6\u4F4D\u7F6E\uFF1A");
		lblNewLabel.setBounds(10, 10, 118, 15);
		contentPane.add(lblNewLabel);

		showBugFilePath = new JTextField();
		showBugFilePath.setEditable(false);
		showBugFilePath.setBounds(127, 7, 361, 21);
		contentPane.add(showBugFilePath);
		showBugFilePath.setColumns(10);

		JButton button = new JButton("\u4FEE\u6539");
		button.setBounds(498, 6, 69, 23);
		contentPane.add(button);

		JLabel label = new JLabel("\u9879\u76EE\u540D\u79F0\uFF1A");
		label.setBounds(10, 38, 69, 15);
		contentPane.add(label);

		projectName = new JTextField();
		projectName.setBounds(82, 35, 485, 21);
		contentPane.add(projectName);
		projectName.setColumns(10);

		JLabel label_1 = new JLabel("\u7248\u672C\u4FE1\u606F\uFF1A");
		label_1.setBounds(10, 69, 69, 15);
		contentPane.add(label_1);

		Version = new JTextField();
		Version.setColumns(10);
		Version.setBounds(82, 66, 485, 21);
		contentPane.add(Version);

		label_2 = new JLabel("\u6D4B\u8BD5\u65F6\u95F4\uFF1A");
		label_2.setBounds(10, 125, 69, 15);
		contentPane.add(label_2);

		SpinnerDateModel model = new SpinnerDateModel();
		startTime = new JSpinner(model);
		startTime.setValue(new Date());
		JSpinner.DateEditor de_startTime = new JSpinner.DateEditor(startTime, "yyyy.MM.dd");
		startTime.setEditor(de_startTime);
		startTime.setBounds(82, 122, 93, 28);
		contentPane.add(startTime);

		label_3 = new JLabel("\uFF5E");
		label_3.setBounds(185, 129, 22, 15);
		contentPane.add(label_3);

		SpinnerDateModel model2 = new SpinnerDateModel();
		endTime = new JSpinner(model2);
		endTime.setValue(new Date());
		JSpinner.DateEditor de_endTime = new JSpinner.DateEditor(endTime, "yyyy.MM.dd");
		endTime.setEditor(de_endTime);
		endTime.setBounds(206, 122, 93, 28);
		contentPane.add(endTime);

		person = new JTextField();
		person.setColumns(10);
		person.setBounds(82, 160, 485, 21);
		contentPane.add(person);

		JLabel label_4 = new JLabel("\u6D4B\u8BD5\u6210\u5458\uFF1A");
		label_4.setBounds(10, 163, 69, 15);
		contentPane.add(label_4);

		JLabel label_5 = new JLabel("\u6D4B\u8BD5\u76EE\u7684\uFF1A");
		label_5.setBounds(10, 191, 69, 15);
		contentPane.add(label_5);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 216, 557, 70);
		contentPane.add(scrollPane);

		purpose = new AutoNumberJTextArea();
		purpose.setLineWrap(true);
		scrollPane.setViewportView(purpose);

		JLabel label_6 = new JLabel("\u6D4B\u8BD5\u4F9D\u636E\uFF1A");
		label_6.setBounds(10, 296, 69, 15);
		contentPane.add(label_6);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 321, 557, 70);
		contentPane.add(scrollPane_1);

		gist = new AutoNumberJTextArea();
		gist.setLineWrap(true);
		scrollPane_1.setViewportView(gist);

		JLabel lblbug = new JLabel("\u6FC0\u6D3BBug\u6570\uFF1A");
		lblbug.setBounds(10, 401, 84, 15);
		contentPane.add(lblbug);

		JLabel label_7 = new JLabel("\u4E00\u7EA7\uFF1A");
		label_7.setBounds(10, 433, 44, 15);
		contentPane.add(label_7);

		activeOneBug = new JSpinner();
		activeOneBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		activeOneBug.setBounds(48, 426, 46, 28);
		contentPane.add(activeOneBug);

		JLabel label_8 = new JLabel("\u4E8C\u7EA7\uFF1A");
		label_8.setBounds(123, 433, 44, 15);
		contentPane.add(label_8);

		activeTwoBug = new JSpinner();
		activeTwoBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		activeTwoBug.setBounds(161, 426, 46, 28);
		contentPane.add(activeTwoBug);

		JLabel label_9 = new JLabel("\u4E09\u7EA7\uFF1A");
		label_9.setBounds(235, 433, 44, 15);
		contentPane.add(label_9);

		activeThreeBug = new JSpinner();
		activeThreeBug.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updataContent();
			}
		});
		activeThreeBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		activeThreeBug.setBounds(273, 426, 46, 28);
		contentPane.add(activeThreeBug);

		JLabel label_10 = new JLabel("\u56DB\u7EA7\uFF1A");
		label_10.setBounds(345, 433, 44, 15);
		contentPane.add(label_10);

		activeFourBug = new JSpinner();
		activeFourBug.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updataContent();
			}
		});
		activeFourBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		activeFourBug.setBounds(383, 426, 46, 28);
		contentPane.add(activeFourBug);

		JLabel lblbug_1 = new JLabel("\u65B0\u63D0\u4EA4Bug\u6570\uFF1A");
		lblbug_1.setBounds(10, 458, 93, 15);
		contentPane.add(lblbug_1);

		JLabel label_12 = new JLabel("\u4E00\u7EA7\uFF1A");
		label_12.setBounds(10, 488, 44, 15);
		contentPane.add(label_12);

		newOneBug = new JSpinner();
		newOneBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		newOneBug.setBounds(48, 481, 46, 28);
		contentPane.add(newOneBug);

		JLabel label_13 = new JLabel("\u4E8C\u7EA7\uFF1A");
		label_13.setBounds(123, 488, 44, 15);
		contentPane.add(label_13);

		newTwoBug = new JSpinner();
		newTwoBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		newTwoBug.setBounds(161, 481, 46, 28);
		contentPane.add(newTwoBug);

		JLabel label_14 = new JLabel("\u4E09\u7EA7\uFF1A");
		label_14.setBounds(235, 488, 44, 15);
		contentPane.add(label_14);

		newThreeBug = new JSpinner();
		newThreeBug.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updataContent();
			}
		});
		newThreeBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		newThreeBug.setBounds(273, 481, 46, 28);
		contentPane.add(newThreeBug);

		JLabel label_15 = new JLabel("\u56DB\u7EA7\uFF1A");
		label_15.setBounds(345, 488, 44, 15);
		contentPane.add(label_15);

		newFourBug = new JSpinner();
		newFourBug.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updataContent();
			}
		});
		newFourBug.setModel(new SpinnerNumberModel(0, 0, null, 1));
		newFourBug.setBounds(383, 481, 46, 28);
		contentPane.add(newFourBug);

		JLabel label_11 = new JLabel("\u662F\u5426\u901A\u8FC7\uFF1A");
		label_11.setBounds(447, 444, 69, 15);
		contentPane.add(label_11);

		isPass = new JCheckBox("\u4E0D\u901A\u8FC7");
		isPass.setToolTipText(
				"\u6839\u636E\u4E00\u3001\u4E8C\u7EA7Bug\u7684\u6570\u91CF\u81EA\u52A8\u66F4\u6539\uFF0C\u4E5F\u53EF\u4EE5\u624B\u52A8\u66F4\u6539");
		isPass.addChangeListener(new ChangeListener() {
			// 不添加状态改变事件会导致增加一二级BUG数后复选框不自动勾选
			@Override
			public void stateChanged(ChangeEvent e) {
				if (isPass.isSelected()) {
					isPass.setText("通过");
				} else {
					isPass.setText("不通过");
				}

				updataContent();
			}
		});
		isPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isPass.isSelected()) {
					isPass.setText("通过");
				} else {
					isPass.setText("不通过");
				}

				updataContent();
			}
		});
		isPass.setBounds(447, 465, 103, 23);
		contentPane.add(isPass);

		final JButton createReport = new JButton("\u751F\u6210\u62A5\u544A");
		createReport.setBounds(10, 672, 93, 23);
		contentPane.add(createReport);

		final JButton sandMail = new JButton("\u53D1\u9001\u90AE\u4EF6");
		sandMail.setEnabled(false);
		sandMail.setBounds(241, 672, 93, 23);
		contentPane.add(sandMail);

		JButton reset = new JButton("\u91CD\u7F6E");
		reset.setBounds(359, 672, 93, 23);
		contentPane.add(reset);

		JButton showCode = new JButton("\u751F\u6210\u4EE3\u7801");
		showCode.setBounds(474, 672, 93, 23);
		contentPane.add(showCode);

		JLabel label_16 = new JLabel("\u6D4B\u8BD5\u8303\u56F4\uFF1A");
		label_16.setBounds(10, 97, 69, 15);
		contentPane.add(label_16);

		range = new JTextField();
		range.setColumns(10);
		range.setBounds(82, 94, 485, 21);
		contentPane.add(range);

		JLabel label_17 = new JLabel("\u62A5\u544A\u5185\u5BB9\uFF1A");
		label_17.setBounds(10, 519, 69, 15);
		contentPane.add(label_17);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 544, 557, 118);
		contentPane.add(scrollPane_2);

		content = new JTextArea();
		content.setToolTipText(
				"\u6D4B\u8BD5\u62A5\u544A\u7684\u5185\u5BB9\u53EF\u6839\u636E\u5176\u4E0A\u65B9\u7684\u8BBE\u7F6E\u81EA\u52A8\u53D8\u66F4\u6587\u5B57\uFF0C\u4E5F\u53EF\u4EE5\u5728\u6B64\u5904\u66F4\u6539\uFF0C\u66F4\u6539\u540E\u6587\u5B57\u5C06\u53CD\u6620\u5230\u6D4B\u8BD5\u62A5\u544A\u548C\u90AE\u4EF6\u5185\u5BB9\u4E2D");
		content.setLineWrap(true);
		scrollPane_2.setViewportView(content);

		printReport = new JButton("\u6253\u5370\u62A5\u544A");
		printReport.setEnabled(false);
		printReport.setBounds(125, 672, 93, 23);
		contentPane.add(printReport);

		final JButton refreshBug = new JButton("\u5237\u65B0Bug\u6570\u91CF");
		refreshBug.setToolTipText(
				"\u80FD\u6839\u636E\u6D4B\u8BD5\u65F6\u95F4\u6765\u91CD\u65B0\u83B7\u53D6Bug\u6570\u91CF");
		refreshBug.setBounds(326, 124, 126, 23);
		contentPane.add(refreshBug);

		activeOneBug.addChangeListener(new ChangeListener() {
			// 判断激活或者新提交的BUG是否有一级与二级的BUG，若有，则将是否通过的选择框设为勾选状态
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) activeOneBug.getValue() > 0 || (int) activeTwoBug.getValue() > 0
						|| (int) newOneBug.getValue() > 0 || (int) newTwoBug.getValue() > 0) {
					isPass.setSelected(false);
				} else {
					isPass.setSelected(true);
				}

				updataContent();
			}
		});

		activeTwoBug.addChangeListener(new ChangeListener() {
			// 判断激活或者新提交的BUG是否有一级与二级的BUG，若有，则将是否通过的选择框设为勾选状态
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) activeOneBug.getValue() > 0 || (int) activeTwoBug.getValue() > 0
						|| (int) newOneBug.getValue() > 0 || (int) newTwoBug.getValue() > 0) {
					isPass.setSelected(false);
				} else {
					isPass.setSelected(true);
				}

				updataContent();
			}
		});

		newOneBug.addChangeListener(new ChangeListener() {
			// 判断激活或者新提交的BUG是否有一级与二级的BUG，若有，则将是否通过的选择框设为勾选状态
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) activeOneBug.getValue() > 0 || (int) activeTwoBug.getValue() > 0
						|| (int) newOneBug.getValue() > 0 || (int) newTwoBug.getValue() > 0) {
					isPass.setSelected(false);
				} else {
					isPass.setSelected(true);
				}

				updataContent();
			}
		});

		newTwoBug.addChangeListener(new ChangeListener() {
			// 判断激活或者新提交的BUG是否有一级与二级的BUG，若有，则将是否通过的选择框设为勾选状态
			@Override
			public void stateChanged(ChangeEvent e) {
				if ((int) activeOneBug.getValue() > 0 || (int) activeTwoBug.getValue() > 0
						|| (int) newOneBug.getValue() > 0 || (int) newTwoBug.getValue() > 0) {
					isPass.setSelected(false);
				} else {
					isPass.setSelected(true);
				}

				updataContent();
			}
		});

		// TODO 修改按钮的点击事件
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				sandMail.setEnabled(false);
				printReport.setEnabled(false);
				refreshBug.setEnabled(true);
				TestReportMainFrame.frame.setVisible(true);
			}
		});

		// TODO 生成报告按钮点击事件
		createReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 弹出一个确认弹框的方法
				// JOptionPane.showMessageDialog(confirm, "未选择测试用例文件");
				// System.out.println(endTime.getValue().toString());

				String[] s = TestReportMainFrame.s;

				// 拼接测试时间
				StringBuilder time = new StringBuilder();
				time.append(new SimpleDateFormat("yyyy.MM.dd").format(startTime.getValue()));
				time.append("-");
				time.append(new SimpleDateFormat("yyyy.MM.dd").format(endTime.getValue()));

				TestReportMainFrame.tr.setFileName(Version.getText() + "版本测试报告");
				// new File(TestReportMainFrame.bugFileEdit.getText()).delete();
				try {
					TestReportMainFrame.tr.createReport(projectName.getText(), Version.getText(), time.toString(),
							person.getText(), range.getText(), purpose.getText(), gist.getText(), content.getText(),
							("附：" + Version.getText() + "版本Bug汇总表"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 生成报告时需要更新手动输入的BUG数量，此时需要重新获取并存储BUG数量，之后写入xml文件中
				s[0] = projectName.getText();
				s[1] = Version.getText().split("Ver")[1];
				s[2] = String.valueOf(newOneBug.getValue());
				s[3] = String.valueOf(newTwoBug.getValue());
				s[4] = String.valueOf(newThreeBug.getValue());
				s[5] = String.valueOf(newFourBug.getValue());

				// 循环，删除time变量中所有的“.”
				while (true) {
					// 判断time中是否还包含“.”，没有则结束循环
					if (time.indexOf(".") == -1) {
						break;
					}

					// 删除“.”
					time.deleteCharAt(time.indexOf("."));
				}

				try {
					TestReportMainFrame.tr.writeVersionBugNumber(s, time.toString());
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 弹出一个提示框，表示测试报告生成完毕
				JOptionPane.showMessageDialog(purpose, "测试报告生成完毕，详见：" + TestReportMainFrame.tr.getSavePath()
						+ TestReportMainFrame.tr.getFileName() + "\\" + TestReportMainFrame.tr.getFileName() + ".docx");

				sandMail.setEnabled(true);
				printReport.setEnabled(true);
				refreshBug.setEnabled(false);
			}
		});

		// TODO 打印报告按钮点击事件
		printReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 弹出提示框，提示用户确认发送邮件的信息无误后，方可发送邮件
				int choose = JOptionPane.showConfirmDialog(purpose, "请确认测试报告内容无误后，点击“是”按钮来打印测试报告");

				// 若用户点击“否”或“取消”按钮，则终止方法运行
				if (choose != 0) {
					return;
				}

				// 执行发送邮件的方法
				try {
					TestReportMainFrame.tr.printReport(
							new File(TestReportMainFrame.tr.getSavePath() + Version.getText() + "版本测试报告\\"
									+ Version.getText() + "版本测试报告.docx"),
							new SAXReader().read(TestReportMainFrame.tr.xml).getRootElement().element("report")
									.attributeValue("print_name"));
				} catch (Exception e1) {
				}

				// 弹出一个提示框，表示测试报告生成完毕
				JOptionPane.showMessageDialog(purpose, "测试报告已进入打印队列，网络问题可能会造成延迟，若长时间打印机无反应，则打印可能失败了");
			}
		});

		// TODO 发送邮件按钮点击事件
		sandMail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SandMailFrame.showFrame();
			}
		});

		// TODO 重置按钮点击事件
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniData();
			}
		});

		// TODO 生成代码按钮点击事件
		showCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 计算测试时间（天数）
				long time = ((Date) (endTime.getValue())).getTime() - ((Date) (startTime.getValue())).getTime();

				// TODO 由于平板上无法完成界面的编辑，故先将代码存储，回去后编辑文本域，使其反映到文本域上
				// 用于存储生成的代码
				StringBuilder code = new StringBuilder();

				code.append("//TODO 若需要包名，请自行添加\n");
				code.append("//package \n\n");
				code.append("import java.io.File;\n");
				code.append("import java.io.IOException;\n");
				code.append("import java.io.UnsupportedEncodingException;\n\n");
				code.append("import javax.mail.MessagingException;\n\n");
				code.append("import org.dom4j.DocumentException;\n\n");
				code.append("import pres.auxiliary.report.TestReport;\n\n");

				code.append("public class ReportCode {\n");
				code.append("\tpublic static void main(String[] args) {\n");

				code.append("\t\tTestReport tr = new TestReport();\n");
				code.append("\t\t//设置测试报告保存位置\n");
				code.append("\t\t//TODO 此处需要自行将单个反斜杠变为两个反斜杠，否则会报错\n");
				code.append("\t\ttr.setSavePath(\"" + TestReportMainFrame.tr.getSavePath() + "\");\n");
				code.append("\t\t//创建测试报告\n");
				code.append("\t\t//TODO 此处需要自行将单个反斜杠变为两个反斜杠，否则会报错\n");
				code.append("\t\ttry {\n");
				code.append("\t\t\ttr.createReport(new File(\"" + TestReportMainFrame.bugFileEdit.getText() + "\"), "
						+ ((time / 1000 / 60 / 60 / 24) + 1) + ", \"" + person.getText() + "\", \"" + range.getText()
						+ "\");\n");
				code.append("\t\t} catch (IOException e1) {\n");
				code.append("\t\t\te1.printStackTrace();\n");
				code.append("\t\t}\n\n");

				code.append("\t\t//发送邮件，可按需自行放开注释\n");
				code.append("\t\t/*\n");
				code.append("\t\ttry {\n");
				code.append("\t\t\ttr.sandMail(tr.getMailContent(), \"" + projectName.getText() + "版本测试报告" + "\", \""
						+ Version.getText() + "\", new File(tr.getSavePath() + tr.getFileName() + \".zip\"));\n");
				code.append("\t\t} catch (UnsupportedEncodingException e1) {\n");
				code.append("\t\t\te1.printStackTrace();\n");
				code.append("\t\t} catch (DocumentException e1) {\n");
				code.append("\t\t\te1.printStackTrace();\n");
				code.append("\t\t} catch (MessagingException e1) {\n");
				code.append("\t\t\te1.printStackTrace();\n");
				code.append("\t\t} catch (IOException e1) {\n");
				code.append("\t\t\te1.printStackTrace();\n");
				code.append("\t\t}\n");
				code.append("\t\t*/\n\n");

				code.append("\t\t//打印测试报告，可按需自行放开注释\n");
				code.append("\t\t/*\n");
				code.append("\t\ttry {\n");
				code.append(
						"\t\t\ttr.printReport(new File(tr.getSavePath() + tr.getFileName() + \".docx\"), new SAXReader().read(new File(\"ConfigurationFiles/ReportConfigurationFile/TestReportConfiguration.xml\")).getRootElement().element(\"report\").attributeValue(\"print_name\"));\n");
				code.append("\t\t} catch (DocumentException e) {\n");
				code.append("\t\t\te.printStackTrace();\n");
				code.append("\t\t}\n");
				code.append("\t\t*/\n\n");

				code.append("\t\t//若XML文件配置无误，则可调用以下代码：\n");
				code.append("\t\t//TestReport tr = new TestReport();\n");
				code.append("\t\t//tr.setSavePath(\"" + TestReportMainFrame.tr.getSavePath() + "\");\n");
				code.append("\t\t//tr.autoWriteReport(true, true)\n");
				code.append("\t}\n");
				code.append("}\n");
				// System.out.println(code.toString());

				// 创建存储测试报告代码存放的文件夹
				File f = new File("Code\\Report\\");
				f.mkdirs();

				// 创建写入流，保存生成的代码
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(new File(f + "\\ReportCode.java")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 写入代码
				try {
					bw.write(code.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 关闭流
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 弹出文件夹
				try {
					java.awt.Desktop.getDesktop().open(f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 弹出一个提示框，表示测试报告生成完毕
				JOptionPane.showMessageDialog(purpose,
						"代码已生成，详见项目路径下：" + new File(f + "\\ReportCode.java").getAbsolutePath());
			}
		});

		// TODO 刷新Bug数量按钮事件
		refreshBug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 读取BUG列表文件，重新刷新BUG数量
				// 读取Bug列表文件
				String[] ss = null;
				try {
					ss = TestReportMainFrame.tr.readFile(new File(TestReportMainFrame.bugFileEdit.getText()),
							projectName.getText(), Version.getText().split("Ver")[1],
							new SimpleDateFormat("yyyyMMdd").format(startTime.getValue()),
							new SimpleDateFormat("yyyyMMdd").format(endTime.getValue()));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				activeOneBug.setValue(Integer.valueOf(ss[6]));
				activeTwoBug.setValue(Integer.valueOf(ss[7]));
				activeThreeBug.setValue(Integer.valueOf(ss[8]));
				activeFourBug.setValue(Integer.valueOf(ss[9]));

				newOneBug.setValue(Integer.valueOf(ss[2]));
				newTwoBug.setValue(Integer.valueOf(ss[3]));
				newThreeBug.setValue(Integer.valueOf(ss[4]));
				newFourBug.setValue(Integer.valueOf(ss[5]));
			}
		});
	}

	/**
	 * 该方法用于在其他界面需要显示窗口时调用
	 */
	public static void showFrame() {
		iniData();
		frame.setVisible(true);
	}

	private static void iniData() {
		// 设置读取到的参数
		showBugFilePath.setText(TestReportMainFrame.bugFileEdit.getText());
		projectName.setText(TestReportMainFrame.s[0]);
		Version.setText(TestReportMainFrame.s[0] + "Ver" + TestReportMainFrame.s[1]);
		try {
			startTime.setValue(new SimpleDateFormat("yyyy.MM.dd").parse(TestReportMainFrame.s[14]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			endTime.setValue(new SimpleDateFormat("yyyy.MM.dd").parse(TestReportMainFrame.s[15]));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		range.setText(TestReportMainFrame.s[16]);
		person.setText(TestReportMainFrame.s[17]);
		purpose.setText(TestReportMainFrame.s[18]);
		gist.setText(TestReportMainFrame.s[19]);

		activeOneBug.setValue(Integer.valueOf(TestReportMainFrame.s[6]));
		activeTwoBug.setValue(Integer.valueOf(TestReportMainFrame.s[7]));
		activeThreeBug.setValue(Integer.valueOf(TestReportMainFrame.s[8]));
		activeFourBug.setValue(Integer.valueOf(TestReportMainFrame.s[9]));
		newOneBug.setValue(Integer.valueOf(TestReportMainFrame.s[2]));
		newTwoBug.setValue(Integer.valueOf(TestReportMainFrame.s[3]));
		newThreeBug.setValue(Integer.valueOf(TestReportMainFrame.s[4]));
		newFourBug.setValue(Integer.valueOf(TestReportMainFrame.s[5]));

		// 设置是否通过的复选框
		if ((int) activeOneBug.getValue() > 0 || (int) activeTwoBug.getValue() > 0 || (int) newOneBug.getValue() > 0
				|| (int) newTwoBug.getValue() > 0) {
			isPass.setSelected(false);
		} else {
			isPass.setSelected(true);
		}

		updataContent();
	}

	private static void updataContent() {
		StringBuilder testResult = new StringBuilder();
		if ((int) activeOneBug.getValue() != 0 || (int) activeTwoBug.getValue() != 0
				|| (int) activeThreeBug.getValue() != 0 || (int) activeFourBug.getValue() != 0) {
			testResult.append("回归测试发现并激活" + String.valueOf((int) activeOneBug.getValue() + (int) activeTwoBug.getValue()
					+ (int) activeThreeBug.getValue() + (int) activeFourBug.getValue()) + "个Bug，其中，");
			if ((int) activeOneBug.getValue() != 0) {
				testResult.append("一级Bug有" + (int) activeOneBug.getValue() + "个，");
			}
			if ((int) activeTwoBug.getValue() != 0) {
				testResult.append("二级Bug有" + (int) activeTwoBug.getValue() + "个，");
			}
			if ((int) activeThreeBug.getValue() != 0) {
				testResult.append("三级Bug有" + (int) activeThreeBug.getValue() + "个，");
			}
			if ((int) activeFourBug.getValue() != 0) {
				testResult.append("四级Bug有" + (int) activeFourBug.getValue() + "个，");
			}
			testResult.replace(testResult.lastIndexOf("，"), testResult.lastIndexOf("，") + 1, "；");
		}
		testResult.append("新发现并提交" + String.valueOf((int) newOneBug.getValue() + (int) newTwoBug.getValue()
				+ (int) newThreeBug.getValue() + (int) newFourBug.getValue()) + "个Bug，其中，");
		testResult.append("一级Bug有" + (int) newOneBug.getValue() + "个，");
		testResult.append("二级Bug有" + (int) newTwoBug.getValue() + "个，");
		testResult.append("三级Bug有" + (int) newThreeBug.getValue() + "个，");
		testResult.append("四级Bug有" + (int) newFourBug.getValue() + "个；");
		testResult.append("详情请查看禅道系统或者问题汇总中的Bug记录详情。");
		// 多选框是否被勾选，根据判断给出相应的文字结果
		if (!isPass.isSelected()) {
			testResult.append("由于该版本存在二级以上的Bug，根据测试依据软件测试通过准则，判定该版本测试结果为：不通过。\n");
		} else {
			testResult.append("由于该版本未发现二级以上的Bug，根据测试依据软件测试通过准则，判定该版本测试结果为：通过。\n");
		}
		
		//若有严重的BUG存在，则获取其信息列表，之后将严重的BUG信息放入报告中
		if ( (int) newOneBug.getValue() != 0 || (int) newTwoBug.getValue() != 0) {
			List<BugInformation> bugInformation = TestReportMainFrame.tr.getBugInformations();
			testResult.append("其中，严重的BUG基本信息如下：\n");
			for (int i = 0; i < bugInformation.size(); i++) {
				testResult.append("●");
				testResult.append(bugInformation.get(i).bugTitle);
				testResult.append("（"+ bugInformation.get(i).bugLv + "级BUG，禅道编号为：" + bugInformation.get(i).bugID + "）\n");
			}
		}

		//添加未处理的BUG数量
		if ( Integer.valueOf(TestReportMainFrame.s[10]) != 0 || Integer.valueOf(TestReportMainFrame.s[11]) != 0 || 
				Integer.valueOf(TestReportMainFrame.s[12]) != 0 || Integer.valueOf(TestReportMainFrame.s[13]) != 0 ) {
			testResult.append("在往期版本中，发现存在未解决、延期处理的一、二级Bug，总数为" + 
				(Integer.valueOf(TestReportMainFrame.s[10]) + 
					Integer.valueOf(TestReportMainFrame.s[11]) + 
					Integer.valueOf(TestReportMainFrame.s[12]) + 
					Integer.valueOf(TestReportMainFrame.s[13])) + "个，其中，");
			if ( Integer.valueOf(TestReportMainFrame.s[10]) != 0 ) {
				testResult.append("未解决的一级Bug有" + Integer.valueOf(TestReportMainFrame.s[10]) + "个，");
			}
			if ( Integer.valueOf(TestReportMainFrame.s[11]) != 0 ) {
				testResult.append("未解决二级Bug有" + Integer.valueOf(TestReportMainFrame.s[11]) + "个，");
			}
			if ( Integer.valueOf(TestReportMainFrame.s[12]) != 0 ) {
				testResult.append("延期解决的一级Bug有" + Integer.valueOf(TestReportMainFrame.s[12]) + "个，");
			}
			if ( Integer.valueOf(TestReportMainFrame.s[13]) != 0 ) {
				testResult.append("延期解决的二级Bug有" + Integer.valueOf(TestReportMainFrame.s[13]) + "个，");
			}
			testResult.append("望技术经理、开发人员及项目经理重视！\n");
			
		}

		content.setText(testResult.toString());
	}
}
