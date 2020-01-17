package test.javase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBefore {
	@Test
	public static void show() {
		System.out.println("haha");
	}

	@Before
	public static void before() {
		System.out.println("xixi");
	}

	@After
	public static void after() {
		System.out.println("hehe");
	}
}
