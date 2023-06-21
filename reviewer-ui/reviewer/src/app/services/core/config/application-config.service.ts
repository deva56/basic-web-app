import { Injectable } from '@angular/core';
import { ApplicationGeneralConstants } from 'src/app/core/constants/app-general-constants';

@Injectable({
  providedIn: 'root'
})
export class ApplicationConfigService {

  private endpointPrefix = ApplicationGeneralConstants.apiUrlEndpoint

  constructor() { }

  getEndpointFor(api: string): string {
    return `${this.endpointPrefix}${api}`;
  }
}
