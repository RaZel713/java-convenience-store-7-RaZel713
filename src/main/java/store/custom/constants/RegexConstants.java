package store.custom.constants;

public class RegexConstants {
    public static final String PRODUCT_ORDER_REGEX = "\\[[A-Za-z가-힣]+-\\d+\\]"; // 상품명-수량 형식

    public static final String CONSECUTIVE_COMMAS_REGEX = ",+";

    public static final String SINGLE_COMMA = ",";

    public static final String LEADING_TRAILING_COMMA_REGEX = "^,|,$";

    public static final String EMPTY_STRING = "";

    public static final String HYPHEN = "-";
}