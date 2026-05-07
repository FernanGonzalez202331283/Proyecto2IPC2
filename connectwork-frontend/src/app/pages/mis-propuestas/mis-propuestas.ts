import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-mis-propuestas',
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-propuestas.html',
  styleUrl: './mis-propuestas.css',
})
export class MisPropuestas implements OnInit {

  propuestas: any[] = [];

  constructor(private http: HttpClient,  private cd: ChangeDetectorRef, private router: Router) {}

  ngOnInit() {
    this.cargarPropuestas();
  }

  cargarPropuestas() {

  const token = localStorage.getItem('token');

  this.http.get<any[]>(
    'http://localhost:8080/Proyecto2IPC2/freelancer/propuestas',
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  ).subscribe(data => {
    this.propuestas = data;
    this.cd.detectChanges();
  });
}

 retirar(id: number) {

  const token = localStorage.getItem('token');

  this.http.post(
    `http://localhost:8080/Proyecto2IPC2/freelancer/propuesta/retirar?id=${id}`, 
    {},
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  ).subscribe({
    next: () => {
      const propuesta = this.propuestas.find(p => p.id === id);
      if (propuesta) {
        propuesta.estado = 'RETIRADA';
      }
      this.cd.detectChanges();

    },
    error: (err) => {
      console.log(err);
    }
  });
}
regresar() {
  this.router.navigate(['/dashboard-freelancer']);
}
}
