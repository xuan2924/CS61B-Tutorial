import org.junit.Test;

import static org.junit.Assert.*;

public class MylinkedListDequeTest {
    @Test
    public void testReGet() {
        LinkedListDeque<Integer> ld = new LinkedListDeque<>();

        for (int i = 0; i < 10; i++) {
            ld.addLast(i);
        }

        Integer exp = 5;
        assertEquals("re: ", exp, ld.getRecursive(5));
        assertEquals("In: ", exp, ld.get(5));

        exp = 0;
        Integer first = ld.removeFirst();
        assertEquals("first", exp, first);
        exp = 9;
        Integer last = ld.removeLast();
        assertEquals("last", exp, last);
    }
}
