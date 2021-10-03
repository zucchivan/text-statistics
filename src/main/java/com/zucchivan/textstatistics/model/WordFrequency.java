package com.zucchivan.textstatistics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordFrequency implements IWordFrequency {

    @JsonProperty
    private String word;

    @JsonProperty
    private long frequency;

    public WordFrequency(String word, long frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public String word() {
        return word;
    }

    @Override
    public long frequency() {
        return frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

}
