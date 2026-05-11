import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-reporte-ingresos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-ingresos.html',
  styleUrl: './reporte-ingresos.css',
})
export class ReporteIngresos implements OnInit{

  fechaInicio = '';
  fechaFin = '';

  totalContratos = 0;
  totalComisiones = 0;

  url = 'http://localhost:8080/Proyecto2IPC2/ReporteIngresosServlet';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {}

  cargar() {

    this.http.get<any>(
      `${this.url}?fechaInicio=${this.fechaInicio}&fechaFin=${this.fechaFin}`
    ).subscribe(res => {

      this.totalContratos = res.totalContratos;
      this.totalComisiones = res.totalComisiones;

    });
  }
   regresar() {
  this.router.navigate(['/admin']);
}

exportarPDF() {

    const doc =
      new jsPDF();

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
      'Reporte de Ingresos',
      20,
      22
    );

    // FECHA GENERADA
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

    // FILTRO FECHAS
    doc.setFontSize(13);

    doc.text(
      `Fecha Inicio: ${this.fechaInicio}`,
      20,
      60
    );

    doc.text(
      `Fecha Fin: ${this.fechaFin}`,
      20,
      70
    );

    // LINEA
    doc.setDrawColor(220);

    doc.line(
      20,
      80,
      190,
      80
    );

    // TARJETA CONTRATOS
    doc.setFillColor(
      37,
      99,
      235
    );

    doc.roundedRect(
      20,
      95,
      75,
      45,
      5,
      5,
      'F'
    );

    doc.setTextColor(
      255,
      255,
      255
    );

    doc.setFontSize(14);

    doc.text(
      'Total Contratos',
      28,
      112
    );

    doc.setFontSize(24);

    doc.text(
      `${this.totalContratos}`,
      50,
      128
    );

    // TARJETA COMISIONES
    doc.setFillColor(
      16,
      185,
      129
    );

    doc.roundedRect(
      115,
      95,
      75,
      45,
      5,
      5,
      'F'
    );

    doc.setFontSize(14);

    doc.text(
      'Comisiones',
      130,
      112
    );

    doc.setFontSize(22);

    doc.text(
      `Q${this.totalComisiones}`,
      125,
      128
    );

    // FOOTER
    doc.setTextColor(
      100,
      116,
      139
    );

    doc.setFontSize(10);

    doc.text(
      'Sistema Administrativo de Freelancers',
      20,
      280
    );

    // DESCARGAR
    doc.save(
      'reporte-ingresos.pdf'
    );
  }
}
