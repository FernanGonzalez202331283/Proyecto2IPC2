import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-calificar-freelancer',
  imports: [CommonModule,FormsModule],
  templateUrl: './calificar-freelancer.html',
  styleUrl: './calificar-freelancer.css',
})
export class CalificarFreelancer {

  contratoId!: number;

  estrellas: number = 5;
  comentario: string = '';

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {

    this.contratoId = Number(
      this.route.snapshot.paramMap.get('id')
    );

  }

  seleccionarEstrella(valor: number) {

    this.estrellas = valor;

  }

  guardarCalificacion() {

    const body = {

      contratoId: this.contratoId,
      estrellas: this.estrellas,
      comentario: this.comentario

    };

    this.http.post(
      `${this.api}/cliente/calificacion`,
      body
    ).subscribe({

      next: () => {

        alert('Calificación enviada');

        this.router.navigate(['/dashboard-cliente']);

      },

      error: (err) => {

        console.log(err);

        alert('Error al enviar calificación');

      }

    });

  }
}
