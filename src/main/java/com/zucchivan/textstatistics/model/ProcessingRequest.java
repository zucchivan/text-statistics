package com.zucchivan.textstatistics.model;

import lombok.Data;

@Data
public class ProcessingRequest {

    private String url;
    private Integer numberOfFrequentWords;
    private Integer numberOfLongestWords;

}
