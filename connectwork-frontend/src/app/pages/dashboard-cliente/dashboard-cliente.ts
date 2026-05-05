import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { DashboardService } from '../../services/dashboard';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { interval } from 'rxjs';
import { Subscription } from 'rxjs';
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
  private sub!: Subscription;

  constructor(
    private http: HttpClient,
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit() {
    const userStr = localStorage.getItem('usuario');
    const token = localStorage.getItem('token');

    if (!userStr || !token) {
      this.logout();
      return;
    }

    const user = JSON.parse(userStr);
    this.username = user.username;

    this.cargarDashboard();

    // evento manual
    this.dashboardService.actualizar$.subscribe(() => {
      this.cargarDashboard();
    });

    // polling controlado
    this.sub = interval(5000).subscribe(() => {
      if (localStorage.getItem('token')) {
        this.cargarDashboard();
      }
    });
  }

  cargarDashboard() {
    const token = localStorage.getItem('token');

    if (!token) {
      this.logout();
      return;
    }

    this.http.get<any>(`${this.api}/cliente/dashboard`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (data) => {
        this.saldo = data.saldo || 0;
        this.totalProyectos = data.totalProyectos || 0;
        this.propuestas = data.propuestas || 0;
        this.activos = data.activos || 0;

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Error dashboard", err);

        if (err.status === 401) {
          this.logout();
        }
      }
    });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');

    this.auth.logout?.();
    this.router.navigate(['/login']);
  }

  ngOnDestroy() {
    this.sub?.unsubscribe();
  }
}