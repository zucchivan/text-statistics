package com.zucchivan.textstatistics.model;

/**
* Represents a word and its frequency.-
* Interface given with challenge's instructions
*/
public interface IWordFrequency {

    /**
    * The word.
    * @return the word as a string.
    */
    String word();

    /**
    * The frequency.
    * @return a long representing the frequency of the word.
    */
    long frequency();
}