export class PaginationDTO<T> {
    totalPages: number = 0; 
    totalItems: number = 0; 
    currentPage: number = 0; 
    value: T | any;
}