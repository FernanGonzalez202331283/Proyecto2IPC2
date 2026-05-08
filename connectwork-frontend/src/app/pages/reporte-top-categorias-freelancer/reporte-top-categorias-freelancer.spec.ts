import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteTopCategoriasFreelancer } from './reporte-top-categorias-freelancer';

describe('ReporteTopCategoriasFreelancer', () => {
  let component: ReporteTopCategoriasFreelancer;
  let fixture: ComponentFixture<ReporteTopCategoriasFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteTopCategoriasFreelancer],
    }).compileComponents();

    fixture = TestBed.createComponent(ReporteTopCategoriasFreelancer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
