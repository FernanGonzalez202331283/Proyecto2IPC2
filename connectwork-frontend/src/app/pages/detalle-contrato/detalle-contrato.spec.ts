import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleContrato } from './detalle-contrato';

describe('DetalleContrato', () => {
  let component: DetalleContrato;
  let fixture: ComponentFixture<DetalleContrato>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleContrato],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleContrato);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
