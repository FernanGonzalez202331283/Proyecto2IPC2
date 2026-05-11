import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: 'app-reporte-top-categorias-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-top-categorias-freelancer.html',
  styleUrl: './reporte-top-categorias-freelancer.css',
})
export class ReporteTopCategoriasFreelancer {
  categorias: any[] = [];

  url =
  'http://localhost:8080/Proyecto2IPC2/ReporteTopCategoriasFreelancerServlet';

  constructor(private http: HttpClient, private router: Router, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {

    const token =
    localStorage.getItem('token');

    this.http.get<any[]>(this.url, {

      headers: {
        Authorization: `Bearer ${token}`
      }

    }).subscribe({

      next: (res) => {

        this.categorias = res;
        this.cd.markForCheck();
      },

      error: (err) => {

        console.log(err);
        this.cd.markForCheck();
      }
    });
  }
   regresar() {
  this.router.navigate(['/dashboard-freelancer']);
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
    'Top 5 Categorías',
    20,
    22
  );

  // FECHA
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

  // DATOS
  const datos =
    this.categorias.map(c => [

      c.categoria,

      c.cantidadContratos,

      `Q${c.totalIngresos}`
    ]);

  // TABLA
  autoTable(doc, {

    startY: 60,

    head: [[
      'Categoría',
      'Contratos',
      'Total Ingresos'
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
    'Sistema de Freelancers - Top Categorías',
    20,
    285
  );

  // DESCARGAR
  doc.save(
    'top-categorias-freelancer.pdf'
  );
}
}

