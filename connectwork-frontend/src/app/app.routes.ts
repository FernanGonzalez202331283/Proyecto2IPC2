import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Register } from './pages/register/register';
import { Admin } from './pages/admin/admin';
import { CompletarCliente } from './pages/completar-cliente/completar-cliente';
import { CompletarFreelancer } from './pages/completar-freelancer/completar-freelancer';
import { DashboardCliente } from './pages/dashboard-cliente/dashboard-cliente';
import { DashboardFreelancer } from './pages/dashboard-freelancer/dashboard-freelancer';
import { ListarProyectos } from './pages/listar-proyectos/listar-proyectos';
import { authGuard } from './guards/auth-guard';
import { PublicarProyecto } from './pages/publicar-proyecto/publicar-proyecto';
import { Recargar } from './pages/recargar/recargar';
import { MisProyectos } from './pages/mis-proyectos/mis-proyectos';
export const routes: Routes = [
    { path: '', component: Login },
    { path: 'login', component: Login },
    { path: 'dashboard', component: Dashboard },
    { path: 'register', component: Register },

    { path: 'admin', component: Admin },
  { path: 'completar-cliente', component: CompletarCliente },
  { path: 'completar-freelancer', component: CompletarFreelancer },
  { path: 'dashboard-cliente', component: DashboardCliente },
  { path: 'dashboard-freelancer', component: DashboardFreelancer },
  { 
    path: 'proyectos', 
    component: ListarProyectos, 
    canActivate: [authGuard],
    data: { roles: ['CLIENTE', 'FREELANCER'] }
  },
  {
  path: 'publicar-proyecto',
  loadComponent: () => import('./pages/publicar-proyecto/publicar-proyecto')
    .then(m => m.PublicarProyecto)
},
{ path: 'recargar', component: Recargar },

{
  path: 'mis-proyectos',
  loadComponent: () => import('./pages/mis-proyectos/mis-proyectos')
    .then(m => m.MisProyectos)
},

{
  path: 'dashboard-freelancer',
  loadComponent: () => import('./pages/dashboard-freelancer/dashboard-freelancer')
    .then(m => m.DashboardFreelancer)
}
];
