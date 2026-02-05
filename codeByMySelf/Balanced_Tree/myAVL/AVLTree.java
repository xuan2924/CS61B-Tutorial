package Balanced_Tree.myAVL;

import java.util.LinkedList;

public class AVLTree<T extends Comparable<T>> {

    private Node root;
    private int size;

    private class Node {
        int height;
        T data;
        Node left, right;

        public Node(T data) {
            this.data = data;
            this.height = 1;
        }
    }

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    /*======查询操作======*/

    //Finished:获取树中元素个数
    public int size() {
        return size;
    }

    //Finished:判断是否为空
    public boolean isEmpty() {
        return this.size == 0;
    }

    //Finished:获取树的高度
    public int getHeight() {
        return height(root);
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    //Finished:验证是否满足AVL树的性质
    public boolean isAVL() {
        return isAVLHelper(root);
    }

    //Finished:辅助函数：计算平衡因子
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private boolean isAVLHelper(Node n) {
        if (n == null) {
            return true;
        }
        int balance = getBalance(n);
        if (Math.abs(balance) > 1) {
            return false;
        }
        return isAVLHelper(n.left) && isAVLHelper(n.right);
    }

    /*====基本操作===*/

    //Finished:左旋
    private Node rotateLeft(Node n) {
        Node x = n.right;
        Node y = x.left;

        //执行旋转
        x.left = n;
        n.right = y;

        //更新高度
        //更新顺序：先更新子节点，再更新父节点
        updateHeight(n);
        updateHeight(x);

        //返回新的根节点
        return x;
    }

    //Finished:右旋
    private Node rotateRight(Node n) {
        Node x = n.left;
        Node y = x.right;

        //执行右旋
        x.right = n;
        n.left = y;

        //更新高度
        //更新顺序：先更新子节点，再更新父节点
        updateHeight(n);
        updateHeight(x);

        //返回新的根节点
        return x;
    }

    //Finished:更新高度
    private void updateHeight(Node n) {
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
    }

    //Finished:插入元素并自动平衡
    public void insert(T data) {
        root = insertHelper(root, data);

    }

    public Node insertHelper(Node n, T data) {
        //1.标准BST插入
        if (n == null) {
            size += 1;
            return new Node(data);
        }
        int cmp = n.data.compareTo(data);
        if (cmp > 0) {
            n.left = insertHelper(n.left, data);
        } else if (cmp < 0) {
            n.right = insertHelper(n.right, data);
        } else {
            //重复元素，不插入
            return n;
        }

        //2.更新当前节点高度
        updateHeight(n);

        //3.获取平衡因子
        int balance = getBalance(n);

        //4.不平衡，分四种情况处理

        //data是插入的节点是=数据
        //如LL的情况下，data.compareTo(n.left.data) < 0说明插入的是左节点左边，进而导致的平衡失败
        //可以使用compareTo的方法,但是为何和后面删除方法的实现统一，使用getBalance
        //注意删除操作会有balance==0的情况

        //Left Left Case
        if (balance > 1 && getBalance(n.left) >= 0) {
            return rotateRight(n);
        }

        //Right Right Case
        if (balance < -1 && getBalance(n.right) <= 0) {
            return rotateLeft(n);
        }

        //Left Right Case
        if (balance > 1 && getBalance(n.left) < 0) {
            n.left = rotateLeft(n.left);
            return rotateRight(n);
        }

        //Right Left Case
        if (balance < -1 && getBalance(n.right) > 0) {
            n.right = rotateRight(n.right);
            return rotateLeft(n);
        }

        return n;
    }

    //Finished:删除元素并自动平衡
    public void delete(T data) {
        root = deleteHelper(root, data);
    }

    private Node deleteHelper(Node n, T data) {
        //1.标准的BST删除
        if (n == null) {
            return null;
        }

        int cmp = n.data.compareTo(data);

        if (cmp > 0) {
            n.left = deleteHelper(n.left, data);
        } else if (cmp < 0) {
            n.right = deleteHelper(n.right, data);
        } else {
            size -= 1;
            if (n.left == null) {
                return n.right;
            }
            if (n.right == null) {
                return n.left;
            }

            //普通的BST树使用deleteMin操作，可以性能优化一些
            //但是AVL和红黑树不同，删除还有调整旋转保持平衡，为了代码不复用，使用deleteHelper
            Node temp = findMin(n.right);
            n.data = temp.data;
            n.right = deleteHelper(n.right, temp.data);
        }

        // 在删除后的回溯阶段
        if (n == null) return null; // 如果删完后当前子树空了，直接返回

        //2.更新当前节点高度
        updateHeight(n);

        //3.获取平衡因子
        int balance = getBalance(n);

        //4.如果不平衡，分四种情况处理

        //Left Left Case :删除的节点在左子树的右子树上，删除后形成LL形

        if (balance > 1 && getBalance(n.left) >= 0) {
            return rotateRight(n);
        }

        //Left Right Case
        if (balance > 1 && getBalance(n.left) < 0) {
            n.left = rotateLeft(n.left);
            return rotateRight(n);
        }

        //Right Right Case
        if (balance < -1 && getBalance(n.right) <= 0) {
            return rotateLeft(n);
        }

        //Right Left Case
        if (balance < -1 && getBalance(n.right) > 0) {
            n.right = rotateRight(n.right);
            return rotateLeft(n);
        }

        return n;
    }

    private Node findMin(Node n) {
        if (n == null) {
            return null;
        }
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }


    //Finished:查找元素
    public boolean search(T data) {
        return searchHelper(root, data);
    }

    private boolean searchHelper(Node n, T data) {
        if (n == null) {
            return false;
        }
        int cmp = n.data.compareTo(data);
        if (cmp > 0) {
            return searchHelper(n.left, data);
        } else if (cmp < 0) {
            return searchHelper(n.right, data);
        } else {
            return true;
        }
    }

    //Finished:清空树
    public void clear() {
        root = null;
        size = 0;
    }

    /*======遍历操作======*/

    //Finished:中序遍历
    public void inorderTraversal() {
        System.out.print("Inorder : ");
        inorderHelper(root);
        System.out.println();
    }

    private void inorderHelper(Node n) {
        if (n != null) {
            inorderHelper(n.left);
            System.out.print(n.data + " ");
            inorderHelper(n.right);
        }
    }

    //Finished：前序遍历
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

    //Finished: 后序遍历
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

    //Finished：层序遍历
    public void levelOrderTraversal() {
        System.out.println("levelOrder: ");
        if (root == null) {
            System.out.println();
            return;
        }

        //Queue是一个接口，不可以直接new
        java.util.Queue<Node> queue = new LinkedList<>();

        //offer,poll更安全 add，remove会直接抛出异常
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            System.out.print(curr.data + " ");

            if (curr.left != null) {
                queue.offer(curr.left);
            }
            if (curr.right != null) {
                queue.offer(curr.right);
            }

        }

        System.out.println();
    }
}