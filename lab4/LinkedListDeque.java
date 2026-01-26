public class LinkedListDeque<T> {
    //1.设为static，节省内存
    private static class Node<T> {
        private T item;
        private Node<T> prev;
        private Node<T> next;

        public Node(T i, Node<T> p, Node<T> n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    //2.设置成final
    private final Node<T> sentinel;
    private int size;

    //创建空队列
    public LinkedListDeque() {
        sentinel = new Node<>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /**
    //递归实现get
    public T getRecursive(int index) {
        //直接这样如果返回null，就会因为null.item报错
        //return getNextNode(index, 0, sentinel.next).item;
        if (index < 0) {
            return null;
        }
        Node res = getNextNode(index, 0, sentinel.next);
        return (res == null) ? null : res.item;
    }

    private Node getNextNode(int index, int count, Node curr) {
        if (curr == sentinel) {
            return null;
        }
        if (count == index) {
            return curr;
        }
        return getNextNode(index, count + 1, curr.next);
    }
     */
    //5.简化后的递归get
    public T getRecursive(int index){
        if(index < 0 || index >= size){
            return null;
        }
        return getNextNode(index, sentinel.next).item;
    }

    private Node<T> getNextNode(int index, Node<T> curr){
        if(index == 0){
            return curr;
        }
        return getNextNode(index - 1,curr.next);
    }

    //迭代实现
    //4.优化实现get，双向搜索
    public T get(int index) {
        //边界检查
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index out of range");
        }
        //int count = 0;
        Node<T> p;
        /**
         while (p.next != sentinel) {
         p = p.next;
         if (count == index) {
         return p.item;
         }
         count++;
         }
         */
        //使用for更优雅
        if (index < size / 2) {
            //从头开始找
            p = sentinel.next;
            for (int i = 0; i < index; i++) {
                p = p.next;
            }
        } else {
            p = sentinel.prev;
            for (int i = 0; i < size - 1 - index; i++) {
                p = p.prev;
            }
        }
        return null;
    }


    public int size() {
        return this.size;
    }

    //3.处理null的情况
    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        Node<T> newNode = new Node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    //3.处理null的情况
    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        Node<T> newNode = new Node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        Node<T> p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node<T> firstNode = sentinel.next;
        sentinel.next = firstNode.next;
        firstNode.next.prev = sentinel;
        T res = firstNode.item;
        //垃圾回收
        firstNode.next = null;
        firstNode.prev = null;
        firstNode.item = null;//彻底清除游离
        size -= 1;
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node<T> lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        T res = lastNode.item;
        lastNode.next = null;
        lastNode.prev = null;
        lastNode.item = null;
        size -= 1;
        return res;
    }
}
