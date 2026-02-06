package Balanced_Tree.Claude_version;

/**
 * 红黑树实现（用于与AVL树对比学习）
 * CS61B Data Structures
 * 
 * 红黑树性质：
 * 1. 每个节点是红色或黑色
 * 2. 根节点是黑色
 * 3. 所有叶子节点（NIL）是黑色
 * 4. 红色节点的两个子节点都是黑色（不能有连续的红色节点）
 * 5. 从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点
 */
class RedBlackTree_CLRS_EASY<T extends Comparable<T>> {
    
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
    }
    
    private Node root;
    private Node NIL;  // 哨兵节点，代表所有叶子节点
    private int size;
    
    /**
     * 构造函数
     */
    public RedBlackTree_CLRS_EASY() {
        NIL = new Node(null);
        NIL.color = BLACK;
        NIL.left = null;
        NIL.right = null;
        root = NIL;
        size = 0;
    }
    
    /**
     * 左旋转
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
    
    /**
     * 插入元素
     */
    public void insert(T data) {
        Node newNode = new Node(data);
        newNode.left = NIL;
        newNode.right = NIL;
        
        Node parent = null;
        Node current = root;
        
        // 找到插入位置
        while (current != NIL) {
            parent = current;
            if (data.compareTo(current.data) < 0) {
                current = current.left;
            } else if (data.compareTo(current.data) > 0) {
                current = current.right;
            } else {
                return;  // 重复元素，不插入
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
        
        // 修复红黑树性质
        insertFixup(newNode);
    }
    
    /**
     * 插入后修复红黑树性质
     */
    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;  // 叔叔节点
                
                if (y.color == RED) {
                    // Case 1: 叔叔是红色
                    z.parent.color = BLACK;
                    y.color = BLACK;
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
                Node y = z.parent.parent.left;  // 叔叔节点
                
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }
    
    /**
     * 查找元素
     */
    public boolean search(T data) {
        Node current = root;
        while (current != NIL) {
            int cmp = data.compareTo(current.data);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 中序遍历
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
     * 打印树结构
     */
    public void printTree() {
        System.out.println("Red-Black Tree Structure:");
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
    
    /**
     * 验证是否满足红黑树性质
     */
    public boolean isValidRedBlackTree() {
        if (root.color != BLACK) {
            return false;
        }
        return checkBlackHeight(root) != -1 && !hasConsecutiveRed(root);
    }
    
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
    
    private boolean hasConsecutiveRed(Node node) {
        if (node == NIL) {
            return false;
        }
        
        if (node.color == RED) {
            if (node.left.color == RED || node.right.color == RED) {
                return true;
            }
        }
        
        return hasConsecutiveRed(node.left) || hasConsecutiveRed(node.right);
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}
