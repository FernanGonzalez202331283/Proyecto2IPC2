import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-cliente-contrato-detalle',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cliente-contrato-detalle.html',
  styleUrl: './cliente-contrato-detalle.css',
})
export class ClienteContratoDetalle implements OnInit {
  
  entregas: any[] = [];
  ultimaEntrega: any;
  contratoId!: number;

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
   this.route.paramMap.subscribe(params => {
    this.contratoId = Number(params.get('id'));
    this.cargarEntregas();
  });

  }

  cargarEntregas() {
  const token = localStorage.getItem('token');

  this.http.get<any[]>(`${this.api}/entregas?contratoId=${this.contratoId}`, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe(data => {

    console.log("ENTREGAS:", data);

    this.entregas = data || [];
    this.ultimaEntrega = this.entregas.length > 0 ? this.entregas[0] : null;
    this.cdr.detectChanges();

  });
}

  aprobar() {
  this.http.post(`${this.api}/entregas/revisar`, {
    accion: "APROBAR",
    contratoId: this.contratoId
  }).subscribe(() => {
    alert("Entrega aprobada");
    this.cargarEntregas();
  });
}

  rechazar() {
    const motivo = prompt("Motivo del rechazo:");
    if (!motivo) return;

    this.http.post(`${this.api}/entregas/revisar`, {
      accion: "RECHAZAR",
      entregaId: this.ultimaEntrega.id,
      contratoId: this.contratoId,
      motivo
    }).subscribe(() => {
      alert("Entrega rechazada");
      this.cargarEntregas();
    });
  }

  cancelar() {
    const motivo = prompt("Motivo de cancelación:");
    if (!motivo) return;

    this.http.post(`${this.api}/entregas/revisar`, {
      accion: "CANCELAR",
      contratoId: this.contratoId,
      motivo
    }).subscribe(() => {
      alert("Contrato cancelado");
    });
  }
}
