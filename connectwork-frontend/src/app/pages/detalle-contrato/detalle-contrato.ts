import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-detalle-contrato',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './detalle-contrato.html',
  styleUrls: ['./detalle-contrato.css'],
})
export class DetalleContrato implements OnInit {


  contratoId!: number;
  entregas: any[] = [];
  cargando = true;

  descripcionEntrega: string = '';
  archivoUrl: string = '';

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute, 
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.contratoId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarEntregas();
  }

 cargarEntregas() {
  const token = localStorage.getItem('token');

  this.cargando = true;
  this.cdr.detectChanges(); 

  this.http.get<any[]>(`${this.api}/entregas?contratoId=${this.contratoId}`, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: (data) => {
      this.entregas = data || [];
      this.cargando = false;
      this.cdr.detectChanges(); 
    },
    error: (err) => {
      console.error(err);
      this.entregas = [];
      this.cargando = false;
      this.cdr.detectChanges();
    }
  });
}

  subirEntrega() {
    const token = localStorage.getItem('token');

    if (!this.descripcionEntrega || !this.archivoUrl) {
      alert("Completa todos los campos");
      return;
    }

    const body = {
      contratoId: this.contratoId,
      descripcion: this.descripcionEntrega,
     archivo: this.archivoUrl
    };

    this.http.post(`${this.api}/entregas`, body, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: () => {
        alert("Entrega enviada");

        this.descripcionEntrega = '';
        this.archivoUrl = '';

        this.cargarEntregas(); 
      },
      error: (err) => {
        console.error(err);
        alert("Error al subir entrega");
      }
    });
  }

  hayPendiente(): boolean {
  return this.entregas.some(e => e.estado === 'PENDIENTE');
}
  regresar() {
    this.router.navigate(['/dashboard-freelancer']);
  }
}
