package store.custom.service.parser;

import static store.custom.constants.RegexConstants.HYPHEN;
import static store.custom.constants.RegexConstants.SINGLE_COMMA;

import java.util.ArrayList;
import java.util.List;
import store.custom.Utils.StringUtils;
import store.custom.model.order.OrderSheet;
import store.custom.model.order.OrderedProduct;
import store.custom.model.product.Products;
import store.custom.validator.Validator;

public class OrderParser {
    public OrderSheet run(String orderRequest, Products productCatalog) {
        Validator.validateEmptyInput(orderRequest);
        List<String> orderForms = parseOrderRequest(orderRequest);
        OrderSheet orderSheet = createOrderSheet(orderForms);

        Validator.validateOrderSheet(productCatalog, orderSheet);

        return orderSheet;
    }

    private List<String> parseOrderRequest(String orderRequest) {
        String cleanedComma = StringUtils.cleanConsecutiveCommas(orderRequest);
        List<String> orderForm = StringUtils.splitStringByDelimiter(cleanedComma, SINGLE_COMMA);
        List<String> filteredOrderForms = StringUtils.trimAndFilterEmptyStrings(orderForm);

        Validator.validateOrderForm(filteredOrderForms);

        return filteredOrderForms;
    }

    private OrderSheet createOrderSheet(List<String> orderForms) {
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for (String orderForm : orderForms) {
            OrderedProduct orderedProduct = createOrderedProduct(parseOrderForm(orderForm));
            orderedProducts.add(orderedProduct);
        }
        return new OrderSheet(orderedProducts);
    }

    private List<String> parseOrderForm(String orderForm) {
        orderForm = orderForm.substring(1, orderForm.length() - 1); // 대괄호 제거
        return StringUtils.splitStringByDelimiter(orderForm, HYPHEN);
    }

    private OrderedProduct createOrderedProduct(List<String> orderForm) {
        String orderedProductName = orderForm.get(0).trim();
        int orderedProductQuantity = Integer.parseInt(orderForm.get(1).trim());
        return new OrderedProduct(orderedProductName, orderedProductQuantity, 0, null, 0, 0);
    }
}