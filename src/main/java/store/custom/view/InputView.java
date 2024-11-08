package store.custom.view;

import static store.custom.view.DisplayConstants.PRODUCT_INPUT_PROMPT;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String inputProductsToPurchase() {
        System.out.println(PRODUCT_INPUT_PROMPT);
        return Console.readLine();
    }
}