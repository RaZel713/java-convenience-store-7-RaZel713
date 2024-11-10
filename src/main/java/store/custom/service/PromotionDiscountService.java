package store.custom.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.custom.model.PromotionInfo;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotion;
import store.custom.model.promotion.Promotions;

public class PromotionDiscountService {
    private static final String BEFORE_PROMOTION_START = "프로모션 시작 전";
    private static final String AFTER_PROMOTION_END = "프로모션 종료 후";

    public void run(Products productCatalog, Promotions promotions, OrderSheet orderSheet) {
        for (OrderedProduct orderProduct : orderSheet.getOrderSheet()) {
            setProductPriceAndPromotion(orderProduct, productCatalog); // 가격과 프로모션 정보 옮기기
            setPromotionDetails(orderProduct, promotions); // 프로모션 관련 정보 옮기기 (buy,get,기한별)
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
            if (promotion != null) { // 프로모션이 있으면
                applyPromotionBuyAndGet(orderProduct, promotion);
                applyPromotionDates(orderProduct, today, promotion);
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

    private void applyPromotionBuyAndGet(OrderedProduct orderProduct, Promotion promotion) {
        orderProduct.setBuy(promotion.getBuy());
        orderProduct.setGet(promotion.getGet());
    }

    private void applyPromotionDates(OrderedProduct orderProduct, LocalDate today, Promotion promotion) {
        if (today.isBefore(LocalDate.parse(promotion.getStartDate()))) {
            orderProduct.setPromotion(BEFORE_PROMOTION_START);
        }

        if (today.isAfter(LocalDate.parse(promotion.getEndDate()))) {
            orderProduct.setPromotion(AFTER_PROMOTION_END);
        }
    }

    public List<List<Integer>> createPromotionResults(Products products, OrderSheet orderSheet) {
        List<List<Integer>> orderSheetPromotionResults = new ArrayList<>();

        for (OrderedProduct orderProduct : orderSheet.getOrderSheet()) { // 주문 목록의 제품 별로
            PromotionInfo promotionInfo = createPromotionInfo(products.getProducts(), orderProduct);
            List<Integer> result = calculateResult(orderProduct, promotionInfo);
            orderSheetPromotionResults.add(result);
        }
        return orderSheetPromotionResults;
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

    private List<Integer> calculateResult(OrderedProduct orderProduct, PromotionInfo promotionInfo) {
        if (promotionInfo.getRemainder() == -1) { // 프로모션이 없는 경우
            return Arrays.asList(-1, -1, -1);
        }
        if (promotionInfo.getRemainder() == 0) {
            return applyFullPromotionConditions(promotionInfo);
        }
        if (promotionInfo.getRemainder() < orderProduct.getBuy()) {
            return applyPartialPromotionConditions(promotionInfo);
        }
        return applyAdditionalPromotionConditions(promotionInfo);
    }

    private List<Integer> applyFullPromotionConditions(PromotionInfo promotionInfo) {
        if (promotionInfo.getOrderQuantity() > promotionInfo.getPromotionQuantity()) {
            int validPromotionCount = promotionInfo.getPromotionQuantity() / promotionInfo.getConditions();
            int excludedCount = (promotionInfo.getQuotient() - validPromotionCount) * promotionInfo.getConditions();
            return Arrays.asList(validPromotionCount, 0, excludedCount);
        }
        // if (promotionInfo.getOrderQuantity() <= promotionInfo.getPromotionQuantity())
        return Arrays.asList(promotionInfo.getQuotient(), 0, 0);
    }

    private List<Integer> applyPartialPromotionConditions(PromotionInfo promotionInfo) {
        if (promotionInfo.getConditions() * promotionInfo.getQuotient() > promotionInfo.getPromotionQuantity()) {
            int validPromotionCount = promotionInfo.getPromotionQuantity() / promotionInfo.getConditions();
            int excludedCount = (promotionInfo.getQuotient() - validPromotionCount) * promotionInfo.getConditions();
            return Arrays.asList(validPromotionCount, 0, excludedCount + promotionInfo.getRemainder());
        }
        return Arrays.asList(promotionInfo.getQuotient(), 0, promotionInfo.getRemainder());
    }

    private List<Integer> applyAdditionalPromotionConditions(PromotionInfo promotionInfo) {
        if (promotionInfo.getConditions() * (promotionInfo.getQuotient() + 1) > promotionInfo.getPromotionQuantity()) {
            applyPartialPromotionConditions(promotionInfo);
        }
        return Arrays.asList(promotionInfo.getQuotient(), 1, 0);
    }
}