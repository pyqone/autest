package test.javase;

import java.util.ArrayList;

import pres.auxiliary.tool.string.RandomString;

/**
 * FileName: SweepPlan.java
 * 
 * 用于进行值日安排
 * 
 * @author 彭宇琦
 * @Deta 2019年1月9日 上午9:23:23
 * @version ver1.0
 */
public class SweepPlan {
	public static void main(String[] args) {
		//6个值日地点：1表示女厕；2表示男厕；3表示老总办公室、倒垃圾；4表示前台、倒垃圾；5表示大厅；6表示大厅
		RandomString rs = new RandomString("123456");
		rs.setRepeat(false);
		String s = "";
		
		ArrayList<String> l = new ArrayList<>();
		l.add("彭宇琦");l.add("韦    顺");l.add("蓝石玉");l.add("梁俊宏");l.add("梁叶辰");l.add("梁文玉");
		for ( int j = 1; j < 4; j++ ) {
			System.out.println("-----------第" + j + "次结果-----------");
			//分配任务
			while( true ) {
				s = rs.toString(6);
				//如果女厕未安排女生，则重新安排
				if( s.charAt(4) != '1' && s.charAt(5) != '1' ) {
					continue;
				}
				//如果男厕安排到女生，则重新安排
				if ( s.charAt(4) == '2' || s.charAt(5) == '2' ) {
					continue;
				}
				break;
			}
			for ( int i = 0; i < l.size(); i++ ) {
				System.out.println(l.get(i) + ":" + show(s.charAt(i)));
			}
		}
	}
	
	//用于将数字翻译成值日位置
	public static String show(char c) {
		switch(c) {
		case '1' :
			return "女厕";
		case '2' :
			return "男厕";
		case '3' :
			return "老总办公室、倒垃圾";
		case '4' :
			return "前台、倒垃圾";
		case '5' :
			return "大厅";
		case '6' :
			return "大厅";
		default:
			return "";
		}
	}
}
