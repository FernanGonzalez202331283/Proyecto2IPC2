import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClienteContratos } from './cliente-contratos';

describe('ClienteContratos', () => {
  let component: ClienteContratos;
  let fixture: ComponentFixture<ClienteContratos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteContratos],
    }).compileComponents();

    fixture = TestBed.createComponent(ClienteContratos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
