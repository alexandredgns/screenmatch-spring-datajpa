package br.com.alura.screenmatch_spring_datajpa.repository;

import br.com.alura.screenmatch_spring_datajpa.model.Category;
import br.com.alura.screenmatch_spring_datajpa.model.Episode;
import br.com.alura.screenmatch_spring_datajpa.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String title);

    List<Serie> findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(String actor, Double rating);

    List<Serie> findTop5ByOrderByImdbRatingDesc();

    List<Serie> findByGenre(Category genre);

    List<Serie> findByTotalSeasonsLessThanEqualAndImdbRatingGreaterThanEqual(Integer numberSeasons, Double rating);

    @Query("SELECT s FROM Serie s WHERE s.totalSeasons <= :numberSeasons AND s.imdbRating >= :rating")
    List<Serie> seriesByTotalSeasonsAndRating(int numberSeasons, double rating);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE e.title ILIKE %:episodeSnippet%")
    List<Episode> episodeBySnippet(String episodeSnippet);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie ORDER BY e.imdbRating DESC LIMIT 5")
    List<Episode> topEpisodesbySerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie AND YEAR(e.released) >= :year")
    List<Episode> episodesByYearAndSerie(Serie serie, int year);
}


