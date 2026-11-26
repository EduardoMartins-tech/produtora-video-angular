import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

// Faz as requisições HTTP para o ClienteController do Spring Boot.
// O proxy (proxy.conf.json) manda /clientes para http://127.0.0.1:8080.
@Injectable({ providedIn: 'root' })
export class ClienteApiService {
  private readonly http = inject(HttpClient);
  private readonly url = '/clientes';

  // GET: listar todos.
  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.url}/buscar`);
  }

  // GET: consultar pelo id.
  buscarPorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.url}/buscar/${id}`);
  }

  // POST: cadastrar.
  cadastrar(dados: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(`${this.url}/salvar`, dados);
  }

  // PUT: atualizar pelo id.
  atualizar(id: number, dados: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.url}/atualizar/${id}`, dados);
  }

  // DELETE: excluir pelo id.
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/deletar/${id}`);
  }
}
