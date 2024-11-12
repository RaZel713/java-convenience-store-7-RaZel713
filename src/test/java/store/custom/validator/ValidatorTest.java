package store.custom.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class ValidatorTest {

    @DisplayName("유효성검증_입력이_NULL일때_테스트")
    @Test
    void 유효성검증_입력이_NULL일때_테스트() {
        assertThatThrownBy(() -> Validator.validateEmptyInput(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("유효성검증_입력이_빈문자열일때_테스트")
    @Test
    void 유효성검증_입력이_빈문자열일때_테스트() {
        assertThatThrownBy(() -> Validator.validateEmptyInput(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("유효성검증_입력이_공백으로구성되어있을때_테스트")
    @Test
    void 유효성검증_입력이_공백으로구성되어있을때_테스트() {
        assertThatThrownBy(() -> Validator.validateEmptyInput("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("유효성검사_주문형식1_테스트")
    @Test
    void 유효성검사_주문형식1_테스트() {
        List<String> orderForm = new ArrayList<>();
        orderForm.add("[사이다-3]");
        orderForm.add("(콜라-1)");

        assertThatThrownBy(() -> Validator.validateOrderForm(orderForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_ORDER_FORMAT);
    }

    @DisplayName("유효성검사_주문형식2_테스트")
    @Test
    void 유효성검사_주문형식2_테스트() {
        List<String> orderForm = new ArrayList<>();
        orderForm.add("[사이다-3]");
        orderForm.add("[[콜라-1]]");

        assertThatThrownBy(() -> Validator.validateOrderForm(orderForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_ORDER_FORMAT);
    }

    @DisplayName("유효성검사_주문형식3_테스트")
    @Test
    void 유효성검사_주문형식3_테스트() {
        List<String> orderForm = new ArrayList<>();
        orderForm.add("[사이다-3]");
        orderForm.add("[콜라--1]");

        assertThatThrownBy(() -> Validator.validateOrderForm(orderForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_ORDER_FORMAT);
    }

    @DisplayName("유효성검사_주문형식4_테스트")
    @Test
    void 유효성검사_주문형식4_테스트() {
        List<String> orderForm = new ArrayList<>();
        orderForm.add("[사이다-3]");
        orderForm.add("[콜라:1]");

        assertThatThrownBy(() -> Validator.validateOrderForm(orderForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_ORDER_FORMAT);
    }

    @DisplayName("유효성검사_존재하지않는제품을주문한경우_테스트")
    @Test
    void 유효성검사_존재하지않는제품을주문한경우_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("콜라", 1000, 10, null),
                new Product("오렌지주스", 1800, 9, "MD추천상품"),
                new Product("물", 500, 10, null)
        ));

        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 9, 0, null, 0, 0),
                new OrderedProduct("맥주", 10, 0, null, 0, 0)
        ));

        assertThatThrownBy(() -> Validator.validateOrderedProductsName(originalCatalog, orderSheet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.NON_EXISTENT_PRODUCT);
    }

    @DisplayName("유효성검사_재고보다많이주문한경우_테스트")
    @Test
    void 유효성검사_재고보다많이주문한경우_테스트() {
        Products originalCatalog = new Products(List.of(
                new Product("콜라", 1000, 10, "탄산2+1"),
                new Product("콜라", 1000, 10, null),
                new Product("오렌지주스", 1800, 9, "MD추천상품"),
                new Product("물", 500, 10, null)
        ));

        OrderSheet orderSheet = new OrderSheet(List.of(
                new OrderedProduct("콜라", 21, 0, null, 0, 0),
                new OrderedProduct("물", 5, 0, null, 0, 0)
        ));

        assertThatThrownBy(() -> Validator.validateOrderedProductsQuantity(originalCatalog, orderSheet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INSUFFICIENT_STOCK);
    }

    @DisplayName("유효성검사_응답입력_테스트")
    @Test
    void 유효성검사_응답입력_테스트() {
        assertThatThrownBy(() -> Validator.validateYesOrNoInput("YY"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }
}