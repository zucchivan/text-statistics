package com.zucchivan.textstatistics;

import com.zucchivan.textstatistics.operation.TextProcessingOperations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TextStatisticsApplicationTests {

	@Autowired
	private TextProcessingOperations operations;

	@Test
	void contextLoads() {
		assertTrue(operations != null);
	}

	@Test
	void textIsProcessed() {
		var map = operations.proccessText("https://www.gutenberg.org/files/45839/45839.txt");
		assertTrue(map.entrySet().size() > 0);
	}

}
