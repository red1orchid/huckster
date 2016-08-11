package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 11.08.2016.
 */
public class DiscountEntity {
    private int id;
    private String category;
    private String vendor;
    private int minPrice;
    private int maxPrice;
    private int discount1;
    private int discount2;

    public DiscountEntity(int id, String category, String vendor, int priceFrom, int priceTo, int discount1, int discount2) {
        this.id = id;
        this.category = category;
        this.vendor = vendor;
        this.minPrice = priceFrom;
        this.maxPrice = priceTo;
        this.discount1 = discount1;
        this.discount2 = discount2;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

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
}
