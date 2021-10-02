package com.zucchivan.textstatistics.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProcessingResponse {

    private List<IWordFrequency> mostFrequentWords;
    private List<String> longestWords;
    private Long numberOfLines;
    private Long numberOfWords;
    private String message;

}
