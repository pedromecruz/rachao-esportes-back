import { Auth, mensagemErro } from '../../services/api';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private cdr = inject(ChangeDetectorRef);

  private auth = inject(Auth);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  cadastrado = this.route.snapshot.queryParamMap.has('cadastrado');
  busy = false;
  erro = '';
  submitted = false;

  mostrarSenha = false;

  loginForm = new FormGroup({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),

    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  onSubmit(): void {
    if (this.busy) return;
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    this.busy = true;
    this.erro = '';
    const v = this.loginForm.getRawValue();
    this.auth.login(v.email, v.password).subscribe({
      next: () => {
        this.cdr.markForCheck();
        const r = this.route.snapshot.queryParamMap.get('retorno');
        void this.router.navigateByUrl(
          r && /^\/(?!\/)/.test(r) && !r.startsWith('/login') ? r : '/partidas',
        );
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.busy = false;
        this.erro = mensagemErro(e);
      },
    });
  }
}
