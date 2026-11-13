import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Estrutura dos dados recebidos do ViaCEP
export interface EnderecoCep {
  cep: string;
  logradouro: string;
  complemento: string;
  bairro: string;
  localidade: string;
  uf: string;
  erro?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class Cep {

  constructor(private http: HttpClient) {}

  consultarCep(cep: string): Observable<EnderecoCep> {

    const cepLimpo = cep.replace(/\D/g, '');

    return this.http.get<EnderecoCep>(
      `https://viacep.com.br/ws/${cepLimpo}/json/`
    );

  }

}