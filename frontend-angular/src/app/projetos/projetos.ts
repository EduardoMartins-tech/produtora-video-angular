import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { Cliente } from '../models/cliente.model';
import { Projeto, projetoVazio } from '../models/projeto.model';
import { ClienteApiService } from '../services/cliente';
import { ProjetoApiService } from '../services/projeto';

@Component({
  selector: 'app-projetos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './projetos.html',
  styleUrl: './projetos.css',
})
export class Projetos implements OnInit {
  private readonly api = inject(ProjetoApiService);
  private readonly apiClientes = inject(ClienteApiService);
  private readonly plataforma = inject(PLATFORM_ID);
  private readonly cdr = inject(ChangeDetectorRef);

  projetos: Projeto[] = [];
  clientes: Cliente[] = []; // para o <select> do formulário
  formulario: Projeto = projetoVazio();
  editandoId: number | null = null;
  exibindoFormulario = false;
  carregando = false;
  salvando = false;
  mensagem = '';
  erro = '';
  filtro = '';

  ngOnInit(): void {
    if (isPlatformBrowser(this.plataforma)) {
      this.listar();
      this.apiClientes.listar().subscribe({
        next: (dados) => {
          this.clientes = dados ?? [];
          this.cdr.markForCheck();
        },
      });
    }
  }

  // Filtro por id, título ou nome do cliente
  get projetosFiltrados(): Projeto[] {
    const termo = this.filtro.trim().toLowerCase();
    if (!termo) return this.projetos;
    return this.projetos.filter((p) =>
      `${p.idProjeto} ${p.tituloProjeto} ${p.cliente?.nomeCliente ?? ''}`.toLowerCase().includes(termo),
    );
  }

  listar(): void {
    this.carregando = true;
    this.erro = '';
    this.api.listar().subscribe({
      next: (dados) => {
        this.projetos = dados ?? [];
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
    this.formulario = projetoVazio();
    this.editandoId = null;
    this.exibindoFormulario = true;
    this.erro = '';
    this.mensagem = '';
  }

  editar(projeto: Projeto): void {
    if (projeto.idProjeto == null) return;
    this.editandoId = projeto.idProjeto;
    // Só os campos que a API recebe (o cliente vai pelo idCliente)
    this.formulario = {
      idCliente: projeto.idCliente ?? projeto.cliente?.idCliente ?? null,
      tituloProjeto: projeto.tituloProjeto,
      dataInicio: projeto.dataInicio,
      dataEntrega: projeto.dataEntrega,
      orcamento: projeto.orcamento,
    };
    this.exibindoFormulario = true;
    this.erro = '';
    this.mensagem = '';
  }

  cancelar(): void {
    this.exibindoFormulario = false;
    this.editandoId = null;
    this.formulario = projetoVazio();
  }

  salvar(form: NgForm): void {
    if (form.invalid || this.salvando) {
      form.control.markAllAsTouched();
      return;
    }
    if (this.formulario.dataEntrega < this.formulario.dataInicio) {
      this.erro = 'A data de entrega não pode ser antes da data de início.';
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
        this.mensagem = criando ? 'Projeto cadastrado com sucesso.' : 'Projeto atualizado com sucesso.';
        this.listar();
      },
      error: (falha: unknown) => {
        this.salvando = false;
        this.erro = this.descreverErro(falha);
        this.cdr.markForCheck();
      },
    });
  }

  excluir(projeto: Projeto): void {
    if (projeto.idProjeto == null) return;
    if (!confirm(`Deseja realmente excluir o projeto "${projeto.tituloProjeto}"?`)) return;
    this.erro = '';
    this.mensagem = '';
    this.api.excluir(projeto.idProjeto).subscribe({
      next: () => {
        this.mensagem = 'Projeto excluído com sucesso.';
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
    if (falha.status === 404) return 'Projeto não encontrado.';
    if (excluindo) return 'Não foi possível excluir: o projeto tem entregas cadastradas.';
    if (falha.status >= 500) return 'Não foi possível salvar. Confira o cliente e o orçamento (máximo 99.999.999,99).';
    return `Erro HTTP ${falha.status}.`;
  }
}
