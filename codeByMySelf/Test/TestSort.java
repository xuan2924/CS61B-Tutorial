package Test;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestSort {

    /** 测试 Sort.sort 方法 */
    @Test
    public void testSort() {
        String[] input = {"i", "have", "an", "egg"};
        String[] expected = {"an", "egg", "have", "i"};

        Sort.sort(input);

        // 使用 assertArrayEquals 检查数组内容是否完全一致
        assertArrayEquals(expected, input);
    }

    /** 测试 Sort.findSmallest 方法 */
    @Test
    public void testFindSmallest() {
        String[] input = {"i", "have", "an", "egg"};
        int expected = 2; // "an" 是最小的，索引为 2

        int actual = Sort.findSmallest(input, 0);
        assertEquals(expected, actual);

        int expected2 = 3; // 从索引 3 开始看，最小的是 "egg"
        int actual2 = Sort.findSmallest(input, 3);
        assertEquals(expected2, actual2);
    }

    /** 测试 Sort.swap 方法 */
    @Test
    public void testSwap() {
        String[] input = {"i", "have", "an", "egg"};
        String[] expected = {"an", "have", "i", "egg"};

        Sort.swap(input, 0, 2);

        assertArrayEquals(expected, input);
    }
}