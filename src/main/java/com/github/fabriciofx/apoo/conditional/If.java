package com.github.fabriciofx.apoo.conditional;

public final class If<T> implements Expression<T> {
	private final Condition condition;
	private final Expression<T> left;
	private final Expression<T> right;

	public If(final Condition condition, final Expression<T> left,
			final Expression<T> right) {
		this.condition = condition;
		this.left = left;
		this.right = right;
	}

	@Override
	public T evaluate() {
		return condition.evaluate() ? left.evaluate() : right.evaluate();
	}
}
