package com.auxiliary.tool.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>文件名：</b>WriteSingleTempletFile.java</p>
 * <p><b>用途：</b>
 * </p>
 * <p><b>编码时间：</b>2021年5月31日下午8:50:42</p>
 * <p><b>修改时间：</b>2021年5月31日下午8:50:42</p>
 * @author 
 * @version Ver1.0
 * @since JDK 1.8
 * @param <T>
 */
public abstract class WriteSingleTempletFile<T extends WriteSingleTempletFile<T>> extends WriteTempletFile<T> {

	public WriteSingleTempletFile(FileTemplet templet) {
		super(templet);
	}

	public WriteSingleTempletFile(WriteTempletFile<?> writeTempletFile) {
		super(writeTempletFile);
	}

	protected WriteSingleTempletFile() {
		super();
	}
	
	@Override
	public void write(int caseStartIndex, int caseEndIndex) {
		//判断模板文件是否存在，若不存在，则创建模板文件
		if (!new File(templet.getTempletAttribute(FileTemplet.KEY_SAVE).toString()).exists()) {
			createTempletFile(templet);
		}
		
		super.write(caseStartIndex, caseEndIndex);
	}

	@Override
	protected List<String> getAllTempletJson() {
		List<String> tempList = new ArrayList<>();
		tempList.add(templet.getTempletJson());
		return tempList; 
	}
}
