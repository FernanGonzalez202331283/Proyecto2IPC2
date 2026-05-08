import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-reporte-proyectos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-proyectos.html',
  styleUrl: './reporte-proyectos.css',
})
export class ReporteProyectos {
fechaInicio = '';
  fechaFin = '';

  proyectos: any[] = [];

  mensaje = '';

  url =
  'http://localhost:8080/Proyecto2IPC2/ReporteProyectosClienteServlet';

  constructor(private http: HttpClient, private cd: ChangeDetectorRef, private router: Router) {}

  cargarReporte() {

  this.mensaje = '';

  const token = localStorage.getItem('token');

  this.http.get<any[]>(
    `${this.url}?fechaInicio=${this.fechaInicio}&fechaFin=${this.fechaFin}`,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  ).subscribe({

    next: (res) => {

      this.proyectos = res;

      if (this.proyectos.length === 0) {

        this.mensaje =
        'No existen proyectos en ese intervalo';
        this.cd.markForCheck();
      }
    },

    error: (err) => {

      console.log(err);

      this.mensaje =
      'Error al cargar reporte';
      this.cd.markForCheck();
    }
  });
}
regresar() {
  this.router.navigate(['/dashboard-cliente']);
}
}
