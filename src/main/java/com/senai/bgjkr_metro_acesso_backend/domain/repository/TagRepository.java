package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagPcd, String> {
    Optional<TagPcd> findByCodigoTag(String codigoTag);

    Optional<TagPcd> findByIdAndAtivoTrue(String id);

    List<TagPcd> findAllByAtivoTrue();

    Optional<TagPcd> findByCodigoTagAndAtivoTrue(String codigoTag);

    String codigoTag(String codigoTag);
}