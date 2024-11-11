package store.custom.model.order;

public class OrderedProduct {
    private final String name;
    private int quantity;
    private int totalPrice;
    private String promotion;
    private int buy;
    private int get;

    public OrderedProduct(String name, int quantity, int totalPrice, String promotion, int buy, int get) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.promotion = promotion;
        this.buy = buy;
        this.get = get;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getGet() {
        return get;
    }

    public void setGet(int get) {
        this.get = get;
    }
}