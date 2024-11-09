package store.custom.service;

import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;

public class MemberShipDiscountService {
    public int run(String response, OrderSheet orderSheet) {
        int nonPromotionProductPrice = calculateNonPromotionProductPrice(orderSheet);

        if (response.equals("Y")) {
            return calculateMembershipDiscount(nonPromotionProductPrice);
        }

        return 0;
    }

    private int calculateNonPromotionProductPrice(OrderSheet orderSheet) {
        int nonPromotionProductPrice = 0;

        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getPromotion() == null) {
                nonPromotionProductPrice += orderedProduct.getTotalPrice();
            }
        }

        return nonPromotionProductPrice;
    }

    private int calculateMembershipDiscount(int nonPromotionProductPrice) {
        int discountPrice = nonPromotionProductPrice / 100 * 30; // 30프로
        if (discountPrice < 8000) {
            return discountPrice;
        }
        return 8000;
    }
}