package store.custom.view;

import static store.custom.view.DisplayConstants.GREETING_MESSAGE;
import static store.custom.view.DisplayConstants.HYPHEN_SPACE;
import static store.custom.view.DisplayConstants.MONEY_UNIT;
import static store.custom.view.DisplayConstants.PRODUCT_COUNT_UNIT;
import static store.custom.view.DisplayConstants.PRODUCT_LIST_MESSAGE;
import static store.custom.view.DisplayConstants.SPACE;
import static store.custom.view.DisplayConstants.ZERO_STOCK_MESSAGE;

import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class OutputView {
    public void displayProducts(Products productCatalog) {
        displayWelcomeMessage();

        for (Product product : productCatalog.getProducts()) {
            displayProductInfo(product);
        }
        System.out.println();
    }

    private void displayWelcomeMessage() {
        System.out.println(GREETING_MESSAGE);
        System.out.println(PRODUCT_LIST_MESSAGE);
        System.out.println();
    }

    private void displayProductInfo(Product product) {
        String displayFormat = productInfoFormat(product);
        System.out.println(displayFormat);
    }

    private String productInfoFormat(Product product) {
        String price = String.format("%,d" + MONEY_UNIT, product.getPrice());
        String quantity = formatQuantityWithStockCheck(product);

        String displayFormat = HYPHEN_SPACE + product.getName() + SPACE + price + SPACE + quantity;

        return appendPromotionInfo(product, displayFormat);
    }

    private String formatQuantityWithStockCheck(Product product) {
        String quantity = String.format("%,d" + PRODUCT_COUNT_UNIT, product.getQuantity());

        if (product.getQuantity() == 0) {
            quantity = ZERO_STOCK_MESSAGE;
        }

        return quantity;
    }

    private String appendPromotionInfo(Product product, String displayFormat) {
        if (product.getPromotion() != null) {
            displayFormat += (SPACE + product.getPromotion());
        }
        return displayFormat;
    }
}