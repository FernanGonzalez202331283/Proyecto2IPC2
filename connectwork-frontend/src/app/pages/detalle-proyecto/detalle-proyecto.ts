import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-detalle-proyecto',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './detalle-proyecto.html',
  styleUrl: './detalle-proyecto.css',
})
export class DetalleProyecto {

  
  proyecto: any;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private cd: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');

      if (id) {
        this.cargarProyecto(id);
      }
    });
  }

  cargarProyecto(id: any) {
    const token = localStorage.getItem('token');

    this.http.get<any>(`http://localhost:8080/Proyecto2IPC2/proyectos?id=${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe(data => {
      this.proyecto = data;
      this.cd.detectChanges();
    });
  }

  regresar() {
  this.router.navigate(['/explorar-proyectos']);
}
}
