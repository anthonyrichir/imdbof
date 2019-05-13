package com.imdbof.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "original_title", nullable = false)
    private String originalTitle;

    @NotNull
    @Column(name = "vote_count", nullable = false)
    private Integer voteCount;

    @NotNull
    @Column(name = "vote_average", nullable = false)
    private Float voteAverage;

    @NotNull
    @Column(name = "video", nullable = false)
    private Boolean video;

    @NotNull
    @Column(name = "popularity", nullable = false)
    private Float popularity;

    @NotNull
    @Column(name = "poster_path", nullable = false)
    private String posterPath;

    @NotNull
    @Column(name = "original_language", nullable = false)
    private String originalLanguage;

    @NotNull
    @Column(name = "backdrop_path", nullable = false)
    private String backdropPath;

    @NotNull
    @Column(name = "adult", nullable = false)
    private Boolean adult;

    @NotNull
    @Column(name = "release_date", nullable = false)
    private String releaseDate;

    
    @Lob
    @Column(name = "overview", nullable = false)
    private String overview;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_category",
               joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Movie originalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Movie voteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Movie addVote() {
        this.voteCount++;
        return this;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public Movie voteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Boolean isVideo() {
        return video;
    }

    public Movie video(Boolean video) {
        this.video = video;
        return this;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Float getPopularity() {
        return popularity;
    }

    public Movie popularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie posterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public Movie originalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Movie backdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean isAdult() {
        return adult;
    }

    public Movie adult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie releaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public Movie overview(String overview) {
        this.overview = overview;
        return this;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Movie categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Movie addCategory(Category category) {
        this.categories.add(category);
        category.getMovies().add(this);
        return this;
    }

    public Movie removeCategory(Category category) {
        this.categories.remove(category);
        category.getMovies().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if (movie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", originalTitle='" + getOriginalTitle() + "'" +
            ", voteCount=" + getVoteCount() +
            ", voteAverage=" + getVoteAverage() +
            ", video='" + isVideo() + "'" +
            ", popularity=" + getPopularity() +
            ", posterPath='" + getPosterPath() + "'" +
            ", originalLanguage='" + getOriginalLanguage() + "'" +
            ", backdropPath='" + getBackdropPath() + "'" +
            ", adult='" + isAdult() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", overview='" + getOverview() + "'" +
            "}";
    }
}
