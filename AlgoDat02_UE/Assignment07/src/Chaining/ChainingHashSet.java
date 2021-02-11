package Chaining;

public class ChainingHashSet extends AbstractHashSet implements MyHashSet {

	private ChainingHashNode[] array;
	private int size;

	public ChainingHashSet(int capacity) {
		if (capacity < 1) {
			//Assume a default value as a array smaller 1 is a bit pointless
			capacity = 10;
		}
		array = new ChainingHashNode[capacity];
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
		//Get index of insert as this is the calculated hash value
		ChainingHashNode newNode = new ChainingHashNode(key, data);
		int index = getHashCode(key, array.length);

		//If null at index, perfect, just insert
		if (array[index] == null) {
			array[index] = newNode;
			size++;
			return  true;
		}

		//Else go through the nodes at the index and insert unless
		//another node with the same key is already in there
		ChainingHashNode cur = array[index];

		while (cur != null) {
			if (key.equals(cur.key)) {
				return false;
			}
			if (cur.next == null) {
				cur.next = newNode;
				size++;
				return true;
			}

			cur = cur.next;
		}
		return true;
	}

	@Override
	public boolean contains(Integer key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null");
		}

		ChainingHashNode cur = array[getHashCode(key, array.length)];
		while (cur != null) {
			if (key.equals(cur.key)) {
				return true;
			}
			cur = cur.next;
		}
		return false;
	}

	@Override
	public boolean remove(Integer key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null");
		}

		int index = getHashCode(key, array.length);
		ChainingHashNode cur = array[index];

		if (cur == null) {
			return false;
		}

		if (key.equals(cur.key)) {
			array[index] = cur.next;
			size--;
			return true;
		}
		while (cur.next != null) {
			if (key.equals(cur.next.key)) {
				cur.next = cur.next.next;
				size--;
				return true;
			}
			cur = cur.next;
		}

		return false;
	}

	@Override
	public void clear() {
		size = 0;
		for (int i=0; i < array.length; i++) {
			array[i] = null;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i=0; i < array.length; i++) {
			ChainingHashNode cur = array[i];
			sb.append(i + " {");
			while (cur != null) {
				sb.append(cur.data);
				if (cur.next != null) {
					sb.append(", ");
				}
				cur = cur.next;
			}
			sb.append("}");
			if (i+1 < array.length) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	@Override
	public ChainingHashNode[] getHashTable() {
		return array;
	}

}
