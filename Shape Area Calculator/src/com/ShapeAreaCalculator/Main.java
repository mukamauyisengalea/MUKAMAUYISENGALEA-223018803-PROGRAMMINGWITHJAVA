package com.ShapeAreaCalculator;

public class Main {
	public static void main(String[] args) {
		Shape[] shapes = {new Rectangle(30, 15), new Circle(34), new Triangle(19, 14)};
		for (Shape shape : shapes) {
			System.out.println("The area of " + shape.getClass().getSimpleName() + ": " + shape.calculateArea());
		}
	}
}
