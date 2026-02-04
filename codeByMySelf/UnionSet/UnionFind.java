package UnionSet;

public class UnionFind {
    private int[] parent;
    private int[] size; //记录以 i 为根的节点数
    private int number;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        number = n;
        for (int i = 0; i < n; i += 1) {
            parent[i] = i;
            size[i] = 1;

        }
    }

    public void validate(int v1) {
        if (v1 >= number || v1 < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int sizeOf(int v1) {
        validate(v1);
        return size[find(v1)];
    }

    public int parent(int v1) {
        if (parent[v1] == v1) {
            return -number;
        } else {
            return parent[v1];
        }
    }

    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    public int find(int v1) {
        //确保数据合理
        validate(v1);
        int root = v1;
        while (parent[root] != root) {
            root = parent[root];
        }

        //路径压缩
        /*错误：
        while (parent[v1] != root) {
            parent[v1] = root;
            v1 = parent[root];
        }
         */
        while (v1 != root) {
            int next = parent[v1];
            parent[v1] = root;
            v1 = next;
        }
        return root;
    }

    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);

        /*
        直接操作，没有改变沿途的数据
         if (sizeOf(v1) >= sizeOf(v2)) {
            parent[v2] = v1;
        } else {
            parent[v1] = v2;
        }
         */
        int root1 = find(v1);
        int root2 = find(v2);

        if (root1 == root2) return; // 已经在同一个集合

        if(sizeOf(root1) >= sizeOf(root2)){
            parent[root2] = root1;
            size[root1] += size[root2];
        }else{
            parent[root1] = root2;
            size[root2] += size[root1];
        }
    }
}