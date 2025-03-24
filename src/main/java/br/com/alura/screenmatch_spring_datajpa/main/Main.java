package br.com.alura.screenmatch_spring_datajpa.main;

import br.com.alura.screenmatch_spring_datajpa.model.*;
import br.com.alura.screenmatch_spring_datajpa.repository.SerieRepository;
import br.com.alura.screenmatch_spring_datajpa.service.ApiConsumption;
import br.com.alura.screenmatch_spring_datajpa.service.DataConversion;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final String QUERY = "https://www.omdbapi.com/?";
    private static final String API_KEY = "apikey=dbb76fcb&t=";

    private final ApiConsumption apiConsumption = new ApiConsumption();
    private final DataConversion dataConversion = new DataConversion();
    private final Scanner scanner = new Scanner(System.in);
    private List<SerieInfo> serieInfoList = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> searchedSerie;

    public Main(SerieRepository repository) {
        this.repository = repository;
    }


    public void displayMenu() {
        var option = -1;

        while (option != 0) {
            var menu = """
                    1 - Search for series
                    2 - Search for episodes
                    3 - List searched series
                    4 - Searched series by Title
                    5 - Searched series by Actor
                    6 - Searched Top 5 Series
                    7 - Searched series by Genre
                    8 - By Number of Seasons and Rating 
                    9 - Search episodes by snippet
                    10 - Top 5 Episodes by Serie
                    11 - Search Episodes by Date
                    
                    0 - Exit
                    """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchSeries();
                    break;
                case 2:
                    searchEpisodeBySeries();
                    break;
                case 3:
                    listSearchedSeries();
                    break;
                case 4:
                    searchedSeriesByTitle();
                    break;
                case 5:
                    searchedSeriesByActor();
                    break;
                case 6:
                    searchedTop5Series();
                    break;
                case 7:
                    searchedSeriesByGenre();
                    break;
                case 8:
                    searchedSeriesBySeasonsAndRating();
                    break;
                case 9:
                    searchEpisodeBySnippet();
                    break;
                case 10:
                    topEpisodesbySerie();
                    break;
                case 11:
                    searchEpisodesByDate();
                    break;
                case 0:
                    System.out.println("Application Finished");
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        }
    }


    private SerieInfo getSerieInfo() {
        System.out.println("Wich serie you want to find?");
        var search = scanner.nextLine();
        var query = QUERY + API_KEY + search.replace(" ", "+");
        var json = apiConsumption.getData(query);

        var seriesInfo = dataConversion.getData(json, SerieInfo.class);
        return seriesInfo;
    }

    private void searchSeries() {
        SerieInfo serieInfo = getSerieInfo();
        Serie serie = new Serie(serieInfo);
        //seriesInfoList.add(info);
        repository.save(serie);
        System.out.println(serieInfo);
    }

    private void searchEpisodeBySeries() {
        //SeriesInfo seriesInfo = getSeriesInfo();

        listSearchedSeries();
        System.out.println("Choose a serie:");
        var serieTitle = scanner.nextLine();

//        Optional<Serie> serie = series.stream()
//                .filter(s -> s.getTitle().toLowerCase().contains(serieTitle.toLowerCase()))
//                .findFirst();
//        ------------------------------
//  -----> Now using the JPA Repository:
//        ------------------------------
        Optional<Serie> serie = repository.findByTitleContainingIgnoreCase(serieTitle);

        if(serie.isPresent()) {
            var serieFiltered = serie.get();
            List<SeasonInfo> seasons = new ArrayList<>();

            for (int i = 1; i <= serieFiltered.getTotalSeasons(); i++) {
                var json = apiConsumption.getData(QUERY + API_KEY + serieFiltered.getTitle().replace(" ", "+") + "&Season=" + i);
                SeasonInfo seasonInfo = dataConversion.getData(json, SeasonInfo.class);
                seasons.add(seasonInfo);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(si -> si.episodes().stream()
                            .map(e -> new Episode(si.number(), e)))
                    .collect(Collectors.toList());

            serieFiltered.setEpisodes(episodes);
            repository.save(serieFiltered);

        } else {
            System.out.println("Serie was not found!");
        }
    }

    private void listSearchedSeries() {
        series = repository.findAll();
//  ----> Using Database instead of lists and streams
//        series = seriesInfoList.stream()
//                .map(si -> new Serie(si))
//                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void searchedSeriesByTitle() {
        System.out.println("Choose a serie:");
        var serieTitle = scanner.nextLine();
        searchedSerie = repository.findByTitleContainingIgnoreCase(serieTitle);

        if(searchedSerie.isPresent()) {
            System.out.println("---- Serie Info -----\n" + searchedSerie.get());
        } else {
            System.out.println("Serie not found");
        }
    }

    private void searchedSeriesByActor() {
        System.out.println("Choose an actor:");
        var actor = scanner.nextLine();
        List<Serie> searchedSeries = repository.findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(actor, 8.3);
        System.out.println("Results for " + actor + " :");
        searchedSeries.forEach(s ->
                System.out.println(s.getTitle() + " [rating: " + s.getImdbRating() + "]"));
    }

    private void searchedTop5Series() {
        List<Serie> searchedSeries = repository.findTop5ByOrderByImdbRatingDesc();
        searchedSeries.forEach(s ->
                System.out.println(s.getTitle() + " [rating: " + s.getImdbRating() + "]"));
    }

    private void searchedSeriesByGenre() {
        System.out.println("Search series by genre: ");
        var genre = scanner.nextLine();
        List<Serie> searchedSeries = repository.findByGenre(Category.fromString(genre));
        searchedSeries.forEach(s ->
                System.out.println(s.getTitle() + " [rating: " + s.getImdbRating() + "] "
                + s.getGenre()));

    }

    private void searchedSeriesBySeasonsAndRating() {
        System.out.println("Maximum Number of Seasons: ");
        var numberSeasons = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Rating: ");
        var rating = scanner.nextDouble();
        scanner.nextLine();

        //List<Serie> searchedSeries = repository.findByTotalSeasonsLessThanEqualAndImdbRatingGreaterThanEqual(numberSeasons, rating);
        // ----> NOW USING JPQL:
        List<Serie> searchedSeries = repository.seriesByTotalSeasonsAndRating(numberSeasons, rating);
        searchedSeries.forEach(s ->
                System.out.println(s.getTitle() + " [Rating: " + s.getImdbRating() + "] "
                        + ", [Seasons: " + s.getTotalSeasons() + "]"));
    }

    private void searchEpisodeBySnippet() {
        System.out.println("What is the title of the episode: ");
        var episodeSnippet = scanner.nextLine();

        List<Episode> searchedEpisodes = repository.episodeBySnippet(episodeSnippet);
        searchedEpisodes.forEach(e ->
                System.out.printf("Serie: %s, Season %s, Episode %s - %s\n",
                        e.getSerie().getTitle(), e.getSeason(),
                        e.getNumber(), e.getTitle()));
    }

    private void topEpisodesbySerie() {
        searchedSeriesByTitle();
        if (searchedSerie.isPresent()) {
            Serie serie = searchedSerie.get();
            List<Episode> topEpisodes = repository.topEpisodesbySerie(serie);
            topEpisodes.forEach(e ->
                    System.out.printf("Serie: %s, Season %s, Episode %s - %s, Rating: %s\n",
                            e.getSerie().getTitle(), e.getSeason(),
                            e.getNumber(), e.getTitle(), e.getImdbRating()));

        }
    }

    private void searchEpisodesByDate() {
        searchedSeriesByTitle();
        if (searchedSerie.isPresent()) {
            System.out.println("Search for episodes from wich year?");
            var year = scanner.nextInt();
            scanner.nextLine();

            Serie serie = searchedSerie.get();
            List<Episode> searchedEpisodes = repository.episodesByYearAndSerie(serie, year);
            searchedEpisodes.forEach(System.out::println);
        }
    }

}


