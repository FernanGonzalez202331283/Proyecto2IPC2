import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
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
}
