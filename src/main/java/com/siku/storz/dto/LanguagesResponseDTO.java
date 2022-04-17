package com.siku.storz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class LanguagesResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, String> languages;
    private String description;
    private String status;
}
