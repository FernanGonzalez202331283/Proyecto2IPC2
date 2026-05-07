import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-explorar-proyectos',
  standalone  : true,
  imports: [CommonModule, FormsModule],
  templateUrl: './explorar-proyectos.html',
  styleUrl: './explorar-proyectos.css',
})
export class ExplorarProyectos implements OnInit {
  
  proyectos: any[] = [];
  proyectosOriginal: any[] = [];

  habilidades: any[] = [];
  habilidadSeleccionada: number | null = null;

  categorias: any[] = [];
  categoriaSeleccionada: number | null = null;

  presupuestoMin: number | null = null;
  presupuestoMax: number | null = null;

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router,  private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.cargarProyectos();
    this.cargarCategorias();
    this.cargarHabilidades();
  }

  cargarHabilidades() {
    this.http.get<any[]>(`${this.api}/habilidades`)
      .subscribe({
        next: (data) => this.habilidades = data,
        error: (err) => console.error(err)
      });
  }

  cargarProyectos() {
  const token = localStorage.getItem('token');

  this.http.get<any[]>(`${this.api}/proyectos`, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: (data) => {
      this.proyectos = data;
      this.proyectosOriginal = data;

      this.cdr.detectChanges(); 
    },
    error: (err) => console.error("ERROR:", err)
  });
}

  cargarCategorias() {
    this.http.get<any[]>(`${this.api}/categorias`)
      .subscribe({
        next: (data) => this.categorias = data,
        error: (err) => console.error(err)
      });
  }

 filtrar() {
  this.proyectos = this.proyectosOriginal.filter(p => {

    const cumpleCategoria =
      !this.categoriaSeleccionada || p.categoriaId == this.categoriaSeleccionada;

    const cumpleHabilidad =
      !this.habilidadSeleccionada ||
      p.habilidades?.includes(this.habilidadSeleccionada);

    const cumpleMin =
      this.presupuestoMin == null || p.presupuesto >= this.presupuestoMin;

    const cumpleMax =
      this.presupuestoMax == null || p.presupuesto <= this.presupuestoMax;

    return cumpleCategoria && cumpleHabilidad && cumpleMin && cumpleMax;
  });
}

  limpiarFiltros() {
    this.categoriaSeleccionada = null;
    this.habilidadSeleccionada = null;
    this.presupuestoMin = null;
    this.presupuestoMax = null;
    this.proyectos = this.proyectosOriginal;
  }

  verDetalle(id: number) {
    this.router.navigate(['/proyecto', id]);
  }

  regresar() {
    this.router.navigate(['/dashboard-freelancer']);
  }
}
