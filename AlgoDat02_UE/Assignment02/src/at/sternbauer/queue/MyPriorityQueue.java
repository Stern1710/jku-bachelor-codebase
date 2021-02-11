package at.sternbauer.queue;

import java.util.NoSuchElementException;

public interface MyPriorityQueue <T> {
    public boolean isEmpty(); //Returns true if the PQ is empty; false otherwise
    public int size(); //Returns the current size of the PQ
    public void insert(Long elem) //Inserts a new element into the PQ
            throws IllegalArgumentException;
    public Long removeMin() //Removes the min element from the PQ and returns it
            throws NoSuchElementException;
    public Long min() //Returns the min element from the PQ
            throws NoSuchElementException;
    public Object[] toArray(); //Returns an array representation of the PQ
}