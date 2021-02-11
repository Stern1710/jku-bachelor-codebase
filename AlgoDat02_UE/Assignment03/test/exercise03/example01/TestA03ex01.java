package exercise03.example01;

import static org.junit.Assert.*;

import exercise03.example01.MinHeap;
import org.junit.Test;

public class TestA03ex01 {
	/**
	 * ************************************
	 * Test In-Place HeapSort based on MinHeap implementation
	 * 
	 * ************************************
	 */
	Long testArray[] = {3L, 9L, 17L, 2L, 23L, 1L, 5L, 4L, 19L, 17L, 7L, 18L, 8L, 67L, 6L, 11L, 0L};
	MinHeap<Long> heap;
	
	@Test
	public void testBottomUpHeapConstruction() {		
		try { 
			heap = new MinHeap<Long>(null);
		} catch(Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}			
		heap = new MinHeap<Long>(testArray);
		
		assertEquals(testArray.length, heap.size());
		assertEquals(Long.valueOf(0L), heap.min());
		
		assertEquals(Long.valueOf(0L), heap.removeMin());
		assertEquals(Long.valueOf(1L), heap.removeMin());
		assertEquals(Long.valueOf(2L), heap.removeMin());
		assertEquals(Long.valueOf(3L), heap.removeMin());
		assertEquals(Long.valueOf(4L), heap.removeMin());
		assertEquals(Long.valueOf(5L), heap.removeMin());
		assertEquals(Long.valueOf(6L), heap.removeMin());
		assertEquals(Long.valueOf(7L), heap.removeMin());
		assertEquals(Long.valueOf(8L), heap.removeMin());
		assertEquals(Long.valueOf(9L), heap.removeMin());
		assertEquals(Long.valueOf(11L), heap.removeMin());
		assertEquals(Long.valueOf(17L), heap.removeMin());
		assertEquals(Long.valueOf(17L), heap.removeMin());
		assertEquals(Long.valueOf(18L), heap.removeMin());
		assertEquals(Long.valueOf(19L), heap.removeMin());
		assertEquals(Long.valueOf(23L), heap.removeMin());
		assertEquals(Long.valueOf(67L), heap.removeMin());
		
		assertEquals(0, heap.size());
		assertEquals(true, heap.isEmpty());
	}
	
	@Test
	public void testContains() {
		heap = new MinHeap<Long>(testArray);
		assertTrue(heap.contains(Long.valueOf(3)));
		assertTrue(heap.contains(Long.valueOf(9)));
		assertTrue(heap.contains(Long.valueOf(17)));
		assertTrue(heap.contains(Long.valueOf(2)));
		assertTrue(heap.contains(Long.valueOf(23)));
		assertTrue(heap.contains(Long.valueOf(1)));
		assertTrue(heap.contains(Long.valueOf(5)));
		assertTrue(heap.contains(Long.valueOf(4)));
		assertTrue(heap.contains(Long.valueOf(19)));
		assertTrue(heap.contains(Long.valueOf(17)));
		assertTrue(heap.contains(Long.valueOf(7)));
		assertTrue(heap.contains(Long.valueOf(18)));
		assertTrue(heap.contains(Long.valueOf(8)));
		assertTrue(heap.contains(Long.valueOf(67)));
		assertTrue(heap.contains(Long.valueOf(6)));
		assertTrue(heap.contains(Long.valueOf(11)));
		assertTrue(heap.contains(Long.valueOf(0)));
		
		assertFalse(heap.contains(Long.valueOf(44)));

		// ...
		// ...
	}
		
	@Test
	public void testSort() {
		heap = new MinHeap<Long>(testArray);
		MinHeap.sort(testArray);
		
		assertEquals(Long.valueOf(0L), testArray[16]);
		assertEquals(Long.valueOf(1L), testArray[15]);
		assertEquals(Long.valueOf(2L), testArray[14]);
		assertEquals(Long.valueOf(3L), testArray[13]);
		assertEquals(Long.valueOf(4L), testArray[12]);
		assertEquals(Long.valueOf(5L), testArray[11]);
		assertEquals(Long.valueOf(6L), testArray[10]);
		assertEquals(Long.valueOf(7L), testArray[9]);
		assertEquals(Long.valueOf(8L), testArray[8]);
		assertEquals(Long.valueOf(9L), testArray[7]);
		assertEquals(Long.valueOf(11L), testArray[6]);
		assertEquals(Long.valueOf(17L), testArray[5]);
		assertEquals(Long.valueOf(17L), testArray[4]);
		assertEquals(Long.valueOf(18L), testArray[3]);
		assertEquals(Long.valueOf(19L), testArray[2]);
		assertEquals(Long.valueOf(23L), testArray[1]);
		assertEquals(Long.valueOf(67L), testArray[0]);
	}

	@Test
	public void testRunTime() {
		int numberCount = 1000;
		Long prior[] = new Long[numberCount];
		
		for(int i = 0; i<numberCount; i++) prior[i] = Long.valueOf(Double.valueOf(10000000*Math.random()).longValue());
		
		Long s = System.currentTimeMillis();
		heap = new MinHeap<Long>(prior);
		Long e = System.currentTimeMillis();
		System.out.println("Numbers: "+numberCount+"; Time: "+(e-s)+";");
				
		// ...
		// ...
	}
}
