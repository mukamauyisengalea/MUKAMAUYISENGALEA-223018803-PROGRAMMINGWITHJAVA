package com.BankAccount;

public class BankAccountManager {
	 private static final BankAccount[] bankAccounts = {
         new BankAccount("MUKAMA", 1234),
         new BankAccount("KAMANA", 5678)
 };
 public static void main(String[] args) {
     bankAccounts[0].deposit(5000);
     bankAccounts[1].deposit(3000);
     bankAccounts[0].withdraw(1000);
     bankAccounts[1].withdraw(2000);
     bankAccounts[0].withdraw(5000);
 }
}

