import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, tap, throwError } from 'rxjs';

export interface Jogador {
  id: number;
  nome: string;
  email: string;
  dataNascimento: string;
  cep: string | null;
  descricao: string | null;
  altura: number | null;
}
export interface Esporte {
  id: number;
  esporte: string;
}
export interface Partida {
  id: number;
  jogadorHostId: number;
  jogadorHostNome: string;
  esporteId: number;
  esporte: string;
  qtdTimes: number;
  qtdJogadoresTime: number;
  dataHora: string;
  local: string;
  status: string;
  inscritos: number;
  vagasRestantes: number;
}
export interface PartidaRequest {
  esporteId: number;
  qtdTimes: number;
  qtdJogadoresTime: number;
  dataHora: string;
  local: string;
}
export interface Inscricao {
  id: number;
  partidaId: number;
  jogadorId: number;
  jogadorNome: string;
}
interface LoginResponse {
  token: string;
  jogadorId: number;
  nome: string;
  expiraEmSegundos: number;
}
interface Sessao extends LoginResponse {
  expiraEm: number;
}
export const API = '/api';

export function mensagemErro(error: unknown): string {
  if (!(error instanceof HttpErrorResponse)) return 'Não foi possível concluir a operação.';
  if (error.status === 0)
    return 'Não foi possível conectar ao servidor. Verifique se o back-end está em execução.';
  if (error.status === 401) return 'Sua sessão expirou. Entre novamente.';
  const body = error.error;
  return (
    body?.erro ||
    body?.detail ||
    body?.message ||
    (
      {
        403: 'Você não tem permissão para esta operação.',
        404: 'Registro não encontrado.',
        409: 'A operação conflita com o estado atual. Atualize a página.',
      } as Record<number, string>
    )[error.status] ||
    'Não foi possível concluir a operação. Tente novamente.'
  );
}

@Injectable({ providedIn: 'root' })
export class Auth {
  private http = inject(HttpClient);
  readonly sessao = signal<Sessao | null>(this.restaurar());
  private restaurar(): Sessao | null {
    try {
      const s = JSON.parse(sessionStorage.getItem('rachao.sessao') || 'null');
      return s &&
        typeof s.token === 'string' &&
        Number.isInteger(s.jogadorId) &&
        s.expiraEm > Date.now()
        ? s
        : null;
    } catch {
      return null;
    }
  }
  ativa() {
    const s = this.sessao();
    return !!s && s.expiraEm > Date.now();
  }
  limpar() {
    sessionStorage.removeItem('rachao.sessao');
    this.sessao.set(null);
  }
  login(email: string, senha: string) {
    return this.http.post<LoginResponse>(`${API}/auth/login`, { email: email.trim(), senha }).pipe(
      tap((r) => {
        const s = { ...r, expiraEm: Date.now() + r.expiraEmSegundos * 1000 };
        sessionStorage.setItem('rachao.sessao', JSON.stringify(s));
        this.sessao.set(s);
      }),
    );
  }
  logout() {
    return this.http.post<void>(`${API}/auth/logout`, {});
  }
}
export const authGuard: CanActivateFn = (_, state) =>
  inject(Auth).ativa() ||
  inject(Router).createUrlTree(['/login'], { queryParams: { retorno: state.url } });
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(Auth);
  const router = inject(Router);
  const api = req.url === API || req.url.startsWith(`${API}/`);
  const publicAuth =
    req.url === `${API}/auth/login` || (req.method === 'POST' && req.url === `${API}/jogadores`);
  if (api && !publicAuth && auth.ativa())
    req = req.clone({ setHeaders: { Authorization: `Bearer ${auth.sessao()!.token}` } });
  return next(req).pipe(
    catchError((error) => {
      if (api && !publicAuth && error.status === 401) {
        const retorno = router.url;
        auth.limpar();
        void router.navigate(['/login'], { queryParams: { retorno } });
      }
      return throwError(() => error);
    }),
  );
};

@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);
  cadastrar(dados: { nome: string; email: string; senha: string; dataNascimento: string }) {
    return this.http.post<Jogador>(`${API}/jogadores`, dados);
  }
  perfil(id: number) {
    return this.http.get<Jogador>(`${API}/jogadores/${id}`);
  }
  salvarPerfil(
    id: number,
    dados: { cpf?: string; cep?: string; descricao?: string; altura?: number },
  ) {
    return this.http.put<Jogador>(`${API}/jogadores/${id}/perfil`, dados);
  }
  esportes() {
    return this.http.get<Esporte[]>(`${API}/esportes`);
  }
  partidas() {
    return this.http.get<Partida[]>(`${API}/partidas`);
  }
  criar(dados: PartidaRequest) {
    return this.http.post<Partida>(`${API}/partidas`, dados);
  }
  editar(id: number, dados: PartidaRequest) {
    return this.http.put<Partida>(`${API}/partidas/${id}`, dados);
  }
  cancelar(id: number) {
    return this.http.post<Partida>(`${API}/partidas/${id}/cancelar`, {});
  }
  inscricoes(id: number) {
    return this.http.get<Inscricao[]>(`${API}/partidas/${id}/inscricoes`);
  }
  minhas(id: number) {
    return this.http.get<Inscricao[]>(`${API}/jogadores/${id}/partidas`);
  }
  entrar(id: number) {
    return this.http.post<Inscricao>(`${API}/partidas/${id}/inscricoes`, {});
  }
  sair(id: number, jogadorId: number) {
    return this.http.delete<void>(`${API}/partidas/${id}/inscricoes/${jogadorId}`);
  }
}
