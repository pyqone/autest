package pres.auxiliary.tool.date;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.testng.annotations.Test;

public class TimeTest {
	Time time = new Time();
	
	/**
	 * 用于测试四种构造方法 ：<br>
	 * {@link Time#Time()}<br>
	 * {@link Time#Time(java.util.Date)}<br>
	 * {@link Time#Time(long)}<br>
	 * {@link Time#Time(String)}
	 * @throws ParseException 
	 */
	@Test
	public void newTimeTest() throws ParseException {
		time = new Time();
		System.out.println(time.getTime());
		System.out.println("-".repeat(20));
		time = new Time(new Date(1575387550000L));
		System.out.println(time.getTime());
		System.out.println("-".repeat(20));
		time = new Time(1575387800000L);
		System.out.println(time.getTime());
		System.out.println("-".repeat(20));
		time = new Time("2019-12-04 03:03:20");
		System.out.println(time.getTime());
		System.out.println("-".repeat(20));
	}
	
	@Test
	public void test() throws ParseException {
		time = new Time();
		time.addTime("-2d6h");
		System.out.println(time.getTime());
	}
	
	/**
	 * 测试{@link Time#getDate()}
	 */
	@Test
	public void getDateTest() {
		time.setNowTime();
		System.out.println(time.getDate().getTime());
		System.out.println("-".repeat(20));
		time = new Time(new Date(1575387550000L));
		System.out.println(time.getDate().getTime());
		System.out.println("-".repeat(20));
		time = new Time(1575387800000L);
		System.out.println(time.getDate().getTime());
		System.out.println("-".repeat(20));
		time.setTime("2019\\12\\04 03时03分20秒");
		System.out.println(time.getDate().getTime());
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 测试{@link Time#getFormatTime()}
	 */
	@Test
	public void getFormatTimeTest() {
		time.setNowTime();
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time = new Time(new Date(1575387550000L));
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time = new Time(1575387800000L);
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time.setTime("2019年12月04日 03时03分20秒");
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time = new Time("2019/12/04 03:03:20");
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time.setTime("2019年12月04日");
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time.setTime("2019年12月04日 03时03分");
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
		time.setNowTime();
		System.out.println(time.getFormatTime());
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 测试{@link Time#getFormatTime(String)}
	 */
	@Test
	public void getFormatTimeTest_String() {
		time.setNowTime();
		System.out.println(time.getFormatTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("-".repeat(20));
		time.setTime("2019/12/04 03:03:20");
		System.out.println(time.getFormatTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("-".repeat(20));
		time.setTime(new Date());
		System.out.println(time.getFormatTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("-".repeat(20));
		time.setTime(1576037076297L);
		System.out.println(time.getFormatTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println(time.getFormatTime("2019/12/04 03:03:20"));
		System.out.println("-".repeat(20));
		time.setTime("2019/12/04");
		System.out.println(time.getFormatTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("-".repeat(20));
		time.setTime("03:03:20");
		System.out.println(time.getFormatTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("-".repeat(20));
	}
	
	/**
	 * 测试{@link Time#addTime(String)}
	 */
	@Test
	public void addTimeTest() {
		time.setTime("2019年12月01日 00:00:00");
		time.addTime("-1.5y-2.3M2W5d1.5h-0.5min20.5S");
		System.out.println(time.getFormatTime());
		time.initTime();
		System.out.println(time.getFormatTime());
		time.setTime("2019-12-11");
		time.addTime("-2年 -   9 月 - 17 日");
		System.out.println(time.getFormatTime());
	}
	
	@Test
	public void use3() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\8.test\\TestTime\\time.txt")));
		bw.write("[\r\n");
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007962\",\r\n");
//		time.addTime("-3h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-3h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("6min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("}\r\n");
		bw.write("]\r\n");
		
		bw.close();
		Desktop.getDesktop().open(new File("D:\\8.test\\TestTime\\time.txt"));
	}
	
	@Test
	public void use() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\8.test\\TestTime\\time.txt")));
		bw.write("[\r\n");
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007962\",\r\n");
		time.addTime("-3h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-3h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3.1h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007963\",\r\n");
		time.addTime("-6h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-3h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3.1h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007964\",\r\n");
		time.addTime("-3h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-6h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6.1h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00002042\",\r\n");
		time.addTime("-6h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-6h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6.1h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		/*
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00000182\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-2.9h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007341\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3h");
		System.out.println(time.getFormatTime());
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3.1h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00002417\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.5h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.8h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007961\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6h");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6.5h");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("}\r\n");
		*/
		bw.write("]\r\n");
		
		bw.close();
		Desktop.getDesktop().open(new File("D:\\8.test\\TestTime\\time.txt"));
	}
	
	@Test
	public void use2() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\8.test\\TestTime\\time.txt")));
		bw.write("[\r\n");
		bw.write("{\r\n");
		time.setNowTime();
		time.addTime("-1min");
		bw.write("\"equName\":\"");
		bw.write("FT00007962\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00007963\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00007964\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00002042\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00000182\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00007341\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00002417\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("},\r\n");
		
		time.setNowTime();
		time.addTime("-1min");
		bw.write("{\r\n");
		bw.write("\"equName\":\"");
		bw.write("FT00007961\",\r\n");
		bw.write("\"heartTime\":\"" + time.getTime() + "\",\r\n");
		bw.write("\"inOrOutSideTime\":\"" + time.getTime() + "\",\r\n");
		time.addTime("1min");
		bw.write("\"personTime\":\"" + time.getTime() + "\"\r\n");
		bw.write("}\r\n");
		bw.write("]\r\n");
		
		bw.close();
		Desktop.getDesktop().open(new File("D:\\8.test\\TestTime\\time.txt"));
	}
	
	@Test
	public void getJson() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\8.test\\TestTime\\time2.txt")));
		bw.write("[\r\n");
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00000359\",\r\n");
		time.addTime("-2.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-5min");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("0min");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007948\",\r\n");
		time.addTime("-3.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-5min");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("0min");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007947\",\r\n");
		time.addTime("-5.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-5min");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("0min");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007570\",\r\n");
		time.addTime("-6.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.setNowTime();
		time.addTime("-5min");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("0min");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00007571\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-2.5h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-2.4h\",\r\n");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00000190\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-2.9h");
		System.out.println(time.getFormatTime());
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3.5h");
		System.out.println(time.getFormatTime());
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00000347\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.6h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.8h");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00000453\",\r\n");
		time.addTime("0h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.8h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6.5h");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00003963\",\r\n");
		time.addTime("-3.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-2.7h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3.5h");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00005253\",\r\n");
		time.addTime("-6.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-2.7h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-3.5h");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00005344\",\r\n");
		time.addTime("-3.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.7h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6.5h");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("},\r\n");
		
		bw.write("{\r\n");
		time.setNowTime();
		bw.write("\"equName\":\"");
		bw.write("FT00006885\",\r\n");
		time.addTime("-6.5h");
		bw.write("\"heartTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-5.7h");
		bw.write("\"inOrOutSideTime\":\"" + time.getFormatTime() + "\",\r\n");
		time.initTime();
		time.addTime("-6.5h");
		bw.write("\"personTime\":\"" + time.getFormatTime() + "\"\r\n");
		bw.write("}\r\n");
		bw.write("]\r\n");
		
		bw.close();
		Desktop.getDesktop().open(new File("D:\\8.test\\TestTime\\time2.txt"));
	}
}
