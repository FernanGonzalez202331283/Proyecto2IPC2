import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { DashboardService } from '../../services/dashboard';
@Component({
  selector: 'app-mis-proyectos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-proyectos.html',
  styleUrl: './mis-proyectos.css',
})
export class MisProyectos {
  
  proyectos: any[] = [];
  filtro: string = 'TODOS';
  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private location: Location, private router: Router) {}

  ngOnInit() {
    this.cargarProyectos();
  }

  cargarProyectos() {
    const token = localStorage.getItem('token');

    this.http.get<any[]>(`${this.api}/proyectos`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (data) => this.proyectos = data,
      error: (err) => console.error(err)
    });
  }

  cambiarFiltro(f: string) {
    this.filtro = f;
  }

  get proyectosFiltrados() {
    if (this.filtro === 'TODOS') return this.proyectos;

    if (this.filtro === 'ABIERTO') {
      return this.proyectos.filter(p => p.estado === 'ABIERTO');
    }

    if (this.filtro === 'EN_PROGRESO') {
      return this.proyectos.filter(p => p.estado === 'EN_PROGRESO');
    }

    if (this.filtro === 'COMPLETADO') {
      return this.proyectos.filter(p => p.estado === 'COMPLETADO');
    }

    return this.proyectos;
  }

  regresar() {
  this.location.back();
}
irPublicar() {
  this.router.navigate(['/publicar-proyecto']);
}
}
