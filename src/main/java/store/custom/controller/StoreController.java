package store.custom.controller;

import static store.custom.constants.StringConstants.PRODUCTS_FILE_PATH;
import static store.custom.constants.StringConstants.PROMOTIONS_FILE_PATH;

import java.util.List;
import store.custom.model.OrderSheet;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotions;
import store.custom.service.FileReader;
import store.custom.service.OrderSheetMakingService;
import store.custom.service.ProductCatalogEditor;
import store.custom.service.ProductParser;
import store.custom.service.PromotionParser;
import store.custom.view.InputView;
import store.custom.view.OutputView;

public class StoreController {
    private final OutputView outputView;
    private final InputView inputView;
    private final OrderSheetMakingService orderSheetMakingService;

    public StoreController(OutputView outputView, InputView inputView) {
        this.outputView = outputView;
        this.inputView = inputView;
        this.orderSheetMakingService = new OrderSheetMakingService();
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


    }
}