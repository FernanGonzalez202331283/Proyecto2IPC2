import { Component, OnInit } from '@angular/core';
import { AdminSolicitudesService } from '../../services/admin-solicitudes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-admin-solicitudes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-solicitudes.html',
  styleUrl: './admin-solicitudes.css',
})
export class AdminSolicitudes  implements OnInit{
 
  categorias: any[] = [];

  habilidades: any[] = [];

  cargandoCategorias = false;

  cargandoHabilidades = false;

  constructor(
    private service: AdminSolicitudesService,  private cdr: ChangeDetectorRef, private router: Router
  ) {}

  ngOnInit(): void {

    this.cargarCategorias();

    this.cargarHabilidades();
  }

  cargarCategorias() {

    this.cargandoCategorias = true;

    this.service.listarCategorias()
      .subscribe({

        next: (data) => {

          this.categorias = data;
          this.cargandoCategorias = false;
          this.cdr.markForCheck();
        },

        error: (err) => {

          console.error(err);
          this.cargandoCategorias = false;
          this.cdr.markForCheck();
        }
      });
  }

  aceptarCategoria(id: number) {

  this.service.aceptarCategoria(id)
    .subscribe({

      next: () => {

        this.mostrarMensaje(
          'Categoría aceptada correctamente',
          'success'
        );

        this.cargarCategorias();

        this.cdr.markForCheck();
      },

      error: (err) => {

        console.error(err);

        this.mostrarMensaje(
          'Error al aceptar categoría',
          'error'
        );
      }
    });
}
  rechazarCategoria(id: number) {

  this.service.rechazarCategoria(id)
    .subscribe({

      next: () => {

        this.mostrarMensaje(
          'Categoría rechazada correctamente',
          'success'
        );

        this.cargarCategorias();

        this.cdr.markForCheck();
      },

      error: (err) => {

        console.error(err);

        this.mostrarMensaje(
          'Error al rechazar categoría',
          'error'
        );
      }
    });
} 
  cargarHabilidades() {

    this.cargandoHabilidades = true;

    this.service.listarHabilidades()
      .subscribe({

        next: (data) => {

          this.habilidades = data;
          this.cdr.markForCheck();
          this.cargandoHabilidades = false;
        },

        error: (err) => {

          console.error(err);
          this.cdr.markForCheck();
          this.cargandoHabilidades = false;
        }
      });
  }

  aceptarHabilidad(id: number) {

  this.service.aceptarHabilidad(id)
    .subscribe({

      next: () => {

        this.mostrarMensaje(
          'Habilidad aceptada correctamente',
          'success'
        );

        this.cargarHabilidades();

        this.cdr.markForCheck();
      },

      error: (err) => {

        console.error(err);

        this.mostrarMensaje(
          'Error al aceptar habilidad',
          'error'
        );
      }
    });
}

 rechazarHabilidad(id: number) {

  this.service.rechazarHabilidad(id)
    .subscribe({

      next: () => {

        this.mostrarMensaje(
          'Habilidad rechazada correctamente',
          'success'
        );

        this.cargarHabilidades();

        this.cdr.markForCheck();
      },

      error: (err) => {

        console.error(err);

        this.mostrarMensaje(
          'Error al rechazar habilidad',
          'error'
        );
      }
    });
}
  mensaje = '';

tipoMensaje: 'success' | 'error' | '' = '';

mostrarMensaje(texto: string, tipo: 'success' | 'error') {

  this.mensaje = texto;

  this.tipoMensaje = tipo;

  setTimeout(() => {

    this.mensaje = '';
    this.tipoMensaje = '';

  }, 3000);
}

regresar() {
  this.router.navigate(['/admin']);
}
  
}