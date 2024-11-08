package store.custom.model;

import java.util.ArrayList;
import java.util.List;
import store.custom.model.product.Product;

public class OrderSheet {
    private final List<Product> orderSheet;

    public OrderSheet(List<Product> orderSheet) {
        this.orderSheet = new ArrayList<>(orderSheet);
    }

    public List<Product> getOrderSheet() {
        return orderSheet;
    }

    public int getOrderSheetSize() {
        return orderSheet.size();
    }

    public Product getOrderSheetByIndex(int index) {
        if (index < 0 || index >= orderSheet.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return orderSheet.get(index);
    }
}