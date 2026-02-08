package Chap8;

public class StringOps {

    /**
     * Reverses the given string.
     * Throws IllegalArgumentException if the input is null.
     */
    public String reverseString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        return new StringBuilder(input).reverse().toString();
    }

    /**
     * Checks if a string is a palindrome (reads the same forwards and backwards).
     * It is case-insensitive and ignores spaces.
     * Returns false if the input is null.
     */
    public boolean isPalindrome(String input) {
        if (input == null)
            return false;
        String cleaned = input.replaceAll("\\s+", "").toLowerCase();
        String reversed = new StringBuilder(cleaned).reverse().toString();
        return cleaned.equals(reversed);
    }

    /**
     * Counts the number of vowels (a, e, i, o, u) in the string.
     * Case-insensitive. Returns 0 if input is null.
     */
    public int countVowels(String input) {
        if (input == null)
            return 0;
        int count = 0;
        String lowerCaseInput = input.toLowerCase();
        for (int i = 0; i < lowerCaseInput.length(); i++) {
            char ch = lowerCaseInput.charAt(i);
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }
        return count;
    }
}

// For reverseString:
// 1. A standard string (e.g., "hello" should return "olleh").
// 2. A null string (ensure it throws an IllegalArgumentException).

// For isPalindrome:
// 3. A simple valid palindrome (e.g., "racecar").
// 4. A valid palindrome with mixed cases and spaces (e.g., "A man a plan a canal Panama").
// 5. A string that is not a palindrome (e.g., "java"). 
// 6. A null string (ensure it returns false).

// For countVowels: 
// 7. A string with mixed-case vowels (e.g., "Education" should return 5). 
// 8. A string with absolutely no vowels (e.g., "Rhythm" should return 0).
