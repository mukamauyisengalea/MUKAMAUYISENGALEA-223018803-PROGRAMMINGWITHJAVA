package com.overriding;

public class Test2 {

	public static void main(String[] args) {
		SBI s=new SBI();
		ICICI i=new ICICI();
		AXIS a=new AXIS();
		System.out.println("SBI Rate of interest:"+s.getRateOfInterest());
		System.out.println("ICICI Rate of interest:"+i.getRateOfInterest());
		System.out.println("AXIS Rate of interest:"+a.getRateOfInterest());

	}

}
