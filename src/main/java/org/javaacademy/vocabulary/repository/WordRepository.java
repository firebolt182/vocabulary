package org.javaacademy.vocabulary.repository;

import java.util.*;
import lombok.SneakyThrows;
import org.javaacademy.vocabulary.entity.Word;
import org.springframework.stereotype.Service;

@Service
public class WordRepository {
    private Map<String, Word> words = new TreeMap<>();

    public Word add(String word, String description) {
        Word newWord = new Word(word, description);
        words.put(newWord.getWord(), newWord);
        return newWord;
    }

    @SneakyThrows
    public List<Word> findAll() {
        Thread.sleep(1000);
        return new ArrayList<>(words.values());
    }

    public Optional<Word> findByKey(String key) {
        return Optional.ofNullable(words.get(key));
    }

    public void updateByKey(String key, Word newWord) {
        if (!words.containsKey(key)) {
            throw new RuntimeException("Ключ отсутствует");
        }
        words.put(key, newWord);
    }

    public boolean delete(String key) {
        return words.remove(key) != null;
    }
}
