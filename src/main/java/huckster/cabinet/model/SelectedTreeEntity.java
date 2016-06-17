package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 17.06.2016.
 */
public class SelectedTreeEntity {
    int id;
    String title;
    int parentId;
    boolean isSelected;

    public SelectedTreeEntity(int id, String title, int parentId, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getParentId() {
        return parentId;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
