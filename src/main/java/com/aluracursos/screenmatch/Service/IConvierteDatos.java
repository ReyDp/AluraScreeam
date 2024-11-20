package com.aluracursos.screenmatch.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IConvierteDatos {
    <T> T obtnerDatos(String json, Class <T> clase);
}
