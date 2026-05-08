import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-reporte-propuestas-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-propuestas-freelancer.html',
  styleUrl: './reporte-propuestas-freelancer.css',
})
export class ReportePropuestasFreelancer {

  fechaInicio = '';
  fechaFin = '';

  propuestas: any[] = [];

  mensaje = '';

  url =
  'http://localhost:8080/Proyecto2IPC2/ReportePropuestasFreelancerServlet';

  constructor(private http: HttpClient, private router: Router, private cd: ChangeDetectorRef) {}

  cargar() {

    const token =
    localStorage.getItem('token');

    this.http.get<any[]>(
      `${this.url}?fechaInicio=${this.fechaInicio}&fechaFin=${this.fechaFin}`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    ).subscribe({

      next: (res) => {

        this.propuestas = res;

        if (this.propuestas.length === 0) {

          this.mensaje =
          'No hay propuestas';
          this.cd.markForCheck();
        }
      },

      error: () => {

        this.mensaje =
        'Error al cargar reporte';
        this.cd.markForCheck();
      }
    });
  }
   regresar() {
  this.router.navigate(['/dashboard-freelancer']);
}
}
