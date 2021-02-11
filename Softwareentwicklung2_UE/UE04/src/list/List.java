package list;


public interface List<T> extends Iterable<T> {
	void add(T value);

	T get(int index);

	T remove(int index);
	
	boolean remove(Object obj);

	T removeLast();
	
	boolean contains(Object obj);

	int indexOf(Object obj);

	int size();
}
