import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExplorarProyectos } from './explorar-proyectos';

describe('ExplorarProyectos', () => {
  let component: ExplorarProyectos;
  let fixture: ComponentFixture<ExplorarProyectos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExplorarProyectos],
    }).compileComponents();

    fixture = TestBed.createComponent(ExplorarProyectos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
