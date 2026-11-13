import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Cadastro } from './pages/cadastro/cadastro';
import { Partidas } from './pages/partidas/partidas';
import { Perfil } from './pages/perfil/perfil';
import { authGuard } from './services/api';
export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'cadastro', component: Cadastro },
  { path: 'partidas', component: Partidas, canActivate: [authGuard] },
  {
    path: 'minhas-partidas',
    component: Partidas,
    canActivate: [authGuard],
    data: { minhas: true },
  },
  { path: 'perfil', component: Perfil, canActivate: [authGuard] },
  { path: 'criar-partida', redirectTo: 'partidas', pathMatch: 'full' },
  { path: '**', redirectTo: '' },
];
