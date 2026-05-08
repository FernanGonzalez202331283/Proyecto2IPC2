import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-solicitar-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './solicitar-categoria.html',
  styleUrl: './solicitar-categoria.css',
})
export class SolicitarCategoria {

  nombre = '';

  private api =
    'http://localhost:8080/Proyecto2IPC2/solicitud-categoria';

  constructor(private http: HttpClient, private router: Router) {}

  enviarSolicitud() {

    // OBTENER USUARIO DEL LOCALSTORAGE
    const usuario =
      JSON.parse(
        localStorage.getItem('usuario')!
      );

    const body = {

      nombre: this.nombre,

      usuario_id: usuario.id

    };

    console.log(body);

    this.http.post(this.api, body)
      .subscribe({

        next: () => {

          alert('Solicitud enviada');

          this.nombre = '';

        },

        error: (err) => {

          console.error(err);

        }

      });

  }
  regresar() {
    this.router.navigate(['/dashboard-cliente']);
  }
}
