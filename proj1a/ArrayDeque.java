public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int front;
    private int rear;
    private final int initCapacity = 8;

    public ArrayDeque() {
        items = (T[]) new Object[initCapacity];
        size = 0;
        rear = 0;
        front = initCapacity - 1;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[front] = item;
        front = (front - 1 + items.length) % items.length;
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

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        //System.arraycopy(items, 0, newItems, 0, items.length);
        //不可以简单的复制
        int current = (front + 1) % items.length;

        for (int i = 0; i < items.length; i++) {
            newItems[i] = items[current];
            current = (current + 1) % items.length;
        }
        items = newItems;
        front = capacity - 1;//新数组的front指向末尾（数据从0开始数）
        rear = size;
    }

    public void printDeque() {
        for (T t : items) {
            System.out.print(t + " ");
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;/**/
        }
        if (size < items.length / 4) {
            resize(items.length / 2);
        }
        T res = items[(front + 1) % items.length];
        items[front] = null;
        front = (front + 1) % items.length;
        size -= 1;
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size < items.length / 4) {
            resize(items.length / 2);
        }
        T res = items[(rear - 1) % items.length];
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
