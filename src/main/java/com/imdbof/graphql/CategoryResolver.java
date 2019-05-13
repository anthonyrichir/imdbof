package com.imdbof.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.imdbof.domain.Category;
import com.imdbof.domain.Movie;
import com.imdbof.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryResolver implements GraphQLResolver<Category> {

    private final CategoryRepository categoryRepository;

    public CategoryResolver(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Movie> getMovies(Category category) {
        return new ArrayList<>(categoryRepository.findById(category.getId()).orElseThrow(RuntimeException::new).getMovies());
    }
}
