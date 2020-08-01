package test.javase;

public class TestMakeDirector {
	public static void main(String[] args) {
//		MakeDirectory.createAllFolder();
		MakeDirectory.setSavePath("C:");
		MakeDirectory.createAllFolder();
		System.out.println("The End");
	}
}
