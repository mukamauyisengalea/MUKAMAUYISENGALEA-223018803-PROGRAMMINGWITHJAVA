package com.students;

public class TestStaticvaluable1 {

	public static void main(String[] args) {
		Student s1=new Student(111,"Peter");
		Student s2=new Student(222,"Aryan");
		//we can change the college of all objects by the single of code
		//student.college="UR";
		s1.display();
		s2.display();

	}

}
