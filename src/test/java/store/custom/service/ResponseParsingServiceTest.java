package store.custom.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.validator.CustomErrorMessages;

public class ResponseParsingServiceTest {
    private final ResponseParsingService responseParsingService = new ResponseParsingService();

    @DisplayName("응답변환기_빈문자열_테스트")
    @Test
    void 응답변환기_빈문자열_테스트() {
        assertThatThrownBy(() -> responseParsingService.run(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("응답변환기_Y나N이아닌경우_테스트")
    @Test
    void 응답변환기_Y나N이아닌경우_테스트() {
        assertThatThrownBy(() -> responseParsingService.run("A"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CustomErrorMessages.INVALID_INPUT);
    }

    @DisplayName("응답변환기_공백제거_테스트")
    @Test
    void 응답변환기_공백제거_테스트() {
        assertEquals("Y", responseParsingService.run(" Y  "));
    }

    @DisplayName("응답변환기_정상_테스트")
    @Test
    void 응답변환기_정상_테스트() {
        assertEquals("Y", responseParsingService.run("Y"));
    }
}