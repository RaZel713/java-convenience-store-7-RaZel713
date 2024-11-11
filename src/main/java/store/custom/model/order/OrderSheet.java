package store.custom.model.order;

import static store.custom.validator.CustomErrorMessages.INVALID_INDEX;

import java.util.ArrayList;
import java.util.List;

public class OrderSheet {
    private final List<OrderedProduct> orderSheet;

    public OrderSheet(List<OrderedProduct> orderSheet) {
        this.orderSheet = new ArrayList<>(orderSheet);
    }

    public List<OrderedProduct> getOrderSheet() {
        return orderSheet;
    }

    public OrderedProduct getOrderSheetByIndex(int index) {
        if (index < 0 || index >= orderSheet.size()) {
            throw new IndexOutOfBoundsException(INVALID_INDEX + index);
        }
        return orderSheet.get(index);
    }
}