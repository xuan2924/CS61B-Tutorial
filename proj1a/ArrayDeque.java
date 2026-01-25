public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int front;
    private int rear;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = rear = 0;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[front] = item;
        front = (front + 1) % items.length;
        size += 1;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[rear] = item;
        rear = (rear + 1) % items.length;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return this.size;
    }

    public void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        System.arraycopy(items, 0, newItems,0, items.length);
        items = newItems;
    }

    public void printDeque() {
        for (T t : items) {
            System.out.print(t + " ");
        }
    }

    public T removeFirst() {
        if (size < items.length / 4) {
            resize(items.length / 2);
        }
        T res = items[front];
        items[front] = null;
        front = (front - 1) % items.length;
        size -= 1;
        return res;
    }

    public T removeLast() {
        if (size < items.length / 4) {
            resize(items.length / 2);
        }
        T res = items[rear];
        items[rear] = null;
        rear = (rear - 1) % items.length;
        size -= 1;
        return res;
    }

    public T get(int index) {
        if (size < index) {
            return null;
        }
        return items[index];
    }
}