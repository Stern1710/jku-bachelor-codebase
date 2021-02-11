package Quadratic;

public interface MyHashSet {
	
	/**
	 * returns the number of stored keys (these must be unique!).
	 */
    public int size(); 	

    /**
     * Inserts a key and returns true if it was successful.
     * If there is already an entry with the same key, the new key will
     * not be inserted and false is returned.
     */ 
    public boolean insert(Integer key, String data)  
      throws IllegalArgumentException;      
       
    /**
     *  Returns true if the key is already stored, otherwise false.
     */
    public boolean contains(Integer key) 
      throws IllegalArgumentException;  

    /*
     * Removes the key and returns true if it has been removed, false otherwise.
     */
    public boolean remove(Integer key)  
      throws IllegalArgumentException; 

    /*
     *  Returns a string representation of the hash table (array indices and stored element keys)
     *  Idx_0 {Node, Node, ... }, Idx_1 {...}
	 *  0 {13}, 1 {82, 92, 12}, 2 {2, 32}, ...
	 */
    public String toString(); 	
    
    /*
     * Removes all stored elements
     */
    public void clear(); 
    
    /*
     * return the hashtable
     */
    public OpenHashNode[] getHashTable();
}
