package huckster.cabinet;

/**
 * Created by Perevalova Marina on 10.05.2016.
 */
public class MenuItem {
    private String link;
    private String name;
    private String icon;

    MenuItem(String name, String link, String icon) {
        this.link = link;
        this.name = name;
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}