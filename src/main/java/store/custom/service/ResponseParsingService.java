package store.custom.service;

import store.custom.validator.Validator;

public class ResponseParsingService {

    public String run(String response) {
        Validator.validateEmptyInput(response);

        response = response.trim();

        Validator.validateYesOrNoInput(response);

        return response;
    }
}