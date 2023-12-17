package com.reactives_spring.movies_info_service.mapper;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import com.reactives_spring.movies_info_service.dto.MovieInfoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovieInfoMapper {

    @Mapping(target = "movieInfoId", ignore = true)
    MovieInfo toMovieInfo(MovieInfoRequest movieInfo);

    MovieInfo toMovieInfoWithId(MovieInfoRequest movieInfo);
}
