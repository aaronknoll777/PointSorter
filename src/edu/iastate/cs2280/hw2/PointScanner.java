package edu.iastate.cs2280.hw2;

/**
 *  
 * @author Aaron Knoll
 *
 */

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.io.*;
import java.util.*;

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
		
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if(pts == null || pts.length == 0) {
			throw new IllegalArgumentException("Point array cannot be null or empty");
		}
		this.points = Arrays.copyOf(pts, pts.length);
		this.sortingAlgorithm = algo;
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		this.sortingAlgorithm = algo;
		List<Point> pointList = new ArrayList<>();
		
		try (Scanner scanner = new Scanner(new File(inputFileName))){
			while(scanner.hasNextInt()) {
				int xPoint = scanner.nextInt();
				if(!scanner.hasNextInt()) {
					throw new InputMismatchException("File contains an odd number of integers");
				}
				int yPoint = scanner.nextInt();
				pointList.add(new Point(xPoint, yPoint));
			}
		}
		this.points = pointList.toArray(new Point[0]);
	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		if(points.length == 0) {
			return;
		}
		
		AbstractSorter aSorter; 
		
		switch(sortingAlgorithm) {
		case SelectionSort:
			aSorter = new SelectionSorter(points);
			break;
		case InsertionSort:
			aSorter = new InsertionSorter(points);
			break;
		case MergeSort:
			aSorter = new MergeSorter(points);
			break;
		case QuickSort:
			aSorter = new QuickSorter(points);
			break;
		default:
			throw new IllegalArgumentException("Unsupported sorting algorithm");
		}
		
		long startTime = System.nanoTime();
		
		aSorter.setComparator(0);
		
		aSorter.sort();
		int medX = aSorter.points[(points.length - 1) / 2].getX();
		
		aSorter.setComparator(1);
		aSorter.sort();
		int medY = aSorter.points[(points.length - 1) / 2].getY();
		
		long endTime = System.nanoTime();
		scanTime = endTime - startTime;
		
		medianCoordinatePoint = new Point(medX, medY);
		
		try {
			writeMCPToFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		return String.format("%-15s %-6d %-10d%n", sortingAlgorithm, points.length, scanTime); 
	}
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		return String.format("MCP: (%d, %d)", medianCoordinatePoint.getX(), medianCoordinatePoint.getY()); 
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		try(PrintWriter writer = new PrintWriter("output.txt")){
			writer.println(toString());
		}
	}		
}
