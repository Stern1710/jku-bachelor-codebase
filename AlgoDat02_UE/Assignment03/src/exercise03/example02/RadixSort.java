package exercise03.example02;

import java.util.Arrays;
import java.util.Collections;

public class RadixSort {

	/* The complexity of this algorithm is:
    	N ... length of list
    	m ... number of buckets
    	Ideal complexity of Bucketsort is O(N+m) as
    		* Storing numbers into bucket accords to O(N)
    		* Merging numbers into a single list accords to O(m)

    	Biggest depth in bucketsort with m*N for going through all items and sorting them
    	--> O(m*N)
    	merge		O(N)
    	getDigit	O(1)
    	sort		O(N)
    */
	public static MyLinkedList sort(Integer list[]) throws IllegalArgumentException {
		if (list == null) throw new IllegalArgumentException("List must not be null");
		for (Integer i : list) {
			if (i == null) throw new IllegalArgumentException("List elements must not be null");
		}
		//Idea: As we are dealing with numbers 0..9, use 10 LinkedLists whereas index from 0...9
		MyLinkedList[] buckets = new MyLinkedList[10];
		for (int i=0; i < buckets.length; i++) {
			buckets[i] = new MyLinkedList();
		}
		//Fill first list with Integer values fro passed array
		for (Integer i : list) {
			buckets[0].add(i);
		}

		// Idea: Get maximum value and from this one the length --> Maximum sorting position
		int digits = Collections.max(Arrays.asList(list)).toString().length();
		for (int k=0; k <= digits; k++) {
			bucketSort(k, buckets);
		}

		merge(buckets); //Merge all into bucket with index 0
		return buckets[0];
	}

	// returns the digit (base10) of val at position pos
	private static int getDigit(int val, int pos) {
		return (int)((val / Math.pow(10, pos)) % 10);
	}

	// calculates Buckets for position pos, Position can be like "Einer", "Zehner", "Hunderter" etc.
	private static void bucketSort(int pos, MyLinkedList[] buckets) {
		//Idea: Create a corresponding list to the current buckets
		MyLinkedList[] moreSorted = new MyLinkedList[buckets.length];
		for (int i=0; i < moreSorted.length; i++) {
			moreSorted[i] = new MyLinkedList();
		}

		//Fill new with each value from old one
		for (MyLinkedList mll : buckets) {
			for (MyNode cur = mll.head; cur != null; cur = cur.next) {
				moreSorted[getDigit(cur.value, pos)].add(cur.value);
			}
		}

		//Set overwrite old one with new one
		for (int i=0; i < moreSorted.length && i < buckets.length; i++) {
			buckets[i] = moreSorted[i];
		}
	}

	// Merges bucket lists into one list
	private static void merge(MyLinkedList[] buckets) {
		if (buckets.length > 0) {
			for (int i = 1; i < buckets.length; i++) {
				buckets[0].link(buckets[i]);
			}
		}
	}
}
