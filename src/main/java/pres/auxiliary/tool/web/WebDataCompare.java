package pres.auxiliary.tool.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

/**
 * <p>
 * <b>文件名：</b>WebDataCompare.java
 * </p>
 * <p>
 * <b>用途：</b>用于对页面数据的操作工具
 * </p>
 * <p>
 * <b>编码时间：</b>2019年8月2日上午9:45:00
 * </p>
 * <p>
 * <b>修改时间：</b>2019年8月3日上午10:42:00
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 *
 */
public class WebDataCompare {
	/**
	 * 定义文件中的换行符
	 */
	private static final String FILE_LINK = "\r\n";
	
	/**
	 * 存储用户传入的WebDriver对象，当为null时，则使用默认的WebDriver对象，若用户定义时，则使用定义的WebDriver对象
	 */
	private static WebDriver driver = null;
	
	/**
	 * 该方法用于设置WebDriver对象
	 * @param driver WebDriver对象
	 */
	public static void setWebDriver(WebDriver driver) {
		WebDataCompare.driver = driver;
	}
	
	/**
	 * 该方法用于统计页面数据，根据数据的约束条件进行数据的组合统计，并将统计存储指定的excel文件中
	 * 
	 * @param folder     文件保存的文件夹
	 * @param fileName   文件名
	 * @param conditions 条件类对象组
	 * @param iframes    需要定位到列表所在的窗体
	 * @return 统计结果文件
	 * @throws IOException
	 */
	public static File statisData(File folder, String fileName, Condition[] conditions, String... iframes)
			throws IOException {
		Event event = null;
		if (driver == null) {
			// 构造事件类对象
			event = Event.newInstance(new ChromeBrower("resource/BrowserDriver/chromedriver.exe", 9222).getDriver());
			// 循环，添加所有的iframe
			for (String iframe : iframes) {
				event.switchFrame(iframe);
			}
		} else {
			event = Event.newInstance(driver);
		}

		// 定义map对象，用于记录列表数据
		LinkedHashMap<Condition, LinkedHashMap<String, Integer>> datas = new LinkedHashMap<Condition, LinkedHashMap<String, Integer>>();
		// 定义map，用于存储组合条件的数据
		LinkedHashMap<String, Integer> groupDatas = new LinkedHashMap<String, Integer>();

		// 创建文件夹
		folder.mkdirs();
		// 定义目标文件
		File targeFile = new File(folder, ("\\" + fileName + ".xlsx"));
		// 判断文件是否存在，若文件存在，则读取文件
		if (targeFile.exists()) {
			// 循环，抽取title，用于指定Sheet的名称，并存储该Sheet
			for (Condition condition : conditions) {
				// 构造统计map
				datas.put(condition, readFile(targeFile, condition.getTitle(), 1, 0, 1));
			}

			// 存储组合条件的map
			groupDatas = readFile(targeFile, "组合条件", 1, 0, 1);
		} else {
			// 循环，抽取title，构造map
			for (Condition condition : conditions) {
				// 构造统计map
				datas.put(condition, new LinkedHashMap<String, Integer>());
			}
		}

		// 指定当前获取列表的行数
		int index = 0;
		// 循环，获取页面每一行的数据
		while (true) {
			// 用于指定循环是否继续
			boolean hasData = true;
			// 用于判断数据是否符合约束条件
			boolean constraint = true;
			// 用于存储组合查询符合约束条件的元素值
			String constraintText = "";

			// 循环，datas中存储的所有的Condition对象，以用于获取一行数据
			for (Condition condition : datas.keySet()) {
				try {
					// 读取第index行数据
					String text = condition.getListEvent(event.getDriver()).getEvent(condition.getElement(), index).getText().getStringValve();
					// 判断该数据是否存在于condition指向的Map，若存在，则其Integer加1，不存在，则put如Map
					if (datas.get(condition).containsKey(text)) {
						datas.get(condition).put(text, datas.get(condition).get(text) + 1);
					} else {
						datas.get(condition).put(text, 1);
					}

					//判断condition中是否存储了约束条件，若无约束条件，则无需判断
					if (condition.isConstraint()) {
						// 判断constraint是否已经为false，若已经为false，则表示某一列的元素已经不符合约束，则无需再对比其他列的数据
						if (!constraint) {
							continue;
						} else {
							// 若constraint为true，则对比当前列的数据是否符合约束，并将返回值存储至constraint中
							if (constraint = condition.isConformToConstraint(text)) {
								constraintText += (condition.getTitle() + ":" + text + FILE_LINK);
							} else {
								//当constraint为false时，则直接将constraintText置为空，以便于之后存储判断
								constraintText = "";
							}
						}
					}
					// 判断该值是否符合约束条件
				} catch (IndexOutOfBoundsException e) {
					// 若列表元素已被全部读取，则此会抛出IndexOutOfBoundsException，则可直接结束循环
					hasData = false;
					break;
				}
			}
			// 判断hasData值，若hasData为false，则表示此时列表上某一列或所有列的数据读取完毕，则可以结束外层循环
			if (!hasData) {
				break;
			}
			// 判断需要存储的约束条件是否为空，若为空，则不进行存储
			//注意，其为空只会有两种情况：
			//1.constraint为true，但condition不存在约束条件，此时，循环中不走约束条件的判断，导致constraint恒为true，但constraintText仍为初始空值
			//2.condition存在约束，但其中一个元素未通过约束判断，导致constraintText置为了空值
			//故此处应用constraintText是否为空作为存储至map的条件依据
			if (!constraintText.isEmpty()) {
				constraintText = constraintText.substring(0, constraintText.length() - FILE_LINK.length());
				if (groupDatas.containsKey(constraintText)) {
					groupDatas.put(constraintText, groupDatas.get(constraintText) + 1);
				} else {
					groupDatas.put(constraintText, 1);
				}
			}

			// 初始化数据
			index++;
			constraintText = "";
		}

		//循环，读取datas中存储的所有的数据，将其一一写入文件中
		for (Condition condition : datas.keySet()) {
			writeFile(targeFile, datas.get(condition), condition.getTitle(), 0, 0, 1);
		}

		if (groupDatas.size() != 0) {
			writeFile(targeFile, groupDatas, "组合条件", 0, 0, 1);
		}
		
		//若使用默认的WebDriver对象，则关闭其对象
		if (driver == null) {
			event.getDriver().quit();
		}
		
		//打开文件夹
		java.awt.Desktop.getDesktop().open(folder);
		return targeFile;
	}

	/**
	 * 该方法用于将读取到的列表文件存储至文件中
	 * @param targeFile 目标文件
	 * @param datas 存储了数据的map类对象
	 * @param sheetName 需要写入的Sheet名称
	 * @param dataRow 从哪一行开始写
	 * @param keyCell 标题列
	 * @param valueCell 数据值列
	 * @throws IOException
	 */
	private static void writeFile(File targeFile, LinkedHashMap<String, Integer> datas, String sheetName, int dataRow,
			int keyCell, int valueCell) throws IOException {
		// 定义xlsx操作类对象，若文件存在则续写，文件不存在则新写
		XSSFWorkbook xlsx = null;
		XSSFSheet sheet = null;
		//判断文件是否存在，若文件不存在，则创建xlsx文件，若文件存在，则按照指定的数据读取xlsx
		if (targeFile.exists()) {
			xlsx = new XSSFWorkbook(new FileInputStream(targeFile));
			//判断sheet是否存在，若sheet不存在，则创建sheet
			if ((sheet = xlsx.getSheet(sheetName)) == null) {
				sheet = xlsx.createSheet(sheetName);
			}
		} else {
			xlsx = new XSSFWorkbook();
			sheet = xlsx.createSheet(sheetName);
		}
		//创建标题行
		XSSFRow xr = sheet.createRow(dataRow++);
		xr.createCell(keyCell).setCellValue("列表值");
		xr.createCell(valueCell).setCellValue("出现次数");

		//设置单元格自动换行并垂直居中
		XSSFCellStyle xcs = xlsx.createCellStyle();
		xcs.setWrapText(true);
		xcs.setVerticalAlignment(VerticalAlignment.CENTER);
		
		//循环，读取map中所有的元素
		for (String data : datas.keySet()) {
			//创建一行数据，并将行数向下移
			xr = sheet.createRow(dataRow++);
			//创建并存储列表值及出现次数
			xr.createCell(keyCell).setCellValue(data);
			xr.getCell(keyCell).setCellStyle(xcs);
			xr.createCell(valueCell).setCellValue(datas.get(data));
			xr.getCell(valueCell).setCellStyle(xcs);
		}
		
		//添加筛选项
		sheet.setAutoFilter(CellRangeAddress.valueOf((String.valueOf((char) (65 + keyCell - 1))
	            + "1:" + String.valueOf((char) (65 + keyCell))
	            + "1")));
		
		sheet.setAutoFilter(CellRangeAddress.valueOf((String.valueOf((char) (65 + valueCell - 1))
	            + "1:" + String.valueOf((char) (65 + valueCell))
	            + "1")));
		
		//写入文件
		xlsx.write(new FileOutputStream(targeFile));
		//关闭流
		xlsx.close();
	}

	/**
	 * 该方法用于读取指定的excel文件，将其转换成map进行返回
	 * @param targeFile 目标文件
	 * @param sheetName 需要读取的Sheet名
	 * @param dataRow 从哪一行开始读取
	 * @param keyCell key所在的列
	 * @param valueCell value所在的列
	 * @return 返回LinkedHashMap对象
	 * @throws IOException
	 */
	private static LinkedHashMap<String, Integer> readFile(File targeFile, String sheetName, int dataRow, int keyCell,
			int valueCell) throws IOException {
		//存储获取到的数据
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		
		XSSFWorkbook xlsx = new XSSFWorkbook(new FileInputStream(targeFile));
		//判断sheet是否存在，若sheet不存在，则创建sheet
		XSSFSheet sheet = xlsx.getSheet(sheetName);
		if (sheet == null) {
			xlsx.close();
			return map;
		}
		
		//循环，读取所有的行
		for (int i = dataRow; i < sheet.getLastRowNum() + 1; i++) {
			//读取相应的key和value，put入map中
			String key = "";
			try {
				key = sheet.getRow(i).getCell(keyCell).toString();
			} catch (NullPointerException e) {
				xlsx.close();
				throw new IncorrectFileException("文件读取错误，指定的行数：" + keyCell + "不存在");
			}
			int value = 0;
			try {
				String s = sheet.getRow(i).getCell(valueCell).toString();
				value = Integer.valueOf(s.substring(0, s.indexOf(".")));
			} catch (NumberFormatException e) {
				xlsx.close();
				throw new IncorrectFileException("统计数据“" + sheet.getRow(i).getCell(valueCell).toString() + "”无法转换成数字");
			} catch (NullPointerException e) {
				xlsx.close();
				throw new IncorrectFileException("文件读取错误，指定的行数：" + valueCell + "不存在");
			}
			map.put(key, value);
		}
		
		xlsx.close();
		return map;
	}
}
