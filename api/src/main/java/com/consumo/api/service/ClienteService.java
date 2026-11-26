package com.consumo.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.consumo.api.entity.ClienteEntity;
import com.consumo.api.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteEntity salvar(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteEntity> buscar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClienteEntity buscarPorId(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ClienteEntity> buscarPorNome(String nome) {
        return clienteRepository.findByNomeClienteContainingIgnoreCaseOrderByNomeClienteAsc(nome);
    }

    @Transactional
    public ClienteEntity atualizar(Integer id, ClienteEntity novosDados) {
        ClienteEntity cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return null;
        }

        cliente.setNomeCliente(novosDados.getNomeCliente());
        cliente.setCpfCnpj(novosDados.getCpfCnpj());
        cliente.setTelefone(novosDados.getTelefone());
        cliente.setEmail(novosDados.getEmail());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(Integer id) {
        clienteRepository.deleteById(id);
    }
}
