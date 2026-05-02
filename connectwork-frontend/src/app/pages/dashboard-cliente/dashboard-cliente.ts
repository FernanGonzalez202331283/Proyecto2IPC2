import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { DashboardService } from '../../services/dashboard';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
@Component({
  selector: 'app-dashboard-cliente',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './dashboard-cliente.html',
  styleUrl: './dashboard-cliente.css',
})
export class DashboardCliente {

  saldo: number = 0;
  totalProyectos: number = 0;
  propuestas = 0;
  activos = 0;
  username = '';
  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient, 
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('usuario')!);
    this.username = user.username;

    this.dashboardService.actualizar$.subscribe(() => {
      console.log("Actualizando datos del dashboard..."); 
      this.cargarDashboard();
    });
  }
cargarDashboard() {
    const token = localStorage.getItem('token');

    this.http.get<any>(`${this.api}/cliente/dashboard`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (data) => {
        console.log("Asignando saldo:", data.saldo);
        
        this.saldo = data.saldo; 
        this.totalProyectos = data.totalProyectos;
        this.propuestas = data.propuestas || 0;
        this.activos = data.activos || 0;

        console.log("Variable this.saldo ahora vale:", this.saldo);
        this.cdr.detectChanges(); 
      },
      error: (err) => console.error("Error al cargar dashboard", err)
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