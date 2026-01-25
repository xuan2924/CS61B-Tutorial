import org.junit.Test;

import static org.junit.Assert.*;

public class MyTestArrayDeque {
    @Test
    public void testN8RemoveFirst() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        for (int i = 0; i < 10; i++) {
            ad.addLast(i);
        }

        assertEquals(10, ad.size());

        Integer first = ad.removeFirst();

        assertNotEquals("当 N=10 时，removeFirst 不应返回 null", first);
        assertEquals(0, (int) first);
    }

    @Test
    public void testN8RemoveLast() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        for (int i = 0; i < 8; i++) {
            ad.addLast(i);
        }

        //assertEquals(10, ad.size());

        Integer last = ad.removeLast();

        //assertNotEquals("当 N=10 时，removeFirst 不应返回 null", last);
        //assertEquals(9, (int) last);
        assertNotNull(last);
    }
}
