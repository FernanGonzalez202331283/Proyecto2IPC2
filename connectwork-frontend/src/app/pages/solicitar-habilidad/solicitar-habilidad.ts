import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-solicitar-habilidad',
  standalone: true,
  imports: [CommonModule, FormsModule ],
  templateUrl: './solicitar-habilidad.html',
  styleUrl: './solicitar-habilidad.css',
})
export class SolicitarHabilidad {

  nombre: string = '';
  descripcion: string = '';

  categoriaId: number = 0;

  categorias: any[] = [];

  mensaje = '';
  error = '';

  private api =
    'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {

    this.cargarCategorias();

  }

  cargarCategorias() {

    this.http.get<any[]>(
      `${this.api}/categorias`
    ).subscribe({

      next: (data) => {

        this.categorias = data;
        this.cdr.markForCheck();

      },

      error: (err) => {

        console.error(err);
        this.cdr.markForCheck();

      }

    });

  }

  enviar() {

    if (
      !this.nombre ||
      !this.descripcion ||
      this.categoriaId == 0
    ) {

      this.error =
        'Completa todos los campos';
        this.cdr.markForCheck();

      return;
    }

    const token =
      localStorage.getItem('token');

    this.http.post(

      `${this.api}/freelancer/solicitud-habilidad`,

      {
        nombre: this.nombre,
        descripcion: this.descripcion,
        categoriaId: this.categoriaId
      },

      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }

    ).subscribe({

      next: (res: any) => {

        this.mensaje = res.msg;
        this.cdr.markForCheck();

        this.error = '';

        this.nombre = '';
        this.descripcion = '';
        this.categoriaId = 0;
        this.cdr.markForCheck();

      },

      error: (err) => {

        console.error(err);

        this.error =
          err.error?.error || 'Error';
          this.cdr.markForCheck();

      }

    });

  }

  regresar() {

    this.router.navigate([
      '/dashboard-freelancer'
    ]);

  }

}
