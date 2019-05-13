package com.imdbof.web.rest;

import com.imdbof.ImdbofApp;

import com.imdbof.domain.Movie;
import com.imdbof.domain.Category;
import com.imdbof.repository.MovieRepository;
import com.imdbof.service.MovieService;
import com.imdbof.service.dto.MovieDTO;
import com.imdbof.service.mapper.MovieMapper;
import com.imdbof.web.rest.errors.ExceptionTranslator;
import com.imdbof.service.dto.MovieCriteria;
import com.imdbof.service.MovieQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.imdbof.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MovieResource REST controller.
 *
 * @see MovieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImdbofApp.class)
public class MovieResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOTE_COUNT = 1;
    private static final Integer UPDATED_VOTE_COUNT = 2;

    private static final Float DEFAULT_VOTE_AVERAGE = 1F;
    private static final Float UPDATED_VOTE_AVERAGE = 2F;

    private static final Boolean DEFAULT_VIDEO = false;
    private static final Boolean UPDATED_VIDEO = true;

    private static final Float DEFAULT_POPULARITY = 1F;
    private static final Float UPDATED_POPULARITY = 2F;

    private static final String DEFAULT_POSTER_PATH = "AAAAAAAAAA";
    private static final String UPDATED_POSTER_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_BACKDROP_PATH = "AAAAAAAAAA";
    private static final String UPDATED_BACKDROP_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ADULT = false;
    private static final Boolean UPDATED_ADULT = true;

    private static final String DEFAULT_RELEASE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_OVERVIEW = "AAAAAAAAAA";
    private static final String UPDATED_OVERVIEW = "BBBBBBBBBB";

    @Autowired
    private MovieRepository movieRepository;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Autowired
    private MovieMapper movieMapper;

    @Mock
    private MovieService movieServiceMock;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieQueryService movieQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMovieMockMvc;

    private Movie movie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MovieResource movieResource = new MovieResource(movieService, movieQueryService);
        this.restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(DEFAULT_TITLE)
            .originalTitle(DEFAULT_ORIGINAL_TITLE)
            .voteCount(DEFAULT_VOTE_COUNT)
            .voteAverage(DEFAULT_VOTE_AVERAGE)
            .video(DEFAULT_VIDEO)
            .popularity(DEFAULT_POPULARITY)
            .posterPath(DEFAULT_POSTER_PATH)
            .originalLanguage(DEFAULT_ORIGINAL_LANGUAGE)
            .backdropPath(DEFAULT_BACKDROP_PATH)
            .adult(DEFAULT_ADULT)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .overview(DEFAULT_OVERVIEW);
        return movie;
    }

    @Before
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getOriginalTitle()).isEqualTo(DEFAULT_ORIGINAL_TITLE);
        assertThat(testMovie.getVoteCount()).isEqualTo(DEFAULT_VOTE_COUNT);
        assertThat(testMovie.getVoteAverage()).isEqualTo(DEFAULT_VOTE_AVERAGE);
        assertThat(testMovie.isVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testMovie.getPopularity()).isEqualTo(DEFAULT_POPULARITY);
        assertThat(testMovie.getPosterPath()).isEqualTo(DEFAULT_POSTER_PATH);
        assertThat(testMovie.getOriginalLanguage()).isEqualTo(DEFAULT_ORIGINAL_LANGUAGE);
        assertThat(testMovie.getBackdropPath()).isEqualTo(DEFAULT_BACKDROP_PATH);
        assertThat(testMovie.isAdult()).isEqualTo(DEFAULT_ADULT);
        assertThat(testMovie.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testMovie.getOverview()).isEqualTo(DEFAULT_OVERVIEW);
    }

    @Test
    @Transactional
    public void createMovieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie with an existing ID
        movie.setId(1L);
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setTitle(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginalTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setOriginalTitle(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoteCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setVoteCount(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoteAverageIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setVoteAverage(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVideoIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setVideo(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPopularityIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setPopularity(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPosterPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setPosterPath(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginalLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setOriginalLanguage(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBackdropPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setBackdropPath(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdultIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setAdult(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReleaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setReleaseDate(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].originalTitle").value(hasItem(DEFAULT_ORIGINAL_TITLE.toString())))
            .andExpect(jsonPath("$.[*].voteCount").value(hasItem(DEFAULT_VOTE_COUNT)))
            .andExpect(jsonPath("$.[*].voteAverage").value(hasItem(DEFAULT_VOTE_AVERAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO.booleanValue())))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY.doubleValue())))
            .andExpect(jsonPath("$.[*].posterPath").value(hasItem(DEFAULT_POSTER_PATH.toString())))
            .andExpect(jsonPath("$.[*].originalLanguage").value(hasItem(DEFAULT_ORIGINAL_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].backdropPath").value(hasItem(DEFAULT_BACKDROP_PATH.toString())))
            .andExpect(jsonPath("$.[*].adult").value(hasItem(DEFAULT_ADULT.booleanValue())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].overview").value(hasItem(DEFAULT_OVERVIEW.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        MovieResource movieResource = new MovieResource(movieServiceMock, movieQueryService);
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMovieMockMvc.perform(get("/api/movies?eagerload=true"))
        .andExpect(status().isOk());

        verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        MovieResource movieResource = new MovieResource(movieServiceMock, movieQueryService);
            when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMovieMockMvc.perform(get("/api/movies?eagerload=true"))
        .andExpect(status().isOk());

            verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.originalTitle").value(DEFAULT_ORIGINAL_TITLE.toString()))
            .andExpect(jsonPath("$.voteCount").value(DEFAULT_VOTE_COUNT))
            .andExpect(jsonPath("$.voteAverage").value(DEFAULT_VOTE_AVERAGE.doubleValue()))
            .andExpect(jsonPath("$.video").value(DEFAULT_VIDEO.booleanValue()))
            .andExpect(jsonPath("$.popularity").value(DEFAULT_POPULARITY.doubleValue()))
            .andExpect(jsonPath("$.posterPath").value(DEFAULT_POSTER_PATH.toString()))
            .andExpect(jsonPath("$.originalLanguage").value(DEFAULT_ORIGINAL_LANGUAGE.toString()))
            .andExpect(jsonPath("$.backdropPath").value(DEFAULT_BACKDROP_PATH.toString()))
            .andExpect(jsonPath("$.adult").value(DEFAULT_ADULT.booleanValue()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.overview").value(DEFAULT_OVERVIEW.toString()));
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title equals to DEFAULT_TITLE
        defaultMovieShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMovieShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title is not null
        defaultMovieShouldBeFound("title.specified=true");

        // Get all the movieList where title is null
        defaultMovieShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByOriginalTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where originalTitle equals to DEFAULT_ORIGINAL_TITLE
        defaultMovieShouldBeFound("originalTitle.equals=" + DEFAULT_ORIGINAL_TITLE);

        // Get all the movieList where originalTitle equals to UPDATED_ORIGINAL_TITLE
        defaultMovieShouldNotBeFound("originalTitle.equals=" + UPDATED_ORIGINAL_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByOriginalTitleIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where originalTitle in DEFAULT_ORIGINAL_TITLE or UPDATED_ORIGINAL_TITLE
        defaultMovieShouldBeFound("originalTitle.in=" + DEFAULT_ORIGINAL_TITLE + "," + UPDATED_ORIGINAL_TITLE);

        // Get all the movieList where originalTitle equals to UPDATED_ORIGINAL_TITLE
        defaultMovieShouldNotBeFound("originalTitle.in=" + UPDATED_ORIGINAL_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByOriginalTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where originalTitle is not null
        defaultMovieShouldBeFound("originalTitle.specified=true");

        // Get all the movieList where originalTitle is null
        defaultMovieShouldNotBeFound("originalTitle.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteCountIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteCount equals to DEFAULT_VOTE_COUNT
        defaultMovieShouldBeFound("voteCount.equals=" + DEFAULT_VOTE_COUNT);

        // Get all the movieList where voteCount equals to UPDATED_VOTE_COUNT
        defaultMovieShouldNotBeFound("voteCount.equals=" + UPDATED_VOTE_COUNT);
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteCountIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteCount in DEFAULT_VOTE_COUNT or UPDATED_VOTE_COUNT
        defaultMovieShouldBeFound("voteCount.in=" + DEFAULT_VOTE_COUNT + "," + UPDATED_VOTE_COUNT);

        // Get all the movieList where voteCount equals to UPDATED_VOTE_COUNT
        defaultMovieShouldNotBeFound("voteCount.in=" + UPDATED_VOTE_COUNT);
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteCount is not null
        defaultMovieShouldBeFound("voteCount.specified=true");

        // Get all the movieList where voteCount is null
        defaultMovieShouldNotBeFound("voteCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteCount greater than or equals to DEFAULT_VOTE_COUNT
        defaultMovieShouldBeFound("voteCount.greaterOrEqualThan=" + DEFAULT_VOTE_COUNT);

        // Get all the movieList where voteCount greater than or equals to UPDATED_VOTE_COUNT
        defaultMovieShouldNotBeFound("voteCount.greaterOrEqualThan=" + UPDATED_VOTE_COUNT);
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteCountIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteCount less than or equals to DEFAULT_VOTE_COUNT
        defaultMovieShouldNotBeFound("voteCount.lessThan=" + DEFAULT_VOTE_COUNT);

        // Get all the movieList where voteCount less than or equals to UPDATED_VOTE_COUNT
        defaultMovieShouldBeFound("voteCount.lessThan=" + UPDATED_VOTE_COUNT);
    }


    @Test
    @Transactional
    public void getAllMoviesByVoteAverageIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteAverage equals to DEFAULT_VOTE_AVERAGE
        defaultMovieShouldBeFound("voteAverage.equals=" + DEFAULT_VOTE_AVERAGE);

        // Get all the movieList where voteAverage equals to UPDATED_VOTE_AVERAGE
        defaultMovieShouldNotBeFound("voteAverage.equals=" + UPDATED_VOTE_AVERAGE);
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteAverageIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteAverage in DEFAULT_VOTE_AVERAGE or UPDATED_VOTE_AVERAGE
        defaultMovieShouldBeFound("voteAverage.in=" + DEFAULT_VOTE_AVERAGE + "," + UPDATED_VOTE_AVERAGE);

        // Get all the movieList where voteAverage equals to UPDATED_VOTE_AVERAGE
        defaultMovieShouldNotBeFound("voteAverage.in=" + UPDATED_VOTE_AVERAGE);
    }

    @Test
    @Transactional
    public void getAllMoviesByVoteAverageIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where voteAverage is not null
        defaultMovieShouldBeFound("voteAverage.specified=true");

        // Get all the movieList where voteAverage is null
        defaultMovieShouldNotBeFound("voteAverage.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByVideoIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where video equals to DEFAULT_VIDEO
        defaultMovieShouldBeFound("video.equals=" + DEFAULT_VIDEO);

        // Get all the movieList where video equals to UPDATED_VIDEO
        defaultMovieShouldNotBeFound("video.equals=" + UPDATED_VIDEO);
    }

    @Test
    @Transactional
    public void getAllMoviesByVideoIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where video in DEFAULT_VIDEO or UPDATED_VIDEO
        defaultMovieShouldBeFound("video.in=" + DEFAULT_VIDEO + "," + UPDATED_VIDEO);

        // Get all the movieList where video equals to UPDATED_VIDEO
        defaultMovieShouldNotBeFound("video.in=" + UPDATED_VIDEO);
    }

    @Test
    @Transactional
    public void getAllMoviesByVideoIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where video is not null
        defaultMovieShouldBeFound("video.specified=true");

        // Get all the movieList where video is null
        defaultMovieShouldNotBeFound("video.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity equals to DEFAULT_POPULARITY
        defaultMovieShouldBeFound("popularity.equals=" + DEFAULT_POPULARITY);

        // Get all the movieList where popularity equals to UPDATED_POPULARITY
        defaultMovieShouldNotBeFound("popularity.equals=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity in DEFAULT_POPULARITY or UPDATED_POPULARITY
        defaultMovieShouldBeFound("popularity.in=" + DEFAULT_POPULARITY + "," + UPDATED_POPULARITY);

        // Get all the movieList where popularity equals to UPDATED_POPULARITY
        defaultMovieShouldNotBeFound("popularity.in=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllMoviesByPopularityIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where popularity is not null
        defaultMovieShouldBeFound("popularity.specified=true");

        // Get all the movieList where popularity is null
        defaultMovieShouldNotBeFound("popularity.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByPosterPathIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where posterPath equals to DEFAULT_POSTER_PATH
        defaultMovieShouldBeFound("posterPath.equals=" + DEFAULT_POSTER_PATH);

        // Get all the movieList where posterPath equals to UPDATED_POSTER_PATH
        defaultMovieShouldNotBeFound("posterPath.equals=" + UPDATED_POSTER_PATH);
    }

    @Test
    @Transactional
    public void getAllMoviesByPosterPathIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where posterPath in DEFAULT_POSTER_PATH or UPDATED_POSTER_PATH
        defaultMovieShouldBeFound("posterPath.in=" + DEFAULT_POSTER_PATH + "," + UPDATED_POSTER_PATH);

        // Get all the movieList where posterPath equals to UPDATED_POSTER_PATH
        defaultMovieShouldNotBeFound("posterPath.in=" + UPDATED_POSTER_PATH);
    }

    @Test
    @Transactional
    public void getAllMoviesByPosterPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where posterPath is not null
        defaultMovieShouldBeFound("posterPath.specified=true");

        // Get all the movieList where posterPath is null
        defaultMovieShouldNotBeFound("posterPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByOriginalLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where originalLanguage equals to DEFAULT_ORIGINAL_LANGUAGE
        defaultMovieShouldBeFound("originalLanguage.equals=" + DEFAULT_ORIGINAL_LANGUAGE);

        // Get all the movieList where originalLanguage equals to UPDATED_ORIGINAL_LANGUAGE
        defaultMovieShouldNotBeFound("originalLanguage.equals=" + UPDATED_ORIGINAL_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllMoviesByOriginalLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where originalLanguage in DEFAULT_ORIGINAL_LANGUAGE or UPDATED_ORIGINAL_LANGUAGE
        defaultMovieShouldBeFound("originalLanguage.in=" + DEFAULT_ORIGINAL_LANGUAGE + "," + UPDATED_ORIGINAL_LANGUAGE);

        // Get all the movieList where originalLanguage equals to UPDATED_ORIGINAL_LANGUAGE
        defaultMovieShouldNotBeFound("originalLanguage.in=" + UPDATED_ORIGINAL_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllMoviesByOriginalLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where originalLanguage is not null
        defaultMovieShouldBeFound("originalLanguage.specified=true");

        // Get all the movieList where originalLanguage is null
        defaultMovieShouldNotBeFound("originalLanguage.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByBackdropPathIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where backdropPath equals to DEFAULT_BACKDROP_PATH
        defaultMovieShouldBeFound("backdropPath.equals=" + DEFAULT_BACKDROP_PATH);

        // Get all the movieList where backdropPath equals to UPDATED_BACKDROP_PATH
        defaultMovieShouldNotBeFound("backdropPath.equals=" + UPDATED_BACKDROP_PATH);
    }

    @Test
    @Transactional
    public void getAllMoviesByBackdropPathIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where backdropPath in DEFAULT_BACKDROP_PATH or UPDATED_BACKDROP_PATH
        defaultMovieShouldBeFound("backdropPath.in=" + DEFAULT_BACKDROP_PATH + "," + UPDATED_BACKDROP_PATH);

        // Get all the movieList where backdropPath equals to UPDATED_BACKDROP_PATH
        defaultMovieShouldNotBeFound("backdropPath.in=" + UPDATED_BACKDROP_PATH);
    }

    @Test
    @Transactional
    public void getAllMoviesByBackdropPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where backdropPath is not null
        defaultMovieShouldBeFound("backdropPath.specified=true");

        // Get all the movieList where backdropPath is null
        defaultMovieShouldNotBeFound("backdropPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByAdultIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where adult equals to DEFAULT_ADULT
        defaultMovieShouldBeFound("adult.equals=" + DEFAULT_ADULT);

        // Get all the movieList where adult equals to UPDATED_ADULT
        defaultMovieShouldNotBeFound("adult.equals=" + UPDATED_ADULT);
    }

    @Test
    @Transactional
    public void getAllMoviesByAdultIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where adult in DEFAULT_ADULT or UPDATED_ADULT
        defaultMovieShouldBeFound("adult.in=" + DEFAULT_ADULT + "," + UPDATED_ADULT);

        // Get all the movieList where adult equals to UPDATED_ADULT
        defaultMovieShouldNotBeFound("adult.in=" + UPDATED_ADULT);
    }

    @Test
    @Transactional
    public void getAllMoviesByAdultIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where adult is not null
        defaultMovieShouldBeFound("adult.specified=true");

        // Get all the movieList where adult is null
        defaultMovieShouldNotBeFound("adult.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the movieList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultMovieShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the movieList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultMovieShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllMoviesByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseDate is not null
        defaultMovieShouldBeFound("releaseDate.specified=true");

        // Get all the movieList where releaseDate is null
        defaultMovieShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        movie.addCategory(category);
        movieRepository.saveAndFlush(movie);
        Long categoryId = category.getId();

        // Get all the movieList where category equals to categoryId
        defaultMovieShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the movieList where category equals to categoryId + 1
        defaultMovieShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMovieShouldBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].originalTitle").value(hasItem(DEFAULT_ORIGINAL_TITLE)))
            .andExpect(jsonPath("$.[*].voteCount").value(hasItem(DEFAULT_VOTE_COUNT)))
            .andExpect(jsonPath("$.[*].voteAverage").value(hasItem(DEFAULT_VOTE_AVERAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO.booleanValue())))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY.doubleValue())))
            .andExpect(jsonPath("$.[*].posterPath").value(hasItem(DEFAULT_POSTER_PATH)))
            .andExpect(jsonPath("$.[*].originalLanguage").value(hasItem(DEFAULT_ORIGINAL_LANGUAGE)))
            .andExpect(jsonPath("$.[*].backdropPath").value(hasItem(DEFAULT_BACKDROP_PATH)))
            .andExpect(jsonPath("$.[*].adult").value(hasItem(DEFAULT_ADULT.booleanValue())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE)))
            .andExpect(jsonPath("$.[*].overview").value(hasItem(DEFAULT_OVERVIEW.toString())));

        // Check, that the count call also returns 1
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMovieShouldNotBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .title(UPDATED_TITLE)
            .originalTitle(UPDATED_ORIGINAL_TITLE)
            .voteCount(UPDATED_VOTE_COUNT)
            .voteAverage(UPDATED_VOTE_AVERAGE)
            .video(UPDATED_VIDEO)
            .popularity(UPDATED_POPULARITY)
            .posterPath(UPDATED_POSTER_PATH)
            .originalLanguage(UPDATED_ORIGINAL_LANGUAGE)
            .backdropPath(UPDATED_BACKDROP_PATH)
            .adult(UPDATED_ADULT)
            .releaseDate(UPDATED_RELEASE_DATE)
            .overview(UPDATED_OVERVIEW);
        MovieDTO movieDTO = movieMapper.toDto(updatedMovie);

        restMovieMockMvc.perform(put("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getOriginalTitle()).isEqualTo(UPDATED_ORIGINAL_TITLE);
        assertThat(testMovie.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
        assertThat(testMovie.getVoteAverage()).isEqualTo(UPDATED_VOTE_AVERAGE);
        assertThat(testMovie.isVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testMovie.getPopularity()).isEqualTo(UPDATED_POPULARITY);
        assertThat(testMovie.getPosterPath()).isEqualTo(UPDATED_POSTER_PATH);
        assertThat(testMovie.getOriginalLanguage()).isEqualTo(UPDATED_ORIGINAL_LANGUAGE);
        assertThat(testMovie.getBackdropPath()).isEqualTo(UPDATED_BACKDROP_PATH);
        assertThat(testMovie.isAdult()).isEqualTo(UPDATED_ADULT);
        assertThat(testMovie.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testMovie.getOverview()).isEqualTo(UPDATED_OVERVIEW);
    }

    @Test
    @Transactional
    public void updateNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc.perform(put("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Delete the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movie.class);
        Movie movie1 = new Movie();
        movie1.setId(1L);
        Movie movie2 = new Movie();
        movie2.setId(movie1.getId());
        assertThat(movie1).isEqualTo(movie2);
        movie2.setId(2L);
        assertThat(movie1).isNotEqualTo(movie2);
        movie1.setId(null);
        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovieDTO.class);
        MovieDTO movieDTO1 = new MovieDTO();
        movieDTO1.setId(1L);
        MovieDTO movieDTO2 = new MovieDTO();
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO2.setId(movieDTO1.getId());
        assertThat(movieDTO1).isEqualTo(movieDTO2);
        movieDTO2.setId(2L);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO1.setId(null);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(movieMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(movieMapper.fromId(null)).isNull();
    }
}
