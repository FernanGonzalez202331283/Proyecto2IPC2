import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalificarFreelancer } from './calificar-freelancer';

describe('CalificarFreelancer', () => {
  let component: CalificarFreelancer;
  let fixture: ComponentFixture<CalificarFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalificarFreelancer],
    }).compileComponents();

    fixture = TestBed.createComponent(CalificarFreelancer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
