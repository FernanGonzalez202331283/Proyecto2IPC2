import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: 'app-admin-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-reportes.html',
  styleUrl: './admin-reportes.css',
})
export class AdminReportes implements OnInit {
  historial: any[] = [];

  mensaje: string = '';

  url = 'http://localhost:8080/Proyecto2IPC2/HistorialComisionServlet';

  constructor(
    private http: HttpClient,
    private cd: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.obtenerHistorial();
  }

  obtenerHistorial() {
    this.http.get<any[]>(this.url).subscribe({
      next: (res) => {
        this.historial = res;
        this.cd.markForCheck();
      },

      error: (err) => {
        console.log(err);

        this.mensaje = 'Error al cargar historial';
        this.cd.markForCheck();
      },
    });
  }
  regresar() {
    this.router.navigate(['/admin']);
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

  doc.setFontSize(24);

  doc.text(
    'Historial de Comisión',
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

  // TABLA
  const datos =
    this.historial.map(h => [

      `${h.porcentaje}%`,

      h.fechaInicio,

      h.fechaFin
        ? h.fechaFin
        : 'ACTUAL'
    ]);

  autoTable(doc, {

    startY: 60,

    head: [[
      'Porcentaje',
      'Fecha Inicio',
      'Fecha Fin'
    ]],

    body: datos,

    theme: 'grid',

    headStyles: {

      fillColor: [37, 99, 235],

      textColor: [255,255,255]
    },

    styles: {

      fontSize: 11,

      cellPadding: 4
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
    'Sistema Administrativo de Freelancers',
    20,
    285
  );

  // DESCARGAR
  doc.save(
    'historial-comisiones.pdf'
  );
}
}
