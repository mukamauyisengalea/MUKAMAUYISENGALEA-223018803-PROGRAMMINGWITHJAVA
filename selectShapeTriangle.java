package com.entities;

public class selectShapeTriangle {
		private double base;
		private double height;
		private double hyp;
		public selectShapeTriangle(double base, double height) {
			this.base = base;
			this.height = height;
			double result = (base * base) + (height * height);
			this.hyp = Math.sqrt(result);
		}
		public double calculateArea() {
			return base * height/ 2;
		}
		public double calculatePerimeter() {
			return base + height + hyp;
		}

}
