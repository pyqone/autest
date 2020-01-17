package pres.auxiliary.tool.ui.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

/**
 * 实现自动编号的JTextArea控件，编号的方式为“数字.”，如“1.”，每次按下回车键即在下一行自动编号，每次清空所有的文本时则
 * 自动加上“1.”
 * @author 彭宇琦
 */
public class AutoNumberJTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 设置初始的编号及键盘监听事件，以实现自动编号
	 */
	public AutoNumberJTextArea() {
		setText("1.");
		//内部直接实现键盘监听事件
		addKeyListener(new AutoNumberJTextAreaKeyListener());
	}
	
	/**
	 * 构造自动编号的键盘监听事件类
	 * @author 彭宇琦
	 */
	private class AutoNumberJTextAreaKeyListener implements KeyListener {
		private StringBuilder sb = new StringBuilder();
		
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			//判断用户是否按下回车，按下回车后则存储当前文本域中的内容，并添加编号
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				sb.delete(0, sb.length());
				sb.append(getText());
				sb.append("\n");
				sb.append(getNumber());
				sb.append(".");
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			//判断用户释放的按钮
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				//若释放回车，则清空文本域中的内容，并将sb中的内容放入文本域中
				setText("");
				setText(sb.toString());
			} else if ( e.getKeyCode() == KeyEvent.VK_BACK_SPACE ) {
				//若按下的是退格，则判断文本域中的内容是否被清空，若被清空则添加编号“1.”
				if ( getText().equals("") ) {
					setText("1.");
				}
			}
		}
		
		/**
		 * 该方法用于判断文本当前的段落数，根据段落数返回当前行所对应的编号
		 * @return 段落所对应的编号数字
		 */
		private int getNumber() {
			//判断文本是否为空，若为空，则返回2，若不为空则返回当前所在段落的行数
			if ( sb.equals("") ) {
				return 2;
			} else {
				//存储的字符的
				return sb.toString().split("\n").length + 1;
			}
		}
	}
}
