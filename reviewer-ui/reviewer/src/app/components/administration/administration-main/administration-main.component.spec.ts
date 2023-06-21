import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrationMainComponent } from './administration-main.component';

describe('AdministrationMainComponent', () => {
  let component: AdministrationMainComponent;
  let fixture: ComponentFixture<AdministrationMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdministrationMainComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrationMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
