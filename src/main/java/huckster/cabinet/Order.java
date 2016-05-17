package huckster.cabinet;

/**
 * Created by PerevalovaMA on 17.05.2016.
 */
public class Order {
    private int id;
    private int ruleId;
    private String articul;
    private String vendorCode;
    private String model;
    private double priceBase;
    private double priceResult;
    private int discount;
    private String phone;
    private String city;
    private String date;
    private String phrase;
    private String status;
    private String comment;

    public void setId(int id) {
        this.id = id;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPriceBase(double priceBase) {
        this.priceBase = priceBase;
    }

    public void setPriceResult(double priceResult) {
        this.priceResult = priceResult;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setComment(String comment) {
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

    public double getPriceBase() {
        return priceBase;
    }

    public double getPriceResult() {
        return priceResult;
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

    public String getDate() {
        return date;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }
}
