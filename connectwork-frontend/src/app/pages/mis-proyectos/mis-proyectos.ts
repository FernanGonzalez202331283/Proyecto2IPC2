import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-mis-proyectos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-proyectos.html',
  styleUrl: './mis-proyectos.css',
})
export class MisProyectos {
 proyectos: any[] = [];
  filtro: string = 'TODOS';
  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient, 
    private location: Location, 
    private router: Router, 
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    // Esto se ejecuta apenas el componente "nace", cargando los datos de inmediato
    this.cargarProyectos();
  }

  cargarProyectos() {
  const token = localStorage.getItem('token');

  this.http.get<any[]>(`${this.api}/proyectos`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  }).subscribe({
    next: (data) => {
      this.proyectos = [...data];

      // Forzar detección de cambios
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error("Error al cargar proyectos:", err);
    }
  });
}

  cambiarFiltro(f: string) {
    this.filtro = f;
  }

  // Este getter se actualiza automáticamente cada vez que 'proyectos' o 'filtro' cambian
  get proyectosFiltrados() {
    if (!this.proyectos || this.proyectos.length === 0) {
      return [];
    }

    if (this.filtro === 'TODOS') {
      return this.proyectos;
    }

    // Filtrado dinámico más limpio
    return this.proyectos.filter(p => p.estado === this.filtro);
  }

  regresar() {
    this.location.back();
  }

  irPublicar() {
    this.router.navigate(['/publicar-proyecto']);
  }
}
