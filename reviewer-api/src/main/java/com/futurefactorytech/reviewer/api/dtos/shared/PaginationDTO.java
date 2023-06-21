package com.futurefactorytech.reviewer.api.dtos.shared;

public record PaginationDTO<T>(int totalPages, long totalItems, int currentPage, T value) {

}
