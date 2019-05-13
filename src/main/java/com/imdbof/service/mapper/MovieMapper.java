package com.imdbof.service.mapper;

import com.imdbof.domain.*;
import com.imdbof.service.dto.MovieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Movie and its DTO MovieDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface MovieMapper extends EntityMapper<MovieDTO, Movie> {



    default Movie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(id);
        return movie;
    }
}
