package com.example.devdojo.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class AnimePostDto {

    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
}
