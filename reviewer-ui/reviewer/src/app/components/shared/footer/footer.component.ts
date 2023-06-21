import { Component } from '@angular/core';
import { ApplicationGeneralConstants } from 'src/app/core/constants/app-general-constants';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {

  currentYear: number = new Date().getFullYear();
  companyName: string = ApplicationGeneralConstants.companyName;

}
