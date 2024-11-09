package store.custom.controller;

import static store.custom.constants.StringConstants.PRODUCTS_FILE_PATH;
import static store.custom.constants.StringConstants.PROMOTIONS_FILE_PATH;

import java.util.List;
import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotions;
import store.custom.service.FileReader;
import store.custom.service.OrderSheetEditService;
import store.custom.service.OrderSheetMakingService;
import store.custom.service.ProductCatalogEditor;
import store.custom.service.ProductParser;
import store.custom.service.PromotionDiscountService;
import store.custom.service.PromotionParser;
import store.custom.service.ResponseParsingService;
import store.custom.view.InputView;
import store.custom.view.OutputView;

public class StoreController {
    private final OutputView outputView;
    private final InputView inputView;
    private final OrderSheetMakingService orderSheetMakingService;
    private final PromotionDiscountService promotionDiscountService;
    private final ResponseParsingService responseParsingService;
    private final OrderSheetEditService orderSheetEditService;

    public StoreController(OutputView outputView, InputView inputView) {
        this.outputView = outputView;
        this.inputView = inputView;
        this.orderSheetMakingService = new OrderSheetMakingService();
        this.promotionDiscountService = new PromotionDiscountService();
        this.responseParsingService = new ResponseParsingService();
        this.orderSheetEditService = new OrderSheetEditService();
    }

    public void start() {
        List<String> productsLines = FileReader.run(PRODUCTS_FILE_PATH);
        Products productCatalog = ProductParser.run(productsLines);
        productCatalog = ProductCatalogEditor.run(productCatalog);

        outputView.displayProducts(productCatalog);

        List<String> promotionLines = FileReader.run(PROMOTIONS_FILE_PATH);
        Promotions promotions = PromotionParser.run(promotionLines);

        String orderRequest = inputView.inputProductsToPurchase();
        OrderSheet orderSheet = orderSheetMakingService.run(orderRequest, productCatalog);

        promotionDiscountService.run(productCatalog, promotions, orderSheet);

        List<List<Integer>> orderSheetPromotionResults =
                promotionDiscountService.createPromotionResults(productCatalog, orderSheet);

        handlePromotionResults(orderSheet, orderSheetPromotionResults);
        ProductCatalogEditor.adjustInventoryForOrders(orderSheet, productCatalog);
    }

    public void handlePromotionResults(OrderSheet orderSheet, List<List<Integer>> orderSheetPromotionResults) {
        for (int currentIndex = 0; currentIndex < orderSheetPromotionResults.size(); currentIndex++) {
            List<Integer> orderSheetPromotionResult = orderSheetPromotionResults.get(currentIndex);

            if (orderSheetPromotionResult.equals(List.of(-1, -1, -1))) {
                orderSheetEditService.computeTotalWithoutPromotion(orderSheet.getOrderSheetByIndex(currentIndex));
            }

            String name = getProductName(orderSheet, currentIndex);
            int noPromotionProductCount = orderSheetPromotionResult.get(2);

            String response = handleNoPromotion(noPromotionProductCount, name);
            if (response != null) {
                responseParsingService.run(response);
                orderSheetEditService.applyNonDiscountedPurchaseDecision
                        (response, orderSheetPromotionResult, orderSheet.getOrderSheetByIndex(currentIndex));
            }

            int promotionAdditionalCount = orderSheetPromotionResult.get(1);

            response = handleFreeProductAddition(promotionAdditionalCount, name, orderSheet, currentIndex);
            if (response != null) {
                responseParsingService.run(response);
                orderSheetEditService.applyAdditionalPromotionDecision
                        (response, orderSheetPromotionResult, orderSheet.getOrderSheetByIndex(currentIndex));
            }
        }
    }

    private String getProductName(OrderSheet orderSheet, int index) {
        OrderedProduct orderedProduct = orderSheet.getOrderSheetByIndex(index);
        return orderedProduct.getName();
    }

    private String handleNoPromotion(int noPromotionProductCount, String name) {
        if (noPromotionProductCount > 0) {
            return inputView.askForNoPromotionDiscount(name, noPromotionProductCount);
        }
        return null;
    }

    private String handleFreeProductAddition(int promotionAdditionalCount, String name, OrderSheet orderSheet,
                                             int index) {
        if (promotionAdditionalCount > 0) {
            int additionalFreeProduct = getAdditionalFreeProduct(orderSheet, index);
            return inputView.askForFreeProductAddition(name, additionalFreeProduct);
        }
        return null;
    }

    private int getAdditionalFreeProduct(OrderSheet orderSheet, int index) {
        OrderedProduct orderedProduct = orderSheet.getOrderSheetByIndex(index);
        return orderedProduct.getGet();
    }
}