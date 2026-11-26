import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Projeto } from '../models/projeto.model';

// Faz as requisições HTTP para o ProjetoVideoController do Spring Boot.
// O proxy (proxy.conf.json) manda /projetos para http://127.0.0.1:8080.
@Injectable({ providedIn: 'root' })
export class ProjetoApiService {
  private readonly http = inject(HttpClient);
  private readonly url = '/projetos';

  // GET: listar todos.
  listar(): Observable<Projeto[]> {
    return this.http.get<Projeto[]>(`${this.url}/buscar`);
  }

  // GET: consultar pelo id.
  buscarPorId(id: number): Observable<Projeto> {
    return this.http.get<Projeto>(`${this.url}/buscar/${id}`);
  }

  // POST: cadastrar.
  cadastrar(dados: Projeto): Observable<Projeto> {
    return this.http.post<Projeto>(`${this.url}/salvar`, dados);
  }

  // PUT: atualizar pelo id.
  atualizar(id: number, dados: Projeto): Observable<Projeto> {
    return this.http.put<Projeto>(`${this.url}/atualizar/${id}`, dados);
  }

  // DELETE: excluir pelo id.
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/deletar/${id}`);
  }
}
