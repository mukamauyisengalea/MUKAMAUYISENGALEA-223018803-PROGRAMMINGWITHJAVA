package com.constructorexample;

public class Student3 {

	int id;
	String name;
	//method to display the value of id and name
	void display(){System.out.println(id+name);}

	public static void main(String[] args) {
		// create objects
		Student3 s1=new Student3();
		Student3 s2=new Student3();
		//display value object
		s1.display();
		s2.display();

	}

}
