package Chaining;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestChainingHashSet { 

	private boolean insert(MyHashSet set, String key, String data) {
		return set.insert(Integer.valueOf(key),  data);
	}
	
	private boolean contains(MyHashSet set, String key) {
		return set.contains(Integer.valueOf(key));
	}

	private boolean remove(MyHashSet set, String key) {
		return set.remove(Integer.valueOf(key));
	}
	
	@Test
	public void testSize() {
		ChainingHashSet set = new ChainingHashSet(11);
		assertEquals(0, set.size());
				
		insert(set, "5", "5");			assertEquals(1, set.size());
		insert(set, "6", "6");			assertEquals(2, set.size());
		insert(set, "7", "7");			assertEquals(3, set.size());
		insert(set, "8", "8");			assertEquals(4, set.size());
		insert(set, "9", "9");			assertEquals(5, set.size());
		insert(set, "10", "10");		assertEquals(6, set.size());
		insert(set, "11", "11");		assertEquals(7, set.size());
		insert(set, "12", "12");		assertEquals(8, set.size());
		insert(set, "13", "13");		assertEquals(9, set.size());
		insert(set, "14", "14");		assertEquals(10, set.size());
		insert(set, "15", "15");		assertEquals(11, set.size());
		insert(set, "5", "5");			assertEquals(11, set.size());
	}
	
	@Test
    public void testInsert() {
		ChainingHashSet set = new ChainingHashSet(11);
		assertEquals(0, set.size());
		insert(set, "5", "5");			assertEquals(1, set.size());
		insert(set, "5", "5");			assertEquals(1, set.size());
		insert(set, "6", "6");			assertEquals(2, set.size());
		insert(set, "7", "7");			assertEquals(3, set.size());
		insert(set, "8", "8");			assertEquals(4, set.size());
		insert(set, "9", "9");			assertEquals(5, set.size());
		insert(set, "10", "10");		assertEquals(6, set.size());
		insert(set, "11", "11");		assertEquals(7, set.size());
		insert(set, "12", "12");		assertEquals(8, set.size());
		insert(set, "10", "10");		assertEquals(8, set.size());
		insert(set, "13", "13");		assertEquals(9, set.size());
		assertFalse(insert(set, "13", "13"));		assertEquals(9, set.size());
		assertFalse(insert(set, "13", "13"));		assertEquals(9, set.size());
		
		// check content
		ChainingHashNode[] hashTable = set.getHashTable();
		// Index 0
		assertEquals(Integer.valueOf(11), hashTable[0].key);
		assertEquals(null, hashTable[0].next);
		// Index 1
		assertEquals(Integer.valueOf(12), hashTable[1].key);
		assertEquals(null, hashTable[1].next);
		// Index 2
		assertEquals(Integer.valueOf(13), hashTable[2].key);
		assertEquals(null, hashTable[2].next);
		// Index 3
		assertEquals(null, hashTable[3]);
		// Index 4
		assertEquals(null, hashTable[4]);
		// Index 5
		assertEquals(Integer.valueOf(5), hashTable[5].key);
		assertEquals(null, hashTable[5].next);
		// Index 6
		assertEquals(Integer.valueOf(6), hashTable[6].key);
		assertEquals(null, hashTable[6].next);
		// Index 7
		assertEquals(Integer.valueOf(7), hashTable[7].key);
		assertEquals(null, hashTable[7].next);
		// Index 8
		assertEquals(Integer.valueOf(8), hashTable[8].key);
		assertEquals(null, hashTable[8].next);
		// Index 9
		assertEquals(Integer.valueOf(9), hashTable[9].key);
		assertEquals(null, hashTable[9].next);
		// Index 10
		assertEquals(Integer.valueOf(10), hashTable[10].key);
		assertEquals(null, hashTable[10].next);
	}
	
	@Test
    public void testContains() {
		ChainingHashSet set = new ChainingHashSet(11);
		assertEquals(0, set.size());

		insert(set, "5", "5");			assertEquals(1, set.size());
		insert(set, "6", "6");			assertEquals(2, set.size());
		insert(set, "7", "7");			assertEquals(3, set.size());
		insert(set, "8", "8");			assertEquals(4, set.size());
		insert(set, "9", "9");			assertEquals(5, set.size());
		insert(set, "10", "10");		assertEquals(6, set.size());
		insert(set, "11", "11");		assertEquals(7, set.size());
		insert(set, "12", "12");		assertEquals(8, set.size());
		insert(set, "13", "13");		assertEquals(9, set.size());
		insert(set, "14", "14");		assertEquals(10, set.size());
		insert(set, "15", "15");		assertEquals(11, set.size());
		
		assertTrue(contains(set, "5"));
		assertTrue(contains(set, "6"));
		assertTrue(contains(set, "7"));
		assertTrue(contains(set, "8"));
		assertTrue(contains(set, "9"));
		assertTrue(contains(set, "10"));
		assertTrue(contains(set, "11"));
		assertTrue(contains(set, "12"));
		assertTrue(contains(set, "13"));
		assertTrue(contains(set, "14"));
		assertTrue(contains(set, "15"));
		assertFalse(contains(set, "1"));
		assertFalse(contains(set, "2"));
		assertFalse(contains(set, "3"));
		assertFalse(contains(set, "4"));
		assertFalse(contains(set, "16"));
		assertFalse(contains(set, "17"));
	}
	
	@Test
    public void testRemove() {
		ChainingHashSet set = new ChainingHashSet(11);
		assertEquals(0, set.size());
		insert(set, "5", "5");			assertEquals(1, set.size());
		insert(set, "6", "6");			assertEquals(2, set.size());
		insert(set, "7", "7");			assertEquals(3, set.size());
		insert(set, "8", "8");			assertEquals(4, set.size());
		insert(set, "9", "9");			assertEquals(5, set.size());
		insert(set, "10", "10");		assertEquals(6, set.size());
		insert(set, "11", "11");		assertEquals(7, set.size());
		insert(set, "12", "12");		assertEquals(8, set.size());
		insert(set, "13", "13");		assertEquals(9, set.size());
		insert(set, "14", "14");		assertEquals(10, set.size());
		insert(set, "15", "15");		assertEquals(11, set.size());
		
		assertTrue(remove(set, "15"));		assertEquals(10, set.size());
		assertFalse(remove(set, "15"));		assertEquals(10, set.size());
		assertTrue(remove(set, "9"));		assertEquals(9, set.size());
		assertFalse(remove(set, "9"));		assertEquals(9, set.size());
	}
	
	@Test
	public void testHashTable() {
		ChainingHashSet set = new ChainingHashSet(11);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");
		insert(set, "8", "8");
		insert(set, "12", "12");
		insert(set, "9", "9");
		insert(set, "78", "78");
		insert(set, "10", "10");
		insert(set, "16", "16");
		insert(set, "11", "11");
		insert(set, "17", "17");
		insert(set, "12", "12");
		insert(set, "33", "33");
		insert(set, "13", "13");
		insert(set, "56", "56");
		insert(set, "14", "14");
		insert(set, "100", "100");
		insert(set, "15", "15");
		insert(set, "99", "99");
		
		// check chaining
		ChainingHashNode[] hashTable = set.getHashTable();
		// Index 0
		assertEquals(Integer.valueOf(44), hashTable[0].key);
		assertEquals(Integer.valueOf(11), hashTable[0].next.key);
		assertEquals(Integer.valueOf(33), hashTable[0].next.next.key);
		assertEquals(Integer.valueOf(99), hashTable[0].next.next.next.key);
		assertEquals(null, hashTable[0].next.next.next.next);
		// Index 1
		assertEquals(Integer.valueOf(34), hashTable[1].key);
		assertEquals(Integer.valueOf(12), hashTable[1].next.key);
		assertEquals(Integer.valueOf(78), hashTable[1].next.next.key);
		assertEquals(Integer.valueOf(56), hashTable[1].next.next.next.key);
		assertEquals(Integer.valueOf(100), hashTable[1].next.next.next.next.key);
		assertEquals(null, hashTable[1].next.next.next.next.next);
		// Index 2
		assertEquals(Integer.valueOf(13), hashTable[2].key);
		assertEquals(null, hashTable[2].next);
		// Index 3
		assertEquals(Integer.valueOf(14), hashTable[3].key);
		assertEquals(null, hashTable[3].next);
		// Index 4
		assertEquals(Integer.valueOf(15), hashTable[4].key);
		assertEquals(null, hashTable[4].next);
		// Index 5
		assertEquals(Integer.valueOf(5), hashTable[5].key);
		assertEquals(Integer.valueOf(16), hashTable[5].next.key);
		assertEquals(null, hashTable[5].next.next);
		// Index 6
		assertEquals(Integer.valueOf(6), hashTable[6].key);
		assertEquals(Integer.valueOf(61), hashTable[6].next.key);
		assertEquals(Integer.valueOf(17), hashTable[6].next.next.key);
		assertEquals(null, hashTable[6].next.next.next);
		// Index 7
		assertEquals(Integer.valueOf(7), hashTable[7].key);
		assertEquals(null, hashTable[7].next);
		// Index 8
		assertEquals(Integer.valueOf(8), hashTable[8].key);
		assertEquals(null, hashTable[8].next);
		// Index 9
		assertEquals(Integer.valueOf(9), hashTable[9].key);
		assertEquals(null, hashTable[9].next);
		// Index 10
		assertEquals(Integer.valueOf(21), hashTable[10].key);
		assertEquals(Integer.valueOf(10), hashTable[10].next.key);
		assertEquals(null, hashTable[10].next.next);
	}
	
	@Test
    public void testToString() {
		//visual Test: Console

		System.out.println("\nTest ToString:");
		System.out.println("The internal structure of the hash table should look similar to this:");
		System.out.println("0 {44}, 1 {78, 14}, 2 {-}, ...");

		ChainingHashSet set = new ChainingHashSet(11);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");
		insert(set, "8", "8");
		insert(set, "12", "12");
		insert(set, "9", "9");
		insert(set, "78", "78");
		insert(set, "10", "10");
		insert(set, "16", "16");
		insert(set, "11", "11");
		insert(set, "17", "17");
		insert(set, "12", "12");
		insert(set, "33", "33");
		insert(set, "13", "13");
		insert(set, "56", "56");
		insert(set, "14", "14");
		insert(set, "100", "100");
		insert(set, "15", "15");
		insert(set, "99", "99");
		
		System.out.println("Solution:\n" + set.toString());
	}
	
	@Test
    public void testClear() {
		ChainingHashSet set = new ChainingHashSet(11);
		ChainingHashNode [] hashTable = set.getHashTable();
		
		assertEquals(0, set.size());

		insert(set, "5", "5");			assertEquals(1, set.size());
		insert(set, "6", "6");			assertEquals(2, set.size());
		insert(set, "7", "7");			assertEquals(3, set.size());
		insert(set, "8", "8");			assertEquals(4, set.size());
		insert(set, "9", "9");			assertEquals(5, set.size());
		insert(set, "10", "10");		assertEquals(6, set.size());
		insert(set, "11", "11");		assertEquals(7, set.size());
		insert(set, "12", "12");		assertEquals(8, set.size());
		insert(set, "13", "13");		assertEquals(9, set.size());
		insert(set, "14", "14");		assertEquals(10, set.size());
		insert(set, "15", "15");		assertEquals(11, set.size());
		
		set.clear();
		assertEquals(0, set.size());
		
		for(int i = 0; i<hashTable.length; i++) {
			assertEquals(null,hashTable[i]); 
		}
	}
}
