package com.devtrack.devtrack_backend.repository;

import com.devtrack.devtrack_backend.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    Optional<Label> findByNameIgnoreCase(String name);
}
