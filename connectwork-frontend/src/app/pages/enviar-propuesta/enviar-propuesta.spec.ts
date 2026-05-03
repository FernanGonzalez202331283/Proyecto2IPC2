import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnviarPropuesta } from './enviar-propuesta';

describe('EnviarPropuesta', () => {
  let component: EnviarPropuesta;
  let fixture: ComponentFixture<EnviarPropuesta>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnviarPropuesta],
    }).compileComponents();

    fixture = TestBed.createComponent(EnviarPropuesta);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
