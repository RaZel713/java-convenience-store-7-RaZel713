package store.custom.model.PromotionResult;

import static store.custom.validator.CustomErrorMessages.INVALID_INDEX;

import java.util.List;

public class PromotionResults {
    private final List<PromotionResult> promotionResults;

    public PromotionResults(List<PromotionResult> promotionResults) {
        this.promotionResults = promotionResults;
    }

    public int getPromotionResultCount() {
        return promotionResults.size();
    }

    public PromotionResult getPromotionResultByIndex(int index) {
        if (index < 0 || index >= promotionResults.size()) {
            throw new IndexOutOfBoundsException(INVALID_INDEX + index);
        }
        return promotionResults.get(index);
    }
}