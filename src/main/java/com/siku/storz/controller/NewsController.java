package com.siku.storz.controller;

import com.siku.storz.dto.LanguagesResponseDTO;
import com.siku.storz.dto.NewsResponseDTO;
import com.siku.storz.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(final NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/lang")
    public LanguagesResponseDTO getLanguages() {
        long start = System.nanoTime();
        LanguagesResponseDTO response = newsService.getLanguages();
        long duration = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start,TimeUnit.NANOSECONDS);
        log.info("Method execution time:{}ms", duration);
        return response;
    }

    @GetMapping("/news")
    @ExceptionHandler(AuthenticationException.class)
    public NewsResponseDTO getNews(@RequestParam(required = false) String language, @RequestParam(required = false) String keyword) {
        long start = System.nanoTime();
        NewsResponseDTO response = newsService.getNews(language,keyword);
        long duration = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start,TimeUnit.NANOSECONDS);
        log.info("Method execution time:{}ms", duration);
        return response;
    }
}
