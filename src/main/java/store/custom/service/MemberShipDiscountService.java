package store.custom.service;

import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;

public class MemberShipDiscountService {
    public int run(String response, OrderSheet orderSheet) {
        int totalPrice = calculateTotalPrice(orderSheet);

        if (response.equals("Y")) {
            return totalPrice - calculateDiscountPrice(totalPrice);
        }

        return totalPrice;
    }

    private int calculateTotalPrice(OrderSheet orderSheet) {
        int totalPrice = 0;

        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            totalPrice += orderedProduct.getTotalPrice();
        }

        return totalPrice;
    }

    private int calculateDiscountPrice(int totalPrice) {
        int discountPrice = totalPrice / 100 * 30; // 30프로
        if (discountPrice < 8000) {
            return discountPrice;
        }
        return 8000;
    }
}