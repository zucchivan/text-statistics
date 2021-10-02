package com.zucchivan.textstatistics.controller;

import com.zucchivan.textstatistics.model.ProcessingRequest;
import com.zucchivan.textstatistics.model.ProcessingResponse;
import com.zucchivan.textstatistics.operation.TextProcessingOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TextStatisticsController {

    private final Logger logger = LoggerFactory.getLogger(TextStatisticsController.class);

    private static final int DEFAULT_NUMBER_FREQUENT_WORDS = 10;
    private static final int DEFAULT_NUMBER_LONGEST_WORDS = 10;

    @GetMapping("/process")
    public ResponseEntity<ProcessingResponse> process(@RequestBody ProcessingRequest request) {
        if (request.getUrl() == null || request.getUrl().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(ProcessingResponse.builder()
                            .message("Text URL is missing!")
                            .build());
        }

        var operations = new TextProcessingOperations(request.getUrl());

        var numberOfFrequentWords = Optional.ofNullable(request.getNumberOfFrequentWords())
                .orElse(DEFAULT_NUMBER_FREQUENT_WORDS);
        var numberOfLongestWords = Optional.ofNullable(request.getNumberOfLongestWords())
                .orElse(DEFAULT_NUMBER_LONGEST_WORDS);

        ResponseEntity<ProcessingResponse> response = null;
        try {
            var frequentWords = operations.topWords(numberOfFrequentWords);
            var longestWords = operations.longestWords(numberOfLongestWords);
            var numberOfLines = operations.numberOfLines();
            var numberOfWords = operations.numberOfWords();

            response = ResponseEntity.ok(ProcessingResponse.builder()
                    .mostFrequentWords(frequentWords)
                    .numberOfLines(numberOfLines)
                    .numberOfWords(numberOfWords)
                    .longestWords(longestWords)
                    .build());
        } catch (Exception e) {
            response = ResponseEntity
                    .internalServerError()
                    .body(ProcessingResponse.builder()
                            .message("An internal error occurred while executing text processing operations.")
                            .build());
            logger.error("Error while processing the text! ", e);
        }

        return response;
    }

}
