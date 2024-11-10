package store.custom.service.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.model.promotion.Promotion;
import store.custom.model.promotion.Promotions;

public class PromotionParserTest {

    @DisplayName("프로모션 파일 관리 EMPTY 테스트")
    @Test
    void 프로모션_파일_관리_EMPTY_테스트() {
        List<String> lines = List.of();
        Promotions promotions = PromotionParser.run(lines);

        assertNotNull(promotions);
        assertEquals(0, promotions.getPromotions().size());
    }

    @DisplayName("프로모션 파일 관리 NULL 테스트")
    @Test
    void 프로모션_파일_관리_NULL_테스트() {
        Promotions promotions = PromotionParser.run(null);

        assertNotNull(promotions);
        assertEquals(0, promotions.getPromotions().size());
    }

    @DisplayName("프로모션 파일 관리 정상 테스트")
    @Test
    void 프로모션_파일_관리_정상_테스트() {
        List<String> lines = List.of(
                "name,buy,get,start_date,end_date",
                "탄산2+1,2,1,2024-12-01,2024-12-31",
                "반짝할인,1,1,2024-11-25,2024-11-30"
        );

        Promotions promotions = PromotionParser.run(lines);

        assertNotNull(promotions);
        assertEquals(2, promotions.getPromotions().size());

        Promotion firstPromotion = promotions.getPromotions().get(0);
        assertEquals("탄산2+1", firstPromotion.getName());
        assertEquals(2, firstPromotion.getBuy());
        assertEquals(1, firstPromotion.getGet());
        assertEquals("2024-12-01", firstPromotion.getStartDate());
        assertEquals("2024-12-31", firstPromotion.getEndDate());

        Promotion secondPromotion = promotions.getPromotions().get(1);
        assertEquals("반짝할인", secondPromotion.getName());
        assertEquals(1, secondPromotion.getBuy());
        assertEquals(1, secondPromotion.getGet());
        assertEquals("2024-11-25", secondPromotion.getStartDate());
        assertEquals("2024-11-30", secondPromotion.getEndDate());
    }

    @DisplayName("프로모션 파일 관리 공백 처리 테스트")
    @Test
    void 프로모션_파일_관리_공백_처리_테스트() {
        List<String> lines = List.of(
                "Name,Buy,Get,StartDate,EndDate",
                "  탄산2+1  , 2 , 1 , 2024-12-01 , 2024-12-31 "
        );

        Promotions promotions = PromotionParser.run(lines);

        assertNotNull(promotions);
        assertEquals(1, promotions.getPromotions().size());

        Promotion promotion = promotions.getPromotions().get(0);
        assertEquals("탄산2+1", promotion.getName());
        assertEquals(2, promotion.getBuy());
        assertEquals(1, promotion.getGet());
        assertEquals("2024-12-01", promotion.getStartDate());
        assertEquals("2024-12-31", promotion.getEndDate());
    }
}