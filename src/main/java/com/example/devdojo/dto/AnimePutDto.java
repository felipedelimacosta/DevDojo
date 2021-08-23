package com.example.devdojo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimePutDto {
    private Long id;
    private String name;
}

