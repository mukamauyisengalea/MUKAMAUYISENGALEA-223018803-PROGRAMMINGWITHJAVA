package com.ShapeAreaCalculator;

public class Rectangle extends  Shape {
    private final double width;
    private final double length;
    public Rectangle(double width, double length) {
        this.width = width;
        this.length = length;
    }

    @Override
    public double calculateArea() {
        return length * width;
    }
}
