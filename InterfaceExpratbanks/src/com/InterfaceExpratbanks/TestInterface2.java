package com.InterfaceExpratbanks;

public class TestInterface2 {
		 public static void main(String args[]) {
		     Bank b = new SBI();  // or use new PNB()
		     System.out.println("ROI: " + b.rateOfInterest());
		 }
}
