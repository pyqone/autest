package test.javase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class TestList {
	@Test
	public void test() {
		//TODO 
		SwitchFrame sf = new SwitchFrame();
	}
	
	class SwitchFrame {
		private ArrayList<String> parentIframeList = new ArrayList<String>();
		
		ArrayList<String> iframeNameList = new ArrayList<String>();
		
		public SwitchFrame() {
			parentIframeList.add("f1");
			parentIframeList.add("f2");
			parentIframeList.add("f3");
		}
		
		/**
		 * 该方法用于将窗体切回顶层，当本身是在最顶层时，则该方法将使用无效
		 */
		public void switchRootFrame() {
			//清空iframeNameList中的内容
			iframeNameList.clear();
		}
		
		/**
		 * 该方法用于将窗体切换到上一层（父层）。若当前层只有一层，则调用方法后切回顶层；
		 * 若当前层为最顶层时，则该方法将使用无效
		 */
		public void switchParentFrame() {
			//若iframeNameList大于1层，则向上切换窗体
			if (iframeNameList.size() > 1) {
				iframeNameList.remove(iframeNameList.size() - 1);
			} else if (iframeNameList.size() == 1) {
				//若iframeNameList等于1层，则调用切换至顶层的方法
				switchRootFrame();
			} else {
				//若iframeNameList小于1层，则不做操作
				return;
			}
		}

		/**
		 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
		 * 名称对应的定位方式，或直接传入xpath与css定位方式，
		 * 根据定位方式对相应的窗体进行定位。当传入的窗体为当前窗体的前层（父层）窗体时，
		 * 通过该方法将调用切换父层的方法，将窗体切换到父层上，例如：<br>
		 * 当前存在f1, f2, f3, f4四层窗体，则调用方法：<br>
		 * switchFrame("f2")<br>
		 * 此时窗体将回到f2层，无需再从顶层开始向下切换。<br>
		 * 注意，窗体的切换按照从前向后的顺序进行切换，切换顺序不能相反
		 * 
		 * @param names 窗体的名称或xpath与css定位方式
		 */
		public void switchFrame(String...names) {
			switchFrame(Arrays.asList(names));
		}
		
		/**
		 * 通过传入在xml文件中的控件名称，到类中指向的xml文件中查找控件
		 * 名称对应的定位方式，或直接传入xpath与css定位方式，
		 * 根据定位方式对相应的窗体进行定位。当传入的窗体为当前窗体的前层（父层）窗体时，
		 * 通过该方法将调用切换父层的方法，将窗体切换到父层上，例如：<br>
		 * 当前存在f1, f2, f3, f4四层窗体，则调用方法：<br>
		 * List<String> nameList = new ArrayList<String>();<br>
		 * nameList.add("f2");<br>
		 * switchFrame(nameList)<br>
		 * 此时窗体将回到f2层，无需再从顶层开始向下切换。<br>
		 * 注意，窗体的切换按照从前向后的顺序进行切换，切换顺序不能相反
		 * 
		 * @param nameList 存储窗体的名称或xpath与css定位方式的List集合
		 */
		public void switchFrame(List<String> nameList) {
			nameList.forEach(name -> {
				//判断name指向的窗体是否在iframeNameList中，若存在，则向上切换父层，直到切换到name指向的窗体；若不存在，则直接切换，并添加窗体名称
				if (iframeNameList.contains(name)) {
					//获取name窗体在iframeNameList中的位置
					int index = iframeNameList.indexOf(name);
					//获取需要向上切换窗体的次数，公式为推断出来
					int count = iframeNameList.size() - index - 1;
					for (int i = 0; i < count; i++) {
						switchParentFrame();
					}
				} else {
					//切换窗体
					iframeNameList.add(name);
				}
			});
		}
	}
}
