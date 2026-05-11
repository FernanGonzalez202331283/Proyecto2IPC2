import { ChangeDetectorRef, Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
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
exportarPDF() {

  const doc = new jsPDF();

  const fechaActual =
    new Date().toLocaleDateString();

  // HEADER
  doc.setFillColor(
    37,
    99,
    235
  );

  doc.rect(
    0,
    0,
    210,
    35,
    'F'
  );

  // TITULO
  doc.setTextColor(
    255,
    255,
    255
  );

  doc.setFontSize(22);

  doc.text(
    'Historial de Recargas',
    20,
    22
  );

  // FECHA GENERACION
  doc.setTextColor(
    100,
    116,
    139
  );

  doc.setFontSize(11);

  doc.text(
    `Generado: ${fechaActual}`,
    20,
    45
  );

  // FILTROS
  doc.setFontSize(12);

  doc.text(
    `Fecha Inicio: ${this.fechaInicio}`,
    20,
    58
  );

  doc.text(
    `Fecha Fin: ${this.fechaFin}`,
    20,
    68
  );

  // DATOS TABLA
  const datos =
    this.recargas.map(r => [

      `Q${r.monto}`,

      r.fecha
    ]);

  // TABLA
  autoTable(doc, {

    startY: 80,

    head: [[
      'Monto',
      'Fecha y Hora'
    ]],

    body: datos,

    theme: 'grid',

    styles: {

      fontSize: 10,

      cellPadding: 4
    },

    headStyles: {

      fillColor: [37, 99, 235],

      textColor: [255,255,255]
    },

    alternateRowStyles: {

      fillColor: [245,247,250]
    }
  });

  // FOOTER
  doc.setFontSize(10);

  doc.setTextColor(
    120
  );

  doc.text(
    'Sistema de Freelancers - Reporte de Recargas',
    20,
    285
  );

  // DESCARGAR
  doc.save(
    'historial-recargas.pdf'
  );
}
}
