package com.imdbof.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.imdbof.domain.Category;
import com.imdbof.domain.Movie;
import com.imdbof.repository.CategoryRepository;
import com.imdbof.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class Query implements GraphQLQueryResolver {

    private static final Logger log = LoggerFactory.getLogger(Query.class);

    private final MovieRepository movieRepository;

    private final CategoryRepository categoryRepository;

    public Query(MovieRepository movieRepository, CategoryRepository categoryRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
    }

    public String hello() {
        return "Hello world!";
    }

    @Transactional(readOnly = true)
    public List<Movie> getMovies(Optional<String> categoryName) {
        log.info("getMovies with optional categoryName {}", categoryName);

        return categoryName.isPresent() ? movieRepository.findByCategoriesName(categoryName.get()) : movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        log.info("getCategories");
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Movie> getMovie(Long id) {
        log.info("getMovie: {}", id);
        return movieRepository.findById(id);
    }
}
