package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;

public class MemberShipDiscountServiceTest {
    private final MemberShipDiscountService memberShipDiscountService = new MemberShipDiscountService();

    @DisplayName("멤버십할인서비스_멤버십할인적용_최대할인_테스트")
    @Test
    void 멤버십할인서비스_멤버십할인적용_최대할인_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 5, 5000, null, 0, 0),
                new OrderedProduct("에너지바", 5, 10000, null, 0, 0),
                new OrderedProduct("비타민워터", 6, 9000, null, 0, 0)
        ));

        int totalPrice = memberShipDiscountService.run("Y", orderSheet); // 31,000-8,000

        assertEquals(23000, totalPrice);
    }

    @DisplayName("멤버십할인서비스_멤버십할인적용_테스트")
    @Test
    void 멤버십할인서비스_멤버십할인적용_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 5, 5000, null, 0, 0)
        ));

        int totalPrice = memberShipDiscountService.run("Y", orderSheet); // 12,000-3,600

        assertEquals(8400, totalPrice);
    }

    @DisplayName("멤버십할인서비스_멤버십할인미적용_테스트")
    @Test
    void 멤버십할인서비스_멤버십할인미적용_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 5, 5000, null, 0, 0)
        ));

        int totalPrice = memberShipDiscountService.run("N", orderSheet); // 12,000

        assertEquals(12000, totalPrice);
    }
}