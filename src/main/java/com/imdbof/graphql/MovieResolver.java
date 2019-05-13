package com.imdbof.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.imdbof.domain.Category;
import com.imdbof.domain.Movie;
import com.imdbof.repository.MovieRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieResolver implements GraphQLResolver<Movie> {

    private final MovieRepository movieRepository;

    public MovieResolver(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories(Movie movie) {
        return new ArrayList<>(movieRepository.findById(movie.getId()).orElseThrow(RuntimeException::new).getCategories());
    }
}
