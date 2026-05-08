import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitarCategoria } from './solicitar-categoria';

describe('SolicitarCategoria', () => {
  let component: SolicitarCategoria;
  let fixture: ComponentFixture<SolicitarCategoria>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolicitarCategoria],
    }).compileComponents();

    fixture = TestBed.createComponent(SolicitarCategoria);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
