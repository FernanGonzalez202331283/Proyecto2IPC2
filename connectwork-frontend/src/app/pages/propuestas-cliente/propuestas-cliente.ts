import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { NavigationEnd } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-propuestas-cliente',
  standalone: true,
  imports: [FormsModule, CommonModule ],
  templateUrl: './propuestas-cliente.html',
  styleUrl: './propuestas-cliente.css',
})
export class PropuestasCliente {

  propuestas: any[] = [];
  userId!: number;

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {

    const user = JSON.parse(localStorage.getItem('usuario')!);
    this.userId = user?.id;

    console.log("USER ID:", this.userId);

    if (!this.userId) return;

    this.cargarPropuestas();
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.cargarPropuestas();
      });
  }

  cargarPropuestas() {

    this.http.get<any[]>(
      `http://localhost:8080/Proyecto2IPC2/cliente/propuestas?userId=${this.userId}`
    ).subscribe({
      next: (data) => {
        console.log("PROPUESTAS:", data);

        this.propuestas = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }
  volver() {
  this.router.navigate(['/dashboard-cliente']);
}
}
