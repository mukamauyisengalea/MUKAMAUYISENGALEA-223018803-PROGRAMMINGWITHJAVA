//MUKAMA UYISENGA LEA-223018803-19-2025//
import java.util.Scanner;

// package:com.Entities
class Rectangle {
	private double length;
	private double width;

	public Rectangle(double length, double width) {
		this.length = length;
		this.width = width;
	}

	public double calculateArea() {
		return length * width;
	}

	public double calculatePerimeter() {
		return 2 * (length + width);
	}
}

class Square {
	private double side;

	public Square(double side) {
		this.side = side;
	}

	public double calculateArea() {
		return side * side;
	}

	public double calculatePerimeter() {
		return 4 * side;
	}
}

class Circle {
	private double radius;
	private final double PI = 3.14;

	public Circle(double radius) {
		this.radius = radius;
	}

	public double calculateArea() {
		return PI * radius * radius;
	}

	public double calculatePerimeter() {
		return 2 * PI * radius;
	}
}

class Triangle {
	private double base;
	private double height;
	private double side1, side2, side3;

	public Triangle(double base, double height, double side1, double side2, double side3) {
		this.base = base;
		this.height = height;
		this.side1 = side1;
		this.side2 = side2;
		this.side3 = side3;
	}

	public double calculateArea() {
		return 0.5 * base * height;
	}

	public double calculatePerimeter() {
		return side1 + side2 + side3;
	}
}

//  package2.com.entities 
class TestRectangle {
	public static void test(double length, double width, char choice) {
		Rectangle rect = new Rectangle(length, width);
		if (choice == 'A') {
			System.out.println("Area of Rectangle = " + rect.calculateArea());
		} else {
			System.out.println("Perimeter of Rectangle = " + rect.calculatePerimeter());
		}
	}
}

class TestSquare {
	public static void test(double side, char choice) {
		Square sq = new Square(side);
		if (choice == 'A') {
			System.out.println("Area of Square = " + sq.calculateArea());
		} else {
			System.out.println("Perimeter of Square = " + sq.calculatePerimeter());
		}
	}
}

class TestCircle {
	public static void test(double radius, char choice) {
		Circle circle = new Circle(radius);
		if (choice == 'A') {
			System.out.println("Area of Circle = " + circle.calculateArea());
		} else {
			System.out.println("Perimeter of Circle = " + circle.calculatePerimeter());
		}
	}
}

class TestTriangle {
	public static void test(double base, double height, double side1, double side2, double side3, char choice) {
		Triangle tri = new Triangle(base, height, side1, side2, side3);
		if (choice == 'A') {
			System.out.println("Area of Triangle = " + tri.calculateArea());
		} else {
			System.out.println("Perimeter of Triangle = " + tri.calculatePerimeter());
		}
	}
}

//   Main Program 
public class MainMenu {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String continueChoice;

		do {
			System.out.println("\nSelect a shape:");
			System.out.println("1. Rectangle");
			System.out.println("2. Circle");
			System.out.println("3. Triangle");
			System.out.println("4. Square");   
			System.out.print("Enter your choice: ");
			int shapeChoice = sc.nextInt();

			System.out.println("\nI would like to calculate:");
			System.out.println("A. Area");
			System.out.println("B. Perimeter");
			System.out.print("Enter your choice (A/B): ");
			char calcChoice = sc.next().toUpperCase().charAt(0);

			switch (shapeChoice) {
			case 1: // Rectangle
				System.out.print("Enter length: ");
				double length = sc.nextDouble();
				System.out.print("Enter width: ");
				double width = sc.nextDouble();
				TestRectangle.test(length, width, calcChoice);
				break;

			case 2: // Circle
				System.out.print("Enter radius: ");
				double radius = sc.nextDouble();
				TestCircle.test(radius, calcChoice);
				break;

			case 3: // Triangle
				System.out.print("Enter base: ");
				double base = sc.nextDouble();
				System.out.print("Enter height: ");
				double height = sc.nextDouble();
				System.out.print("Enter side1: ");
				double s1 = sc.nextDouble();
				System.out.print("Enter side2: ");
				double s2 = sc.nextDouble();
				System.out.print("Enter side3: ");
				double s3 = sc.nextDouble();
				TestTriangle.test(base, height, s1, s2, s3, calcChoice);
				break;

			case 4: // Square
				System.out.print("Enter side: ");
				double side = sc.nextDouble();
				TestSquare.test(side, calcChoice);
				break;

			default:
				System.out.println("Invalid shape choice!");
			}

			System.out.print("\nWould you like to continue? (Yes/No): ");
			continueChoice = sc.next();

		} while (continueChoice.equalsIgnoreCase("Yes"));

		System.out.println("Thanks you");
		sc.close();
	}
}
//MAKE ONE PACKAGE AND COMBINE ALL CLASS IN ORDER TO MAKE PROJECT//