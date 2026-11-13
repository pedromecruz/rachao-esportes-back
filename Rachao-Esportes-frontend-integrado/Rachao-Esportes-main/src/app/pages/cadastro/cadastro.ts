import { Api, mensagemErro } from '../../services/api';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { Footer } from '../../compartilhado/footer/footer';

function senhasIguaisValidator(control: AbstractControl): ValidationErrors | null {
  const senha = control.get('senha')?.value;
  const confirmarSenha = control.get('confirmarSenha')?.value;

  if (!senha || !confirmarSenha) {
    return null;
  }

  return senha === confirmarSenha ? null : { senhasDiferentes: true };
}

@Component({
  selector: 'app-cadastro',
  imports: [RouterLink, ReactiveFormsModule, Footer],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css',
})
export class Cadastro {
  private cdr = inject(ChangeDetectorRef);
  private api = inject(Api);
  private router = inject(Router);
  busy = false;
  erro = '';
  submitted = false;

  mostrarSenha = false;
  mostrarConfirmacaoSenha = false;

  cadastroForm = new FormGroup(
    {
      dataNascimento: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
      nome: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(3)],
      }),

      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),

      senha: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(8)],
      }),

      confirmarSenha: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
    },
    {
      validators: senhasIguaisValidator,
    },
  );

  onSubmit(): void {
    if (this.busy) return;
    this.submitted = true;

    this.cadastroForm.markAllAsTouched();

    if (this.cadastroForm.invalid) {
      return;
    }
    const v = this.cadastroForm.getRawValue();
    const hoje = new Date();
    const nascimento = new Date(v.dataNascimento + 'T12:00:00');
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    if (
      hoje.getMonth() < nascimento.getMonth() ||
      (hoje.getMonth() === nascimento.getMonth() && hoje.getDate() < nascimento.getDate())
    )
      idade--;
    if (!Number.isFinite(nascimento.getTime()) || idade < 18 || nascimento > hoje) {
      this.erro = 'É necessário ter pelo menos 18 anos.';
      return;
    }
    this.busy = true;
    this.erro = '';
    this.api
      .cadastrar({
        nome: v.nome.trim(),
        email: v.email.trim(),
        senha: v.senha,
        dataNascimento: v.dataNascimento,
      })
      .subscribe({
        next: () => {
          this.cdr.markForCheck();
          void this.router.navigate(['/login'], { queryParams: { cadastrado: '1' } });
        },
        error: (e) => {
          this.cdr.markForCheck();
          this.busy = false;
          this.erro = mensagemErro(e);
        },
      });
  }
}
