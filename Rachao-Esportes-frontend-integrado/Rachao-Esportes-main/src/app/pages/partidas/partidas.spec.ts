import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Auth, Partida } from '../../services/api';
import { Partidas } from './partidas';
describe('Partidas', () => {
  let component: Partidas;
  let http: HttpTestingController;
  const partida = {
    id: 3,
    jogadorHostId: 8,
    jogadorHostNome: 'Ana',
    esporteId: 1,
    esporte: 'FUTEBOL',
    qtdTimes: 2,
    qtdJogadoresTime: 5,
    dataHora: '2099-12-10T19:00:00',
    local: 'Quadra Central',
    status: 'PENDENTE',
    inscritos: 2,
    vagasRestantes: 8,
  } satisfies Partida;
  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      imports: [Partidas],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    });
    TestBed.inject(Auth).sessao.set({
      token: 'teste',
      jogadorId: 7,
      nome: 'Maria',
      expiraEmSegundos: 43200,
      expiraEm: Date.now() + 43200000,
    });
    component = TestBed.createComponent(Partidas).componentInstance;
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());
  it('carrega partidas e cruza as inscrições do próprio jogador', () => {
    component.carregar();
    http.expectOne('/api/partidas').flush([partida]);
    http
      .expectOne('/api/jogadores/7/partidas')
      .flush([{ id: 1, partidaId: 3, jogadorId: 7, jogadorNome: 'Maria' }]);
    component.somenteMinhas = true;
    expect(component.filtradas).toEqual([partida]);
    expect(component.host(partida)).toBe(false);
    component.busca = 'inexistente';
    expect(component.filtradas).toEqual([]);
  });
  it('não oferece inscrições em partidas passadas ou canceladas', () => {
    expect(component.futura(partida)).toBe(true);
    expect(component.futura({ ...partida, status: 'CANCELADA' })).toBe(false);
    expect(component.futura({ ...partida, dataHora: '2000-01-01T19:00:00' })).toBe(false);
  });
});
