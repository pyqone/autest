package com.auxiliary.tool.file.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import com.opencsv.CSVReader;

/**
 * <p>
 * <b>文件名：</b>CsvTableFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义csv文件的内容读取方法，用于读取以csv为后缀的表格文件。在按行读取文件内容时，会将一行的每一个元素
 * 拼接在一起，每个元素中间会拼接一个{@link AbstractTableFileRead#SPLIT_SIGN}字符，形成一串字符串进行返回。
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:03:13
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:03:13
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class CsvTableFileRead extends AbstractTableFileRead {
	/**
	 * 根据文件类对象，打开相应的文件
	 * 
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public CsvTableFileRead(File readFile) {
		super(readFile);
		// 定义CSV文件对象
		try (CSVReader csv = new CSVReader(
				new FileReader(Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象"))))) {
			// 获取文件中所有的内容
			List<String[]> wordDataList = csv.readAll();

			// 遍历所有的行，并使用特殊的字符进行拼接
			wordDataList.stream().map(texts -> {
				StringJoiner text = new StringJoiner(SPLIT_SIGN);
				Arrays.stream(texts).forEach(text::add);
				return text.toString();
			}).forEach(textList::add);
		} catch (IOException e) {
			throw new FileException("文件读取异常", readFile);
		}
	}

	/**
	 * 返回csv文件读取类对象
	 * 
	 * @return 文件读取类对象
	 */
	public CSVReader getReadClass() {
		// 定义CSV文件对象
		try (FileReader fileStream = new FileReader(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象")))) {
			return new CSVReader(fileStream);
		} catch (IOException e) {
			throw new FileException("文件读取异常", readFile, e);
		}
	}
}
