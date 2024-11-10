package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;

public class MemberShipDiscountServiceTest {
    private final MemberShipDiscountService memberShipDiscountService = new MemberShipDiscountService();

    @DisplayName("멤버십할인서비스_멤버십할인적용_최대할인_테스트")
    @Test
    void 멤버십할인서비스_멤버십할인적용_최대할인_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 4, 4000, null, 0, 0),
                new OrderedProduct("에너지바", 10, 20000, null, 0, 0),
                new OrderedProduct("비타민워터", 6, 9000, null, 0, 0)
        ));

        int membershipDiscount = memberShipDiscountService.run("Y", orderSheet); // 33,000 의 30% = 9,900

        assertEquals(8000, membershipDiscount);
    }

    @DisplayName("멤버십할인서비스_멤버십할인적용_테스트")
    @Test
    void 멤버십할인서비스_멤버십할인적용_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 6, 6000, null, 0, 0)
        ));

        int membershipDiscount = memberShipDiscountService.run("Y", orderSheet); // 6,000 의 30%

        assertEquals(1800, membershipDiscount);
    }

    @DisplayName("멤버십할인서비스_멤버십할인미적용_테스트")
    @Test
    void 멤버십할인서비스_멤버십할인미적용_테스트() {
        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 10, 7000, "탄산2+1", 7, 3),
                new OrderedProduct("물", 5, 5000, null, 0, 0)
        ));

        int membershipDiscount = memberShipDiscountService.run("N", orderSheet);

        assertEquals(0, membershipDiscount);
    }
}