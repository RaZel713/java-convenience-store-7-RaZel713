package store.custom.model.product;

import java.util.ArrayList;
import java.util.List;

public class Products {
    private final List<Product> productCatalog;

    public Products(List<Product> productCatalog) {
        this.productCatalog = new ArrayList<>(productCatalog);
    }

    public List<Product> getProducts() {
        return productCatalog;
    }

    public int getProductsSize() {
        return productCatalog.size();
    }

    public Product getProductByIndex(int index) {
        if (index < 0 || index >= productCatalog.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return productCatalog.get(index);
    }
}