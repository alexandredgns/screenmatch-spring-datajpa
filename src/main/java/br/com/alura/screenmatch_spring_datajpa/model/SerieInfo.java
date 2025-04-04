package br.com.alura.screenmatch_spring_datajpa.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieInfo(@JsonAlias("Title") String title,
                        @JsonAlias("totalSeasons") Integer totalSeasons,
                        @JsonAlias("imdbRating") String imdbRating,
                        @JsonAlias("Genre") String genre,
                        @JsonAlias("Actors") String actors,
                        @JsonAlias("Poster") String poster,
                        @JsonAlias("Plot") String plot) {
}

