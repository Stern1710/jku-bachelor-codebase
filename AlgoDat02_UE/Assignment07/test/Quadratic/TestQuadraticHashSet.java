package Quadratic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import Quadratic.MyHashSet;
import Quadratic.QuadraticHashSet;
import org.junit.Test;

public class TestQuadraticHashSet {

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
		QuadraticHashSet set = new QuadraticHashSet(17);
		
		assertEquals(0, set.size());
		
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"6","6");		assertEquals(2, set.size());
		insert(set,"7","7");		assertEquals(3, set.size());
		insert(set,"8","8");		assertEquals(4, set.size());
		insert(set,"9","9");		assertEquals(5, set.size());
		insert(set,"10","10");		assertEquals(6, set.size());
		insert(set,"11","11");		assertEquals(7, set.size());
		insert(set,"12","12");		assertEquals(8, set.size());
		insert(set,"13","13");		assertEquals(9, set.size());
		insert(set,"14","14");		assertEquals(10, set.size());
		insert(set,"20","20");		assertEquals(11, set.size());
		insert(set,"19","19");		assertEquals(12, set.size());
		insert(set,"18","18");		assertEquals(13, set.size());
		insert(set,"17","17");		assertEquals(14, set.size());
		insert(set,"16","16");		assertEquals(15, set.size());
		insert(set,"25","25");		assertEquals(16, set.size());
		insert(set,"26","26");		assertEquals(16, set.size());
	}
	
	@Test
	public void testSizeInclRemove() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		
		assertEquals(0, set.size());
		
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"6","6");		assertEquals(2, set.size());
		insert(set,"7","7");		assertEquals(3, set.size());
		remove(set,"5");			assertEquals(2, set.size());
		remove(set,"6");			assertEquals(1, set.size());
		remove(set,"7");			assertEquals(0, set.size());
	}
	
	@Test
    public void testInsert() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		
		assertEquals(0, set.size());
		
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"6","6");		assertEquals(2, set.size());
		insert(set,"7","7");		assertEquals(3, set.size());
		insert(set,"8","8");		assertEquals(4, set.size());
		insert(set,"9","9");		assertEquals(5, set.size());
		insert(set,"10","10");		assertEquals(6, set.size());
		insert(set,"11","11");		assertEquals(7, set.size());
		insert(set,"12","12");		assertEquals(8, set.size());
		insert(set,"13","13");		assertEquals(9, set.size());
		insert(set,"14","14");		assertEquals(10, set.size());
		insert(set,"20","20");		assertEquals(11, set.size()); // 3
		insert(set,"19","19");		assertEquals(12, set.size()); // 2
		insert(set,"18","18");		assertEquals(13, set.size()); // 1
		insert(set,"17","17");		assertEquals(14, set.size()); // 0
		insert(set,"16","16");		assertEquals(15, set.size());
		insert(set,"25","25");		assertEquals(16, set.size()); // 16
		insert(set,"26","26");		assertEquals(16, set.size()); // 4
		
		String s = set.toString();
		assertTrue(s.contains("5"));
		assertTrue(s.contains("6"));
		assertTrue(s.contains("7"));
		assertTrue(s.contains("8"));
		assertTrue(s.contains("9"));
		assertTrue(s.contains("10"));
		assertTrue(s.contains("11"));
		assertFalse(s.contains("27"));
		assertFalse(s.contains("30"));
		assertFalse(s.contains("31"));
	}
	
	@Test
    public void testContains() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		
		assertEquals(0, set.size());
		
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"6","6");		assertEquals(2, set.size());
		insert(set,"7","7");		assertEquals(3, set.size());
		insert(set,"8","8");		assertEquals(4, set.size());
		insert(set,"9","9");		assertEquals(5, set.size());
		insert(set,"10","10");		assertEquals(6, set.size());
		insert(set,"11","11");		assertEquals(7, set.size());
		insert(set,"12","12");		assertEquals(8, set.size());
		insert(set,"13","13");		assertEquals(9, set.size());
		insert(set,"14","14");		assertEquals(10, set.size());
		insert(set,"20","20");		assertEquals(11, set.size());
		insert(set,"19","19");		assertEquals(12, set.size());
		insert(set,"18","18");		assertEquals(13, set.size());
		insert(set,"17","17");		assertEquals(14, set.size());
		insert(set,"16","16");		assertEquals(15, set.size());
		insert(set,"25","25");		assertEquals(16, set.size());
		insert(set,"26","26");		assertEquals(16, set.size());
		
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
		assertFalse(contains(set, "15"));
		assertFalse(contains(set, "1"));
		assertFalse(contains(set, "2"));
		assertFalse(contains(set, "3"));
		assertFalse(contains(set, "4"));
		assertTrue(contains(set, "16"));
		assertTrue(contains(set, "17"));
		assertTrue(contains(set, "25"));
		assertFalse(contains(set, "26"));
	}
	
	@Test
    public void testRemove() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		
		assertEquals(0, set.size());
		
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"6","6");		assertEquals(2, set.size());
		insert(set,"7","7");		assertEquals(3, set.size());
		insert(set,"8","8");		assertEquals(4, set.size());
		insert(set,"9","9");		assertEquals(5, set.size());
		insert(set,"10","10");		assertEquals(6, set.size());
		insert(set,"11","11");		assertEquals(7, set.size());
		insert(set,"12","12");		assertEquals(8, set.size());
		insert(set,"13","13");		assertEquals(9, set.size());
		insert(set,"14","14");		assertEquals(10, set.size());
		insert(set,"20","20");		assertEquals(11, set.size());
		insert(set,"19","19");		assertEquals(12, set.size());
		insert(set,"18","18");		assertEquals(13, set.size());
		insert(set,"17","17");		assertEquals(14, set.size());
		insert(set,"16","16");		assertEquals(15, set.size());
		insert(set,"25","25");		assertEquals(16, set.size());
		insert(set,"26","26");		assertEquals(16, set.size());
		
		assertTrue(remove(set, "20"));		assertEquals(15, set.size());
		assertFalse(remove(set, "20"));		assertEquals(15, set.size());
		assertTrue(remove(set, "9"));		assertEquals(14, set.size());
		assertFalse(remove(set, "9"));		assertEquals(14, set.size()); 
	}
	
	@Test
    public void testToString() {
		//visual Test: Console
		System.out.println("Test toString");
		System.out.println("The internal structure of the hash table should look similar to this:");
		System.out.println("0 {20}, 1 {25,12}, 2 {-}, ...");
		
		//...

		System.out.println("\nSolution:");
		//System.out.println(set.toString());
	}
	
	
	private boolean testHashTable(Integer[] testHashTable, QuadraticHashSet set) {
		OpenHashNode [] quadraticHashTable = set.getHashTable();
		
		if(testHashTable.length != quadraticHashTable.length)
			return false;
		
		
		for(int i = 0; i < testHashTable.length; i++) {
			if(quadraticHashTable[i] == null) {
				if (testHashTable[i] != null) return false;
			}
			else {		
				if (testHashTable[i] != quadraticHashTable[i].key)
					return false;
			}
		}
		
		return true;
	}
	
	Integer [] testHashTable_null  =  {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
	Integer [] testHashTable_Step1 =  {34,null,null,null,21,5,6,7,null,null,44,61,null,null,null,null,null};
	Integer [] testHashTable_Step2 =  {34,null,null,null,21,5,6,7,null,78,44,61,null,null,null,null,null};
	Integer [] testHashTable_Step3 =  {34,null,null,null,21,5,6,7,null,78,44,61,null,null,10,null,null};
	Integer [] testHashTable_Step4 =  {34,null,null,null,21,5,6,7,null,78,44,61,11,null,10,null,16};
	Integer [] testHashTable_Step5 =  {34,17,null,null,21,5,6,7,null,78,44,61,11,null,10,null,16};
	Integer [] testHashTable_Step6 =  {34,17,null,null,21,5,6,7,null,78,44,61,11,12,10,null,16};
	Integer [] testHashTable_Step7 =  {34,17,100,56,21,5,6,7,null,78,44,61,11,12,10,33,16};
	Integer [] testHashTable_RemoveFindInsert_Step1 =  {34,null,null,null,21,5,6,7,null,78,44,61,null,null,10,null,null};
	Integer [] testHashTable_RemoveFindInsert_Step3 =  {34,null,null,null,21,5,6,7,null,78,44,95,null,null,10,null,null};
	@Test
	public void testHashTable_Step1() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		
		assertTrue(testHashTable(testHashTable_Step1, set));
	}
	
	@Test
	public void testHashTable_Step2() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		assertTrue(testHashTable(testHashTable_Step2, set));
	}
	@Test
	public void testHashTable_Step3() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");
		assertTrue(testHashTable(testHashTable_Step3, set));
	}
	@Test
	public void testHashTable_Step4() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");

		insert(set, "16", "16");
		insert(set, "11", "11");
		assertTrue(testHashTable(testHashTable_Step4, set));
	}
	
	@Test
	public void testHashTable_Step5() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");
		insert(set, "16", "16");
		insert(set, "11", "11");
		
		insert(set, "17", "17");
		assertTrue(testHashTable(testHashTable_Step5, set));
	}
	
	@Test
	public void testHashTable_Step6() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");
		insert(set, "16", "16");
		insert(set, "11", "11");
		insert(set, "17", "17");
		
		insert(set, "12", "12");
		assertTrue(testHashTable(testHashTable_Step6, set));
	}
	
	@Test
	public void testHashTable_Step7() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");
		insert(set, "16", "16");
		insert(set, "11", "11");
		insert(set, "17", "17");
		insert(set, "12", "12");
		
		insert(set, "33", "33"); // idx 15
		insert(set, "13", "13"); // cannot be inserted 
		insert(set, "56", "56"); // idx 3
		insert(set, "14", "14"); // cannot be inserted
		insert(set, "100", "100"); // idx 2
		insert(set, "15", "15"); // cannot be inserted
		assertTrue(testHashTable(testHashTable_Step7, set));
	}
	
	@Test
	public void testHashTable_RemoveFindInsert_Step1() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		OpenHashNode[] hashTable = set.getHashTable();
		
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");
		
		// test removed flag
		assertFalse(hashTable[11].removed);
		remove(set, "61");
		assertTrue(testHashTable(testHashTable_RemoveFindInsert_Step1, set));
		assertTrue(hashTable[11].removed);
	}
	
	@Test
	public void testHashTable_RemoveFindInsert_Step2() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		
		assertFalse(contains(set, "78"));
		insert(set, "78", "78");	// two collisions
		assertTrue(contains(set, "78"));
		insert(set, "10", "10");	// 3 collisions
		
		remove(set, "61");

		// these still must be found
		assertTrue(contains(set,"78"));
		assertTrue(contains(set,"10"));
	}
	
	@Test
	public void testHashTable_RemoveFindInsert_Step3() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		OpenHashNode[] hashTable = set.getHashTable();
		
		assertEquals(0, set.size());
		
		insert(set, "5", "5");		
		insert(set, "34", "34");
		insert(set, "5", "5");
		insert(set, "21", "21");
		insert(set, "6", "6");	
		insert(set, "44", "44");
		insert(set, "7", "7");
		insert(set, "61", "61");	// first collision
		insert(set, "78", "78");	// two collisions
		insert(set, "10", "10");	// 3 collisions
		
		remove(set, "61");
		
		// test removed flag again
		assertTrue(hashTable[11].removed);
		insert(set, "95", "95");
		assertFalse(hashTable[11].removed);
		
		assertTrue(testHashTable(testHashTable_RemoveFindInsert_Step3, set));
	}
	
	@Test
    public void testClear() {
		QuadraticHashSet set = new QuadraticHashSet(17);
		
		assertEquals(0, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"5","5");		assertEquals(1, set.size());
		insert(set,"6","6");		assertEquals(2, set.size());
		insert(set,"7","7");		assertEquals(3, set.size());
		insert(set,"8","8");		assertEquals(4, set.size());
		insert(set,"9","9");		assertEquals(5, set.size());
		insert(set,"10","10");		assertEquals(6, set.size());
		insert(set,"11","11");		assertEquals(7, set.size());
		insert(set,"12","12");		assertEquals(8, set.size());
		insert(set,"13","13");		assertEquals(9, set.size());
		insert(set,"14","14");		assertEquals(10, set.size());
		insert(set,"20","20");		assertEquals(11, set.size());
		insert(set,"19","19");		assertEquals(12, set.size());
		insert(set,"18","18");		assertEquals(13, set.size());
		insert(set,"17","17");		assertEquals(14, set.size());
		insert(set,"16","16");		assertEquals(15, set.size());
		insert(set,"25","25");		assertEquals(16, set.size());
		insert(set,"26","26");		assertEquals(16, set.size());
		
		set.clear();
		
		assertEquals(0, set.size());
		assertTrue(testHashTable(testHashTable_null, set));
	}
}
