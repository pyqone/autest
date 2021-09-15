package com.auxiliary.tool.file.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * <p>
 * <b>文件名：</b>DocContentFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义旧版Word文件的内容读取方法，用于读取以doc为后缀的07版word文档文件
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:00:01
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:00:01
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DocContentFileRead extends AbstractContentFileRead {
	/**
	 * 根据文件类对象，打开相应的文件
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public DocContentFileRead(File readFile) {
		super(readFile);
		// 读取word
		Optional<HWPFDocument> wordOptional = Optional.empty();
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象", readFile)))) {
			wordOptional = Optional.ofNullable(new HWPFDocument(fip));
		} catch (IOException e) {
			throw new FileException("文件打开异常" , readFile, e);
		}

		try (HWPFDocument word = wordOptional.orElseThrow(() -> new FileException("Word文件读取类未构造"))) {
			// 获取word中的所有内容
			Range wordFileRange = word.getRange();
			// 生成段落下标，遍历word文档中的所有段落
			IntStream.range(0, wordFileRange.numParagraphs())
					// 将下标转换为段落类对象
					.mapToObj(wordFileRange::getParagraph)
					.filter(para -> para != null)
					// 读取段落的内容，对内容进行封装
					.map(Paragraph::text)
					// 去除换行符，并过滤掉空行
					.map(text -> text.replaceAll("\\r", ""))
					// 按行存储至列表对象中
					.forEach(textList::add);
		} catch (IOException e) {
			throw new FileException("文件读取异常", readFile, e);
		}
	}

	@Override
	public List<String> readAllContext() {
		return textList;
	}

	/**
	 * 用于返回07版word的读取类对象（poi）
	 * @return 文件读取类对象
	 */
	public HWPFDocument getReadClass() {
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("文件读取异常", readFile)))) {
			return  new HWPFDocument(fip);
		} catch (IOException e) {
			throw new FileException("文件打开异常" , readFile, e);
		}
	}
}
