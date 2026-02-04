public class UnionFindAdvanced {
    //不使用size数组,使用负数形式,根是负数
    private int[] parent;

    public UnionFindAdvanced(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i += 1) {
            parent[i] = -1;
        }
    }

    public void validate(int v1) {
        if (v1 >= parent.length || v1 < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int sizeOf(int v1) {
        validate(v1);
        int root = find(v1);
        return -parent[root];
    }

    public int parent(int v1) {
        validate(v1);
        return parent[v1];
    }

    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    public int find(int v1) {
        validate(v1);
        if (parent[v1] < 0) {
            return v1;
        } else {
            parent[v1] = find(parent[v1]);
            return parent[v1];
        }
    }


    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);

        int root1 = find(v1);
        int root2 = find(v2);

        // 修正：如果已经是同一棵树，直接退出
        if (root1 == root2) return;

        if (sizeOf(root1) >= sizeOf(root2)) {
            parent[root1] += parent[root2];
            parent[root2] = root1;
        } else {
            parent[root2] += parent[root1];
            parent[root1] = root2;
        }
    }
}