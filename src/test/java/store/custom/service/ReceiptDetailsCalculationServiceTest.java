package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;

public class ReceiptDetailsCalculationServiceTest {
    @Test
    public void 영수증내역계산서비스_주문제품중프로모션제품이있을때_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 2, 1000, null, 0, 0)
        ));

        List<Integer> result = ReceiptDetailsCalculationService.run(orderSheet);

        assertEquals(12, result.get(0)); // totalQuantity
        assertEquals(11000, result.get(1)); // totalPrice
        assertEquals(3000, result.get(2)); // promotionDiscount
    }

    @Test
    public void 영수증내역계산서비스_주문제품중프로모션제품이없을때_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("탄산수", 5, 5000, null, 0, 0),
                new OrderedProduct("물", 2, 1000, null, 0, 0)
        ));

        List<Integer> result = ReceiptDetailsCalculationService.run(orderSheet);

        assertEquals(7, result.get(0)); // totalQuantity
        assertEquals(6000, result.get(1)); // totalPrice
        assertEquals(0, result.get(2)); // promotionDiscount
    }
}