import { Projeto } from './projeto.model';

// Representa uma entrega de um projeto (EntregaProjetoEntity).
export interface Entrega {
  idEntrega?: number;
  idProjeto: number | null;
  projetoVideo?: Projeto;
  dataEntrega: string;      // "AAAA-MM-DD"
  formatoArquivo: string;   // MP4, MOV...
  statusEntrega: string;    // Pendente, Em revisão, Entregue...
}

export function entregaVazia(): Entrega {
  return { idProjeto: null, dataEntrega: '', formatoArquivo: '', statusEntrega: 'Pendente' };
}
