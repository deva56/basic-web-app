import { BasicOperationResult } from "./basic-operation-result";

export class BasicOperationValueResult<T> extends BasicOperationResult {
    value: T | any  ;
}