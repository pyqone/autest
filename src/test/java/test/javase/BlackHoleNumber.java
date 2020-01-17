package test.javase;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * <p><b>文件名：</b>BlackHoleNumber.java</p>
 * <p><b>用途：</b>求取黑洞数</p>
 * <p><b>编码时间：</b>2018年12月24日 上午8:11:29</p>
 * <p><b>问题描述：</b>编程求三位数中的“黑洞数”。<br>
 * 黑洞数又称陷阱数，任何一个数字不全相同的整数，经有限次“重排求差”操作，
 * 总会得到某一个或一些数，这些数即为黑洞数。“重排求差”操作是将组成一个数
 * 的各位数字重排得到的最大数减去最小数，例如207，“重排求差”操作序列是720-027=693，
 * 963-369=594，954-459=495，再做下去就不变了，再用208算一次，
 * 也停止到495，所以495是三位黑洞数。</p>
 * 
 * <p><b>问题分析：</b>根据“黑洞数”定义，对于任一个数字不全相同的整数，
 * 最后结果总会掉入到一个黑洞圈或黑洞数里，最后结果一旦为黑洞数，无论再重复
 * 进行多少次的“重排求差”操作，则结果都是一样的，可把结果相等作为判断“黑洞
 * 数”的依据。</p>
 * 
 * <p><b>过程如下：</b>
 * <ol>
 * <li>将任一个三位数进行拆分。</li>
 * <li>拆分后的数据重新组合，将可以组合的最大值减去最小值，差赋给变量j。</li>
 * <li>将当前差值暂存到另一变量h中：h=j。</li>
 * <li>对变量j执行拆分、重组、求差操作，差值仍然存储到变量j中。</li>
 * <li>判断当前差值j是否与前一次的差相等，若相等将差值输出并结束循环，否则，重复步骤 3、4和 5。</li>
 * </ol>
 * </p>
 * @author Linux公社
 */
public class BlackHoleNumber {
	//存储每次重排后得到的运算结果
	private static ArrayList<Integer> numList = new ArrayList<>();
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		int i;
	    int max, min, j = 0;
	    //控制循环次数
	    int k = 0;
	    
	    System.out.println("请输入一个数字：");
	    i = new Scanner(System.in).nextInt();

	    //获取最大最小的数字
	    max = maxNumber(i);
	    min = minNumber(i);
	    
	    //j=max-min;
	    //循环进行运算
	    while(true) {
	    	//计算两数之差
	        j=max-min;
	        //通过showBlackHole()方法判断运算结果是否掉入黑洞，若在黑洞中，则结束循环
	        if(showBlackHole(j))
	        {
	            break;
	        }
	        
	        System.out.println("第" + ++k + "次运算：" + max + "-" + min + " = " + j);
	        
	        //获取最大最小的数字
		    max = maxNumber(j);
		    min = minNumber(j);
	    }
	}
	
	/**
	 * 求取数字重排后的最大的数字
	 * @param num 需要重排的数字
	 * @return 重排后得到的最大数字
	 */
	private static int maxNumber(int num) {
		//将字符串逐一切开
		String[] nums = String.valueOf(num).split("");
		//将切分到的字符串转成数字
		int[] numbers = new int[nums.length];
		for ( int i = 0; i < nums.length; i++ ) {
			numbers[i] = Integer.valueOf(nums[i]);
		}
		
		//冒泡排序，按照从大到小的顺序进行排序
		for ( int i = 0; i < numbers.length - 1; i++ ) {
			for ( int j = 0; j < numbers.length - 1 - i; j++ ) {
				if ( numbers[j] < numbers[j + 1] ) {
					int temp = numbers[j];
					numbers[j] = numbers[j + 1];
					numbers[j + 1] = temp;
				}
			}
		}
		
		//将重排得到的数字组合成字符串
		String numberText = "";
		for ( int number : numbers ) {
			numberText += number;
		}
		//将字符串转换为数字
		return Integer.valueOf(numberText);
	}
	
	/**
	 * 求取数字重排后的最大的数字
	 * @param num 需要重排的数字
	 * @return 重排后得到的最大数字
	 */
	private static int minNumber(int num) {
		//将字符串逐一切开
		String[] nums = String.valueOf(num).split("");
		//将切分到的字符串转成数字
		int[] numbers = new int[nums.length];
		for ( int i = 0; i < nums.length; i++ ) {
			numbers[i] = Integer.valueOf(nums[i]);
		}
		
		//冒泡排序，按照从大到小的顺序进行排序
		for ( int i = 0; i < numbers.length - 1; i++ ) {
			for ( int j = 0; j < numbers.length - 1 - i; j++ ) {
				if ( numbers[j] > numbers[j + 1] ) {
					int temp = numbers[j];
					numbers[j] = numbers[j + 1];
					numbers[j + 1] = temp;
				}
			}
		}
		
		//将重排得到的数字组合成字符串
		String numberText = "";
		for ( int number : numbers ) {
			numberText += number;
		}
		//将字符串转换为数字
		return Integer.valueOf(numberText);
	}
	
	/**
	 * 用于判断并显示黑洞数
	 * @param num 运算结果
	 * @return 是否为黑洞数
	 */
	private static boolean showBlackHole(int num) {
		//用于循环后判断该数字是否为黑洞数
		boolean blackHole = false;
		//循环，判断数字是否已在列表中，若存在，则说明计算结果已进入循环，可进行输出
		int i = 0;
		for (; i < numList.size(); i++ ) {
			if ( numList.get(i) == num ) {
				blackHole = true;
				break;
			}
		}
		
		//若循环结束后，其数字是一个黑洞数，则输出黑洞数，不是，则存储运算结果
		if ( blackHole ) {
			System.out.print("黑洞数为：");
			//由于上次循环发现重复后，则直接结束了循环，故此处可以直接衔接上循环来输出结果
			for (; i < numList.size(); i++ ) {
				System.out.print(numList.get(i));
				if ( i < numList.size() - 1 ) {
					System.out.print("→");
				}
			} 
		} else {
			numList.add(num);
		}
		
		return blackHole;
	}
}

