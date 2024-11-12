package store.custom.model;

public class PromotionInfo {
    private final int promotionQuantity;
    private final int promotionConditions;
    private final int orderQuantity;
    private final int quotient;
    private final int remainder;

    public PromotionInfo(int promotionQuantity, int promotionConditions, int orderQuantity,
                         int quotient, int remainder) {
        this.promotionQuantity = promotionQuantity;
        this.promotionConditions = promotionConditions;
        this.orderQuantity = orderQuantity;
        this.quotient = quotient;
        this.remainder = remainder;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public int getConditions() {
        return promotionConditions;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public int getQuotient() {
        return quotient;
    }

    public int getRemainder() {
        return remainder;
    }
}