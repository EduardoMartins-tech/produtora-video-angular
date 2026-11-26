package com.consumo.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.consumo.api.entity.ProjetoVideoEntity;

public interface ProjetoVideoRepository extends JpaRepository<ProjetoVideoEntity, Integer> {

    List<ProjetoVideoEntity> findByOrcamentoGreaterThanEqualOrderByDataEntregaAsc(BigDecimal orcamentoMinimo);

    List<ProjetoVideoEntity> findByCliente_IdClienteOrderByDataInicioDesc(Integer idCliente);
}
