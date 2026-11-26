package com.consumo.api.entity;

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
@Table(name = "EntregaProjeto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntregaProjetoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEntrega")
    private Integer idEntrega;

    @ManyToOne
    @JoinColumn(name = "idProjeto", nullable = false)
    private ProjetoVideoEntity projetoVideo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "dataEntrega", nullable = false)
    private LocalDate dataEntrega;

    @Column(name = "formatoArquivo", length = 20, nullable = false)
    private String formatoArquivo;

    @Column(name = "statusEntrega", length = 30, nullable = false)
    private String statusEntrega;

    public Integer getIdProjeto() {
        return projetoVideo != null ? projetoVideo.getIdProjeto() : null;
    }

    public void setIdProjeto(Integer idProjeto) {
        if (idProjeto != null) {
            if (this.projetoVideo == null) {
                this.projetoVideo = new ProjetoVideoEntity();
            }
            this.projetoVideo.setIdProjeto(idProjeto);
        }
    }
}
