package Quadratic;

abstract class AbstractHashSet{
	
	public final int getHashCode(Integer key, Integer hashTableLength) {
		return ((key.hashCode() % hashTableLength) + hashTableLength) % hashTableLength;
	};
}
