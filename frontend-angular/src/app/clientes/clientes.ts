import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { Cliente, clienteVazio } from '../models/cliente.model';
import { ClienteApiService } from '../services/cliente';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes implements OnInit {
  private readonly api = inject(ClienteApiService);
  private readonly plataforma = inject(PLATFORM_ID);
  private readonly cdr = inject(ChangeDetectorRef);

  clientes: Cliente[] = [];
  formulario: Cliente = clienteVazio();
  editandoId: number | null = null;
  exibindoFormulario = false;
  carregando = false;
  salvando = false;
  mensagem = '';
  erro = '';
  filtro = '';

  ngOnInit(): void {
    // No SSR não chama a API: só no navegador
    if (isPlatformBrowser(this.plataforma)) {
      this.listar();
    }
  }

  // Filtro por id, nome, CPF/CNPJ ou e-mail
  get clientesFiltrados(): Cliente[] {
    const termo = this.filtro.trim().toLowerCase();
    if (!termo) return this.clientes;
    return this.clientes.filter((c) =>
      `${c.idCliente} ${c.nomeCliente} ${c.cpfCnpj} ${c.email}`.toLowerCase().includes(termo),
    );
  }

  // GET
  listar(): void {
    this.carregando = true;
    this.erro = '';
    this.api.listar().subscribe({
      next: (dados) => {
        this.clientes = dados ?? [];
        this.carregando = false;
        this.cdr.markForCheck();
      },
      error: (falha: unknown) => {
        this.carregando = false;
        this.erro = this.descreverErro(falha);
        this.cdr.markForCheck();
      },
    });
  }

  novo(): void {
    this.formulario = clienteVazio();
    this.editandoId = null;
    this.exibindoFormulario = true;
    this.erro = '';
    this.mensagem = '';
  }

  editar(cliente: Cliente): void {
    if (cliente.idCliente == null) return;
    this.editandoId = cliente.idCliente;
    this.formulario = { ...cliente };
    this.exibindoFormulario = true;
    this.erro = '';
    this.mensagem = '';
  }

  cancelar(): void {
    this.exibindoFormulario = false;
    this.editandoId = null;
    this.formulario = clienteVazio();
  }

  // POST (novo) ou PUT (edição)
  salvar(form: NgForm): void {
    if (form.invalid || this.salvando) {
      form.control.markAllAsTouched();
      return;
    }
    this.salvando = true;
    this.erro = '';
    const criando = this.editandoId === null;
    const operacao = criando
      ? this.api.cadastrar(this.formulario)
      : this.api.atualizar(this.editandoId!, this.formulario);

    operacao.subscribe({
      next: () => {
        this.salvando = false;
        this.cancelar();
        this.mensagem = criando ? 'Cliente cadastrado com sucesso.' : 'Cliente atualizado com sucesso.';
        this.listar();
      },
      error: (falha: unknown) => {
        this.salvando = false;
        this.erro = this.descreverErro(falha);
        this.cdr.markForCheck();
      },
    });
  }

  // DELETE
  excluir(cliente: Cliente): void {
    if (cliente.idCliente == null) return;
    if (!confirm(`Deseja realmente excluir o cliente ${cliente.nomeCliente}?`)) return;
    this.erro = '';
    this.mensagem = '';
    this.api.excluir(cliente.idCliente).subscribe({
      next: () => {
        this.mensagem = 'Cliente excluído com sucesso.';
        this.listar();
      },
      error: (falha: unknown) => {
        this.erro = this.descreverErro(falha, true);
        this.cdr.markForCheck();
      },
    });
  }

  private descreverErro(falha: unknown, excluindo = false): string {
    if (!(falha instanceof HttpErrorResponse)) return 'Ocorreu um erro inesperado.';
    if (falha.status === 0) return 'Não foi possível acessar a API Spring Boot (porta 8080).';
    if (falha.status === 400) return 'Dados inválidos. Confira o formulário.';
    if (falha.status === 404) return 'Cliente não encontrado.';
    if (excluindo) return 'Não foi possível excluir: o cliente tem projetos cadastrados.';
    if (falha.status >= 500) return 'Não foi possível salvar. Confira se o CPF/CNPJ ou o e-mail já estão cadastrados.';
    return `Erro HTTP ${falha.status}.`;
  }
}
