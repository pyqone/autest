package test.javase;

import java.util.ArrayList;
import java.util.Collection;

public class TestLambda {
	public static void main(String[] args) {
		//lambda遍历Collection
		Collection<String> l = new ArrayList<String>();
		l.add("haha");
		l.add("xixi");
		l.add("gege");
		l.forEach(c -> {
			c += "1";
			System.out.println(c);
		});
	}
}
