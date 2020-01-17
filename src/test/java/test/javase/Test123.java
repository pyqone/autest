package test.javase;

import pres.auxiliary.work.testcase.templet.ZentaoTemplet;
import pres.auxiliary.work.testcase.writecase.PresetCase;

public class Test123 {
	public static void main(String[] args) throws Exception {
		String s = "Fgfdsdfg\\sfdggwe\\grew.gsfd.sdf.txt";
		String ss = "Fgfdsdfg\\sfdggwe\\grew";
		
		System.out.println(s.split("\\.").length);
		System.out.println(ss.split("\\.").length);
		
		System.out.println("The End");
		//System.out.println(UUID.randomUUID().toString());
	}
}
