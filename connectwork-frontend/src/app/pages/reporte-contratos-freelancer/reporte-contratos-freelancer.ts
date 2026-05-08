import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient

 } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-reporte-contratos-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-contratos-freelancer.html',
  styleUrl: './reporte-contratos-freelancer.css',
})
export class ReporteContratosFreelancer {
  
  fechaInicio = '';
  fechaFin = '';

  contratos: any[] = [];

  mensaje = '';

  url =
  'http://localhost:8080/Proyecto2IPC2/ReporteContratosFreelancerServlet';

  constructor(private http: HttpClient, private cd: ChangeDetectorRef, private router: Router) {}

  cargarReporte() {

    this.mensaje = '';

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

        this.contratos = res;

        if (this.contratos.length === 0) {

          this.mensaje =
          'No existen contratos completados';
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
  this.router.navigate(['/dashboard-freelancer']);
}
}
