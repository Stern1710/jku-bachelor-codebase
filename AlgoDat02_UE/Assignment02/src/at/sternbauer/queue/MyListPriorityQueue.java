package at.sternbauer.queue;

import at.sternbauer.list.MyLinkedList;

import java.util.NoSuchElementException;

public class MyListPriorityQueue <T> implements MyPriorityQueue<Long> {
    private final MyLinkedList<T> list;

    public MyListPriorityQueue() {
        list = new MyLinkedList<>();
    }

    @Override
    public boolean isEmpty() {
        return list.size() <= 0;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void insert(Long elem) throws IllegalArgumentException {
        list.addSorted(elem);
    }

    @Override
    public Long removeMin() throws NoSuchElementException {
        Long min = list.removeFirst();
        if (min == null) {
            throw new NoSuchElementException("Queue ist empty - No min element to get");
        }
        return min;
    }

    @Override
    public Long min() throws NoSuchElementException {
        Long min = list.getFirst();
        if (min == null) {
            throw new NoSuchElementException("Queue ist empty - No min element to get");
        }
        return min;
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }
}