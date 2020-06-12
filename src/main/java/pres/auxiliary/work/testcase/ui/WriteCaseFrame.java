package pres.auxiliary.work.testcase.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;

import pres.auxiliary.tool.ui.control.AutoNumberJTextArea;
import pres.auxiliary.tool.ui.control.InputDoSearchComboBox;

class WriteCaseFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private static JTextField showCaseFilePath = new JTextField();
	
	private static WriteCaseFrame frame = new WriteCaseFrame();
	private JTextField controlName;
	private JTextField title;
	private JTextField keyword;
	private JTextField successExpectation;
	private JTextField failExpectation;
	private JTextField informationName;
	private JTextField buttonName;
	private JTextArea addInformation_precondition;
	private JTextField timeName;
	private JTextField listName;
	private JTextField searchCondition;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WriteCaseFrame frame = new WriteCaseFrame();
					frame.setVisible(true);
					//设置窗体显示在屏幕中心
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 程序的入口
	 */
	public static void uiMain() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WriteCaseFrame frame = new WriteCaseFrame();
					frame.setVisible(true);
					//设置窗体显示在屏幕中心
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public WriteCaseFrame() {
		setTitle("\u6D4B\u8BD5\u7528\u4F8B\u751F\u6210\u5668");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("\u8FD0\u884C");
		menuBar.add(menu);
		
		JMenuItem menuItem_1 = new JMenuItem("\u751F\u6210\u7528\u4F8B");
		menuItem_1.setSelected(true);
		menu.add(menuItem_1);
		
		JMenuItem menuItem = new JMenuItem("\u751F\u6210\u4EE3\u7801");
		menu.add(menuItem);
		
		JMenuItem menuItem_2 = new JMenuItem("\u6807\u8BB0\u7528\u4F8B");
		menu.add(menuItem_2);
		
		JMenuItem menuItem_3 = new JMenuItem("\u91CD\u7F6E");
		menu.add(menuItem_3);
		
		JMenu menu_1 = new JMenu("\u5E2E\u52A9");
		menuBar.add(menu_1);
		
		JMenuItem menuItem_4 = new JMenuItem("\u5173\u4E8E");
		menu_1.add(menuItem_4);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel showPanel = new JPanel();
		mainPanel.add(showPanel);
		showPanel.setLayout(null);
		
		JPanel typePanel = new JPanel();
		typePanel.setBounds(0, 0, 774, 70);
		showPanel.add(typePanel);
		typePanel.setLayout(null);
		
		JLabel label_5 = new JLabel("\u8BF7\u9009\u62E9\u6A21\u5757\uFF1A");
		label_5.setBounds(10, 42, 85, 15);
		typePanel.add(label_5);
		
		JComboBox ModlueComboBox = new InputDoSearchComboBox(new String[] {"模版1", "582", "/基础信息管理/车辆管理/车辆基础信息管理/车辆基础信息(#265)"});
		ModlueComboBox.setBounds(93, 39, 293, 21);
		typePanel.add(ModlueComboBox);
		
		JComboBox storyComboBox = new InputDoSearchComboBox(new String[] {"模版1", "582", "/基础信息管理/车辆管理/车辆基础信息管理/车辆基础信息(#265)"});
		storyComboBox.setModel(new DefaultComboBoxModel(new String[] {"\u8F68\u8FF9\u56DE\u653E(#158)", "\u8F66\u8F86\u76D1\u63A7(#157)", "\u767B\u9646\u65B9\u5F0F(#156)", "\u5E73\u53F0\u517C\u5BB9\u6027(#155)", "\u8FD0\u884C\u73AF\u5883(#154)", "\u5E76\u53D1\u5904\u7406(#153)", "\u63A5\u5165\u91CF(#152)", "\u91CC\u7A0B\u7EDF\u8BA1(#151)", "\u56FE\u50CF\u67E5\u8BE2(#150)", "\u505C\u8F66\u7EDF\u8BA1(#149)"}));
		storyComboBox.setBounds(480, 39, 290, 21);
		typePanel.add(storyComboBox);
		
		JLabel label_6 = new JLabel("\u6D4B\u8BD5\u7528\u4F8B\u6587\u4EF6\u4F4D\u7F6E\uFF1A");
		label_6.setBounds(10, 13, 122, 15);
		typePanel.add(label_6);
		
		JButton reCaseFilePathButton = new JButton("\u4FEE\u6539");
		//添加修改按钮的点击事件
		reCaseFilePathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				WriteCaseMainFrame.frame.setVisible(true);
			}
		});
		reCaseFilePathButton.setBounds(677, 9, 93, 23);
		typePanel.add(reCaseFilePathButton);
		
		showCaseFilePath.setEnabled(false);
		showCaseFilePath.setBounds(132, 10, 535, 21);
		showCaseFilePath.setText(WriteCaseMainFrame.caseFilePath);
		typePanel.add(showCaseFilePath);
		showCaseFilePath.setColumns(100);
		
		JLabel label_7 = new JLabel("\u8BF7\u9009\u62E9\u9700\u6C42\uFF1A");
		label_7.setBounds(396, 42, 85, 15);
		typePanel.add(label_7);
		
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setToolTipText("");
		tabbedPane.setBounds(0, 73, 774, 298);
		showPanel.add(tabbedPane);
		
		JPanel myCase = new JPanel();
		tabbedPane.addTab("自定义测试用例", null, myCase, null);
		myCase.setLayout(null);
		
		JLabel label = new JLabel("\u7528\u4F8B\u6807\u9898\uFF1A");
		label.setBounds(10, 10, 70, 15);
		myCase.add(label);
		
		title = new JTextField();
		title.setBounds(79, 7, 322, 21);
		myCase.add(title);
		title.setColumns(10);
		
		JLabel label_8 = new JLabel("\u4F18\u5148\u7EA7\uFF1A");
		label_8.setBounds(411, 10, 54, 15);
		myCase.add(label_8);
		
		JSpinner rank = new JSpinner();
		rank.setModel(new SpinnerNumberModel(2, 1, 4, 1));
		rank.setBounds(463, 6, 70, 22);
		myCase.add(rank);
		
		JLabel label_9 = new JLabel("\u5173\u952E\u8BCD\uFF1A");
		label_9.setBounds(542, 9, 54, 15);
		myCase.add(label_9);
		
		keyword = new JTextField();
		keyword.setColumns(10);
		keyword.setBounds(593, 7, 166, 21);
		myCase.add(keyword);
		
		JLabel label_10 = new JLabel("\u6B65\u9AA4\uFF1A");
		label_10.setBounds(10, 65, 54, 15);
		myCase.add(label_10);
		
		JLabel label_11 = new JLabel("\u9884\u671F\uFF1A");
		label_11.setBounds(10, 138, 70, 15);
		myCase.add(label_11);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(79, 37, 680, 69);
		myCase.add(scrollPane);
		
		JTextArea step = new AutoNumberJTextArea();
		step.setLineWrap(true);
		scrollPane.setViewportView(step);
		
		JLabel label_12 = new JLabel("\u524D\u7F6E\u6761\u4EF6\uFF1A");
		label_12.setBounds(10, 211, 70, 15);
		myCase.add(label_12);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(79, 184, 680, 69);
		myCase.add(scrollPane_2);
		
		JTextArea myCase_Precondition = new AutoNumberJTextArea();
		myCase_Precondition.setLineWrap(true);
		scrollPane_2.setViewportView(myCase_Precondition);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(79, 110, 680, 69);
		myCase.add(scrollPane_1);
		
		JTextArea expectation = new AutoNumberJTextArea();
		expectation.setLineWrap(true);
		scrollPane_1.setViewportView(expectation);
		
		JPanel addInformation = new JPanel();
		tabbedPane.addTab("添加信息类测试用例", null, addInformation, null);
		addInformation.setLayout(null);
		
		final JLabel label_1 = new JLabel("\u662F\u5426\u53EF\u91CD\u590D\uFF1A");
		label_1.setToolTipText("\u9009\u62E9\u8BE5\u63A7\u4EF6\u4E2D\u7684\u503C\u662F\u5426\u53EF\u4EE5\u4E0E\u5176\u4ED6\u4FE1\u606F\u4E2D\u8BE5\u63A7\u4EF6\u7684\u503C\u76F8\u540C");
		label_1.setBounds(10, 94, 79, 15);
		addInformation.add(label_1);
		
		ButtonGroup isrepeat = new ButtonGroup();
		
		final JRadioButton repeat = new JRadioButton("\u53EF\u91CD\u590D");
		repeat.setBounds(85, 91, 66, 23);
		repeat.setSelected(true);
		addInformation.add(repeat);
		
		final JRadioButton unrepeat = new JRadioButton("\u4E0D\u53EF\u91CD\u590D");
		unrepeat.setBounds(153, 91, 79, 23);
		addInformation.add(unrepeat);
		
		isrepeat.add(repeat);
		isrepeat.add(unrepeat);
		
		final JLabel label_3 = new JLabel("\u63A7\u4EF6\u540D\u79F0\uFF1A");
		label_3.setToolTipText("\u8F93\u5165\u5BF9\u5E94\u63A7\u4EF6\u7684\u540D\u79F0");
		label_3.setBounds(255, 69, 66, 15);
		addInformation.add(label_3);
		
		controlName = new JTextField();
		controlName.setBounds(320, 66, 131, 21);
		controlName.setColumns(10);
		addInformation.add(controlName);
		
		final JLabel label_4 = new JLabel("\u662F\u5426\u5FC5\u586B\uFF1A");
		label_4.setToolTipText("\u9009\u62E9\u8BE5\u63A7\u4EF6\u662F\u5426\u4E3A\u5FC5\u586B\u9879");
		label_4.setBounds(486, 69, 66, 15);
		addInformation.add(label_4);
		
		//用于添加单选按钮的关联
		ButtonGroup isMust = new ButtonGroup();
		
		final JRadioButton must = new JRadioButton("\u5FC5\u586B");
		must.setBounds(549, 66, 53, 23);
		must.setSelected(true);
		addInformation.add(must);
		
		final JRadioButton unmust = new JRadioButton("\u975E\u5FC5\u586B");
		unmust.setBounds(603, 66, 72, 23);
		addInformation.add(unmust);
		
		isMust.add(must);
		isMust.add(unmust);
		
		JLabel lblNewLabel = new JLabel("\u6210\u529F\u9884\u671F\uFF1A");
		lblNewLabel.setToolTipText("\u4FE1\u606F\u6DFB\u52A0\u6210\u529F\uFF0C\u754C\u9762\u8FD4\u56DE\u81F3\u5217\u8868\u9875\uFF0C\u4E14\u5728\u5217\u8868\u7B2C\u4E00\u6761\u663E\u793A\u65B0\u589E\u7684\u4FE1\u606F");
		lblNewLabel.setBounds(209, 13, 66, 15);
		addInformation.add(lblNewLabel);
		
		successExpectation = new JTextField();
		successExpectation.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				successExpectation.setToolTipText(successExpectation.getText());
			}
		});
		successExpectation.setText("信息添加成功，界面返回至列表页，且在列表第一条显示新增的信息");
		successExpectation.setToolTipText("");
		successExpectation.setBounds(274, 10, 202, 21);
		addInformation.add(successExpectation);
		successExpectation.setColumns(10);
		
		failExpectation = new JTextField();
		failExpectation.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				failExpectation.setToolTipText(failExpectation.getText());
			}
		});
		failExpectation.setText("信息添加失败，并给出相应的提示");
		failExpectation.setToolTipText("");
		failExpectation.setColumns(10);
		failExpectation.setBounds(274, 38, 202, 21);
		addInformation.add(failExpectation);
		
		JLabel label_13 = new JLabel("\u5931\u8D25\u9884\u671F\uFF1A");
		label_13.setToolTipText("\u4FE1\u606F\u6DFB\u52A0\u5931\u8D25\uFF0C\u5E76\u7ED9\u51FA\u76F8\u5E94\u7684\u63D0\u793A");
		label_13.setBounds(209, 41, 66, 15);
		addInformation.add(label_13);
		
		JLabel label_14 = new JLabel("\u524D\u7F6E\r\n\u6761\u4EF6\uFF1A");
		label_14.setToolTipText("\u586B\u5199\u76F8\u5E94\u7684\u524D\u7F6E\u6761\u4EF6");
		label_14.setBounds(486, 10, 72, 46);
		addInformation.add(label_14);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(557, 10, 202, 46);
		addInformation.add(scrollPane_3);
		
		addInformation_precondition = new AutoNumberJTextArea();
		addInformation_precondition.setText("1.已在添加信息界面\n2.除该信息外其他信息均正确填写");
		addInformation_precondition.setToolTipText("");
		scrollPane_3.setViewportView(addInformation_precondition);
		
		informationName = new JTextField();
		informationName.setColumns(10);
		informationName.setBounds(75, 10, 124, 21);
		addInformation.add(informationName);
		
		JLabel label_15 = new JLabel("\u4FE1\u606F\u540D\u79F0\uFF1A");
		label_15.setBounds(10, 13, 66, 15);
		addInformation.add(label_15);
		
		JLabel label_16 = new JLabel("\u6309\u94AE\u540D\u79F0\uFF1A");
		label_16.setBounds(10, 41, 66, 15);
		addInformation.add(label_16);
		
		buttonName = new JTextField();
		buttonName.setColumns(10);
		buttonName.setBounds(75, 38, 124, 21);
		addInformation.add(buttonName);
		
		JLabel label_17 = new JLabel("\u6D4B\u8BD5\u7528\u4F8B\u7C7B\u578B\uFF1A");
		label_17.setToolTipText("\u9009\u62E9\u751F\u6210\u7684\u6D4B\u8BD5\u7528\u4F8B\u6A21\u7248\u7C7B\u578B");
		label_17.setBounds(10, 69, 91, 15);
		addInformation.add(label_17);
		
		final JComboBox<String> caseType = new JComboBox<String>();
		caseType.setToolTipText("");
		caseType.setModel(new DefaultComboBoxModel(new String[] {"\u6587\u672C\u6846\u6D4B\u8BD5\u7528\u4F8B", "\u4E0B\u62C9\u83DC\u5355\u6D4B\u8BD5\u7528\u4F8B", "\u5355\u9009\u6309\u94AE\u6D4B\u8BD5\u7528\u4F8B", "\u590D\u9009\u6846\u6D4B\u8BD5\u7528\u4F8B", "\u666E\u901A\u65E5\u671F\u6D4B\u8BD5\u7528\u4F8B", "\u5F00\u59CB\u65F6\u95F4\u6D4B\u8BD5\u7528\u4F8B", "\u7ED3\u675F\u65F6\u95F4\u6D4B\u8BD5\u7528\u4F8B", "\u7535\u8BDD\u53F7\u7801\u6D4B\u8BD5\u7528\u4F8B", "\u8EAB\u4EFD\u8BC1\u6D4B\u8BD5\u7528\u4F8B", "\u4E0A\u4F20\u56FE\u7247\u6D4B\u8BD5\u7528\u4F8B", "\u4E0A\u4F20\u6587\u4EF6\u6D4B\u8BD5\u7528\u4F8B", "\u5B8C\u6574\u586B\u5199\u4FE1\u606F\u6D4B\u8BD5\u7528\u4F8B", "\u4E0D\u5B8C\u6574\u586B\u5199\u4FE1\u606F\u6D4B\u8BD5\u7528\u4F8B"}));
		caseType.setBounds(99, 66, 121, 21);
		addInformation.add(caseType);
		
		ButtonGroup isInput = new ButtonGroup();
		
		final JRadioButton uninput = new JRadioButton("\u4E0D\u53EF\u8F93\u5165");
		uninput.setEnabled(false);
		uninput.setBounds(398, 91, 79, 23);
		addInformation.add(uninput);
		
		final JRadioButton input = new JRadioButton("\u53EF\u8F93\u5165");
		input.setEnabled(false);
		input.setSelected(true);
		input.setBounds(330, 91, 66, 23);
		addInformation.add(input);
		
		isInput.add(input);
		isInput.add(uninput);
		
		final JLabel label_2 = new JLabel("\u662F\u5426\u53EF\u8F93\u5165\uFF1A");
		label_2.setEnabled(false);
		label_2.setToolTipText("\u9009\u62E9\u8BE5\u63A7\u4EF6\u4E2D\u7684\u503C\u662F\u5426\u53EF\u4EE5\u8FDB\u884C\u8F93\u5165");
		label_2.setBounds(255, 94, 79, 15);
		addInformation.add(label_2);
		
		final JLabel label_18 = new JLabel("\u5F00\u59CB\u6216\u7ED3\u675F\u65F6\u95F4\u540D\u79F0\uFF1A");
		label_18.setEnabled(false);
		label_18.setToolTipText("\u5F53\u9009\u62E9\u7684\u63A7\u4EF6\u7C7B\u578B\u4E3A\u65E5\u671F\u65F6\u53EF\u901A\u8FC7\u8F93\u5165\u5F00\u59CB\u6216\u7ED3\u675F\u65F6\u95F4\u7684\u540D\u79F0");
		label_18.setBounds(496, 94, 131, 15);
		addInformation.add(label_18);
		
		timeName = new JTextField();
		timeName.setEnabled(false);
		timeName.setColumns(10);
		timeName.setBounds(628, 91, 131, 21);
		addInformation.add(timeName);
		
		final JLabel label_19 = new JLabel("\u7535\u8BDD\u53F7\u7801\u7C7B\u578B\uFF1A");
		label_19.setEnabled(false);
		label_19.setToolTipText("\u82E5\u9009\u62E9\u7535\u8BDD\u53F7\u7801\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u52FE\u9009\u5BF9\u5E94\u7684\u53F7\u7801\u7684\u7C7B\u578B");
		label_19.setBounds(10, 117, 91, 15);
		addInformation.add(label_19);
		
		ButtonGroup phoneType = new ButtonGroup();
		
		final JRadioButton moble = new JRadioButton("\u624B\u673A\u53F7\u7801");
		moble.setEnabled(false);
		moble.setSelected(true);
		moble.setBounds(95, 114, 84, 23);
		addInformation.add(moble);
		
		final JRadioButton fixed = new JRadioButton("\u56FA\u5B9A\u7535\u8BDD");
		fixed.setEnabled(false);
		fixed.setBounds(177, 114, 84, 23);
		addInformation.add(fixed);
		
		phoneType.add(fixed);
		phoneType.add(moble);
		
		final JLabel label_20 = new JLabel("\u662F\u5426\u53EF\u91CD\u590D\u4E0A\u4F20\uFF1A");
		label_20.setEnabled(false);
		label_20.setToolTipText("\u82E5\u9009\u62E9\u4E3A\u4E0A\u4F20\u6587\u4EF6\u6216\u56FE\u7247\u7C7B\u578B\u7684\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u9009\u62E9\u662F\u5426\u53EF\u4EE5\u91CD\u590D\u4E0A\u4F20\u540C\u4E00\u4E2A\u6587\u4EF6");
		label_20.setBounds(287, 117, 107, 15);
		addInformation.add(label_20);
		
		ButtonGroup isrepeatUpdata = new ButtonGroup();
		
		final JRadioButton repeatUpdata = new JRadioButton("\u53EF\u91CD\u590D");
		repeatUpdata.setEnabled(false);
		repeatUpdata.setSelected(true);
		repeatUpdata.setBounds(389, 114, 66, 23);
		addInformation.add(repeatUpdata);
		
		final JRadioButton unrepeatUpdata = new JRadioButton("\u4E0D\u53EF\u91CD\u590D");
		unrepeatUpdata.setEnabled(false);
		unrepeatUpdata.setBounds(457, 114, 79, 23);
		addInformation.add(unrepeatUpdata);
		
		isrepeatUpdata.add(repeatUpdata);
		isrepeatUpdata.add(unrepeatUpdata);
		
		final JLabel label_21 = new JLabel("\u6587\u4EF6\u662F\u5426\u6709\u5927\u5C0F\u9650\u5236\uFF1A");
		label_21.setEnabled(false);
		label_21.setToolTipText("\u82E5\u9009\u62E9\u4E3A\u4E0A\u4F20\u6587\u4EF6\u6216\u56FE\u7247\u7C7B\u578B\u7684\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u9009\u62E9\u662F\u5426\u6709\u5BF9\u6587\u4EF6\u7684\u5927\u5C0F\u505A\u9650\u5236");
		label_21.setBounds(542, 118, 130, 15);
		addInformation.add(label_21);
		
		ButtonGroup isFileSize = new ButtonGroup();
		
		final JRadioButton is = new JRadioButton("\u6709");
		is.setEnabled(false);
		is.setSelected(true);
		is.setBounds(667, 115, 45, 23);
		addInformation.add(is);
		
		final JRadioButton no = new JRadioButton("\u65E0");
		no.setEnabled(false);
		no.setBounds(714, 115, 45, 23);
		addInformation.add(no);
		
		isFileSize.add(is);
		isFileSize.add(no);
		
		final JSpinner minLength = new JSpinner();
		minLength.setEnabled(false);
		minLength.setModel(new SpinnerNumberModel(0, -1, null, 1));
		minLength.setBounds(101, 218, 53, 22);
		addInformation.add(minLength);
		
		JLabel label_23 = new JLabel("\uFF5E");
		label_23.setBounds(157, 221, 17, 15);
		addInformation.add(label_23);
		
		final JSpinner maxLength = new JSpinner();
		maxLength.setEnabled(false);
		maxLength.setModel(new SpinnerNumberModel(0, -1, null, 1));
		maxLength.setBounds(172, 218, 53, 22);
		addInformation.add(maxLength);
		
		final JCheckBox lengthConfine = new JCheckBox("\u8F93\u5165\u957F\u5EA6\uFF1A");
		lengthConfine.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				minLength.setEnabled(lengthConfine.isSelected());
				maxLength.setEnabled(lengthConfine.isSelected());
			}
		});
		lengthConfine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				minLength.setEnabled(lengthConfine.isSelected());
				maxLength.setEnabled(lengthConfine.isSelected());
			}
		});
		lengthConfine.setToolTipText("\u82E5\u9009\u62E9\u6587\u672C\u6846\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u52FE\u9009\u662F\u5426\u6709\u8F93\u5165\u957F\u5EA6\u9650\u5236\uFF0C\u5E76\u8F93\u5165\u5176\u957F\u5EA6\u9650\u5236");
		lengthConfine.setBounds(10, 217, 91, 23);
		addInformation.add(lengthConfine);
		
		final JSpinner maxNum = new JSpinner();
		maxNum.setEnabled(false);
		maxNum.setModel(new SpinnerNumberModel(0, -1, null, 1));
		maxNum.setBounds(436, 218, 53, 22);
		addInformation.add(maxNum);
		
		JLabel label_22 = new JLabel("\uFF5E");
		label_22.setBounds(421, 221, 17, 15);
		addInformation.add(label_22);
		
		final JSpinner minNum = new JSpinner();
		minNum.setEnabled(false);
		minNum.setModel(new SpinnerNumberModel(0, -1, null, 1));
		minNum.setBounds(365, 218, 53, 22);
		addInformation.add(minNum);
		
		final JCheckBox numConfine = new JCheckBox("\u6570\u5B57\u5927\u5C0F\uFF1A");
		numConfine.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				minNum.setEnabled(numConfine.isSelected());
				maxNum.setEnabled(numConfine.isSelected());
			}
		});
		numConfine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				minNum.setEnabled(numConfine.isSelected());
				maxNum.setEnabled(numConfine.isSelected());
			}
		});
		numConfine.setToolTipText("\u82E5\u9009\u62E9\u6587\u672C\u6846\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u52FE\u9009\u662F\u5426\u6709\u6570\u5B57\u5927\u5C0F\u7684\u8F93\u5165\u9650\u5236\uFF0C\u5E76\u8F93\u5165\u5176\u5927\u5C0F\u9650\u5236");
		numConfine.setBounds(274, 217, 91, 23);
		addInformation.add(numConfine);
		
		final JCheckBox en = new JCheckBox("\u82F1\u6587");
		en.setEnabled(false);
		en.setBounds(128, 138, 53, 23);
		addInformation.add(en);
		
		final JCheckBox num = new JCheckBox("\u6570\u5B57");
		num.setEnabled(false);
		num.setBounds(187, 138, 53, 23);
		addInformation.add(num);
		
		final JCheckBox ch = new JCheckBox("\u6C49\u5B57");
		ch.setEnabled(false);
		ch.setBounds(246, 138, 53, 23);
		addInformation.add(ch);
		
		final JCheckBox spe = new JCheckBox("\u7279\u6B8A\u5B57\u7B26");
		spe.setEnabled(false);
		spe.setBounds(303, 138, 79, 23);
		addInformation.add(spe);
		
		final JCheckBox inputConfine = new JCheckBox("\u8F93\u5165\u5B57\u7B26\u9650\u5236\uFF1A");
		inputConfine.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				en.setEnabled(inputConfine.isSelected());
				num.setEnabled(inputConfine.isSelected());
				ch.setEnabled(inputConfine.isSelected());
				spe.setEnabled(inputConfine.isSelected());
			}
		});
		inputConfine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				en.setEnabled(inputConfine.isSelected());
				num.setEnabled(inputConfine.isSelected());
				ch.setEnabled(inputConfine.isSelected());
				spe.setEnabled(inputConfine.isSelected());
			}
		});
		inputConfine.setToolTipText("\u82E5\u9009\u62E9\u6587\u672C\u6846\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u53EF\u52FE\u9009\u662F\u5426\u6709\u8F93\u5165\u7684\u5B57\u7B26\u7C7B\u578B\u9650\u5236\uFF0C\u5176\u8BE6\u7EC6\u7684\u7C7B\u578B\u6709\u54EA\u4E9B");
		inputConfine.setBounds(10, 138, 116, 23);
		addInformation.add(inputConfine);
		
		final JCheckBox jpg = new JCheckBox(".jpg");
		jpg.setEnabled(false);
		jpg.setBounds(99, 163, 53, 23);
		addInformation.add(jpg);
		
		final JCheckBox gif = new JCheckBox(".gif");
		gif.setEnabled(false);
		gif.setBounds(158, 163, 53, 23);
		addInformation.add(gif);
		
		final JCheckBox png = new JCheckBox(".png");
		png.setEnabled(false);
		png.setBounds(217, 163, 53, 23);
		addInformation.add(png);
		
		final JCheckBox bmp = new JCheckBox(".bmp");
		bmp.setEnabled(false);
		bmp.setBounds(276, 163, 53, 23);
		addInformation.add(bmp);
		
		final JCheckBox doc = new JCheckBox(".doc");
		doc.setEnabled(false);
		doc.setBounds(335, 163, 53, 23);
		addInformation.add(doc);
		
		final JCheckBox docx = new JCheckBox(".docx");
		docx.setEnabled(false);
		docx.setBounds(394, 163, 57, 23);
		addInformation.add(docx);
		
		final JCheckBox xls = new JCheckBox(".xls");
		xls.setEnabled(false);
		xls.setBounds(453, 163, 53, 23);
		addInformation.add(xls);
		
		final JCheckBox xlsx = new JCheckBox(".xlsx");
		xlsx.setEnabled(false);
		xlsx.setBounds(512, 163, 57, 23);
		addInformation.add(xlsx);
		
		final JCheckBox txt = new JCheckBox(".txt");
		txt.setEnabled(false);
		txt.setBounds(571, 163, 53, 23);
		addInformation.add(txt);
		
		final JCheckBox fileConfine = new JCheckBox("\u6587\u4EF6\u9650\u5236\uFF1A");
		fileConfine.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				jpg.setEnabled(fileConfine.isSelected());
				gif.setEnabled(fileConfine.isSelected());
				png.setEnabled(fileConfine.isSelected());
				bmp.setEnabled(fileConfine.isSelected());
				//判断在下拉菜单中选择的选项是否是上传文件类的测试用例，是的话再开放非图片类格式的选择
				if ( ((String)caseType.getSelectedItem()).equals("上传文件测试用例") ) {
					doc.setEnabled(fileConfine.isSelected());
					docx.setEnabled(fileConfine.isSelected());
					xls.setEnabled(fileConfine.isSelected());
					xlsx.setEnabled(fileConfine.isSelected());
					txt.setEnabled(fileConfine.isSelected());
				}
			}
		});
		fileConfine.setEnabled(false);
		fileConfine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jpg.setEnabled(fileConfine.isSelected());
				gif.setEnabled(fileConfine.isSelected());
				png.setEnabled(fileConfine.isSelected());
				bmp.setEnabled(fileConfine.isSelected());
				//判断在下拉菜单中选择的选项是否是上传文件类的测试用例，是的话再开放非图片类格式的选择
				if ( ((String)caseType.getSelectedItem()).equals("上传文件测试用例") ) {
					doc.setEnabled(fileConfine.isSelected());
					docx.setEnabled(fileConfine.isSelected());
					xls.setEnabled(fileConfine.isSelected());
					xlsx.setEnabled(fileConfine.isSelected());
					txt.setEnabled(fileConfine.isSelected());
				}
			}
		});
		fileConfine.setToolTipText("\u82E5\u9009\u62E9\u4E0A\u4F20\u56FE\u7247\u6216\u6587\u4EF6\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u53EF\u52FE\u9009\u662F\u5426\u6709\u4E0A\u4F20\u7684\u6587\u4EF6\u683C\u5F0F\u9650\u5236\uFF0C\u5E76\u53EF\u52FE\u9009\u5177\u4F53\u7684\u9650\u5236\u6709\u54EA\u4E9B");
		fileConfine.setBounds(10, 163, 91, 23);
		addInformation.add(fileConfine);
		
		final JLabel lblNewLabel_1 = new JLabel("\u56FE\u7247\u4E0A\u4F20\u65B9\u5F0F\uFF1A");
		lblNewLabel_1.setEnabled(false);
		lblNewLabel_1.setToolTipText("\u82E5\u9009\u62E9\u4E0A\u4F20\u56FE\u7247\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u53EF\u52FE\u9009\u4E0A\u4F20\u56FE\u7247\u7684\u65B9\u5F0F");
		lblNewLabel_1.setBounds(10, 192, 91, 15);
		addInformation.add(lblNewLabel_1);
		
		final JCheckBox photographUpdata = new JCheckBox("\u62CD\u7167\u4E0A\u4F20");
		photographUpdata.setEnabled(false);
		photographUpdata.setBounds(99, 188, 80, 23);
		addInformation.add(photographUpdata);
		
		final JCheckBox folderUpdata = new JCheckBox("\u6587\u4EF6\u5939\u4E0A\u4F20");
		folderUpdata.setEnabled(false);
		folderUpdata.setBounds(177, 188, 91, 23);
		addInformation.add(folderUpdata);
		
		final JSpinner minFileNum = new JSpinner();
		minFileNum.setEnabled(false);
		minFileNum.setModel(new SpinnerNumberModel(0, -1, null, 1));
		minFileNum.setBounds(635, 218, 53, 22);
		addInformation.add(minFileNum);
		
		JLabel label_24 = new JLabel("\uFF5E");
		label_24.setBounds(691, 222, 17, 15);
		addInformation.add(label_24);
		
		final JSpinner maxFileNum = new JSpinner();
		maxFileNum.setEnabled(false);
		maxFileNum.setModel(new SpinnerNumberModel(0, -1, null, 1));
		maxFileNum.setBounds(706, 218, 53, 22);
		addInformation.add(maxFileNum);
		
		final JCheckBox fileNumConfine = new JCheckBox("\u6587\u4EF6\u4E2A\u6570\uFF1A");
		fileNumConfine.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				minFileNum.setEnabled(fileNumConfine.isSelected());
				maxFileNum.setEnabled(fileNumConfine.isSelected());
			}
		});
		fileNumConfine.setEnabled(false);
		fileNumConfine.setToolTipText("\u82E5\u9009\u62E9\u4E0A\u4F20\u6587\u4EF6\u6216\u56FE\u7247\u7C7B\u578B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u52FE\u9009\u662F\u5426\u6709\u6587\u4EF6\u4E2A\u6570\u9650\u5236\uFF0C\u5E76\u8F93\u5165\u5176\u6587\u4EF6\u4E2A\u6570");
		fileNumConfine.setBounds(544, 217, 91, 23);
		addInformation.add(fileNumConfine);
		fileNumConfine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				minFileNum.setEnabled(fileNumConfine.isSelected());
				maxFileNum.setEnabled(fileNumConfine.isSelected());
			}
		});
		
		JPanel browseList = new JPanel();
		tabbedPane.addTab("浏览列表类测试用例", null, browseList, null);
		browseList.setLayout(null);
		
		JLabel label_26 = new JLabel("\u5217\u8868\u540D\u79F0\uFF1A");
		label_26.setToolTipText("\u9700\u8981\u6DFB\u52A0\u6D4B\u8BD5\u7528\u4F8B\u7684\u5217\u8868\u540D\u79F0");
		label_26.setBounds(10, 13, 66, 15);
		browseList.add(label_26);
		
		listName = new JTextField();
		listName.setColumns(10);
		listName.setBounds(75, 10, 124, 21);
		browseList.add(listName);
		
		JLabel label_25 = new JLabel("\u6D4B\u8BD5\u7528\u4F8B\u7C7B\u578B\uFF1A");
		label_25.setToolTipText("\u9009\u62E9\u751F\u6210\u7684\u6D4B\u8BD5\u7528\u4F8B\u6A21\u7248\u7C7B\u578B");
		label_25.setBounds(220, 13, 91, 15);
		browseList.add(label_25);
		
		final JComboBox<String> broCaseType = new JComboBox<String>();
		broCaseType.setModel(new DefaultComboBoxModel(new String[] {"\u6D4F\u89C8app\u5217\u8868\u6D4B\u8BD5\u7528\u4F8B", "\u6D4F\u89C8web\u5217\u8868\u6D4B\u8BD5\u7528\u4F8B", "\u641C\u7D22\u76F8\u5173\u6D4B\u8BD5\u7528\u4F8B", "\u5217\u8868\u6570\u636E\u6392\u5E8F\u6D4B\u8BD5\u7528\u4F8B", "\u5BFC\u51FA\u5217\u8868\u6570\u636E\u6D4B\u8BD5\u7528\u4F8B", "\u5BFC\u5165\u6570\u636E\u6D4B\u8BD5\u7528\u4F8B"}));
		broCaseType.setToolTipText("");
		broCaseType.setBounds(309, 10, 121, 21);
		browseList.add(broCaseType);
		
		final JLabel label_27 = new JLabel("\u641C\u7D22\u6761\u4EF6\u7C7B\u578B\uFF1A");
		label_27.setEnabled(false);
		label_27.setToolTipText("\u82E5\u52FE\u9009\u641C\u7D22\u7C7B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u9700\u8981\u9009\u62E9\u641C\u7D22\u6761\u4EF6\u7684\u63A7\u4EF6\u7C7B\u578B\uFF0C\u5982\u4E00\u822C\u6309\u8F66\u724C\u641C\u7D22\u90FD\u662F\u8F93\u5165\u8F66\u724C\uFF0C\u5219\u6B64\u5904\u9009\u62E9\u6587\u672C\u6846");
		label_27.setBounds(220, 66, 91, 15);
		browseList.add(label_27);
		
		final JComboBox<String> searchConditionCtrolType = new JComboBox<String>();
		searchConditionCtrolType.setEnabled(false);
		searchConditionCtrolType.setModel(new DefaultComboBoxModel(new String[] {"\u6587\u672C\u6846", "\u4E0B\u62C9\u83DC\u5355", "\u65F6\u95F4\u63A7\u4EF6"}));
		searchConditionCtrolType.setToolTipText("");
		searchConditionCtrolType.setBounds(309, 63, 121, 21);
		browseList.add(searchConditionCtrolType);
		
		final JLabel label_28 = new JLabel("\u641C\u7D22\u6761\u4EF6\uFF1A");
		label_28.setToolTipText("\u82E5\u9009\u62E9\u641C\u7D22\u7C7B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u586B\u5199\u5BF9\u5217\u8868\u641C\u7D22\u7684\u6761\u4EF6\uFF0C\u5982\u901A\u8FC7\u8F66\u724C\u67E5\u627E\u8F66\u8F86\u8D44\u6E90\uFF0C\u5219\u6B64\u5904\u586B\u5199\u8F66\u724C");
		label_28.setEnabled(false);
		label_28.setBounds(10, 66, 66, 15);
		browseList.add(label_28);
		
		searchCondition = new JTextField();
		searchCondition.setEnabled(false);
		searchCondition.setColumns(10);
		searchCondition.setBounds(75, 63, 124, 21);
		browseList.add(searchCondition);
		
		final JCheckBox isTimeSlot = new JCheckBox("\u65F6\u95F4\u6BB5\u641C\u7D22");
		isTimeSlot.setToolTipText("\u82E5\u9009\u62E9\u7684\u65F6\u95F4\u7C7B\u578B\u7684\u63A7\u4EF6\uFF0C\u5219\u53EF\u9009\u62E9\u8BE5\u63A7\u4EF6\u5BF9\u5E94\u7684\u662F\u5426\u662F\u6309\u4E00\u6BB5\u65F6\u95F4\u641C\u7D22\uFF0C\u82E5\u662F\u5219\u52FE\u9009\uFF0C\u53CD\u4E4B\u4E5F\u4E0D\u52FE\u9009");
		isTimeSlot.setEnabled(false);
		isTimeSlot.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if ( isTimeSlot.isSelected() ) {
					isTimeSlot.setText("时间段搜索");
				} else {
					isTimeSlot.setText("非时间段搜索");
				}
			}
		});
		isTimeSlot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isTimeSlot.isSelected() ) {
					isTimeSlot.setText("时间段搜索");
				} else {
					isTimeSlot.setText("非时间段搜索");
				}
			}
		});
		isTimeSlot.setSelected(true);
		isTimeSlot.setBounds(456, 62, 103, 23);
		isTimeSlot.setVisible(false);
		browseList.add(isTimeSlot);
		
		final JCheckBox isSelectData = new JCheckBox("\u53EF\u52FE\u9009\u6570\u636E");
		isSelectData.setToolTipText("\u82E5\u9009\u62E9\u4E3A\u5BFC\u51FA\u6570\u636E\u7C7B\u6D4B\u8BD5\u7528\u4F8B\uFF0C\u5219\u53EF\u9009\u62E9\u662F\u5426\u5141\u8BB8\u5728\u5217\u8868\u4E0A\u52FE\u9009\u6570\u636E\uFF0C\u5141\u8BB8\u5219\u9009\u62E9\uFF0C\u4E0D\u5141\u8BB8\u5219\u53BB\u6389\u9009\u62E9");
		isSelectData.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if ( isSelectData.isSelected() ) {
					isSelectData.setText("可勾选数据");
				} else {
					isSelectData.setText("不可勾选数据");
				}
			}
		});
		isSelectData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( isSelectData.isSelected() ) {
					isSelectData.setText("可勾选数据");
				} else {
					isSelectData.setText("不可勾选数据");
				}
			}
		});
		isSelectData.setSelected(true);
		isSelectData.setBounds(456, 9, 103, 23);
		isSelectData.setVisible(false);
		browseList.add(isSelectData);
		
		JPanel login = new JPanel();
		tabbedPane.addTab("登录类测试用例", null, login, null);
		login.setLayout(null);
		
		JLabel label_29 = new JLabel("\u6D4B\u8BD5\u7528\u4F8B\u7C7B\u578B\uFF1A");
		label_29.setToolTipText("\u9009\u62E9\u751F\u6210\u7684\u6D4B\u8BD5\u7528\u4F8B\u6A21\u7248\u7C7B\u578B");
		label_29.setBounds(10, 13, 91, 15);
		login.add(label_29);
		
		final JComboBox<String> loginCaseType = new JComboBox<String>();
		loginCaseType.setModel(new DefaultComboBoxModel(new String[] {"\u6B63\u786E\u767B\u5F55\u7C7B\u6D4B\u8BD5\u7528\u4F8B", "\u975E\u5E38\u89C4\u767B\u5F55\u7C7B\u6D4B\u8BD5\u7528\u4F8B", "\u9A8C\u8BC1\u7801\u767B\u5F55\u6D4B\u8BD5\u7528\u4F8B", "\u4E0D\u540C\u6743\u9650\u767B\u5F55\u6D4B\u8BD5\u7528\u4F8B"}));
		loginCaseType.setToolTipText("");
		loginCaseType.setBounds(99, 10, 121, 21);
		login.add(loginCaseType);
		
		final JCheckBox isSplitLogin = new JCheckBox("\u65E0\u9700\u5206\u5F00\u767B\u5F55");
		isSplitLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSplitLogin.isSelected()) {
					isSplitLogin.setText("需要分开登录");
				} else {
					isSplitLogin.setText("无需分开登录");
				}
			}
		});
		isSplitLogin.setToolTipText("\u82E5\u9009\u62E9\u6743\u9650\u767B\u5F55\u7C7B\u6D4B\u8BD5\u7528\u4F8B\u65F6\uFF0C\u53EF\u52FE\u9009\u662F\u5426\u9700\u8981\u5206\u5F00\u767B\u5F55\uFF0C\u5373\u4E0D\u540C\u6743\u9650\u7684\u7528\u6237\u5728\u4E0D\u540C\u7684\u754C\u9762\u767B\u5F55\uFF0C\u82E5\u52FE\u9009\u5219\u8868\u793A\u9700\u8981\u5206\u9875\uFF0C\u53CD\u4E4B\u4E5F\u4E0D\u9700\u8981");
		isSplitLogin.setBounds(249, 9, 103, 23);
		isSplitLogin.setVisible(false);
		login.add(isSplitLogin);
		
		JPanel operateInformation = new JPanel();
		tabbedPane.addTab("编辑信息类测试用例", null, operateInformation, null);
		
		JPanel map = new JPanel();
		tabbedPane.addTab("地图类测试用例", null, map, null);
		
		JButton generateCase = new JButton("\u751F\u6210\u7528\u4F8B");
		generateCase.setBounds(197, 375, 93, 23);
		showPanel.add(generateCase);
		
		JButton generateCode = new JButton("\u751F\u6210\u4EE3\u7801");
		generateCode.setBounds(300, 375, 93, 23);
		showPanel.add(generateCode);
		
		JButton reset = new JButton("\u6807\u8BB0\u7528\u4F8B");
		reset.setBounds(403, 375, 93, 23);
		showPanel.add(reset);
		
		JButton caseTab = new JButton("\u91CD\u7F6E");
		caseTab.setBounds(506, 375, 93, 23);
		showPanel.add(caseTab);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 408, 764, 122);
		showPanel.add(panel);
		panel.setLayout(null);
		
		//TODO 添加信息类测试用例界面，测试用例类型下拉框控制的控件
		caseType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				//获取当前下拉框中选中的选项下标
				int index = caseType.getSelectedIndex();
				//判断下标的值，根据不同的值以隐藏其控件
				switch (index) {
				case 0://文本框测试用例
					label_1.setEnabled(true);
					repeat.setEnabled(true);
					unrepeat.setEnabled(true);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(true);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(true);
					numConfine.setEnabled(true);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 1://下拉菜单测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 2://单选按钮测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 3://复选框测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 4://普通日期测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(true);
					input.setEnabled(true);
					uninput.setEnabled(true);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 5://开始时间测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(true);
					input.setEnabled(true);
					uninput.setEnabled(true);
					label_18.setEnabled(true);
					timeName.setEnabled(true);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 6://结束时间测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(true);
					input.setEnabled(true);
					uninput.setEnabled(true);
					label_18.setEnabled(true);
					timeName.setEnabled(true);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 7://电话号码测试用例
					label_1.setEnabled(true);
					repeat.setEnabled(true);
					unrepeat.setEnabled(true);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(true);
					moble.setEnabled(true);
					fixed.setEnabled(true);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 8://身份证测试用例
					label_1.setEnabled(true);
					repeat.setEnabled(true);
					unrepeat.setEnabled(true);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 9://上传图片测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(true);
					repeatUpdata.setEnabled(true);
					unrepeatUpdata.setEnabled(true);
					label_21.setEnabled(true);
					is.setEnabled(true);
					no.setEnabled(true);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(true);
					lblNewLabel_1.setEnabled(true);
					photographUpdata.setEnabled(true);
					folderUpdata.setEnabled(true);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(true);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 10://上传文件测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(true);
					repeatUpdata.setEnabled(true);
					unrepeatUpdata.setEnabled(true);
					label_21.setEnabled(true);
					is.setEnabled(true);
					no.setEnabled(true);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(true);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(true);
					
					label_3.setEnabled(true);
					controlName.setEnabled(true);
					label_4.setEnabled(true);
					must.setEnabled(true);
					unmust.setEnabled(true);
					break;
				case 11://完整填写信息测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(false);
					controlName.setEnabled(false);
					label_4.setEnabled(false);
					must.setEnabled(false);
					unmust.setEnabled(false);
					break;
				case 12://不完整填写信息测试用例
					label_1.setEnabled(false);
					repeat.setEnabled(false);
					unrepeat.setEnabled(false);
					label_2.setEnabled(false);
					input.setEnabled(false);
					uninput.setEnabled(false);
					label_18.setEnabled(false);
					timeName.setEnabled(false);
					label_19.setEnabled(false);
					moble.setEnabled(false);
					fixed.setEnabled(false);
					label_20.setEnabled(false);
					repeatUpdata.setEnabled(false);
					unrepeatUpdata.setEnabled(false);
					label_21.setEnabled(false);
					is.setEnabled(false);
					no.setEnabled(false);
					inputConfine.setEnabled(false);
					inputConfine.setSelected(false);
					fileConfine.setEnabled(false);
					fileConfine.setSelected(false);
					lblNewLabel_1.setEnabled(false);
					photographUpdata.setEnabled(false);
					folderUpdata.setEnabled(false);
					lengthConfine.setEnabled(false);
					lengthConfine.setSelected(false);
					numConfine.setEnabled(false);
					numConfine.setSelected(false);
					fileNumConfine.setEnabled(false);
					fileNumConfine.setSelected(false);
					
					label_3.setEnabled(false);
					controlName.setEnabled(false);
					label_4.setEnabled(false);
					must.setEnabled(false);
					unmust.setEnabled(false);
					break;
				}
			}
		});
		
		//TODO 浏览列表类测试用例界面，测试用例类型控制的控件
		broCaseType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//判断选择的用例类型是否为搜索类型的测试用例，若是，则开放其搜索相关的控件，否则，则不开放
				if ( ((String)broCaseType.getSelectedItem()).equals("搜索相关测试用例") ) {
					label_28.setEnabled(true);
					searchCondition.setEnabled(true);
					label_27.setEnabled(true);
					searchConditionCtrolType.setEnabled(true);
					isTimeSlot.setEnabled(true);
				} else {
					label_28.setEnabled(false);
					searchCondition.setEnabled(false);
					label_27.setEnabled(false);
					searchConditionCtrolType.setEnabled(false);
					isTimeSlot.setEnabled(false);
				}
				
				//判断选择的用例类型是否为导出列表数据测试用例，若是，则显示选择是否可勾选，否则，则隐藏
				if ( ((String)broCaseType.getSelectedItem()).equals("导出列表数据测试用例") ) {
					isSelectData.setVisible(true);
				} else {
					isSelectData.setVisible(false);
				}
			}
		});
		
		//TODO 浏览列表类测试用例界面，搜索条件控件类型下拉框
		searchConditionCtrolType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ((String) searchConditionCtrolType.getSelectedItem()).equals("时间控件") ) {
					isTimeSlot.setVisible(true);
				} else {
					isTimeSlot.setVisible(false);
				}
			}
		});
		
		//TODO 登录类测试用例界面，测试用例类型下拉框
		loginCaseType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( ((String) loginCaseType.getSelectedItem()).equals("不同权限登录测试用例") ) {
					isSplitLogin.setVisible(true);
				} else {
					isSplitLogin.setVisible(false);
				}
			}
		});
	}
	
	/**
	 * 该方法用于在其他界面需要显示窗口时调用
	 */
	public static void showFrame() {
		frame.setVisible(true);
		showCaseFilePath.setText(WriteCaseMainFrame.caseFilePath);
	}
}

/**
 * 该类用于使表格中的数据进行换行的操作
 */
class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public TableCellTextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // 计算当下行的最佳高度
        int maxPreferredHeight = 0;
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            setText("" + table.getValueAt(row, i));
            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
        }
        
        // 少了这行则处理器瞎忙
        if (table.getRowHeight(row) != maxPreferredHeight) {
            table.setRowHeight(row, maxPreferredHeight);
        }
        setText(value == null ? "" : value.toString());
        
        return this;
    }
}
