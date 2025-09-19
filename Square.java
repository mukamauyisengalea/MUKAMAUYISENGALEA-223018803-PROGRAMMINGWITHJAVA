package com.entities;

 
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

    
