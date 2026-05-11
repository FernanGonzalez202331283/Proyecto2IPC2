import { ChangeDetectorRef, Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
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
    'Gastos por Categoría',
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

  // DATOS
  const datos =
    this.categorias.map(c => [

      c.categoria,

      `Q${c.totalGastado}`
    ]);

  // TABLA
  autoTable(doc, {

    startY: 82,

    head: [[
      'Categoría',
      'Total Gastado'
    ]],

    body: datos,

    theme: 'grid',

    styles: {

      fontSize: 11,

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
    'Sistema de Freelancers - Reporte de Gastos',
    20,
    285
  );

  // DESCARGAR
  doc.save(
    'gastos-categorias.pdf'
  );
}
}

