package test.javase;

import java.io.IOException;

public class TestDate {
	public static void main(String[] args) throws IOException {
		/*
		Date d = new Date();
		System.out.println(new SimpleDateFormat("yyyy.MM.dd").format(d));
		d.setDate(d.getDate() + 50);
		System.out.println(new SimpleDateFormat("yyyy.MM.dd").format(d));

		System.out.println(new File("C:\\tes1t").exists());

		// 用于读取模版文件
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("Templet\\TestReportTemplet.docx")));
		// 用于创建用户定义的文件夹
		File f = new File("C:\\AutoTest\\TestReport");
		// 判断文件夹是否创建，若已创建则不再创建
		if (!f.exists()) {
			f.mkdirs();
		}
		// 用于写入到用户定义的文件夹中
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f + "\\copy.docx"));
		// 用于作为缓冲的byte数组
		byte[] b = new byte[1024];

		// 读取文本的一段
		int i = bis.read(b);

		// 循环，直至文本被完全写入
		while (i != -1) {
			bos.write(b);
			bos.flush();
			i = bis.read(b);
		}

		// 关闭流
		bis.close();
		bos.close();
		*/
		
		String s = "1.2.3-4-5-6-7-8-9";
		String[] ss;
		ss = s.split("\\.");
		for (String sss : ss) {
			System.out.println(sss);
		}
		System.out.println();
		
		ss = ss[2].split("-");
		for (String sss : ss) {
			System.out.println(sss);
		}
		System.out.println(Integer.MAX_VALUE);
		System.out.println("The End");
	}
}
