package store.custom.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.product.Product;
import store.custom.validator.CustomErrorMessages;

public class OrderSheetMakerTest {

    @DisplayName("주문서제작모델_빈문자열일때_테스트")
    @Test
    void 주문서제작모델_빈문자열일때_테스트() {
        OrderSheetMaker orderSheetMaker = new OrderSheetMaker();

        assertThatThrownBy(() -> orderSheetMaker.run(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("주문서제작모델_잘못된형식일때_테스트")
    @Test
    void 주문서제작모델_잘못된형식일때_테스트() {
        OrderSheetMaker orderSheetMaker = new OrderSheetMaker();

        assertThatThrownBy(() -> orderSheetMaker.run("[콜라-10],[사이다]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_ORDER_FORMAT);
    }

    @DisplayName("주문서제작모델_정상_테스트")
    @Test
    void 주문서제작모델_정상_테스트() {
        String orderRequest = "[콜라-10],[사이다-5]";

        OrderSheetMaker orderSheetMaker = new OrderSheetMaker();
        OrderSheet orderSheet = orderSheetMaker.run(orderRequest);

        assertNotNull(orderSheet);
        assertEquals(2, orderSheet.getOrderSheet().size());

        Product product1 = orderSheet.getOrderSheet().get(0);
        assertEquals("콜라", product1.getName());
        assertEquals(10, product1.getQuantity());

        Product product2 = orderSheet.getOrderSheet().get(1);
        assertEquals("사이다", product2.getName());
        assertEquals(5, product2.getQuantity());
    }
}