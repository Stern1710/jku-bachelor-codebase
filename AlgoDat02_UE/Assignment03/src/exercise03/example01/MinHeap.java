package exercise03.example01;

import java.util.NoSuchElementException;

public class MinHeap<T> implements MyPriorityQueue<Long> {
    private Long[] array;
    private int size = 0;

    public MinHeap(int initCapacity) {
        array = new Long[initCapacity];
    }

    // create a MinHeap using bottom-up construction in-place
    public MinHeap(Long list[]) throws IllegalArgumentException {
        if (list == null) throw new IllegalArgumentException("List must not be null");
        for (Long l : list) {
            if (l == null) throw new IllegalArgumentException("Items in list must not be null");
        }

        array = list;
        size = array.length;
        for (int startI = ((size) / 2) - 1; startI >= 0; startI--) {
            downHeap(startI);
        }
    }

    // search of elements
    public boolean contains(Long val) throws IllegalArgumentException {
        if (val == null) throw new IllegalArgumentException("Passed value must not be null");
        //Start search in item with index 0 and then proceed from there on
        return containsCheck(0, val);
    }

    // sorting with HeapSort
    public static void sort(Long list[]) throws IllegalArgumentException {
        if (list == null) throw new IllegalArgumentException("Passed table must not be null");
        for (Long l : list) {
            if (l == null) throw new IllegalArgumentException("Content of table must not be null");
        }

        //Build Heap
        MinHeap heap = new MinHeap(list);
        //In-Place replacement on table
        while (heap.size() > 0) {
            heap.array[heap.size()-1] = heap.removeMin();
        }
    }

    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void insert(Long elem) throws IllegalArgumentException {
        if (elem == null) throw new IllegalArgumentException("Inserted item cannot be null -> Rejected");
        if (size >= array.length - 1) {
            //Array is full, new array with double the length and fill with the existing values
            Long[] newArray = new Long[array.length*2];
            java.lang.System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size] = new Long(elem);
        upHeap(size);

        size++;
    }

    @Override
    public Long removeMin() throws NoSuchElementException {
        if (array.length < 1 || size < 1) throw new NoSuchElementException("Array is too short or not enough elements to have a min item to remove");

        Long retVal = array[0];
        size--;
        array[0] = array[size];
        array[size] = null;
        downHeap(0);
        return retVal;
    }

    @Override
    public Long min() throws NoSuchElementException {
        if (array.length < 1 || size < 1) throw new NoSuchElementException("Array is too short or not enough elements to have a min item");

        return array[0];
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

    private boolean containsCheck (int index, Long value) {
        //Check if index is valuable and if index value is higher element
        //As this is a MinHeap, no lower element can be found after index, therefore false
        if (index >= size || array[index].compareTo(value) > 0) return false;
        //Check if current element equals the value or one of the children
        if (array[index].compareTo(value) == 0
            || containsCheck(leftChild(index), value)
            || containsCheck(rightChild(index), value)) {
            return true;
        }
        //DEFAULT: Nothing was found in item itself or kids (no kids or not in kids branch) -> false
        return false;
    }

    private void upHeap(int index) {
        if (index > 0) {
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

        if (cil < size) {
            //Left child exists
            if (cir < size) {
                //Both children exist --> Compare current value with smaller child
                //If value is bigger than smaller child -> Swap and get down Lower
                //Else value already in the right position
                if (array[cil].compareTo(array[cir]) < 0) {
                    if (array[cil].compareTo(array[index]) < 0) {
                        swap(cil, index);
                        downHeap(cil);
                    }
                } else {
                    if (array[cir].compareTo(array[index]) < 0) {
                        swap(cir, index);
                        downHeap(cir);
                    }
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
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2*index + 1;
    }

    private int rightChild(int index) {
        return 2*index + 2;
    }

    private void swap(int index1, int index2) {
        Long temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public Long get(int i) {
        return array[i];
    }
}
