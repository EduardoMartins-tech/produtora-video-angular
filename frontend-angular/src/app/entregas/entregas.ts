import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { Entrega, entregaVazia } from '../models/entrega.model';
import { Projeto } from '../models/projeto.model';
import { EntregaApiService } from '../services/entrega';
import { ProjetoApiService } from '../services/projeto';

@Component({
  selector: 'app-entregas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './entregas.html',
  styleUrl: './entregas.css',
})
export class Entregas implements OnInit {
  private readonly api = inject(EntregaApiService);
  private readonly apiProjetos = inject(ProjetoApiService);
  private readonly plataforma = inject(PLATFORM_ID);
  private readonly cdr = inject(ChangeDetectorRef);

  // Opções usadas nos selects (no banco já existem MP4/MOV e Aprovado/Entregue)
  readonly formatos = ['MP4', 'MOV', 'AVI', 'MKV'];
  readonly status = ['Pendente', 'Em revisão', 'Entregue', 'Aprovado'];

  entregas: Entrega[] = [];
  projetos: Projeto[] = [];
  formulario: Entrega = entregaVazia();
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
      this.apiProjetos.listar().subscribe({
        next: (dados) => {
          this.projetos = dados ?? [];
          this.cdr.markForCheck();
        },
      });
    }
  }

  // Filtro por projeto, status ou formato
  get entregasFiltradas(): Entrega[] {
    const termo = this.filtro.trim().toLowerCase();
    if (!termo) return this.entregas;
    return this.entregas.filter((e) =>
      `${e.idEntrega} ${e.projetoVideo?.tituloProjeto ?? ''} ${e.statusEntrega} ${e.formatoArquivo}`
        .toLowerCase()
        .includes(termo),
    );
  }

  listar(): void {
    this.carregando = true;
    this.erro = '';
    this.api.listar().subscribe({
      next: (dados) => {
        this.entregas = dados ?? [];
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
    this.formulario = entregaVazia();
    this.editandoId = null;
    this.exibindoFormulario = true;
    this.erro = '';
    this.mensagem = '';
  }

  editar(entrega: Entrega): void {
    if (entrega.idEntrega == null) return;
    this.editandoId = entrega.idEntrega;
    this.formulario = {
      idProjeto: entrega.idProjeto ?? entrega.projetoVideo?.idProjeto ?? null,
      dataEntrega: entrega.dataEntrega,
      formatoArquivo: entrega.formatoArquivo,
      statusEntrega: entrega.statusEntrega,
    };
    this.exibindoFormulario = true;
    this.erro = '';
    this.mensagem = '';
  }

  cancelar(): void {
    this.exibindoFormulario = false;
    this.editandoId = null;
    this.formulario = entregaVazia();
  }

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
        this.mensagem = criando ? 'Entrega cadastrada com sucesso.' : 'Entrega atualizada com sucesso.';
        this.listar();
      },
      error: (falha: unknown) => {
        this.salvando = false;
        this.erro = this.descreverErro(falha);
        this.cdr.markForCheck();
      },
    });
  }

  excluir(entrega: Entrega): void {
    if (entrega.idEntrega == null) return;
    if (!confirm(`Deseja realmente excluir a entrega #${entrega.idEntrega}?`)) return;
    this.erro = '';
    this.mensagem = '';
    this.api.excluir(entrega.idEntrega).subscribe({
      next: () => {
        this.mensagem = 'Entrega excluída com sucesso.';
        this.listar();
      },
      error: (falha: unknown) => {
        this.erro = this.descreverErro(falha);
        this.cdr.markForCheck();
      },
    });
  }

  private descreverErro(falha: unknown): string {
    if (!(falha instanceof HttpErrorResponse)) return 'Ocorreu um erro inesperado.';
    if (falha.status === 0) return 'Não foi possível acessar a API Spring Boot (porta 8080).';
    if (falha.status === 400) return 'Dados inválidos. Confira o formulário.';
    if (falha.status === 404) return 'Entrega não encontrada.';
    if (falha.status >= 500) return 'Erro no servidor. Confira o terminal do Spring Boot.';
    return `Erro HTTP ${falha.status}.`;
  }
}
