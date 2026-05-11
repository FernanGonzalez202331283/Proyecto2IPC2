import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: 'app-top-freelancers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './top-freelancers.html',
  styleUrl: './top-freelancers.css',
})
export class TopFreelancers implements OnInit{

  fechaInicio: string = '';

  fechaFin: string = '';

  freelancers: any[] = [];

  mensaje: string = '';

  url =
    'http://localhost:8080/Proyecto2IPC2/ReporteTopFreelancersServlet';

  constructor(
    private http: HttpClient, private cd: ChangeDetectorRef, private router: Router
  ) {}

  ngOnInit(): void {}

  obtenerReporte() {

    if (!this.fechaInicio || !this.fechaFin) {

      this.mensaje =
        'Seleccione ambas fechas';

      return;
    }

    const params = {

      fechaInicio: this.fechaInicio,

      fechaFin: this.fechaFin,
    };

    this.http.get<any[]>(
      this.url,
      { params }
    ).subscribe({

      next: (res) => {

        this.freelancers = res;

        this.mensaje = '';

        console.log(res);
        this.cd.markForCheck();
      },

      error: (err) => {

        console.log(err);

        this.mensaje =
          'Error al obtener reporte';
        this.cd.markForCheck();
      }
    });
  }
   regresar() {
  this.router.navigate(['/admin']);
}

exportarPDF() {

  const doc = new jsPDF();

  // Título
  doc.setFontSize(18);

  doc.text('Top 5 Freelancers', 14, 20);

  // Fechas
  doc.setFontSize(12);

  doc.text(
    `Desde: ${this.fechaInicio}  Hasta: ${this.fechaFin}`,
    14,
    30
  );

  // Datos de la tabla
  const datos = this.freelancers.map(f => [
    f.nombre,
    f.contratosCompletados,
    `Q${f.totalGenerado}`,
    `Q${f.comisionPlataforma}`
  ]);

  autoTable(doc, {
    startY: 40,
    head: [[
      'Freelancer',
      'Contratos',
      'Total Generado',
      'Comisión Plataforma'
    ]],
    body: datos
  });

  // Descargar PDF
  doc.save('reporte-freelancers.pdf');
}
}
