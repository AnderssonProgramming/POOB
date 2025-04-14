import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Counts {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        
        Scanner sc = new Scanner(System.in);
        int lineNumber = 1;
        
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            System.out.println(lineNumber + " " + line);
            lineNumber++;
            
        }
        
        
    }
}