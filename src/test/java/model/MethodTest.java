package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MethodTest {

	@Test
	public void 출력값들을_보자() {
		assertTrue(Method.POST.equals("POST"));
		System.out.println(Method.POST);
	}

}
