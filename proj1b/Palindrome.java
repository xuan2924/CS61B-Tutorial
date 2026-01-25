public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> list = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            list.addLast(word.charAt(i));
        }
        return list;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> order = wordToDeque(word);
        if (order.size() % 2 == 0) {
            while (!order.isEmpty()) {
                if (order.removeFirst() != order.removeLast()) {
                    return false;
                }
            }
            return true;
        } else {
            while (order.size() != 1) {
                if (order.removeFirst() != order.removeLast()) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> order = wordToDeque(word);
        if (order.size() % 2 == 0) {
            while (!order.isEmpty()) {
                if (!cc.equalChars(order.removeFirst(), order.removeLast())) {
                    return false;
                }
            }
            return true;
        } else {
            while (order.size() != 1) {
                if (!cc.equalChars(order.removeFirst(), order.removeLast())) {
                    return false;
                }
            }
            return true;
        }
    }

}

