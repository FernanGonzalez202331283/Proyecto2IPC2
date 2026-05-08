import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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
}
