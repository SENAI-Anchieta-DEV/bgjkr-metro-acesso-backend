package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.PendenciaAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendenciaRepository extends JpaRepository<PendenciaAtendimento, String> {
    List<PendenciaAtendimento> findAllByAtivoTrue();

    List<PendenciaAtendimento> findAllByAgenteAndAtivoTrue(AgenteAtendimento agente);

    List<PendenciaAtendimento> findAllByEstacaoAndAtivoTrue(Estacao estacao);
}
