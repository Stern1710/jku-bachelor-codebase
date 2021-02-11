package exercise03.example02;

import static org.junit.Assert.*;
import java.util.Arrays;

import exercise03.example02.MyLinkedList;
import exercise03.example02.MyNode;
import exercise03.example02.RadixSort;
import org.junit.Test;

public class TestA03ex02 {
	/**
	 * ************************************
	 * Test RadixSort
	 * ************************************
	 */
	Integer testArray[] = {33, 914, 17216, 2123, 21123, 164584, 5142, 412, 191, 17125, 2231, 2123, 22, 6711, 21, 112314515, 0};
	
	@Test
	public void testRadixSort() {
		
		MyLinkedList res = RadixSort.sort(testArray.clone());
		Arrays.sort(testArray);
		
		MyNode tmp = res.head;
		for(int i = 0; i<testArray.length; i++) {
			assertTrue(tmp!=null);
			assertEquals(tmp.value, testArray[i]);
			tmp = tmp.next;
		}
	}

	// ...
}
