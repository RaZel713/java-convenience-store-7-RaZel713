package store.custom.service;

import java.util.Arrays;
import java.util.List;
import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;

public class ReceiptDetailsCalculationService {
    public static List<Integer> run(OrderSheet orderSheet) {
        int totalPrice = 0;
        int totalQuantity = 0;
        int promotionDiscount = 0;

        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            totalQuantity += orderedProduct.getQuantity();
            totalPrice += calculateTotalPrice(orderedProduct);
            promotionDiscount = calculatePromotionDiscount(orderedProduct, promotionDiscount);
        }
        return Arrays.asList(totalQuantity, totalPrice, promotionDiscount);
    }

    private static int calculateTotalPrice(OrderedProduct orderedProduct) {
        if (orderedProduct.getBuy() == 0) {
            return calculateNonPromotionPrice(orderedProduct);
        }
        return calculatePromotionPrice(orderedProduct);
    }

    private static int calculatePromotionPrice(OrderedProduct orderedProduct) {
        int productPrice = orderedProduct.getTotalPrice() / orderedProduct.getBuy();
        return productPrice * orderedProduct.getQuantity();
    }

    private static int calculateNonPromotionPrice(OrderedProduct orderedProduct) {
        return orderedProduct.getTotalPrice();
    }

    private static int calculatePromotionDiscount(OrderedProduct orderedProduct, int promotionDiscount) {
        if (orderedProduct.getBuy() != 0) {
            int productPrice = orderedProduct.getTotalPrice() / orderedProduct.getBuy();
            return promotionDiscount + productPrice * orderedProduct.getGet();
        }
        return promotionDiscount;
    }
}