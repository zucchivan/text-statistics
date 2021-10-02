package com.zucchivan.textstatistics.model;

public class WordFrequency implements IWordFrequency {

    private String word;
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
}
