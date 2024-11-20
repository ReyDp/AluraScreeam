package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series,Long> {

    Optional<Series> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Series> findTop5ByOrderByEvaluacionDesc();

    List<Series> findByGenero(Categoria categoria);

}
