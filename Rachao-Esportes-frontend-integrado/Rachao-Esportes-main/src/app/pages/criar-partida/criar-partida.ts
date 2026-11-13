import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  Output,
  inject,
  OnChanges,
  OnInit,
} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Api, Esporte, Partida, mensagemErro } from '../../services/api';
@Component({
  selector: 'app-criar-partida',
  imports: [ReactiveFormsModule],
  templateUrl: './criar-partida.html',
  styleUrl: './criar-partida.css',
})
export class CriarPartida implements OnInit, OnChanges {
  private cdr = inject(ChangeDetectorRef);
  private api = inject(Api);
  @Input() aberto = false;
  @Input() partida: Partida | null = null;
  @Output() fechar = new EventEmitter<void>();
  @Output() salvo = new EventEmitter<void>();
  esportes: Esporte[] = [];
  busy = false;
  erro = '';
  partidaForm = new FormGroup({
    esporteId: new FormControl(0, { nonNullable: true, validators: [Validators.min(1)] }),
    dataHora: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    qtdTimes: new FormControl(2, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.min(2),
        Validators.max(20),
        Validators.pattern(/^\d+$/),
      ],
    }),
    qtdJogadoresTime: new FormControl(5, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.min(1),
        Validators.max(50),
        Validators.pattern(/^\d+$/),
      ],
    }),
    local: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/\S/)],
    }),
  });
  ngOnInit() {
    this.carregarEsportes();
  }
  carregarEsportes() {
    this.api.esportes().subscribe({
      next: (e) => {
        this.esportes = e;
        this.cdr.markForCheck();
      },
      error: (e) => {
        this.erro = mensagemErro(e);
        this.cdr.markForCheck();
      },
    });
  }
  ngOnChanges() {
    this.erro = '';
    if (this.partida)
      this.partidaForm.reset({ ...this.partida, dataHora: this.partida.dataHora.slice(0, 16) });
    else
      this.partidaForm.reset({
        esporteId: 0,
        dataHora: '',
        qtdTimes: 2,
        qtdJogadoresTime: 5,
        local: '',
      });
  }
  fecharModal() {
    if (!this.busy) this.fechar.emit();
  }
  get totalParticipantes() {
    const v = this.partidaForm.getRawValue();
    return v.qtdTimes * v.qtdJogadoresTime;
  }
  onSubmit() {
    if (this.busy) return;
    this.partidaForm.markAllAsTouched();
    this.erro = '';
    if (this.partidaForm.invalid) {
      this.erro = 'Revise os campos. Use de 2 a 20 times e de 1 a 50 jogadores por time.';
      return;
    }
    const v = this.partidaForm.getRawValue();
    if (new Date(v.dataHora + ':00-03:00').getTime() <= Date.now()) {
      this.erro = 'Informe uma data futura no horário de São Paulo.';
      return;
    }
    const dados = { ...v, local: v.local.trim(), dataHora: v.dataHora + ':00' };
    this.busy = true;
    (this.partida ? this.api.editar(this.partida.id, dados) : this.api.criar(dados)).subscribe({
      next: () => {
        this.cdr.markForCheck();
        this.busy = false;
        this.salvo.emit();
        this.fechar.emit();
      },
      error: (e) => {
        this.cdr.markForCheck();
        this.busy = false;
        this.erro = mensagemErro(e);
      },
    });
  }
}
