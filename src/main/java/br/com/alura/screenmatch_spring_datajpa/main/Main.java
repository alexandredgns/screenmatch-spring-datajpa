package br.com.alura.screenmatch_spring_datajpa.main;

import br.com.alura.screenmatch_spring_datajpa.model.Episode;
import br.com.alura.screenmatch_spring_datajpa.model.SeasonInfo;
import br.com.alura.screenmatch_spring_datajpa.model.Serie;
import br.com.alura.screenmatch_spring_datajpa.model.SeriesInfo;
import br.com.alura.screenmatch_spring_datajpa.repository.SerieRepository;
import br.com.alura.screenmatch_spring_datajpa.service.ApiConsumption;
import br.com.alura.screenmatch_spring_datajpa.service.DataConversion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final String QUERY = "https://www.omdbapi.com/?";
    private static final String API_KEY = "apikey=dbb76fcb&t=";

    private final ApiConsumption apiConsumption = new ApiConsumption();
    private final DataConversion dataConversion = new DataConversion();
    private final Scanner scanner = new Scanner(System.in);
    private List<SeriesInfo> seriesInfoList = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

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
                case 0:
                    System.out.println("Application Finished");
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        }
    }


    private SeriesInfo getSeriesInfo() {
        System.out.println("Wich serie you want to find?");
        var search = scanner.nextLine();
        var query = QUERY + API_KEY + search.replace(" ", "+");
        var json = apiConsumption.getData(query);

        var seriesInfo = dataConversion.getData(json, SeriesInfo.class);
        return seriesInfo;
    }

    private void searchSeries() {
        SeriesInfo info = getSeriesInfo();
        Serie serie = new Serie(info);
        //seriesInfoList.add(info);
        repository.save(serie);
        System.out.println(info);
    }

    private void searchEpisodeBySeries() {
        //SeriesInfo seriesInfo = getSeriesInfo();

        listSearchedSeries();
        System.out.println("Choose a serie:");
        var serieTitle = scanner.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(serieTitle.toLowerCase()))
                .findFirst();

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

//        series = seriesInfoList.stream()
//                .map(s -> new Serie(s))
//                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }


}


