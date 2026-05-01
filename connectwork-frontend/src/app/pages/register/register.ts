import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router, RouterModule } from '@angular/router'; 
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  nombre = '';
  username = '';
  password = '';
  correo = '';
  telefono = '';
  direccion = '';
  cui = '';
  fechaNacimiento = '';
  rol = '';
  
  constructor(private auth: AuthService, private router: Router) {}

  registrar() {
   const datos = {
      nombre: this.nombre,
      username: this.username,
      password: this.password,
      correo: this.correo,
      telefono: this.telefono,
      direccion: this.direccion,
      cui: this.cui,
      fechaNacimiento: this.fechaNacimiento,
      rol: this.rol
    };

    this.auth.register(datos).subscribe({
      next: () => {
        alert('Usuario registrado correctamente');
        this.router.navigate(['/login']);
      },
      error: (err) => alert('Error: ' + err.error.error)
    });
  }
}
