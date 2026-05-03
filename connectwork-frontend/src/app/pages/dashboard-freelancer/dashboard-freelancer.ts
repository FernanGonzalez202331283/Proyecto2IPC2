import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DashboardService } from '../../services/dashboard';
import { AuthService } from '../../services/auth';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-dashboard-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dashboard-freelancer.html',
  styleUrl: './dashboard-freelancer.css',
})
export class DashboardFreelancer implements OnInit {

  
  username = '';
  saldo = 0;
  propuestas = 0;
  contratos = 0;
  proyectos: any[] = [];

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router,private auth: AuthService ) {}

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('usuario')!);
    this.username = user.username;

    if (user.perfilCompleto === 0) {
    this.router.navigate(['/completar-freelancer']);
    return;
  }

    this.cargarDashboard();
    this.cargarProyectos();
  }

  cargarDashboard() {
    const token = localStorage.getItem('token');

    this.http.get<any>(`${this.api}/freelancer/dashboard`, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: (data) => {
        this.saldo = data.saldo || 0;
        this.propuestas = data.propuestas || 0;
        this.contratos = data.contratos || 0;
      },
      error: (err) => console.error(err)
    });
  }

  cargarProyectos() {
    const token = localStorage.getItem('token');

    this.http.get<any[]>(`${this.api}/proyectos`, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: (data) => {
        this.proyectos = data.slice(0, 5); // solo 3 recientes
      },
      error: (err) => console.error(err)
    });
  }

   logout() {
  // Opción simple
  localStorage.removeItem('token');
  localStorage.removeItem('usuario');

  // Si ya tienes método en AuthService (mejor usarlo)
  this.auth.logout?.();

  this.router.navigate(['/login']);
}
}
