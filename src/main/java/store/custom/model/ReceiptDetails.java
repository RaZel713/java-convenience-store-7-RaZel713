package store.custom.model;

public class ReceiptDetails {
    private final int totalQuantity;
    private final int totalPrice;

    private final int promotionDiscount;
    private final int membershipDiscount;

    private final int finalPrice;

    public ReceiptDetails(int totalQuantity, int totalPrice, int promotionDiscount, int membershipDiscount) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;

        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalPrice = totalPrice - promotionDiscount - membershipDiscount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}