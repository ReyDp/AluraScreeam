package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series,Long> {
    Optional<Series> findByTituloContainsIgnoreCase(String nombreSerie);

}
