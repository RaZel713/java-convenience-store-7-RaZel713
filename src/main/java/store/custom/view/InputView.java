package store.custom.view;

import static store.custom.view.DisplayConstants.ADDITIONAL_PURCHASE_PROMPT;
import static store.custom.view.DisplayConstants.MEMBERSHIP_DISCOUNT_PROMPT;
import static store.custom.view.DisplayConstants.NON_PROMOTIONAL_PRODUCT_PROMPT;
import static store.custom.view.DisplayConstants.PRODUCT_TO_PURCHASE_PROMPT;
import static store.custom.view.DisplayConstants.PROMOTION_FREEBIE_PROMPT;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String askForProductsToPurchase() {
        System.out.println(PRODUCT_TO_PURCHASE_PROMPT);
        return Console.readLine();
    }

    public String askForFreebie(String productName, int freeProductCount) {
        String message = String.format(PROMOTION_FREEBIE_PROMPT, productName, freeProductCount);
        System.out.println(message);
        return Console.readLine();
    }

    public String askForProductWithoutPromotion(String productName, int productsWithoutPromotion) {
        String message = String.format(NON_PROMOTIONAL_PRODUCT_PROMPT, productName, productsWithoutPromotion);
        System.out.println(message);
        return Console.readLine();
    }

    public String askForMembershipDiscount() {
        System.out.println(MEMBERSHIP_DISCOUNT_PROMPT);
        return Console.readLine();
    }

    public String askForAdditionalPurchase() {
        System.out.println(ADDITIONAL_PURCHASE_PROMPT);
        return Console.readLine();
    }
}