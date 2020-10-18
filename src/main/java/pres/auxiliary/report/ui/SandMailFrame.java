package pres.auxiliary.report.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.dom4j.DocumentException;

/**
 * FileName: SandMailFrame.java
 * 
 * 用于给出一个发送邮件确认的弹窗，在该弹窗上可以修改邮件收件人和抄送人
 * 
 * @author 彭宇琦
 * @Deta 2018年4月27日 下午4:49:26
 * @version ver1.0
 */
public class SandMailFrame extends JFrame {

	private static final long serialVersionUID = -1048290948430116615L;
	private JPanel contentPane;
	protected static JTextArea to;
	protected static JTextArea cc;
	protected static JTextArea content;
	static SandMailFrame frame = new SandMailFrame();

	/**
	 * Create the frame.
	 */
	public SandMailFrame() {
		setTitle("\u68C0\u67E5\u90AE\u4EF6\u63A5\u6536\u4EBA");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 487, 607);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u6536\u4EF6\u4EBA\uFF1A");
		label.setBounds(10, 10, 65, 15);
		contentPane.add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 414, 93);
		contentPane.add(scrollPane);
		
		to = new JTextArea();
		to.setEditable(false);
		to.setLineWrap(true);
		scrollPane.setViewportView(to);
		
		JLabel label_1 = new JLabel("\u6284\u9001\u4EBA\uFF1A");
		label_1.setBounds(10, 138, 65, 15);
		contentPane.add(label_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 163, 414, 93);
		contentPane.add(scrollPane_1);
		
		cc = new JTextArea();
		cc.setEditable(false);
		cc.setLineWrap(true);
		scrollPane_1.setViewportView(cc);
		
		JButton yes = new JButton("\u786E\u5B9A");
		yes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行发送邮件的方法
				try {
					TestReportMainFrame.tr.sandMail(TestReportFrame.content.getText(),
							TestReportFrame.projectName.getText(),
							(TestReportFrame.Version.getText() + "版本测试报告"), TestReportFrame.range.getText(), 
							new File(TestReportMainFrame.tr.getSavePath()
									+ TestReportMainFrame.tr.getFileName()
									+ ".zip"));
				} catch (DocumentException | MessagingException
						| IOException e1) {
					e1.printStackTrace();
				}

				new File(TestReportMainFrame.tr.getSavePath()
						+ TestReportMainFrame.tr.getFileName() + ".zip")
								.delete();
				
				frame.setVisible(false);
				// 弹出一个提示框，表示测试报告生成完毕
				JOptionPane.showMessageDialog(TestReportFrame.purpose, "邮件已发送成功，请在邮箱中查看");
			}
		});
		yes.setBounds(171, 498, 93, 23);
		contentPane.add(yes);
		
		JButton no = new JButton("\u53D6\u6D88");
		no.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//隐藏窗口
				frame.setVisible(false);
			}
		});
		no.setBounds(331, 498, 93, 23);
		contentPane.add(no);
		
		JButton refresh = new JButton("\u5237\u65B0");
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//刷新数据，即调用初始化数据方法
				initData();
			}
		});
		refresh.setBounds(11, 498, 93, 23);
		contentPane.add(refresh);
		
		JLabel label_2 = new JLabel("\u5185\u5BB9\u9884\u89C8\uFF1A");
		label_2.setBounds(10, 266, 94, 15);
		contentPane.add(label_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 291, 414, 197);
		contentPane.add(scrollPane_2);
		
		content = new JTextArea();
		content.setLineWrap(true);
		scrollPane_2.setViewportView(content);
	}
	
	public static void showFrame() {
		initData();
		frame.setVisible(true);
	}
	
	/**
	 * 初始化数据
	 */
	public static void initData() {
		//根据项目名称获取邮件的收件人与抄送人
		String[][] strs = TestReportMainFrame.tr.getMailToAndCc(TestReportFrame.projectName.getText());
		
		StringBuilder sb = new StringBuilder();
		//获取收件人
		for (String s : strs[0]) {
			sb.append(s + "\n");
		}
		sb.delete(sb.lastIndexOf("\n"), sb.length());
		
		//填写收件人到界面中
		to.setText(sb.toString());
		
		//清空存储的收件人
		sb.delete(0, sb.length());
		
		//获取抄送人
		for (String s : strs[1]) {
			sb.append(s + "\n");
		}
		sb.delete(sb.lastIndexOf("\n"), sb.length());
		
		//填写抄送人到界面中
		cc.setText(sb.toString());
		
		String text = "";
		try {
			text = TestReportMainFrame.tr.getMailContent(TestReportFrame.content.getText(),
					TestReportFrame.projectName.getText(),
					(TestReportFrame.Version.getText() + "版本测试报告"), TestReportFrame.range.getText());
		} catch (DocumentException | MessagingException | IOException e) {
			e.printStackTrace();
		}
		//在预览信息中添加文本
		content.setText(text);
	}
}
