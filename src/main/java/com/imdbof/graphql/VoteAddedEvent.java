package com.imdbof.graphql;

import com.imdbof.domain.Movie;
import org.springframework.context.ApplicationEvent;

public class VoteAddedEvent extends ApplicationEvent {

    private Movie movie;

    public VoteAddedEvent(Object source, Movie movie) {
        super(source);
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
