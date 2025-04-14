import java.util.Scanner;

public class Anagram {
    static boolean isAnagram(String a, String b) {
        
        a = a.toLowerCase();
        b = b.toLowerCase();
        
        // If lengths are not equal, they can't be anagrams
        if (a.length() != b.length()) {
            return false;
        }
        
        int[] frequency = new int[26];
        
        // Count the characters in the first string
        for (char c : a.toCharArray()) {
            frequency[c - 'a']++;
        }
        
        // Subtract the characters in the second string
        for (char c : b.toCharArray()) {
            frequency[c - 'a']--;
        }
        
        // Check if all frequencies are zero
        for (int count : frequency) {
            if (count != 0) {
                return false; // If any count is not zero, they are not anagrams
            }
        }
        
        return true; // All frequencies are zero, so they are anagrams
    }

    public static void main(String[] args) {
    
        Scanner scan = new Scanner(System.in);
        String a = scan.next();
        String b = scan.next();
        scan.close();
        boolean ret = isAnagram(a, b);
        System.out.println( (ret) ? "Anagrams" : "Not Anagrams" );
    }
}