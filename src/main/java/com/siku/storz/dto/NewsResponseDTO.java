package com.siku.storz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewsResponseDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private Integer page;
    private List<NewsDTO> news;
}
