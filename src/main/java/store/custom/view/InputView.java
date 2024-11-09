package store.custom.view;

import static store.custom.view.DisplayConstants.MEMBERSHIP_DISCOUNT_PROMPT;
import static store.custom.view.DisplayConstants.NO_PROMOTION_APPLIED_PROMPT;
import static store.custom.view.DisplayConstants.PRODUCT_INPUT_PROMPT;
import static store.custom.view.DisplayConstants.PROMOTION_FREE_ITEM_PROMPT;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String inputProductsToPurchase() {
        System.out.println(PRODUCT_INPUT_PROMPT);
        return Console.readLine();
    }

    public String askForFreeProductAddition(String productName, int freeProductCount) {
        String message = String.format(PROMOTION_FREE_ITEM_PROMPT, productName, freeProductCount);
        System.out.println(message);
        return Console.readLine();
    }

    public String askForNoPromotionDiscount(String productName, int productsWithoutPromotion) {
        String message = String.format(NO_PROMOTION_APPLIED_PROMPT, productName, productsWithoutPromotion);
        System.out.println(message);
        return Console.readLine();
    }

    public String inputMembershipDiscount() {
        System.out.println(MEMBERSHIP_DISCOUNT_PROMPT);
        return Console.readLine();
    }
}