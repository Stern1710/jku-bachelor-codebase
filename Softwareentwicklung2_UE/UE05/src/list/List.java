package list;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface List<E> extends Iterable<E> {

	/**
	 * Returns the number of elements of this list
	 * @return the number of elements
	 */
	public int size();

	/**
	 * Adds an element to this list
	 * @param elem the element to be added
	 */
	public void add(E elem);

	/** Tests if the object is contained in this list
	 * @param obj the object to be tested
	 * @return true if obj is contained, false otherwise
	 */
	public boolean contains(Object obj);

	public boolean addAll(Iterable<? extends E> elems);

	public boolean containsAll(Iterable<?> c);

	// HOF -------------------------------------------------------------------

	// TODO: The following methods have to be implemented in LinkedList

	public <R> List<R> map(Function<? super E, ? extends R> mapper);

	public List<E> filter(Predicate<? super E> predicate);

	public <R> R reduce(R identity, BiFunction<R, ? super E, R> accumulator);

	public List<E> distinct();
}
