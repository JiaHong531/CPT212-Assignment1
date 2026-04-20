import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class SimpleMultiplication {

    // counters based on textbook primitive operation categories
    static long assignmentCounter   = 0;
    static long arrayLookupCounter  = 0;
    static long comparisonCounter   = 0;
    static long arithmeticCounter   = 0;
    static long methodCallCounter   = 0;
    static long returnCounter       = 0;
    static long totalCounter        = 0;

    // multiplies two n-digit numbers given as digit arrays (most-significant digit first)
    // both arrays must have the same length n
    // partialOut and carrierOut are filled so main() can use them for printing
    // returns the result as a digit array of length 2n
    public static int[] simpleMultiply(int[] multiplicand, int[] multiplier, int n,
                                        int[][] partialOut, int[][] carrierOut) {

        // counting the call to this method itself
        methodCallCounter++;        // simpleMultiply(...)

        // reset all counters for this call
        assignmentCounter   = 0;
        arrayLookupCounter  = 0;
        comparisonCounter   = 0;
        arithmeticCounter   = 0;
        methodCallCounter   = 1;    // preserve the method call count above
        returnCounter       = 0;

        // partial[row][col] - unit digit of each pairwise product
        // carrier[row][col] - tens digit of each pairwise product
        // row = n-1-i, so row 0 corresponds to the rightmost multiplier digit
        int[][] partial = new int[n][n];
        int[][] carrier = new int[n][n];
        assignmentCounter += 2;     // int[][] partial = ..., int[][] carrier = ...

        // accumulator - size 2n, each cell may temporarily exceed 9 before normalization
        // position index from the left:
        //   partial[row][j] -> acc[n + j - row]
        //   carrier[row][j] -> acc[n + j - row - 1]
        int[] acc = new int[2 * n];
        assignmentCounter++;        // int[] acc = ...
        arithmeticCounter++;        // 2 * n

        // ----------------------------------------------------------------
        // Step 1 - digit by digit multiplication
        // i walks the multiplier from right to left (n-1 down to 0)
        // ----------------------------------------------------------------

        // for (int i = n-1; i >= 0; i--)
        assignmentCounter++;        // i = n-1 (init)
        arithmeticCounter++;        // n-1 (arithmetic in init)
        comparisonCounter++;        // i >= 0 (first check)
        for (int i = n - 1; i >= 0; i--) {

            int row = n - 1 - i;
            arithmeticCounter++;    // n-1-i
            assignmentCounter++;    // row =

            // for (int j = n-1; j >= 0; j--)
            assignmentCounter++;    // j = n-1 (init)
            arithmeticCounter++;    // n-1 (arithmetic in init)
            comparisonCounter++;    // j >= 0 (first check)
            for (int j = n - 1; j >= 0; j--) {

                int product = multiplier[i] * multiplicand[j];
                arrayLookupCounter += 2;    // multiplier[i], multiplicand[j]
                arithmeticCounter++;        // * (multiplication)
                assignmentCounter++;        // product =

                partial[row][j] = product % 10;
                arrayLookupCounter++;       // partial[row][j]
                arithmeticCounter++;        // % 10
                assignmentCounter++;        // partial[row][j] =

                carrier[row][j] = product / 10;
                arrayLookupCounter++;       // carrier[row][j]
                arithmeticCounter++;        // / 10
                assignmentCounter++;        // carrier[row][j] =

                // j-- : 1 arithmetic + 1 assignment, then re-check
                arithmeticCounter++;        // j-1
                assignmentCounter++;        // j = j-1
                comparisonCounter++;        // j >= 0 re-check (covers final failing check)
            }

            // add partial[row] into acc - partial[row][j] goes to acc[n + j - row]
            // for (int j = n-1; j >= 0; j--)
            assignmentCounter++;    // j = n-1 (init)
            arithmeticCounter++;    // n-1 (arithmetic in init)
            comparisonCounter++;    // j >= 0 (first check)
            for (int j = n - 1; j >= 0; j--) {

                int pos = n + j - row;
                arithmeticCounter += 2;     // n+j, then -row
                assignmentCounter++;        // pos =

                acc[pos] += partial[row][j];
                arrayLookupCounter += 2;    // acc[pos], partial[row][j]
                arithmeticCounter++;        // +=
                assignmentCounter++;        // acc[pos] =

                // j--
                arithmeticCounter++;
                assignmentCounter++;
                comparisonCounter++;        // j >= 0 re-check
            }

            // add carrier[row] into acc - carrier[row][j] goes to acc[n + j - row - 1]
            // for (int j = n-1; j >= 0; j--)
            assignmentCounter++;    // j = n-1 (init)
            arithmeticCounter++;    // n-1 (arithmetic in init)
            comparisonCounter++;    // j >= 0 (first check)
            for (int j = n - 1; j >= 0; j--) {

                int pos = n + j - row - 1;
                arithmeticCounter += 3;     // n+j, -row, -1
                assignmentCounter++;        // pos =

                acc[pos] += carrier[row][j];
                arrayLookupCounter += 2;    // acc[pos], carrier[row][j]
                arithmeticCounter++;        // +=
                assignmentCounter++;        // acc[pos] =

                // j--
                arithmeticCounter++;
                assignmentCounter++;
                comparisonCounter++;        // j >= 0 re-check
            }

            // i-- : 1 arithmetic + 1 assignment, then re-check
            arithmeticCounter++;
            assignmentCounter++;
            comparisonCounter++;            // i >= 0 re-check
        }

        // ----------------------------------------------------------------
        // Step 3 - normalize: propagate carry rightward through acc
        // walk right to left, push tens digit of each cell to the left neighbor
        // ----------------------------------------------------------------

        // for (int k = 2n-1; k >= 1; k--)
        assignmentCounter++;        // k = 2n-1 (init)
        arithmeticCounter += 2;     // 2*n, then -1 (in 2n-1)
        comparisonCounter++;        // k >= 1 (first check)
        for (int k = 2 * n - 1; k >= 1; k--) {

            arrayLookupCounter++;   // acc[k]
            comparisonCounter++;    // acc[k] >= 10
            if (acc[k] >= 10) {
                acc[k - 1] += acc[k] / 10;
                arrayLookupCounter += 2;    // acc[k-1], acc[k]
                arithmeticCounter += 3;     // k-1, /10, +=
                assignmentCounter++;        // acc[k-1] =

                acc[k] = acc[k] % 10;
                arrayLookupCounter++;       // acc[k]
                arithmeticCounter++;        // % 10
                assignmentCounter++;        // acc[k] =
            }

            // k-- : 1 arithmetic + 1 assignment, then re-check
            arithmeticCounter++;
            assignmentCounter++;
            comparisonCounter++;            // k >= 1 re-check
        }

        // copy partial and carrier to output arrays so main() can print steps
        // for (int i = 0; i < n; i++)
        assignmentCounter++;        // i = 0 (init)
        comparisonCounter++;        // i < n (first check)
        for (int i = 0; i < n; i++) {
            // for (int j = 0; j < n; j++)
            assignmentCounter++;    // j = 0 (init)
            comparisonCounter++;    // j < n (first check)
            for (int j = 0; j < n; j++) {
                partialOut[i][j] = partial[i][j];
                arrayLookupCounter += 2;    // partialOut[i][j], partial[i][j]
                assignmentCounter++;

                carrierOut[i][j] = carrier[i][j];
                arrayLookupCounter += 2;    // carrierOut[i][j], carrier[i][j]
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

        returnCounter++;            // return acc
        return acc;
    }

    // converts a digit array to a plain string - not part of the algorithm
    private static String arrayToString(int[] digits) {
        char[] chars = new char[digits.length];
        for (int i = 0; i < digits.length; i++) {
            chars[i] = (char) ('0' + digits[i]);
        }
        return new String(chars);
    }

    // strips leading zeros for clean display
    private static String stripLeadingZeros(int[] digits) {
        // find where the actual digits start (skip leading zeros)
        int start = 0;
        while (start < digits.length - 1 && digits[start] == 0) {
            start++;
        }

        // copy the meaningful digits into a char array
        char[] chars = new char[digits.length - start];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('0' + digits[start + i]);
        }
        return new String(chars);
    }

    // generates a random n-digit number as a digit array (no leading zeros)
    private static int[] generateRandomNumber(Random rand, int n) {
        int[] digits = new int[n];
        digits[0] = rand.nextInt(9) + 1;    // first digit: 1 to 9
        for (int i = 1; i < n; i++) {
            digits[i] = rand.nextInt(10);   // remaining: 0 to 9
        }
        return digits;
    }

    // sums all individual counters into totalCounter
    private static void updateTotal() {
        totalCounter = assignmentCounter + arrayLookupCounter + comparisonCounter
                     + arithmeticCounter + methodCallCounter + returnCounter;
    }

    // prints the full operation count breakdown
    private static void printCounters() {
        System.out.println("Total primitive operations : " + totalCounter);
        System.out.println("  assignments   = " + assignmentCounter);
        System.out.println("  array lookups = " + arrayLookupCounter);
        System.out.println("  comparisons   = " + comparisonCounter);
        System.out.println("  arithmetic    = " + arithmeticCounter);
        System.out.println("  method calls  = " + methodCallCounter);
        System.out.println("  returns       = " + returnCounter);
    }

    // prints step-by-step partial products and carriers matching the assignment PDF format
    // each row pair is shifted left by its row index to show positional value
    // the last carrier row gets a "+" prefix
    private static void printSteps(int[][] partial, int[][] carrier,
                                   int[] multiplicand, int[] multiplier, int n) {
        int colWidth = n + 14;

        for (int row = 0; row < n; row++) {
            int mDigit = multiplier[n - 1 - row];  // multiplier digit for this row
            // row 0 = rightmost (no shift), row 1 = 1 space left, etc.
            String shift = " ".repeat(row);
            String partialStr = arrayToString(partial[row]) + shift;
            String carrierStr = arrayToString(carrier[row]) + shift;

            // partial products row
            System.out.printf("    %" + colWidth + "s   partial products for (=%s x %d)%n",
                    partialStr, arrayToString(multiplicand), mDigit);

            // carrier row - last one gets "+" prefix
            if (row == n - 1) {
                System.out.printf("+   %" + colWidth + "s   carriers for (%s x %d)%n",
                        carrierStr, arrayToString(multiplicand), mDigit);
            } else {
                System.out.printf("    %" + colWidth + "s   carriers for (%s x %d)%n",
                        carrierStr, arrayToString(multiplicand), mDigit);
            }
        }
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

        // arrays passed into simpleMultiply to receive partial and carrier for printing
        int[][] partial = new int[n][n];
        int[][] carrier = new int[n][n];

        // run the algorithm
        int[] result = simpleMultiply(multiplicand, multiplier, n, partial, carrier);
        updateTotal();

        // for small n: show full formatted multiplication layout with steps
        // for large n: skip display to avoid flooding the console
        if (n <= 10) {
            int colWidth = n + 14;
            String sep   = "    " + "-".repeat(colWidth + 4);
            System.out.printf("    %" + colWidth + "s%n", arrayToString(multiplicand));
            System.out.printf("x   %" + colWidth + "s%n", arrayToString(multiplier));
            System.out.println(sep);
            printSteps(partial, carrier, multiplicand, multiplier, n);
            System.out.println(sep);
            System.out.printf("    %" + colWidth + "s%n", stripLeadingZeros(result));
            System.out.println("    " + "=".repeat(colWidth + 4));
        } else {
            System.out.println("(Step-by-step output suppressed for n > 10)");
            System.out.println("Result has " + stripLeadingZeros(result).length() + " digits.");
        }
        System.out.println();

        System.out.println("Primitive operation count for n=" + n + ":");
        printCounters();

        // performance sweep: n=1 to user's n, write to CSV
        System.out.println("\nRunning performance sweep n=1 to " + n + "...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SimpleMultiplicationDataset.csv"))) {

            writer.write("n,TotalOps,Assignments,ArrayLookups,Comparisons,Arithmetic,MethodCalls,Returns\n");

            for (int digitCount = 1; digitCount <= n; digitCount++) {
                int[] num1              = generateRandomNumber(rand, digitCount);
                int[] num2              = generateRandomNumber(rand, digitCount);
                int[][] partialStorage  = new int[digitCount][digitCount];
                int[][] carrierStorage  = new int[digitCount][digitCount];

                simpleMultiply(num1, num2, digitCount, partialStorage, carrierStorage);
                updateTotal();

                writer.write(digitCount + "," + totalCounter + "," + assignmentCounter + ","
                        + arrayLookupCounter + "," + comparisonCounter + ","
                        + arithmeticCounter + "," + methodCallCounter + ","
                        + returnCounter + "\n");
            }

        } catch (IOException e) {
            System.out.println("Could not write CSV: " + e.getMessage());
        }

        System.out.println("Done. Check SimpleMultiplicationDataset.csv for the data.");
    }
}