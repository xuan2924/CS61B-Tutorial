import static org.junit.Assert.*;

import org.junit.Test;

public class TestArrayDequeGold {
    /**
     * @source StudentArrayDequeLauncher.java
     */
    @Test
    public void test1() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad1 = new ArrayDequeSolution<>();

        String log = ""; //记录操作过程
        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.25) {
                sad1.addFirst(i);
                ad1.addFirst(i);
                log += "addFirst(" + i + ")\n";
            } else if (numberBetweenZeroAndOne >= 0.25 && numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                ad1.addLast(i);
                log += "addLast(" + i + ")\n";
            } else if (numberBetweenZeroAndOne >= 0.5 && numberBetweenZeroAndOne <= 0.75) {
                log += "removeFirst()\n";
                assertEquals(log, sad1.removeFirst(), ad1.removeFirst());
            } else {
                log += "removeLast()\n";
                assertEquals(log, sad1.removeLast(), ad1.removeLast());
            }
        }
    }


//    private boolean check(StudentArrayDeque<Integer>sad, ArrayDequeSolution<Integer>ad){
//        if(sad.size() != ad.size()){
//            return false;
//        }
//        int length = ad.size();
//        for(int i = 0; i < length; i++){
//            if(!ad.get(i).equals(sad.get(i))){
//                return false;
//            }
//        }
//        return true;
//    }
}
