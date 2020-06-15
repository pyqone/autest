package pres.auxiliary.work.selenium.datadriven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;

import pres.auxiliary.directory.exception.IncorrectDirectoryException;
import pres.auxiliary.report.IncompatibleFileLayoutException;

/**
 * 该类用于在自动化测试中读取数据驱动，可通过该类中提供的方法，将数据驱动中的信息读取出来，文件支持csv、xlsx、xls和txt文件格式，
 * 但txt文件中的数据需要以\t作为数据间的分隔，换行作为行间的分隔。类支持通过列数查询列数据，亦支持通过列名查找列数据，在编辑数据驱动文件时，
 * 第一行可以是标题行（若为标题行，则在添加数据驱动文件时传入true作为首航为标题行的依据），也可以不是，但一定要保证首行的数据完整性，详见{@link #addDataFile(File, boolean)}
 * 
 * @author 彭宇琦
 * @Deta 2018年8月28日 10:58:31
 */
public class DataDriven {
	// 定义存储DataDriverList类型的Map，其键为数据列的名称
	private HashMap<String, DataDriven.DataDriverList> dataDriver = new HashMap<String, DataDriven.DataDriverList>();
	// 定义存储数据列名称的列表，用于指定其列的位置，以便于操作数据列表
	private ArrayList<String> dataDriverListName = new ArrayList<String>();
	//存储标题行的位置
	private boolean oneRowIsTitle = false;

	// 用于判断是否忽略空行
	private boolean emptyCell = true;

	// 用于存储当前返回数据的下标
	private int nowIndex = 0;
	// 用于存储列表中最短的一列，由于是要存储最小值，所以初始化为int类型的最大值，使其最小值可以被存储
	private int minListLength = Integer.MAX_VALUE;

	// 用于存储数据驱动文件
	private File dataFile = null;

	// 用于存储需要读取的列名
	private String[] readListName;

	/**
	 * 用于指定数据驱动文件
	 * 
	 * @param dataFile 数据驱动文件对象
	 * @throws DataNotFoundException 当文件中未包含数据时，抛出该异常
	 */
	public DataDriven(File dataFile, boolean oneRowIsTitle) throws DataNotFoundException {
		addDataFile(dataFile, oneRowIsTitle);
	}

	/**
	 * 无参构造，不做任何操作，请使用{@link #setDataFile(File)}方法定义数据驱动文件
	 */
	public DataDriven() {
	}

	/**
	 * 用于添加数据驱动文件对象，调用该方法时，默认文件中不包含标题，若文件中的第一行为标题行，则请调用{@link #addDataFile(File, boolean)}
	 * 
	 * @param dataFile 数据驱动文件
	 * @throws DataNotFoundException 当文件中未包含数据时，抛出该异常
	 */
	public void addDataFile(File dataFile) throws DataNotFoundException {
		addDataFile(dataFile, false);
	}
	
	/**
	 * 用于添加数据驱动文件对象，当数据驱动文件中的第一行为标题行时，则设置oneRowIsTitle为true，若不包含标题行，则传入false.
	 * 注意，标题行只能在文件第一行
	 * @param dataFile 数据驱动文件
	 * @param oneRowIsTitle 是否存在
	 * @throws DataNotFoundException
	 */
	public void addDataFile(File dataFile, boolean oneRowIsTitle) throws DataNotFoundException {
		this.dataFile = dataFile;
		this.oneRowIsTitle = oneRowIsTitle;
		// 设置文件后直接读取该文件中的数据
		try {
			readData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 将所有列的列名存入readListName中
		setReadAllList();
	}

	/**
	 * 用于返回是否允许忽略空行，若该开关为false，则表示不忽略空位置，即若遇到空位时，则以空字符串代替；为true，则在遇到空位时不进行存储
	 * 
	 * @return
	 */
	public boolean isEmptyCell() {
		return emptyCell;
	}

	/**
	 * 用于设置是否允许忽略空行，若该开关为false，则表示不忽略空位置，即若遇到空位时，则以空字符串代替；为true，则在遇到空位时不进行存储。默认情况下为true
	 * 
	 * @param emptyCell
	 */
	public void setEmptyCell(boolean emptyCell) {
		this.emptyCell = emptyCell;
	}

	/**
	 * 用于返回需要读取的列的名称
	 * 
	 * @return 需要读取的列名
	 */
	public String[] getReadListName() {
		return readListName;
	}

	/**
	 * 用于设置需要读取的列。该方法亦可作为数据列读取顺序排序使用，即读取数据驱动时，会根据此处传入的列名的顺序进行读取
	 * 
	 * @param readListName 需要读取的列名
	 */
	public void setReadListName(String... readListName) {
		if (0 == readListName.length) {
			return;
		}

		this.readListName = readListName;
	}
	
	public void setReadAllList() {
		// 将所有列的列名存入readListName中
		readListName = new String[dataDriverListName.size()];
		readListName = dataDriverListName.toArray(readListName);
	}

	/**
	 * 用于返回需要读取的列的序号
	 * 
	 * @return 需要读取的列序号
	 */
	public int[] getReadListId() {
		int[] indexs = new int[readListName.length];

		// 循环，遍历readListName，查找其序号
		for (int j = 0; j < readListName.length; j++) {
			for (int i = 0; i < dataDriverListName.size(); i++) {
				if (readListName[j].equals(dataDriverListName.get(i))) {
					indexs[j] = i;
					break;
				}
			}
		}

		return indexs;
	}

	/**
	 * 用于根据列的序号设置需要读取的列。该方法亦可作为数据列读取顺序排序使用，即读取数据驱动时，会根据此处传入的列名的顺序进行读取
	 * @param indexs 需要读取的列的序号
	 */
	public void setReadListId(int... indexs) {
		if (0 == indexs.length) {
			return;
		}
		
		readListName = new String[indexs.length];
		int i = 0;
		for (int index : indexs) {
			//若传入的参数小于0或者大于列名数组的长度时，则跳过
			if (index < 0 || index >= dataDriverListName.size()) {
				continue;
			}
			readListName[i++] = dataDriverListName.get(index);
		}
	}
	
	/**
	 * 该方法用于返回指向的数据驱动 文件
	 * @return 数据驱动文件
	 */
	public File getDataFile() {
		return dataFile;
	}

	/**
	 * 该方法用于获取列的个数
	 * 
	 * @return 列的个数
	 */
	public int getColumnLength() {
		return dataDriverListName.size();
	}

	/**
	 * 用于返回当前列表最短列的行数
	 * 
	 * @return 最短列的行数
	 */
	public int getMinRowLength() {
		return minListLength;
	}

	/**
	 * 该方法用于返回某列列表的名称
	 * 
	 * @param index 指定的列
	 * @return 下标对应的列表名称
	 */
	public String getListName(int index) {
		return dataDriverListName.get(index);
	}
	
	/**
	 * 用于返回当前正在读取到的行数
	 * @return 当前正在读取到的行数
	 */
	public int getNowIndex() {
		return nowIndex;
	}

	/**
	 * 用于设置下次读取的行数，若行数大于当前最短行的行数或未存储数据时，则设置无效
	 * @param nowIndex 设置读取的行数
	 */
	public void setNowIndex(int nowIndex) {
		if (dataDriverListName.size() == 0) {
			return;
		}
		
		if (nowIndex >= minListLength) {
			return;
		}
		
		for (String listName : dataDriverListName) {
			dataDriver.get(listName).setNowIndex(nowIndex);
		}
		
		this.nowIndex = nowIndex;
	}

	/**
	 * 用于根据新列名来替换原列名
	 * 
	 * @param oldListName 原列表的名称
	 * @param newListName 新列表的名称
	 */
	public void replaceListName(String oldListName, String newListName) {
		// 替换原名称列表中的名称对应的下标，将其作为参数，传入重载方法中
		for (int i = 0; i < dataDriverListName.size(); i++) {
			if (dataDriverListName.get(i).equals(oldListName)) {
				replaceListName(i, newListName);
				break;
			}
		}
		
		//同步更新需要读取的列中的字段
		if (readListName != null) {
			for ( int i = 0; i < readListName.length; i++ ) {
				if (readListName[i].equals(oldListName)) {
					readListName[i] = newListName;
					break;
				}
			}
		}
	}

	/**
	 * 用于根据列的下表来替换原列名
	 * @param index 列的下标
	 * @param newListName 新的列名
	 */
	public void replaceListName(int index, String newListName) {
		String oldListName = dataDriverListName.get(index);
		// 删除原key，将返回被删除key对应的value重新存储到新的key中
		dataDriver.put(newListName, dataDriver.remove(oldListName));
		// 替换原名称列表中的名称
		dataDriverListName.set(index, newListName);
		
		//同步更新需要读取的列中的字段
		if (readListName != null) {
			for ( int i = 0; i < readListName.length; i++ ) {
				if (readListName[i].equals(oldListName)) {
					readListName[i] = newListName;
					break;
				}
			}
		}
	}
	
	/**
	 * 该方法用于返回制定的一行的数据，注意，该方法返回的列数与设置需要返回的列数相关，详见{@link #setReadListId(int...)}和{@link #setReadListName(String...)}
	 * @param row 制定的行
	 * @return 该行对应的数据
	 */
	public String[] getRow(int row) {
		//判断数据列表中是否存在数据
		if (0 == readListName.length) {
			return null;
		}
		//判断下标是否正确
		if ( row > minListLength || row < 0) {
			throw new ArrayIndexOutOfBoundsException("下标越界：" + row);
		}
		
		String[] rows = new String[readListName.length];
		//循环，读取所有可读的列
		int index = 0;
		for ( String name : readListName ) {
			rows[index++] = dataDriver.get(name).get(row);
		}
		
		return rows;
	}
	
	/**
	 * 用于根据列序号返回指定的列
	 * @param column 指定的列数
	 * @return 列数对应的一列数据
	 */
	public String[] getColumn(int column) {
		//判断数据列表中是否存在数据
		if (0 == readListName.length) {
			return null;
		}
		//判断下标是否正确
		if ( column > dataDriverListName.size() || column < 0) {
			throw new ArrayIndexOutOfBoundsException("下标越界：" + column);
		}
		
		return getColumn(dataDriverListName.get(column));
	}
	
	/**
	 * 用于根据列名返回指定的列
	 * @param column 指定的列名
	 * @return 列数对应的一列数据
	 */
	public String[] getColumn(String listName) {
		//判断数据列表中是否存在数据
		if (0 == readListName.length) {
			return null;
		}
		//判断下标是否正确
		if (!dataDriver.containsKey(listName)) {
			throw new ArrayIndexOutOfBoundsException("列名不正确");
		}
		
		DataDriverList data = dataDriver.get(listName);
		return data.get(0, data.size());
	}

	/**
	 * 判断当前读取的到的行数是否已经与当前列的最小行数一致，若小于最小行数，则返回false，反之，则返回true
	 * 
	 * @return
	 */
	public boolean hasNext() {
		if (minListLength == nowIndex) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 读取并返回列表下一行数据，需要保证hasNext()为true，否则将抛出异常
	 * 
	 * @return 列表下一行数据
	 */
	public String[] next() {
		if (!hasNext()) {
			throw new ArrayIndexOutOfBoundsException("列表元素已全部返回");
		}

		String[] datas = new String[readListName.length];

		// 循环，遍历列表，读取其中的值
		int i = 0;
		for (String listName : readListName) {
			//若该列的列名不存在，则不读取
			if (!dataDriver.containsKey(listName)) {
				continue;
			}
			// 存储数据
			datas[i++] = dataDriver.get(listName).next();
		}

		nowIndex++;
		return datas;
	}

	/**
	 * 该方法用于保存当前存储的数据
	 * @param newDataFile
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public void saveData(File newDataFile) throws IOException, InvalidFormatException {
		// 判断数据驱动文件是否为空，若为空则抛出FileNotFoundException异常
		if (newDataFile == null) {
			throw new FileNotFoundException("未定义数据驱动文件对象");
		}
		
		// 返回文件的绝对路径，并将其路径按照“.”来拆分，以获取文件的后缀名
		String[] temp = newDataFile.getAbsolutePath().split("\\.");
		// 判断传入的路径是否包含文件路径（存在后缀时则表示路径包含文件）
		if (temp.length == 1) {
			throw new IncorrectDirectoryException("指定的路径不是文件所在路径");
		}
		// 通过判断后，则获取数组中最后一个元素，该元素即为文件的后缀名
		String fileTyp = temp[temp.length - 1];
		
		//创建保存数据驱动的文件夹，可无视该文件夹是否存在
		newDataFile.getParentFile().mkdirs();
		
		switch (fileTyp) {
		case "txt":
			saveTxt(newDataFile);
			break;
		case "xls":
			saveXls(newDataFile);
			break;
		case "xlsx":
			saveXlsx(newDataFile);
			break;
		case "csv":
			saveCsv(newDataFile);
			break;
		default:
			throw new IncompatibleFileLayoutException("不兼容的数据驱动文件格式：." + fileTyp);
		}
	}

	/**
	 * 该方法用于读取数据驱动文件中的数据将其数据存入到指定的容器中
	 * 
	 * @throws DataNotFoundException
	 */
	private void readData() throws IOException, DataNotFoundException {
		// 判断数据驱动文件是否为空，若为空则抛出FileNotFoundException异常
		if (dataFile == null) {
			throw new FileNotFoundException("未定义数据驱动文件对象");
		}

		// 返回文件的绝对路径，并将其路径按照“.”来拆分，以获取文件的后缀名
		String[] temp = dataFile.getAbsolutePath().split("\\.");
		// 判断传入的路径是否包含文件路径（存在后缀时则表示路径包含文件）
		if (temp.length == 1) {
			throw new IncorrectDirectoryException("指定的路径不是文件所在路径");
		}
		// 通过判断后，则获取数组中最后一个元素，该元素即为文件的后缀名
		String fileTyp = temp[temp.length - 1];
		//存储当前列的长度，以方便后续 的读取
		int nowListLength = dataDriverListName.size();
		
		switch (fileTyp) {
		case "txt":
			readTxt();
			break;
		case "xls":
			readXls();
			break;
		case "xlsx":
			readXlsx();
			break;
		case "csv":
			readCsv();
			break;
		default:
			throw new IncompatibleFileLayoutException("不兼容的数据驱动文件格式：." + fileTyp);
		}

		// 遍历数据驱动，查找最短列，并记录其行数
		// TODO 可优化，由于多次读取文件后，其已经存储了前次读取的得到的最短列的行数，若再次读取驱动文件时，可不用再把原有的列重新遍历
		for (int i = nowListLength; i < dataDriverListName.size(); i++) {
			int length = 0;
			minListLength = (length = dataDriver.get(dataDriverListName.get(i)).size()) > minListLength ? minListLength : length;
		}
	}

	/**
	 * 用于读取.csv文件格式
	 * 
	 * @throws IOException
	 * @throws DataNotFoundException
	 */
	private void readCsv() throws IOException, DataNotFoundException {
		// 读取csv文件，将其数据转换成List<String[]>类型
		CSVReader csvReader = new CSVReader(new FileReader(this.dataFile));
		List<String[]> dataFileList = csvReader.readAll();
		csvReader.close();

		// 判断是否获取到数据，若未获取到数据，则抛出数据为找到的异常
		if (dataFileList.size() == 0) {
			throw new DataNotFoundException("文件中未包含数据");
		}
		
		// 循环，读取数据的所有列，由于openCSV是按照行来读取列表，故需要将其转换
		// 由于CSV文件会自动补全空缺的列，故无需先获取最大值
		for (int cell = 0; cell < dataFileList.get(0).length; cell++) {
			// 定义数据列的名称
			String listName = ("NewDataList" + (cell + 1));
			
			DataDriverList dataList = new DataDriverList();
			for (int row = 0; row < dataFileList.size(); row++) {
				if (oneRowIsTitle && row == 0) {
					listName = dataFileList.get(row)[cell];
					//判断该列名是否已经存在，若存在，则为其加上标号
					int i = 1;
					while(dataDriverListName.indexOf(listName) != -1) {
						//如果该名称加上当前标号后不重复，则将其加上标号，并结束循环
						if ( dataDriverListName.indexOf(listName + i) == -1 ) {
							listName += i;
							break;
						}
						//若列名仍然重复，则标号自增，重新循环判断，避免重复累加标号
						i++;
					}
				} else {
					// 若读取的位置不为空，则进行正常进行存储
					if (!dataFileList.get(row)[cell].isEmpty()) {
						dataList.add(dataFileList.get(row)[cell]);
					} else {
						// 若读取的位置为空，则再判断是否忽略空位置，若不忽略空位置，则将该列存储为空字符串
						if (!isEmptyCell()) {
							dataList.add("");
						}
					}
				}
			}

			// 存储当前遍历的列，并给予起初始的名称“NewDataListX”
			dataDriver.put(listName, dataList);
			// 存储列表的名称
			dataDriverListName.add(listName);
		}
	}

	/**
	 * 用于读取xlsx文件格式
	 * 
	 * @throws IOException
	 * @throws DataNotFoundException
	 */
	private void readXlsx() throws IOException, DataNotFoundException {
		// 读取导出的bug列表文件
		FileInputStream fip = new FileInputStream(dataFile);
		// 通过XSSFWorkbook对表格文件进行操作
		XSSFWorkbook xw = new XSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 读取工作簿
		XSSFSheet xs = xw.getSheetAt(0);
		// 如果未读取到数据，则抛出DataNotFoundException
		if (xs.getLastRowNum() == 0) {
			xw.close();
			throw new DataNotFoundException("文件中未包含数据");
		}

		// 读取所有的元素，按照先列后行的方式读取，便于存储数据
		// 读取所有的列
		for (int i = 0; i < xs.getRow(0).getLastCellNum(); i++) {
			// 定义数据列的名称
			String listName = ("NewDataList" + (i + 1));
			// 定义DataDriverList对象
			DataDriverList dataList = new DataDriverList();
			// 读取所有的行
			for (int j = 0; j < xs.getLastRowNum() + 1; j++) {
				if (oneRowIsTitle && j == 0) {
					listName = xs.getRow(j).getCell(i).toString();
					//判断该列名是否已经存在，若存在，则为其加上标号
					int index = 1;
					while(dataDriverListName.indexOf(listName) != -1) {
						//如果该名称加上当前标号后不重复，则将其加上标号，并结束循环
						if ( dataDriverListName.indexOf(listName + index) == -1 ) {
							listName += index;
							break;
						}
						//若列名仍然重复，则标号自增，重新循环判断，避免重复累加标号
						index++;
					}
				} else {
					try {
						String text = xs.getRow(j).getCell(i).toString();
						text = text.indexOf(".0") > -1 ? text.substring(0, text.indexOf(".0")) : text;
						dataList.add(text);
					} catch (NullPointerException e) {
						// 当抛出NullPointerException，则表示读取的位置为空，则再判断是否忽略空位置，若不忽略空位置，则将该列存储为空字符串
						if (!isEmptyCell()) {
							dataList.add("");
						}
						continue;
					}
				}
			}
			
			// 存储当前遍历的列，并给予起初始的名称“NewDataListX”
			dataDriver.put(listName, dataList);
			// 存储列表的名称
			dataDriverListName.add(listName);
		}

		xw.close();
	}

	/**
	 * 用于读取xls文件格式
	 * 
	 * @throws IOException
	 * @throws DataNotFoundException
	 */
	private void readXls() throws IOException, DataNotFoundException {
		// 读取导出的bug列表文件
		FileInputStream fip = new FileInputStream(dataFile);
		// 通过XSSFWorkbook对表格文件进行操作
		HSSFWorkbook hw = new HSSFWorkbook(fip);
		// 关闭流
		fip.close();

		// 读取工作簿
		HSSFSheet hs = hw.getSheetAt(0);
		// 如果未读取到数据，则抛出DataNotFoundException
		if (hs.getLastRowNum() == 0) {
			hw.close();
			throw new DataNotFoundException("文件中未包含数据");
		}

		// 读取所有的元素，按照先列后行的方式读取，便于存储数据
		// 读取所有的列
		for (int i = 0; i < hs.getRow(0).getLastCellNum(); i++) {
			// 定义数据列的名称
			String listName = ("NewDataList" + (i + 1));
			
			// 定义DataDriverList对象
			DataDriverList dataList = new DataDriverList();
			// 读取所有的行
			for (int j = 0; j < hs.getLastRowNum() + 1; j++) {
				if (oneRowIsTitle && j == 0) {
					listName = hs.getRow(j).getCell(i).toString();
					//判断该列名是否已经存在，若存在，则为其加上标号
					int index = 1;
					while(dataDriverListName.indexOf(listName) != -1) {
						//如果该名称加上当前标号后不重复，则将其加上标号，并结束循环
						if ( dataDriverListName.indexOf(listName + index) == -1 ) {
							listName += index;
							break;
						}
						//若列名仍然重复，则标号自增，重新循环判断，避免重复累加标号
						index++;
					}
				} else {
					try {
						String text = hs.getRow(j).getCell(i).toString();
						text = text.indexOf(".0") > -1 ? text.substring(0, text.indexOf(".0")) : text;
						dataList.add(text);
					} catch (NullPointerException e) {
						// 当抛出NullPointerException，则表示读取的位置为空，则再判断是否忽略空位置，若不忽略空位置，则将该列存储为空字符串
						if (!isEmptyCell()) {
							dataList.add("");
						}
						continue;
					}
				}
			}
			
			// 存储当前遍历的列，并给予起初始的名称“NewDataListX”
			dataDriver.put(listName, dataList);
			// 存储列表的名称
			dataDriverListName.add(listName);
		}

		hw.close();
	}

	/**
	 * 用于读取txt文件格式，但仅支持tab最为列的分隔，换行最为行的分隔
	 * 
	 * @throws IOException
	 * @throws DataNotFoundException
	 */
	private void readTxt() throws IOException, DataNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(this.dataFile));
		String text = "";
		String[] rows;

		// 先读取一行数据，若无数据，则抛出异常
		text = br.readLine();
		rows = text.split("\\t");
		if (rows.length == 0) {
			br.close();
			throw new DataNotFoundException("文件中未包含数据");
		}
		
		//用于存储当前数据列的长度以及加上切分后的长度，以便于向其后继续添加数据列
		int nowListNumber = dataDriverListName.size();
		int newListNumber = dataDriverListName.size() + rows.length;
		//判断数据文件中的第一行是否为标题行，若为标题行，则将标题添存入标题数组中
		if (oneRowIsTitle) {
			//循环，切分获得的数据存储列表中
			for (String listName : rows) {
				//判断该列名是否已经存在，若存在，则为其加上标号
				int index = 1;
				while(dataDriverListName.indexOf(listName) != -1) {
					//如果该名称加上当前标号后不重复，则将其加上标号，并结束循环
					if ( dataDriverListName.indexOf(listName + index) == -1 ) {
						listName += index;
						break;
					}
					//若列名仍然重复，则标号自增，重新循环判断，避免重复累加标号
					index++;
				}
				dataDriverListName.add(listName);
				dataDriver.put(listName, new DataDriverList());
			}
		} else {
			//如果数据文件第一行非标题行时，则为列取一个名称，并存储第一行数据，作为列的基准
			// 循环，添加列表标题以及第一行数据。为保证能读取多个数据驱动文件，故将下标从dataDriverListName的长度开始，到dataDriverListName长度加上切分数据的数组的长度
			for (int i = nowListNumber; i < newListNumber; i++) {
				// 定义数据列的名称
				String listName = ("NewDataList" + (i + 1));
				// 存储当前遍历的列，并给予起初始的名称“NewDataListX”
				dataDriver.put(listName, new DataDriverList());
				// 存储列表的名称
				dataDriverListName.add(listName);
				// 存储数据
				dataDriver.get(dataDriverListName.get(i)).add(rows[i - nowListNumber]);
			}
		}

		// 循环，读取所有的信息
		while ((text = br.readLine()) != null) {
			rows = text.split("\\t");
			// 判断其切割后得到的数据是否与列表中存储的列数相同，若不相同，则表示存在行数不一样的数据
			//(newListNumber - nowListNumber)得到的结果是第一行数据的长度，当前行的数据与第一行的数据进行对比
			if (rows.length < (newListNumber - nowListNumber)) {
				// 在字符串的末尾加上一个字符，之后再重新切分，目的在于由于split方法会自动去掉后面切分时为空的字符，但中间或前面有字符时不会被切除，故可以在
				// 字符串的末尾加上一个字符，使其可以可以直接被切分
				text += "*";
				rows = text.split("\\t");
				//切分后，由于末尾多出一个*号，故此处应该将末尾的*号替换为空字符
				rows[rows.length - 1] = "";
			}
			// 循环，向列表中添加数据
			for (int i = nowListNumber; i < newListNumber; i++) {
				// 存储数据，如果数据为空，则不进行存储
				if (!rows[i - nowListNumber].isEmpty()) {
					dataDriver.get(dataDriverListName.get(i)).add(rows[i - nowListNumber]);
				} else {
					// 若读取的位置为空，则再判断是否忽略空位置，若不忽略空位置，则将该列存储为空字符串
					if (!isEmptyCell()) {
						dataDriver.get(dataDriverListName.get(i)).add("");
					}
				}
			}
		}

		br.close();
	}

	/**
	 * 用于创建数据驱动文件，并将数据写入文件中
	 * 
	 * @throws IOException
	 * @throws DataNotFoundException
	 */
	protected File createDataFile(String saveFileName, StringBuilder sb) throws IOException, DataNotFoundException {
		// 用于存储文件名
		String FileName = saveFileName;
		// 判断文件名是否为空或者为null，为空或为null则按照默认文件名命名
		if (FileName == null || FileName.equals("")) {
			FileName = "NewDataDriven";
		}

		// 定义数据驱动文件并在项目路径下创建该文件
		File dataFile = new File("Data\\");
		dataFile.mkdirs();
		// 若有文件名重复，则在文件名后加上数字，以避免文件名重复
		// 添加数字后缀
		int i = 0;
		// 循环，为文件名进行编号，避免文件名重复
		do {
			// 判断当前编号是否为0，为0则不添加数字后缀
			if (i == 0) {
				dataFile = new File("Data\\" + FileName + ".txt");
			} else {
				dataFile = new File("Data\\" + FileName + "(" + i + ").txt");
			}

			// 判断当前文件是否有重复，若无重复则结束循环，有重复则编号加1，继续循环
			if (dataFile.exists()) {
				i++;
				continue;
			} else {
				break;
			}
		} while (true);

		// 写入文件
		// 定义缓冲流
		BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));
		// 将数据 写入文件中
		bw.write(sb.toString());
		bw.flush();
		// 关闭流
		bw.close();

		// 将类中的属性指向生成的文件对象
		addDataFile(dataFile);

		return dataFile;
	}

	/**
	 * 该方法用于返回数据驱动，每个String[]中存储一列数据
	 * 
	 * @return 数据驱动
	 */
	public List<String[]> toDataDariver() {
		ArrayList<String[]> datas = new ArrayList<String[]>();

		// 循环，读取数据驱动中的所有列，按照列名进行获取
		for (String listName : dataDriverListName) {
			// 根据列表获取到数据驱动的列，并将该列存储下来
			DataDriverList dataList = dataDriver.get(listName);
			datas.add(dataList.get(0, dataList.size()));
		}

		// 返回数据驱动
		return datas;
	}
	
	/**
	 * 用于写入csv类型的文件
	 * @param newDataFile
	 * @throws IOException
	 */
	private void saveCsv(File newDataFile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(newDataFile));
		StringBuilder text = new StringBuilder("");
		
		String[] rowDatas = toString().split("\\n"); 
		//存储每行数据信息
		for (String rowData : rowDatas) {
			String[] datas = rowData.split(", ");
			for (String data : datas) {
				text.append(data + ",");
			}
			text.replace(text.length() - 1, text.length(), "\r\n");
		}
		
		bw.write(text.toString());
		bw.flush();
		bw.close();
	}

	/**
	 * 用于写入xlsx文件
	 * @param newDataFile
	 * @throws IOException
	 */
	private void saveXlsx(File newDataFile) throws IOException {
		//定义xlsx文件对象，并指向第一个sheet
		XSSFWorkbook xw = new XSSFWorkbook();
		XSSFSheet xs = xw.createSheet();
		
		String[] rowDatas = toString().split("\\n"); 
		//存储每行数据信息
		//将行数指向第二行
		int i = 0;
		for (String rowData : rowDatas) {
			//定义列数
			int j = 0;
			//创建行
			XSSFRow row = xs.createRow(i++);
			String[] datas = rowData.split(", ");
			for (String data : datas) {
				row.createCell(j++).setCellValue(data);
			}
		}
		
		// 将预设内容写入Excel文件中
		// 定义输出流，用于向指定的Excel文件中写入内容
		FileOutputStream fop = new FileOutputStream(newDataFile);
		// 写入文件
		xw.write(fop);
		// 关闭流
		fop.close();
		xw.close();
	}

	/**
	 * 用于写入xls文件
	 * @param newDataFile
	 * @throws IOException
	 */
	private void saveXls(File newDataFile) throws IOException {
		//定义xlsx文件对象，并指向第一个sheet
		HSSFWorkbook hw = new HSSFWorkbook();
		HSSFSheet hs = hw.createSheet();
		
		String[] rowDatas = toString().split("\\n"); 
		//存储每行数据信息
		//将行数指向第二行
		int i = 0;
		for (String rowData : rowDatas) {
			//定义列数
			int j = 0;
			//创建行
			HSSFRow row = hs.createRow(i++);
			String[] datas = rowData.split(", ");
			for (String data : datas) {
				row.createCell(j++).setCellValue(data);
			}
		}
		
		// 将预设内容写入Excel文件中
		// 定义输出流，用于向指定的Excel文件中写入内容
		FileOutputStream fop = new FileOutputStream(newDataFile);
		// 写入文件
		hw.write(fop);
		// 关闭流
		fop.close();
		hw.close();
		
	}

	/**
	 * 用于写入txt文件
	 * @param newDataFile
	 * @throws IOException 
	 */
	private void saveTxt(File newDataFile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(newDataFile));
		StringBuilder text = new StringBuilder("");
		
		String[] rowDatas = toString().split("\\n"); 
		//存储每行数据信息
		for (String rowData : rowDatas) {
			String[] datas = rowData.split(", ");
			for (String data : datas) {
				text.append(data + "\t");
			}
			text.replace(text.length() - 1, text.length(), "\r\n");
		}
		
		bw.write(text.toString());
		bw.flush();
		bw.close();
		
	}

	@Override
	public String toString() {
		StringBuilder dataText = new StringBuilder();
		// 将列表的名字拼接至dataText中
		for (String dataName : dataDriverListName) {
			dataText.append(dataName + ", ");
		}
		dataText.delete(dataText.lastIndexOf(", "), dataText.length());
		dataText.append("\n");

		// 再将数据按照列的形式拼接至dataText中
		List<String[]> dataList = toDataDariver();
		// 先求出列表的最大的长度
		int maxLength = 0;
		for (int i = 0; i < dataList.size(); i++) {
			maxLength = maxLength < dataList.get(i).length ? dataList.get(i).length : maxLength;
		}

		for (int i = 0; i < maxLength; i++) {
			for (int j = 0; j < dataList.size(); j++) {
				try {
					dataText.append(dataList.get(j)[i] + ", ");
				} catch (ArrayIndexOutOfBoundsException e) {
					dataText.append(", ");
				}
			}
			dataText.delete(dataText.lastIndexOf(", "), dataText.length());
			dataText.append("\n");
		}

		return dataText.toString();
	}

	public class DataDriverList {
		// 用于存储一列数据驱动
		private ArrayList<String> dataList = new ArrayList<>();
		// 用于存储当前返回数据的下标
		private int nowIndex = 0;

		/**
		 * 构造方法，用于初始化数据
		 * 
		 * @param datas 数据，可为数组，也可以是多个数据传入
		 */
		public DataDriverList(String... datas) {
			dataList.addAll(Arrays.asList(datas));
		}

		public DataDriverList() {
		}

		/**
		 * 用于添加一组数据
		 * 
		 * @param datas
		 */
		protected void add(String... datas) {
			dataList.addAll(Arrays.asList(datas));
		}

		/**
		 * 用于添加单个数据
		 * 
		 * @param data
		 */
		protected void add(String data) {
			dataList.add(data);
		}

		/**
		 * 用于返回是否存在下一个元素
		 * 
		 * @return
		 */
		public boolean hasNext() {
			// 判断当前元素的下标是否与列表中存储的数据量一致，一致则返回false
			if (nowIndex == dataList.size()) {
				return false;
			} else {
				return true;
			}
		}

		/**
		 * 用于返回下一个元素
		 * 
		 * @return 下一个元素
		 */
		public String next() {
			if (hasNext()) {
				// 返回当前下标指向的元素，并在执行后对其加1
				return dataList.get(nowIndex++);
			} else {
				throw new ArrayIndexOutOfBoundsException("列表元素已全部返回");
			}
		}

		/**
		 * 用于设置开始返回元素的下标，当下标大于当前列表的下标的时，则设置无效
		 * 
		 * @param index 开始返回元素的下标
		 */
		public void setNowIndex(int index) {
			// 判断传入的下标是否大于列表的最大长度，若大于该长度，则直接返回
			if (index >= dataList.size()) {
				return;
			}

			nowIndex = index;
		}

		/**
		 * 用于返回当前可返回元素的下标
		 * 
		 * @return
		 */
		public int getNowIndex() {
			return nowIndex;
		}

		/**
		 * 用于返回列表的长度
		 * 
		 * @return 列表的长度
		 */
		public int size() {
			return dataList.size();
		}

		/**
		 * 用于返回指定 下标的数据
		 * 
		 * @param index 指定的下标
		 * @return 下标对应的数据
		 */
		public String get(int index) {
			return dataList.get(index);
		}

		/**
		 * 用于返回指定下标的一组数据，如果两个值相同，则同{@link #get(int)}
		 * 
		 * @param start 起始下标
		 * @param end   结束下标
		 * @return 数据组
		 */
		public String[] get(int start, int end) {
			// 如果起始下标或结束下标中有一个元素为0，则抛出ArrayIndexOutOfBoundsException异常
			if (start < 0 || end < 0) {
				throw new ArrayIndexOutOfBoundsException("下标越界：(" + start + ", " + end + ")");
			}
			// 如果两下标相等，则直接调用get()方法
			if (start == end) {
				return new String[] { get(end) };
			}
			// 如果起始下标大于结束下标，则将两个下标调换位置
			if (start > end) {
				int temp = start;
				start = end;
				end = temp;
			}

			// 定义数组，规定其长度为end - start
			String[] data = new String[end - start];

			// 循环，逐个读取数据，并将数据存入数组中
			for (int i = 0, j = start; j < end; i++, j++) {
				data[i] = dataList.get(j);
			}

			return data;
		}
	}
}
