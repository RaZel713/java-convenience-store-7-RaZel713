package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.OrderedProduct;

public class OrderSheetEditServiceTest {
    private final OrderSheetEditService orderSheetEditService = new OrderSheetEditService();

    @DisplayName("주문서편집서비스_할인되지않는제품구매_테스트")
    @Test
    void 주문서편집서비스_할인되지않는제품구매_테스트() {
        String response = "Y";
        List<Integer> promotionResult = Arrays.asList(3, 0, 1);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 10, 1000, "탄산2+1", 2, 1);

        orderSheetEditService.applyResponseForNoPromotion(response, promotionResult, orderedProduct);

        assertEquals(7, orderedProduct.getBuy());
        assertEquals(3, orderedProduct.getGet());
        assertEquals(7000, orderedProduct.getTotalPrice());
        assertEquals(10, orderedProduct.getQuantity());
    }

    @DisplayName("주문서편집서비스_할인되지않는제품미구매_테스트")
    @Test
    void 주문서편집서비스_할인되지않는제품미구매_테스트() {
        String response = "N";
        List<Integer> promotionResult = Arrays.asList(3, 0, 1);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 10, 1000, "탄산2+1", 2, 1);

        orderSheetEditService.applyResponseForNoPromotion(response, promotionResult, orderedProduct);

        assertEquals(6, orderedProduct.getBuy());
        assertEquals(3, orderedProduct.getGet());
        assertEquals(9, orderedProduct.getQuantity());
        assertEquals(6000, orderedProduct.getTotalPrice());
    }

    @DisplayName("주문서편집서비스_증정품추가_테스트")
    @Test
    void 주문서편집서비스_증정품추가_테스트() {
        String response = "Y";
        List<Integer> promotionResult = Arrays.asList(2, 1, 0);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 8, 1000, "탄산2+1", 2, 1);

        orderSheetEditService.applyResponseForFreeProduct(response, promotionResult, orderedProduct);

        assertEquals(6, orderedProduct.getBuy());
        assertEquals(9, orderedProduct.getQuantity());
        assertEquals(3, orderedProduct.getGet());
        assertEquals(6000, orderedProduct.getTotalPrice());
    }

    @DisplayName("주문서편집서비스_증정품제외_테스트")
    @Test
    void 주문서편집서비스_증정품제외_테스트() {
        String response = "N";
        List<Integer> promotionResult = Arrays.asList(2, 1, 0);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 8, 1000, "탄산2+1", 2, 1);

        orderSheetEditService.applyResponseForFreeProduct(response, promotionResult, orderedProduct);

        assertEquals(6, orderedProduct.getBuy());
        assertEquals(8, orderedProduct.getQuantity());
        assertEquals(2, orderedProduct.getGet());
        assertEquals(6000, orderedProduct.getTotalPrice());
    }

    @DisplayName("주문서편집서비스_프로모션이없는경우_테스트")
    @Test
    void 주문서편집서비스_프로모션이없는경우_테스트() {

        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 8, 1000, null, 0, 0);

        orderSheetEditService.computeTotalWithoutPromotion(orderedProduct);

        assertEquals(0, orderedProduct.getBuy());
        assertEquals(0, orderedProduct.getGet());
        assertEquals(8, orderedProduct.getQuantity());
        assertEquals(8000, orderedProduct.getTotalPrice());
    }
}