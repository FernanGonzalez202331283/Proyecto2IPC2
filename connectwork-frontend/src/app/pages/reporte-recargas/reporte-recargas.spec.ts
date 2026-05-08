import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteRecargas } from './reporte-recargas';

describe('ReporteRecargas', () => {
  let component: ReporteRecargas;
  let fixture: ComponentFixture<ReporteRecargas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteRecargas],
    }).compileComponents();

    fixture = TestBed.createComponent(ReporteRecargas);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
