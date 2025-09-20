package com.employee;

public class TestEmployee {

	public static void main(String[] args) {
		Employee e1=new Employee();
		Employee e2=new Employee();
		Employee e3=new Employee();
		e1.insert(101,"Fred",45000);
		e2.insert(102,"Mary",25000);
		e3.insert(103,"Danny",55000);
		e1.display();
		e2.display();
		e3.display();

	}

}
