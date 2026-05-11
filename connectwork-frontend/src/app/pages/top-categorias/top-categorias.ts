import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Route, Router } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: 'app-top-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './top-categorias.html',
  styleUrl: './top-categorias.css',
})
export class TopCategorias implements OnInit{
  fechaInicio: string = '';

  fechaFin: string = '';

  categorias: any[] = [];

  mensaje: string = '';

  url =
'http://localhost:8080/Proyecto2IPC2/ReporteTopCategoriasServlet';

  constructor(
    private http: HttpClient, private cd: ChangeDetectorRef, private router: Router
  ) {}

  ngOnInit(): void {}

  obtenerReporte() {

    if (
      !this.fechaInicio ||
      !this.fechaFin
    ) {

      this.mensaje =
        'Seleccione ambas fechas';

      return;
    }

    const params = {

      fechaInicio:
        this.fechaInicio,

      fechaFin:
        this.fechaFin,
    };

    this.http.get<any[]>(
      this.url,
      { params }
    ).subscribe({

      next: (res) => {

        this.categorias = res;

        this.mensaje = '';
        this.cd.markForCheck();

        console.log(res);
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

  // TITULO
  doc.setFontSize(18);

  doc.text(
    'Top 5 Categorías',
    14,
    20
  );

  // FECHAS
  doc.setFontSize(12);

  doc.text(
    `Desde: ${this.fechaInicio} Hasta: ${this.fechaFin}`,
    14,
    30
  );

  // DATOS
  const datos = this.categorias.map(c => [
    c.categoria,
    c.cantidadContratos,
    `Q${c.totalComisiones}`
  ]);

  // TABLA
  autoTable(doc, {

    startY: 40,

    head: [[
      'Categoría',
      'Contratos',
      'Total Comisiones'
    ]],

    body: datos,

    theme: 'grid',

    headStyles: {
      fillColor: [41, 128, 185]
    },

    styles: {
      fontSize: 10
    }
  });

  // DESCARGAR
  doc.save(
    'reporte-categorias.pdf'
  );
}
}
