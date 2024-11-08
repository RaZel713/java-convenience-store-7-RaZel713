package store.custom.controller;

import static store.custom.constants.StringConstants.PRODUCTS_FILE_PATH;
import static store.custom.constants.StringConstants.PROMOTIONS_FILE_PATH;

import java.util.List;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotions;
import store.custom.service.FileReader;
import store.custom.service.ProductCatalogEditor;
import store.custom.service.ProductParser;
import store.custom.service.PromotionParser;
import store.custom.view.OutputView;

public class StoreController {
    private final OutputView outputView;

    public StoreController(OutputView outputView) {
        this.outputView = outputView;
    }

    public void start() {
        List<String> productsLines = FileReader.run(PRODUCTS_FILE_PATH);
        Products productCatalog = ProductParser.run(productsLines);
        productCatalog = ProductCatalogEditor.run(productCatalog);
        outputView.displayProducts(productCatalog);

        List<String> promotionLines = FileReader.run(PROMOTIONS_FILE_PATH);
        Promotions promotions = PromotionParser.run(promotionLines);
    }
}