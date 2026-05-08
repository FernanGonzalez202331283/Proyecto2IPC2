import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdminSolicitudesService {
  private api =
    'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient
  ) {}


  listarCategorias(): Observable<any[]> {

    return this.http.get<any[]>(
      `${this.api}/admin/solicitudes-categoria`
    );
  }

  aceptarCategoria(id: number) {

    return this.http.put(
      `${this.api}/admin/solicitudes-categoria?id=${id}&accion=aceptar`,
      {}
    );
  }

  rechazarCategoria(id: number) {

    return this.http.put(
      `${this.api}/admin/solicitudes-categoria?id=${id}&accion=rechazar`,
      {}
    );
  }

  // =========================
  // HABILIDADES
  // =========================

  listarHabilidades(): Observable<any[]> {

    return this.http.get<any[]>(
      `${this.api}/admin/solicitudes-habilidad`
    );
  }

  aceptarHabilidad(id: number) {

    return this.http.put(
      `${this.api}/admin/solicitudes-habilidad?id=${id}&accion=aceptar`,
      {}
    );
  }

  rechazarHabilidad(id: number) {

    return this.http.put(
      `${this.api}/admin/solicitudes-habilidad?id=${id}&accion=rechazar`,
      {}
    );
  }
}
