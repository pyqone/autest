package test.javase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class TestReadWordTable {
	public static void main(String[] args) throws IOException {
		readWeekReport(new File("E:\\test.docx"));
	}

	public static void readWeekReport(File report) throws IOException {
		// 使用POI中的读取word文件的方法
		XWPFDocument xd = new XWPFDocument(new FileInputStream(report));
		
		//记录重要信息
		//报告人姓名
		String name = "";
		//报告开始时间
		String startTime = "";//可能不需要
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 6);
		c.add(Calendar.WEEK_OF_MONTH, -1);
		c.add(Calendar.DATE, 1);
		System.out.println(new SimpleDateFormat("YYYY-MM-dd").format(c.getTime()));
		
		//工作内容
		String content = "";
		//工作天数
		String day = "";

		List<XWPFTable> tables = xd.getTables();// 得到word中的表格
		// 循环，获取文档中所有的表格
		for ( int a = 0; a < tables.size(); a++ ) {
			// 获取表格中的所有行
			List<XWPFTableRow> rows = tables.get(a).getRows();

			// 循环用于获取行中所有的列
			for (int i = 0; i < rows.size(); i++) {
				List<XWPFTableCell> cells = rows.get(i).getTableCells();

				// 用于获取列中所有的段
				for (int j = 0; j < cells.size(); j++) {
					List<XWPFParagraph> paras = cells.get(j).getParagraphs();

					// 用于读取段中所有的文字
					for (int k = 0; k < paras.size(); k++) {
						//读取第一个表格第3行第2列（姓名）
						if ( a == 0 && i == 2 && j == 1 ) {
							name = paras.get(k).getText();
						}
						
						//读取工作内容
						if( a == 1 && i > 0 && j == 2 ) {
							content += paras.get(k).getText();
							content += "*";
						}
						
						//读取工作天数
						if( a == 1 && i > 0 && j == 3 ) {
							day += paras.get(k).getText();
							day += "*";
						}
					}
				}
			}
		}
		
		//删除最后一个*号
		content.substring(0, content.lastIndexOf("*"));
		day.substring(0, day.lastIndexOf("*"));
		

		xd.close();
	}

	private static void writeSummarySheet(File templet) {

	}
}
