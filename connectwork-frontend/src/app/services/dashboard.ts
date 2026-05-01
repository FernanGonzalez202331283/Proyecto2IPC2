import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  // BehaviorSubject guarda el último valor, así el Dashboard lo lee al "nacer"
  private actualizarSource = new BehaviorSubject<boolean>(false);
  actualizar$ = this.actualizarSource.asObservable();

  notificarActualizacion() {
    this.actualizarSource.next(true);
  }
}
