import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletarFreelancer } from './completar-freelancer';

describe('CompletarFreelancer', () => {
  let component: CompletarFreelancer;
  let fixture: ComponentFixture<CompletarFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompletarFreelancer],
    }).compileComponents();

    fixture = TestBed.createComponent(CompletarFreelancer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
