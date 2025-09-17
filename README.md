# MUKAMAUYISENGALEA-223018803-PROGRAMMINGWITHJAVA
PROGRAMMINGWITHJAVA

 
 **Supermarket Billing System – Case Study Overview**


**Purpose of the Project1**

This project is designed to help a supermarket owner streamline the billing process. Instead of manually calculating the cost of each item a customer buys—which can be slow and prone to mistakes—the system automates everything. It ensures faster checkout, accurate totals, and smart discount handling.


**Why This Project Matters**

**Real-World Impact**

- Efficiency Boost: Cashiers can process purchases faster, reducing long queues and improving customer satisfaction.
- Error Reduction: Manual calculations often lead to mistakes. This system ensures precision in billing.
- Smart Discounts: Automatically applies a discount when the total exceeds a certain amount, encouraging bulk purchases and rewarding customers.

  **Business Benefits**
- Saves time and labor costs.
- Enhances professionalism and trust with customers.
- Makes daily sales tracking easier for the owner.

 **Learning Outcomes for Students**

This project is a fantastic way to learn key programming concepts in a practical setting:
 Variables
Used to store item names, prices, quantities, and totals—just like a digital memory.
 Arrays (or Lists)
Can be used to organize multiple items efficiently, making it easier to manage and display them.
 Arithmetic Operators
Essential for calculating subtotals, totals, and discounts—core to any billing system.
 For Loop
Allows the program to repeat actions for each item the customer buys. It’s like telling the computer: “Do this for every product.”
 Conditional Statements
Used to check if the total bill qualifies for a discount. This teaches decision-making logic in programming.


**What the Program Does**

- Asks how many different items were purchased.
- For each item, it collects:
- Name of the item
- Price per unit
- Quantity purchased
- Calculates the subtotal for each item and adds it to the grand total.
- If the total is more than 50,000, it applies a 5% discount.
- Prints a clean receipt showing:
- Each item’s details
- Total before discount
- Discount amount
- Final amount to be paid

**Why It’s a Great Beginner Project**

- It’s relatable—everyone shops!
- It combines multiple programming concepts in one cohesive task.
- It builds confidence by showing how code can solve real problems.





**Student Grading System – PROJECT 2**

**Background**
Imagine a university lecturer sitting in an exam hall or office, receiving student scores one by one. They don’t know how many students will show up, but they need a reliable way to:
- Record each student’s mark
- Instantly assign a grade
- Track overall class performance

This project creates a digital assistant that does all of that—quickly, accurately, and without the need for manual calculations.



**Purpose of the System**
The goal is to simplify the grading process. Instead of flipping through grading charts or tallying results by hand, the lecturer enters each score and the system:
- Assigns a grade immediately
- Tracks how many students passed or failed
- Calculates the class pass rate at the end

It’s like turning a tedious task into a smooth, intelligent workflow.



**Why This Project Is Valuable**

**Practical Benefits**
- **Time-Saving**: No need to manually calculate grades or count pass/fail stats.
- **Accuracy**: Eliminates human error in grading and reporting.
- **Flexibility**: Works for any number of students—whether it’s 5 or 500.
- **Instant Feedback**: Grades are shown immediately, helping lecturers respond quickly.

**Educational Insights**
- Helps lecturers understand class performance trends.
- Encourages data-driven teaching adjustments.
- Builds trust with students through consistent grading.



**What Students Learn by Building It**

This project is a hands-on way to explore key programming concepts:

- **While Loop**: Used to keep collecting data until the lecturer decides to stop. It teaches how to handle unpredictable input sizes.
- **If–Else Logic**: Helps the system make decisions—like assigning grades based on score ranges.
- **Counters and Percentages**: Tracks how many students passed or failed, and calculates the overall pass rate. This introduces basic data analysis.



**How the System Works (Step-by-Step)**

1. The lecturer enters a student’s score.
2. The system checks the score and assigns a grade:
   - A for top performers
   - B, C, D for average to borderline
   - F for failing
3. The grade is displayed immediately.
4. The system keeps count of:
   - Total students
   - Number of passes (score ≥ 50)
   - Number of fails (score < 50)
5. When the lecturer is done, they enter a special value to stop.
6. The system prints a summary report:
   - Total students graded
   - Pass/fail breakdown
   - Class pass rate in percentage



**Why It’s a Great Beginner Project**
- It’s relatable—everyone has received grades.
- It’s practical—solves a real problem in education.
- It’s educational—teaches how to build systems that think, count, and report.

  



**Class Attendance Tracker – PROJECT3**

**Background**
Imagine a class representative (CR) who’s responsible for tracking attendance every day. Instead of scribbling numbers in a notebook or trying to remember who showed up, they want a simple digital system that:
- Records attendance daily
- Allows flexibility (some days they might skip)
- Gives a clear summary at the end of the semester

This project builds that system—smart, simple, and reliable.



**Purpose of the System**
The goal is to help the CR keep accurate attendance records over time. It’s not just about counting heads—it’s about understanding patterns. Was attendance strong throughout the semester? Were there days when many students were absent? This system answers those questions with clarity.




**Why This Project Is Valuable**

**Practical Benefits**
- **Consistency**: Ensures attendance is recorded every time the CR chooses to log it.
- **Flexibility**: The CR can decide when to stop entering data—perfect for unpredictable schedules.
- **Insightful Summary**: At the end, the system shows average attendance and flags low-attendance days.

**Educational Benefits**
- Encourages responsibility and organization.
- Helps identify trends (e.g., low attendance on Mondays?).
- Supports better planning for future classes or events.


**What Students Learn by Building It**

This project is a hands-on way to explore key programming concepts:

- **Do-While Loop**: Ensures attendance is recorded at least once, even if the CR only logs one day.
- **Arrays**: Used to store daily attendance numbers—like a digital ledger.
- **Conditional Logic**: Helps analyze which days had poor attendance and calculate averages.



 **How the System Works (Step-by-Step)**

1. The CR enters the total number of students in the class.
2. Each day:
   - They input how many students were present.
   - The system stores this number.
   - The CR is asked if they want to enter attendance for another day.
3. After data entry ends:
   - The system calculates the average attendance.
   - It identifies days where attendance was below 50% of the class size.
   - It prints a table showing attendance per day.
   - It shows the percentage of days with low attendance.



**Why It’s a Great Beginner Project**
- It’s practical—attendance tracking is a real need in schools and universities.
- It’s educational—teaches how to build systems that store, analyze, and report data.
- It’s empowering—shows how simple tools can make a big difference in organization and planning.


**project 3 code**
import java.util.Scanner;

public class AttendanceTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter total number of students in class: ");
        int classSize = sc.nextInt();

        int[] attendance = new int[30];
        int dayCount = 0;
        String choice;

        do {
            System.out.print("Enter number of students present on Day " + (dayCount + 1) + ": ");
            attendance[dayCount] = sc.nextInt();
            dayCount++;

            System.out.print("Record another day? (yes/no): ");
            choice = sc.next();
        } while (choice.equalsIgnoreCase("yes") && dayCount < 30);

        int lowAttendanceDays = 0, totalAttendance = 0;
        System.out.println(" Attendance Report");
        for (int i = 0; i < dayCount; i++) {
            System.out.printf("Day %d -> Present: %d\n", i + 1, attendance[i]);
            totalAttendance += attendance[i];
            if (attendance[i] < classSize / 2) lowAttendanceDays++;
        }

        double average = totalAttendance / (double) dayCount;
        double lowAttendanceRate = (lowAttendanceDays * 100.0) / dayCount;

        System.out.printf("Average Attendance: %.2f\n", average);
        System.out.printf("Low Attendance Days: %d (%.2f%%)\n", lowAttendanceDays, lowAttendanceRate);
    }
}








