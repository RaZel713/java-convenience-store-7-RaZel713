package store.custom.service;

import java.util.ArrayList;
import java.util.List;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class ProductCatalogEditor {
    public static Products run(Products originalCatalog) {
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

        if (currentProduct.getPromotion() != null) { // 해당 상품에 프로모션 값이 존재하면
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
}