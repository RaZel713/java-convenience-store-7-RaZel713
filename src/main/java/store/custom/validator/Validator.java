package store.custom.validator;

import static store.custom.constants.RegexConstants.PRODUCT_ORDER_REGEX;
import static store.custom.validator.CustomErrorMessages.INVALID_INPUT;

import java.util.List;
import store.custom.model.OrderSheet;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class Validator {
    public static void validateEmptyInput(String input) {
        checkNullInput(input);
        checkEmptyInput(input);
        checkWhitespaceOnlyInput(input);
    }

    private static void checkNullInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private static void checkEmptyInput(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private static void checkWhitespaceOnlyInput(String input) {
        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    public static void validateOrderForm(List<String> orderForms) {
        for (String orderForm : orderForms) {
            if (!orderForm.matches(PRODUCT_ORDER_REGEX)) {
                throw new IllegalArgumentException(CustomErrorMessages.INVALID_ORDER_FORMAT);
            }
        }
    }

    public static void validateOrderedProductsName(Products products, OrderSheet orderSheet) {
        for (Product orderedProduct : orderSheet.getOrderSheet()) {
            if (!isProductNameMatched(products, orderedProduct)) { // 제품이 없으면
                throw new IllegalArgumentException(CustomErrorMessages.NON_EXISTENT_PRODUCT);
            }
        }
    }

    private static boolean isProductNameMatched(Products products, Product orderedProduct) {
        for (Product product : products.getProducts()) {
            if (product.getName().equals(orderedProduct.getName())) {
                return true; // 제품이 존재할 때 true 반환
            }
        }
        return false; // 제품이 존재하지않을 때 false 반환
    }

    public static void validateOrderedProductsQuantity(Products products, OrderSheet orderSheet) {
        for (Product orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getQuantity() > calculateProductTotalQuantity(products, orderedProduct)) {
                throw new IllegalArgumentException(CustomErrorMessages.INSUFFICIENT_STOCK);
            }
        }
    }

    private static int calculateProductTotalQuantity(Products products, Product orderedProduct) {
        int totalQuantity = 0;

        for (Product product : products.getProducts()) {
            if (product.getName().equals(orderedProduct.getName())) {
                totalQuantity += product.getQuantity();
            }
        }

        return totalQuantity;
    }
}