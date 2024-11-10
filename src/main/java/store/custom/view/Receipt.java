package store.custom.view;

public enum Receipt {
    TITLE("==============W 편의점================"),
    ITEM_HEADER("상품명\t\t\t수량\t\t금액"),
    ITEM_NAME("%s\t\t\t%d\t\t%,d"),
    FREE_ITEM("=============증	정==============="),
    FREE_ITEM_NAME("%s\t\t\t%d"),
    DIVIDING_LINE("===================================="),
    TOTAL("총구매액\t\t\t%d\t\t\t%,d"),
    PROMOTION_DISCOUNT("행사할인\t\t\t\t\t\t-%,d"),
    MEMBERSHIP_DISCOUNT("멤버십할인\t\t\t\t\t\t-%,d"),
    FINAL_PRICE("내실돈\t\t\t\t\t\t%,d");

    private final String format;

    Receipt(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public String format(Object... args) {
        return String.format(format, args);
    }
}