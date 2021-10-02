package com.zucchivan.textstatistics.controller;

import com.zucchivan.textstatistics.model.ProcessingRequest;
import com.zucchivan.textstatistics.model.ProcessingResponse;
import com.zucchivan.textstatistics.operation.TextProcessingOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextStatisticsController {

    @Autowired
    private TextProcessingOperations operations;

    @GetMapping("/process")
    public ResponseEntity<ProcessingResponse> process(@RequestBody ProcessingRequest request) {
        if (request.getUrl() == null || request.getUrl().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(ProcessingResponse
                            .builder()
                            .message("Text URL is missing!")
                            .build());
        }

        var operations = new TextProcessingOperations(request.getUrl());

        var frequentWords = operations.topWords(request.getNumberOfFrequentWords());
        var longestWords = operations.longestWords(request.getNumberOfLongestWords());
        var numberOfLines = operations.numberOfLines();
        var numberOfWords = operations.numberOfWords();

        var response = ProcessingResponse
                .builder()
                .mostFrequentWords(frequentWords)
                .numberOfLines(numberOfLines)
                .numberOfWords(numberOfWords)
                .longestWords(longestWords)
                .build();

        return ResponseEntity.ok(response);
    }

}
