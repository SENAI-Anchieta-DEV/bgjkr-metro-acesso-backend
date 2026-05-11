package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgenteRepository extends JpaRepository<AgenteAtendimento, String> {

    Optional<AgenteAtendimento> findByEmailAndAtivoTrue(String email);

    Optional<AgenteAtendimento> findByEmail(String email);

    List<AgenteAtendimento> findAllByAtivoTrue();

    @Query("SELECT a FROM AgenteAtendimento a WHERE :horario BETWEEN a.inicioTurno AND a.fimTurno")
    List<AgenteAtendimento> findByEstacaoAndHorarioNoTurno(@Param("estacao") Estacao estacao, @Param("horario") LocalTime horario);
}