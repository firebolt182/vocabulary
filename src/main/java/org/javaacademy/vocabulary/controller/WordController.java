package org.javaacademy.vocabulary.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.vocabulary.dto.PageDto;
import org.javaacademy.vocabulary.dto.WordDtoRq;
import org.javaacademy.vocabulary.dto.WordDtoRs;
import org.javaacademy.vocabulary.service.WordService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/word")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "findAll")
public class WordController {
    private final WordService wordService;

    @PostMapping
    @CacheEvict(cacheNames = "findAll", allEntries = true)
    public ResponseEntity<WordDtoRs> create(@RequestBody WordDtoRq body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wordService.create(body));
    }

    @GetMapping
    @Cacheable(cacheNames = "findAll")
    @CachePut(cacheNames = "findAll", condition = "#refresh==true")
    public ResponseEntity<PageDto<List<WordDtoRs>>> findAll(
            @RequestParam(required = false) Integer startElement,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) boolean refresh) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(wordService.findAll(startElement, pageSize));
    }

    @GetMapping("/{key}")
    public ResponseEntity<WordDtoRs> findByKey(@PathVariable String key) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(wordService.findByKey(key));
    }

    @PutMapping("/{key}")
    public ResponseEntity updateWord(@PathVariable String key, @RequestBody WordDtoRq wordDtoRq) {
        wordService.updateByKey(key, wordDtoRq);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/{key}")
    public ResponseEntity patchDescription(@PathVariable String key,
                                           @RequestBody WordDtoRq wordDtoRq) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(wordService.patchDescription(key, wordDtoRq));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity deleteByKey(@PathVariable String key) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(wordService.deleteByKey(key));
    }
}
