public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int front;
    private int rear;

    //改进：使用static final定义常量
    //常量大写
    private static final int INITIAL_CAPACITY = 8;
    private static final int MIN_CAPACITY_TO_SHRINK = 16;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
        rear = 0;
        front = INITIAL_CAPACITY - 1;
    }

    public void addFirst(T item) {
        //null的处理
        if (item == null) {
            //你传给我的参数不对
            throw new IllegalArgumentException("item cannot be null");
        }
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[front] = item;
        front = (front - 1 + items.length) % items.length;
        size += 1;
    }

    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
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

    public T removeFirst() {
        if (size == 0) {
            return null; /**/
        }

        //T res = items[(front + 1) % items.length];
        //items[front] = null;
        //防止对象游离
        //items[(front + 1) % items.length] = null;
        //front = (front + 1) % items.length;
        //size -= 1;
        int firstIndex = ((front + 1) % items.length);
        T res = items[firstIndex];
        items[firstIndex] = null;
        front = firstIndex;

        //缩容检查，在size减少后进行
        if (size < items.length / 4 && items.length >= sizeLimit) {
            resize(items.length / 2);
        }
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        //T res = items[(rear - 1 + items.length) % items.length];
        //items[rear] = null;
        //items[(rear - 1 + items.length) % items.length] = null;
        //rear = (rear - 1 + items.length) % items.length;
        int lastIndex = (rear - 1 + items.length) % items.length;
        T res = items[lastIndex];
        items[lastIndex] = null;
        rear = lastIndex;
        size -= 1;

        if (size < items.length / 4 && items.length >= sizeLimit) {
            resize(items.length / 2);
        }
        return res;
    }

    public T get(int index) {
        //return items[index];
        //真正的索引：(front + 1 + index) % items.length
        if (index < 0 || size <= index) {
            throw new IndexOutOfBoundsException("index out of range");
        }
        return items[(front + 1 + index) % items.length];
    }
}
