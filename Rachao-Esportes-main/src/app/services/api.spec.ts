import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { Api, Auth, authInterceptor } from './api';

describe('Integração com o back-end', () => {
  let http: HttpTestingController;
  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => {
    http.verify();
    sessionStorage.clear();
  });
  function login() {
    TestBed.inject(Auth).login(' jogador@example.com ', 'Senha123!').subscribe();
    const req = http.expectOne('/api/auth/login');
    expect(req.request.body).toEqual({ email: 'jogador@example.com', senha: 'Senha123!' });
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({ token: 'sessao-teste', jogadorId: 7, nome: 'Jogador', expiraEmSegundos: 43200 });
  }
  it('envia o token e inscreve sem aceitar um jogador arbitrário no corpo', () => {
    login();
    TestBed.inject(Api).entrar(3).subscribe();
    const req = http.expectOne('/api/partidas/3/inscricoes');
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe('Bearer sessao-teste');
    expect(req.request.body).toEqual({});
    req.flush({ id: 1, partidaId: 3, jogadorId: 7, jogadorNome: 'Jogador' });
  });
  it('não vaza token para serviços externos', () => {
    login();
    TestBed.inject(HttpClient).get('https://viacep.com.br/ws/01310930/json/').subscribe();
    const req = http.expectOne('https://viacep.com.br/ws/01310930/json/');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });
  it('descarta sessão quando o servidor rejeita o token', () => {
    login();
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate').mockResolvedValue(true);
    TestBed.inject(Api)
      .minhas(7)
      .subscribe({ error: () => {} });
    http
      .expectOne('/api/jogadores/7/partidas')
      .flush({}, { status: 401, statusText: 'Unauthorized' });
    expect(TestBed.inject(Auth).sessao()).toBeNull();
    expect(sessionStorage.getItem('rachao.sessao')).toBeNull();
    expect(router.navigate).toHaveBeenCalled();
  });
  it('ignora sessão expirada ao recarregar', () => {
    sessionStorage.setItem(
      'rachao.sessao',
      JSON.stringify({ token: 'velho', jogadorId: 7, expiraEm: Date.now() - 1 }),
    );
    expect(TestBed.inject(Auth).ativa()).toBe(false);
  });
});
