package com.imdbof.web.rest;
import com.imdbof.service.MovieService;
import com.imdbof.web.rest.errors.BadRequestAlertException;
import com.imdbof.web.rest.util.HeaderUtil;
import com.imdbof.web.rest.util.PaginationUtil;
import com.imdbof.service.dto.MovieDTO;
import com.imdbof.service.dto.MovieCriteria;
import com.imdbof.service.MovieQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Movie.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);

    private static final String ENTITY_NAME = "movie";

    private final MovieService movieService;

    private final MovieQueryService movieQueryService;

    public MovieResource(MovieService movieService, MovieQueryService movieQueryService) {
        this.movieService = movieService;
        this.movieQueryService = movieQueryService;
    }

    /**
     * POST  /movies : Create a new movie.
     *
     * @param movieDTO the movieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movieDTO, or with status 400 (Bad Request) if the movie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movies")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movieDTO);
        if (movieDTO.getId() != null) {
            throw new BadRequestAlertException("A new movie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovieDTO result = movieService.save(movieDTO);
        return ResponseEntity.created(new URI("/api/movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movies : Updates an existing movie.
     *
     * @param movieDTO the movieDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movieDTO,
     * or with status 400 (Bad Request) if the movieDTO is not valid,
     * or with status 500 (Internal Server Error) if the movieDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movies")
    public ResponseEntity<MovieDTO> updateMovie(@Valid @RequestBody MovieDTO movieDTO) throws URISyntaxException {
        log.debug("REST request to update Movie : {}", movieDTO);
        if (movieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MovieDTO result = movieService.save(movieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, movieDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movies : get all the movies.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of movies in body
     */
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDTO>> getAllMovies(MovieCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Movies by criteria: {}", criteria);
        Page<MovieDTO> page = movieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movies");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /movies/count : count all the movies.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/movies/count")
    public ResponseEntity<Long> countMovies(MovieCriteria criteria) {
        log.debug("REST request to count Movies by criteria: {}", criteria);
        return ResponseEntity.ok().body(movieQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /movies/:id : get the "id" movie.
     *
     * @param id the id of the movieDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movieDTO, or with status 404 (Not Found)
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        Optional<MovieDTO> movieDTO = movieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movieDTO);
    }

    /**
     * DELETE  /movies/:id : delete the "id" movie.
     *
     * @param id the id of the movieDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
