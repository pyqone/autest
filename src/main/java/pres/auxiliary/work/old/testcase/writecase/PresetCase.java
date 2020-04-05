package pres.auxiliary.work.old.testcase.writecase;

import java.io.File;
import java.io.IOException;

/**
 * 该类是测试用例对象调用的整合，可通过该类中的get方法来调用所有测试用例生成类
 * 
 * @author 彭宇琦
 * @version Ver1.0
 */
public class PresetCase extends Case {
	// 定义所有与生成预设用例相关的类
	// TODO 当测试用例模版类增加时，此处也必须继续添加
	private AddInformation ai;
	private BrowseList bl;
	private Username us;
	private Map ma;
	private Operate oi;
	private MyCase ca;
	private Video vi;

	public PresetCase() {
		super();
	}

	/**
	 * 该构造用于当未通过ZentaoTemplet类创造模版创造测试用例文件时调用，可用于续写测试用例
	 * 
	 * @param excelTempletFile
	 *            测试用例文件对象
	 * @throws IOException 
	 */
	public PresetCase(File excelTempletFile) throws IOException {
		super(excelTempletFile);
	}
	
	//为每一个测试用例生成对象添加一个get方法
	//TODO 当测试用例模版类增加时，也得新增相应的get方法
	/**
	 * 该方法用于返回生成新增信息的测试用例模版类
	 * @return AddInformation对象
	 * @throws IOException
	 */
	public AddInformation getAddInformation() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( ai == null ) {
			ai = new AddInformation(getTempletFile());
		}
		
		return ai;
	}
	
	/**
	 * 该方法用于返回登录相关的测试用例模版类
	 * @return Login对象
	 * @throws IOException
	 */
	public Username getUsername() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( us == null ) {
			us = new Username(getTempletFile());
		}
		
		return us;
	}
	
	/**
	 * 该方法用于返回浏览列表的测试用例模版类
	 * @return BrowseList对象
	 * @throws IOException
	 */
	public BrowseList getBrowseList() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( bl == null ) {
			bl = new BrowseList(getTempletFile());
		}
		
		return bl;
	}
	
	/**
	 * 该方法用于返回地图相关的测试用例模版类
	 * @return Map对象
	 * @throws IOException
	 */
	public Map getMap() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( ma == null ) {
			ma = new Map(getTempletFile());
		}
		
		return ma;
	}
	
	/**
	 * 该方法用于返回修改信息的测试用例模版类
	 * @return OperateInformation对象
	 * @throws IOException
	 */
	public Operate getOperateInformation() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( oi == null ) {
			oi = new Operate(getTempletFile());
		}
		
		return oi;
	}
	
	/**
	 * 该方法用于返回自定义编写测试用例类
	 * @return MyCase对象
	 * @throws IOException
	 */
	public MyCase getMyCase() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( ca == null ) {
			ca = new MyCase(getTempletFile());
		}
		
		return ca;
	}
	
	/**
	 * 该方法用于返回视频相关测试用例类
	 * @return Video对象
	 * @throws IOException
	 */
	public Video getVideo() throws IOException {
		//判断对象是否为空，再判断对象指向的文件是否与最新的测试用例文件（excel）对象一致，若为空或者不一致，则构造对象
		if ( vi == null ) {
			vi = new Video(getTempletFile());
		}
		
		return vi;
	}
}
