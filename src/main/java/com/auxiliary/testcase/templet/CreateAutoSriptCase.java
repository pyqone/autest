package com.auxiliary.testcase.templet;

import java.io.File;

/**
 * <p><b>文件名：</b>CreateAutoSriptCase.java</p>
 * <p><b>用途：</b>
 * </p>
 * <p><b>编码时间：</b>2021年7月9日上午8:20:53</p>
 * <p><b>修改时间：</b>2021年7月9日上午8:20:53</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 */
public abstract class CreateAutoSriptCase extends Case implements GetAutoScript {

	public CreateAutoSriptCase(File configXmlFile) {
		super(configXmlFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAutoScriptJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
