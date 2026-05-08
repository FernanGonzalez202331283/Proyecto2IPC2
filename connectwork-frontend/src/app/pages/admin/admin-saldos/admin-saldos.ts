import { ChangeDetectorRef, Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PlataformaService } from '../../../services/plataforma';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-saldos',
  standalone:true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-saldos.html',
  styleUrl: './admin-saldos.css',
})
export class AdminSaldos implements OnInit {

  saldo = 0;

  comisiones: any[] = [];

  constructor(
    private plataformaService: PlataformaService, private cd: ChangeDetectorRef, private router: Router 
  ) {}

  ngOnInit(): void {

    this.cargarDatos();
  }

  cargarDatos(): void {

    this.plataformaService
      .obtenerDatosSaldos()
      .subscribe({

        next: (data: any) => {

          this.saldo =
            data.saldo;
          this.cd.markForCheck();

          this.comisiones =
            data.comisiones;
          this.cd.markForCheck();
        },

        error: (err) => {
          console.error(err);
        }
      });
  }
  regresar() {
  this.router.navigate(['/admin']);
}
}
