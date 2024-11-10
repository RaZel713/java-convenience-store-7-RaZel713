package store.custom.service.editor;

import static store.custom.constants.StringConstants.AFTER_PROMOTION_END;
import static store.custom.constants.StringConstants.BEFORE_PROMOTION_START;
import static store.custom.constants.StringConstants.RESPONSE_NO;
import static store.custom.constants.StringConstants.RESPONSE_YES;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import store.custom.model.PromotionResult.PromotionResult;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotion;
import store.custom.model.promotion.Promotions;

public class OrderSheetEditor {
    public void addPromotionInfo(Products productCatalog, Promotions promotions, OrderSheet orderSheet) {
        for (OrderedProduct orderProduct : orderSheet.getOrderSheet()) {
            setProductPriceAndPromotion(orderProduct, productCatalog); // 가격과 프로모션 추가
            setPromotionDetails(orderProduct, promotions); // 프로모션 관련 정보 추가
        }
    }

    private void setProductPriceAndPromotion(OrderedProduct orderProduct, Products productCatalog) {
        Product product = findOrderedProduct(orderProduct, productCatalog);
        if (product != null) {
            orderProduct.setTotalPrice(product.getPrice());
            setPromotionIfExist(orderProduct, product);
        }
    }

    private Product findOrderedProduct(OrderedProduct orderProduct, Products productCatalog) {
        for (Product product : productCatalog.getProducts()) {
            if (product.getName().equals(orderProduct.getName())) {
                return product;
            }
        } // 이름이 같은 제품은 프로모션 있는 순 -> 프로모션 없는 순
        return null;
    }

    private void setPromotionIfExist(OrderedProduct orderProduct, Product product) {
        if (product.getPromotion() != null) {
            orderProduct.setPromotion(product.getPromotion());
        }
    }

    private void setPromotionDetails(OrderedProduct orderProduct, Promotions promotions) {
        if (orderProduct.getPromotion() != null) {
            LocalDate today = DateTimes.now().toLocalDate();
            Promotion promotion = findPromotion(orderProduct.getPromotion(), promotions);
            if (promotion != null) {
                applyPromotionDates(orderProduct, today, promotion);
                applyPromotionBuyAndGet(orderProduct, promotion);
            }
        }
    }

    private Promotion findPromotion(String promotionName, Promotions promotions) {
        for (Promotion promotion : promotions.getPromotions()) {
            if (promotion.getName().equals(promotionName)) {
                return promotion;
            }
        }
        return null;
    }

    private void applyPromotionDates(OrderedProduct orderProduct, LocalDate today, Promotion promotion) {
        if (today.isBefore(LocalDate.parse(promotion.getStartDate()))) {
            orderProduct.setPromotion(BEFORE_PROMOTION_START);
        }

        if (today.isAfter(LocalDate.parse(promotion.getEndDate()))) {
            orderProduct.setPromotion(AFTER_PROMOTION_END);
        }
    }

    private void applyPromotionBuyAndGet(OrderedProduct orderProduct, Promotion promotion) {
        String orderProductPromotion = orderProduct.getPromotion();

        if (orderProductPromotion != null && !orderProductPromotion.equals(BEFORE_PROMOTION_START)
                && !orderProductPromotion.equals(AFTER_PROMOTION_END)) {
            orderProduct.setBuy(promotion.getBuy());
            orderProduct.setGet(promotion.getGet());
        }
    }

    // 프로모션 결과

    public void applyResponseForNoPromotion(String response, PromotionResult promotionResult,
                                            OrderedProduct orderedProduct) {
        if (response.equals(RESPONSE_YES)) {
            processPurchaseWithNoDiscount(orderedProduct, promotionResult);
        }

        if (response.equals(RESPONSE_NO)) {
            processNonPurchaseWithNoDiscount(orderedProduct, promotionResult);
        }
    }

    private void processPurchaseWithNoDiscount(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        orderedProduct.setBuy(
                orderedProduct.getBuy() * promotionResult.getPromotionAppliedCount()
                        + promotionResult.getNonPromotionProductCount());
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.getPromotionAppliedCount());
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity());
    }

    private void processNonPurchaseWithNoDiscount(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * promotionResult.getPromotionAppliedCount());
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.getPromotionAppliedCount());
        orderedProduct.setQuantity(orderedProduct.getQuantity() - promotionResult.getNonPromotionProductCount());
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity());
    }

    public void applyResponseForFreeProduct(String response, PromotionResult promotionResult,
                                            OrderedProduct orderedProduct) {
        if (response.equals(RESPONSE_YES)) {
            processAdditionalPromotionApplied(orderedProduct, promotionResult);
        }
        if (response.equals(RESPONSE_NO)) {
            processAdditionalPromotionNotApplied(orderedProduct, promotionResult);
        }
    }

    private void processAdditionalPromotionApplied(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * (promotionResult.getPromotionAppliedCount() + 1));
        orderedProduct.setQuantity(orderedProduct.getQuantity() + orderedProduct.getGet());
        orderedProduct.setGet(orderedProduct.getGet() * (promotionResult.getPromotionAppliedCount() + 1));
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity());
    }

    private void processAdditionalPromotionNotApplied(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * (promotionResult.getPromotionAppliedCount() + 1));
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.getPromotionAppliedCount());
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity());
    }

    public void computeTotalWithoutPromotion(OrderedProduct orderedProduct) {
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity()); // 총가격
    }

    public void computeTotalWithPromotion(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        orderedProduct.setBuy(orderedProduct.getBuy() * promotionResult.getPromotionAppliedCount());
        orderedProduct.setGet(orderedProduct.getGet() * promotionResult.getPromotionAppliedCount());
        orderedProduct.setTotalPrice(orderedProduct.getTotalPrice() * orderedProduct.getQuantity());
    }
}