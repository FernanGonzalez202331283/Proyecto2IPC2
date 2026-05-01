import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletarCliente } from './completar-cliente';

describe('CompletarCliente', () => {
  let component: CompletarCliente;
  let fixture: ComponentFixture<CompletarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompletarCliente],
    }).compileComponents();

    fixture = TestBed.createComponent(CompletarCliente);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
