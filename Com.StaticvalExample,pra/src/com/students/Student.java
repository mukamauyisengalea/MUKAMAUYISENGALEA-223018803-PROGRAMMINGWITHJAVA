package com.students;

public class Student {
	int rollno;//instance variable
	String name;
	static String college="INES";//Static variable
	//constructor
	Student(int r,String n){
		rollno=r;
		name=n;
	}
	//method to display the values
	void display(){System.out.println(rollno+""+name+""+college);}
public static void main(String[] args) {
	// TODO Auto-generated method stub

}

}
