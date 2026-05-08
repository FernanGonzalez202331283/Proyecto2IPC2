import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteGastosCategoria } from './reporte-gastos-categoria';

describe('ReporteGastosCategoria', () => {
  let component: ReporteGastosCategoria;
  let fixture: ComponentFixture<ReporteGastosCategoria>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteGastosCategoria],
    }).compileComponents();

    fixture = TestBed.createComponent(ReporteGastosCategoria);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
