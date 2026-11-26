package com.consumo.api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ProjetoVideo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoVideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProjeto")
    private Integer idProjeto;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private ClienteEntity cliente;

    @Column(name = "tituloProjeto", length = 150, nullable = false)
    private String tituloProjeto;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "dataInicio", nullable = false)
    private LocalDate dataInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "dataEntrega", nullable = false)
    private LocalDate dataEntrega;

    @Column(name = "orcamento", precision = 10, scale = 2, nullable = false)
    private BigDecimal orcamento = BigDecimal.ZERO;

    public Integer getIdCliente() {
        return cliente != null ? cliente.getIdCliente() : null;
    }

    public void setIdCliente(Integer idCliente) {
        if (idCliente != null) {
            if (this.cliente == null) {
                this.cliente = new ClienteEntity();
            }
            this.cliente.setIdCliente(idCliente);
        }
    }
}
