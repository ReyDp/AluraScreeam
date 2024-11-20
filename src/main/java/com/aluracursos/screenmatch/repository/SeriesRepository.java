package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series,Long> {

    Optional<Series> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Series> findTop5ByOrderByEvaluacionDesc();

    List<Series> findByGenero(Categoria categoria);

    @Query("SELECT s FROM Series s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Series> seriesPorTemporadaYEvaluaciones(int totalTemporadas, Double evaluacion);

    @Query("SELECT e FROM Series s JOIN s.episodio e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> EspisodiosPorNormbre (String nombreEpisodio);

    @Query("SELECT e FROM Series s JOIN s.episodio e WHERE s = :series ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios (Series series);

}