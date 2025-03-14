package br.com.alura.screenmatch_spring_datajpa.model;

import br.com.alura.screenmatch_spring_datajpa.service.CallChatGPT;

import java.util.OptionalDouble;

public class Serie {
    private String title;
    private Integer totalSeasons;
    private Double imdbRating;
    private Category genre;
    private String actors;
    private String poster;
    private String plot;

    public Serie(SeriesInfo seriesInfo) {
        this.title = seriesInfo.title();
        this.totalSeasons = seriesInfo.totalSeasons();
        this.imdbRating = OptionalDouble.of(Double.valueOf(seriesInfo.imdbRating())).orElse(0);
        this.genre = Category.fromString(seriesInfo.genre().split(",")[0]);
        this.actors = seriesInfo.actors();
        this.poster = seriesInfo.poster();
        this.plot = CallChatGPT.translateToPortuguese(seriesInfo.plot()).trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return
                "genre=" + genre + "\n" +
                ", title='" + title + "\n" +
                ", totalSeasons=" + totalSeasons + "\n" +
                ", imdbRating=" + imdbRating + "\n" +
                ", actors='" + actors + "\n" +
                ", poster='" + poster + "\n" +
                ", plot='" + plot;

    }
}
