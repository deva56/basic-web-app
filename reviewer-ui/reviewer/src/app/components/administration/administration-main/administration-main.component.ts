import { Component } from '@angular/core';
import { ApplicationGeneralConstants } from 'src/app/core/constants/app-general-constants';

@Component({
  selector: 'app-administration-main',
  templateUrl: './administration-main.component.html',
  styleUrls: ['./administration-main.component.css']
})
export class AdministrationMainComponent {

  apiUrlEndpoint = ApplicationGeneralConstants.apiUrlEndpoint;

  constructor() { }

}
