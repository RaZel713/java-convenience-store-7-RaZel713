package store;

import store.custom.controller.StoreController;
import store.custom.view.InputView;
import store.custom.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();

        StoreController storeController = new StoreController(outputView, inputView);
        storeController.start();
    }
}