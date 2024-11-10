package store.custom.service.maker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        OrderedProduct orderProduct = new OrderedProduct("콜라", 6, 1000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        List<List<Integer>> results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(Arrays.asList(2, 0, 0), results.getFirst());
    }

    @DisplayName("프로모션결과메이커_프로모션미적용상품이존재할때_테스트")
    @Test
    public void 프로모션결과메이커_프로모션미적용상품이존재할때_테스트() {
        OrderedProduct orderProduct = new OrderedProduct("콜라", 4, 1000, "탄산2+1", 2, 1);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        List<List<Integer>> results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(Arrays.asList(1, 0, 1), results.getFirst());
    }

    @DisplayName("프로모션결과메이커_프로모션상품이없을때_테스트")
    @Test
    public void 프로모션결과메이커_프로모션상품이없을때_테스트() {
        OrderedProduct orderProduct = new OrderedProduct("물", 2, 500, null, 0, 0);
        OrderSheet testOrderSheet = new OrderSheet(Collections.singletonList(orderProduct));

        List<List<Integer>> results = promotionResultMaker.createPromotionResults(products, testOrderSheet);

        assertEquals(Arrays.asList(-1, -1, -1), results.getFirst());
    }
}