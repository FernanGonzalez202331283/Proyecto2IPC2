import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { DashboardService } from '../../services/dashboard';
@Component({
  selector: 'app-recargar',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './recargar.html',
  styleUrl: './recargar.css',
})
export class Recargar {

  monto = 0;
  private api = 'http://localhost:8080/Proyecto2IPC2';

 constructor(private http: HttpClient, private router: Router, private dashboardService: DashboardService) {}

  recargar() {
  if (this.monto <= 0) {
    alert('Ingresa un monto válido');
    return;
  }

  const token = localStorage.getItem('token');

  this.http.post(`${this.api}/cliente/recargar`, {
    monto: this.monto
  }, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: () => {
      // 1. Notificamos al servicio (para que el Dashboard sepa que debe refrescar)
  this.dashboardService.notificarActualizacion();
  
  // 2. El alert es CLAVE aquí: detiene la ejecución de JavaScript.
  // Mientras el usuario lee el mensaje y le da "Aceptar", 
  // MySQL tiene tiempo de sobra (milisegundos) para terminar el UPDATE.
  alert('Saldo recargado exitosamente');

  // 3. Limpiamos el input
  this.monto = 0;

  // 4. Navegamos. 
  // Al llegar al dashboard, como el servicio tiene el aviso, se ejecutará el GET.
  this.router.navigate(['/dashboard-cliente']);
    },
    error: (err) => {
      console.error(err);
      alert('Error al recargar saldo');
    }
  });
}

regresar() {
  window.history.back();
}
}
