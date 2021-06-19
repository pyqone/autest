package com.auxiliary.testcase.file.extend;

import com.auxiliary.testcase.templet.LabelType;
import com.auxiliary.tool.file.FileTemplet;
import com.auxiliary.tool.file.WriteSingleTempletFile;

public class WriteMarkdownTestCase extends WriteSingleTempletFile<WriteMarkdownTestCase> {

	@Override
	protected void contentWriteTemplet(FileTemplet templet, int caseStartIndex, int caseEndIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createTempletFile(FileTemplet templet) {
		// TODO Auto-generated method stub
		
	}
	
	public static FileTemplet getMarkdownTemplet() {
		FileTemplet templet = new FileTemplet("result/Markdown测试用例.xlsx");
		
		templet.addField(LabelType.TITLE.getName());
		templet.addField(LabelType.STEP.getName());
		templet.addField(LabelType.EXCEPT.getName());
		
		return templet;
	}
}
