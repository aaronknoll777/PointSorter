package edu.iastate.cs2280.hw2;

/**
 *  
 * @author Aaron Knoll
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		Scanner scnr = new Scanner(System.in);
		int trialNum = 1;
		
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
		
		while (true) {
			System.out.print("Trial " + trialNum + ": ");
			int choice = scnr.nextInt();
			
			Point[] points = null;
			String fileName = null;
			PointScanner[] scanners = new PointScanner[4]; 
			
			if(choice == 3) {
				break;
			}
			else if(choice == 1) {
				System.out.print("Enter number of random points: ");
				int numOfPts = scnr.nextInt();
				Random rand = new Random();
				points = generateRandomPoints(numOfPts, rand);
				scanners[0] = new PointScanner(points, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(points, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(points, Algorithm.MergeSort);
				scanners[3] = new PointScanner(points, Algorithm.QuickSort);
			}
			else{
				System.out.println("Points from a file");
				System.out.print("File name: ");
				fileName = scnr.next();
				scanners[0] = new PointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new PointScanner(fileName, Algorithm.QuickSort);
			}
			
			for(PointScanner s : scanners) {
				s.scan();
			}
			
			System.out.println("algorithm       size   time (ns)");
	        System.out.println("----------------------------------");
	        for (PointScanner s : scanners) {
	            System.out.print(s.stats());
	        }
	        System.out.println("----------------------------------");
	        
	        trialNum++;
		}
		scnr.close();
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if (numPts < 1) {
            throw new IllegalArgumentException("Number of points must be at least 1.");
        }

        Point[] points = new Point[numPts];
        for (int i = 0; i < numPts; i++) {
            int x = rand.nextInt(101) - 50;
            int y = rand.nextInt(101) - 50;
            points[i] = new Point(x, y);
        }
        return points;
	}
	
}
