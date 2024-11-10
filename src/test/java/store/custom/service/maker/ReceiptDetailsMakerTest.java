package store.custom.service.maker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.ReceiptDetails;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;

class ReceiptDetailsMakerTest {
    private final ReceiptDetailsMaker receiptDetailsMaker = new ReceiptDetailsMaker();
    private OrderSheet orderSheet;

    @BeforeEach
    void setUp() {
        OrderedProduct product1 = new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3);
        OrderedProduct product2 = new OrderedProduct("물", 5, 5000, null, 0, 0); // 프로모션 있는 제품
        orderSheet = new OrderSheet(Arrays.asList(product1, product2));
    }

    @DisplayName("영수증세부내역메이커_멤버십적용할때_테스트")
    @Test
    void 영수증세부내역메이커_멤버십적용할때_테스트() {
        ReceiptDetails receipt = receiptDetailsMaker.run(orderSheet, "Y");

        assertEquals(15, receipt.getTotalQuantity());
        assertEquals(15000, receipt.getTotalPrice());
        assertEquals(3000, receipt.getPromotionDiscount());
        assertEquals(1500, receipt.getMembershipDiscount());
        assertEquals(10500, receipt.getFinalPrice());
    }

    @DisplayName("영수증세부내역메이커_멤버십적용안할때_테스트")
    @Test
    void 영수증세부내역메이커_멤버십적용안할때_테스트() {
        ReceiptDetails receipt = receiptDetailsMaker.run(orderSheet, "N");

        assertEquals(15, receipt.getTotalQuantity());
        assertEquals(15000, receipt.getTotalPrice());
        assertEquals(3000, receipt.getPromotionDiscount());
        assertEquals(0, receipt.getMembershipDiscount());
        assertEquals(12000, receipt.getFinalPrice());
    }
}
