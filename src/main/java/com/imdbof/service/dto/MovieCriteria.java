package com.imdbof.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Movie entity. This class is used in MovieResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /movies?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MovieCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter originalTitle;

    private IntegerFilter voteCount;

    private FloatFilter voteAverage;

    private BooleanFilter video;

    private FloatFilter popularity;

    private StringFilter posterPath;

    private StringFilter originalLanguage;

    private StringFilter backdropPath;

    private BooleanFilter adult;

    private StringFilter releaseDate;

    private LongFilter categoryId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(StringFilter originalTitle) {
        this.originalTitle = originalTitle;
    }

    public IntegerFilter getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(IntegerFilter voteCount) {
        this.voteCount = voteCount;
    }

    public FloatFilter getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(FloatFilter voteAverage) {
        this.voteAverage = voteAverage;
    }

    public BooleanFilter getVideo() {
        return video;
    }

    public void setVideo(BooleanFilter video) {
        this.video = video;
    }

    public FloatFilter getPopularity() {
        return popularity;
    }

    public void setPopularity(FloatFilter popularity) {
        this.popularity = popularity;
    }

    public StringFilter getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(StringFilter posterPath) {
        this.posterPath = posterPath;
    }

    public StringFilter getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(StringFilter originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public StringFilter getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(StringFilter backdropPath) {
        this.backdropPath = backdropPath;
    }

    public BooleanFilter getAdult() {
        return adult;
    }

    public void setAdult(BooleanFilter adult) {
        this.adult = adult;
    }

    public StringFilter getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(StringFilter releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MovieCriteria that = (MovieCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(originalTitle, that.originalTitle) &&
            Objects.equals(voteCount, that.voteCount) &&
            Objects.equals(voteAverage, that.voteAverage) &&
            Objects.equals(video, that.video) &&
            Objects.equals(popularity, that.popularity) &&
            Objects.equals(posterPath, that.posterPath) &&
            Objects.equals(originalLanguage, that.originalLanguage) &&
            Objects.equals(backdropPath, that.backdropPath) &&
            Objects.equals(adult, that.adult) &&
            Objects.equals(releaseDate, that.releaseDate) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        originalTitle,
        voteCount,
        voteAverage,
        video,
        popularity,
        posterPath,
        originalLanguage,
        backdropPath,
        adult,
        releaseDate,
        categoryId
        );
    }

    @Override
    public String toString() {
        return "MovieCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (originalTitle != null ? "originalTitle=" + originalTitle + ", " : "") +
                (voteCount != null ? "voteCount=" + voteCount + ", " : "") +
                (voteAverage != null ? "voteAverage=" + voteAverage + ", " : "") +
                (video != null ? "video=" + video + ", " : "") +
                (popularity != null ? "popularity=" + popularity + ", " : "") +
                (posterPath != null ? "posterPath=" + posterPath + ", " : "") +
                (originalLanguage != null ? "originalLanguage=" + originalLanguage + ", " : "") +
                (backdropPath != null ? "backdropPath=" + backdropPath + ", " : "") +
                (adult != null ? "adult=" + adult + ", " : "") +
                (releaseDate != null ? "releaseDate=" + releaseDate + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }

}
