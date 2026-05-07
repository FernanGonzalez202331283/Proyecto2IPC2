import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { DashboardService } from '../../services/dashboard';
import { ChangeDetectorRef } from '@angular/core';


@Component({
  selector: 'app-completar-cliente',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './completar-cliente.html',
  styleUrl: './completar-cliente.css',
})
export class CompletarCliente {
  username = '';
descripcion = '';
  sector = '';
  sitioWeb = '';

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
  private http: HttpClient, 
  private dashboardService: DashboardService,
  private cdr: ChangeDetectorRef,
  private router: Router,
  private auth: AuthService
) {}

  guardar() {
    const data = {
      descripcion: this.descripcion,
      sector: this.sector,
      sitioWeb: this.sitioWeb
    };

    const token = localStorage.getItem('token');

    this.http.post(`${this.api}/completarPerfil`, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: () => {

        alert('Perfil completado');
        const user = JSON.parse(localStorage.getItem('usuario')!);
        user.perfilCompleto = 1;
        localStorage.setItem('usuario', JSON.stringify(user));
        this.router.navigate(['/dashboard-cliente']);
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
