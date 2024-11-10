package store.custom.service.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class ProductParserTest {

    @DisplayName("제품변환기_EMPTY_테스트")
    @Test
    void 제품변환기_EMPTY_테스트() {
        List<String> lines = List.of();
        Products products = ProductParser.run(lines);

        assertNotNull(products);
        assertEquals(0, products.getProducts().size());
    }

    @DisplayName("제품변환기_NULL_테스트")
    @Test
    void 제품변환기_NULL_테스트() {
        Products products = ProductParser.run(null);

        assertNotNull(products);
        assertEquals(0, products.getProducts().size());
    }

    @DisplayName("제품변환기_정상_테스트")
    @Test
    void 제품변환기_정상_테스트() {
        List<String> lines = List.of(
                "name,price,quantity,promotion",
                "오렌지주스,1800,9,MD추천상품",
                "물,500,10,null"
        );

        Products products = ProductParser.run(lines);

        assertNotNull(products);
        assertEquals(2, products.getProducts().size());

        Product firstProduct = products.getProducts().get(0);
        assertEquals("오렌지주스", firstProduct.getName());
        assertEquals(1800, firstProduct.getPrice());
        assertEquals(9, firstProduct.getQuantity());
        assertEquals("MD추천상품", firstProduct.getPromotion());

        Product secondProduct = products.getProducts().get(1);
        assertEquals("물", secondProduct.getName());
        assertEquals(500, secondProduct.getPrice());
        assertEquals(10, secondProduct.getQuantity());
        assertNull(secondProduct.getPromotion());
    }

    @DisplayName("제품변환기_공백처리_테스트")
    @Test
    void 제품변환기_공백처리_테스트() {
        List<String> lines = List.of(
                "Name,Price,Quantity,Promotion",
                "  오렌지주스  , 1800 , 9 ,  MD추천상품  "
        );

        Products products = ProductParser.run(lines);

        assertNotNull(products);
        assertEquals(1, products.getProducts().size());

        Product product = products.getProducts().get(0);
        assertEquals("오렌지주스", product.getName());
        assertEquals(1800, product.getPrice());
        assertEquals(9, product.getQuantity());
        assertEquals("MD추천상품", product.getPromotion());
    }
}
