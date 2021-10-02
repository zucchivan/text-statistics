package com.zucchivan.textstatistics;

import com.zucchivan.textstatistics.operation.TextProcessingOperations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TextStatisticsApplicationTests {

	@Test
	void contextLoads() {
		//assertTrue(operations != null);
	}

	@Test
	void textIsProcessed() {
		var operations = new TextProcessingOperations("https://www.gutenberg.org/files/45839/45839.txt");

		assertTrue(operations.numberOfWords() > 0);
		assertTrue(operations.numberOfLines() > 0);
		assertTrue(operations.topWords(10).size() > 0);
		assertTrue(operations.longestWords(10).size() > 0);
	}

}
