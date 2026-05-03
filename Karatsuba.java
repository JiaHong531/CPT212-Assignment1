/// Java Program for CPT212 Assignment 1 - Part 2
/// Karatsuba Algorithm with Operation Counters

import java.math.BigInteger;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;

// MAin class
class Karatsuba {

    // ====================== COUNTERS ======================
    // Primitive operation counters for Karatsuba Algorithm
    public static long assignmentCounter = 0;
    public static long comparisonCounter  = 0;
    public static long additionCounter    = 0;
    public static long subtractionCounter = 0;
    public static long multiplicationCounter = 0;
    public static long divisionCounter    = 0;
    public static long moduloCounter      = 0;
    public static long methodCallCounter  = 0;
    public static long returnCounter      = 0;
    public static long totalCounter       = 0;

    // Main driver method - implements Karatsuba multiplication algorithm
    // Recursively multiplies two BigInteger numbers using divide-and-conquer approach
    public static BigInteger mult(BigInteger x, BigInteger y) {

        // counting the call to this method itself
        methodCallCounter++;

        // Base case: if both numbers are single digit, perform direct multiplication
        if (x.compareTo(BigInteger.TEN) < 0 && y.compareTo(BigInteger.TEN) < 0) {
            comparisonCounter += 2;
            multiplicationCounter += 1;
            returnCounter += 1;
            return x.multiply(y);
        }

        // Declaring variables in order to find length of both integer numbers x and y
        int noOneLength = numLength(x);
        int noTwoLength = numLength(y);
        methodCallCounter += 2;
        assignmentCounter += 2;

        // Finding maximum length from both numbers using math library max function
        int maxNumLength = Math.max(noOneLength, noTwoLength);
        comparisonCounter += 1;
        assignmentCounter += 1;

        // Rounding up the divided Max length: (maxNumLength / 2) + (maxNumLength % 2)
        int halfMaxNumLength = (maxNumLength / 2) + (maxNumLength % 2);
        assignmentCounter += 1;
        divisionCounter += 1;      // for /
        moduloCounter += 1;        // for %
        additionCounter += 1;      // for the + operator

        // Multiplier - compute 10^halfMaxNumLength
        BigInteger maxNumLengthTen = BigInteger.TEN.pow(halfMaxNumLength);
        multiplicationCounter += 1;
        assignmentCounter += 1;

        // Compute the expressions - split x and y into high and low parts
        BigInteger a = x.divide(maxNumLengthTen);
        BigInteger b = x.mod(maxNumLengthTen);
        BigInteger c = y.divide(maxNumLengthTen);
        BigInteger d = y.mod(maxNumLengthTen);
        divisionCounter += 2;
        moduloCounter += 2;
        assignmentCounter += 4;

        // Recursive calls - Compute all multiplying variables needed
        // using Karatsuba formula: z0, z1 = (a+b)(c+d), z2
        BigInteger z0 = mult(a, c);
        BigInteger z1 = mult(a.add(b), c.add(d));
        BigInteger z2 = mult(b, d);
        methodCallCounter += 3;
        additionCounter += 2;       // a.add(b) and c.add(d)
        assignmentCounter += 3;

        // Final result: ans = z0 * 10^(2*half) + (z1 - z0 - z2) * 10^half + z2
        BigInteger ans = z0.multiply(BigInteger.TEN.pow(halfMaxNumLength * 2))
                .add((z1.subtract(z0).subtract(z2)).multiply(BigInteger.TEN.pow(halfMaxNumLength)))
                .add(z2);

        multiplicationCounter += 3;
        additionCounter += 2;
        subtractionCounter += 2;
        assignmentCounter += 1;
        returnCounter += 1;

        return ans;
    }

    // Method 1
    // To calculate length of the number (number of digits)
    public static int numLength(BigInteger n)
    {
        int noLen = 0;
        assignmentCounter += 1;

        while (n.compareTo(BigInteger.ZERO) > 0) {
            noLen++;
            n = n.divide(BigInteger.TEN);
            comparisonCounter += 1;
            divisionCounter += 1;
            assignmentCounter += 2;
        }

        // Returning length of number n
        returnCounter += 1;
        return noLen;
    }

    // Helper method to generate random n-digit number (no leading zero)
    private static String generateRandomNumber(Random rand, int n)
    {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int digit = (i == 0) ? rand.nextInt(9) + 1 : rand.nextInt(10);
            num.append(digit);
        }
        return num.toString();
    }

    // Method 2
    // Main driver function
    public static void main(String[] args)
    {

        Random rand = new Random();
        int MAX_N = 10000;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("KaratsubaDataset.csv"))) {
            writer.write("Number of digits (n),Total Primitive Operations,Assignments,Comparisons,Additions,Subtractions,Multiplications,Divisions,Modulos,MethodCalls,Returns\n"");

            for (int n = 1; n <= MAX_N; n++) {

                // Reset all counters before each test case
                assignmentCounter = 0;
                comparisonCounter = 0;
                additionCounter = 0;
                subtractionCounter = 0;
                multiplicationCounter = 0;
                divisionCounter = 0;
                moduloCounter = 0;
                methodCallCounter = 0;
                returnCounter = 0;

                String xStr = generateRandomNumber(rand, n);
                String yStr = generateRandomNumber(rand, n);

                BigInteger x = new BigInteger(xStr);
                BigInteger y = new BigInteger(yStr);

                BigInteger expectedProduct = x.multiply(y);
                BigInteger actualProduct = mult(x, y);

                // Calculate total operations
                totalCounter = assignmentCounter + comparisonCounter + additionCounter +
                        subtractionCounter + multiplicationCounter + divisionCounter +
                        moduloCounter + methodCallCounter + returnCounter;

                // Clean output: only n, Expected, Actual and Total Operations
                System.out.println("n = " + n);
                System.out.println("Multiplicand : " + x);
                System.out.println("Multiplier   : " + y);
                System.out.println("Expected     : " + expectedProduct);
                System.out.println("Actual       : " + actualProduct);
                System.out.println("Total Operations : " + totalCounter);
                System.out.println();

                // Write to CSV
                writer.write(n + "," + totalCounter + "," + assignmentCounter + "," +
                        comparisonCounter + "," + additionCounter + "," + subtractionCounter + "," +
                        multiplicationCounter + "," + divisionCounter + "," + moduloCounter + "," +
                        methodCallCounter + "," + returnCounter + "\n");
            }

            System.out.println("Dataset successfully written to KaratsubaDataset.csv");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}