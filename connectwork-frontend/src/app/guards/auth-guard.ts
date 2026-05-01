import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = (route) => {
  const router = inject(Router);

  const token = localStorage.getItem('token');
  const userStr = localStorage.getItem('usuario');

  const path = route.routeConfig?.path;

  // evita volver al login si ya se encuentra logueado
  if (path === 'login' && token) {
    router.navigate(['/']);
    return false;
  }

  // por si no es auteticado
  if (!token || !userStr) {
    router.navigate(['/login']);
    return false;
  }

  const user = JSON.parse(userStr);

  // valida el rol 
  const rolesPermitidos = route.data?.['roles'];

  if (rolesPermitidos && !rolesPermitidos.includes(user.rol)) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};