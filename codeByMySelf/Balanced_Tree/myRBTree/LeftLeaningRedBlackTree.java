package Balanced_Tree.myRBTree;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Queue;

public class LeftLeaningRedBlackTree<Key extends Comparable<Key>, Value> {

    //颜色常量
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    //节点类
    private class Node {
        Node left, right;
        int size; // 子树中的节点个数,方便size()的实现
        boolean color;
        Key key;
        Value value;

        public Node(Key key, Value value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private Node root;

    //构造函数
    public LeftLeaningRedBlackTree() {
        this.root = null;
    }

    /*===============辅助方法===============*/

    //判断树是否是空
    public boolean isEmpty() {
        return root == null;
    }

    //返回树的大小
    public int size() {
        return size(root);
    }

    //返回节点的大小
    private int size(Node n) {
        return n == null ? 0 : n.size;
    }

    //判断节点否为红
    private boolean isRed(Node n) {
        if (n == null) {
            return false; //空节点是黑色
        }
        return n.color;
    }

    /*================旋转操作===============*/

    //左旋：维持左倾，将右红链接转到左红链接
    private Node rotateLeft(Node n) {
        /*
         *新根节点 x 继承原根节点 h 的颜色（保持与父节点连接的性质不变）。
         *原根节点 h 变成红色（因为它现在变成了 x 的左红子节点）。
         */

        //如果assert后面的condition为假，会抛出错误
        assert n != null && isRed(n.right);

        Node h = n.right;
        n.right = h.left;
        h.left = n;
        h.color = n.color;
        n.color = RED;
        h.size = n.size;
        n.size = 1 + size(n.left) + size(n.right);

        return h;
    }

    //右旋：出现连续的两个左倾红节点
    private Node rotateRight(Node n) {
        //临时4节点：左右孩子都是红色
        //把父节点向上挤，颜色翻转，父节点变红色，和上面的节点红链接
        assert n != null && isRed(n.left);

        Node h = n.left;
        n.left = h.right;
        h.right = n;
        h.color = n.color;
        n.color = RED;
        h.size = n.size;
        n.size = 1 + size(n.left) + size(n.right);

        return h;
    }

    private void flipColors(Node n) {
        assert n != null && n.left != null && n.right != null;
        assert (isRed(n) && !isRed(n.left) && !isRed(n.right))
                || (!isRed(n) && isRed(n.left) && isRed(n.right));

        n.left.color = !n.left.color;
        n.right.color = !n.right.color;
        n.color = !n.color;
    }

    /*================插入操作===============*/
    public void put(Key key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        root = putHelper(root, key, value);
        /*
         * root变色的可能：
         * 1.原来的树是空的，插入节点默认是红色
         * 2. 红-黑-红 翻转后变成 黑-红-黑 ，此时根要变成黑色
         */
        root.color = BLACK;
    }

    private Node putHelper(Node n, Key key, Value value) {
        //1.标准BST插入
        if (n == null) {
            return new Node(key, value, RED, 1);
        }

        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            n.right = putHelper(n.right, key, value);
        } else if (cmp < 0) {
            n.left = putHelper(n.left, key, value);
        } else {
            //更新已经保存的键
            n.value = value;
        }

        //2.LLRB维护
        /*
         *三个if顺序不可改变，在回朔过程立刻修复
         *1.标准化：rotateLeft 把所有红链接统一转到左边。
         *2.形态调整：rotateRight 把连续的左红链接（不平衡的 4-节点）调整为左右红链接（平衡的 4-节点）。
         *3.分裂：flipColors 把平衡的 4-节点拆分，将红色推向父节点。
         */

        //如果只有isRed(n.right)的情况下，会左旋 -> 右旋 -> 翻转（双红情况会绕一大圈）
        // 1. 如果右链接是红色而左链接是黑色，左旋转
        //    这确保了"没有右红链接"的性质
        if (isRed(n.right) && !isRed(n.left)) {
            n = rotateLeft(n);
        }

        // 2. 如果左链接和左-左链接都是红色，右旋转
        //    这处理了连续的左红链接（临时的4-节点）
        if (isRed(n.left) && isRed(n.left.left)) {
            n = rotateRight(n);
        }

        // 3. 如果左右链接都是红色，颜色翻转
        //    这将4-节点分裂，相当于2-3树的节点分裂
        if (isRed(n.left) && isRed(n.right)) {
            flipColors(n);
        }

        return n;
    }

    /*================删除===============*/

    /*删除最小*/
    public void deleteMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Tree is Empty!");
        }

        //如果根节点是 2 节点，把根节点强行变红，后续向下传递给子节点
        //LLRB的删除操作不停留在黑色节点上
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = deleteMinHelper(root);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node deleteMinHelper(Node n) {
        if (n.left == null) {
            return null;
        }

        //左倾红黑树保证了右子树的颜色不会是红色，所以如果左子树是黑色，右子树也一定是黑色
        //递归下来的h，从root开始保证传下来h是红色
        if (!isRed(n.left) && !isRed(n.left.left)) {
            n = moveRedLeft(n);
        }

        n.left = deleteMinHelper(n.left);
        return balance(n);
    }

    public void deleteMax() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = deleteMaxHelper(root);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node deleteMaxHelper(Node n) {
        //左边有红节点可以借
        if (isRed(n.left)) {
            n = rotateRight(n);
        }

        if (n.right == null) {
            return null;
        }

        if (!isRed(n.right) && !isRed(n.right.left)) {
            n = moveRedRight(n);
        }

        n.right = deleteMaxHelper(n.right);
        return balance(n);
    }

    public void delete(Key key) {
        if (key == null) {
            throw new NoSuchElementException("key is null");
        }
        if (!contain(key)) {
            return;
        }
        //第一桶红颜料：根节点
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = deleteHelper(root, key);
        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node deleteHelper(Node n, Key key) {
        //前面先检验存在可以后才进行删除操作，所以不用n == null的情况
        if (key.compareTo(n.key) < 0) {
            //避免删除2节点
            if (!isRed(n.left) && !isRed(n.left.left)) {
                n = moveRedLeft(n);
            }
            n.left = deleteHelper(n.left, key);
        } else {
            //往右边走的删除
            //把左边的红色“借”或“推”到右边，使右路变红，并暴露出真正的最大值。
            if (isRed(n.left)) {
                n = rotateRight(n);
            }

            //n.right == null:真正的出口，只有一个节点的情况
            //开始第一步进行了右旋操作，所以h不会有左节点的情况, 也就说此时是叶子节点
            if (key.compareTo(n.key) == 0 && n.right == null) {
                return null;
            }

            //接着往右边跑，注意可能出现2节点
            if (!isRed(n.right) && !isRed(n.right.left)) {
                n = moveRedRight(n);
            }

            if (key.compareTo(n.key) == 0) {
                //标准删除
                Node x = minHelper(n.right);
                n.key = x.key;
                n.value = x.value;
                n.right = deleteMinHelper(n.right);
            } else {
                //如果原来有左节点，也在一开始右旋了
                //所以此时没有左节点情况，只用看右边
                n.right = deleteHelper(n.right, key);
            }
        }
        return balance(n);
    }

    private Node moveRedRight(Node n) {
        flipColors(n);
        if (n.left!=null&& isRed(n.left.left)) {
            n = rotateRight(n);
            flipColors(n);
        }
        return n;
    }

    private Node moveRedLeft(Node n) {
        flipColors(n);
        if (n.right!=null && isRed(n.right.left)) {
            n.right = rotateRight(n.right);
            n = rotateLeft(n);
            flipColors(n);
        }
        return n;
    }


    private Node balance(Node n) {
        if (isRed(n.right)) {
            n = rotateLeft(n);
        }
        if (isRed(n.left) && isRed(n.left.left)) {
            n = rotateRight(n);
        }
        if (isRed(n.left) && isRed(n.right)) {
            flipColors(n);
        }

        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }

    /*================查询==============*/
    public Key min() {
        if (isEmpty()) {
            throw new NoSuchElementException("Tree is Empty");
        }
        return minHelper(root).key;
    }

    private Node minHelper(Node n) {
        if (n.left == null) {
            return n;
        }
        return minHelper(n.left);
    }

    public Key max() {
        if (isEmpty()) {
            throw new NoSuchElementException("Tree is Empty");
        }
        return maxHelper(root).key;
    }

    private Node maxHelper(Node n) {
        if (n.right == null) {
            return n;
        }
        return maxHelper(n.right);
    }

    public boolean contain(Key key) {
        return get(key) != null;
    }

    public Value get(Key key) {
        if (key == null) {
            throw new NoSuchElementException("key not null");
        }
        return getHelper(root, key);
    }

    private Value getHelper(Node n, Key key) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            return getHelper(n.right, key);
        } else if (cmp < 0) {
            return getHelper(n.left, key);
        } else {
            return n.value;
        }
    }

    /*===============遍历===============*/

    /**
     * 中序遍历（升序）
     */
    public void inorderTraversal() {
        System.out.print("Inorder: ");
        inorderHelper(root);
        System.out.println();
    }

    private void inorderHelper(Node node) {
        if (node != null) {
            inorderHelper(node.left);
            System.out.print(node.key + "=" + node.value + " ");
            inorderHelper(node.right);
        }
    }

    /**
     * 前序遍历
     */
    public void preorderTraversal() {
        System.out.print("Preorder: ");
        preorderHelper(root);
        System.out.println();
    }

    private void preorderHelper(Node node) {
        if (node != null) {
            System.out.print(node.key + "=" + node.value + " ");
            preorderHelper(node.left);
            preorderHelper(node.right);
        }
    }

    /**
     * 后序遍历
     */
    public void postorderTraversal() {
        System.out.print("Postorder: ");
        postorderHelper(root);
        System.out.println();
    }

    private void postorderHelper(Node node) {
        if (node != null) {
            postorderHelper(node.left);
            postorderHelper(node.right);
            System.out.print(node.key + "=" + node.value + " ");
        }
    }

    /**
     * 层序遍历（广度优先）
     */
    public void levelOrderTraversal() {
        System.out.print("Level Order: ");
        if (root == null) {
            System.out.println();
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.key + "=" + current.value + " ");

            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        System.out.println();
    }

    /**
     * 获取所有键（升序）
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new LinkedList<>();
        return keys(min(), max());
    }

    //通过剪枝操作提高效率，如在100000个数据，找【100,110】范围
    /*
     *   如果当前节点的键是 `500`，代码执行 `cmplo < 0` (100 < 500) 为真，去左子树找。
     *   但它会发现 `cmphi > 0` (110 > 500) 为假，于是**直接跳过整个右子树**。
     *   这种“跳过”逻辑在树的高层发生时，能瞬间排除掉数以万计的不相关节点。
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new LinkedList<>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node h, Queue<Key> queue, Key lo, Key hi) {
        if (h == null) return;
        int cmplo = lo.compareTo(h.key);
        int cmphi = hi.compareTo(h.key);

        // 1. 如果 lo 比当前 key 小，说明左子树中“可能”还有符合条件的键
        if (cmplo < 0) keys(h.left, queue, lo, hi);
        // 2. 如果当前 key 就在 [lo, hi] 之间，加入队列
        if (cmplo <= 0 && cmphi >= 0) queue.offer(h.key);
        // 3. 如果 hi 比当前 key 大，说明右子树中“可能”还有符合条件的键
        if (cmphi > 0) keys(h.right, queue, lo, hi);
    }

}