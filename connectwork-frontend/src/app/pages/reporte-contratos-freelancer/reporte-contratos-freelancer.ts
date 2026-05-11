import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient} from '@angular/common/http';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
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
    'Contratos Completados',
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
    this.contratos.map(c => [

      c.cliente,

      c.proyecto,

      `Q${c.monto}`,

      c.calificacion > 0
        ? `${c.calificacion}/5`
        : 'Sin calificación',

      c.fecha
    ]);

  // TABLA
  autoTable(doc, {

    startY: 82,

    head: [[
      'Cliente',
      'Proyecto',
      'Monto',
      'Calificación',
      'Fecha'
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
    'Sistema de Freelancers - Reporte de Contratos',
    20,
    285
  );

  // DESCARGAR
  doc.save(
    'contratos-freelancer.pdf'
  );
}
}
