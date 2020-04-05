package pres.auxiliary.work.old.testcase.change;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pres.auxiliary.work.old.testcase.writecase.Case;

/**
 * 该类提供标记测试用例文件中最后一条测试用例的一些方法，包括添加注释、改变整行字体颜色和改变某一步骤的字体颜色
 * 
 * @author 彭宇琦
 * @version V1.1
 * @since JDK1.7
 * @since POI3.17
 */
public class Tab {
	// 用于构造对象
	private static Tab t = null;
	// 用于指向测试用例文件
	protected static File caseFile;

	// 添加字体颜色
	/**
	 * 设置字体颜色为红色
	 */
	public static final short RED = IndexedColors.RED.getIndex();
	/**
	 * 设置字体颜色为黄色
	 */
	public static final short YELLOW = IndexedColors.YELLOW.getIndex();
	/**
	 * 设置字体颜色为蓝色
	 */
	public static final short BLUE = IndexedColors.BLUE.getIndex();
	/**
	 * 设置字体颜色为绿色
	 */
	public static final short GREEN = IndexedColors.GREEN.getIndex();

	/**
	 * 私有构造，禁止直接创建类对象
	 * */
	protected Tab(File caseFile) {
		Tab.caseFile = caseFile;
	}

	/**
	 * 该方法用于构造CaseTab对象
	 * 
	 * @param caseFile
	 *            测试用例文件对象
	 * @return Tab对象
	 */
	public static Tab newInstence(File caseFile) {
		// 判断类中的属性c是否已被构造，若未被构造则进行构造对象操作，若已被构造则将传入的文件对象赋给属性中
		if (Tab.caseFile != caseFile) {
			t = new Tab(caseFile);
		}

		return t;
	}

	/**
	 * 该方法用于向测试用例文件最后一行的第三列（即用例标题那一列）中设置一个注释
	 * 
	 * @param content
	 *            注释的内容
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab setTab(String content) throws IOException {
		XSSFWorkbook xw = before();
		return setTab(xw, Case.getInsertRowNum(), 2, content);
	}

	/**
	 * 该方法用于将最后一行标记为指定的颜色
	 * 
	 * @param color
	 *            颜色代码，可在类中选择
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab setRowColorTab(short color) throws IOException {
		// 获取工作表
		XSSFWorkbook xw = before();
		XSSFSheet xs = xw.getSheetAt(0);

		// 获取最后一行
		XSSFRow xr = xs.getRow(Case.getInsertRowNum());

		// 循环，标记该段落所有单元格的颜色
		for (int i = 0; i < xr.getLastCellNum() + 1; i++) {
			try {
				setColorTab(xr.getCell(i), color);
			} catch (NullPointerException e) {
				continue;
			}
		}

		after(xw);

		return this;
	}

	/**
	 * 该方法用于为用例某一个步骤添加颜色标记
	 * @param step 指定的步骤
	 * @param color 标记的颜色
	 * @return Tab对象
	 * @throws IOException
	 */
	public Tab setStepColorTab(int step, short color) throws IOException {
		// 获取工作表
		XSSFWorkbook xw = before();
		XSSFSheet xs = xw.getSheetAt(0);

		setStepColorTab(xw, xs.getRow(Case.getInsertRowNum()).getCell(3), xs
				.getRow(Case.getInsertRowNum()).getCell(4), step, color);

		after(xw);

		return this;
	}

	/**
	 * 该方法用于创建一个XSSFWorkbook对象
	 * 
	 * @return
	 * @throws IOException
	 */
	protected XSSFWorkbook before() throws IOException {
		FileInputStream fip = new FileInputStream(caseFile);
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		fip.close();
		
		return xw;
	}

	/**
	 * 该方法用于将修改后的表格写入测试用例文件中
	 * 
	 * @param xw
	 * @throws IOException
	 */
	protected void after(XSSFWorkbook xw) throws IOException {
		FileOutputStream fop = new FileOutputStream(caseFile);
		xw.write(fop);
		fop.close();
	}

	/**
	 * 该方法用于向单元格添加标注
	 * 
	 * @param xw
	 * @param rowNum
	 * @param content
	 * @return
	 * @throws IOException
	 */
	protected Tab setTab(XSSFWorkbook xw, int rowNum, int cellNum,
			String content) throws IOException {
		// 读取第一个工作表
		XSSFSheet xs = xw.getSheetAt(0);

		/*
		 * //创建一个poi工具类，用于创建标注 CreationHelper ch = xw.getCreationHelper();
		 * //创建一个换图对象 Drawing dr = xs.createDrawingPatriarch();
		 * //ClientAnchor是附属在WorkSheet上的一个对象，用于将标注其固定在一个单元格的左上角和右下角.
		 * ClientAnchor ca = ch.createClientAnchor();
		 */

		// 创建一个标注，并确定其在一个单元格的左上角和右下角
		Comment com = xs.createDrawingPatriarch().createCellComment(
				xw.getCreationHelper().createClientAnchor());
		// 创建标注的内容
		com.setString(xw.getCreationHelper().createRichTextString(content));
		// 创建标注的作者(作者为计算机名称)
		com.setAuthor(System.getenv().get("COMPUTERNAME"));
		// 将标注附加到单元格上
		xs.getRow(rowNum).getCell(cellNum).setCellComment(com);

		// 写入测试用例文件
		after(xw);
		return this;
	}

	/**
	 * 该方法用于对单元格设置颜色标记
	 * 
	 * @param xc
	 * @param rowNum
	 * @param cellNum
	 * @param color
	 * @throws IOException
	 */
	protected void setColorTab(XSSFCell xc, short color) throws IOException {
		// 获取单元格的样式，并设置字体颜色
		XSSFCellStyle xcs = xc.getCellStyle();
		XSSFFont xf = xcs.getFont();
		xf.setColor(color);
		// 将样式表设置回单元格中
		xc.setCellStyle(xcs);
	}

	/**
	 * 该方法用于设置用例
	 * 
	 * @param stxc
	 *            步骤单元格
	 * @param exxc
	 *            预期单元格
	 * @param step
	 *            待标记的步骤
	 * @param color
	 *            待标记的颜色
	 */
	protected void setStepColorTab(XSSFWorkbook xw, XSSFCell stxc,
			XSSFCell exxc, int step, short color) throws IOException {
		// color)
		// 如果单元格没有数据，则直接返回
		if (stxc == null || exxc == null) {
			return;
		}

		/*
		 * 颜色需要重新创建，不能直接读取 // 获取单元格的样式，并设置字体颜色 XSSFFont stxf =
		 * stxc.getCellStyle().getFont(); stxf.setColor(color); XSSFFont exxf =
		 * exxc.getCellStyle().getFont(); exxf.setColor(color);
		 */

		// 判断单元格中是否存在数据，如果不存在则直接返回
		String[] st = stxc.getStringCellValue().split("\n");
		String[] ex = exxc.getStringCellValue().split("\n");

		// 由于第一步与其他步骤的处理方式不同，故需要单独分离
		if (step == 1) {
			// 将新的样式设置入指定的步骤中
			stxc.getRichStringCellValue().applyFont(0, st[0].length(),
					font(xw, color));
			exxc.getRichStringCellValue().applyFont(0, ex[0].length(),
					font(xw, color));
		} else {
			// 判断传入的步骤是否大于当前共有的步骤，若大于则直接返回
			if (step > st.length) {
				return;
			}

			// 用于计算传入的步骤在步骤单元格与预期单元格的中的位置
			int stIndex = 0;
			int exIndex = 0;
			// 计算步骤位置的算法为传入的步骤数之前的步骤的所有文字数量
			// 循环，累加字符串的长度
			for (int i = 1; i < step; i++) {
				// 文字的数量
				stIndex += st[i - 1].length();
				// “\n”符的占位量
				stIndex += 1;
				exIndex += ex[i - 1].length();
				exIndex += 1;
			}

			// 设置字体
			stxc.getRichStringCellValue().applyFont(stIndex,
					(stIndex + st[step - 1].length()), font(xw, color));
			exxc.getRichStringCellValue().applyFont(exIndex,
					(exIndex + ex[step - 1].length()), font(xw, color));
		}
	}

	/**
	 * 创建标记的新字体
	 * 
	 * @param xw
	 * @param color
	 * @return
	 */
	private XSSFFont font(XSSFWorkbook xw, short color) {
		XSSFFont xf = xw.createFont();
		// 设置字体名称
		xf.setFontName("宋体");
		// 设置字体大小，注意，字体大小单位为磅，小四字体对应12磅
		xf.setFontHeightInPoints((short) 12);
		// 设置颜色
		xf.setColor(color);

		return xf;
	}
}
