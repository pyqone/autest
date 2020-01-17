package pres.auxiliary.report.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pres.auxiliary.report.TestReport;

/**
 * 测试报告主界面，用于设置报告生成位置以及Bug列表文件的位置
 * @author 彭宇琦
 * @version Ver1.0
 */
public class TestReportMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	static TestReport tr = new TestReport();

	// 设置默认的文件保存位置
	static String saveFilePath = "C:\\AutoTest\\Report";

	/**
	 * 存储测试报告中的所有内容，以下是存储的信息：<br/>
	 * <table border="1">
	 * 	<tr>
	 * 		<td>s[0]:项目名称</td>
	 * 		<td>s[1]:当前版本</td>
	 *	 	<td>s[2]:1级BUG数量</td>
	 *		<td>s[3]:2级BUG数量</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>s[4]:3级BUG数量</td>
	 *	 	<td>s[5]:4级BUG数量</td>
	 * 		<td>s[6]:激活1级BUG数量</td>
	 * 		<td>s[7]:激活2级BUG数量</td>
	 * 	</tr>
	 * 	<tr>
	 *	 	<td>s[8]:激活3级BUG数量</td>
	 * 		<td>s[9]:激活4级BUG数量</td>
	 * 		<td>s[10]:未解决的1级BUG数量</td>
	 *	 	<td>s[11]:未解决的2级BUG数量</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>s[12]:延期解决的1级BUG数量</td>
	 * 		<td>s[13]:延期解决的2级BUG数量</td>
	 *	 	<td>s[14]:测试开始时间</td>
	 *		<td>s[15]:测试结束时间</td>
	 * 	</tr>
	 *  <tr>
	 *  	<td>s[16]:测试范围</td>
	 *		<td>s[17]:测试人员</td>
	 * 		<td>s[18]:测试目的</td>
	 * 		<td>s[19]:测试依据</td>
	 * 	</tr>
	 * </table>
	 * <br/>
	 * <font size="3"><b>千万不要随意改动顺序，需要添加则向下加，不要覆盖前面的！否则主界面会有BUG!</b></font>
	 * 
	 */
	static String[] s = new String[20];

	private JPanel contentPane;
	static JTextField bugFileEdit;
	private JTextField reportSavePathEdit;

	// 创建自身窗口
	static TestReportMainFrame frame = new TestReportMainFrame();

	// 设置选择器，选择默认位置为C:\\AutoTest\\Report
	private JFileChooser chooser = new JFileChooser(new File(saveFilePath));
	private JButton createFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
	private JButton storyFileButton = new JButton("\u9009\u62E9\u6587\u4EF6");
	private JLabel createFileLabel = new JLabel(
			"\u8BF7\u9009\u62E9Bug\u6587\u4EF6\u4F4D\u7F6E\uFF1A");
	private JLabel storyFileLabel = new JLabel(
			"\u8BF7\u9009\u62E9\u62A5\u544A\u4FDD\u5B58\u4F4D\u7F6E\uFF1A");

	public static void main(String[] args) {
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
	 * 可以通过该方法调用测试报告主界面
	 */
	public static void Main() {
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
	public TestReportMainFrame() {
		setTitle("\u6D4B\u8BD5\u62A5\u544A\u751F\u6210\u5668");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 207);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JButton confirm = new JButton("\u786E\u5B9A");
		confirm.setBounds(193, 90, 93, 23);
		contentPane.add(confirm);

		JButton cancel = new JButton("\u53D6\u6D88");
		// 添加取消按钮事件
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 关闭窗体
				Runtime.getRuntime().exit(0);
			}
		});
		cancel.setBounds(369, 90, 93, 23);
		contentPane.add(cancel);

		createFileLabel.setBounds(10, 14, 134, 15);
		contentPane.add(createFileLabel);

		bugFileEdit = new JTextField();
		bugFileEdit.setEditable(false);
		bugFileEdit.setColumns(10);
		bugFileEdit.setBounds(143, 11, 218, 21);
		contentPane.add(bugFileEdit);
		createFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 不允许用户多选
				chooser.setMultiSelectionEnabled(false);
				// 1表示文件夹，0表示文件
				chooser.setFileSelectionMode(0);
				// 设置其选择的路径
				chooser.setCurrentDirectory(new File(bugFileEdit.getText()));
				// 判断文件是否存在，若存在，则设置在下一次打开选择文件窗口时能选中当前选择的文件
				if (bugFileEdit.getText().isEmpty()) {
					chooser.setSelectedFile(new File(bugFileEdit.getText()));
				}
				// 设置文件过滤器
				chooser.setFileFilter(new FileNameExtensionFilter(
						"请选择Excel文件或CSV文件(.xlsx .xls .csv)", "xlsx", "xls", "csv"));

				// showOpenDialog(button)方法返回用户是否选择了文件夹
				int returnVal = chooser.showOpenDialog(createFileButton);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// chooser.getSelectedFile()方法返回一个File对象
					bugFileEdit.setText(
							chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		createFileButton.setBounds(371, 10, 93, 23);
		contentPane.add(createFileButton);

		storyFileLabel.setBounds(10, 50, 134, 15);
		contentPane.add(storyFileLabel);

		reportSavePathEdit = new JTextField();
		reportSavePathEdit.setText("C:\\AutoTest\\Report");
		reportSavePathEdit.setEditable(false);
		reportSavePathEdit.setColumns(10);
		reportSavePathEdit.setBounds(143, 47, 218, 21);
		contentPane.add(reportSavePathEdit);
		storyFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 不允许用户多选
				chooser.setMultiSelectionEnabled(false);
				// 1表示文件夹，0表示文件
				chooser.setFileSelectionMode(1);
				// 设置其选择的路径
				chooser.setCurrentDirectory(
						new File(reportSavePathEdit.getText()));

				// showOpenDialog(button)方法返回用户是否选择了文件夹
				int returnVal = chooser.showOpenDialog(storyFileButton);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// chooser.getSelectedFile()方法返回一个File对象
					reportSavePathEdit.setText(
							chooser.getSelectedFile().getAbsolutePath());
				}
				
				//设置测试报告的存放路径
				tr.setSavePath(reportSavePathEdit.getText() + "\\");
			}
		});

		storyFileButton.setBounds(371, 46, 93, 23);
		contentPane.add(storyFileButton);

		JButton btnbug = new JButton("\u5BFC\u51FA\u6587\u4EF6");
		btnbug.setToolTipText("\u6839\u636E\u914D\u7F6E\u6587\u4EF6\u4E2D\u7684\u5185\u5BB9\uFF0C\u81EA\u52A8\u5411\u7985\u9053\u4E0A\u5BFC\u51FABug\u5217\u8868\u6587\u4EF6\uFF0C\u5E76\u5C06\u8DEF\u5F84\u586B\u5165Bug\u6587\u4EF6\u4F4D\u7F6E\u4E2D");
		btnbug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Document dom = null;
				try {
					dom = new SAXReader().read(tr.xml);
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}

				// 获取基本信息并存储
				String url = dom.getRootElement().element("url")
						.attributeValue("url");
				String firefoxDirectory = dom.getRootElement()
						.element("firefox").attributeValue("directory");
				String firefoxProfiles = dom.getRootElement().element("firefox")
						.attributeValue("profiles");
				String username = dom.getRootElement().element("user")
						.attributeValue("username");
				String password = dom.getRootElement().element("user")
						.attributeValue("password");
				String search = dom.getRootElement().element("project")
						.attributeValue("name");
				String downloadSaveDirectory = dom.getRootElement()
						.element("firefox")
						.attributeValue("download_save_directory");
				String downloadFileName = dom.getRootElement().element("report")
						.attributeValue("download_file_name");

				// 对禅道进行操作
				try {
					tr.operationZendao(firefoxDirectory, firefoxProfiles, url,
							username, password, search, downloadFileName);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				bugFileEdit.setText(
						downloadSaveDirectory + downloadFileName + ".csv");
			}
		});
		btnbug.setBounds(10, 90, 93, 23);
		contentPane.add(btnbug);

		// 添加确定按钮的点击事件
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				
				Document dom = null;
				try {
					dom = new SAXReader().read(tr.xml);
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}

				// 读取Bug列表文件
				String[] ss = null;
				try {
					ss = tr.readFile(new File(bugFileEdit.getText()),
							Integer.valueOf(dom.getRootElement()
									.element("report").attributeValue("days")));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// 记录从BUG列表文件中读取到的信息
				int i = 0;
				for (String str : ss) {
					s[i++] = str;
				}

				// 存储测试开始和结束时间
				// 判断是否需要修改测试时间，若不需要则自动计算，需要则直接读取
				if (dom.getRootElement().element("report").element("content")
						.element("time").attributeValue("is_change")
						.equalsIgnoreCase("false")) {
					// 存储测试结束时间，即当前的时间
					Date d = new Date();
					// 设置测试开始时间，调用Calendar类进行处理
					Calendar calendar = Calendar.getInstance();
					// 设置当前时间
					calendar.setTime(d);
					// 设置当前的天数减去测试时间，得到测试开始时间
					calendar.add(Calendar.DAY_OF_MONTH,
							(1 - Integer.valueOf(
									dom.getRootElement().element("report")
											.attributeValue("days"))));
					// 将得到的时间转换为Date进行返回
					d = calendar.getTime();
					// 存储测试开始时间
					s[i++] = new SimpleDateFormat("yyyy.MM.dd").format(d);

					d = new Date();
					s[i++] = new SimpleDateFormat("yyyy.MM.dd").format(d);
					// d.setDate(d.getDate() - testDay);//方法过时
				} else {
					s[i++] = dom.getRootElement().element("report")
							.element("content").element("time")
							.element("starttime").getText();
					s[i++] = dom.getRootElement().element("report")
							.element("content").element("time")
							.element("endtime").getText();
				}

				// 添加测试范围
				s[i++] = dom.getRootElement().element("report")
						.attributeValue("range");

				// 添加参与测试的人员
				@SuppressWarnings("unchecked")
				List<Element> l = dom.getRootElement().element("report")
						.element("testperson").elements();
				// 初始化存储的空间
				s[i] = "";
				for (Element ee : l) {
					if (ee.attributeValue("participation")
							.equalsIgnoreCase("true")) {
						s[i] += ee.attributeValue("name");
						s[i] += "、";
					}
				}
				// 截断字符串，使其不包含最后一个顿号
				s[i] = s[i].substring(0, s[i].lastIndexOf("、"));
				// 下标前进1
				i++;

				// 添加测试目的
				@SuppressWarnings("unchecked")
				List<Element> purposes = (List<Element>) dom.getRootElement()
						.element("report").element("content").element("purpose")
						.elements();
				s[i] = "";
				// 获取测试目的
				for (int j = 0; j < purposes.size(); j++) {
					s[i] += ((j + 1) + "." + purposes.get(j).getText() + "\n");
				}
				// 截断字符串，使其不包含最后一个顿号
				s[i] = s[i].substring(0, s[i].lastIndexOf("\n"));
				// 下标前进1
				i++;
				
				// 读取配置文件中的测试依据
				StringBuilder gist = new StringBuilder();
				@SuppressWarnings("unchecked")
				List<Element> gists = (List<Element>) dom.getRootElement()
						.element("report").element("content").element("gist")
						.elements();
				s[i] = "";
				// 获取测试目的
				for (int j = 0; j < gists.size(); j++) {
					// 修改带${name}的
					if (gists.get(j).getText().indexOf("${name}") > -1) {
						gist.append((j + 1) + "." + gists.get(j).getText() + "\n");
						//注意，s[0]中存储的是项目名称
						gist.replace(gist.indexOf("$"), gist.indexOf("}") + 1, s[0]);
					} else {
						gist.append((j + 1) + "." + gists.get(j).getText() + "\n");
					}
				}
				s[i] = gist.toString();
				// 截断字符串，使其不包含最后一个换行符
				s[i] = s[i].substring(0, s[i].lastIndexOf("\n"));
				
//				// 设置本窗体不显示
				frame.setVisible(false);
//				 弹出TestReportFrame界面
				TestReportFrame.showFrame();
			}
		});
	}
}
