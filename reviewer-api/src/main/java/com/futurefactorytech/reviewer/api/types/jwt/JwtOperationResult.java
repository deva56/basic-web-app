package com.futurefactorytech.reviewer.api.types.jwt;

public record JwtOperationResult<T, K>(T operationResult, K possibleOperationException) {

}
