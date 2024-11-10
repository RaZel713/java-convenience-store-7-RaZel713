package store.custom.controller;

import static store.custom.constants.StringConstants.PRODUCTS_FILE_PATH;
import static store.custom.constants.StringConstants.PROMOTIONS_FILE_PATH;

import java.util.List;
import store.custom.model.OrderSheet;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotions;
import store.custom.service.FileReader;
import store.custom.service.MemberShipDiscountService;
import store.custom.service.OrderSheetEditService;
import store.custom.service.OrderSheetMakingService;
import store.custom.service.ProductCatalogEditor;
import store.custom.service.ProductParser;
import store.custom.service.PromotionDiscountService;
import store.custom.service.PromotionParser;
import store.custom.service.ReceiptDetailsCalculationService;
import store.custom.service.ResponseParsingService;
import store.custom.view.InputView;
import store.custom.view.OutputView;

public class StoreController {
    private final MemberShipDiscountService memberShipDiscountService;
    private final OrderSheetEditService orderSheetEditService;
    private final OrderSheetMakingService orderSheetMakingService;
    private final PromotionDiscountService promotionDiscountService;
    private final ResponseParsingService responseParsingService;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.memberShipDiscountService = new MemberShipDiscountService();
        this.orderSheetEditService = new OrderSheetEditService();
        this.orderSheetMakingService = new OrderSheetMakingService();
        this.promotionDiscountService = new PromotionDiscountService();
        this.responseParsingService = new ResponseParsingService();

        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start() {
        Products productCatalog = setUpProductCatalog();
        Promotions promotionCatalog = setUpPromotionCatalog();

        // 종료시까지 반복할 것
        handleStoreOrder(productCatalog, promotionCatalog);
    }

    // 편의점 프로그램 초기 셋업 메서드
    private Products setUpProductCatalog() {
        List<String> productsLines = FileReader.run(PRODUCTS_FILE_PATH);
        Products productCatalog = ProductParser.run(productsLines);
        return ProductCatalogEditor.run(productCatalog);
    }

    private Promotions setUpPromotionCatalog() {
        List<String> promotionLines = FileReader.run(PROMOTIONS_FILE_PATH);
        return PromotionParser.run(promotionLines);
    }

    private void handleStoreOrder(Products productCatalog, Promotions promotionCatalog) {
        outputView.displayProducts(productCatalog); // 재고 내역 출력

        OrderSheet orderSheet = inputOrderRequest(productCatalog); // 주문서 만들기

        promotionDiscountService.run(productCatalog, promotionCatalog, orderSheet); // 주문서 정보 추가 입력

        List<List<Integer>> orderSheetPromotionResults =
                promotionDiscountService.createPromotionResults(productCatalog, orderSheet); // 프로모션 결과지 만들기

        handlePromotionResults(orderSheet, orderSheetPromotionResults); // 프로모션 결과지를 주문서에 반영

        ProductCatalogEditor.adjustInventoryForOrders(orderSheet, productCatalog); // 주문서에 맞춰 재고 관리

        String membershipResponse = inputResponseForMembership();
        int membershipDiscount = memberShipDiscountService.run(membershipResponse, orderSheet);

        outputView.displayReceipt(orderSheet, ReceiptDetailsCalculationService.run(orderSheet),
                membershipDiscount); // 레시피 출력
    }

    // 프로모션 행사 적용 결과 처리 메서드
    private void handlePromotionResults(OrderSheet orderSheet, List<List<Integer>> promotionResults) {
        for (int currentIndex = 0; currentIndex < promotionResults.size(); currentIndex++) {
            List<Integer> promotionResult = promotionResults.get(currentIndex);

            handleNonPromotionProduct(orderSheet, promotionResult, currentIndex);
            handleExcludedPromotionProduct(orderSheet, promotionResult, currentIndex);
            handleAdditionalFreebie(orderSheet, promotionResult, currentIndex);
        }
    }

    private void handleNonPromotionProduct(OrderSheet orderSheet, List<Integer> promotionResult, int index) {
        if (promotionResult.equals(List.of(-1, -1, -1))) {
            orderSheetEditService.computeTotalWithoutPromotion(orderSheet.getOrderSheetByIndex(index));
        }
    }

    private void handleExcludedPromotionProduct(OrderSheet orderSheet, List<Integer> promotionResult, int index) {
        String orderedProductName = getProductName(orderSheet, index);
        int nonPromotionalProduct = promotionResult.get(2);

        if (nonPromotionalProduct > 0) {
            String responseForNoPromotion = inputResponseForNoPromotion(orderedProductName, nonPromotionalProduct);
            orderSheetEditService.applyResponseForNoPromotion
                    (responseForNoPromotion, promotionResult, orderSheet.getOrderSheetByIndex(index));
        }
    }

    private void handleAdditionalFreebie(OrderSheet orderSheet, List<Integer> promotionResult, int index) {
        String orderedProductName = getProductName(orderSheet, index);

        if (promotionResult.get(1) > 0) {
            int additionalFreebie = getAdditionalFreebie(orderSheet, index);
            String responseForFreeProduct = inputResponseForFreebie(orderedProductName, additionalFreebie);
            orderSheetEditService.applyResponseForFreeProduct
                    (responseForFreeProduct, promotionResult, orderSheet.getOrderSheetByIndex(index));
        }
    }

    private String getProductName(OrderSheet orderSheet, int currentIndex) {
        return orderSheet.getOrderSheetByIndex(currentIndex).getName();
    }

    private int getAdditionalFreebie(OrderSheet orderSheet, int currentIndex) {
        return orderSheet.getOrderSheetByIndex(currentIndex).getGet();
    }

    // 사용자 입력 관련 메서드 (Error 시 재입력)
    private OrderSheet inputOrderRequest(Products productCatalog) {
        while (true) {
            try {
                String orderRequest = inputView.inputProductsToPurchase();
                return orderSheetMakingService.run(orderRequest, productCatalog);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    } // 완료

    private String inputResponseForNoPromotion(String name, int noPromotionProductCount) {
        while (true) {
            try {
                String response = inputView.askForNoPromotion(name, noPromotionProductCount);
                return responseParsingService.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    } // 완료

    private String inputResponseForFreebie(String name, int additionalFreeProduct) {
        while (true) {
            try {
                String response = inputView.askForFreebie(name, additionalFreeProduct);
                return responseParsingService.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    } // 완료

    private String inputResponseForMembership() {
        while (true) {
            try {
                String response = inputView.inputMembershipDiscount();
                return responseParsingService.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    } // 완료
}