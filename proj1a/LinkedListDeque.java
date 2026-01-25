public class LinkedListDeque<T> {
    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    //创建空队列
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    //递归实现get
    public T getRecursive(int index) {
        return getNextNode(index, 0, sentinel.next).item;
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

    //迭代实现
    public T get(int index) {
        int count = 0;
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            if (count == index) {
                return p.item;
            }
            count++;
        }
        return null;
    }


    public int size() {
        return this.size;
    }


    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node firsttNode = sentinel.next;
        sentinel.next = firsttNode.next;
        firsttNode.next.prev = sentinel;
        T res = firsttNode.item;
        firsttNode.next = null;
        firsttNode.prev = null;
        size -= 1;
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        T res = lastNode.item;
        lastNode.next = null;
        lastNode.prev = null;
        size -= 1;
        return res;
    }
}
