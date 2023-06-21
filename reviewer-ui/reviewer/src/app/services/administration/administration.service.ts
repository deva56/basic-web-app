import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { Observable } from 'rxjs';
import { BasicOperationValueResult } from 'src/app/models/shared/basic-operation-value-result';
import { UserDTO } from 'src/app/models/administration/userDTO';
import { ApiRoutesConstants } from 'src/app/core/constants/api-routes-constants';
import { PaginationDTO } from 'src/app/models/shared/paginationDTO';

@Injectable({
  providedIn: 'root'
})
export class AdministrationService {

  administrationApi: string = ApiRoutesConstants.administrationApi;

  constructor(private httpClient: HttpClient,
    private applicationConfigService: ApplicationConfigService) { }

  getAllUsers(options: {}): Observable<BasicOperationValueResult<PaginationDTO<UserDTO[]>>> {
    return this.httpClient.get<BasicOperationValueResult<PaginationDTO<UserDTO[]>>>(this.applicationConfigService.getEndpointFor(`${this.administrationApi}/get-all-users`), { params: options });
  }

  getOpenApiInfo(): Observable<Object> {
    return this.httpClient.get(this.applicationConfigService.getEndpointFor("docs/api-docs"));
  }
}
