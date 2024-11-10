package store.custom.model.PromotionResult;

import java.util.List;

public class PromotionResults {
    private final List<PromotionResult> promotionResults;

    public PromotionResults(List<PromotionResult> promotionResults) {
        this.promotionResults = promotionResults;
    }

    public List<PromotionResult> getPromotionResults() {
        return promotionResults;
    }

    public int getPromotionResultCount() {
        return promotionResults.size();
    }

    public PromotionResult getPromotionResultByIndex(int index) {
        if (index < 0 || index >= promotionResults.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return promotionResults.get(index);
    }
}