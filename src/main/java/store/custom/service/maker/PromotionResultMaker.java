package store.custom.service.maker;

import static store.custom.constants.StringConstants.AFTER_PROMOTION_END;
import static store.custom.constants.StringConstants.BEFORE_PROMOTION_START;

import java.util.ArrayList;
import java.util.List;
import store.custom.model.PromotionInfo;
import store.custom.model.PromotionResult.PromotionResult;
import store.custom.model.PromotionResult.PromotionResults;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class PromotionResultMaker {
    public PromotionResults createPromotionResults(Products products, OrderSheet orderSheet) {
        List<PromotionResult> results = new ArrayList<>();

        for (OrderedProduct orderProduct : orderSheet.getOrderSheet()) { // 주문 목록의 제품 별로
            PromotionInfo promotionInfo = createPromotionInfo(products.getProducts(), orderProduct);
            PromotionResult result = calculateResult(orderProduct, promotionInfo);
            results.add(result);
        }
        return new PromotionResults(results);
    }

    private PromotionInfo createPromotionInfo(List<Product> products, OrderedProduct orderProduct) {
        int promotionQuantity = quantityWithPromotion(products, orderProduct.getName());
        int orderQuantity = orderProduct.getQuantity();
        if (isPromotionNull(orderProduct)) {
            return new PromotionInfo(promotionQuantity, -1, orderQuantity, -1, -1);
        }
        int promotionConditions = orderProduct.getBuy() + orderProduct.getGet();
        return new PromotionInfo(promotionQuantity, promotionConditions, orderQuantity,
                orderQuantity / promotionConditions, orderQuantity % promotionConditions);
    }

    private int quantityWithPromotion(List<Product> products, String productName) {
        int quantity = 0;
        for (Product product : products) {
            if (product.getName().equals(productName) && product.getPromotion() != null) {
                quantity = product.getQuantity();
            }
        }
        return quantity;
    }

    private boolean isPromotionNull(OrderedProduct orderProduct) {
        String promotion = orderProduct.getPromotion();
        return (promotion == null || promotion.equals(BEFORE_PROMOTION_START) || promotion.equals(AFTER_PROMOTION_END));
    }

    private PromotionResult calculateResult(OrderedProduct orderProduct, PromotionInfo promotionInfo) {
        if (promotionInfo.getRemainder() == -1) { // 프로모션이 없는 경우
            return new PromotionResult(-1, -1, -1);
        }
        if (promotionInfo.getRemainder() == 0) {
            return applyFullPromotionConditions(promotionInfo);
        }
        if (promotionInfo.getRemainder() < orderProduct.getBuy()) {
            return applyPartialPromotionConditions(promotionInfo);
        }
        return applyAdditionalPromotionConditions(promotionInfo);
    }

    private PromotionResult applyFullPromotionConditions(PromotionInfo promotionInfo) {
        if (promotionInfo.getOrderQuantity() > promotionInfo.getPromotionQuantity()) {
            int validPromotionCount = promotionInfo.getPromotionQuantity() / promotionInfo.getConditions();
            int excludedCount = (promotionInfo.getQuotient() - validPromotionCount) * promotionInfo.getConditions();
            return new PromotionResult(validPromotionCount, 0, excludedCount);
        }
        // if (promotionInfo.getOrderQuantity() <= promotionInfo.getPromotionQuantity())
        return new PromotionResult(promotionInfo.getQuotient(), 0, 0);
    }

    private PromotionResult applyPartialPromotionConditions(PromotionInfo promotionInfo) {
        if (promotionInfo.getConditions() * promotionInfo.getQuotient() > promotionInfo.getPromotionQuantity()) {
            int validPromotionCount = promotionInfo.getPromotionQuantity() / promotionInfo.getConditions();
            int excludedCount = (promotionInfo.getQuotient() - validPromotionCount) * promotionInfo.getConditions();
            return new PromotionResult(validPromotionCount, 0, excludedCount + promotionInfo.getRemainder());
        }
        return new PromotionResult(promotionInfo.getQuotient(), 0, promotionInfo.getRemainder());
    }

    private PromotionResult applyAdditionalPromotionConditions(PromotionInfo promotionInfo) {
        if (promotionInfo.getConditions() * (promotionInfo.getQuotient() + 1) > promotionInfo.getPromotionQuantity()) {
            applyPartialPromotionConditions(promotionInfo);
        }
        return new PromotionResult(promotionInfo.getQuotient(), 1, 0);
    }
}