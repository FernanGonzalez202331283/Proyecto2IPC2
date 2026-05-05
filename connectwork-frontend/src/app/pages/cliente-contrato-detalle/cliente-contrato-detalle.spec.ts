import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClienteContratoDetalle } from './cliente-contrato-detalle';

describe('ClienteContratoDetalle', () => {
  let component: ClienteContratoDetalle;
  let fixture: ComponentFixture<ClienteContratoDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteContratoDetalle],
    }).compileComponents();

    fixture = TestBed.createComponent(ClienteContratoDetalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
