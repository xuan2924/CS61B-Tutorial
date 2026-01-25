public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int front;
    private int rear;
    private final int initCapacity = 8;
    private final int sizeLimit = 16;

    public ArrayDeque() {
        items = (T[]) new Object[initCapacity];
        size = 0;
        rear = 0;
        front = initCapacity - 1;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[front] = item;
        front = (front - 1 + items.length) % items.length;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[rear] = item;
        rear = (rear + 1) % items.length;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        //System.arraycopy(items, 0, newItems, 0, items.length);
        //不可以简单的复制
        //注意缩容的时候用的是size，如果用items.length会多循环
        int current = (front + 1) % items.length;

        for (int i = 0; i < size; i++) {
            newItems[i] = items[current];
            //注意current是在旧的数组循环
            current = (current + 1) % items.length;
        }
        items = newItems;
        front = capacity - 1; //新数组的front指向末尾（数据从0开始数）
        rear = size;
    }

    @Override
    public void printDeque() {
        /*
        for (T t : items) {
            //不是按照物理顺序打印
            //按照逻辑顺序
            //System.out.print(t + " ");
        }
         */
        int current = (front + 1) % items.length;
        for (int i = 0; i < size; i++) {
            System.out.print(items[current] + " ");
            current = (current + 1) % items.length;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null; /**/
        }
        if (size < items.length / 4 && items.length >= sizeLimit) {
            resize(items.length / 2);
        }
        T res = items[(front + 1) % items.length];
        //items[front] = null;
        items[(front + 1) % items.length] = null;
        front = (front + 1) % items.length;
        size -= 1;
        return res;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size < items.length / 4 && items.length >= sizeLimit) {
            resize(items.length / 2);
        }
        T res = items[(rear - 1 + items.length) % items.length];
        //items[rear] = null;
        items[(rear - 1 + items.length) % items.length] = null;
        rear = (rear - 1 + items.length) % items.length;
        size -= 1;
        return res;
    }

    @Override
    public T get(int index) {
        //return items[index];
        //真正的索引：(front + 1 + index) % items.length
        if (index < 0 || size <= index) {
            return null;
        }
        return items[(front + 1 + index) % items.length];
    }
}
