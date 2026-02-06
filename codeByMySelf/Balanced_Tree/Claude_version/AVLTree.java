package Balanced_Tree.Claude_version;

/**
 * AVL平衡树完整实现
 * CS61B Data Structures
 * 
 * 功能包括：
 * - 插入 (insert)
 * - 删除 (delete)
 * - 查找 (search)
 * - 遍历 (inorder, preorder, postorder)
 * - 自动平衡
 */
public class AVLTree<T extends Comparable<T>> {
    
    /**
     * AVL树节点类
     */
    private class Node {
        T data;
        Node left;
        Node right;
        int height;  // 节点高度
        
        Node(T data) {
            this.data = data;
            this.height = 1;  // 新节点初始高度为1
        }
    }
    
    private Node root;
    private int size;
    
    /**
     * 构造函数
     */
    public AVLTree() {
        this.root = null;
        this.size = 0;
    }
    
    /**
     * 获取节点高度
     */
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }
    
    /**
     * 更新节点高度
     */
    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }
    
    /**
     * 获取平衡因子
     * 平衡因子 = 左子树高度 - 右子树高度
     */
    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
    
    /**
     * 右旋转
     *        y                               x
     *       / \     Right Rotation          / \
     *      x   T3   – – – – – – – >        T1  y
     *     / \                                 / \
     *    T1  T2                              T2  T3
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        // 执行旋转
        x.right = y;
        y.left = T2;
        
        // 更新高度
        updateHeight(y);
        updateHeight(x);
        
        return x;  // 返回新的根节点
    }
    
    /**
     * 左旋转
     *      x                                y
     *     / \      Left Rotation           / \
     *    T1  y     – – – – – – – >        x   T3
     *       / \                          / \
     *      T2  T3                       T1  T2
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        // 执行旋转
        y.left = x;
        x.right = T2;
        
        // 更新高度
        updateHeight(x);
        updateHeight(y);
        
        return y;  // 返回新的根节点
    }
    
    /**
     * 插入元素（公共方法）
     */
    public void insert(T data) {
        root = insertHelper(root, data);
    }
    
    /**
     * 插入元素（递归辅助方法）
     */
    private Node insertHelper(Node node, T data) {
        // 1. 标准BST插入
        if (node == null) {
            size++;
            return new Node(data);
        }
        
        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insertHelper(node.left, data);
        } else if (cmp > 0) {
            node.right = insertHelper(node.right, data);
        } else {
            // 重复元素，不插入
            return node;
        }
        
        // 2. 更新当前节点高度
        updateHeight(node);
        
        // 3. 获取平衡因子
        int balance = getBalance(node);
        
        // 4. 如果不平衡，有四种情况需要处理
        
        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }
        
        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }
        
        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Right Left Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    /**
     * 查找最小值节点
     */
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    /**
     * 删除元素（公共方法）
     */
    public void delete(T data) {
        root = deleteHelper(root, data);
    }
    
    /**
     * 删除元素（递归辅助方法）
     */
    private Node deleteHelper(Node node, T data) {
        // 1. 标准BST删除
        if (node == null) {
            return null;
        }
        
        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = deleteHelper(node.left, data);
        } else if (cmp > 0) {
            node.right = deleteHelper(node.right, data);
        } else {
            // 找到要删除的节点
            size--;
            
            // 只有一个子节点或没有子节点
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            
            // 有两个子节点：找到右子树的最小节点（中序后继）
            Node temp = findMin(node.right);
            node.data = temp.data;
            node.right = deleteHelper(node.right, temp.data);
        }
        
        // 如果树只有一个节点，直接返回
        if (node == null) {
            return null;
        }
        
        // 2. 更新当前节点高度
        updateHeight(node);
        
        // 3. 获取平衡因子
        int balance = getBalance(node);
        
        // 4. 如果不平衡，有四种情况需要处理
        
        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        
        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        
        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    /**
     * 查找元素
     */
    public boolean search(T data) {
        return searchHelper(root, data);
    }
    
    /**
     * 查找元素（递归辅助方法）
     */
    private boolean searchHelper(Node node, T data) {
        if (node == null) {
            return false;
        }
        
        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            return searchHelper(node.left, data);
        } else if (cmp > 0) {
            return searchHelper(node.right, data);
        } else {
            return true;
        }
    }
    
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
        if (node != null) {
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
        if (node != null) {
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
        if (root == null) {
            System.out.println();
            return;
        }
        
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.data + " ");
            
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
        return height(root);
    }
    
    /**
     * 清空树
     */
    public void clear() {
        root = null;
        size = 0;
    }
    
    /**
     * 打印树的结构（用于调试）
     */
    public void printTree() {
        System.out.println("Tree Structure:");
        printTreeHelper(root, "", true);
    }
    
    private void printTreeHelper(Node node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }
        
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.data + " (h:" + node.height + ")");
        
        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTreeHelper(node.left, prefix + (isTail ? "    " : "│   "), node.right == null);
            }
            if (node.right != null) {
                printTreeHelper(node.right, prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
    
    /**
     * 验证树是否是AVL树（用于测试）
     */
    public boolean isAVL() {
        return isAVLHelper(root);
    }
    
    private boolean isAVLHelper(Node node) {
        if (node == null) {
            return true;
        }
        
        int balance = getBalance(node);
        if (Math.abs(balance) > 1) {
            return false;
        }
        
        return isAVLHelper(node.left) && isAVLHelper(node.right);
    }
}
