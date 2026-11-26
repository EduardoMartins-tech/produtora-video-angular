// Representa um cliente que chega da API Spring Boot (ClienteEntity).
export interface Cliente {
  idCliente?: number;
  nomeCliente: string;
  cpfCnpj: string;
  telefone: string;
  email: string;
}

// Valores iniciais do formulário de cadastro.
export function clienteVazio(): Cliente {
  return { nomeCliente: '', cpfCnpj: '', telefone: '', email: '' };
}
