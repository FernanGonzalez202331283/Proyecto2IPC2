import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-categorias.html',
  styleUrl: './admin-categorias.css',
})
export class AdminCategorias {
  categorias: any[] = [];

  nombre = '';

  api = 'http://localhost:8080/Proyecto2IPC2/categorias';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit() {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.http.get<any[]>(this.api).subscribe({
      next: (data) => {
        this.categorias = data;

        // REFRESCAR VISTA
        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  crearCategoria() {
    if (!this.nombre.trim()) {
      return;
    }

    this.http
      .post(this.api, {
        nombre: this.nombre,
      })
      .subscribe({
        next: () => {
          // LIMPIAR INPUT
          this.nombre = '';

          // RECARGAR TABLA
          this.cargarCategorias();

          // REFRESCAR
          this.cdr.detectChanges();
        },

        error: (err) => {
          console.error(err);
        },
      });
  }

  cambiarEstado(cat: any, estado: number) {
    this.http
      .put(this.api, {
        id: cat.id,
        estado: estado,
      })
      .subscribe({
        next: () => {
          // ACTUALIZAR LOCALMENTE
          cat.estado = estado;

          // REFRESCAR VISTA
          this.cdr.detectChanges();
        },

        error: (err) => {
          console.error(err);
        },
      });
  }
  regresar() {
    this.router.navigate(['/admin']);
  }
}
