import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Comparison {

    public static void main(String[] args) {

        String simpleFile   = "SimpleMultiplicationDataset.csv";
        String karatsubaFile = "KaratsubaDataset.csv";
        String outputFile   = "ComparisonDataset.csv";

        Map<Integer, Long> simpleOps   = new LinkedHashMap<>();
        Map<Integer, Long> karatsubaOps = new LinkedHashMap<>();

        // Read Simple Multiplication dataset
        try (BufferedReader reader = new BufferedReader(new FileReader(simpleFile))) {
            reader.readLine(); 
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int n        = Integer.parseInt(parts[0].trim());
                long totalOps = Long.parseLong(parts[1].trim());
                simpleOps.put(n, totalOps);
            }
        } catch (IOException e) {
            System.out.println("Could not read " + simpleFile + ": " + e.getMessage());
            return;
        }

        // Read Karatsuba dataset
        try (BufferedReader reader = new BufferedReader(new FileReader(karatsubaFile))) {
            reader.readLine(); 
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int n        = Integer.parseInt(parts[0].trim());
                long totalOps = Long.parseLong(parts[1].trim());
                karatsubaOps.put(n, totalOps);
            }
        } catch (IOException e) {
            System.out.println("Could not read " + karatsubaFile + ": " + e.getMessage());
            return;
        }

        // Write merged comparison CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            writer.write("Number of digits (n),Simple Multiplication,Karatsuba\n");

            for (int n : simpleOps.keySet()) {
                long simple   = simpleOps.getOrDefault(n, -1L);
                long karatsuba = karatsubaOps.getOrDefault(n, -1L);
                writer.write(n + "," + simple + "," + karatsuba + "\n");
            }

        } catch (IOException e) {
            System.out.println("Could not write " + outputFile + ": " + e.getMessage());
            return;
        }

        System.out.println("Done. Check ComparisonDataset.csv for the merged data.");
    }
}