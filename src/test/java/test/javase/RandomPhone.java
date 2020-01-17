package test.javase;

import pres.auxiliary.tool.randomstring.RandomString;
import pres.auxiliary.tool.randomstring.StringMode;

public class RandomPhone {
	public static void main(String[] args) {
		String s = "133,153,180,181,189,177,1700,199";
		RandomString rs = new RandomString(StringMode.NUM);
		
		for (String ss : s.split("\\,")) {
			if (ss.length() == 3) {
				System.out.println(ss + rs.toString(8));
				System.out.println(ss + rs.toString(7));
				System.out.println(ss + rs.toString(9));
				System.out.println(ss + "@" + rs.toString(7));
			} else {
				System.out.println(ss + rs.toString(7));
				System.out.println(ss + rs.toString(6));
				System.out.println(ss + rs.toString(8));
				System.out.println(ss + "@" + rs.toString(6));
			}
		}
	}
}
