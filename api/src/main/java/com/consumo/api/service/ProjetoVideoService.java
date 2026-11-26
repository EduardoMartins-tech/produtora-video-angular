package com.consumo.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.consumo.api.entity.ClienteEntity;
import com.consumo.api.entity.ProjetoVideoEntity;
import com.consumo.api.repository.ClienteRepository;
import com.consumo.api.repository.ProjetoVideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjetoVideoService {

    private final ProjetoVideoRepository projetoVideoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public ProjetoVideoEntity salvar(ProjetoVideoEntity projeto) {
        if (projeto.getIdCliente() != null) {
            ClienteEntity cliente = clienteRepository.findById(projeto.getIdCliente()).orElse(null);
            if (cliente != null) {
                projeto.setCliente(cliente);
            }
        }
        return projetoVideoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public List<ProjetoVideoEntity> buscar() {
        return projetoVideoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProjetoVideoEntity buscarPorId(Integer id) {
        return projetoVideoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ProjetoVideoEntity> buscarPorOrcamentoMinimo(BigDecimal orcamentoMinimo) {
        return projetoVideoRepository.findByOrcamentoGreaterThanEqualOrderByDataEntregaAsc(orcamentoMinimo);
    }

    @Transactional
    public ProjetoVideoEntity atualizar(Integer id, ProjetoVideoEntity novosDados) {
        ProjetoVideoEntity projeto = projetoVideoRepository.findById(id).orElse(null);
        if (projeto == null) {
            return null;
        }

        if (novosDados.getIdCliente() != null) {
            ClienteEntity cliente = clienteRepository.findById(novosDados.getIdCliente()).orElse(null);
            if (cliente != null) {
                projeto.setCliente(cliente);
            }
        }

        projeto.setTituloProjeto(novosDados.getTituloProjeto());
        projeto.setDataInicio(novosDados.getDataInicio());
        projeto.setDataEntrega(novosDados.getDataEntrega());
        if (novosDados.getOrcamento() != null) {
            projeto.setOrcamento(novosDados.getOrcamento());
        }

        return projetoVideoRepository.save(projeto);
    }

    @Transactional
    public void excluir(Integer id) {
        projetoVideoRepository.deleteById(id);
    }
}
