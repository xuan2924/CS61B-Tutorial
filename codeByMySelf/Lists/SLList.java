/**
 * SLList 是一个单向链表，对底层的 IntNode 进行了封装。
 * 采用了哨兵节点 (Sentinel Node) 来简化边界情况处理。
 */
public class SLList {

    /**
     * 嵌套类 IntNode。
     * 使用 static 是因为它不需要访问外部 SLList 的实例变量，更节省内存。
     */
    private static class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    /* 哨兵节点：始终存在，sentinel.next 指向真正的第一个节点 */
    private IntNode sentinel;
    /* 缓存链表的大小，使 size() 方法的时间复杂度为 O(1) */
    private int size;

    /** 创建一个空的 SLList */
    public SLList() {
        sentinel = new IntNode(61, null); // 哨兵的值可以是任意的
        size = 0;
    }

    /** 创建一个包含单个元素 x 的 SLList */
    public SLList(int x) {
        sentinel = new IntNode(61, null);
        sentinel.next = new IntNode(x, null);
        size = 1;
    }

    /** 在链表头部添加元素 x */
    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size += 1;
    }

    /** 返回链表头部的第一个元素的值 */
    public int getFirst() {
        return sentinel.next.item;
    }

    /** 在链表末尾添加元素 x */
    public void addLast(int x) {
        size += 1;
        IntNode p = sentinel;

        /* 移动 p 直到它指向最后一个节点 */
        while (p.next != null) {
            p = p.next;
        }

        p.next = new IntNode(x, null);
    }

    /** 返回链表的大小 (已优化的 O(1) 实现) */
    public int size() {
        return size;
    }

    /**
     * 辅助方法：删除第一个元素并返回其值
     * (这是课程练习中常见的补充方法)
     */
    public int removeFirst() {
        if (sentinel.next == null) {
            return -1; // 或者抛出异常
        }
        int val = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return val;
    }

    public static void main(String[] args) {
        /* 测试代码 */
        SLList L = new SLList();
        L.addLast(20);
        L.addFirst(10);
        L.addLast(30);
        System.out.println("Size: " + L.size());      // 应输出 3
        System.out.println("First: " + L.getFirst()); // 应输出 10
    }
}