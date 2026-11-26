package com.consumo.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.consumo.api.entity.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    List<ClienteEntity> findByNomeClienteContainingIgnoreCaseOrderByNomeClienteAsc(String nomeCliente);

    List<ClienteEntity> findByCpfCnpjContainingOrderByNomeClienteAsc(String termoCpfCnpj);
}
