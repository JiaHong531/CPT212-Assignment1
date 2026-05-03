import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class SimpleMultiplication {

    // counters based on primitive operation categories
    static long assignmentCounter = 0;
    static long arrayLookupCounter = 0;
    static long comparisonCounter = 0;
    static long arithmeticCounter = 0;
    static long methodCallCounter = 0;
    static long returnCounter = 0;
    static long totalCounter = 0;

    public static int[] longMultiplication(int[] multiplicand, int[] multiplier, int n,
            int[][] partialOut, int[][] carrierOut) {

        // counting the call to this method itself
        methodCallCounter++; // longMultiplication(...)

        // reset all counters for this call
        assignmentCounter = 0;
        arrayLookupCounter = 0;
        comparisonCounter = 0;
        arithmeticCounter = 0;
        methodCallCounter = 1; // preserve the method call count above
        returnCounter = 0;

        int[][] partial = new int[n][n]; // Allocate a new 2D array for the partial product digits 
        int[][] carrier = new int[n][n]; // Allocate a new 2D array for the carrier digits
        assignmentCounter += 2; // int[][] partial = ..., int[][] carrier = ...

        int[] acc = new int[2 * n]; // Allocate a 1D array of size 2n to accumulate the final sum 
        assignmentCounter++; // int[] acc = ...
        arithmeticCounter++; // 2 * n

        // for (int i = n-1; i >= 0; i--)
        assignmentCounter++; // i = n-1
        arithmeticCounter++; // n-1
        comparisonCounter++; // i >= 0
        for (int i = n - 1; i >= 0; i--) {

            int row = n - 1 - i; // Calculate which row of the partial/carrier table we are filling
            arithmeticCounter++; // n-1-i
            assignmentCounter++; // row =

            // for (int j = n-1; j >= 0; j--)
            assignmentCounter++; // j = n-1
            arithmeticCounter++; // n-1 
            comparisonCounter++; // j >= 0
            for (int j = n - 1; j >= 0; j--) {

                int product = multiplier[i] * multiplicand[j]; // Multiply the multiplier by the multiplicand 
                arrayLookupCounter += 2; // multiplier[i], multiplicand[j]
                arithmeticCounter++; // * (multiplication)
                assignmentCounter++; // product =

                partial[row][j] = product % 10; // Store the ones-place digit of the product in the partial array
                arrayLookupCounter++; // partial[row][j]
                arithmeticCounter++; // % 10
                assignmentCounter++; // partial[row][j] =

                carrier[row][j] = product / 10; // Store the tens-place digit of the product in the carrier array
                arrayLookupCounter++; // carrier[row][j]
                arithmeticCounter++; // / 10
                assignmentCounter++; // carrier[row][j] =

                // j-- : 1 arithmetic + 1 assignment, then re-check
                arithmeticCounter++; // j-1
                assignmentCounter++; // j = j-1
                comparisonCounter++; // j >= 0 re-check (covers final failing check)
            }

            // for (int j = n-1; j >= 0; j--)
            assignmentCounter++; // j = n-1
            arithmeticCounter++; // n-1
            comparisonCounter++; // j >= 0
            for (int j = n - 1; j >= 0; j--) {

                int pos = n + j - row; // Calculate the position in the 1D accumulator
                arithmeticCounter += 2; // n+j, then -row
                assignmentCounter++; // pos =

                acc[pos] += partial[row][j]; // Add the partial digit in the accumulator
                arrayLookupCounter += 2; // acc[pos], partial[row][j]
                arithmeticCounter++; // +=
                assignmentCounter++; // acc[pos] =

                // j--
                arithmeticCounter++;
                assignmentCounter++;
                comparisonCounter++; // j >= 0
            }

            // for (int j = n-1; j >= 0; j--)
            assignmentCounter++; // j = n-1
            arithmeticCounter++; // n-1
            comparisonCounter++; // j >= 0
            for (int j = n - 1; j >= 0; j--) {

                int pos = n + j - row - 1; // Calculate position for carrier
                arithmeticCounter += 3; // n+j, -row, -1
                assignmentCounter++; // pos =

                acc[pos] += carrier[row][j]; // Add the carrier digit in the accumulator
                arrayLookupCounter += 2; // acc[pos], carrier[row][j]
                arithmeticCounter++; // +=
                assignmentCounter++; // acc[pos] =

                // j--
                arithmeticCounter++;
                assignmentCounter++;
                comparisonCounter++; // j >= 0
            }

            // i-- : 1 arithmetic + 1 assignment, then re-check
            arithmeticCounter++;
            assignmentCounter++;
            comparisonCounter++; // i >= 0
        }

        // for (int k = 2n-1; k >= 1; k--)
        assignmentCounter++; // k = 2n-1
        arithmeticCounter += 2; // 2*n, then -1
        comparisonCounter++; // k >= 1
        for (int k = 2 * n - 1; k >= 1; k--) {

            arrayLookupCounter++; // acc[k]
            comparisonCounter++; // acc[k] >= 10
            if (acc[k] >= 10) { // Check if acc[k] contains a value >= 10
                acc[k - 1] += acc[k] / 10; // Take the tens-digit and add it to the neighbor on the left
                arrayLookupCounter += 2; // acc[k-1], acc[k]
                arithmeticCounter += 3; // k-1, /10, +=
                assignmentCounter++; // acc[k-1] =

                acc[k] = acc[k] % 10; // Keep only the ones-digit at the current position
                arrayLookupCounter++; // acc[k]
                arithmeticCounter++; // % 10
                assignmentCounter++; // acc[k] =
            }

            // k-- : 1 arithmetic + 1 assignment, then re-check
            arithmeticCounter++;
            assignmentCounter++;
            comparisonCounter++; // k >= 1
        }

        // for (int i = 0; i < n; i++)
        assignmentCounter++; // i = 0
        comparisonCounter++; // i < n
        for (int i = 0; i < n; i++) {
            // for (int j = 0; j < n; j++)
            assignmentCounter++; // j = 0
            comparisonCounter++; // j < n
            for (int j = 0; j < n; j++) {
                partialOut[i][j] = partial[i][j]; // Transfer local partial results to the partialOut array
                arrayLookupCounter += 2; // partialOut[i][j], partial[i][j]
                assignmentCounter++;

                carrierOut[i][j] = carrier[i][j]; // Transfer local carrier results to the carrierOut array
                arrayLookupCounter += 2; // carrierOut[i][j], carrier[i][j]
                assignmentCounter++;

                // j++
                arithmeticCounter++;
                assignmentCounter++;
                comparisonCounter++;
            }
            // i++
            arithmeticCounter++;
            assignmentCounter++;
            comparisonCounter++;
        }

        returnCounter++; 
        return acc; // Return acc
    }

    // Converts a digit array to a plain string
    private static String arrayToString(int[] digits) {
        char[] chars = new char[digits.length];
        for (int i = 0; i < digits.length; i++) {
            chars[i] = (char) ('0' + digits[i]);
        }
        return new String(chars);
    }

    // Strips leading zeros for clean display
    private static String stripLeadingZeros(int[] digits) {
        int start = 0; // find where the actual digits start
        while (start < digits.length - 1 && digits[start] == 0) {
            start++;
        }

        // Copy the meaningful digits into a char array
        char[] chars = new char[digits.length - start];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('0' + digits[start + i]);
        }
        return new String(chars);
    }

    // Generates a random n-digit number as a digit array
    private static int[] generateRandomNumber(Random rand, int n) {
        int[] digits = new int[n];
        digits[0] = rand.nextInt(9) + 1; // first digit from 1 to 9
        for (int i = 1; i < n; i++) {
            digits[i] = rand.nextInt(10); // remaining from 0 to 9
        }
        return digits;
    }

    // Sums all individual counters into totalCounter
    private static void updateTotal() {
        totalCounter = assignmentCounter + arrayLookupCounter + comparisonCounter
                + arithmeticCounter + methodCallCounter + returnCounter;
    }

    // Prints the full operation's counters 
    private static void printCounters() {
        System.out.println("Total primitive operations : " + totalCounter);
        System.out.println("  assignments   = " + assignmentCounter);
        System.out.println("  array lookups = " + arrayLookupCounter);
        System.out.println("  comparisons   = " + comparisonCounter);
        System.out.println("  arithmetic    = " + arithmeticCounter);
        System.out.println("  method calls  = " + methodCallCounter);
        System.out.println("  returns       = " + returnCounter);
    }

    // Prints step-by-step partial products and carriers
    private static void printSteps(int[][] partial, int[][] carrier,
            int[] multiplicand, int[] multiplier, int n) {
        int colWidth = n + 14;

        for (int row = 0; row < n; row++) {
            int mDigit = multiplier[n - 1 - row]; // Identify the specific digit from the multiplier

            // Create string of partial digits and add spaces
            String partialStr = arrayToString(partial[row]) + " ".repeat(row);

            // Create string of carrier digits and shift alignment
            String carrierStr = arrayToString(carrier[row]) + " ".repeat(row + 1);

            // Print partial products 
            System.out.printf("    %" + colWidth + "s   partial products for (%s x %d)%n",
                    partialStr, arrayToString(multiplicand), mDigit);

            if (row == n - 1) {
                System.out.printf("+   %" + colWidth + "s   carriers for (%s x %d)%n",
                        carrierStr, arrayToString(multiplicand), mDigit); // Add a "+" symbol at last row
            } else {
                System.out.printf("    %" + colWidth + "s   carriers for (%s x %d)%n",
                        carrierStr, arrayToString(multiplicand), mDigit); // Print carrier line without "+" symbol
            }
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        System.out.print("Number of digits, n: ");
        int n = scanner.nextInt();
        scanner.close();

        int[] multiplicand = generateRandomNumber(rand, n);
        int[] multiplier = generateRandomNumber(rand, n);

        System.out.println("Multiplicand: " + arrayToString(multiplicand));
        System.out.println("Multiplier  : " + arrayToString(multiplier));
        System.out.println();

        int[][] partial = new int[n][n];
        int[][] carrier = new int[n][n];

        // Run the simple multiplication
        int[] result = longMultiplication(multiplicand, multiplier, n, partial, carrier);
        updateTotal();

        // Display a step-by-step long multiplication tables when n is less than or equal to 10
        if (n <= 10) {
            int colWidth = n + 14;
            String separatorLine = "    " + "-".repeat(colWidth + 4);
            System.out.printf("    %" + colWidth + "s%n", arrayToString(multiplicand));
            System.out.printf("x   %" + colWidth + "s%n", arrayToString(multiplier));
            System.out.println(separatorLine);
            printSteps(partial, carrier, multiplicand, multiplier, n);
            System.out.println(separatorLine);
            System.out.printf("    %" + colWidth + "s%n", stripLeadingZeros(result));
            System.out.println("    " + "=".repeat(colWidth + 4));
        } else {
            System.out.println("Result: " + stripLeadingZeros(result)); // Print the result for large n
        }
        System.out.println();

        System.out.println("Primitive operation count for n=" + n + ":");
        printCounters();

        // Perform performance sweep from n=1 to user's input n and write it into CSV
        System.out.println("\nRunning performance sweep n=1 to " + n + "...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SimpleMultiplicationDataset.csv"))) {

            writer.write("Number of digits (n),Total Primitive Operations,Assignments,ArrayLookups,Comparisons,Arithmetic,MethodCalls,Returns\n");

            for (int digitCount = 1; digitCount <= n; digitCount++) {

                // Determine number of runs based on n
                int runs;
                if (digitCount <= 50)
                    runs = 100;
                else if (digitCount <= 500)
                    runs = 20;
                else if (digitCount <= 5000)
                    runs = 5;
                else
                    runs = 3;

                // Accumulators for averaging
                long sumTotal = 0, sumAssign = 0, sumArray = 0;
                long sumCmp = 0, sumArith = 0, sumMethod = 0, sumReturn = 0;

                for (int r = 0; r < runs; r++) {
                    int[] num1 = generateRandomNumber(rand, digitCount);
                    int[] num2 = generateRandomNumber(rand, digitCount);
                    int[][] partialStorage = new int[digitCount][digitCount];
                    int[][] carrierStorage = new int[digitCount][digitCount];

                    longMultiplication(num1, num2, digitCount, partialStorage, carrierStorage);
                    updateTotal();

                    sumTotal += totalCounter;
                    sumAssign += assignmentCounter;
                    sumArray += arrayLookupCounter;
                    sumCmp += comparisonCounter;
                    sumArith += arithmeticCounter;
                    sumMethod += methodCallCounter;
                    sumReturn += returnCounter;
                }

                // Write averaged values
                writer.write(digitCount + ","
                        + (sumTotal / runs) + ","
                        + (sumAssign / runs) + ","
                        + (sumArray / runs) + ","
                        + (sumCmp / runs) + ","
                        + (sumArith / runs) + ","
                        + (sumMethod / runs) + ","
                        + (sumReturn / runs) + "\n");
            }

        } catch (IOException e) {
            System.out.println("Could not write CSV: " + e.getMessage());
        }

        System.out.println("Done. Check SimpleMultiplicationDataset.csv for the data.");
    }
}