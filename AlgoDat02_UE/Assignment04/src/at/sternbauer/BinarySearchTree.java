package at.sternbauer;

public interface BinarySearchTree {

	/**
	 * PLEASE NOTE: THIS METHOD WAS INSERTED TO MAKE IT EASIER FOR THE TUTOR TO TEST
	 * HOWEVER, FOR THE REST VERSION 1 OF THE ASSIGNMENT WAS WORKED OUT!
	 * @return root of the binary search tree
	 * Returns the root of the binary search tree, or null if the tree is empty.
	 */
	public BinaryTreeNode getRoot();

	/**
	 * @param elem
	 * @return 
	 * @throws IllegalArgumentException
	 * Insert the element elem into the tree and return true if it was successful.
	 * Elements with the same key are not allowed, in this case false is returned.
 	 * Null-elements are not allowed, in this case an exception is thrown.
	 */
	public boolean insert(Integer key, String elem) throws IllegalArgumentException;
	
	/**
	 * @param key
	 * @return value
	 * @throws IllegalArgumentException
	 * Returns the value of the first found element with the given key, or null if element was not found.
	 */
	public String find(Integer key) throws IllegalArgumentException;
	
	/**
	 * @param key
	 * @return success
	 * @throws IllegalArgumentException
	 * Removes the first element with the given key, and returns true if element was found AND removed.
	 */
	public boolean remove(Integer key) throws IllegalArgumentException;
	
	/**
	 * @return
	 * Returns the number of elements stored in the binary tree.
	 */
	public int size();	
	
	/**
	 * @return
	 * Returns an array-representation of the stored elements (Postorder traversal).
	 */
	public Object[] toArrayPostOrder();
	
	/**
	 * @return
	 * Returns an array-representation of the stored elements (Inorder traversal).
	 */
	public Object[] toArrayInOrder();
	
	/**
	 * @return
	 * Returns an array-representation of the stored elements (Preorder traversal).
	 */
	public Object[] toArrayPreOrder();
	
	/**
	 * @param key
	 * @return key of parent
	 * @throws IllegalArgumentException
	 * Search the parent node of the node with the given key,
     * and return its key (or null if not found).
	 */
	public Integer getParent(Integer key) throws IllegalArgumentException;
	
	/**
	 * @param key
	 * @return 
	 * @throws IllegalArgumentException
	 * Return true if the node with the given key is the root, otherwise return false.
	 */
	public boolean isRoot(Integer key) throws IllegalArgumentException;
	
	/**
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * Return true if the node with the given key is an internal node, otherwise return false.
	 */
	public boolean isInternal(Integer key) throws IllegalArgumentException;
	
	/**
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 * Return true if the node with the given key is an internal node, otherwise return false.
	 */
	public boolean isExternal(Integer key) throws IllegalArgumentException;
}
