import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContratosActivos } from './contratos-activos';

describe('ContratosActivos', () => {
  let component: ContratosActivos;
  let fixture: ComponentFixture<ContratosActivos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContratosActivos],
    }).compileComponents();

    fixture = TestBed.createComponent(ContratosActivos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
