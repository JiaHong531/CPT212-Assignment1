# CPT212 Assignment 1

**Course:** CPT212 Design & Analysis of Algorithms  
**Semester:** II, Academic Session 2025/2026  
**Group Members:** Tan Jia Hong (23303405), Ng Xin Yuan (23302695)

## Overview
Implementation and complexity analysis of two multiplication algorithms:
- **Simple Multiplication** - digit-by-digit long multiplication (O(n²))
- **Karatsuba Algorithm** - divide-and-conquer multiplication (O(n^1.585))

## Files
- `SimpleMultiplication.java` - Simple Multiplication with primitive operation counters
- `Karatsuba.java` - Karatsuba Algorithm with primitive operation counters
- `Comparison.java` - Merges both CSV datasets for graph plotting
- `SimpleMultiplicationDataset.csv` - Operation count data for Simple Multiplication
- `KaratsubaDataset.csv` - Operation count data for Karatsuba
- `ComparisonDataset.csv` - Combined dataset for comparison graphs

## How to Run
```bash
javac SimpleMultiplication.java
java SimpleMultiplication

javac Karatsuba.java
java Karatsuba

javac Comparison.java
java Comparison
```