import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class PlataformaService {

  private api =
    'http://localhost:8080/Proyecto2IPC2';

  constructor(
    private http: HttpClient
  ) {}

  obtenerDatosSaldos(): Observable<any> {

    return this.http.get<any>(
      `${this.api}/admin/saldos`
    );
  }
}
