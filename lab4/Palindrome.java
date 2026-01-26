public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        //使用Deque<Character>接口，如果以后LinkedListDeque性能不好
        //只用该wordToDeque的一行代码，isPalindrome的逻辑一字不改
        Deque<Character> list = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            list.addLast(word.charAt(i));
        }
        return list;
    }

    //回文数的递归定义：首尾字符相等，剩余部分也是回文，就是回文
    //不判断奇偶情况
    public boolean isPalindrome(String word) {
        return isPalindromeHelper(wordToDeque(word));
    }

    private boolean isPalindromeHelper(Deque<Character> d) {
        if (d.size() <= 1) {
            return true;
        }
        if (d.removeLast() != d.removeFirst()) {
            return false;
        }
        return isPalindromeHelper(d);
    }

    private boolean isPalindromeHelper(Deque<Character> d, CharacterComparator cc) {
        if (d.size() <= 1) {
            return true;
        }
        if (cc.equalChars(d.removeLast(), d.removeFirst())) {
            return false;
        }
        return isPalindromeHelper(d);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        return isPalindromeHelper(wordToDeque(word), cc);
    }

}

