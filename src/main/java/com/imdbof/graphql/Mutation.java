package com.imdbof.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.imdbof.domain.Movie;
import com.imdbof.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Component
public class Mutation implements GraphQLMutationResolver {

    private static final Logger log = LoggerFactory.getLogger(Mutation.class);

    private final MovieRepository movieRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public Mutation(MovieRepository movieRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.movieRepository = movieRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public Movie addVote(Long movieId) {
        log.info("addVote on {}", movieId);
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(EntityNotFoundException::new)
            .addVote();

        applicationEventPublisher.publishEvent(new VoteAddedEvent(this, movie));

        return movie;
    }
}
