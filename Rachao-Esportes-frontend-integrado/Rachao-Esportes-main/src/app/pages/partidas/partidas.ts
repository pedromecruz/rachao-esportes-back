import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin, Observable } from 'rxjs';
import { Api, Auth, Partida, Inscricao, mensagemErro } from '../../services/api';
import { CriarPartida } from '../criar-partida/criar-partida';
@Component({
  selector: 'app-partidas',
  imports: [RouterLink, CriarPartida, DatePipe, FormsModule],
  templateUrl: './partidas.html',
  styleUrl: './partidas.css',
})
export class Partidas implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  private api = inject(Api);
  auth = inject(Auth);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  partidas: Partida[] = [];
  minhas: Inscricao[] = [];
  participantes: Inscricao[] = [];
  detalhe: Partida | null = null;
  editar: Partida | null = null;
  confirmacao: Partida | null = null;
  modalAberto = false;
  loading = false;
  busy = false;
  erro = '';
  sucesso = '';
  busca = '';
  esporte = '';
  somenteMinhas = false;
  ngOnInit() {
    this.route.data.subscribe((d) => {
      this.somenteMinhas = !!d['minhas'];
      this.carregar();
    });
  }
  carregar() {
    this.loading = true;
    this.erro = '';
    forkJoin({
      partidas: this.api.partidas(),
      minhas: this.api.minhas(this.auth.sessao()!.jogadorId),
    }).subscribe({
      next: (r) => {
        this.cdr.markForCheck();
        this.partidas = r.partidas;
        this.minhas = r.minhas;
        this.loading = false;
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.loading = false;
        this.erro = mensagemErro(e);
      },
    });
  }
  get filtradas() {
    return this.partidas
      .filter(
        (p) =>
          (!this.somenteMinhas || this.inscrito(p)) &&
          (!this.esporte || p.esporte === this.esporte) &&
          `${p.local} ${p.jogadorHostNome}`.toLowerCase().includes(this.busca.toLowerCase()),
      )
      .sort((a, b) => a.dataHora.localeCompare(b.dataHora));
  }
  inscrito(p: Partida) {
    return this.minhas.some((i) => i.partidaId === p.id);
  }
  host(p: Partida) {
    return p.jogadorHostId === this.auth.sessao()?.jogadorId;
  }
  futura(p: Partida) {
    return p.status === 'PENDENTE' && new Date(p.dataHora + '-03:00').getTime() > Date.now();
  }
  abrirModal(p: Partida | null = null) {
    this.editar = p;
    this.modalAberto = true;
  }
  fecharModal() {
    this.modalAberto = false;
    this.editar = null;
  }
  executar(op: Observable<unknown>, mensagem: string) {
    if (this.busy) return;
    this.busy = true;
    this.erro = '';
    this.sucesso = '';
    op.subscribe({
      next: () => {
        this.cdr.markForCheck();
        this.busy = false;
        this.sucesso = mensagem;
        this.detalhe = null;
        this.confirmacao = null;
        this.carregar();
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.busy = false;
        this.erro = mensagemErro(e);
      },
    });
  }
  entrar(p: Partida) {
    this.executar(this.api.entrar(p.id), 'Inscrição confirmada!');
  }
  sair(p: Partida) {
    this.executar(this.api.sair(p.id, this.auth.sessao()!.jogadorId), 'Você saiu da partida.');
  }
  cancelar(p: Partida) {
    this.executar(this.api.cancelar(p.id), 'Partida cancelada.');
  }
  ver(p: Partida) {
    if (this.busy) return;
    this.busy = true;
    this.erro = '';
    this.api.inscricoes(p.id).subscribe({
      next: (i) => {
        this.cdr.markForCheck();
        this.participantes = i;
        this.detalhe = p;
        this.busy = false;
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.busy = false;
        this.erro = mensagemErro(e);
      },
    });
  }
  logout() {
    if (this.busy) return;
    this.busy = true;
    this.erro = '';
    this.auth.logout().subscribe({
      next: () => {
        this.cdr.markForCheck();
        this.auth.limpar();
        void this.router.navigate(['/login']);
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.busy = false;
        this.erro = mensagemErro(e);
      },
    });
  }
}
