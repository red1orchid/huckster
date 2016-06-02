package huckster.cabinet;

/**
 * Created by PerevalovaMA on 01.06.2016.
 */
class TreeNode {
    private int id;
    private int parentId;
    private int level;
    private String data;

    TreeNode(int id, int parentId, int level, String data) {
        this.id = id;
        this.parentId = parentId;
        this.level = level;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public int getLevel() {
        return level;
    }

    public String getData() {
        return data;
    }
}
