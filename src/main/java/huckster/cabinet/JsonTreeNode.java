package huckster.cabinet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PerevalovaMA on 01.06.2016.
 */
class JsonTreeNode {
    private int key;
    private int parent;
    private String title;
    private boolean selected;
    private List<JsonTreeNode> children;

    public JsonTreeNode(int key, String title, int parent, boolean selected, List<JsonTreeNode> children) {
        this.key = key;
        this.title = title;
        this.parent = parent;
        this.children = children;
        this.selected = selected;
    }

    public Integer getKey() {
        return key;
    }

    public Integer getParent() {
        return parent;
    }

    public String getTitle() {
        return title;
    }

    public boolean getSelected() {
        return selected;
    }

    public List<JsonTreeNode> getChildren() {
        return children;
    }

    void addChild(JsonTreeNode node) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(node);
    }
}
