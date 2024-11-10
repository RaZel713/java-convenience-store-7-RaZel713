package store.custom.view;

import static store.custom.view.DisplayConstants.GREETING_MESSAGE;
import static store.custom.view.DisplayConstants.HYPHEN_SPACE;
import static store.custom.view.DisplayConstants.MONEY_UNIT;
import static store.custom.view.DisplayConstants.PRODUCT_COUNT_UNIT;
import static store.custom.view.DisplayConstants.PRODUCT_LIST_MESSAGE;
import static store.custom.view.DisplayConstants.SPACE;
import static store.custom.view.DisplayConstants.ZERO_STOCK_MESSAGE;

import store.custom.model.ReceiptDetails;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class OutputView {
    // 재고 현황 출력 메서드
    public void displayInventoryStatus(Products productCatalog) {
        System.out.println();
        displayWelcomeMessage();

        for (Product product : productCatalog.getProducts()) {
            displayProductInfo(product);
        }
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

    // 영수증 출력 메서드
    public void displayReceipt(OrderSheet orderSheet, ReceiptDetails receiptDetails) {
        System.out.println();
        displayPurchaseHistory(orderSheet);

        displayFreebie(orderSheet);

        displayReceiptDetails(receiptDetails);
    }

    private void displayPurchaseHistory(OrderSheet orderSheet) {
        System.out.println(Receipt.TITLE.getFormat());
        System.out.println(Receipt.ITEM_HEADER.getFormat());

        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            System.out.println(Receipt.ITEM_NAME.format(orderedProduct.getName(),
                    orderedProduct.getQuantity(), orderedProduct.getTotalPrice()));
        }
    }

    private void displayFreebie(OrderSheet orderSheet) {
        System.out.println(Receipt.FREE_ITEM.getFormat());
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getGet() != 0) {
                System.out.println(Receipt.FREE_ITEM_NAME.format(orderedProduct.getName(), orderedProduct.getGet()));
            }
        }
        System.out.println(Receipt.DIVIDING_LINE.getFormat());
    }

    private void displayReceiptDetails(ReceiptDetails receiptDetails) {
        System.out.println(Receipt.TOTAL.format(receiptDetails.getTotalQuantity(), receiptDetails.getTotalPrice()));
        System.out.println(Receipt.PROMOTION_DISCOUNT.format(receiptDetails.getPromotionDiscount()));
        System.out.println(Receipt.MEMBERSHIP_DISCOUNT.format(receiptDetails.getMembershipDiscount()));
        System.out.println(Receipt.FINAL_PRICE.format(receiptDetails.getFinalPrice()));
    }

    // 에러 메시지 출력 메서드
    public void displayErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }
}