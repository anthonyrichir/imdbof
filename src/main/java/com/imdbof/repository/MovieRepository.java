package com.imdbof.repository;

import com.imdbof.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Movie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query(value = "select distinct movie from Movie movie left join fetch movie.categories",
        countQuery = "select count(distinct movie) from Movie movie")
    Page<Movie> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct movie from Movie movie left join fetch movie.categories")
    List<Movie> findAllWithEagerRelationships();

    @Query("select movie from Movie movie left join fetch movie.categories where movie.id =:id")
    Optional<Movie> findOneWithEagerRelationships(@Param("id") Long id);

    List<Movie> findByCategoriesName(String categoryName);

}
