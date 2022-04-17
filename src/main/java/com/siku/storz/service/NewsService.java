package com.siku.storz.service;


import com.siku.storz.config.CacheNames;
import com.siku.storz.dto.LanguagesResponseDTO;
import com.siku.storz.dto.NewsResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class NewsService {

    @Value("${currentsapi.url.search}")
    private String currentsApiSearchUrl;
    @Value("${currentsapi.url.language}")
    private String currentsApiLanguageUrl;
    @Value("${currentsapi.token}")
    private String currentsApiToken;


    @Cacheable(cacheNames = CacheNames.LANGUAGES)
    public LanguagesResponseDTO getLanguages() {
        RestTemplate restTemplate = new RestTemplate();
        LanguagesResponseDTO result = restTemplate.getForObject(currentsApiLanguageUrl, LanguagesResponseDTO.class);
        log.info("Found languages:{}", result);
        return result;
    }

    @Cacheable(
            cacheNames = CacheNames.NEWS,
            key = "#language + #keyword"
    )
    public NewsResponseDTO getNews(String language, String keyword) {
        URI searchUriWithParameters = buildSearchUriWithParameters(language, keyword);
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> request = RequestEntity.get(searchUriWithParameters)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", currentsApiToken)
                .build();

        ResponseEntity<NewsResponseDTO> response = restTemplate.exchange(request, NewsResponseDTO.class);
        return response.getBody();
    }

    private URI buildSearchUriWithParameters(String language, String keyword) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(currentsApiSearchUrl);
        if (!StringUtils.isNullOrEmpty(keyword)) {
            builder.queryParam("keywords", keyword);
        }
        if (!StringUtils.isNullOrEmpty(language)) {
            builder.queryParam("language", language);
        }
        log.info("URL:{}",builder.toUriString());
        return builder.build().toUri();
    }

}
