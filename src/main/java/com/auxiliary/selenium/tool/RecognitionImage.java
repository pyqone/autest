package com.auxiliary.selenium.tool;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * <p>
 * <b>文件名：</b>RecognitionIdentifyingCode.java
 * </p>
 * <p>
 * <b>用途：</b>用于对图片中的文本进行识别
 * </p>
 * <p>
 * <b>编码时间：</b>2018年11月29日 下午7:51:59
 * </p>
 * 
 * @author 彭宇琦
 *
 */
public class RecognitionImage {
	// 定义ITesseract类
	private static ITesseract instance = new Tesseract();

	/**
	 * 设置语言包文件夹的路径，注意，此处是只需要存储语言包存放的文件夹。不需要定位到某一个语言包上，但文件夹的最后一层必须以“tessdata”来命名
	 * 
	 * @param tessdataFolder 语言包存放的文件夹路径
	 */
	public static void setTessdataPath(File tessdataFolder) {
		String judge_FileName = "tessdata";
		// 切分传入的文件夹路径，获取每一个文件夹名称
		String[] folders = tessdataFolder.getAbsolutePath().split("\\\\");
		// 判断最后一个文件夹名称是否为“tessdata”，若不是，则抛出异常
		if (!judge_FileName.equals(folders[folders.length - 1])) {
			throw new IncorrectDirectoryException("语言包存储的文件夹名称必须为“tessdata”");
		}
		// 若通过判断，则存储其文件的路径
		instance.setDatapath(tessdataFolder.getAbsolutePath());
	}

	/**
	 * 用于识别图片中的英文、数字文本，使用方法前需要设置语言包的存储路径
	 * 
	 * @param imageFile 图片文件
	 * @return 识别结果
	 */
	public static String judgeImage(File imageFile) {
		return judgeImage(imageFile, "eng");
	}

	/**
	 * 用于识别图片中的指定语言的文本，使用方法前需要设置语言包的存储路径
	 * 
	 * @param imageFile 图片文件
	 * @param language  语言（其值为语言包文件名称后最前的名称）
	 * @return 识别结果
	 */
	public static String judgeImage(File imageFile, String language) {
		// 通过图片流读取图片，用于获取其图片的长宽，以传入一个完整的图片至方法中
		BufferedImage imageio = Optional.ofNullable(imageFile)
				.map(file -> {
					try {
						return ImageIO.read(imageFile);
					} catch (IOException e) {
						throw new IncorrectDirectoryException("文件有误，无法读取");
					}
				})
				.orElseThrow(() -> new IncorrectDirectoryException("文件有误，无法读取"));
		
		//英文实现方式不同，故加以判断
		String judgeLanguage = "eng";
		if ( judgeLanguage.equals(language) ) {
			return judgeImage(imageFile, 0, 0, imageio.getWidth(), imageio.getHeight());
		} else {
			return judgeImage(imageFile, language, 0, 0, imageio.getWidth(), imageio.getHeight());
		}
	}

	/**
	 * 用于识别图片某一部分中的指定语言的文本，使用方法前需要设置语言包的存储路径
	 * 
	 * @param imageFile 图片文件
	 * @param language  语言（其值为语言包文件名称后最前的名称）
	 * @param x         图片起始位置x值（相对图片左上角）
	 * @param y         图片起始位置y值（相对图片左上角）
	 * @param width     图片需要裁剪的宽度（相对x值）
	 * @param height     图片需要裁剪的高度（相对y值）
	 * @return 识别结果
	 */
	public static String judgeImage(File imageFile, String language, int x, int y, int width, int height) {
		// 设置语言包
		instance.setLanguage(language);
		String result = null;
		try {
			// 读取图片，并设置需要读取的位置
			result = instance.doOCR(imageFile, new Rectangle(x, y, width, height));
		} catch (TesseractException e) {
			throw new IncorrectDirectoryException("文件有误，无法读取");
		}

		return result;
	}
	
	/**
	 * 用于识别图片某一部分中的指定语言的文本，使用方法前需要设置语言包的存储路径
	 * 
	 * @param imageFile 图片文件
	 * @param x         图片起始位置x值（相对图片左上角）
	 * @param y         图片起始位置y值（相对图片左上角）
	 * @param width     图片需要裁剪的宽度（相对x值）
	 * @param height     图片需要裁剪的高度（相对y值）
	 * @return 识别结果
	 */
	public static String judgeImage(File imageFile, int x, int y, int width, int height) {
		// 获取判断得到的文本
		String result = judgeImage(imageFile, "eng", x, y, width, height);
		// 将非英文和数字字符替换为空
		result = result.replaceAll("[^a-z^A-Z^0-9]", "");

		return result;
	}
}
