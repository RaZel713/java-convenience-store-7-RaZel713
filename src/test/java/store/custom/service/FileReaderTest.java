package store.custom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.custom.constants.StringConstants.PRODUCTS_FILE_PATH;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.custom.service.filehandler.FileReader;

public class FileReaderTest {

    @DisplayName("파일리더기_정상_테스트")
    @Test
    void 파일리더기_정상_테스트() {
        List<String> expectedLines;
        expectedLines = List.of(
                "name,price,quantity,promotion",
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "사이다,1000,8,탄산2+1",
                "사이다,1000,7,null",
                "오렌지주스,1800,9,MD추천상품",
                "탄산수,1200,5,탄산2+1",
                "물,500,10,null",
                "비타민워터,1500,6,null",
                "감자칩,1500,5,반짝할인",
                "감자칩,1500,5,null",
                "초코바,1200,5,MD추천상품",
                "초코바,1200,5,null",
                "에너지바,2000,5,null",
                "정식도시락,6400,8,null",
                "컵라면,1700,1,MD추천상품",
                "컵라면,1700,10,null"
        );

        List<String> lines = FileReader.run(PRODUCTS_FILE_PATH);

        assertEquals(expectedLines, lines);
    }
}