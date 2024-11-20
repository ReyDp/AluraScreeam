package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
@Table(name="episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double calificacion;
    private LocalDate fehcaDeLanzamiento;
    @ManyToOne
    private Series series;

    public Episodio(){}
    public Episodio(Integer temporada, DatosEpisodio d) {
        this.temporada = temporada;
        this.titulo = d.titulo();
        this.numeroEpisodio = d.numeroEpisodio();
        try {
            this.calificacion = Double.valueOf(d.calificacion());
        }catch (NumberFormatException e){
            this.calificacion = 0.0;
        }
        String fechaLanzamientoStr = d.fechaLanzamiento();
        if ("N/A".equals(fechaLanzamientoStr)) {
            this.fehcaDeLanzamiento = null;
        } else {
            try {
                this.fehcaDeLanzamiento = LocalDate.parse(fechaLanzamientoStr);
            } catch (DateTimeParseException e) {
                System.out.println("Error al parsear la fecha: " + fechaLanzamientoStr);
                this.fehcaDeLanzamiento = null;
            }
        }
    }

    public Episodio(List<DatosEpisodio> episodios, DatosEpisodio e) {
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDate getFehcaDeLanzamiento() {
        return fehcaDeLanzamiento;
    }

    public void setFehcaDeLanzamiento(LocalDate fehcaDeLanzamiento) {
        this.fehcaDeLanzamiento = fehcaDeLanzamiento;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", calificacion=" + calificacion +
                ", fehcaDeLanzamiento=" + fehcaDeLanzamiento;
    }
}
