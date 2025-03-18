package com.example.MailEx;

import java.util.*;

public class Demo {

	public static double AverageNumber(LinkedList<Object> l) {
		double sum = 0;
		int count = 0;
		for (Object obj : l) {
			if (obj instanceof Integer) {
				sum += (Integer) obj;
				count++;
			} else if (obj instanceof Double) {
				sum += (Double) obj;
				count++;
			}
		}
		if (count == 0) {
			return 0;
		}
		return sum / count;
	}

	public static Object SecondLargestByLength(LinkedList<Object> l) {
		int largestLength = -1;
		int secondLargestLength = -1;
		Object largestElement = null;
		Object secondLargestElement = null;
		for (Object obj : l) {
			int length = 0;
			if (obj instanceof Integer) {
				length = String.valueOf(obj).length();
			} else if (obj instanceof String) {
				length = ((String) obj).length();
			} else if (obj instanceof Double) {
				length = String.valueOf(obj).length();
			}
			if (length > largestLength) {
				secondLargestLength = largestLength;
				secondLargestElement = largestElement;
				largestLength = length;
				largestElement = obj;
			} else if (length > secondLargestLength && length < largestLength) {
				secondLargestLength = length;
				secondLargestElement = obj;
			}
		}
		return secondLargestElement;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the length of the list:");
		int len = sc.nextInt();

		LinkedList<Object> list = new LinkedList<>();
		for (int i = 1; i <= len; i++) {
			System.out.println("Enter element " + i + ":");
			String input = sc.next();
			try {
				if (input.contains(".")) {
					list.add(Double.parseDouble(input));
				} else {
					list.add(Integer.parseInt(input));
				}
			} catch (NumberFormatException e) {
				list.add(input);
			}

		}
		System.out.println("Average of given numbers is: " + AverageNumber(list));
		System.out.println("Second Largest number/text by Length: " + SecondLargestByLength(list));
	}
}
