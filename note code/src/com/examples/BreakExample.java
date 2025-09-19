package com.examples;

public class BreakExample {

	public static void main(String[] args) {
		// using for loop
		int l = 0;
		for (int i=l;i<10;i++){
			if(i==5){
				//breaking the loop
				break;
			}
			System.out.println(i);

		}

	}
}