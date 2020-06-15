package test.javase;

import java.util.Random;

public class TestRandom {
	public static void main(String[] args) {
//		System.out.println(Math.ceil(3.5));
//		System.out.println(0 - new Random().nextInt(90));
//		System.out.print("数字：");
//		int num = new Scanner(System.in).nextInt();
//		System.out.print("数量：");
//		int count = new Scanner(System.in).nextInt();

		int num = 30000000;
		int count = 3;

		int[] nums = new int[count];

		// 向下取整获得平均数
		int avgNum = num / count;
		// 向上取整获得差值
		int diffNum = (int) Math.ceil(avgNum / 10.0);

		int minNum = avgNum - diffNum;
		int maxNum = avgNum + diffNum;

		int sum = 0;
		for (int i = 0; i < count; i++) {
			int ranNum = new Random().nextInt(maxNum - minNum + 1) + minNum;
			sum += ranNum;
			nums[i] = ranNum;
		}

		nums[new Random().nextInt(count)] -= (sum - num);

		sum = 0;
		for (int i = 0; i < count; i++) {
			System.out.println("第" + (i + 1) + "个控件填写：" + nums[i]);
			sum += nums[i];
		}
	}
}
