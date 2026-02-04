import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int size;

        public Node(K key, V val, int size) {
            this.key = key;
            this.value = val;
            this.size = size; // 这个节点的子树包含的节点总数
        }
    }

    public BSTMap() {

    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        /*
         * 左右子节点只能通过 root 或其他节点的 left/right 引用访问
         * 当 root = null 后，没有任何外部方式能访问这些节点
         * Java垃圾回收器会自动回收这些"孤立"的节点
         */
        root = null;
    }


    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return get(root, key) != null;
    }


    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node n, K key) {
        if (n == null) {
            return null;
        }
        if (n.key.equals(key)) {
            return n.value;
        } else if (n.key.compareTo(key) > 0) {
            return get(n.left, key);
        } else {
            return get(n.right, key);
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    //不直接修改父节点的指针，而是让递归函数返回“更新后的子树根节点”，父节点再负责把它接住。
    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value, 1); // 初始为1
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            x.left = put(x.left, key, value);
        } else if (cmp < 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value; //相等情况更新值
        }
        x.size = 1 + size(x.right) + size(x.left); //更新 size
        return x;
    }

    /* Print the BSTMap (key, vale) pairs in increasing of order of key. */
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node x) {
        if (x == null) {
            return;
        }
        if (x.left != null) {
            printInOrder(x.left);
        }
        System.out.println("K: " + x.key + " " + " V:" + x.value);
        if (x.right != null) {
            printInOrder(x.right);
        }
    }

    /* printInOrder() helper.
     * Return the Node with (k + 1)st smallest key.
     */
    private Node select(int k) {
        return select(root, k);
    }

    /* printInOrder() helper.
     * Return key of rank k.
     */
    private Node select(Node x, int k) {
        if (x == null) {
            return null;
        }
        int leftSize = size(x.left); //处理x.left为null的情况
        if (leftSize == k ) {
            return x;
        } else if (leftSize > k ) {
            return select(x.left, k);
        } else {
            return select(x.right, k - leftSize - 1); //减去已经跳过的节点
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        keySetHelper(root,set);
        return set;
    }

    private void keySetHelper(Node x, Set<K> set) {
        if (x == null) {
            return;
        }
        //中序遍历
        keySetHelper(x.left, set);
        set.add(x.key);
        keySetHelper(x.right, set);
    }


    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if(!containsKey(key)){
            return null;
        }
        V res = get(key);
        root = removeHelper(root,key);
        return res;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        V res = get(key);
        if(res == null || !res.equals(value)){
            return null;
        }
        root = removeHelper(root,key);
        return value;
    }

    /* Return the tree which has the node with specific key been removed. */
    //"删除指定key的节点后，这个位置应该接什么"
    private Node removeHelper(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = x.key.compareTo(key);
        if (cmp > 0) {
            // 关键：必须赋值回给 x.left，不能直接 return
            x.left= removeHelper(x.left, key);
        } else if (cmp < 0) {
            x.right=  removeHelper(x.right, key);
        } else {
            /*
             * 1.只有左节点
             * 2.只有右节点
             * 3.两个节点
             */
            if (x.left == null) {
                return x.right;
            }
            if (x.right == null) {
                return x.left;
            }

            //左右子树都存在，有右子树的最小值代替
            Node t = x;
            x = findMin(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = 1 + size(x.right) + size(x.left);
        return x;
    }

    private Node findMin(Node x) {
        if(x == null){
            return null;
        }
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }


    /* Return the tree which has the node with min key been removed. */
    private Node deleteMin(Node x) {
        //找到没有左端点的节点（即最小）
        if(x.left == null){
            return x.right;
        }
        x.left = deleteMin(x.left);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /* Iterator of the BSTMap. */
    public Iterator<K> iterator() {
        return new BSTIterator(root);
    }

    private class BSTIterator implements Iterator<K> {
        private Stack<Node> stack = new Stack<>();

        //初始化把左节点全部压入栈，中序遍历
        public BSTIterator(Node src) {
            while (src != null) {
                stack.push(src);
                src = src.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node curr = stack.pop();

            //处理右节点
            if (curr.right != null) {
                Node temp = curr.right;
                while (temp != null) {
                    stack.push(temp);
                    temp = temp.left;
                }
            }
            return curr.key;
        }
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> bstMap = new BSTMap<>();
        for (int i = 0; i < 10; i++) {
            bstMap.put("hi" + i, 1 + i);
        }
//        bstMap.printInOrder();
        Iterator<String> itr = bstMap.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}