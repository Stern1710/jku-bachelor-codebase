package list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T extends Comparable<? super T>> implements List<T> {
	//FIELDS
	private Node<T> head = null;
	private Node<T> last = null;
	private int n = 0;

	/**
	 * Adds new Item of list type T to the list in ascending order
	 */
	@Override
	public void add(T value) {
		Node<T> current = head;		
		
		if (head == null) {
			head = new Node<T>(value, null, null);
			last = head;
			n++;
			
			return;
		}
		
		for (T temp : this)	 {
			if (value.compareTo(temp) < 0) {
				//Current position is new the new node: current.prev < newNode < current
				
				//Creating new node with next is the current node and prev is current.prev
				Node<T> newNode = new Node<T>(value, current.prev, current);
				if (current.prev == null) {
					head = newNode;
				} else {
					current.prev.next = newNode;
				}
				current.prev = newNode;

				n++;
				return;
			}
			
			current = current.next;
		}
		
		//If no insert was done in the while, newNode is the tail and will be inserted
		Node<T> newNode = new Node<T>(value, last, null);
		last.next = newNode;
		last = newNode;
		n++;
	}

	/**
	 * Gets a single list item of type T by the index, throws Exception if index was out of bound
	 */
	@Override
	public T get(int index) throws IndexOutOfBoundsException {
		int counter = 0;
		
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("No item at given index was found, index might be too high or low\n");
		}
		
		for (T elem : this) {
			if (counter >= index) {
				return elem;
			}
			counter++;
		}
		
		return null;
	}

	/**
	 * Removes a list item by a given index, throws exception if index was out of bound
	 */
	@Override
	public T remove(int index) throws IndexOutOfBoundsException {
		Node<T> current = head;
		int counter = 0;
		
		
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("No removeable object at given index was found, index might be too high or low\n");
		}
		
		for(T elem : this) {
			if (counter >= index) {
				if (current.prev == null && current.next == null) {
					//This means current is head and tail, therefore only object in list
					head = null;
					last = null;
				} else if (current.prev == null) {
					//This means current is head, but not tail
					head = current.next;
					current.next.prev = current.prev;
				} else if (current.next == null) {
					//This means current is tail but not head
					last = current.prev;
					current.prev.next = current.next;
				} else {
					//This means current is a normal item in between two other
					current.prev.next = current.next;
					current.next.prev = current.prev;
				}
				
				n--;
				return elem;
			}
			
			counter++;
			current = current.next;
		}
		
		return null;
	}

	/**
	 * Removes given object from the list, returns true if successful, false if not
	 */
	@Override
	public boolean remove(Object obj) {
		for(T elem : this) {
			if (elem.equals(obj)) {
				T temp = remove(indexOf(elem));
				if (temp != null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public T removeLast() {
		return remove(size() - 1);
	}

	@Override
	public boolean contains(Object obj) {
		for (T elem : this) {
			if (elem.equals(obj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int indexOf(Object obj) {
		int i = 0;	
		for(T elem : this) {
			if (elem.equals(obj)) {
				return i;
			}			
			i++;
		}
		
		return -1;
	}

	@Override
	public int size() {
		return n;
	}
	
	/**
	 * Returns all items in list that, compare to given value, are below or equal
	 */
	public LinkedList<T> below(T value) {
		LinkedList<T> allBelow = new LinkedList<T>();
		
		for(T elem : this) {
			if (elem.compareTo(value) > 0) {
				break;
			}
			allBelow.add(elem);
		}
		
		return allBelow;
	}
	
	
	/**
	 * Returns all items in list that, compare to given value, are above or equal
	 */
	public LinkedList<T> above (T value) {
		LinkedList<T> allAbove = new LinkedList<T>();
		Iterator<T> iter = reverseIterator();
		
		while(iter.hasNext()) {
			T temp = iter.next();
				
			if (temp.compareTo(value) < 0) {
				break;
			}

			allAbove.add(temp);

		}

		return allAbove;
	}
	

	public Iterator<T> reverseIterator () {
		return new ReverseIterator();
	}

	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node<T> current;

			{
				current = head;
			}
			
			@Override
			public boolean hasNext() {
				return current != null;
			}
			
			@Override
			public T next() {
				if (hasNext()) {
					T value = current.value;
					current = current.next;
					return value;
				} else {
					throw new NoSuchElementException("No more items existing in the list");
				}
			}
		};
	}
	
	
	private static class Node<F> {
		//FIELDS
		private final F value;
		private Node<F> prev;
		private Node<F> next;
	
		//CONSTRUCTOR
		private Node(F elem, Node<F> prev, Node<F> next) {
			value = elem;
			this.prev = prev;
			this.next = next;
		}
	}
	
	
	private class ReverseIterator implements Iterator<T> {
		private Node<T> current = last;
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (current != null) {
				T value = current.value;
				current = current.prev;
				return value;
			} else  {
				throw new NoSuchElementException("No more elements found in list");
			}
		}
		
	}
}