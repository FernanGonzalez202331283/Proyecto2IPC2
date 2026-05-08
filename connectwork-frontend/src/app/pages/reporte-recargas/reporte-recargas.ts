import { ChangeDetectorRef, Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-reporte-recargas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-recargas.html',
  styleUrl: './reporte-recargas.css',
})
export class ReporteRecargas {

  fechaInicio = '';
  fechaFin = '';

  recargas: any[] = [];

  mensaje = '';

  url =
  'http://localhost:8080/Proyecto2IPC2/ReporteRecargasClienteServlet';

  constructor(private http: HttpClient, private cd: ChangeDetectorRef, private router: Router) {}

  cargarReporte() {

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

        this.recargas = res;

        if (this.recargas.length === 0) {

          this.mensaje =
          'No existen recargas';
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
