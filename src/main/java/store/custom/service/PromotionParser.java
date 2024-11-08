package store.custom.service;

import java.util.ArrayList;
import java.util.List;
import store.custom.constants.RegexConstants;
import store.custom.model.promotion.Promotion;
import store.custom.model.promotion.Promotions;

public class PromotionParser {
    public static Promotions run(List<String> lines) {
        List<Promotion> promotions = new ArrayList<>();

        if (lines != null) {
            parsePromotionLines(lines, promotions);
        }

        return new Promotions(promotions);
    }

    private static void parsePromotionLines(List<String> lines, List<Promotion> promotions) {
        for (int currentLine = 1; currentLine < lines.size(); currentLine++) {
            List<String> currentLineParts = List.of(lines.get(currentLine).split(RegexConstants.SINGLE_COMMA));
            Promotion promotion = createPromotion(currentLineParts);
            promotions.add(promotion);
        }
    }

    private static Promotion createPromotion(List<String> parts) {
        String name = extractPromotionName(parts);
        int buy = extractPromotionBuy(parts);
        int get = extractPromotionGet(parts);
        String startDate = extractPromotionStartDate(parts);
        String endDate = extractPromotionEndDate(parts);

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private static String extractPromotionName(List<String> parts) {
        return parts.get(0).trim();
    }

    private static int extractPromotionBuy(List<String> parts) {
        return Integer.parseInt(parts.get(1).trim());
    }

    private static int extractPromotionGet(List<String> parts) {
        return Integer.parseInt(parts.get(2).trim());
    }

    private static String extractPromotionStartDate(List<String> parts) {
        return parts.get(3).trim();
    }

    private static String extractPromotionEndDate(List<String> parts) {
        return parts.get(4).trim();
    }
}
