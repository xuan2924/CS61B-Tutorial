package hw3.hash;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* Finished: Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */

    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        // Your code here.

        for (int i = 0; i < 100; i++) {
            // 1. 每次循环都创建一个新的“序列”
            ArrayList<Integer> params = new ArrayList<>();

            // 2. 加入一些不同的元素（比如 i），让这些对象在 equals 上是不同的
            params.add(i);

            // 3. 加入“致命”的固定后缀
            // 只要最后四个数字在所有对象中都一样，它们的 hashCode 就会撞车
            for (int j = 0; j < 4; j++) {
                params.add(255);
            }

            // 4. 使用这个 params 序列创建一个 ComplexOomage
            // 并存入 deadlyList
            deadlyList.add(new ComplexOomage(params));
        }

        // 5. 此时 deadlyList 里的 100 个对象，虽然内容不同，但 hashCode 全部相同
        // 它们会全部掉进同一个桶里，导致 haveNiceHashCodeSpread 返回 false
        // 从而让测试能够检测出这个哈希算法的无能
        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /**
     * Calls tests for SimpleOomage.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
