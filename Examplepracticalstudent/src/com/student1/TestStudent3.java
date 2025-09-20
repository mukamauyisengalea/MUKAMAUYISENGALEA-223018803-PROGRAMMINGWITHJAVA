package com.student1;

public class TestStudent3 {

	public static void main(String args[] ) {
		//creating objects
		student s1=new student();
		student s2=new student();
		student s3=new student();
		//initializing objects
		s1.id=100;
		s1.name="peter";
		s2.id=102;
		s2.name="jack";
		s3.id=103;
		s3.name="vicky";
		//printing data
		System.out.println(s1.id+""+s1.name);
		System.out.println(s2.id+""+s2.name);
		System.out.println(s3.id+""+s3.name);
		

	}

}
