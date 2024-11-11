package store.custom.service.maker;

import static store.custom.constants.NumberConstants.MEMBERSHIP_DISCOUNT_LIMIT;
import static store.custom.constants.NumberConstants.MEMBERSHIP_DISCOUNT_RATE;
import static store.custom.constants.StringConstants.RESPONSE_NO;

import store.custom.model.ReceiptDetails;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;

public class ReceiptDetailsMaker {
    public ReceiptDetails run(OrderSheet orderSheet, String response) {
        int totalQuantity = calculateTotalQuantity(orderSheet);
        int nonPromotionalProductsPrice = calculateNonPromotionalProductsPrice(orderSheet);
        int promotionalProductsPrice = calculatePromotionalProductsPrice(orderSheet);
        int totalPrice = nonPromotionalProductsPrice + promotionalProductsPrice;
        int promotionDiscount = calculatePromotionDiscount(orderSheet);
        int membershipDiscount = calculateMembershipDiscount(nonPromotionalProductsPrice, response);

        return new ReceiptDetails(totalQuantity, totalPrice, promotionDiscount, membershipDiscount);
    }

    private int calculateTotalQuantity(OrderSheet orderSheet) {
        int quantity = 0;
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            quantity += orderedProduct.getQuantity();
        }
        return quantity;
    }

    private int calculateNonPromotionalProductsPrice(OrderSheet orderSheet) {
        int price = 0;
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getBuy() == 0) { // 프로모션 없을 때
                price += orderedProduct.getTotalPrice();
            }
        }
        return price;
    }

    private int calculatePromotionalProductsPrice(OrderSheet orderSheet) {
        int price = 0;
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getBuy() != 0) { // 프로모션 있을 때
                price += orderedProduct.getTotalPrice();
            }
        }
        return price;
    }

    public int calculatePromotionDiscount(OrderSheet orderSheet) {
        int price = 0;
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getBuy() != 0) { // 프로모션 있을 때
                int productPrice = orderedProduct.getTotalPrice() / orderedProduct.getQuantity();
                price += productPrice * orderedProduct.getGet();
            }
        }
        return price;
    }

    private int calculateMembershipDiscount(int nonPromotionProductPrice, String response) {
        if (response.equals(RESPONSE_NO)) {
            return 0;
        }
        int discountPrice = nonPromotionProductPrice * MEMBERSHIP_DISCOUNT_RATE / 100;
        return Math.min(discountPrice, MEMBERSHIP_DISCOUNT_LIMIT);
    }
}