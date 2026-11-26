package com.consumo.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.consumo.api.entity.EntregaProjetoEntity;

public interface EntregaProjetoRepository extends JpaRepository<EntregaProjetoEntity, Integer> {

    List<EntregaProjetoEntity> findByStatusEntregaIgnoreCaseOrderByDataEntregaDesc(String statusEntrega);

    List<EntregaProjetoEntity> findByProjetoVideo_IdProjeto(Integer idProjeto);
}
