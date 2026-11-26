import { Cliente } from './cliente.model';

// Representa um projeto de vídeo (ProjetoVideoEntity).
// A API devolve o objeto cliente completo e também o idCliente.
export interface Projeto {
  idProjeto?: number;
  idCliente: number | null;
  cliente?: Cliente;
  tituloProjeto: string;
  dataInicio: string;   // "AAAA-MM-DD"
  dataEntrega: string;  // "AAAA-MM-DD"
  orcamento: number | null;
}

// O formulário envia só o idCliente; o Spring busca o cliente pelo id.
export function projetoVazio(): Projeto {
  return { idCliente: null, tituloProjeto: '', dataInicio: '', dataEntrega: '', orcamento: null };
}
