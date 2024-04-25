package org.javaacademy.vocabulary.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.vocabulary.dto.PageDto;
import org.javaacademy.vocabulary.dto.WordDtoRq;
import org.javaacademy.vocabulary.dto.WordDtoRs;
import org.javaacademy.vocabulary.entity.Word;
import org.javaacademy.vocabulary.repository.WordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;

    public WordDtoRs create(WordDtoRq dtoRq) {
        Word newWord = convertToEntity(dtoRq);
        wordRepository.add(newWord.getWord(), newWord.getDescription());
        return convertToDtoRs(newWord);
    }

    public PageDto<List<WordDtoRs>> findAll(Integer startElement, Integer pageSize) {
        int repositorySize = wordRepository.findAll().size();
        if (startElement == null) {
            startElement = 0;
        }
        if (pageSize == null) {
            pageSize = repositorySize;
        }
        List<WordDtoRs> wordDtoRsList = wordRepository.findAll().stream()
                .skip(startElement)
                .limit(pageSize)
                .map(this::convertToDtoRs).toList();
        return new PageDto<List<WordDtoRs>>(repositorySize, pageSize,
                startElement, startElement + pageSize,
                wordDtoRsList);
    }

    public WordDtoRs findByKey(String key) {
        return wordRepository.findByKey(key).map(this::convertToDtoRs).orElseThrow();
    }

    public void updateByKey(String key, WordDtoRq dtoRq) {
        if (!key.equals(dtoRq.getWord())) {
            Word newWord = convertToEntity(dtoRq);
            wordRepository.delete(key);
            wordRepository.add(newWord.getWord(), newWord.getDescription());
        } else {
            wordRepository.updateByKey(key, convertToEntity(dtoRq));
        }
    }

    public WordDtoRs patchDescription(String key, WordDtoRq wordDtoRq) {
        Word oldWord = wordRepository.findByKey(key).orElseThrow();
        oldWord.setDescription(wordDtoRq.getDescription() != null
                ? wordDtoRq.getDescription()
                : oldWord.getDescription());
        wordRepository.updateByKey(key, oldWord);
        return convertToDtoRs(oldWord);
    }

    public boolean deleteByKey(String key) {
        return wordRepository.delete(key);
    }

    private Word convertToEntity(WordDtoRq wordDtoRq) {
        return new Word(wordDtoRq.getWord(), wordDtoRq.getDescription());
    }

    private WordDtoRs convertToDtoRs(Word word) {
        return new WordDtoRs(word.getWord(), word.getDescription());
    }
}
