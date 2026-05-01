import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
@Component({
  selector: 'app-explorar-proyectos',
  standalone  : true,
  imports: [CommonModule],
  templateUrl: './explorar-proyectos.html',
  styleUrl: './explorar-proyectos.css',
})
export class ExplorarProyectos implements OnInit {

  proyectos: any[] = [];
  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarProyectos();
  }

  cargarProyectos() {
    const token = localStorage.getItem('token');

    this.http.get<any[]>(`${this.api}/proyectos`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (data) => {
        console.log("PROYECTOS:", data); 
        this.proyectos = data;
      },
      error: (err) => console.error("ERROR:", err)
    });
  }
}
