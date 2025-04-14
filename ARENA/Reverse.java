import java.io.*;
import java.util.*;

public class Reverse {

    public static void main(String[] args) {
        
        Scanner sc=new Scanner(System.in);
        String A=sc.next();
        
        String reverseStr = "";
    
        int strLength = A.length();

        for (int i = (strLength - 1); i >=0; --i) {
        reverseStr = reverseStr + A.charAt(i);
        }

        if (A.toLowerCase().equals(reverseStr.toLowerCase())) {
        System.out.println("Yes");
        }
        else {
        System.out.println("No");
        }
        }
}