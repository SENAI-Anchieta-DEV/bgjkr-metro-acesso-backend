package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagPcd, String> {
    Optional<TagPcd> findByCodigoTag(String codigoTag);

    List<TagPcd> findAllByAtivoTrue();

    Optional<TagPcd> findByCodigoTagAndAtivoTrue(String codigoTag);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
        // Bloqueia a Tag selecionada no banco, evitando concorrência
    Optional<TagPcd> findFirstByUsuarioPcdNull();
}