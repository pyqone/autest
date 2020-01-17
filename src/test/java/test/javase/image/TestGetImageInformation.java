package test.javase.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestGetImageInformation {
	static BufferedImage i;
	
	@BeforeClass
	public static void newImage() {
		try {
			i = ImageIO.read(new File("E:\\测试\\Tsee4j测试\\1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_01_GetWidthAndHight() throws Exception {
		System.out.println("图片的像素为：" + i.getHeight() + " × " + i.getWidth());
	}
}
