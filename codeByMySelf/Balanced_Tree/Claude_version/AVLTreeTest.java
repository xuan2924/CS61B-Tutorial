package Balanced_Tree.Claude_version;

/**
 * AVL树测试类
 * 演示所有功能的使用
 */
public class AVLTreeTest {
    
    public static void main(String[] args) {
        System.out.println("=== AVL树完整功能测试 ===\n");
        
        // 创建AVL树
        AVLTree<Integer> tree = new AVLTree<>();
        
        // 测试1: 插入操作
        System.out.println("1. 测试插入操作");
        System.out.println("插入元素: 10, 20, 30, 40, 50, 25");
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        tree.insert(25);
        
        System.out.println("树的大小: " + tree.size());
        System.out.println("树的高度: " + tree.getHeight());
        tree.printTree();
        System.out.println("是否是AVL树: " + tree.isAVL());
        System.out.println();
        
        // 测试2: 各种遍历
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
        System.out.println("查找50: " + tree.search(50));
        System.out.println();
        
        // 测试4: 删除操作
        System.out.println("4. 测试删除操作");
        System.out.println("删除40");
        tree.delete(40);
        tree.printTree();
        System.out.println("是否是AVL树: " + tree.isAVL());
        tree.inorderTraversal();
        System.out.println();
        
        System.out.println("删除30");
        tree.delete(30);
        tree.printTree();
        System.out.println("是否是AVL树: " + tree.isAVL());
        tree.inorderTraversal();
        System.out.println();
        
        // 测试5: 大量插入测试
        System.out.println("5. 测试大量插入（1-100）");
        AVLTree<Integer> bigTree = new AVLTree<>();
        for (int i = 1; i <= 100; i++) {
            bigTree.insert(i);
        }
        System.out.println("树的大小: " + bigTree.size());
        System.out.println("树的高度: " + bigTree.getHeight());
        System.out.println("是否是AVL树: " + bigTree.isAVL());
        System.out.println();
        
        // 测试6: 随机插入测试
        System.out.println("6. 测试随机插入");
        AVLTree<Integer> randomTree = new AVLTree<>();
        int[] randomValues = {15, 8, 23, 4, 12, 19, 27, 2, 6, 10, 14, 17, 21, 25, 30};
        System.out.print("插入顺序: ");
        for (int val : randomValues) {
            System.out.print(val + " ");
            randomTree.insert(val);
        }
        System.out.println();
        randomTree.printTree();
        System.out.println("是否是AVL树: " + randomTree.isAVL());
        randomTree.inorderTraversal();
        System.out.println();
        
        // 测试7: 字符串类型的AVL树
        System.out.println("7. 测试字符串类型AVL树");
        AVLTree<String> stringTree = new AVLTree<>();
        String[] words = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};
        for (String word : words) {
            stringTree.insert(word);
        }
        stringTree.printTree();
        stringTree.inorderTraversal();
        System.out.println("查找 'cherry': " + stringTree.search("cherry"));
        System.out.println("查找 'orange': " + stringTree.search("orange"));
        System.out.println();
        
        // 测试8: 边界情况
        System.out.println("8. 测试边界情况");
        AVLTree<Integer> edgeTree = new AVLTree<>();
        System.out.println("空树搜索: " + edgeTree.search(1));
        System.out.println("空树大小: " + edgeTree.size());
        System.out.println("空树高度: " + edgeTree.getHeight());
        
        edgeTree.insert(5);
        System.out.println("单节点树高度: " + edgeTree.getHeight());
        edgeTree.delete(5);
        System.out.println("删除后大小: " + edgeTree.size());
        System.out.println();
        
        // 测试9: 重复插入
        System.out.println("9. 测试重复插入");
        AVLTree<Integer> dupTree = new AVLTree<>();
        dupTree.insert(10);
        dupTree.insert(10);
        dupTree.insert(10);
        System.out.println("插入3次10后的大小: " + dupTree.size());
        dupTree.inorderTraversal();
        System.out.println();
        
        // 测试10: 清空操作
        System.out.println("10. 测试清空操作");
        System.out.println("清空前大小: " + tree.size());
        tree.clear();
        System.out.println("清空后大小: " + tree.size());
        System.out.println("清空后是否为空: " + tree.isEmpty());
        
        System.out.println("\n=== 所有测试完成 ===");
    }
}
