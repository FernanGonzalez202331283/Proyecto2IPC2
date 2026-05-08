import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopFreelancers } from './top-freelancers';

describe('TopFreelancers', () => {
  let component: TopFreelancers;
  let fixture: ComponentFixture<TopFreelancers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopFreelancers],
    }).compileComponents();

    fixture = TestBed.createComponent(TopFreelancers);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
