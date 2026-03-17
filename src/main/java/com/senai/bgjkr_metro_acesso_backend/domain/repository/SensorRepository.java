package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
    Optional<Sensor> findByCodigoSensor(String codigoSensor);

    List<Sensor> findAllByEstacao(Estacao estacao);

    Optional<Sensor> findByCodigoSensorAndAtivoTrue(String codigoSensor);

    List<Sensor> findAllByAtivoTrue();
}
