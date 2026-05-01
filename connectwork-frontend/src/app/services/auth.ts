import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
    private api = 'http://localhost:8080/Proyecto2IPC2';

  usuario = signal<any>(null);

  constructor(private http: HttpClient) {}

  login(data: any) {
    return this.http.post(`${this.api}/login`, data);
  }

  guardarSesion(resp: any) {
  localStorage.setItem('token', resp.token);

  const user = {
    id: resp.id,
    username: resp.username,
    rol: resp.rol,
    perfilCompleto: resp.perfilCompleto
  };

  localStorage.setItem('usuario', JSON.stringify(user)); // 🔥 IMPORTANTE
  this.usuario.set(user);
}

  logout() {
    localStorage.removeItem('token');
    this.usuario.set(null);
  }
  register(data: any) {
  return this.http.post(`${this.api}/registro`, data);
}
}
