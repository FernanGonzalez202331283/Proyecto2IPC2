import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitarHabilidad } from './solicitar-habilidad';

describe('SolicitarHabilidad', () => {
  let component: SolicitarHabilidad;
  let fixture: ComponentFixture<SolicitarHabilidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitarHabilidad],
    }).compileComponents();

    fixture = TestBed.createComponent(SolicitarHabilidad);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
