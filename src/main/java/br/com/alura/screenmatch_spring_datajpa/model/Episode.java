package br.com.alura.screenmatch_spring_datajpa.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;
    private String title;
    private Integer number;
    private Double imdbRating;
    private LocalDate released;

    @ManyToOne
    private Serie serie;

    public Episode() {}

    public Episode(Integer season, EpisodeInfo e) {
        this.season = season;
        this.title = e.title();
        this.number = e.number();

        try {
            this.imdbRating = Double.valueOf(e.imdbRating());
        } catch (NumberFormatException ex) {
            this.imdbRating = 0.0;
        }

        try {
            this.released = LocalDate.parse(e.released());
        } catch (DateTimeParseException ex) {
            this.released = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    @Override
    public String toString() {
        return
                "season=" + season +
                ", title=" + title +
                ", number=" + number +
                ", imdbRating=" + imdbRating +
                ", released=" + released;
    }
}
