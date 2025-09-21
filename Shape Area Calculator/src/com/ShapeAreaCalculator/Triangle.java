package com.ShapeAreaCalculator;

public class Triangle extends Shape {
	private final double base;
	private final double height;
	public Triangle(double base, double height) {
		this.base = base;
		this.height = height;
	}

	@Override
	public double calculateArea() {
		return base * height / 2;
	}
}