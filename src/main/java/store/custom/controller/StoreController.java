package store.custom.controller;

import static store.custom.constants.StringConstants.PRODUCTS_FILE_PATH;
import static store.custom.constants.StringConstants.PROMOTIONS_FILE_PATH;
import static store.custom.constants.StringConstants.RESPONSE_YES;

import java.util.List;
import store.custom.model.PromotionResult.PromotionResult;
import store.custom.model.PromotionResult.PromotionResults;
import store.custom.model.ReceiptDetails;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotions;
import store.custom.service.editor.OrderSheetEditor;
import store.custom.service.editor.ProductsEditor;
import store.custom.service.filehandler.FileReader;
import store.custom.service.maker.PromotionResultMaker;
import store.custom.service.maker.ReceiptDetailsMaker;
import store.custom.service.parser.OrderParser;
import store.custom.service.parser.ProductParser;
import store.custom.service.parser.PromotionParser;
import store.custom.service.parser.ResponseParser;
import store.custom.view.InputView;
import store.custom.view.OutputView;

public class StoreController {
    private final OrderSheetEditor orderSheetEditor;
    private final PromotionResultMaker promotionResultsMaker;
    private final ReceiptDetailsMaker receiptDetailsMaker;
    private final OrderParser orderParser;
    private final ResponseParser responseParser;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.orderSheetEditor = new OrderSheetEditor();
        this.promotionResultsMaker = new PromotionResultMaker();
        this.receiptDetailsMaker = new ReceiptDetailsMaker();
        this.orderParser = new OrderParser();
        this.responseParser = new ResponseParser();

        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start() {
        Products productCatalog = setUpProductCatalog();
        Promotions promotionCatalog = setUpPromotionCatalog();

        String repeat = RESPONSE_YES;
        while (RESPONSE_YES.equals(repeat)) {
            outputView.displayInventoryStatus(productCatalog);
            repeat = handleStoreOrder(productCatalog, promotionCatalog);
        }
    }

    // 편의점 프로그램 초기 셋업 메서드
    private Products setUpProductCatalog() {
        List<String> productsLines = FileReader.run(PRODUCTS_FILE_PATH);
        Products productCatalog = ProductParser.run(productsLines);
        return ProductsEditor.run(productCatalog);
    }

    private Promotions setUpPromotionCatalog() {
        List<String> promotionLines = FileReader.run(PROMOTIONS_FILE_PATH);
        return PromotionParser.run(promotionLines);
    }

    // 주문요청 처리 메서드
    private String handleStoreOrder(Products productCatalog, Promotions promotionCatalog) {
        OrderSheet orderSheet = handleOrderSheet(productCatalog, promotionCatalog);
        ProductsEditor.adjustInventoryForOrders(orderSheet, productCatalog); // 주문서에 맞춰 재고 관리
        ReceiptDetails receiptDetails = receiptDetailsMaker.run(orderSheet, inputResponseForMembership());

        outputView.displayReceipt(orderSheet, receiptDetails); // 레시피 출력
        return inputResponseForAdditionalPurchase();
    }

    // 주문서 처리 메서드
    private OrderSheet handleOrderSheet(Products productCatalog, Promotions promotionCatalog) {
        OrderSheet orderSheet = inputOrderRequest(productCatalog);
        orderSheetEditor.addPromotionInfo(productCatalog, promotionCatalog, orderSheet);

        PromotionResults promotionResults =
                promotionResultsMaker.createPromotionResults(productCatalog, orderSheet); // 프로모션 결과지 만들기
        handlePromotionResults(orderSheet, promotionResults); // 프로모션 결과지를 주문서에 반영

        return orderSheet;
    }

    // 프로모션 행사 적용 결과 처리 메서드
    private void handlePromotionResults(OrderSheet orderSheet, PromotionResults promotionResults) {
        for (int index = 0; index < promotionResults.getPromotionResultCount(); index++) {
            PromotionResult promotionResult = promotionResults.getPromotionResultByIndex(index);
            OrderedProduct orderedProduct = orderSheet.getOrderSheetByIndex(index);

            handleExcludedPromotionProduct(orderedProduct, promotionResult);
            handleAdditionalFreebie(orderedProduct, promotionResult);
            handlePromotionProduct(orderedProduct, promotionResult);
        }
    }

    private void handleExcludedPromotionProduct(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        String orderedProductName = orderedProduct.getName();
        int nonPromotionProduct = promotionResult.getNonPromotionProductCount();

        if (nonPromotionProduct > 0) {
            String responseForNoPromotion = inputResponseForNoPromotion(orderedProductName, nonPromotionProduct);
            orderSheetEditor.applyResponseForNoPromotion(responseForNoPromotion, promotionResult, orderedProduct);
        }
    }

    private void handleAdditionalFreebie(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        String orderedProductName = orderedProduct.getName();
        int extraPromotionCount = promotionResult.getExtraPromotionCount();

        if (extraPromotionCount > 0) {
            int additionalFreebie = orderedProduct.getGet();
            String responseForFreeProduct = inputResponseForFreebie(orderedProductName, additionalFreebie);
            orderSheetEditor.applyResponseForFreeProduct(responseForFreeProduct, promotionResult, orderedProduct);
        }
    }

    private void handlePromotionProduct(OrderedProduct orderedProduct, PromotionResult promotionResult) {
        int nonPromotionProduct = promotionResult.getNonPromotionProductCount();
        int extraPromotionCount = promotionResult.getExtraPromotionCount();

        if (extraPromotionCount == 0 && nonPromotionProduct == 0) {
            orderSheetEditor.computeTotalWithPromotion(orderedProduct, promotionResult);
        }
    }

    // 사용자 입력 관련 메서드 (Error 시 재입력)
    private OrderSheet inputOrderRequest(Products productCatalog) {
        while (true) {
            try {
                String orderRequest = inputView.askForProductsToPurchase();
                return orderParser.run(orderRequest, productCatalog);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    }

    private String inputResponseForNoPromotion(String name, int noPromotionProductCount) {
        while (true) {
            try {
                String response = inputView.askForProductWithoutPromotion(name, noPromotionProductCount);
                return responseParser.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    }

    private String inputResponseForFreebie(String name, int additionalFreeProduct) {
        while (true) {
            try {
                String response = inputView.askForFreebie(name, additionalFreeProduct);
                return responseParser.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    }

    private String inputResponseForMembership() {
        while (true) {
            try {
                String response = inputView.askForMembershipDiscount();
                return responseParser.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    }

    private String inputResponseForAdditionalPurchase() {
        while (true) {
            try {
                String response = inputView.askForAdditionalPurchase();
                return responseParser.run(response);
            } catch (IllegalArgumentException e) {
                outputView.displayErrorMessage(e.getMessage());
            }
        }
    }
}