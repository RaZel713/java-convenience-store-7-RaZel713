package store.custom.Utils;

import static store.custom.constants.RegexConstants.CONSECUTIVE_COMMAS_REGEX;
import static store.custom.constants.RegexConstants.EMPTY_STRING;
import static store.custom.constants.RegexConstants.LEADING_TRAILING_COMMA_REGEX;
import static store.custom.constants.RegexConstants.SINGLE_COMMA;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    public static String cleanConsecutiveCommas(String input) {
        return input.replaceAll(CONSECUTIVE_COMMAS_REGEX, SINGLE_COMMA)
                .replaceAll(LEADING_TRAILING_COMMA_REGEX, EMPTY_STRING);
    }

    public static List<String> splitStringByDelimiter(String input, String delimiter) {
        return Arrays.asList(input.split(delimiter));
    }

    public static List<String> trimAndFilterEmptyStrings(List<String> input) {
        return input.stream() // 리스트를 스트림으로 변환
                .map(String::trim) // 각 문자열에 trim() 적용
                .filter(s -> !s.isEmpty()) // 빈 문자열 제외
                .collect(Collectors.toList()); // 리스트로 수집
    }
}
