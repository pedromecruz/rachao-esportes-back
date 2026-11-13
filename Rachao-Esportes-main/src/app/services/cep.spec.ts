import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Cep } from './cep';

describe('Cep', () => {
  let service: Cep;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],});
    service = TestBed.inject(Cep);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

