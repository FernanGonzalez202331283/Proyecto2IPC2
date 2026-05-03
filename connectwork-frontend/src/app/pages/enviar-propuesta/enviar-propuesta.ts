import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
@Component({
  selector: 'app-enviar-propuesta',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './enviar-propuesta.html',
  styleUrl: './enviar-propuesta.css',
})
export class EnviarPropuesta {

  proyectoId: number = 0;

  propuesta = {
    monto: 0,
    tiempo: 0,
    descripcion: ''
  };

  mensaje: string = '';
  error: string = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.proyectoId = Number(this.route.snapshot.paramMap.get('id'));
  }

  enviar() {

    this.mensaje = '';
    this.error = '';
    if (this.propuesta.monto <= 0 ||
        this.propuesta.tiempo <= 0 ||
        !this.propuesta.descripcion.trim()) {

      this.error = "Completa todos los campos correctamente";
      return;
    }

    const token = localStorage.getItem('token');

    const body = {
      proyectoId: this.proyectoId,
      monto: this.propuesta.monto,
      tiempo: this.propuesta.tiempo,
      descripcion: this.propuesta.descripcion
    };

    this.http.post<any>(
      'http://localhost:8080/Proyecto2IPC2/freelancer/propuesta',
      body,
      {
        headers: { Authorization: `Bearer ${token}` }
      }
    ).subscribe({

      next: (res) => {
        this.mensaje = res.msg;

        setTimeout(() => {
          this.router.navigate(['/explorar-proyectos']);
        }, 2000);
      },

      error: (err) => {
        this.error = err.error?.error || "Error al enviar propuesta";
      }

    });
  }

  regresar() {
    this.router.navigate(['/explorar-proyectos']);
  }

}
