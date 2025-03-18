package br.com.alura.screenmatch_spring_datajpa.model;

import br.com.alura.screenmatch_spring_datajpa.service.CallChatGPT;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private Integer totalSeasons;
    private Double imdbRating;

    @Enumerated(EnumType.STRING)
    private Category genre;

    private String actors;
    private String poster;
    private String plot;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episode> episodes = new ArrayList<>();

    public Serie(SeriesInfo seriesInfo) {
        this.title = seriesInfo.title();
        this.totalSeasons = seriesInfo.totalSeasons();
        this.imdbRating = OptionalDouble.of(Double.valueOf(seriesInfo.imdbRating())).orElse(0);
        this.genre = Category.fromString(seriesInfo.genre().split(",")[0]);
        this.actors = seriesInfo.actors();
        this.poster = seriesInfo.poster();
        this.plot = CallChatGPT.translateToPortuguese(seriesInfo.plot()).trim();
    }

    public Serie() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        episodes.forEach(e -> e.setSerie(this));
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return
                "genre=" + genre +
                ", title='" + title + "\'" +
                ", totalSeasons=" + totalSeasons +
                ", imdbRating=" + imdbRating +
                ", actors='" + actors + "\'" +
                ", poster='" + poster + "\'" +
                ", plot='" + plot + "\'" + "\n" +
                ", episodes=" + episodes;

    }
}
