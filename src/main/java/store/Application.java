package store;

import store.custom.controller.StoreController;
import store.custom.view.InputView;
import store.custom.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        
        StoreController storeController = new StoreController(inputView, outputView);
        storeController.start();
    }
}