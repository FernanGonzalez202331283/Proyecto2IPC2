import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {

  // DATOS EJEMPLO
  solicitudesHabilidades = 0;
  solicitudesCategorias = 0;
  categorias = 0;
  habilidades = 0;

  porcentajeComision = 10;

  constructor(private router: Router) {}

  ngOnInit() {

    // luego acá cargas desde backend
    this.solicitudesHabilidades = 4;
    this.solicitudesCategorias = 2;
    this.categorias = 8;
    this.habilidades = 20;
  }

  // NAVEGACIÓN
  irSolicitudesHabilidades() {
    this.router.navigate(['/admin/solicitudes-habilidades']);
  }

  irSolicitudesCategorias() {
    this.router.navigate(['/admin/solicitudes-categorias']);
  }

  irCategorias() {
    this.router.navigate(['/admin/categorias']);
  }

  irHabilidades() {
    this.router.navigate(['/admin/habilidades']);
  }

  irComision() {
    this.router.navigate(['/admin/comision']);
  }

  irReportes() {
    this.router.navigate(['/admin/reportes']);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  irA(ruta: string) {
  this.router.navigate([ruta]);
}

}
