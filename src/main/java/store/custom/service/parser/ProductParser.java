package store.custom.service.parser;

import java.util.ArrayList;
import java.util.List;
import store.custom.constants.RegexConstants;
import store.custom.model.product.Product;
import store.custom.model.product.Products;

public class ProductParser {
    public static Products run(List<String> lines) {
        List<Product> productCatalog = new ArrayList<>();

        if (lines != null) {
            parseProductLines(lines, productCatalog);
        }

        return new Products(productCatalog);
    }

    private static void parseProductLines(List<String> lines, List<Product> productCatalog) {
        for (int currentLine = 1; currentLine < lines.size(); currentLine++) {
            List<String> currentLineParts = List.of(lines.get(currentLine).split(RegexConstants.SINGLE_COMMA));
            Product product = createProduct(currentLineParts);
            productCatalog.add(product);
        }
    }

    private static Product createProduct(List<String> parts) {
        String name = extractProductName(parts);
        int price = extractProductPrice(parts);
        int quantity = extractProductQuantity(parts);
        String promotion = extractProductPromotion(parts);

        return new Product(name, price, quantity, promotion);
    }

    private static String extractProductName(List<String> parts) {
        return parts.get(0).trim();
    }

    private static int extractProductPrice(List<String> parts) {
        return Integer.parseInt(parts.get(1).trim());
    }

    private static int extractProductQuantity(List<String> parts) {
        return Integer.parseInt(parts.get(2).trim());
    }

    private static String extractProductPromotion(List<String> parts) {
        String promotion = parts.get(3).trim();
        if (promotion.equals("null")) {
            return null;
        }
        return promotion;
    }
}