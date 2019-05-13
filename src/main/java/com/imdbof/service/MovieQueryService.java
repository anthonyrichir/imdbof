package com.imdbof.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.imdbof.domain.Movie;
import com.imdbof.domain.*; // for static metamodels
import com.imdbof.repository.MovieRepository;
import com.imdbof.service.dto.MovieCriteria;
import com.imdbof.service.dto.MovieDTO;
import com.imdbof.service.mapper.MovieMapper;

/**
 * Service for executing complex queries for Movie entities in the database.
 * The main input is a {@link MovieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovieDTO} or a {@link Page} of {@link MovieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovieQueryService extends QueryService<Movie> {

    private final Logger log = LoggerFactory.getLogger(MovieQueryService.class);

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    public MovieQueryService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * Return a {@link List} of {@link MovieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovieDTO> findByCriteria(MovieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieMapper.toDto(movieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MovieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovieDTO> findByCriteria(MovieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.findAll(specification, page)
            .map(movieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.count(specification);
    }

    /**
     * Function to convert MovieCriteria to a {@link Specification}
     */
    private Specification<Movie> createSpecification(MovieCriteria criteria) {
        Specification<Movie> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Movie_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Movie_.title));
            }
            if (criteria.getOriginalTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOriginalTitle(), Movie_.originalTitle));
            }
            if (criteria.getVoteCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVoteCount(), Movie_.voteCount));
            }
            if (criteria.getVoteAverage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVoteAverage(), Movie_.voteAverage));
            }
            if (criteria.getVideo() != null) {
                specification = specification.and(buildSpecification(criteria.getVideo(), Movie_.video));
            }
            if (criteria.getPopularity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPopularity(), Movie_.popularity));
            }
            if (criteria.getPosterPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosterPath(), Movie_.posterPath));
            }
            if (criteria.getOriginalLanguage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOriginalLanguage(), Movie_.originalLanguage));
            }
            if (criteria.getBackdropPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBackdropPath(), Movie_.backdropPath));
            }
            if (criteria.getAdult() != null) {
                specification = specification.and(buildSpecification(criteria.getAdult(), Movie_.adult));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReleaseDate(), Movie_.releaseDate));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Movie_.categories, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
