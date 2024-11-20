package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosEpisodio(
        @JsonAlias ("Title") String titulo,

        @JsonAlias ("Episode") Integer numeroEspisodio,

        @JsonAlias ("imdbRating") String calificacion,

        @JsonAlias ("Released") String fechaLanzamiento
) {
}
