import { Component, OnInit } from '@angular/core';
import { AdministrationService } from 'src/app/services/administration/administration.service';
import SwaggerUI from 'swagger-ui';

@Component({
  selector: 'app-swagger-ui',
  templateUrl: './swagger-ui.component.html',
  styleUrls: ['./swagger-ui.component.css']
})
export class SwaggerUiComponent implements OnInit {

  constructor(private administrationService: AdministrationService) { }

  ngOnInit(): void {
    this.fetchOpenApiInfo();
  }

  fetchOpenApiInfo() {
    this.administrationService.getOpenApiInfo().subscribe(result => {
      SwaggerUI({
        dom_id: "#swagger-ui",
        spec: result
      });
    })
  }
}
