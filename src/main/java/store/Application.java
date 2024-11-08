package store;

import store.custom.controller.StoreController;
import store.custom.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();

        StoreController storeController = new StoreController(outputView);
        storeController.start();
    }
}