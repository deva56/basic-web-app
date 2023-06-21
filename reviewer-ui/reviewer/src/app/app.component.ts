import { Component } from '@angular/core';
import { ApplicationGeneralConstants } from './core/constants/app-general-constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = ApplicationGeneralConstants.applicationName;
}
