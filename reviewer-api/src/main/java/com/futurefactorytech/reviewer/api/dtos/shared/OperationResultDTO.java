package com.futurefactorytech.reviewer.api.dtos.shared;

public abstract class OperationResultDTO {

    Integer statusCode;
    String resultDescription;

    public OperationResultDTO(Integer statusCode, String resultDescription) {
        this.statusCode = statusCode;
        this.resultDescription = resultDescription;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }
}
