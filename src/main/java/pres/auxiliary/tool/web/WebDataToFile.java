package pres.auxiliary.tool.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * <p><b>文件名：</b>WebDataToFile.java</p>
 * <p><b>用途：</b>用于从页面爬取数据以文本的形式存储至本地的工具</p>
 * <p><b>编码时间：</b>2019年7月19日下午5:04:20</p>
 * <p><b>修改时间：</b>2019年7月20日下午6:17:20</p>
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class WebDataToFile {
	/**
	 * 定义元素行间的分隔方式
	 */
	private static final String LINE = "\n";
	
	/**
	 * 该方法可用于获取页面的数据，并以docx的文件形式进行存储
	 * @param folder 生成的文件存放位置
	 * @param fileName 生成的文件名
	 * @param element 需要获取文本的元素
	 * @param iframes 定位到元素前所需进入的iframe
	 * @return 返回生成的文件对象
	 * @throws IOException
	 */
	public static File webTextDataToFile(File folder, String fileName, String element, String... iframes) throws IOException {
		//定义浏览器对象，该对象暂时定义为已开启的浏览器对象
		Event event = Event.newInstance(new ChromeBrower("resource/BrowserDriver/chromedriver.exe", 9222).getDriver());
		
		//循环，添加所有的框架
		for (String iframe : iframes) {
			event.switchFrame(iframe);
		}
		
		//获取页面数据
		String text = event.getTextEvent().getText(element).getStringValve().trim();
		
		//返回生成的文件的文件对象
		return writeData(folder, fileName, text, false);
	}
	
	/**
	 * 该方法可用于获取页面列表上的一页数据，并以xlsx的文件形式进行存储
	 * @param folder 生成的文件存放位置
	 * @param fileName 生成的文件名
	 * @param title 列表的标题
	 * @param element 元素的定位方式
	 * @param iframes 定位到元素前所需进入的iframe
	 * @return 返回生成的文件对象
	 * @throws IOException
	 */
	public static File webListDataToFile(File folder, String fileName, String title, String element, String... iframes) throws IOException {
		//定义事件类，用于定位iframe
		Event event = Event.newInstance(new ChromeBrower("resource/BrowserDriver/chromedriver.exe", 9222).getDriver());
		
		//循环，添加所有的框架
		for (String iframe : iframes) {
			event.switchFrame(iframe);
		}
				
		//定义列表元素操作类对象
		DataListEvent listEvent = new DataListEvent(event.getDriver());
		//存储从页面上获取到的元素，并存储传入的标题
		String text = title + LINE;
		
		//获取页面列表数据
		listEvent.add(element);
		//循环，将获取到的文本存储至text中
		for(ListEvent e : listEvent.getEvents(element)) {
			text += (e.getText().getStringValve() + LINE);
		}
		
		//返回生成的文件的文件对象
		return writeData(folder, fileName, text, true);
	}
	
	/**
	 * 该方法用于将数据写入相应的文件中
	 * @param targeFile 目标文件对象
	 * @param text 需要写入文件的文本
	 * @param list 定义写入数据的方式是否为列表 
	 * @return 生成的文件对象
	 * @throws IOException 
	 */
	private static File writeData(File folder, String fileName, String text, boolean list) throws IOException {
		//创建文件夹
		folder.mkdirs();
		File targeFile = null;
		
		//判断写入数据的方式是否为列表，为列表，则按照excel形式存储，为文本则按照docx形式存储
		if (list) {
			//定义需要存储的目标文件
			targeFile = new File(folder + "\\" + fileName + ".xlsx");
			//打开文件夹
			java.awt.Desktop.getDesktop().open(folder);
			return writeXlsx(targeFile, text);
		} else {
			//定义需要存储的目标文件
			targeFile = new File(folder + "\\" + fileName + ".docx");
			//打开文件夹
			java.awt.Desktop.getDesktop().open(folder);
			return writeDocx(targeFile, text);
		}
	}
	
	/**
	 * 用于对docx文本的写入
	 * @param targeFile 目标文件
	 * @param text 文本
	 * @return 生成的文件对象
	 * @throws IOException
	 */
	private static File writeDocx(File targeFile, String text) throws IOException {
		//定义docx操作类对象，若文件存在则续写，文件不存在则新写
		XWPFDocument docx = null;
		if (targeFile.exists()) {
			docx = new XWPFDocument(new FileInputStream(targeFile));
		} else {
			docx = new XWPFDocument();
		}
		
		//创建新的段落，并写入text中的内容
		docx.createParagraph().createRun().setText(text);
		docx.write(new FileOutputStream(targeFile));
		docx.close();
		
		return targeFile;
	}
	
	/**
	 * 用于对xlsx文本的写入
	 * @param targeFile 目标文件
	 * @param text 文本
	 * @return 生成的文件
	 */
	private static File writeXlsx(File targeFile, String text) throws IOException {
		//切割内容，便于后续的存储
		String[] texts = text.split("\\n");
		
		//定义xlsx操作类对象，若文件存在则续写，文件不存在则新写
		XSSFWorkbook xlsx = null;
		XSSFSheet sheet = null;
		//定义需要写入文件中的行和列，用于指向文本内容在文件中的某列的某行写入
		int row = 0;
		int cell = 0;
		//定义循环时需要从texts的哪一个元素开始循环读取
		int i = 0;
		if (targeFile.exists()) {
			xlsx = new XSSFWorkbook(new FileInputStream(targeFile));
			sheet = xlsx.getSheetAt(0);
			
			//获取文件的标题，用于判断该标题是否存在于文件中
			String title = texts[0].trim();
			//循环，读取文件中标题行的所有列，以对比标题是否存在
			for (; cell < sheet.getRow(0).getLastCellNum(); cell++) {
				//对比标题是否存在，若存在，则结束循环，则可以得到cell的值
				if (sheet.getRow(0).getCell(cell).toString().equals(title)) {
					break;
				}
				
			}
			
			//判断当前需要写入的列是否为最后一列，若为不为最后一列，则不需要记录标题列，其i值设为1，表示从texts的第二个元素读取
			//并记录从哪一行开始写入数据
			if (cell != sheet.getRow(0).getLastCellNum()) {
				i = 1;
				//循环，判断当前的应从当前列的哪一行开始写入数据
				//需要注意的是，此处去sheet中的最大行数作为循环结束的标志，这是因为文本中最长的列为sheet.getLastRowNum()，若该列的数据
				//相较sheet.getLastRowNum()短时则直接结束循环，其结束的row + 1便是可写入数据的行
				for (; row < sheet.getLastRowNum(); row++) {
					if (sheet.getRow(row).getCell(cell) == null) {
						row--;
						break;
					}
				}
				System.out.println(row);
				
				row++;
			}
		} else {
			//由于文件不存在，则不需要计算任何的循环值（row、cell、i都为0）
			xlsx = new XSSFWorkbook();
			sheet = xlsx.createSheet();
		}
		
		//写入数据
		for (; i < texts.length; i++, row++) {
			XSSFRow xr = null;
			//判断当前行是否存在，存在则不创新创建，以免清空之前的数据
			if ((xr = sheet.getRow(row)) == null) {
				xr = sheet.createRow(row);
			}
			xr.createCell(cell).setCellValue(texts[i]);
		}
		
		xlsx.write(new FileOutputStream(targeFile));
		xlsx.close();
		
		return targeFile;
	}
}
