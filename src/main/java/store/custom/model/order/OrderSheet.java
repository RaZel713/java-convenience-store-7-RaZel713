package store.custom.model.order;

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

    public int getOrderSheetSize() {
        return orderSheet.size();
    }

    public OrderedProduct getOrderSheetByIndex(int index) {
        if (index < 0 || index >= orderSheet.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return orderSheet.get(index);
    }
}