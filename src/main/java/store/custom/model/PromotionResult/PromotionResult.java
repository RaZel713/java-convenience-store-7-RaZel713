package store.custom.model.PromotionResult;

public class PromotionResult {
    private final int promotionAppliedCount; // 프로모션 적용가능 횟수
    private final int extraPromotionCount; // 프로모션 추가 적용가능 횟수
    private final int nonPromotionProductCount; // 프로모션 미적용상품 갯수

    public PromotionResult(int promotionAppliedCount, int extraPromotionCount, int nonPromotionProductCount) {
        this.promotionAppliedCount = promotionAppliedCount;
        this.extraPromotionCount = extraPromotionCount;
        this.nonPromotionProductCount = nonPromotionProductCount;
    }

    public int getPromotionAppliedCount() {
        return promotionAppliedCount;
    }

    public int getExtraPromotionCount() {
        return extraPromotionCount;
    }

    public int getNonPromotionProductCount() {
        return nonPromotionProductCount;
    }
}