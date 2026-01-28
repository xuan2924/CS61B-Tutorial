package synthesizer; // Finished: Make sure to make this class a part of the synthesizer package
// package <package name>;

//import org.junit.Test;
import org.junit.Assert.*;
//import synthesizer.AbstractBoundedQueue;

import java.util.Iterator;

//Finished: Make sure to make this class and all of its methods public
//Finished: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // Finished: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        this.capacity = capacity;
        this.fillCount = 0;
        first = 0;
        last = 0;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        // Finished: Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = (last + 1) % capacity;
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        // Finished: Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        /**
         int firstIndex = (first - 1 + capacity) % capacity();
         T res = rb[firstIndex];
         fillCount -= 1;
         first = firstIndex;
         return res;
         */
        T res = rb[first];
        fillCount -= 1;
        first = (first + 1) % capacity;
        return res;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        // Finished: Return the first item. None of your instance variables should change.
        //bug : int firstIndex = (first - 1 + capacity) % capacity;
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T res = rb[first];
        return res;
    }

    // Finished: When you get to part 5, implement the needed code to support iteration.
    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    public class ArrayRingBufferIterator implements Iterator<T> {
        private int pos;
        private int count;

        public ArrayRingBufferIterator() {
            pos = first;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            //bug: return count < capacity;
            return count < fillCount;
        }

        @Override
        public T next() {
            T res = rb[pos];
            pos = (pos + 1) % capacity;
            count += 1;
            return res;
        }
    }

}
