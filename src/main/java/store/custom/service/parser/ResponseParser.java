package store.custom.service.parser;

import store.custom.validator.Validator;

public class ResponseParser {

    public String run(String response) {
        Validator.validateEmptyInput(response);

        response = response.trim();

        Validator.validateYesOrNoInput(response);

        return response;
    }
}