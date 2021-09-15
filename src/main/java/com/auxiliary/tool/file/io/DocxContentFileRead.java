package com.auxiliary.tool.file.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.auxiliary.tool.file.UnsupportedFileException;

/**
 * <p>
 * <b>文件名：</b>DocxContentFileRead.java
 * </p>
 * <p>
 * <b>用途：</b> 定义新版Word文件的内容读取方法，用于读取以docx为后缀的新版word文档文件
 * </p>
 * <p>
 * <b>编码时间：</b>2021年9月6日下午7:01:12
 * </p>
 * <p>
 * <b>修改时间：</b>2021年9月6日下午7:01:12
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 */
public class DocxContentFileRead extends AbstractContentFileRead {
	/**
	 * 根据文件类对象，打开相应的文件
	 * @param readFile 文件对象
	 * @throws FileException 当文件打开出错时抛出的异常
	 */
	public DocxContentFileRead(File readFile) {
		super(readFile);
		// 读取word
		Optional<XWPFDocument> wordOptional = Optional.empty();
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象", readFile)))) {
			wordOptional = Optional.ofNullable(new XWPFDocument(fip));
		} catch (IOException e) {
			throw new FileException("文件读取异常" + readFile.getAbsolutePath(), e);
		}

		try (XWPFDocument word = wordOptional.orElseThrow(() -> new UnsupportedFileException("Word文件读取类未构造"))) {
			// 获取文本中所有的段落，若文档中无内容，则抛出异常
			Optional.ofNullable(word.getParagraphs())
					//判定当前内容是否为空
					.filter(list -> list.size() != 0)
					//返回段落集合，若段落集合为空，则抛出异常
					.orElseThrow(() -> new UnsupportedFileException("Word文件中无内容"))
					.stream()
					//过滤掉为null的段落
					.filter(para -> para != null)
					//将段落转换为为本
					.map(XWPFParagraph::getText)
					//拼接文本
					.forEach(textList::add);
		} catch (IOException e) {
			throw new UnsupportedFileException("文件异常，无法进行读取：" + readFile.getAbsolutePath(), e);
		}
	}

	@Override
	public List<String> readAllContext() {
		return textList;
	}

	/**
	 * 返回新版的word的读取类对象（poi）
	 * @return 文件读取类对象
	 */
	public XWPFDocument getReadClass() {
		try (FileInputStream fip = new FileInputStream(
				Optional.ofNullable(readFile).orElseThrow(() -> new FileException("未传入文件对象", readFile)))) {
			return new XWPFDocument(fip);
		} catch (IOException e) {
			throw new FileException("文件读取异常" + readFile.getAbsolutePath(), e);
		}
	}
}
