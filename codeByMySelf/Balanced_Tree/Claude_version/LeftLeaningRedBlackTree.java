package Balanced_Tree.Claude_version;

/**
 * 左倾红黑树（Left-Leaning Red-Black Tree, LLRB）完整实现
 * CS61B Data Structures
 *
 * LLRB性质：
 * 1. 与2-3树保持1-1对应关系
 * 2. 没有节点有2条红色链接
 * 3. 没有红色右链接（核心性质！）
 * 4. 从根到叶的每条路径都有相同数量的黑色链接
 * 5. 树的高度不超过对应2-3树高度的2倍
 *
 * 功能包括：
 * - 插入 (insert) - 简洁的实现
 * - 删除 (delete) - 完整实现
 * - 查找 (search)
 * - 遍历 (inorder, preorder, postorder, level-order)
 * - 自动维护左倾性质
 */
public class LeftLeaningRedBlackTree<Key extends Comparable<Key>, Value> {

    // 颜色常量
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /**
     * LLRB树节点类
     */
    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        boolean color;  // 父链接的颜色：true = RED, false = BLACK
        int size;       // 子树中的节点总数

        Node(Key key, Value value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private Node root;

    /**
     * 构造函数
     */
    public LeftLeaningRedBlackTree() {
        this.root = null;
    }

    // ==================== 辅助方法 ====================

    /**
     * 判断节点是否为红色
     */
    private boolean isRed(Node node) {
        if (node == null) return false;  // null链接是黑色
        return node.color == RED;
    }

    /**
     * 获取节点的大小
     */
    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    /**
     * 获取树的大小（公共方法）
     */
    public int size() {
        return size(root);
    }

    /**
     * 判断树是否为空
     */
    public boolean isEmpty() {
        return root == null;
    }

    // ==================== 旋转操作 ====================

    /**
     * 左旋转
     * 将右红链接转换为左红链接
     *
     *      E(B)                     S(B)
     *     /  \       左旋转         /  \
     *   (B) S(R)    ------->     E(R) (B)
     *       / \                  / \
     *     (B) (B)              (B) (B)
     */
    private Node rotateLeft(Node h) {
        assert h != null && isRed(h.right);

        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);

        return x;
    }

    /**
     * 右旋转
     * 将左红链接向右旋转
     *
     *        S(B)                   E(B)
     *       /  \      右旋转        /  \
     *     E(R) (B)   ------->     (B) S(R)
     *     / \                         / \
     *   (B) (B)                     (B) (B)
     */
    private Node rotateRight(Node h) {
        assert h != null && isRed(h.left);

        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);

        return x;
    }

    /**
     * 颜色翻转
     * 将4-节点分裂
     *
     *       S(B)                    S(R)
     *      /  \      颜色翻转       /  \
     *    E(R) G(R)  ------->     E(B) G(B)
     */
    private void flipColors(Node h) {
        assert h != null && h.left != null && h.right != null;
        assert !isRed(h) && isRed(h.left) && isRed(h.right)
                || isRed(h) && !isRed(h.left) && !isRed(h.right);

        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // ==================== 插入操作 ====================

    /**
     * 插入键值对（公共方法）
     */
    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");

        root = put(root, key, value);
        root.color = BLACK;  // 根节点始终为黑色
    }

    /**
     * 插入键值对（递归辅助方法）
     *
     * 这是LLRB的核心：只需要3个if语句来维护LLRB性质！
     */
    private Node put(Node h, Key key, Value value) {
        // 标准BST插入：到达叶子节点，创建新的红色节点
        if (h == null) return new Node(key, value, RED, 1);

        // 标准BST插入
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = put(h.left, key, value);
        } else if (cmp > 0) {
            h.right = put(h.right, key, value);
        } else {
            h.value = value;  // 更新已存在的键
        }

        // ===== LLRB维护（只需3行！这是LLRB的精髓）=====

        // 1. 如果右链接是红色而左链接是黑色，左旋转
        //    这确保了"没有右红链接"的性质
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }

        // 2. 如果左链接和左-左链接都是红色，右旋转
        //    这处理了连续的左红链接（临时的4-节点）
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }

        // 3. 如果左右链接都是红色，颜色翻转
        //    这将4-节点分裂，相当于2-3树的节点分裂
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    // ==================== 删除操作 ====================

    /**
     * 删除最小键
     */
    public void deleteMin() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Tree is empty");

        // 如果根的两个子节点都是黑色，将根设为红色
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return null;

        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }

        h.left = deleteMin(h.left);
        return balance(h);
    }

    /**
     * 删除最大键
     */
    public void deleteMax() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Tree is empty");

        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
    }

    private Node deleteMax(Node h) {
        if (isRed(h.left)) {
            h = rotateRight(h);
        }

        if (h.right == null) return null;

        if (!isRed(h.right) && !isRed(h.right.left)) {
            h = moveRedRight(h);
        }

        h.right = deleteMax(h.right);
        return balance(h);
    }

    /**
     * 删除指定键
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        if (!contains(key)) return;

        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
    }

    private Node delete(Node h, Key key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && h.right == null) {
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
    }

    /**
     * 将红色链接向左移动
     */
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    /**
     * 将红色链接向右移动
     */
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    /**
     * 恢复LLRB不变性
     */
    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.size = 1 + size(h.left) + size(h.right);
        return h;
    }

    // ==================== 查找操作 ====================

    /**
     * 获取键对应的值
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        return get(root, key);
    }

    private Value get(Node h, Key key) {
        while (h != null) {
            int cmp = key.compareTo(h.key);
            if (cmp < 0) h = h.left;
            else if (cmp > 0) h = h.right;
            else return h.value;
        }
        return null;
    }

    /**
     * 判断树是否包含某个键
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     * 查找最小键
     */
    public Key min() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Tree is empty");
        return min(root).key;
    }

    private Node min(Node h) {
        if (h.left == null) return h;
        return min(h.left);
    }

    /**
     * 查找最大键
     */
    public Key max() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Tree is empty");
        return max(root).key;
    }

    private Node max(Node h) {
        if (h.right == null) return h;
        return max(h.right);
    }

    // ==================== 遍历操作 ====================

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

        java.util.Queue<Node> queue = new java.util.LinkedList<>();
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
        if (isEmpty()) return new java.util.LinkedList<>();
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        java.util.Queue<Key> queue = new java.util.LinkedList<>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node h, java.util.Queue<Key> queue, Key lo, Key hi) {
        if (h == null) return;
        int cmplo = lo.compareTo(h.key);
        int cmphi = hi.compareTo(h.key);
        if (cmplo < 0) keys(h.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.offer(h.key);
        if (cmphi > 0) keys(h.right, queue, lo, hi);
    }

    // ==================== 辅助功能 ====================

    /**
     * 获取树的高度
     */
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * 打印树的结构（显示颜色）
     */
    public void printTree() {
        System.out.println("LLRB Tree Structure:");
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }
        printTreeHelper(root, "", true);
    }

    private void printTreeHelper(Node node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        String colorStr = node.color == RED ? "R" : "B";
        System.out.println(prefix + (isTail ? "└── " : "├── ") +
                node.key + "=" + node.value + "(" + colorStr + ")");

        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTreeHelper(node.left, prefix + (isTail ? "    " : "│   "), node.right == null);
            }
            if (node.right != null) {
                printTreeHelper(node.right, prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }

    // ==================== 验证功能 ====================

    /**
     * 验证是否是有效的LLRB树
     */
    public boolean isLLRB() {
        return isLLRB(root);
    }

    private boolean isLLRB(Node h) {
        if (h == null) return true;

        // 检查是否有右红链接
        if (isRed(h.right)) {
            System.out.println("违反LLRB性质: 节点 " + h.key + " 有右红链接！");
            return false;
        }

        // 检查是否有连续的左红链接
        if (isRed(h) && isRed(h.left)) {
            System.out.println("违反LLRB性质: 节点 " + h.key + " 有连续的红色链接！");
            return false;
        }

        return isLLRB(h.left) && isLLRB(h.right);
    }

    /**
     * 检查是否是有效的BST
     */
    public boolean isBST() {
        return isBST(root, null, null);
    }

    private boolean isBST(Node h, Key min, Key max) {
        if (h == null) return true;
        if (min != null && h.key.compareTo(min) <= 0) return false;
        if (max != null && h.key.compareTo(max) >= 0) return false;
        return isBST(h.left, min, h.key) && isBST(h.right, h.key, max);
    }

    /**
     * 检查黑色链接数是否平衡
     */
    public boolean isBalanced() {
        int black = 0;  // 从根到最左叶子的黑色链接数
        Node h = root;
        while (h != null) {
            if (!isRed(h)) black++;
            h = h.left;
        }
        return isBalanced(root, black);
    }

    private boolean isBalanced(Node h, int black) {
        if (h == null) return black == 0;
        if (!isRed(h)) black--;
        return isBalanced(h.left, black) && isBalanced(h.right, black);
    }

    /**
     * 统计红色节点数量
     */
    public int countRedNodes() {
        return countRedNodes(root);
    }

    private int countRedNodes(Node node) {
        if (node == null) return 0;
        int count = isRed(node) ? 1 : 0;
        return count + countRedNodes(node.left) + countRedNodes(node.right);
    }

    // ==================== 测试主函数 ====================

    /**
     * 测试LLRB树的所有功能
     */
    public static void main(String[] args) {
        System.out.println("=== 左倾红黑树（LLRB）完整功能测试 ===\n");

        // 创建LLRB树
        LeftLeaningRedBlackTree<Integer, String> tree = new LeftLeaningRedBlackTree<>();

        // 测试1: 插入操作
        System.out.println("1. 测试插入操作");
        System.out.println("插入键值对: (10,A), (20,B), (30,C), (40,D), (50,E), (25,F), (15,G), (5,H)");
        tree.put(10, "A");
        tree.put(20, "B");
        tree.put(30, "C");
        tree.put(40, "D");
        tree.put(50, "E");
        tree.put(25, "F");
        tree.put(15, "G");
        tree.put(5, "H");

        System.out.println("树的大小: " + tree.size());
        System.out.println("树的高度: " + tree.height());
        System.out.println("红色节点数: " + tree.countRedNodes());
        tree.printTree();
        System.out.println("是否是合法的LLRB: " + tree.isLLRB());
        System.out.println("是否是合法的BST: " + tree.isBST());
        System.out.println("是否平衡: " + tree.isBalanced());
        System.out.println();

        // 测试2: 遍历操作
        System.out.println("2. 测试遍历操作");
        tree.inorderTraversal();
        tree.preorderTraversal();
        tree.postorderTraversal();
        tree.levelOrderTraversal();
        System.out.println();

        // 测试3: 查找操作
        System.out.println("3. 测试查找操作");
        System.out.println("查找键25的值: " + tree.get(25));
        System.out.println("查找键35的值: " + tree.get(35));
        System.out.println("树是否包含键25: " + tree.contains(25));
        System.out.println("树是否包含键35: " + tree.contains(35));
        System.out.println("最小键: " + tree.min());
        System.out.println("最大键: " + tree.max());
        System.out.println();

        // 测试4: 所有键
        System.out.println("4. 获取所有键");
        System.out.print("所有键: ");
        for (Integer key : tree.keys()) {
            System.out.print(key + " ");
        }
        System.out.println("\n");

        // 测试5: 删除操作
        System.out.println("5. 测试删除操作");
        System.out.println("删除键20");
        tree.delete(20);
        tree.printTree();
        System.out.println("是否是合法的LLRB: " + tree.isLLRB());
        tree.inorderTraversal();
        System.out.println();

        System.out.println("删除最小键");
        tree.deleteMin();
        tree.printTree();
        System.out.println("是否是合法的LLRB: " + tree.isLLRB());
        tree.inorderTraversal();
        System.out.println();

        System.out.println("删除最大键");
        tree.deleteMax();
        tree.printTree();
        System.out.println("是否是合法的LLRB: " + tree.isLLRB());
        tree.inorderTraversal();
        System.out.println();

        // 测试6: 大量插入测试
        System.out.println("6. 测试大量插入（1-100）");
        LeftLeaningRedBlackTree<Integer, Integer> bigTree = new LeftLeaningRedBlackTree<>();
        for (int i = 1; i <= 100; i++) {
            bigTree.put(i, i * 10);
        }
        System.out.println("树的大小: " + bigTree.size());
        System.out.println("树的高度: " + bigTree.height());
        System.out.println("是否是合法的LLRB: " + bigTree.isLLRB());
        System.out.println("是否平衡: " + bigTree.isBalanced());
        System.out.println();

        // 测试7: 字符串键类型的LLRB树
        System.out.println("7. 测试字符串键类型LLRB树");
        LeftLeaningRedBlackTree<String, Integer> stringTree = new LeftLeaningRedBlackTree<>();
        String[] words = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};
        for (int i = 0; i < words.length; i++) {
            stringTree.put(words[i], i + 1);
        }
        stringTree.printTree();
        stringTree.inorderTraversal();
        System.out.println("查找 'cherry': " + stringTree.get("cherry"));
        System.out.println("查找 'orange': " + stringTree.get("orange"));
        System.out.println("是否是合法的LLRB: " + stringTree.isLLRB());
        System.out.println();

        // 测试8: 随机插入测试
        System.out.println("8. 测试随机插入");
        LeftLeaningRedBlackTree<Integer, String> randomTree = new LeftLeaningRedBlackTree<>();
        int[] randomKeys = {15, 8, 23, 4, 12, 19, 27, 2, 6, 10, 14, 17, 21, 25, 30};
        System.out.print("插入键序列: ");
        for (int key : randomKeys) {
            System.out.print(key + " ");
            randomTree.put(key, "V" + key);
        }
        System.out.println();
        randomTree.printTree();
        System.out.println("是否是合法的LLRB: " + randomTree.isLLRB());
        randomTree.inorderTraversal();
        System.out.println();

        // 测试9: 删除后验证
        System.out.println("9. 测试大量删除操作");
        LeftLeaningRedBlackTree<Integer, Integer> delTree = new LeftLeaningRedBlackTree<>();
        for (int i = 1; i <= 20; i++) {
            delTree.put(i, i);
        }
        System.out.println("删除前大小: " + delTree.size());

        for (int i = 1; i <= 10; i++) {
            delTree.delete(i);
            if (!delTree.isLLRB()) {
                System.out.println("删除" + i + "后违反LLRB性质！");
            }
        }
        System.out.println("删除后大小: " + delTree.size());
        delTree.inorderTraversal();
        System.out.println("是否是合法的LLRB: " + delTree.isLLRB());
        System.out.println();

        // 测试10: 验证无右红链接性质
        System.out.println("10. 验证LLRB核心性质：无右红链接");
        LeftLeaningRedBlackTree<Integer, Integer> testTree = new LeftLeaningRedBlackTree<>();
        for (int i = 1; i <= 50; i++) {
            testTree.put(i, i);
        }
        System.out.println("插入1-50后，是否有右红链接: " + !testTree.isLLRB());
        System.out.println("树的高度: " + testTree.height());
        System.out.println("理论最大高度(2 * log2(50)): " + (int)(2 * Math.log(50) / Math.log(2)));
        System.out.println();

        System.out.println("=== 所有测试完成 ===");
    }
}