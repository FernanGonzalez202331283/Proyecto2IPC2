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
import { ExplorarProyectos } from './pages/explorar-proyectos/explorar-proyectos';
import { DetalleProyecto } from './pages/detalle-proyecto/detalle-proyecto';
import { EnviarPropuesta } from './pages/enviar-propuesta/enviar-propuesta';
import { PropuestasCliente } from './pages/propuestas-cliente/propuestas-cliente';
import { ContratosActivos } from './pages/contratos-activos/contratos-activos';
import { ClienteContratoDetalle } from './pages/cliente-contrato-detalle/cliente-contrato-detalle';
import { ClienteContratos } from './pages/cliente-contratos/cliente-contratos';
import { CalificarFreelancer } from './pages/calificar-freelancer/calificar-freelancer';
import { AdminUsuario } from './pages/admin-usuario/admin-usuario';
import { AdminSaldos } from './pages/admin/admin-saldos/admin-saldos';
import { AdminComision } from './pages/admin-comision/admin-comision';
import { TopFreelancers } from './pages/top-freelancers/top-freelancers';
import { TopCategorias } from './pages/top-categorias/top-categorias';
import { ReporteIngresos } from './pages/reporte-ingresos/reporte-ingresos';
import { ReporteProyectos } from './pages/reporte-proyectos/reporte-proyectos';
import { ReporteRecargas } from './pages/reporte-recargas/reporte-recargas';
import { ReporteGastosCategoria } from './pages/reporte-gastos-categoria/reporte-gastos-categoria';
import { ReporteContratosFreelancer } from './pages/reporte-contratos-freelancer/reporte-contratos-freelancer';
import { ReporteTopCategoriasFreelancer } from './pages/reporte-top-categorias-freelancer/reporte-top-categorias-freelancer';
import { ReportePropuestasFreelancer } from './pages/reporte-propuestas-freelancer/reporte-propuestas-freelancer';
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
},
{
  path: 'explorar-proyectos',
  component: ExplorarProyectos
},
{
  path: 'proyecto/:id',
  component: DetalleProyecto
},
{
  path: 'enviar-propuesta/:id',
  loadComponent: () => import('./pages/enviar-propuesta/enviar-propuesta').then(m => m.EnviarPropuesta)
},
{
  path: 'cliente/propuestas',
  loadComponent: () =>
    import('./pages/propuestas-cliente/propuestas-cliente')
      .then(m => m.PropuestasCliente)
}, { path: 'contratos', component: ContratosActivos },
{
  path: 'contrato/:id',
  loadComponent: () => import('./pages/detalle-contrato/detalle-contrato')
    .then(m => m.DetalleContrato)
},
{
  path: 'cliente/contratos',
  loadComponent: () => import('./pages/cliente-contratos/cliente-contratos')
    .then(m => m.ClienteContratos)
},
 {
    path: 'cliente/contrato/:id',
    component: ClienteContratoDetalle
  },
  {
    path: 'mis-propuestas',
    loadComponent: () => import('./pages/mis-propuestas/mis-propuestas')
      .then(m => m.MisPropuestas)
  },
  {
  path: 'solicitar-habilidad',
  loadComponent: () =>
    import('./pages/solicitar-habilidad/solicitar-habilidad')
      .then(m => m.SolicitarHabilidad)
},
 {
    path: 'calificar-freelancer/:id',
    component: CalificarFreelancer
  },
  {
  path: 'admin/usuarios',
  component: AdminUsuario
},
{
  path: 'solicitar-categoria',
  loadComponent: () =>
    import('./pages/solicitar-categoria/solicitar-categoria')
      .then(m => m.SolicitarCategoria)
},
{
  path: 'admin/categorias',
  loadComponent: () =>
    import('./pages/admin-categorias/admin-categorias')
      .then(m => m.AdminCategorias)
},
{
  path: 'admin/solicitudes',
  loadComponent: () =>
    import('./pages/admin-solicitudes/admin-solicitudes')
    .then(m => m.AdminSolicitudes)
},
{
  path: 'admin/saldos',
  component: AdminSaldos
},
{
  path: 'admin/comision',
  component: AdminComision
},
{
  path: 'admin/reportes',
  loadComponent: () =>
    import('./pages/admin-reportes/admin-reportes')
    .then(m => m.AdminReportes)
},
{
  path: 'admin/reportes/top-freelancers',
  component: TopFreelancers
},
{
  path: 'admin/reportes/top-categorias',
  component: TopCategorias
},
{
  path: 'admin/reportes/ingresos-plataforma',
  component: ReporteIngresos
},
{ path: 'cliente/reportes/proyectos',
  component: ReporteProyectos
},
{
  path: 'cliente/reportes/recargas',
  component: ReporteRecargas
},{
  path: 'cliente/reportes/gastos-categorias',
  component: ReporteGastosCategoria
},{
  path: 'freelancer/reportes/contratos',
  component: ReporteContratosFreelancer
},
{
  path:
  'freelancer/reportes/top-categorias',

  component:
  ReporteTopCategoriasFreelancer
},
{
  path:
  'freelancer/reportes/propuestas',

  component:
  ReportePropuestasFreelancer
}
];
