package pres.auxiliary.tool.randomstring;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pres.auxiliary.tool.string.MobleNumberType;
import pres.auxiliary.tool.string.RandomWord;

/**
 * <p><b>文件名：</b>RandomWordTest.java</p>
 * <p><b>用途：</b>
 * 对{@link RandomWord}类进行单元测试
 * </p>
 * <p><b>编码时间：</b>2020年7月4日下午8:35:05</p>
 * <p><b>修改时间：</b>2020年7月4日下午8:35:05</p>
 * @author 彭宇琦
 * @version Ver1.0
 *
 */
public class RandomWordTest {
	RandomWord rw = new RandomWord();
	
	/**
	 * 初始化数据
	 */
	@BeforeClass
	public void init() {
		rw.addRetuenWord(MobleNumberType.CHINA_MOBILE.getRegex());
	}
	
	@AfterMethod
	public void split() {
		System.out.println("*********************************");
	}
	
	/**
	 * 对{@link RandomWord#toWord(int)}方法进行测试
	 */
	@Test
	public void toWordTest_Int() {
		rw.toWord(3).forEach(System.out :: println);
	}
	
	/**
	 * 对{@link RandomWord#toWord(int, int)}方法进行测试
	 */
	@Test
	public void toWordTest_IntInt() {
		rw.toWord(3, 6).forEach(System.out :: println);
	}
	
	/**
	 * 对{@link RandomWord#setRepeat(boolean)}方法进行测试
	 */
	@Test
	public void setRepeatTest() {
		System.out.println("设置词语不可重复前：");
		System.out.println("词语总数：" + MobleNumberType.CHINA_MOBILE.getRegex().size());
		System.out.println("rw.toWord(200)生成词语个数：" + rw.toWord(200).size());
		System.out.println("rw.toWord(100, 200)生成词语个数：" + rw.toWord(100, 200).size());
	
		rw.setRepeat(false);
		System.out.println("设置词语不可重复后：");
		System.out.println("词语总数：" + MobleNumberType.CHINA_MOBILE.getRegex().size());
		System.out.println("rw.toWord(300)生成词语个数：" + rw.toWord(300).size());
		System.out.println("rw.toWord(200, 300)生成词语个数：" + rw.toWord(200, 300).size());
		rw.setRepeat(true);
	}
}
