import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-publicar-proyecto',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './publicar-proyecto.html',
  styleUrl: './publicar-proyecto.css',
})
export class PublicarProyecto {

  titulo = '';
  descripcion = '';
  presupuesto = 0;
  fechaLimite = '';
  categoriaId = 1;

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router) {}

  publicar() {

    if (!this.titulo || !this.descripcion || this.presupuesto <= 0 || !this.fechaLimite) {
      alert('Completa todos los campos');
      return;
    }

    const data = {
      titulo: this.titulo,
      descripcion: this.descripcion,
      presupuesto: this.presupuesto,
      fechaLimite: this.fechaLimite,  
      categoriaId: this.categoriaId
      
    };

    const token = localStorage.getItem('token');

    this.http.post(`${this.api}/proyectos`, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: () => {
        alert('Proyecto publicado correctamente');
        this.router.navigate(['/dashboard-cliente']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al publicar proyecto');
      }
    });
  }
}
