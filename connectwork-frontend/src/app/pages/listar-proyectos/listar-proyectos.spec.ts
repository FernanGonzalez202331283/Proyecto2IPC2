import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarProyectos } from './listar-proyectos';

describe('ListarProyectos', () => {
  let component: ListarProyectos;
  let fixture: ComponentFixture<ListarProyectos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarProyectos],
    }).compileComponents();

    fixture = TestBed.createComponent(ListarProyectos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
