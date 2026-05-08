import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUsuario } from './admin-usuario';

describe('AdminUsuario', () => {
  let component: AdminUsuario;
  let fixture: ComponentFixture<AdminUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminUsuario],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminUsuario);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
