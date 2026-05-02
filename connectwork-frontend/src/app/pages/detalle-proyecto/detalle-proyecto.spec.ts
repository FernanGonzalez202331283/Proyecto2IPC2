import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleProyecto } from './detalle-proyecto';

describe('DetalleProyecto', () => {
  let component: DetalleProyecto;
  let fixture: ComponentFixture<DetalleProyecto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleProyecto],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleProyecto);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
