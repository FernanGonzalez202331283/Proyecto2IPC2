import { Component } from '@angular/core';
import { AuthService } from '../../services/auth'; 
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = '';
  password = '';

  constructor(private auth: AuthService, private router: Router) {}

 login() {
  this.auth.login({
    username: this.username,
    password: this.password
  }).subscribe({
    next: (resp: any) => {

      this.auth.guardarSesion(resp);

      // ADMIN
      if (resp.rol === 'ADMIN') {
        this.router.navigate(['/admin']);
        return;
      }

      // CLIENTE
      if (resp.rol === 'CLIENTE') {
        if (resp.perfilCompleto == 0) {
          this.router.navigate(['/completar-cliente']);
        } else {
          this.router.navigate(['/dashboard-cliente']);
        }
        return;
      }

      // FREELANCER
      if (resp.rol === 'FREELANCER') {
        if (resp.perfilCompleto == 0) {
          this.router.navigate(['/completar-freelancer']);
        } else {
          this.router.navigate(['/dashboard-freelancer']);
        }
        return;
      }

    },
    error: (err) => {
      console.error(err);
      alert('Credenciales incorrectas o error en el servidor');
    }
  });
}

  irRegistro() {
  this.router.navigate(['/register']);
}
}
