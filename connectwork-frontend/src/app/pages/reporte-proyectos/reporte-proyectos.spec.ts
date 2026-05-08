import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteProyectos } from './reporte-proyectos';

describe('ReporteProyectos', () => {
  let component: ReporteProyectos;
  let fixture: ComponentFixture<ReporteProyectos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteProyectos],
    }).compileComponents();

    fixture = TestBed.createComponent(ReporteProyectos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
