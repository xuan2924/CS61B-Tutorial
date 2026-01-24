/**
 * DLList 是一个双向链表。
 * 使用了泛型 <T> 以支持存储任何类型的数据。
 * 采用了单循环哨兵 (Circular Sentinel) 拓扑结构。
 */
public class DLList<T> {

    /** 内部静态节点类 */
    private static class TNode<T> {
        public TNode<T> prev;
        public T item;
        public TNode<T> next;

        public TNode(TNode<T> p, T i, TNode<T> n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    /* 哨兵节点：sentinel.next 是 head，sentinel.prev 是 tail */
    private TNode<T> sentinel;
    private int size;

    /** 创建一个空的 DLList */
    public DLList() {
        // 哨兵节点的 item 可以是 null 或任何占位值
        sentinel = new TNode<>(null, null, null);
        // 循环指向：空的 list 中，哨兵的 prev 和 next 都指向自己
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /** 创建一个包含初始元素 x 的 DLList */
    public DLList(T x) {
        sentinel = new TNode<>(null, null, null);
        TNode<T> newNode = new TNode<>(sentinel, x, sentinel);
        sentinel.next = newNode;
        sentinel.prev = newNode;
        size = 1;
    }

    /** 在头部添加元素 (O(1)) */
    public void addFirst(T x) {
        TNode<T> newNode = new TNode<>(sentinel, x, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    /** 在末尾添加元素 (O(1)) */
    public void addLast(T x) {
        TNode<T> newNode = new TNode<>(sentinel.prev, x, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    /** 获取第一个元素 (O(1)) */
    public T getFirst() {
        return sentinel.next.item;
    }

    /** 获取最后一个元素 (O(1)) */
    public T getLast() {
        return sentinel.prev.item;
    }

    /** 删除并返回最后一个元素 (O(1)) */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T lastItem = sentinel.prev.item;

        // 关键：跳过当前的末尾节点
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;

        size -= 1;
        return lastItem;
    }

    /** 返回链表长度 (O(1)) */
    public int size() {
        return size;
    }

    /** 辅助：打印链表内容 */
    public void print() {
        TNode<T> p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DLList<String> list = new DLList<>();
        list.addLast("World");
        list.addFirst("Hello");
        list.addLast("CS61B");

        list.print(); // 输出: Hello World CS61B

        list.removeLast();
        list.print(); // 输出: Hello World

        System.out.println("Size: " + list.size()); // 输出: 2
    }
}