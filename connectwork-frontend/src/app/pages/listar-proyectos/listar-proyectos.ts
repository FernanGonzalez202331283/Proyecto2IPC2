import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-listar-proyectos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listar-proyectos.html',
  styleUrl: './listar-proyectos.css',
})
export class ListarProyectos  implements OnInit {
    proyectos: any[] = [];
  api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    const token = localStorage.getItem('token');

    this.http.get<any[]>(`${this.api}/Proyectos`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (res) => this.proyectos = res,
      error: (err) => console.error(err)
    });
  }
}
