package com.InterfaceExpratbanks;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;



class A7 implements Printable,Showable { 
public void print(){System.out.println("hello");}
public void show(){System.out.println("welcome");}
	

	
	public static void main(String[] args) {
	A7 obj=new A7();
	obj.print();
	obj.show();
	}
	public int print(Graphics arg0, PageFormat arg1, int arg2)
			throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}
}

