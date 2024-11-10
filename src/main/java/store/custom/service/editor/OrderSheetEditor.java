package store.custom.service.editor;

import java.util.List;
import store.custom.model.order.OrderedProduct;

public class OrderSheetEditor {
    public void applyResponseForNoPromotion(String response, List<Integer> promotionResult,
                                            OrderedProduct orderedProduct) {
        if (response.equals("Y")) {
            processPurchaseWithNoDiscount(orderedProduct, promotionResult);
        }

        if (response.equals("N")) {
            processNonPurchaseWithNoDiscount(orderedProduct, promotionResult);
        }
    }

    private void processPurchaseWithNoDiscount(OrderedProduct orderedProduct, List<Integer> promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * promotionResult.get(0) + promotionResult.get(2));
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.get(0));
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getBuy());
    }

    private void processNonPurchaseWithNoDiscount(OrderedProduct orderedProduct, List<Integer> promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * promotionResult.get(0));
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.get(0));
        orderedProduct.setQuantity(orderedProduct.getQuantity() - promotionResult.get(2));
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getBuy());
    }


    public void applyResponseForFreeProduct(String response, List<Integer> promotionResult,
                                            OrderedProduct orderedProduct) {
        if (response.equals("Y")) {
            processAdditionalPromotionApplied(orderedProduct, promotionResult);
        }
        if (response.equals("N")) {
            processAdditionalPromotionNotApplied(orderedProduct, promotionResult);
        }
    }

    private void processAdditionalPromotionApplied(OrderedProduct orderedProduct, List<Integer> promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * (promotionResult.get(0) + 1));
        orderedProduct.setQuantity(orderedProduct.getQuantity() + orderedProduct.getGet());
        orderedProduct.setGet(orderedProduct.getGet() * (promotionResult.get(0) + 1));
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getBuy());
    }

    private void processAdditionalPromotionNotApplied(OrderedProduct orderedProduct, List<Integer> promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * (promotionResult.get(0) + 1));
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.get(0));
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getBuy());
    }


    public void computeTotalWithoutPromotion(OrderedProduct orderedProduct) {
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity()); // 총가격
    }
}