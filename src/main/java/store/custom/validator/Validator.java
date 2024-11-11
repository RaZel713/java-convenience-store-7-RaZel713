package store.custom.validator;

import static store.custom.constants.RegexConstants.PRODUCT_ORDER_REGEX;
import static store.custom.constants.StringConstants.RESPONSE_NO;
import static store.custom.constants.StringConstants.RESPONSE_YES;
import static store.custom.validator.CustomErrorMessages.INVALID_FILE_PATH;
import static store.custom.validator.CustomErrorMessages.INVALID_INPUT;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class Validator {
    // 파일 리더기 관련 유효성 검사
    public static void validateFilePath(String filePath) {
        if (!Files.exists(Path.of(filePath))) {
            throw new IllegalArgumentException(INVALID_FILE_PATH + filePath);
        }
    }

    // 입력 관련 유효성 검사 (공통)
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

    // 주문 입력 관련 유효성 검사
    public static void validateOrderForm(List<String> orderForms) {
        for (String orderForm : orderForms) {
            if (!orderForm.matches(PRODUCT_ORDER_REGEX)) {
                throw new IllegalArgumentException(CustomErrorMessages.INVALID_ORDER_FORMAT);
            }
        }
    }

    public static void validateOrderSheet(Products products, OrderSheet orderSheet) {
        validateOrderedProductsName(products, orderSheet);
        validateOrderedProductsQuantity(products, orderSheet);
    }

    public static void validateOrderedProductsName(Products products, OrderSheet orderSheet) {
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (!isProductNameMatched(products, orderedProduct)) { // 제품이 없으면
                throw new IllegalArgumentException(CustomErrorMessages.NON_EXISTENT_PRODUCT);
            }
        }
    }

    private static boolean isProductNameMatched(Products products, OrderedProduct orderedProduct) {
        for (Product product : products.getProducts()) {
            if (product.getName().equals(orderedProduct.getName())) {
                return true; // 제품이 존재할 때 true 반환
            }
        }
        return false; // 제품이 존재하지않을 때 false 반환
    }

    public static void validateOrderedProductsQuantity(Products products, OrderSheet orderSheet) {
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            if (orderedProduct.getQuantity() > calculateProductTotalQuantity(products, orderedProduct)) {
                throw new IllegalArgumentException(CustomErrorMessages.INSUFFICIENT_STOCK);
            }
        }
    }

    private static int calculateProductTotalQuantity(Products products, OrderedProduct orderedProduct) {
        int totalQuantity = 0;
        for (Product product : products.getProducts()) {
            if (product.getName().equals(orderedProduct.getName())) {
                totalQuantity += product.getQuantity();
            }
        }
        return totalQuantity;
    }

    // 응답 입력 관련 유효성 검사
    public static void validateYesOrNoInput(String response) {
        if (!response.equals(RESPONSE_YES) && !response.equals(RESPONSE_NO)) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }
}