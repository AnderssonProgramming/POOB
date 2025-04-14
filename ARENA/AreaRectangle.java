import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class AreaRectangle {
    
    static int B;
    static int H;
    static boolean flag = true;
    
    // Static initialization block
    static {
        Scanner sc = new Scanner(System.in);
        B = sc.nextInt(); // Read breadth
        H = sc.nextInt(); // Read height
        
        // Check if either B or H is less than or equal to 0
        if (B <= 0 || H <= 0) {
            System.out.println("java.lang.Exception: Breadth and height must be positive");
            flag = false; // Set flag to false if dimensions are invalid
        }
        sc.close(); // Close the scanner
    }


public static void main(String[] args){
		if(flag){
			int area=B*H;
			System.out.print(area);
		}
		
	}//end of main

}//end of class