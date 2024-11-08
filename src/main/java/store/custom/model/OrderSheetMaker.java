package store.custom.model;

import static store.custom.constants.RegexConstants.HYPHEN;
import static store.custom.constants.RegexConstants.SINGLE_COMMA;

import java.util.ArrayList;
import java.util.List;
import store.custom.Utils.StringUtils;
import store.custom.validator.Validator;

public class OrderSheetMaker {
    public OrderSheet run(String orderRequest) {
        Validator.validateEmptyInput(orderRequest); // 빈입력 유효성 검사

        List<String> orderForms = parseOrderRequest(orderRequest);

        return createOrderSheet(orderForms);
    }

    private static List<String> parseOrderRequest(String orderRequest) {
        String cleanedComma = StringUtils.cleanConsecutiveCommas(orderRequest);
        List<String> orderForm = StringUtils.splitStringByDelimiter(cleanedComma, SINGLE_COMMA);
        List<String> filteredOrderForms = StringUtils.trimAndFilterEmptyStrings(orderForm);

        Validator.validateOrderForm(filteredOrderForms); // 입력 형식 검사

        return filteredOrderForms;
    }

    private static OrderSheet createOrderSheet(List<String> orderForms) {
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for (String orderForm : orderForms) {
            OrderedProduct orderedProduct = createOrderedProduct(parseOrderForm(orderForm));
            orderedProducts.add(orderedProduct);
        }
        return new OrderSheet(orderedProducts);
    }

    private static List<String> parseOrderForm(String orderForm) {
        orderForm = orderForm.substring(1, orderForm.length() - 1); // 대괄호 제거
        return StringUtils.splitStringByDelimiter(orderForm, HYPHEN);
    }

    private static OrderedProduct createOrderedProduct(List<String> parts) {
        return new OrderedProduct(parts.get(0).trim(), Integer.parseInt(parts.get(1).trim()), 0, null, 0, 0);
    }
}