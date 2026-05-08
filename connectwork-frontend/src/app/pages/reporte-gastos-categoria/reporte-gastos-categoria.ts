import { ChangeDetectorRef, Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-reporte-gastos-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-gastos-categoria.html',
  styleUrl: './reporte-gastos-categoria.css',
})
export class ReporteGastosCategoria {

  fechaInicio = '';
  fechaFin = '';

  categorias: any[] = [];

  mensaje = '';

  url =
  'http://localhost:8080/Proyecto2IPC2/ReporteGastosCategoriaServlet';

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

        this.categorias = res;

        if (this.categorias.length === 0) {

          this.mensaje =
          'No existen gastos registrados';
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
