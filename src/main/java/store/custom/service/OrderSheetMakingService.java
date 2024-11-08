package store.custom.service;

import store.custom.model.OrderSheet;
import store.custom.model.OrderSheetMaker;
import store.custom.model.product.Products;
import store.custom.validator.Validator;

public class OrderSheetMakingService {
    private final OrderSheetMaker orderSheetMaker;

    public OrderSheetMakingService() {
        this.orderSheetMaker = new OrderSheetMaker();
    }

    public OrderSheet run(String orderRequest, Products productCatalog) {
        OrderSheet orderSheet = orderSheetMaker.run(orderRequest);

        Validator.validateOrderedProductsName(productCatalog, orderSheet);
        Validator.validateOrderedProductsQuantity(productCatalog, orderSheet);

        return orderSheet;
    }
}