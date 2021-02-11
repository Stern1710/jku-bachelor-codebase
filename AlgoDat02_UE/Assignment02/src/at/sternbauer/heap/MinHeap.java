package at.sternbauer.heap;

import at.sternbauer.queue.MyPriorityQueue;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

public class MinHeap<T> implements MyPriorityQueue<Long>  {
    private Long[] array;
    private int size = 0;

    public MinHeap(int initCapacity) {
        array = new Long[initCapacity];
    }

    @Override
    public boolean isEmpty() {
        return size <= 0 ? true:false;
    }

    @Override
    public int size() { return size; }

    @Override
    public void insert(Long elem) throws IllegalArgumentException {
        if (elem == null) throw new IllegalArgumentException("Inserted item cannot be null -> Rejected");
        if (size >= array.length - 1) {
            //Array is full, new array with double the length and fill with the existing values
            Long[] newArray = new Long[array.length*2];
            java.lang.System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        size++;

        array[size] = new Long(elem);
        upHeap(size);
    }

    @Override
    public Long removeMin() throws NoSuchElementException {
        if (array.length < 2 || size < 1) throw new NoSuchElementException("Array is too short or not enough elements to have a min item to remove");

        Long retVal = array[1];
        array[1] = array[size];
        array[size] = null;
        size--;

        downHeap(1);
        return retVal;
    }

    @Override
    public Long min() throws NoSuchElementException {
        if (array.length < 2 || size < 1) throw new NoSuchElementException("Array is too short or not enough elements to have a min item");

        return array[1];
    }

    @Override
    public Object[] toArray() {
        Long[] retAr = new Long[size];

        //Hacks to make Stefans unit tests work so that the array
        //is neat and free of null objects on the outside
        int i=0;
        for (Long l : array) {
            if (l != null && !l.equals(null)) {
                retAr[i] = l;
                i++;
            }
        }

        return retAr;
    }

    //Suggested private methods
    private void upHeap(int index) {
        if (index > 1) {
            //Not already the first (= min) element
            int pari = parent(index);
            if (array[pari].compareTo(array[index]) > 0) {
                swap(pari, index);
                upHeap(pari);
            }
        }
    }

    private void downHeap(int index) {
        int cil = leftChild(index);
        int cir = rightChild(index);

        if (cil <= size) {
            //Left child exists
            if (cir <= size) {
                //Both children exist --> Compare current value with smaller child
                //If value is bigger than smaller child -> Swap and get down Lower
                //Else value already in the right position
                if (array[cil].compareTo(array[cir]) < 0) {
                    swap(cil, index);
                    downHeap(cil);
                } else {
                    swap(cir, index);
                    downHeap(cir);
                }
            } else {
                //Only left child node exists --> Compare with this one and swap if needed
                if (array[cil].compareTo(array[index]) < 0) {
                    swap(cil, index);
                    downHeap(cil);
                }
            }
        } //If no children exist, item is already at the perfect place
    }

    private int parent(int index) {
        if (index%2 == 0) {
            return index/2;
        }
        return (index-1)/2;

    }

    private int leftChild(int index) {
        return 2*index;
    }

    private int rightChild(int index) {
        return 2*index + 1;
    }

    private void swap(int index1, int index2) {
        Long temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}
