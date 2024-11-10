package store.custom.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;
import store.custom.validator.CustomErrorMessages;

public class OrderSheetMakingServiceTest {
    private Products originalCatalog;  // 클래스 필드로 선언
    private final OrderSheetMakingService orderSheetMakingService = new OrderSheetMakingService();

    @BeforeEach
    void setUp() {
        originalCatalog = new Products(List.of(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("콜라", 1000, 10, null),
                new Product("오렌지주스", 1800, 9, "MD추천상품"),
                new Product("물", 500, 10, null)
        ));
    }

    @DisplayName("주문서제작서비스_빈문자열일때_테스트")
    @Test
    void 주문서제작서비스_빈문자열일때_테스트() {
        assertThatThrownBy(() -> orderSheetMakingService.run("", originalCatalog))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("주문서제작서비스_잘못된형식일때_테스트")
    @Test
    void 주문서제작서비스_잘못된형식일때_테스트() {
        assertThatThrownBy(() -> orderSheetMakingService.run("[콜라-10],물-3", originalCatalog))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_ORDER_FORMAT);
    }

    @DisplayName("주문서제작서비스_존재하지않는제품일때_테스트")
    @Test
    void 주문서제작서비스_존재하지않는제품일때_테스트() {
        assertThatThrownBy(() -> orderSheetMakingService.run("[콜라-10],[맥주-3]", originalCatalog))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.NON_EXISTENT_PRODUCT);
    }

    @DisplayName("주문서제작서비스_재고의수량을초과했을때_테스트")
    @Test
    void 주문서제작서비스_재고의수량을초과했을때_테스트() {
        assertThatThrownBy(() -> orderSheetMakingService.run("[콜라-21],[물-1]", originalCatalog))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INSUFFICIENT_STOCK);
    }

    @DisplayName("주문서제작서비스_정상_테스트")
    @Test
    void 주문서제작서비스_정상_테스트() {
        String orderRequest = "[콜라-10],[물-5]";

        OrderSheet orderSheet = orderSheetMakingService.run(orderRequest, originalCatalog);

        assertNotNull(orderSheet);
        assertEquals(2, orderSheet.getOrderSheet().size());

        OrderedProduct product1 = orderSheet.getOrderSheet().get(0);
        assertEquals("콜라", product1.getName());
        assertEquals(10, product1.getQuantity());

        OrderedProduct product2 = orderSheet.getOrderSheet().get(1);
        assertEquals("물", product2.getName());
        assertEquals(5, product2.getQuantity());
    }
}