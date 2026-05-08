import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-admin-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-usuario.html',
  styleUrl: './admin-usuario.css',
})
export class AdminUsuario {

  usuarios: any[] = [];
  perfilSeleccionado: any = null;

mostrarModalAdmin = false;

nuevoAdmin = {

  nombre: '',
  username: '',
  correo: '',
  password: ''

};

  private api =
    'http://localhost:8080/Proyecto2IPC2/admin/usuarios';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit() {

    this.cargarUsuarios();

  }

  cargarUsuarios() {

    this.http.get<any[]>(this.api)
      .subscribe({

        next: (data) => {

          console.log(data);

          this.usuarios = data;

          // FORZAR REFRESCO
          this.cdr.detectChanges();

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  cambiarEstado(usuario: any, nuevoEstado: number) {

  // CAMBIO VISUAL INMEDIATO
  usuario.estado = nuevoEstado;

  // REFRESCAR UI
  this.cdr.detectChanges();

  const body = {
    id: usuario.id,
    estado: nuevoEstado
  };

  this.http.put(this.api, body)
    .subscribe({

      next: () => {

        console.log('Estado actualizado');

      },

      error: (err) => {

        console.error(err);

        // SI FALLA, REVERTIR CAMBIO
        usuario.estado =
          nuevoEstado === 1 ? 0 : 1;

        this.cdr.detectChanges();

      }

    });

  }

  verPerfil(id: number) {

  this.http.get(

    `${this.api}?id=${id}`

  ).subscribe({

    next: (data) => {

      console.log(data);

      this.perfilSeleccionado = data;

      this.cdr.detectChanges();

    },

    error: (err) => {

      console.error(err);

    }

  });

}
abrirModalAdmin() {

  this.mostrarModalAdmin = true;


}
cerrarModalAdmin() {

  this.mostrarModalAdmin = false;

}
crearAdmin() {

  this.http.post(

    this.api,

    this.nuevoAdmin

  ).subscribe({

    next: (res: any) => {

      alert(res.msg);

      this.mostrarModalAdmin = false;

      this.cargarUsuarios();

      this.nuevoAdmin = {

        nombre: '',
        username: '',
        correo: '',
        password: ''

      };

      this.cdr.detectChanges();

    },

    error: (err) => {

      console.error(err);

      alert(
        err.error?.error ||
        'Error al crear admin'
      );

    }

  });
}
cerrarPerfil() {

  this.perfilSeleccionado = null;

}
   regresar() {
    this.router.navigate(['/admin']);
  }
}
