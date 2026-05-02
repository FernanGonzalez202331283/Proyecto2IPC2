import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-explorar-proyectos',
  standalone  : true,
  imports: [CommonModule, FormsModule],
  templateUrl: './explorar-proyectos.html',
  styleUrl: './explorar-proyectos.css',
})
export class ExplorarProyectos implements OnInit {
 proyectos: any[] = [];
  proyectosOriginal: any[] = [];

  categorias: any[] = [];
  categoriaSeleccionada: number | null = null;

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.cargarProyectos();
    this.cargarCategorias();
  }

  cargarProyectos() {
    const token = localStorage.getItem('token');

    this.http.get<any[]>(`${this.api}/proyectos`, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: (data) => {
        console.log("PROYECTOS:", data);

        this.proyectos = data;
        this.proyectosOriginal = data; 
      },
      error: (err) => console.error("ERROR:", err)
    });
  }

  cargarCategorias() {
    this.http.get<any[]>(`${this.api}/categorias`)
      .subscribe({
        next: (data) => {
          this.categorias = data;
        },
        error: (err) => console.error(err)
      });
  }

  filtrar() {

    if (!this.categoriaSeleccionada) {
      console.log(typeof this.categoriaSeleccionada);
      this.proyectos = this.proyectosOriginal;
      return;
    }

    this.proyectos = this.proyectosOriginal.filter(p =>
      p.categoriaId == this.categoriaSeleccionada
    );
  }
  verDetalle(id: number) {
  this.router.navigate(['/proyecto', id]);
}
regresar() {
  this.router.navigate(['/dashboard-freelancer']);
}
}
