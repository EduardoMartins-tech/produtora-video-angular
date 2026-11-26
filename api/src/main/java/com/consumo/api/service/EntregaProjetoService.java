package com.consumo.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.consumo.api.entity.EntregaProjetoEntity;
import com.consumo.api.entity.ProjetoVideoEntity;
import com.consumo.api.repository.EntregaProjetoRepository;
import com.consumo.api.repository.ProjetoVideoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregaProjetoService {

    private final EntregaProjetoRepository entregaProjetoRepository;
    private final ProjetoVideoRepository projetoVideoRepository;

    @Transactional
    public EntregaProjetoEntity salvar(EntregaProjetoEntity entrega) {
        if (entrega.getIdProjeto() != null) {
            ProjetoVideoEntity projeto = projetoVideoRepository.findById(entrega.getIdProjeto()).orElse(null);
            if (projeto != null) {
                entrega.setProjetoVideo(projeto);
            }
        }
        return entregaProjetoRepository.save(entrega);
    }

    @Transactional(readOnly = true)
    public List<EntregaProjetoEntity> buscar() {
        return entregaProjetoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EntregaProjetoEntity buscarPorId(Integer id) {
        return entregaProjetoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<EntregaProjetoEntity> buscarPorStatus(String status) {
        return entregaProjetoRepository.findByStatusEntregaIgnoreCaseOrderByDataEntregaDesc(status);
    }

    @Transactional
    public EntregaProjetoEntity atualizar(Integer id, EntregaProjetoEntity novosDados) {
        EntregaProjetoEntity entrega = entregaProjetoRepository.findById(id).orElse(null);
        if (entrega == null) {
            return null;
        }

        if (novosDados.getIdProjeto() != null) {
            ProjetoVideoEntity projeto = projetoVideoRepository.findById(novosDados.getIdProjeto()).orElse(null);
            if (projeto != null) {
                entrega.setProjetoVideo(projeto);
            }
        }

        entrega.setDataEntrega(novosDados.getDataEntrega());
        entrega.setFormatoArquivo(novosDados.getFormatoArquivo());
        entrega.setStatusEntrega(novosDados.getStatusEntrega());

        return entregaProjetoRepository.save(entrega);
    }

    @Transactional
    public void excluir(Integer id) {
        entregaProjetoRepository.deleteById(id);
    }
}
