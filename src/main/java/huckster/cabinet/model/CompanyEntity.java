package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 17.06.2016.
 */
public class CompanyEntity {
    String name;
    String currency;

    public CompanyEntity(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }
}
