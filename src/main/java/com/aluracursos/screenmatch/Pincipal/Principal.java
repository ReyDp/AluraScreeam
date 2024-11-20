package com.aluracursos.screenmatch.Pincipal;

import com.aluracursos.screenmatch.Service.ConsumoAPI;
import com.aluracursos.screenmatch.Service.ConvertirDatos;
import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SeriesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY= "&apikey=a4ec26f1";
    private List<DatosSeries> datosSeries = new ArrayList<>();
    ConvertirDatos conversor = new ConvertirDatos();
    private  SeriesRepository repositorio;
    private List<Series> series;
    private  Optional<Series> seriesBuscadas;

    public Principal(SeriesRepository repository) {
        this.repositorio = repository;
    }

    public void mostrarMenu() throws JsonProcessingException {
        System.out.println("**************Screen Match**************");

        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodio
                    3 - Mostrar series buscadas
                    4 - buscar series por titulo
                    5 - Top 5 series
                    6 - Categoria
                    7 - Series por temporada y evaluaciones
                    8 - Buscar episodio
                    9 - Top 5 episodios
                    0 - Salir
                    """;
            System.out.println(menu);
            System.out.print("Escoja una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buseriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriePorCategoria();
                    break;
                case 7:
                    mostrarSeriesPorTemporadasEvaluaciones();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5EpisodiosPorSerie();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }

    private DatosSeries getDatosSeries(){
        System.out.print("Buscar serie: ");
        var serie = sc.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + serie.replace(" ", "+") + API_KEY);
        DatosSeries datos = conversor.obtnerDatos(json, DatosSeries.class);
        System.out.println(datos.sinopsis());
        return datos;
    }


    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.print("Serie de la cual quieres ver los episodios: ");
        var nombreSerie = sc.nextLine();

        Optional<Series> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                DatosTemporadas datosTemporadas = conversor.obtnerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporadas);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d-> d.episodios().stream()
                            .map(e -> new Episodio(d.episodios(),e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }

    private void buscarSerie(){
        DatosSeries datos = getDatosSeries();
        Series series = new Series(datos);
        repositorio.save(series);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Series::getGenero))
                .forEach(System.out::println);
    }

    private void buseriesPorTitulo() {
        System.out.println("Titulo de la serie: ");
        var nombreSerie = sc.nextLine();

        seriesBuscadas = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (seriesBuscadas.isPresent()){
            System.out.println("Serie buscada: "+seriesBuscadas.get());
        }else {
            System.out.println("La serie no fue encontrada");
        }
    }

    private void buscarTop5Series(){
        List<Series> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();

        topSeries.forEach(s -> System.out.println("Serie: "+s.getTitulo()+ " Calificación: "+s.getEvaluacion()));
    }

    private void buscarSeriePorCategoria(){
        System.out.println("Digite el genero de la serie: ");
        var genero = sc.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Series> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria: "+genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void mostrarSeriesPorTemporadasEvaluaciones(){
        System.out.print("Digite la temporada: ");
        var totalTemporada = sc.nextInt();
        System.out.println("Digite la evaluacion: ");
        var evaluacion = sc.nextDouble();
        List<Series> filtroSeries = repositorio.seriesPorTemporadaYEvaluaciones(totalTemporada,evaluacion);
        System.out.println("**** Series filtradas ****");
        filtroSeries.forEach(s-> System.out.println(s.getTitulo()+ " evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo(){
        System.out.println("Digite el nombre del episodio: ");
        var nombreEpisodio = sc.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.EspisodiosPorNormbre(nombreEpisodio);
        episodiosEncontrados.forEach(e-> System.out.printf("Seire: %s Episodio %s Temporada %s Evaluación %s",
                e.getSeries(), e.getNumeroEpisodio(), e.getTemporada(), e.getCalificacion()));
    }

    private void buscarTop5EpisodiosPorSerie(){
        buseriesPorTitulo();
        if (seriesBuscadas.isPresent()){
            var serie = seriesBuscadas.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e-> System.out.printf("Seire: %s Episodio %s Temporada %s Evaluación %s",
                    e.getSeries(), e.getNumeroEpisodio(), e.getTemporada(), e.getCalificacion()));

        }
    }

}
