import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSaldos } from './admin-saldos';

describe('AdminSaldos', () => {
  let component: AdminSaldos;
  let fixture: ComponentFixture<AdminSaldos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSaldos],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminSaldos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
