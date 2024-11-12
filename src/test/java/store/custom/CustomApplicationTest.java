package store.custom;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static store.custom.validator.CustomErrorMessages.INSUFFICIENT_STOCK;
import static store.custom.validator.CustomErrorMessages.INVALID_INPUT;
import static store.custom.validator.CustomErrorMessages.INVALID_ORDER_FORMAT;
import static store.custom.validator.CustomErrorMessages.NON_EXISTENT_PRODUCT;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;
import store.Application;

public class CustomApplicationTest extends NsTest {
    @Test
    void 프리코스실행결과예시_영수증출력_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "Y", "Y", "[콜라-10]", "Y", "N", "Y", "[오렌지주스-1]", "Y", "Y", "N");
            assertThat(output().replaceAll("\\s", "")).contains("콜라33,000");
            assertThat(output().replaceAll("\\s", "")).contains("에너지바510,000");
            assertThat(output().replaceAll("\\s", "")).contains("콜라1");
            assertThat(output().replaceAll("\\s", "")).contains("총구매액813,000");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-1,000");
            assertThat(output().replaceAll("\\s", "")).contains("멤버십할인-3,000");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈9,000");

            assertThat(output().replaceAll("\\s", "")).contains("콜라1010,000");
            assertThat(output().replaceAll("\\s", "")).contains("콜라2");
            assertThat(output().replaceAll("\\s", "")).contains("총구매액1010,000");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-2,000");
            assertThat(output().replaceAll("\\s", "")).contains("멤버십할인-0");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈8,000");

            assertThat(output().replaceAll("\\s", "")).contains("오렌지주스23,600");
            assertThat(output().replaceAll("\\s", "")).contains("오렌지주스1");
            assertThat(output().replaceAll("\\s", "")).contains("총구매액23,600");
            assertThat(output().replaceAll("\\s", "")).contains("행사할인-1,800");
            assertThat(output().replaceAll("\\s", "")).contains("멤버십할인-0");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈1,800");
        });
    }

    @Test
    void 프리코스실행결과예시_재고내역출력_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "Y", "Y", "[콜라-10]", "Y", "N", "Y", "[오렌지주스-1]", "Y", "Y", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
            assertThat(output()).contains(
                    "- 콜라 1,000원 7개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 재고 없음",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
            assertThat(output()).contains(
                    "- 콜라 1,000원 재고 없음 탄산2+1",
                    "- 콜라 1,000원 7개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 재고 없음",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    // 주문 입력 예외 처리

    @Test
    void 주문입력_예외처리_빈문자열일때_테스트() {
        assertSimpleTest(() -> {
            runException("", "N", "N");
            assertThat(output()).contains(INVALID_INPUT);
        });
    }

    @Test
    void 주문입력_예외처리_공백으로만이루어졌을때_테스트() {
        assertSimpleTest(() -> {
            runException("       ", "N", "N");
            assertThat(output()).contains(INVALID_INPUT);
        });
    }

    @Test
    void 주문입력_예외처리_주문형식과다를때1_테스트() {
        assertSimpleTest(() -> {
            runException("[[콜라-12]]", "N", "N");
            assertThat(output()).contains(INVALID_ORDER_FORMAT);
        });
    }

    @Test
    void 주문입력_예외처리_주문형식과다를때2_테스트() {
        assertSimpleTest(() -> {
            runException("[-콜라-12-]", "N", "N");
            assertThat(output()).contains(INVALID_ORDER_FORMAT);
        });
    }

    @Test
    void 주문입력_예외처리_주문형식과다를때3_테스트() {
        assertSimpleTest(() -> {
            runException("[콜라--12]", "N", "N");
            assertThat(output()).contains(INVALID_ORDER_FORMAT);
        });
    }

    @Test
    void 주문입력_예외처리_주문형식과다를때4_테스트() {
        assertSimpleTest(() -> {
            runException("[콜라,12],[사이다-1]", "N", "N");
            assertThat(output()).contains(INVALID_ORDER_FORMAT);
        });
    }

    @Test
    void 주문입력_예외처리_주문형식과다를때5_테스트() {
        assertSimpleTest(() -> {
            runException("[콜라,12][사이다-1]", "N", "N");
            assertThat(output()).contains(INVALID_ORDER_FORMAT);
        });
    }

    @Test
    void 주문입력_예외처리_존재하지않는상품일때_테스트() {
        assertSimpleTest(() -> {
            runException("[맥주-12],[콜라-3]", "N", "N");
            assertThat(output()).contains(NON_EXISTENT_PRODUCT);
        });
    }

    @Test
    void 주문입력_예외처리_재고의수량을초과할때_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains(INSUFFICIENT_STOCK);
        });
    }

    // 응답 입력 예외 처리

    @Test
    void 응답입력_예외처리_빈문자열일때_테스트() {
        assertSimpleTest(() -> {
            runException("[콜라-3]", "", "N");
            assertThat(output()).contains(INVALID_INPUT);
        });
    }

    @Test
    void 응답입력_예외처리_공백으로만이루어졌을때_테스트() {
        assertSimpleTest(() -> {
            runException("[콜라-3]", "      ");
            assertThat(output()).contains(INVALID_INPUT);
        });
    }

    @Test
    void 응답입력_예외처리_Y나N이아닐때_테스트() {
        assertSimpleTest(() -> {
            runException("[콜라-3]", "YY");
            assertThat(output()).contains(INVALID_INPUT);
        });
    }

    // 주문 입력 기능 테스트

    @Test
    void 주문입력_대괄호앞뒤공백처리_테스트() {
        assertSimpleTest(() -> {
            run(" [비타민워터-3] , [물-2] , [정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 주문입력_연속된쉼표처리_테스트() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],,,[물-2],,,[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 주문입력_앞뒤쉼표처리_테스트() {
        assertSimpleTest(() -> {
            run(",[비타민워터-3],[물-2],[정식도시락-2],", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 주문입력_앞뒤연속된쉼표처리_테스트() {
        assertSimpleTest(() -> {
            run(",,,[비타민워터-3],[물-2],[정식도시락-2],,,", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    // 응답 입력 기능 테스트

    @Test
    void 응답입력_공백처리_테스트() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", " N   ", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}