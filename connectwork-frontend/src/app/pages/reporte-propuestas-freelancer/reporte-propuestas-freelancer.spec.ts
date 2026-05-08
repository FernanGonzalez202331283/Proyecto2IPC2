import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportePropuestasFreelancer } from './reporte-propuestas-freelancer';

describe('ReportePropuestasFreelancer', () => {
  let component: ReportePropuestasFreelancer;
  let fixture: ComponentFixture<ReportePropuestasFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportePropuestasFreelancer],
    }).compileComponents();

    fixture = TestBed.createComponent(ReportePropuestasFreelancer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
