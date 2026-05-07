import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
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

  mensaje = '';
  error = '';

  constructor(private http: HttpClient, private router: Router) {}

  enviar() {

    if (!this.nombre || !this.descripcion) {
      this.error = "Completa todos los campos";
      return;
    }

    const token = localStorage.getItem('token');

    this.http.post(
      'http://localhost:8080/Proyecto2IPC2/freelancer/solicitud-habilidad',
      {
        nombre: this.nombre,
        descripcion: this.descripcion
      },
      {
        headers: { Authorization: `Bearer ${token}` }
      }
    ).subscribe({
      next: (res: any) => {
        this.mensaje = res.msg;
        this.error = '';

        this.nombre = '';
        this.descripcion = '';
      },
      error: (err) => {
        this.error = err.error?.error || 'Error';
      }
    });
  }
  regresar() {
    this.router.navigate(['/dashboard-freelancer']);
  }

}
