package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;


import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "Series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(unique = true, nullable = false)
    private String titulo;
    @Column(nullable = false)
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String actores;
    private String sinopsis;
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodioList;

    public Series(){}

    public Series(DatosSeries datosSeries){
        this.titulo = datosSeries.titulo();
        this.totalTemporadas = datosSeries.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSeries.evaluacion())).orElse(0);
        this.poster = datosSeries.poster();
        this.genero = Categoria.fromString(datosSeries.genero().split(",")[0].trim());
        this.actores = datosSeries.actores();
        this.sinopsis = datosSeries.sinopsis();
    }

    @Override
    public String toString() {
        return "Series{" +
                "Id=" + Id +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", genero=" + genero +
                ", actores='" + actores + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", episodio=" + episodioList +
                '}';
    }

    public List<Episodio> getEpisodios() {
        return episodioList;
    }

    public void setEpisodios(List<Episodio> episodioList) {
        episodioList.forEach(e->e.setSeries(this));
        this.episodioList = episodioList;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

}
