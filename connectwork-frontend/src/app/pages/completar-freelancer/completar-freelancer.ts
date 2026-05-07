import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-completar-freelancer',
  standalone: true, 
  imports: [FormsModule, CommonModule],
  templateUrl: './completar-freelancer.html',
  styleUrl: './completar-freelancer.css',
})
export class CompletarFreelancer implements OnInit {

  biografia = '';
  nivelExperiencia = '';
  tarifaHora = 0;

  habilidades: number[] = []; //IMPORTANTE

  listaHabilidades: any[] = []; // catálogo

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.cargarHabilidades();
  }

  cargarHabilidades() {
  const token = localStorage.getItem('token');

  this.http.get<any[]>(`${this.api}/habilidades`, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: (data) => {
      console.log("HABILIDADES:", data); //MIRA ESTO
      this.listaHabilidades = data;
    },
    error: (err) => console.error("ERROR:", err)
  });
}

  toggleHabilidad(id: number) {
    if (this.habilidades.includes(id)) {
      this.habilidades = this.habilidades.filter(h => h !== id);
    } else {
      this.habilidades.push(id);
    }
  }

  guardar() {

    const data = {
      biografia: this.biografia,
      nivelExperiencia: this.nivelExperiencia,
      tarifaHora: this.tarifaHora,
      habilidades: this.habilidades
    };

    const token = localStorage.getItem('token');

    this.http.post(`${this.api}/freelancer/completarPerfil`, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: () => {

        alert('Perfil freelancer completado');

        const user = JSON.parse(localStorage.getItem('usuario')!);
        user.perfilCompleto = 1;
        localStorage.setItem('usuario', JSON.stringify(user));

        this.router.navigate(['/dashboard-freelancer']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al guardar perfil');
      }
    });
  }

  regresar() {
  this.router.navigate(['/login']);
}
}
