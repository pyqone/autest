package pres.auxiliary.work.testcase.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.work.old.testcase.templet.ZentaoTemplet;

public class WriteCaseMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	//设置默认的文件保存位置
	static String saveFilePath = "C:\\AutoTest\\Case";
	//用于存储测试用例文件的路径
	static String caseFilePath = "";

	private JPanel contentPane;
	private JTextField selectFileEdit;
	private JTextField createFileEdit;
	private JTextField storyFileEdit;
	private JTextField moduleFileEdit;
	private JTextField fileNameEdit;
	
	//创建自身窗口
	static WriteCaseMainFrame frame = new WriteCaseMainFrame();
	
	// 设置选择器，选择默认位置为C:\\AutoTest\\Case
	private JFileChooser chooser = new JFileChooser(new File(saveFilePath));
	
	//部分控件需要定义在外面才能被外部方法使用
	private JButton moduleFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
	private JButton selectFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
	private JButton createFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
	private JButton storyFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
	private JLabel createFileLabel = new JLabel("\u8BF7\u9009\u62E9\u6A21\u7248\u521B\u5EFA\u4F4D\u7F6E\uFF1A");
	private JLabel selectFileLabel = new JLabel("\u8BF7\u9009\u62E9\u7528\u4F8B\u6587\u4EF6\u4F4D\u7F6E\uFF1A");
	private JLabel storyFileLabel = new JLabel("\u8BF7\u9009\u62E9\u9700\u6C42\u6587\u4EF6\u4F4D\u7F6E\uFF1A");
	private JLabel moduleFileLabel = new JLabel("\u8BF7\u9009\u62E9\u6A21\u5757\u6587\u4EF6\u4F4D\u7F6E\uFF1A");
	private JLabel fileNameLabel = new JLabel("\u8BF7\u8F93\u5165\u6A21\u7248\u6587\u4EF6\u540D\u79F0\uFF1A");
	
	/**
	 * Launch the application.
	 */
	public static void Main() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
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
	public WriteCaseMainFrame() {
		setTitle("\u6D4B\u8BD5\u7528\u4F8B\u751F\u6210\u5668");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 278);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ButtonGroup isCreate = new ButtonGroup();
		
		final JRadioButton uncreateTemplet = new JRadioButton(
				"\u5DF2\u6709\u6D4B\u8BD5\u7528\u4F8B\u6587\u4EF6\uFF0C\u9700\u8981\u7EED\u5199\u6D4B\u8BD5\u7528\u4F8B");
		uncreateTemplet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//判断单选按钮是否被被选中，若被选中则将选择文件那一类的控件设置可编辑，若未被选择，则设置为不可编辑
				if (uncreateTemplet.isSelected()) {
					enabledSelectFile(false);
				} else {
					enabledSelectFile(true);
				}
			}
		});
		uncreateTemplet.setBounds(6, 153, 321, 23);
		contentPane.add(uncreateTemplet);

		final JRadioButton createTemplet = new JRadioButton(
				"\u521B\u5EFA\u6D4B\u8BD5\u7528\u4F8B\u6587\u4EF6");
		createTemplet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//判断单选按钮是否被被选中，若被选中则将选择文件那一类的控件设置可编辑，若未被选择，则设置为不可编辑
				if (uncreateTemplet.isSelected()) {
					enabledSelectFile(false);
				} else {
					enabledSelectFile(true);
				}
			}
		});
		createTemplet.setSelected(true);
		createTemplet.setBounds(6, 6, 295, 23);
		contentPane.add(createTemplet);
		
		//关联两个单选按钮
		isCreate.add(createTemplet);
		isCreate.add(uncreateTemplet);

		selectFileLabel.setEnabled(false);
		selectFileLabel.setBounds(16, 186, 134, 15);
		contentPane.add(selectFileLabel);

		selectFileEdit = new JTextField();
		selectFileEdit.setEditable(false);
		selectFileEdit.setBounds(149, 183, 218, 21);
		contentPane.add(selectFileEdit);
		selectFileEdit.setColumns(10);

		selectFileButton.setEnabled(false);
		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//不允许用户多选
				chooser.setMultiSelectionEnabled(false);
				//1表示文件夹，0表示文件
				chooser.setFileSelectionMode(0);
				//设置文件过滤器
				chooser.setFileFilter(new FileNameExtensionFilter("请选择Excel 工作簿(.xlsx)", "xlsx"));
				
				//showOpenDialog(button)方法返回用户是否选择了文件夹
				int returnVal = chooser.showOpenDialog(selectFileButton);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//chooser.getSelectedFile()方法返回一个File对象
					selectFileEdit.setText(chooser.getSelectedFile().getAbsolutePath());
					
				}
			}
		});
		selectFileButton.setBounds(377, 182, 93, 23);
		contentPane.add(selectFileButton);
		
		final JButton confirm = new JButton("\u786E\u5B9A");
		//添加确定按钮的点击事件
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//判断当前单选框选中的是哪一个选项
				if ( createTemplet.isSelected() ) {
					//若选中的是创建文件，则创建禅道模版
					ZentaoTemplet.setSavePath(createFileEdit.getText());
					ZentaoTemplet.setFileName(fileNameEdit.getText());
					//处理文件重复的异常
					try{
						ZentaoTemplet.create();
					} catch (IncorrectDirectoryException e1) {
						JOptionPane.showMessageDialog(confirm, e1.getMessage());
						return;
					}
					
					try {
						ZentaoTemplet.readModlueData(new File(moduleFileEdit.getText()));
						ZentaoTemplet.readStoryData(new File(storyFileEdit.getText()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//存储文件保存的路径
					caseFilePath = ZentaoTemplet.getTempletFile().getAbsolutePath();
					
				} else {
					//若选择的是续写测试用例，则直接将文本框中的内容存入caseFilePath中
					//判断文本框内容是否为空，为空则弹出
					if ( !selectFileEdit.getText().equals("") ) {
						caseFilePath = selectFileEdit.getText();
					}
					else {
						JOptionPane.showMessageDialog(confirm, "未选择测试用例文件");
						return;
					}
				}
				
				//弹出WriteCaseUI界面
				WriteCaseFrame.showFrame();
				//设置本窗体不显示
				frame.setVisible(false);
			}
		});
		confirm.setBounds(104, 214, 93, 23);
		contentPane.add(confirm);
		
		JButton cancel = new JButton("\u53D6\u6D88");
		//添加取消按钮事件
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//关闭窗体
				Runtime.getRuntime().exit(0);
			}
		});
		cancel.setBounds(274, 214, 93, 23);
		contentPane.add(cancel);
		
		createFileLabel.setBounds(16, 67, 134, 15);
		contentPane.add(createFileLabel);
		
		createFileEdit = new JTextField();
		createFileEdit.setEditable(false);
		createFileEdit.setColumns(10);
		createFileEdit.setBounds(149, 64, 218, 21);
		contentPane.add(createFileEdit);
		createFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//不允许用户多选
				chooser.setMultiSelectionEnabled(false);
				//1表示文件夹，0表示文件
				chooser.setFileSelectionMode(1);
				
				//showOpenDialog(button)方法返回用户是否选择了文件夹
				int returnVal = chooser.showOpenDialog(createFileButton);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//chooser.getSelectedFile()方法返回一个File对象
					createFileEdit.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		createFileButton.setBounds(377, 63, 93, 23);
		contentPane.add(createFileButton);
		
		storyFileLabel.setBounds(16, 96, 134, 15);
		contentPane.add(storyFileLabel);
		
		storyFileEdit = new JTextField();
		storyFileEdit.setEditable(false);
		storyFileEdit.setColumns(10);
		storyFileEdit.setBounds(149, 93, 218, 21);
		contentPane.add(storyFileEdit);
		storyFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//不允许用户多选
				chooser.setMultiSelectionEnabled(false);
				//1表示文件夹，0表示文件
				chooser.setFileSelectionMode(0);
				//设置文件过滤器
				chooser.setFileFilter(new FileNameExtensionFilter("请选择Excel 工作簿(.xlsx)", "xlsx"));
				
				//showOpenDialog(button)方法返回用户是否选择了文件夹
				int returnVal = chooser.showOpenDialog(storyFileButton);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//chooser.getSelectedFile()方法返回一个File对象
					storyFileEdit.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		storyFileButton.setBounds(377, 92, 93, 23);
		contentPane.add(storyFileButton);
		
		moduleFileLabel.setBounds(16, 125, 134, 15);
		contentPane.add(moduleFileLabel);
		
		moduleFileEdit = new JTextField();
		moduleFileEdit.setEditable(false);
		moduleFileEdit.setColumns(10);
		moduleFileEdit.setBounds(149, 122, 218, 21);
		contentPane.add(moduleFileEdit);
		moduleFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//不允许用户多选
				chooser.setMultiSelectionEnabled(false);
				//1表示文件夹，0表示文件
				chooser.setFileSelectionMode(0);
				//设置文件过滤器
				chooser.setFileFilter(new FileNameExtensionFilter("请选择Excel 工作簿(.xlsx)", "xlsx"));
				
				//showOpenDialog(button)方法返回用户是否选择了文件夹
				int returnVal = chooser.showOpenDialog(moduleFileButton);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					//chooser.getSelectedFile()方法返回一个File对象
					moduleFileEdit.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		moduleFileButton.setBounds(377, 121, 93, 23);
		contentPane.add(moduleFileButton);
		createFileEdit.setText(saveFilePath);
		
		fileNameLabel.setBounds(16, 38, 134, 15);
		contentPane.add(fileNameLabel);
		
		fileNameEdit = new JTextField();
		fileNameEdit.setText("NewCaseFile");
		fileNameEdit.setColumns(10);
		fileNameEdit.setBounds(149, 35, 218, 21);
		contentPane.add(fileNameEdit);
	}
	
	/**
	 * 该方法用于控制选择模版还是创建模版文件的控件的显示与否,当传入true时，则不允许编辑选择文件控件，若传入为false则不允许编辑创建模版文件的控件
	 * @param isEnabled
	 */
	private void enabledSelectFile(boolean isEnabled) {
		fileNameEdit.setEnabled(isEnabled);
		fileNameLabel.setEnabled(isEnabled);
		
		createFileLabel.setEnabled(isEnabled);
		createFileButton.setEnabled(isEnabled);
		
		storyFileLabel.setEnabled(isEnabled);
		storyFileButton.setEnabled(isEnabled);
		
		moduleFileLabel.setEnabled(isEnabled);
		moduleFileButton.setEnabled(isEnabled);
		
		selectFileButton.setEnabled(!isEnabled);
		selectFileLabel.setEnabled(!isEnabled);
	}
}
