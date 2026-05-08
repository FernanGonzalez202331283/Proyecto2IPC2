import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopCategorias } from './top-categorias';

describe('TopCategorias', () => {
  let component: TopCategorias;
  let fixture: ComponentFixture<TopCategorias>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopCategorias],
    }).compileComponents();

    fixture = TestBed.createComponent(TopCategorias);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
