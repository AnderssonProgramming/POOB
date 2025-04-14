import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

class Result {

    /*
     * Complete the 'findDay' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts following parameters:
     *  1. INTEGER month
     *  2. INTEGER day
     *  3. INTEGER year
     */

    public static String findDay(int month, int day, int year) {
        Calendar calendar = Calendar.getInstance();
        
        // Set the calendar to the given date
        calendar.set(year, month - 1, day); // month is 0-based in Calendar
        
        // Get the day of the week as an integer
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Map the integer to the corresponding day name
        switch (dayOfWeek) {
            case Calendar.SUNDAY:    return "SUNDAY";
            case Calendar.MONDAY:    return "MONDAY";
            case Calendar.TUESDAY:   return "TUESDAY";
            case Calendar.WEDNESDAY: return "WEDNESDAY";
            case Calendar.THURSDAY:  return "THURSDAY";
            case Calendar.FRIDAY:    return "FRIDAY";
            case Calendar.SATURDAY:  return "SATURDAY";
            default:                 return ""; // Should never reach here
        }
    }
}

public class FindDay {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int month = Integer.parseInt(firstMultipleInput[0]);

        int day = Integer.parseInt(firstMultipleInput[1]);

        int year = Integer.parseInt(firstMultipleInput[2]);

        String res = Result.findDay(month, day, year);

        bufferedWriter.write(res);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}