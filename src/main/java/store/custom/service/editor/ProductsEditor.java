package store.custom.service.editor;

import java.util.ArrayList;
import java.util.List;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class ProductsEditor {
    // 재고 목록 초기 설정: 목록에 프로모션 상품만 있는 경우 일반 상품 추가
    public static Products inspectProductCatalog(Products originalCatalog) {
        List<Product> resultCatalog = new ArrayList<>();
        int currentIndex;
        for (currentIndex = 0; currentIndex < originalCatalog.getProductsSize() - 1; currentIndex++) {
            currentIndex += inspectProduct(originalCatalog, resultCatalog, currentIndex);
        }
        addLastProduct(originalCatalog, resultCatalog, currentIndex);

        return new Products(resultCatalog);
    }

    private static int inspectProduct(Products original, List<Product> result, int currentIndex) {
        Product currentProduct = original.getProductByIndex(currentIndex);
        Product nextProduct = original.getProductByIndex(currentIndex + 1);

        if (hasSameNameAndPrice(result, currentProduct, nextProduct)) {
            return 1; // 두개의 상품을 검사완료했으므로 index 의 값 1 증가
        }

        return addProductConsideringPromotion(currentProduct, result);
    }

    private static boolean hasSameNameAndPrice(List<Product> result, Product currentProduct, Product nextProduct) {
        if (currentProduct.getName().equals(nextProduct.getName())
                && currentProduct.getPrice() == nextProduct.getPrice()) {
            result.add(currentProduct);
            result.add(nextProduct);
            return true;
        }
        return false;
    }

    private static int addProductConsideringPromotion(Product currentProduct, List<Product> result) {
        result.add(currentProduct);

        if (currentProduct.getPromotion() != null) {
            result.add(createZeroStockProduct(currentProduct)); // 재고가 0인 제품 추가
        }
        return 0;
    }

    private static Product createZeroStockProduct(Product product) {
        return new Product(product.getName(), product.getPrice(), 0, null);
    }

    private static void addLastProduct(Products originalCatalog, List<Product> result, int currentIndex) {
        if (currentIndex == originalCatalog.getProductsSize() - 1) {
            Product lastProduct = originalCatalog.getProductByIndex(originalCatalog.getProductsSize() - 1);
            addProductConsideringPromotion(lastProduct, result);
        }
    }

    // 주문 수량에 따른 재고 조정
    public static void adjustInventoryForOrders(OrderSheet orderSheet, Products productCatalog) {
        for (OrderedProduct orderedProduct : orderSheet.getOrderSheet()) {
            processOrderedProduct(orderedProduct, productCatalog);
        }
    }

    private static void processOrderedProduct(OrderedProduct orderedProduct, Products productCatalog) {
        int remainQuantity = orderedProduct.getQuantity();
        for (Product product : productCatalog.getProducts()) {
            if (remainQuantity == 0) {
                break;
            }
            remainQuantity = updateProductQuantity(orderedProduct, product, remainQuantity);
        }
    }

    private static int updateProductQuantity(OrderedProduct orderedProduct, Product product, int remainQuantity) {
        if (product.getName().equals(orderedProduct.getName())) {
            if (product.getPromotion() != null) {
                return calculateRemainingQuantityWithPromotion(product, remainQuantity);
            }
            return calculateRemainingQuantityWithoutPromotion(product, remainQuantity);
        }
        return remainQuantity;
    }

    private static int calculateRemainingQuantityWithPromotion(Product product, int remainQuantity) {
        int productQuantity = product.getQuantity();

        if (remainQuantity >= productQuantity) {
            product.setQuantity(0);
            return remainQuantity - productQuantity;
        }
        product.setQuantity(productQuantity - remainQuantity);
        return 0;
    }

    private static int calculateRemainingQuantityWithoutPromotion(Product product, int remainQuantity) {
        int productQuantity = product.getQuantity();

        if (remainQuantity == productQuantity) {
            product.setQuantity(0);
            return 0;
        }
        product.setQuantity(productQuantity - remainQuantity);
        return 0;
    }
}