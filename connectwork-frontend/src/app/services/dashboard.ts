import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private actualizarSource = new BehaviorSubject<boolean>(false);
  actualizar$ = this.actualizarSource.asObservable();

  notificarActualizacion() {
    this.actualizarSource.next(true);
  }
}
