package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class OrderEntity {
    private int id;
    private int ruleId;
    private String articul;
    private String vendorCode;
    private String model;
    private Double basePrice;
    private Double resultPrice;
    private int discount;
    private String phone;
    private String city;
    private String creationDate;
    private String phrase;
    private int status;
    private String statusTitle;
    private String comment;

    public OrderEntity(int id, int ruleId, String articul, String vendorCode, String model, Double basePrice, Double resultPrice, int discount, String phone, String city, String creationDate, String phrase, int status, String statusTitle, String comment) {
        this.id = id;
        this.ruleId = ruleId;
        this.articul = articul;
        this.vendorCode = vendorCode;
        this.model = model;
        this.basePrice = basePrice;
        this.resultPrice = resultPrice;
        this.discount = discount;
        this.phone = phone;
        this.city = city;
        this.creationDate = creationDate;
        this.phrase = phrase;
        this.status = status;
        this.statusTitle = statusTitle;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public int getRuleId() {
        return ruleId;
    }

    public String getArticul() {
        return articul;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getModel() {
        return model;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public Double getResultPrice() {
        return resultPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getPhrase() {
        return phrase;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public String getComment() {
        return comment;
    }
}