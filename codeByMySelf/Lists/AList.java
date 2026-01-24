/**
 * AList 是一个基于数组的列表。
 * 核心特性：随机访问速度快 (O(1))，支持动态扩容。
 */
public class AList<Item> {
    private Item[] items;
    private int size;

    /**
     * 创建一个空的 AList
     */
    public AList() {
        // Java 不允许直接创建泛型数组 items = new Item[100];
        // 所以必须先创建 Object 数组再强转
        items = (Item[]) new Object[100];
        size = 0;
    }

    /**
     * 辅助方法：调整数组容量
     */
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        // 将旧数组的元素复制到新数组
        // 参数：源数组, 起始位置, 目标数组, 目标起始位置, 复制长度
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    /**
     * 在列表末尾添加元素 (均摊复杂度 O(1))
     */
    public void addLast(Item x) {
        // 如果数组满了，进行扩容
        if (size == items.length) {
            // 采用几何倍数增长（通常是 2 倍），这是性能的关键
            resize(size * 2);
        }
        items[size] = x;
        size += 1;
    }

    /**
     * 返回末尾元素 (O(1))
     */
    public Item getLast() {
        return items[size - 1];
    }

    /**
     * 获取指定索引的元素 (O(1))
     */
    public Item get(int i) {
        return items[i];
    }

    /**
     * 返回列表大小 (O(1))
     */
    public int size() {
        return size;
    }

    /**
     * 删除并返回末尾元素 (O(1))
     */
    public Item removeLast() {
        Item x = getLast();

        /* 避免“游离指针 (Loitering)”：
         * 在 Java 中，如果数组还持有对象的引用，垃圾回收器就不会回收它。
         * 对于泛型 AList，显式地将不再使用的位置置为 null 是一个好习惯。 */
        items[size - 1] = null;

        size -= 1;

        /* (可选) 进阶：如果利用率太低，可以缩容以节省空间 */
        if (size > 0 && size < items.length / 4) {
            resize(items.length / 2);
        }

        return x;
    }

    public static void main(String[] args) {
        AList<Integer> L = new AList<>();
        L.addLast(10);
        L.addLast(20);
        L.addLast(30);
        System.out.println("Item at 1: " + L.get(1)); // 20
        System.out.println("Size: " + L.size());      // 3
        L.removeLast();
        System.out.println("Last after remove: " + L.getLast()); // 20
    }
}