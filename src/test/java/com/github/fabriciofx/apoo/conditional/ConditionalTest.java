package com.github.fabriciofx.apoo.conditional;

import org.junit.Test;

public final class ConditionalTest {
	@Test
	public void isTrue() throws Exception {
		new IfCode(new GreaterThan<Integer>(2, 1)).isTrue(() -> {
			System.out.println("It's True!");
		});
	}
}