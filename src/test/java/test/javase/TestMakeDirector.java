package test.javase;

import pres.auxiliary.directory.operate.MakeDirectory;

public class TestMakeDirector {
	public static void main(String[] args) {
//		MakeDirectory.createAllFolder();
		MakeDirectory.setSavePath("C:");
		MakeDirectory.createAllFolder();
		System.out.println("The End");
	}
}
