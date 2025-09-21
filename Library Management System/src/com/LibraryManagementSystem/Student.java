package com.LibraryManagementSystem;

public class Student {

	 private final String studentNames;
	    private Book bookBorrowed;
	    public Student(String studentNames) {
	        this.studentNames = studentNames;
	        this.bookBorrowed = null;
	    }


	    public Book getBookBorrowed() {
	        return bookBorrowed;
	    }
	    public void setBookBorrowed(Book bookBorrowed) {
	        this.bookBorrowed = bookBorrowed;
	    }
	    public String getStudentNames() {
	        return studentNames;
	    }
	}	