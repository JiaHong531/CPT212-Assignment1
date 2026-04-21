import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

// CPT212 Assignment 1 - Part 1
// Simple Multiplication Algorithm (clean version)
public class SimpleMultiplicationV1 {

    // multiplies two n-digit numbers given as digit arrays (most-significant digit first)
    // returns the result as a digit array of length 2n
    public static int[] simpleMultiply(int[] multiplicand, int[] multiplier, int n) {

        // partial[row][col] - unit digit of each pairwise product
        // carrier[row][col] - tens digit of each pairwise product
        int[][] partial = new int[n][n];
        int[][] carrier = new int[n][n];
        int[] acc = new int[2 * n];

        // Step 1 - digit by digit multiplication
        for (int i = n - 1; i >= 0; i--) {
            int row = n - 1 - i;

            for (int j = n - 1; j >= 0; j--) {
                int product = multiplier[i] * multiplicand[j];
                partial[row][j] = product % 10;
                carrier[row][j] = product / 10;
            }

            // print step-by-step for small n only
            if (n <= 10) {
                String partialStr = arrayToString(partial[row]) + " ".repeat(row);
                String carrierStr = arrayToString(carrier[row]) + " ".repeat(row + 1);
                int colWidth = n + 14;

                System.out.printf("    %" + colWidth + "s   partial products for (=%s x %d)%n",
                        partialStr, arrayToString(multiplicand), multiplier[i]);

                if (row == n - 1) {
                    System.out.printf("+   %" + colWidth + "s   carriers for (%s x %d)%n",
                            carrierStr, arrayToString(multiplicand), multiplier[i]);
                } else {
                    System.out.printf("    %" + colWidth + "s   carriers for (%s x %d)%n",
                            carrierStr, arrayToString(multiplicand), multiplier[i]);
                }
            }

            // Step 2 - accumulate partial and carrier into result array
            for (int j = n - 1; j >= 0; j--) {
                acc[n + j - row]     += partial[row][j];
                acc[n + j - row - 1] += carrier[row][j];
            }
        }

        // Step 3 - normalize carry
        for (int k = 2 * n - 1; k >= 1; k--) {
            if (acc[k] >= 10) {
                acc[k - 1] += acc[k] / 10;
                acc[k]      = acc[k] % 10;
            }
        }

        return acc;
    }

    private static String arrayToString(int[] digits) {
        char[] chars = new char[digits.length];
        for (int i = 0; i < digits.length; i++) chars[i] = (char) ('0' + digits[i]);
        return new String(chars);
    }

    private static String stripLeadingZeros(int[] digits) {
        int start = 0;
        while (start < digits.length - 1 && digits[start] == 0) start++;
        char[] chars = new char[digits.length - start];
        for (int i = 0; i < chars.length; i++) chars[i] = (char) ('0' + digits[start + i]);
        return new String(chars);
    }

    private static int[] generateRandomNumber(Random rand, int n) {
        int[] digits = new int[n];
        digits[0] = rand.nextInt(9) + 1;
        for (int i = 1; i < n; i++) digits[i] = rand.nextInt(10);
        return digits;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random rand     = new Random();

        System.out.print("Number of digits, n: ");
        int n = scanner.nextInt();
        scanner.close();

        int[] multiplicand = generateRandomNumber(rand, n);
        int[] multiplier   = generateRandomNumber(rand, n);

        System.out.println("Multiplicand: " + arrayToString(multiplicand));
        System.out.println("Multiplier  : " + arrayToString(multiplier));
        System.out.println();

        if (n <= 10) {
            int colWidth = n + 14;
            String sep   = "    " + "-".repeat(colWidth + 4);
            System.out.printf("    %" + colWidth + "s%n", arrayToString(multiplicand));
            System.out.printf("x   %" + colWidth + "s%n", arrayToString(multiplier));
            System.out.println(sep);
            int[] result = simpleMultiply(multiplicand, multiplier, n);
            System.out.println(sep);
            System.out.printf("    %" + colWidth + "s%n", stripLeadingZeros(result));
            System.out.println("    " + "=".repeat(colWidth + 4));
        } else {
            int[] result = simpleMultiply(multiplicand, multiplier, n);
            System.out.println("Result: " + stripLeadingZeros(result));
        }

        // verify against BigInteger
        BigInteger expected = new BigInteger(arrayToString(multiplicand))
                                  .multiply(new BigInteger(arrayToString(multiplier)));
        System.out.println("Expected: " + expected);
    }
}