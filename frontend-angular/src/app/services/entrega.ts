import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Entrega } from '../models/entrega.model';

// Faz as requisições HTTP para o EntregaProjetoController do Spring Boot.
// O proxy (proxy.conf.json) manda /entregas para http://127.0.0.1:8080.
@Injectable({ providedIn: 'root' })
export class EntregaApiService {
  private readonly http = inject(HttpClient);
  private readonly url = '/entregas';

  // GET: listar todos.
  listar(): Observable<Entrega[]> {
    return this.http.get<Entrega[]>(`${this.url}/buscar`);
  }

  // GET: consultar pelo id.
  buscarPorId(id: number): Observable<Entrega> {
    return this.http.get<Entrega>(`${this.url}/buscar/${id}`);
  }

  // POST: cadastrar.
  cadastrar(dados: Entrega): Observable<Entrega> {
    return this.http.post<Entrega>(`${this.url}/salvar`, dados);
  }

  // PUT: atualizar pelo id.
  atualizar(id: number, dados: Entrega): Observable<Entrega> {
    return this.http.put<Entrega>(`${this.url}/atualizar/${id}`, dados);
  }

  // DELETE: excluir pelo id.
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/deletar/${id}`);
  }
}
