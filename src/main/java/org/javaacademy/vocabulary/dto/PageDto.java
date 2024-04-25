package org.javaacademy.vocabulary.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class PageDto<T> {
    @NonNull
    private Integer totalSize;
    @NonNull
    private Integer pageSize;
    @NonNull
    private Integer startElement;
    @NonNull
    private Integer endElement;
    @NonNull
    private T content;

}
