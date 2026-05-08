import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-admin-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-reportes.html',
  styleUrl: './admin-reportes.css',
})
export class AdminReportes implements OnInit {
  historial: any[] = [];

  mensaje: string = '';

  url = 'http://localhost:8080/Proyecto2IPC2/HistorialComisionServlet';

  constructor(
    private http: HttpClient,
    private cd: ChangeDetectorRef,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.obtenerHistorial();
  }

  obtenerHistorial() {
    this.http.get<any[]>(this.url).subscribe({
      next: (res) => {
        this.historial = res;
        this.cd.markForCheck();
      },

      error: (err) => {
        console.log(err);

        this.mensaje = 'Error al cargar historial';
        this.cd.markForCheck();
      },
    });
  }
  regresar() {
    this.router.navigate(['/admin']);
  }
}
