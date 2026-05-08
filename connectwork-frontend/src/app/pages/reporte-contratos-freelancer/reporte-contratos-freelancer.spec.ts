import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteContratosFreelancer } from './reporte-contratos-freelancer';

describe('ReporteContratosFreelancer', () => {
  let component: ReporteContratosFreelancer;
  let fixture: ComponentFixture<ReporteContratosFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteContratosFreelancer],
    }).compileComponents();

    fixture = TestBed.createComponent(ReporteContratosFreelancer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
