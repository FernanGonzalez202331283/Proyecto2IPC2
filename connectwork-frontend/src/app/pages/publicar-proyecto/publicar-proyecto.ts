import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../services/dashboard';
@Component({
  selector: 'app-publicar-proyecto',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './publicar-proyecto.html',
  styleUrl: './publicar-proyecto.css',
})
export class PublicarProyecto {

  titulo = '';
  descripcion = '';
  presupuesto = 0;
  fechaLimite = '';
  categoriaId: number | null = null;
  habilidades: any[] = [];
  habilidadesSeleccionadas: number[] = [];
  categorias: any[] = [];

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient,
    private router: Router,
    private dashboardService: DashboardService
  ) {}

  ngOnInit() {
    this.cargarCategorias(); 
    this.cargarHabilidades(); 
  }

  cargarCategorias() {
    this.http.get<any[]>(`${this.api}/categorias`)
      .subscribe({
        next: (data) => {
          console.log("CATEGORIAS:", data);
          this.categorias = data;
        },
        error: (err) => console.error('Error cargando categorías', err)
      });
  }

  publicar() {

    if (!this.titulo || !this.descripcion || this.presupuesto <= 0 || !this.fechaLimite || !this.categoriaId) {
      alert('Completa todos los campos');
      return;
    }
    if (this.habilidadesSeleccionadas.length === 0) {
    alert('Debes seleccionar al menos una habilidad');
    return;
  }

    const data = {
      titulo: this.titulo,
      descripcion: this.descripcion,
      presupuesto: this.presupuesto,
      fechaLimite: this.fechaLimite,
      categoriaId: this.categoriaId,
      habilidades: this.habilidadesSeleccionadas 
    };

    const token = localStorage.getItem('token');

    this.http.post(`${this.api}/proyectos`, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: () => {

        alert('Proyecto publicado correctamente');
        this.dashboardService.notificarActualizacion();

        this.router.navigate(['/dashboard-cliente']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al publicar proyecto');
      }
    });
  }
  cargarHabilidades() {
  this.http.get<any[]>(`${this.api}/habilidades`)
    .subscribe({
      next: (data) => this.habilidades = data,
      error: (err) => console.error(err)
    });
}

toggleHabilidad(id: number, event: any) {
  if (event.target.checked) {
    this.habilidadesSeleccionadas.push(id);
  } else {
    this.habilidadesSeleccionadas = this.habilidadesSeleccionadas.filter(h => h !== id);
  }
}

  regresar() {
  this.router.navigate(['/dashboard-cliente']);
}
}
