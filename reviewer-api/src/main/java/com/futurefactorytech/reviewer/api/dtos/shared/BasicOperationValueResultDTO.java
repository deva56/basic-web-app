package com.futurefactorytech.reviewer.api.dtos.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BasicOperationValueResultDTO<T> extends OperationResultDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T value;

    public BasicOperationValueResultDTO(Integer statusCode, String resultDescription, T value) {
        super(statusCode, resultDescription);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
