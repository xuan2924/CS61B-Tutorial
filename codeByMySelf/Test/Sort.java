package Test;

public class Sort {
    /** 对字符串数组进行排序 */
    public static void sort(String[] x) {
        sort(x, 0);
    }

    /** 辅助方法：从 start 位置开始向后排序 (递归版或循环版) */
    private static void sort(String[] x, int start) {
        if (start == x.length) {
            return;
        }

        // 1. 找到最小元素的索引
        int smallestIndex = findSmallest(x, start);

        // 2. 交换最小元素到 start 位置
        swap(x, start, smallestIndex);

        // 3. 递归处理剩余部分
        sort(x, start + 1);
    }

    /** 找到从 start 开始的最小元素的索引 */
    public static int findSmallest(String[] x, int start) {
        int smallestIndex = start;
        for (int i = start; i < x.length; i += 1) {
            // compareTo 是 String 的比较方法，返回值 < 0 表示 x[i] 更小
            int cmp = x[i].compareTo(x[smallestIndex]);
            if (cmp < 0) {
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    /** 交换数组中两个索引位置的元素 */
    public static void swap(String[] x, int a, int b) {
        String temp = x[a];
        x[a] = x[b];
        x[b] = temp;
    }
}