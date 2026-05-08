import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { Router } from '@angular/router';

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
}
