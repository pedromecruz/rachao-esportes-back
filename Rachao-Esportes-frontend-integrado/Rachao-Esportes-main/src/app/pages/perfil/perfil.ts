import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Api, Auth, Jogador, mensagemErro } from '../../services/api';
@Component({
  selector: 'app-perfil',
  imports: [RouterLink, ReactiveFormsModule],
  template: `<main class="profile-page">
    <a routerLink="/partidas">← Voltar às partidas</a>
    <h1>Meu perfil</h1>
    @if (loading) {
      <p role="status">Carregando perfil…</p>
    }
    <p role="alert" class="error-message">{{ erro }}</p>
    <p role="status">{{ sucesso }}</p>
    @if (!jogador && !loading) {
      <button (click)="carregar()">Tentar novamente</button>
    }
    @if (jogador) {
      <p>{{ jogador.nome }} · {{ jogador.email }}</p>
      <p>Nascimento: {{ jogador.dataNascimento }}</p>
      <form [formGroup]="form" (ngSubmit)="salvar()">
        <label for="cpf">CPF (opcional; para alterar, informe o número completo)</label
        ><input id="cpf" formControlName="cpf" inputmode="numeric" placeholder="11 dígitos" /><small
          >O CPF cadastrado não é exibido.</small
        >
        <label for="cep">CEP (opcional)</label
        ><input id="cep" formControlName="cep" inputmode="numeric" placeholder="00000-000" />
        <label for="altura">Altura em metros (opcional)</label
        ><input id="altura" formControlName="altura" type="number" min="0.01" max="3" step="0.01" />
        <label for="descricao">Sobre você</label
        ><textarea id="descricao" formControlName="descricao" maxlength="500" rows="5"></textarea>
        @if (form.touched && form.invalid) {
          <p class="error-message">
            Confira CPF (11 dígitos), CEP (8 dígitos) e altura (até 3 metros).
          </p>
        }
        <button type="submit" [disabled]="busy">{{ busy ? 'Salvando…' : 'Salvar perfil' }}</button>
      </form>
    }
  </main>`,
  styles: [
    `
      .profile-page {
        max-width: 720px;
        margin: 40px auto;
        padding: 24px;
      }
      h1 {
        margin: 24px 0;
      }
      form {
        display: grid;
        gap: 12px;
        margin-top: 24px;
      }
      input,
      textarea {
        padding: 14px;
        border: 1px solid #aaa;
        border-radius: 8px;
      }
      button {
        padding: 14px;
        background: #ffbd00;
        border: 0;
        border-radius: 8px;
        cursor: pointer;
      }
      small {
        color: #555;
      }
    `,
  ],
})
export class Perfil implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  private api = inject(Api);
  private auth = inject(Auth);
  jogador: Jogador | null = null;
  loading = false;
  busy = false;
  erro = '';
  sucesso = '';
  form = new FormGroup({
    cpf: new FormControl('', {
      nonNullable: true,
      validators: [Validators.pattern(/^(\d{11}|\d{3}\.\d{3}\.\d{3}-\d{2})$/)],
    }),
    cep: new FormControl('', {
      nonNullable: true,
      validators: [Validators.pattern(/^\d{5}-?\d{3}$/)],
    }),
    descricao: new FormControl('', { nonNullable: true, validators: [Validators.maxLength(500)] }),
    altura: new FormControl<number | null>(null, [Validators.min(0.01), Validators.max(3)]),
  });
  ngOnInit() {
    this.carregar();
  }
  carregar() {
    this.loading = true;
    this.erro = '';
    this.api.perfil(this.auth.sessao()!.jogadorId).subscribe({
      next: (j) => {
        this.cdr.markForCheck();
        this.jogador = j;
        this.form.patchValue({ cep: j.cep || '', descricao: j.descricao || '', altura: j.altura });
        this.loading = false;
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.loading = false;
        this.erro = mensagemErro(e);
      },
    });
  }
  salvar() {
    if (this.busy) return;
    this.form.markAllAsTouched();
    if (this.form.invalid) return;
    const v = this.form.getRawValue();
    this.busy = true;
    this.erro = '';
    this.sucesso = '';
    this.api
      .salvarPerfil(this.auth.sessao()!.jogadorId, {
        descricao: v.descricao,
        ...(v.cpf ? { cpf: v.cpf.replace(/\D/g, '') } : {}),
        ...(v.cep ? { cep: v.cep.replace(/\D/g, '') } : {}),
        ...(v.altura !== null ? { altura: v.altura } : {}),
      })
      .subscribe({
        next: (j) => {
          this.cdr.markForCheck();
          this.jogador = j;
          this.busy = false;
          this.form.controls.cpf.reset();
          this.sucesso = 'Perfil atualizado!';
        },
        error: (e) => {
          this.cdr.markForCheck();
          this.busy = false;
          this.erro = mensagemErro(e);
        },
      });
  }
}
