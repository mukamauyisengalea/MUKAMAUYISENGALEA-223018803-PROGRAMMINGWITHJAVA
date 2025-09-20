package com.StaticEx;

public class Student {
	int rollno;
	String name;
	static String college="ITS";
  //Static method to change the value of static valuable
	static void change(){
		college="BBDIT";
	}
	//constructor to initialize the valuable
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
