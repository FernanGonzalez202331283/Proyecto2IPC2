import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-admin-comision',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-comision.html',
  styleUrl: './admin-comision.css',
})
export class AdminComision implements OnInit{

  porcentajeActual: number = 0;

  nuevoPorcentaje: number = 0;

  mensaje: string = '';

  url =
    'http://localhost:8080/Proyecto2IPC2/ConfiguracionComisionServlet';

  constructor(
    private http: HttpClient, private cd: ChangeDetectorRef, private router: Router
  ) {}

  ngOnInit(): void {

    this.obtenerComision();
  }

  obtenerComision() {

    this.http.get<any>(this.url)
      .subscribe({

        next: (res) => {

          this.porcentajeActual =
            res.porcentajeActual;
            this.cd.detectChanges();
        },

        error: (err) => {

          console.log(err);

          this.mensaje =
            'Error al cargar comisión';
            this.cd.detectChanges();
        }
      });
  }

  actualizarComision() {

    console.log(this.nuevoPorcentaje);

    if (
      Number(this.nuevoPorcentaje) <= 0
    ) {

      this.mensaje =
        'Ingrese un porcentaje válido';

      return;
    }

    const body = {

      nuevoPorcentaje:
        Number(this.nuevoPorcentaje)
    };

    this.http.post<any>(this.url, body)
      .subscribe({

        next: (res) => {

          this.mensaje =
            res.mensaje;

          this.obtenerComision();

          this.nuevoPorcentaje = null as any;
          this.cd.detectChanges();
           this.obtenerComision();
        },

        error: (err) => {

          console.log(err);

          this.mensaje =
            'Error al actualizar comisión';
             this.obtenerComision();
        }
      });
  }
  regresar() {
  this.router.navigate(['/admin']);
}
}
