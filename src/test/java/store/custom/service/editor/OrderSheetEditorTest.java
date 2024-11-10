package store.custom.service.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static store.custom.constants.StringConstants.AFTER_PROMOTION_END;
import static store.custom.constants.StringConstants.BEFORE_PROMOTION_START;
import static store.custom.constants.StringConstants.RESPONSE_NO;
import static store.custom.constants.StringConstants.RESPONSE_YES;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotion;
import store.custom.model.promotion.Promotions;

public class OrderSheetEditorTest {
    private final OrderSheetEditor orderSheetEditor = new OrderSheetEditor();

    @DisplayName("주문서에디터_주문서정보추가기능_정상_테스트")
    @Test
    public void 주문서에디터_주문서정보추가기능_정상_테스트() {
        Products products = new Products(Arrays.asList(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("물", 500, 10, null)
        ));

        Promotions promotions = new Promotions(Collections.singletonList(
                new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01")
        ));

        OrderSheet orderSheet = new OrderSheet(Arrays.asList(
                new OrderedProduct("콜라", 4, 0, null, 0, 0),
                new OrderedProduct("물", 2, 0, null, 0, 0)
        ));

        orderSheetEditor.addPromotionInfo(products, promotions, orderSheet);

        OrderedProduct orderedProduct1 = orderSheet.getOrderSheet().getFirst();
        assertEquals(1000, orderedProduct1.getTotalPrice());
        assertEquals("탄산2+1", orderedProduct1.getPromotion());

        OrderedProduct orderedProduct2 = orderSheet.getOrderSheet().get(1);
        assertEquals(500, orderedProduct2.getTotalPrice());
        assertNull(orderedProduct2.getPromotion());
    }

    @DisplayName("주문서에디터_주문서정보추가기능_프로모션시작전_테스트")
    @Test
    public void 주문서에디터_주문서정보추가기능_프로모션시작전_테스트() {
        Products products = new Products(Arrays.asList(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("물", 500, 10, null)
        ));

        Promotions promotions = new Promotions(Collections.singletonList(
                new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01")
        ));

        OrderSheet orderSheet = new OrderSheet(Arrays.asList(
                new OrderedProduct("콜라", 4, 0, null, 0, 0),
                new OrderedProduct("물", 2, 0, null, 0, 0)
        ));

        promotions.getPromotions().getFirst().setStartDate("2024-12-01");
        orderSheetEditor.addPromotionInfo(products, promotions, orderSheet);

        assertEquals(BEFORE_PROMOTION_START, orderSheet.getOrderSheetByIndex(0).getPromotion());
    }

    @DisplayName("주문서에디터_주문서정보추가기능_프로모션종료후_테스트")
    @Test
    public void 주문서에디터_주문서정보추가기능_프로모션종료후_테스트() {
        Products products = new Products(Arrays.asList(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("물", 500, 10, null)
        ));

        Promotions promotions = new Promotions(Collections.singletonList(
                new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01")
        ));

        OrderSheet orderSheet = new OrderSheet(Arrays.asList(
                new OrderedProduct("콜라", 4, 0, null, 0, 0),
                new OrderedProduct("물", 2, 0, null, 0, 0)
        ));

        promotions.getPromotions().getFirst().setEndDate("2024-10-01");
        orderSheetEditor.addPromotionInfo(products, promotions, orderSheet);

        assertEquals(AFTER_PROMOTION_END, orderSheet.getOrderSheetByIndex(0).getPromotion());
    }

    @DisplayName("주문서에디터_할인되지않는제품구매_테스트")
    @Test
    void 주문서에디터_할인되지않는제품구매_테스트() {
        String response = RESPONSE_YES;
        List<Integer> promotionResult = Arrays.asList(3, 0, 1);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 10, 1000, "탄산2+1", 2, 1);

        orderSheetEditor.applyResponseForNoPromotion(response, promotionResult, orderedProduct);

        assertEquals(7, orderedProduct.getBuy());
        assertEquals(3, orderedProduct.getGet());
        assertEquals(10000, orderedProduct.getTotalPrice());
        assertEquals(10, orderedProduct.getQuantity());
    }

    @DisplayName("주문서에디터_할인되지않는제품미구매_테스트")
    @Test
    void 주문서에디터_할인되지않는제품미구매_테스트() {
        String response = RESPONSE_NO;
        List<Integer> promotionResult = Arrays.asList(3, 0, 1);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 10, 1000, "탄산2+1", 2, 1);

        orderSheetEditor.applyResponseForNoPromotion(response, promotionResult, orderedProduct);

        assertEquals(6, orderedProduct.getBuy());
        assertEquals(3, orderedProduct.getGet());
        assertEquals(9, orderedProduct.getQuantity());
        assertEquals(9000, orderedProduct.getTotalPrice());
    }

    @DisplayName("주문서에디터_증정품추가적용_테스트")
    @Test
    void 주문서편집에디터_증정품추가적용_테스트() {
        String response = RESPONSE_YES;
        List<Integer> promotionResult = Arrays.asList(2, 1, 0);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 8, 1000, "탄산2+1", 2, 1);

        orderSheetEditor.applyResponseForFreeProduct(response, promotionResult, orderedProduct);

        assertEquals(6, orderedProduct.getBuy());
        assertEquals(9, orderedProduct.getQuantity());
        assertEquals(3, orderedProduct.getGet());
        assertEquals(9000, orderedProduct.getTotalPrice());
    }

    @DisplayName("주문서에디터_증정품추가미적용_테스트")
    @Test
    void 주문서에디터_증정품추가미적용_테스트() {
        String response = RESPONSE_NO;
        List<Integer> promotionResult = Arrays.asList(2, 1, 0);
        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 8, 1000, "탄산2+1", 2, 1);

        orderSheetEditor.applyResponseForFreeProduct(response, promotionResult, orderedProduct);

        assertEquals(6, orderedProduct.getBuy());
        assertEquals(8, orderedProduct.getQuantity());
        assertEquals(2, orderedProduct.getGet());
        assertEquals(8000, orderedProduct.getTotalPrice());
    }

    @DisplayName("주문서에디터_프로모션이없는경우_테스트")
    @Test
    void 주문서에디터_프로모션이없는경우_테스트() {

        OrderedProduct orderedProduct = new OrderedProduct(
                "콜라", 8, 1000, null, 0, 0);

        orderSheetEditor.computeTotalWithoutPromotion(orderedProduct);

        assertEquals(0, orderedProduct.getBuy());
        assertEquals(0, orderedProduct.getGet());
        assertEquals(8, orderedProduct.getQuantity());
        assertEquals(8000, orderedProduct.getTotalPrice());
    }
}