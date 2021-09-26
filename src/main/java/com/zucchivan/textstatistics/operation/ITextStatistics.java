package com.zucchivan.textstatistics.operation;

import com.zucchivan.textstatistics.model.IWordFrequency;

import java.util.List;

public interface ITextStatistics {

    /**
    * Returns a list of the most frequented words of the text.
    * @param n how many items of the list
    * @return a list representing the top n frequent words of the text.
    */
    List<IWordFrequency> topWords(int n);

    /**
    * Returns a list of the longest words of the text.
    * @param n how many items to return.
    * @return a list with the n longest words of the text.
    */
    List<String> longestWords(int n);

    /**
    * @return total number of words in the text.
    */
    long numberOfWords();

    /**
    * @return total number of lines of the text.
    */
    long numberOfLines();

}