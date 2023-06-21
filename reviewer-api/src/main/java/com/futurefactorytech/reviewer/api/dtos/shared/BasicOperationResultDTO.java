package com.futurefactorytech.reviewer.api.dtos.shared;

public class BasicOperationResultDTO extends OperationResultDTO {
    public BasicOperationResultDTO(Integer statusCode, String resultDescription) {
        super(statusCode, resultDescription);
    }
}
