package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class ProductCatalogEditorTest {

    @DisplayName("제품목록관리_동일한이름과가격의제품이있을때_테스트")
    @Test
    void 제품목록관리_동일한이름과가격의제품이있을때_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("콜라", 1000, 10, null)
        ));

        Products resultCatalog = ProductCatalogEditor.run(originalCatalog);

        assertEquals(2, resultCatalog.getProductsSize());
        assertEquals("콜라", resultCatalog.getProductByIndex(0).getName());
        assertEquals(1000, resultCatalog.getProductByIndex(0).getPrice());
        assertEquals("콜라", resultCatalog.getProductByIndex(1).getName());
        assertEquals(1000, resultCatalog.getProductByIndex(1).getPrice());
    }

    @Test
    @DisplayName("제품목록관리_프로모션제품만있는제품이존재할때_테스트")
    void 제품목록관리_프로모션제품만있을때_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("오렌지주스", 1800, 9, "MD추천상품"),
                new Product("물", 500, 10, null)
        ));

        Products resultCatalog = ProductCatalogEditor.run(originalCatalog);

        assertEquals(3, resultCatalog.getProductsSize());
        assertEquals("오렌지주스", resultCatalog.getProductByIndex(0).getName());
        assertEquals(9, resultCatalog.getProductByIndex(0).getQuantity());
        assertEquals("오렌지주스", resultCatalog.getProductByIndex(1).getName());
        assertEquals(0, resultCatalog.getProductByIndex(1).getQuantity()); // 프로모션 없는 제품의 재고 0 제품 확인
        assertNull(resultCatalog.getProductByIndex(1).getPromotion());
    }

    @Test
    @DisplayName("제품목록관리_카탈로그마지막제품이프로모션제품만있는제품일때_테스트")
    void 제품목록관리_카탈로그마지막제품이프로모션제품만있는제품일때_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("물", 500, 10, null),
                new Product("오렌지주스", 1800, 9, "MD추천상품")
        ));

        Products resultCatalog = ProductCatalogEditor.run(originalCatalog);

        assertEquals(3, resultCatalog.getProductsSize());
        assertEquals("오렌지주스", resultCatalog.getProductByIndex(1).getName());
        assertEquals("오렌지주스", resultCatalog.getProductByIndex(2).getName());
        assertEquals(0, resultCatalog.getProductByIndex(2).getQuantity());
        assertNull(resultCatalog.getProductByIndex(2).getPromotion());
    }

    @Test
    @DisplayName("제품목록관리_프로모션없는제품만존재할때_테스트")
    void 제품목록관리_프로모션없는제품만존재할때_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("물", 500, 10, null),
                new Product("에너지바", 2000, 5, null)
        ));

        Products resultCatalog = ProductCatalogEditor.run(originalCatalog);

        assertEquals(2, resultCatalog.getProductsSize());
        assertEquals("물", resultCatalog.getProductByIndex(0).getName());
        assertEquals("에너지바", resultCatalog.getProductByIndex(1).getName());
    }

    @Test
    @DisplayName("제품목록관리_재고반영_테스트")
    void 제품목록관리_재고반영_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("콜라", 1000, 10, null),
                new Product("사이다", 1000, 8, "탄산2+1"),
                new Product("사이다", 1000, 7, null),
                new Product("물", 500, 10, null)
        ));

        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 12, 1000, "탄산2+1", 2, 1),
                new OrderedProduct("물", 5, 500, null, 0, 0)
        ));

        ProductCatalogEditor.adjustInventoryForOrders(orderSheet, originalCatalog);

        assertEquals(0, originalCatalog.getProductByIndex(0).getQuantity());
        assertEquals(8, originalCatalog.getProductByIndex(1).getQuantity());
        assertEquals(5, originalCatalog.getProductByIndex(4).getQuantity());
    }
}