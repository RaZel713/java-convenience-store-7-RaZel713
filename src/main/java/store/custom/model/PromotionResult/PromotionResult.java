package store.custom.model.PromotionResult;

public class PromotionResult {
    private final int applicablePromotionCount; // 프로모션 적용가능 횟수
    private final int extraPromotionCount; // 프로모션 추가 적용가능 횟수
    private final int nonPromotionProductCount; // 프로모션 미적용상품 갯수

    public PromotionResult(int applicablePromotionCount, int extraPromotionCount, int nonPromotionProductCount) {
        this.applicablePromotionCount = applicablePromotionCount;
        this.extraPromotionCount = extraPromotionCount;
        this.nonPromotionProductCount = nonPromotionProductCount;
    }

    public int getApplicablePromotionCount() {
        return applicablePromotionCount;
    }

    public int getExtraPromotionCount() {
        return extraPromotionCount;
    }

    public int getNonPromotionProductCount() {
        return nonPromotionProductCount;
    }
}