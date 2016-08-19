package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 11.08.2016.
 */
public class DiscountEntity {
    private int id;
    private Integer categoryId;
    private String category;
    private String vendor;
    private int minPrice;
    private int maxPrice;
    private int discount1;
    private int discount2;
    private String articul;
    private String name;
    private String url;

    public DiscountEntity(int id, int categoryId, String category, String vendor, int priceFrom, int priceTo, int discount1, int discount2) {
        this.id = id;
        if (categoryId != 0) {
            this.categoryId = categoryId;
        }
        this.category = category;
        this.vendor = vendor;
        this.minPrice = priceFrom;
        this.maxPrice = priceTo;
        this.discount1 = discount1;
        this.discount2 = discount2;
    }

    public DiscountEntity(int id, String articul, String name, String vendor, int discount1, int discount2, String url) {
        this.id = id;
        this.articul = articul;
        this.name = name;
        this.vendor = vendor;
        this.url = url;
        this.discount1 = discount1;
        this.discount2 = discount2;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() { return categoryId; }

    public String getVendor() {
        return vendor;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getDiscount1() {
        return discount1;
    }

    public int getDiscount2() {
        return discount2;
    }

    public String getArticul() {
        return articul;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
