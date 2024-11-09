package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.custom.model.OrderSheet;
import store.custom.model.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;
import store.custom.model.promotion.Promotion;
import store.custom.model.promotion.Promotions;

public class PromotionDiscuntServiceTest {
    private PromotionDiscountService promotionDiscountService;
    private Products products;
    private Promotions promotions;
    private OrderSheet orderSheet;

    @BeforeEach
    public void setUp() {
        promotionDiscountService = new PromotionDiscountService();

        Product product1 = new Product("콜라", 1000, 10, "탄산2+1");
        Product product2 = new Product("물", 500, 10, null);
        products = new Products(Arrays.asList(product1, product2));

        Promotion promotion = new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01");
        promotions = new Promotions(Collections.singletonList(promotion));

        OrderedProduct orderedProduct1 = new OrderedProduct("콜라", 4, 0, null, 0, 0);
        OrderedProduct orderedProduct2 = new OrderedProduct("물", 2, 0, null, 0, 0);
        orderSheet = new OrderSheet(Arrays.asList(orderedProduct1, orderedProduct2));
    }

    @Test
    public void testRun() {
        promotionDiscountService.run(products, promotions, orderSheet);

        OrderedProduct orderedProduct1 = orderSheet.getOrderSheet().get(0);
        assertEquals(1000, orderedProduct1.getTotalPrice());
        assertEquals("탄산2+1", orderedProduct1.getPromotion());

        OrderedProduct orderedProduct2 = orderSheet.getOrderSheet().get(1);
        assertEquals(500, orderedProduct2.getTotalPrice());
        assertNull(orderedProduct2.getPromotion());
    }

    @Test
    public void testApplyPromotionDates_BeforePromotionStart() {
        promotions.getPromotions().get(0).setStartDate("2024-12-01");
        promotionDiscountService.run(products, promotions, orderSheet);

        assertEquals("프로모션 시작 전", orderSheet.getOrderSheetByIndex(0).getPromotion());
    }

    @Test
    public void testApplyPromotionDates_AfterPromotionEnd() {
        promotions.getPromotions().get(0).setEndDate("2024-10-01");
        promotionDiscountService.run(products, promotions, orderSheet);

        assertEquals("프로모션 종료 후", orderSheet.getOrderSheetByIndex(0).getPromotion());
    }

    @Test
    public void testCreatePromotionResults_FullPromotion() {
        OrderedProduct orderProduct = new OrderedProduct("콜라", 6, 1000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        List<List<Integer>> results = promotionDiscountService.createPromotionResults(products, testOrderSheet);

        assertEquals(Arrays.asList(2, 0, 0), results.get(0));
    }

    @Test
    public void testCreatePromotionResults_PartialPromotion() {
        OrderedProduct orderProduct = new OrderedProduct("콜라", 4, 1000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        List<List<Integer>> results = promotionDiscountService.createPromotionResults(products, testOrderSheet);

        assertEquals(Arrays.asList(1, 0, 1), results.get(0));
    }

    @Test
    public void testCreatePromotionResults_NoPromotion() {
        OrderedProduct orderProduct = new OrderedProduct("물", 2, 500, null, 0, 0);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        List<List<Integer>> results = promotionDiscountService.createPromotionResults(products, testOrderSheet);

        assertEquals(Arrays.asList(-1, -1, -1), results.get(0));
    }
}