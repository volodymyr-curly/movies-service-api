package com.reactives_spring.movies_info_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfoRequest {

    private String movieInfoId;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "year must not be null")
    @Positive(message = "year must not be positive")
    private Integer year;

    private List<@NotBlank(message = "cast must not be blank")
            String> cast;

    @NotNull(message = "release_date must not be null")
    private LocalDate release_date;
}
