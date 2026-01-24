import static org.junit.Assert.*;

import org.junit.Test;

public class IntListTest {

    /**
     * Example test that verifies correctness of the IntList.of static
     * method. The main point of this is to convince you that
     * assertEquals knows how to handle IntLists just fine.
     */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = intlist.of(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    @Test
    public void testdSquareList() {
        IntList L = intlist.of(1, 2, 3);
        intlist.dSquareList(L);
        assertEquals(intlist.of(1, 4, 9), L);
    }

    /**
     * Do not use the new keyword in your tests. You can create
     * lists using the handy IntList.of method.
     * <p>
     * Make sure to include test cases involving lists of various sizes
     * on both sides of the operation. That includes the empty of, which
     * can be instantiated, for example, with
     * IntList empty = IntList.of().
     * <p>
     * Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     * Anything can happen to A.
     */

    @Test
    public void testSquareListRecursive() {
        IntList L = intlist.of(1, 2, 3);
        IntList res = intlist.squareListRecursive(L);
        assertEquals(intlist.of(1, 2, 3), L);
        assertEquals(intlist.of(1, 4, 9), res);
    }

    @Test
    public void testDcatenate() {
        IntList A = intlist.of(1, 2, 3);
        IntList B = intlist.of(4, 5, 6);
        IntList exp = intlist.of(1, 2, 3, 4, 5, 6);
        assertEquals(exp, intlist.dcatenate(A, B));
        assertEquals(intlist.of(1, 2, 3, 4, 5, 6), A);
    }

    @Test
    public void testCatenate() {
        IntList A = intlist.of(1, 2, 3);
        IntList B = intlist.of(4, 5, 6);
        IntList exp = intlist.of(1, 2, 3, 4, 5, 6);
        assertEquals(exp, intlist.catenate(A, B));
        assertEquals(intlist.of(1, 2, 3), A);
    }

}
