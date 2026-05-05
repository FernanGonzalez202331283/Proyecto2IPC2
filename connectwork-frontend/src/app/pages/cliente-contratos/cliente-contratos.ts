import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-cliente-contratos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cliente-contratos.html',
  styleUrl: './cliente-contratos.css',
})
export class ClienteContratos implements OnInit {
  
  contratos: any[] = [];
  private api = 'http://localhost:8080/Proyecto2IPC2';

  constructor(private http: HttpClient, private router: Router, private cdr: ChangeDetectorRef) {

}

  ngOnInit() {
  this.cargar();
}

 cargar() {
  const token = localStorage.getItem('token');

  this.http.get<any[]>(`${this.api}/cliente/contratos`, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: (data) => {
      console.log("DATA QUE LLEGA:", data); 
     this.contratos = [...data];
      this.cdr.detectChanges();
    },
    error: (err) => console.error(err)
  });
}
  verDetalle(id: number) {
    this.router.navigate(['/cliente/contrato', id]);
  }
}
