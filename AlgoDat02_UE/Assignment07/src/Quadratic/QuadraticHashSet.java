package Quadratic;

public class QuadraticHashSet extends AbstractHashSet implements MyHashSet {

	private OpenHashNode[] array;
	private int size;

	public QuadraticHashSet(int capacity) {
		if (capacity < 1) {
			//Assume a default value as a array smaller 1 is a bit pointless
			capacity = 10;
		}
		array = new OpenHashNode[capacity];
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean insert(Integer key, String data) throws IllegalArgumentException {
		if (key == null || data == null) {
			throw new IllegalArgumentException("Key and Data must not be null");
		}

		OpenHashNode newNode = new OpenHashNode(key, data);
		int indexer = getHashCode(key, array.length);
		int initIndexer = indexer;
		int i=0;

		//Found a duplicate
		if (array[indexer] != null && newNode.equals(array[indexer])) {
			return false;
		}

		while (checkOccupied(indexer)) {
			//Found a duplicate
			if (newNode.key.equals(array[indexer].key)) {
				return false;
			}
			i++;
			indexer = getFollowingIndex(i, initIndexer);
			if (i == 2*array.length) { //Table is full -> gone full circle
				return false;
			}
		}
		array[indexer] = newNode;
		size++;
		return true;
	}

	@Override
	public boolean contains(Integer key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null");
		}

		int indexer = getHashCode(key, array.length);
		int initIndexer = indexer;
		int i=0;

		if (array[indexer] == null) {
			return false; //Found nothing at position
		}

		while (array[indexer] != null) {
			if (key.equals(array[indexer].key)) {
				return true; //Found the key
			}
			i++;
			indexer = getFollowingIndex(i, initIndexer);
			if (i == 2 * array.length) {
				return false; //Gone full circle and did not find anything
			}
		}
		return false;
	}

	@Override
	public boolean remove(Integer key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null");
		}

		int indexer = getHashCode(key, array.length);
		int initIndexer = indexer;
		int i=0;

		if (array[indexer] == null) {
			return false; //Nothing at index to remove
		}
		while (array[indexer] != null) {
			if (key.equals(array[indexer].key) && !array[indexer].removed) {
				array[indexer].removed = true;
				size--;
				return true;
			}
			i++;
			indexer = getFollowingIndex(i, initIndexer);
			if (i == 2 * array.length) {
				return false; //tried everything and found nothing to remove
			}
		}
		return false;
	}

	@Override
	public void clear() {
		size = 0;
		for (int i=0; i < array.length; i++) {
			array[i]= null;
		}
	}

	@Override
	public OpenHashNode[] getHashTable() {
		return array;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i=0; i < array.length; i++) {
			sb.append(i + " {");

			OpenHashNode node = array[i];
			if (node != null && !node.removed) {
				sb.append(node.data);
			}

			sb.append("}");
			if (i+1 < array.length) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	private boolean checkOccupied (int index) {
		return array[index] != null && !array[index].removed;
	}

	private int getFollowingIndex (int counter, int initValue) {
		int halfSquare = (counter/2) * (counter/2);
		int newIndex = (counter%2 == 0) ? initValue + halfSquare : initValue - halfSquare;
		newIndex = newIndex % array.length;

		if (newIndex < 0) {
			newIndex += array.length;
		}

		return newIndex;
	}
}
