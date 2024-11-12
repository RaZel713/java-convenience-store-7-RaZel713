package store.custom.service.maker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.custom.constants.NumberConstants.NOT_FOUND;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.PromotionResult.PromotionResults;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class PromotionResultMakerTest {
    private PromotionResultMaker promotionResultMaker;
    private Products products;

    @BeforeEach
    public void setUp() {
        promotionResultMaker = new PromotionResultMaker();

        products = new Products(Arrays.asList(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("물", 500, 10, null)
        ));
    }

    @DisplayName("프로모션결과메이커_전부프로모션적용가능할때_테스트")
    @Test
    public void 프로모션결과메이커_전부프로모션적용가능할때_테스트() {
        OrderedProduct orderProduct = new OrderedProduct("콜라", 6, 6000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        PromotionResults results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(2, results.getPromotionResultByIndex(0).getApplicablePromotionCount());
        assertEquals(0, results.getPromotionResultByIndex(0).getExtraPromotionCount());
        assertEquals(0, results.getPromotionResultByIndex(0).getNonPromotionProductCount());
    }

    @DisplayName("프로모션결과메이커_프로모션미적용상품이존재할때_테스트")
    @Test
    public void 프로모션결과메이커_프로모션미적용상품이존재할때_테스트() {
        OrderedProduct orderProduct = new OrderedProduct("콜라", 4, 4000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        PromotionResults results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(1, results.getPromotionResultByIndex(0).getApplicablePromotionCount());
        assertEquals(0, results.getPromotionResultByIndex(0).getExtraPromotionCount());
        assertEquals(1, results.getPromotionResultByIndex(0).getNonPromotionProductCount());
    }

    @DisplayName("프로모션결과메이커_프로모션추가적용이가능할때_테스트")
    @Test
    public void 프로모션결과메이커_프로모션추가적용이가능할때_테스트() {
        OrderedProduct orderProduct = new OrderedProduct("콜라", 5, 5000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        PromotionResults results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(1, results.getPromotionResultByIndex(0).getApplicablePromotionCount());
        assertEquals(1, results.getPromotionResultByIndex(0).getExtraPromotionCount());
        assertEquals(0, results.getPromotionResultByIndex(0).getNonPromotionProductCount());
    }

    @DisplayName("프로모션결과메이커_프로모션상품이없을때_테스트")
    @Test
    public void 프로모션결과메이커_프로모션상품이없을때_테스트() {
        OrderedProduct orderProduct = new OrderedProduct("물", 2, 1000, null, 0, 0);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        PromotionResults results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(NOT_FOUND, results.getPromotionResultByIndex(0).getApplicablePromotionCount());
        assertEquals(NOT_FOUND, results.getPromotionResultByIndex(0).getExtraPromotionCount());
        assertEquals(NOT_FOUND, results.getPromotionResultByIndex(0).getNonPromotionProductCount());
    }
}