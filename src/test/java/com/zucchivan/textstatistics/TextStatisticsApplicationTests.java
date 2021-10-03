package com.zucchivan.textstatistics;

import com.zucchivan.textstatistics.operation.TextProcessingOperations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TextStatisticsApplicationTests {

	private static final String DRACULA_TEXT_URL = "https://www.gutenberg.org/files/45839/45839.txt";

	@Test
	void lineCountingTest() {
		var operations = new TextProcessingOperations(DRACULA_TEXT_URL);

		assertTrue(operations.numberOfLines() > 0);
	}

	@Test
	void wordCountingTest() {
		var operations = new TextProcessingOperations(DRACULA_TEXT_URL);

		assertTrue(operations.numberOfWords() > 0);
	}

	@Test
	void wordsFrequencyTest() {
		var operations = new TextProcessingOperations(DRACULA_TEXT_URL);

		assertTrue(operations.topWords(10).size() > 0);
	}

	@Test
	void longestWordsTest() {
		var operations = new TextProcessingOperations(DRACULA_TEXT_URL);

		assertTrue(operations.longestWords(10).size() > 0);
	}

}
