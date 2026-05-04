import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-contratos-activos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contratos-activos.html',
  styleUrl: './contratos-activos.css',
})
export class ContratosActivos implements OnInit {
  cargando = true;
  contratos: any[] = [];
  contratoSeleccionado: any = null;
  entregas: any[] = [];

  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router, private cd: ChangeDetectorRef ) {}

  ngOnInit() {
    this.cargarContratos();
  }

 cargarContratos() {
  const token = localStorage.getItem('token');

  if (!token) {
    console.error("No hay token");
    this.cargando = false;
    this.router.navigate(['/login']);
    return;
  }

  console.log("Token:", token);

  this.cargando = true;

  this.http.get<any[]>(`${this.api}/contratos`, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: (data) => {
      console.log("DATA:", data);

      this.contratos = data || []; 
      this.cargando = false;

      this.cd.detectChanges(); 
    },
    error: (err) => {
      console.error("ERROR:", err);

      this.contratos = []; 

      this.cd.detectChanges(); 
    }
  });
}

  seleccionarContrato(c: any) {
    this.contratoSeleccionado = c;
    this.cargarEntregas(c.contratoId);
  }

  cargarEntregas(id: number) {
    const token = localStorage.getItem('token');

    this.http.get<any[]>(`${this.api}/entregas?contratoId=${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe(data => {
      this.entregas = data;
    });
  }
  verContrato(id: number) {
  this.router.navigate(['/contrato', id]);
}
  regresar() {
    this.router.navigate(['/dashboard-freelancer']);
  }
}
