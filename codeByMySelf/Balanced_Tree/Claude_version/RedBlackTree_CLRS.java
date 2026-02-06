/**
 * 红黑树完整实现
 * CS61B Data Structures
 * 
 * 红黑树性质：
 * 1. 每个节点要么是红色，要么是黑色
 * 2. 根节点是黑色
 * 3. 所有叶子节点（NIL）是黑色
 * 4. 红色节点的两个子节点都是黑色（不能有连续的红色节点）
 * 5. 从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点（黑高相同）
 * 
 * 功能包括：
 * - 插入 (insert)
 * - 删除 (delete)
 * - 查找 (search)
 * - 遍历 (inorder, preorder, postorder, level-order)
 * - 自动平衡
 */
public class RedBlackTree_CLRS<T extends Comparable<T>> {
    
    // 颜色常量
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    
    /**
     * 红黑树节点类
     */
    private class Node {
        T data;
        Node left;
        Node right;
        Node parent;
        boolean color;  // true = RED, false = BLACK
        
        Node(T data) {
            this.data = data;
            this.color = RED;  // 新节点默认为红色
        }
        
        Node(T data, boolean color) {
            this.data = data;
            this.color = color;
        }
    }
    
    private Node root;
    private Node NIL;  // 哨兵节点，代表所有叶子节点
    private int size;
    
    /**
     * 构造函数
     */
    public RedBlackTree_CLRS() {
        NIL = new Node(null, BLACK);
        NIL.left = null;
        NIL.right = null;
        NIL.parent = null;
        root = NIL;
        size = 0;
    }
    
    // ==================== 旋转操作 ====================
    
    /**
     * 左旋转
     *      x                                y
     *     / \      Left Rotation           / \
     *    T1  y     – – – – – – – >        x   T3
     *       / \                          / \
     *      T2  T3                       T1  T2
     */
    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        
        if (y.left != NIL) {
            y.left.parent = x;
        }
        
        y.parent = x.parent;
        
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        
        y.left = x;
        x.parent = y;
    }
    
    /**
     * 右旋转
     *        y                               x
     *       / \     Right Rotation          / \
     *      x   T3   – – – – – – – >        T1  y
     *     / \                                 / \
     *    T1  T2                              T2  T3
     */
    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        
        if (x.right != NIL) {
            x.right.parent = y;
        }
        
        x.parent = y.parent;
        
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        
        x.right = y;
        y.parent = x;
    }
    
    // ==================== 插入操作 ====================
    
    /**
     * 插入元素（公共方法）
     */
    public void insert(T data) {
        Node newNode = new Node(data);
        newNode.left = NIL;
        newNode.right = NIL;
        newNode.parent = null;
        
        Node parent = null;
        Node current = root;
        
        // 1. 找到插入位置（标准BST插入）
        while (current != NIL) {
            parent = current;
            int cmp = data.compareTo(current.data);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                // 重复元素，不插入
                return;
            }
        }
        
        newNode.parent = parent;
        
        if (parent == null) {
            root = newNode;
        } else if (data.compareTo(parent.data) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        
        size++;
        
        // 2. 修复红黑树性质
        insertFixup(newNode);
    }
    
    /**
     * 插入后修复红黑树性质
     * 
     * 情况分析：
     * Case 1: 叔叔是红色 - 重新着色
     * Case 2: 叔叔是黑色，且当前节点是右孩子 - 左旋转变成Case 3
     * Case 3: 叔叔是黑色，且当前节点是左孩子 - 右旋转+重新着色
     */
    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                // 父节点是祖父节点的左孩子
                Node uncle = z.parent.parent.right;
                
                if (uncle.color == RED) {
                    // Case 1: 叔叔是红色
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        // Case 2: 叔叔是黑色，且z是右孩子
                        z = z.parent;
                        rotateLeft(z);
                    }
                    // Case 3: 叔叔是黑色，且z是左孩子
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                // 父节点是祖父节点的右孩子（镜像情况）
                Node uncle = z.parent.parent.left;
                
                if (uncle.color == RED) {
                    // Case 1: 叔叔是红色
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        // Case 2: 叔叔是黑色，且z是左孩子
                        z = z.parent;
                        rotateRight(z);
                    }
                    // Case 3: 叔叔是黑色，且z是右孩子
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;  // 保证根节点是黑色
    }
    
    // ==================== 删除操作 ====================
    
    /**
     * 查找最小值节点
     */
    private Node findMin(Node node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }
    
    /**
     * 用一个子树替换另一个子树
     */
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }
    
    /**
     * 删除元素（公共方法）
     */
    public void delete(T data) {
        Node z = searchNode(root, data);
        if (z == NIL) {
            return;  // 元素不存在
        }
        deleteNode(z);
    }
    
    /**
     * 查找节点（辅助方法）
     */
    private Node searchNode(Node node, T data) {
        while (node != NIL) {
            int cmp = data.compareTo(node.data);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return NIL;
    }
    
    /**
     * 删除节点
     */
    private void deleteNode(Node z) {
        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;
        
        if (z.left == NIL) {
            // 只有右孩子或没有孩子
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            // 只有左孩子
            x = z.left;
            transplant(z, z.left);
        } else {
            // 有两个孩子：找到右子树的最小节点（后继）
            y = findMin(z.right);
            yOriginalColor = y.color;
            x = y.right;
            
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        
        size--;
        
        // 如果删除的节点是黑色，需要修复
        if (yOriginalColor == BLACK) {
            deleteFixup(x);
        }
    }
    
    /**
     * 删除后修复红黑树性质
     * 
     * 情况分析：
     * Case 1: 兄弟是红色 - 旋转+重新着色，转换成Case 2/3/4
     * Case 2: 兄弟是黑色，兄弟的两个孩子都是黑色 - 重新着色
     * Case 3: 兄弟是黑色，兄弟的左孩子是红色，右孩子是黑色 - 旋转+重新着色，转换成Case 4
     * Case 4: 兄弟是黑色，兄弟的右孩子是红色 - 旋转+重新着色
     */
    private void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                // x是左孩子
                Node sibling = x.parent.right;
                
                if (sibling.color == RED) {
                    // Case 1: 兄弟是红色
                    sibling.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    sibling = x.parent.right;
                }
                
                if (sibling.left.color == BLACK && sibling.right.color == BLACK) {
                    // Case 2: 兄弟是黑色，兄弟的两个孩子都是黑色
                    sibling.color = RED;
                    x = x.parent;
                } else {
                    if (sibling.right.color == BLACK) {
                        // Case 3: 兄弟是黑色，兄弟的左孩子是红色，右孩子是黑色
                        sibling.left.color = BLACK;
                        sibling.color = RED;
                        rotateRight(sibling);
                        sibling = x.parent.right;
                    }
                    // Case 4: 兄弟是黑色，兄弟的右孩子是红色
                    sibling.color = x.parent.color;
                    x.parent.color = BLACK;
                    sibling.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                // x是右孩子（镜像情况）
                Node sibling = x.parent.left;
                
                if (sibling.color == RED) {
                    // Case 1: 兄弟是红色
                    sibling.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    sibling = x.parent.left;
                }
                
                if (sibling.right.color == BLACK && sibling.left.color == BLACK) {
                    // Case 2: 兄弟是黑色，兄弟的两个孩子都是黑色
                    sibling.color = RED;
                    x = x.parent;
                } else {
                    if (sibling.left.color == BLACK) {
                        // Case 3: 兄弟是黑色，兄弟的右孩子是红色，左孩子是黑色
                        sibling.right.color = BLACK;
                        sibling.color = RED;
                        rotateLeft(sibling);
                        sibling = x.parent.left;
                    }
                    // Case 4: 兄弟是黑色，兄弟的左孩子是红色
                    sibling.color = x.parent.color;
                    x.parent.color = BLACK;
                    sibling.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }
    
    // ==================== 查找操作 ====================
    
    /**
     * 查找元素
     */
    public boolean search(T data) {
        return searchNode(root, data) != NIL;
    }
    
    /**
     * 查找最小值
     */
    public T findMin() {
        if (root == NIL) {
            return null;
        }
        return findMin(root).data;
    }
    
    /**
     * 查找最大值
     */
    public T findMax() {
        if (root == NIL) {
            return null;
        }
        Node current = root;
        while (current.right != NIL) {
            current = current.right;
        }
        return current.data;
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
        if (node != NIL) {
            inorderHelper(node.left);
            System.out.print(node.data + " ");
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
        if (node != NIL) {
            System.out.print(node.data + " ");
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
        if (node != NIL) {
            postorderHelper(node.left);
            postorderHelper(node.right);
            System.out.print(node.data + " ");
        }
    }
    
    /**
     * 层序遍历（广度优先）
     */
    public void levelOrderTraversal() {
        System.out.print("Level Order: ");
        if (root == NIL) {
            System.out.println();
            return;
        }
        
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.data + " ");
            
            if (current.left != NIL) {
                queue.offer(current.left);
            }
            if (current.right != NIL) {
                queue.offer(current.right);
            }
        }
        System.out.println();
    }
    
    // ==================== 辅助功能 ====================
    
    /**
     * 获取树的大小
     */
    public int size() {
        return size;
    }
    
    /**
     * 检查树是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 获取树的高度
     */
    public int getHeight() {
        return getHeightHelper(root);
    }
    
    private int getHeightHelper(Node node) {
        if (node == NIL) {
            return 0;
        }
        return 1 + Math.max(getHeightHelper(node.left), getHeightHelper(node.right));
    }
    
    /**
     * 获取黑高（从节点到叶子的路径上的黑色节点数）
     */
    public int getBlackHeight() {
        return getBlackHeightHelper(root);
    }
    
    private int getBlackHeightHelper(Node node) {
        if (node == NIL) {
            return 0;
        }
        int leftHeight = getBlackHeightHelper(node.left);
        return leftHeight + (node.color == BLACK ? 1 : 0);
    }
    
    /**
     * 清空树
     */
    public void clear() {
        root = NIL;
        size = 0;
    }
    
    /**
     * 打印树的结构（用于调试）
     */
    public void printTree() {
        System.out.println("Red-Black Tree Structure:");
        if (root == NIL) {
            System.out.println("(empty tree)");
            return;
        }
        printTreeHelper(root, "", true);
    }
    
    private void printTreeHelper(Node node, String prefix, boolean isTail) {
        if (node == NIL) {
            return;
        }
        
        String colorStr = node.color == RED ? "R" : "B";
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.data + "(" + colorStr + ")");
        
        if (node.left != NIL || node.right != NIL) {
            if (node.left != NIL) {
                printTreeHelper(node.left, prefix + (isTail ? "    " : "│   "), node.right == NIL);
            }
            if (node.right != NIL) {
                printTreeHelper(node.right, prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
    
    // ==================== 验证功能 ====================
    
    /**
     * 验证是否满足红黑树所有性质
     */
    public boolean isValidRedBlackTree() {
        // 性质2: 根节点是黑色
        if (root != NIL && root.color != BLACK) {
            System.out.println("违反性质2: 根节点不是黑色");
            return false;
        }
        
        // 性质4: 红色节点的两个子节点都是黑色
        if (hasConsecutiveRed(root)) {
            System.out.println("违反性质4: 存在连续的红色节点");
            return false;
        }
        
        // 性质5: 从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点
        if (checkBlackHeight(root) == -1) {
            System.out.println("违反性质5: 黑高不一致");
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查是否存在连续的红色节点
     */
    private boolean hasConsecutiveRed(Node node) {
        if (node == NIL) {
            return false;
        }
        
        if (node.color == RED) {
            if ((node.left != NIL && node.left.color == RED) || 
                (node.right != NIL && node.right.color == RED)) {
                return true;
            }
        }
        
        return hasConsecutiveRed(node.left) || hasConsecutiveRed(node.right);
    }
    
    /**
     * 检查黑高是否一致
     * 返回黑高，如果不一致返回-1
     */
    private int checkBlackHeight(Node node) {
        if (node == NIL) {
            return 0;
        }
        
        int leftHeight = checkBlackHeight(node.left);
        int rightHeight = checkBlackHeight(node.right);
        
        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) {
            return -1;
        }
        
        return leftHeight + (node.color == BLACK ? 1 : 0);
    }
    
    /**
     * 统计红色节点数量
     */
    public int countRedNodes() {
        return countRedNodesHelper(root);
    }
    
    private int countRedNodesHelper(Node node) {
        if (node == NIL) {
            return 0;
        }
        int count = node.color == RED ? 1 : 0;
        return count + countRedNodesHelper(node.left) + countRedNodesHelper(node.right);
    }
    
    /**
     * 统计黑色节点数量
     */
    public int countBlackNodes() {
        return countBlackNodesHelper(root);
    }
    
    private int countBlackNodesHelper(Node node) {
        if (node == NIL) {
            return 0;
        }
        int count = node.color == BLACK ? 1 : 0;
        return count + countBlackNodesHelper(node.left) + countBlackNodesHelper(node.right);
    }
    
    // ==================== 测试主函数 ====================
    
    /**
     * 测试红黑树的所有功能
     */
    public static void main(String[] args) {
        System.out.println("=== 红黑树完整功能测试 ===\n");
        
        // 创建红黑树
        RedBlackTree_CLRS<Integer> tree = new RedBlackTree_CLRS<>();
        
        // 测试1: 插入操作
        System.out.println("1. 测试插入操作");
        System.out.println("插入元素: 10, 20, 30, 40, 50, 25, 15, 5");
        int[] insertValues = {10, 20, 30, 40, 50, 25, 15, 5};
        for (int val : insertValues) {
            tree.insert(val);
        }
        
        System.out.println("树的大小: " + tree.size());
        System.out.println("树的高度: " + tree.getHeight());
        System.out.println("黑高: " + tree.getBlackHeight());
        System.out.println("红色节点数: " + tree.countRedNodes());
        System.out.println("黑色节点数: " + tree.countBlackNodes());
        tree.printTree();
        System.out.println("是否是合法的红黑树: " + tree.isValidRedBlackTree());
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
        System.out.println("查找25: " + tree.search(25));
        System.out.println("查找35: " + tree.search(35));
        System.out.println("最小值: " + tree.findMin());
        System.out.println("最大值: " + tree.findMax());
        System.out.println();
        
        // 测试4: 删除操作
        System.out.println("4. 测试删除操作");
        System.out.println("删除20");
        tree.delete(20);
        tree.printTree();
        System.out.println("是否是合法的红黑树: " + tree.isValidRedBlackTree());
        tree.inorderTraversal();
        System.out.println();
        
        System.out.println("删除30");
        tree.delete(30);
        tree.printTree();
        System.out.println("是否是合法的红黑树: " + tree.isValidRedBlackTree());
        tree.inorderTraversal();
        System.out.println();
        
        // 测试5: 大量插入测试
        System.out.println("5. 测试大量插入（1-100）");
        RedBlackTree_CLRS<Integer> bigTree = new RedBlackTree_CLRS<>();
        for (int i = 1; i <= 100; i++) {
            bigTree.insert(i);
        }
        System.out.println("树的大小: " + bigTree.size());
        System.out.println("树的高度: " + bigTree.getHeight());
        System.out.println("黑高: " + bigTree.getBlackHeight());
        System.out.println("是否是合法的红黑树: " + bigTree.isValidRedBlackTree());
        System.out.println();
        
        // 测试6: 字符串类型的红黑树
        System.out.println("6. 测试字符串类型红黑树");
        RedBlackTree_CLRS<String> stringTree = new RedBlackTree_CLRS<>();
        String[] words = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};
        for (String word : words) {
            stringTree.insert(word);
        }
        stringTree.printTree();
        stringTree.inorderTraversal();
        System.out.println("查找 'cherry': " + stringTree.search("cherry"));
        System.out.println("查找 'orange': " + stringTree.search("orange"));
        System.out.println();
        
        // 测试7: 删除后验证
        System.out.println("7. 测试大量删除操作");
        RedBlackTree_CLRS<Integer> delTree = new RedBlackTree_CLRS<>();
        for (int i = 1; i <= 20; i++) {
            delTree.insert(i);
        }
        System.out.println("删除前大小: " + delTree.size());
        
        for (int i = 1; i <= 10; i++) {
            delTree.delete(i);
            if (!delTree.isValidRedBlackTree()) {
                System.out.println("删除" + i + "后违反红黑树性质！");
            }
        }
        System.out.println("删除后大小: " + delTree.size());
        delTree.inorderTraversal();
        System.out.println("是否是合法的红黑树: " + delTree.isValidRedBlackTree());
        System.out.println();
        
        // 测试8: 边界情况
        System.out.println("8. 测试边界情况");
        RedBlackTree_CLRS<Integer> edgeTree = new RedBlackTree_CLRS<>();
        System.out.println("空树搜索: " + edgeTree.search(1));
        System.out.println("空树大小: " + edgeTree.size());
        
        edgeTree.insert(5);
        System.out.println("单节点树大小: " + edgeTree.size());
        edgeTree.delete(5);
        System.out.println("删除后大小: " + edgeTree.size());
        System.out.println();
        
        System.out.println("=== 所有测试完成 ===");
    }
}
