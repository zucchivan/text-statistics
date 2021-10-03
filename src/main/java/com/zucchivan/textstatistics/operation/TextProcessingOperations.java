package com.zucchivan.textstatistics.operation;

import com.zucchivan.textstatistics.exception.InvalidOperationException;
import com.zucchivan.textstatistics.model.IWordFrequency;
import com.zucchivan.textstatistics.model.WordFrequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextProcessingOperations implements ITextStatistics {

    private final Logger logger = LoggerFactory.getLogger(TextProcessingOperations.class);

    /* Pre-compiled regex pattern for words */
    private static final Pattern WORDS_REGEX_PATTERN = Pattern.compile("[^\\w]+");

    private String textUrl;
    public TextProcessingOperations(String textUrl) {
        this.textUrl = textUrl;
    }

    @Override
    @Async
    public List<IWordFrequency> topWords(int n) {
        if (this.textUrl == null)
            throw new InvalidOperationException("No text URL set for operation");

        var text = getTextAsString();
        var wordsArray = extractWords(text);
        var frequencyMap = createFrequencyMap(wordsArray);

        List<IWordFrequency> result= frequencyMap.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, AtomicInteger>>comparingInt(i -> i.getValue().get())
                        .reversed())
                .limit(n)
                .map(m -> new WordFrequency(m.getKey(), m.getValue().get()))
                .collect(Collectors.toList());

        return result;
    }

    @Async
    public CompletableFuture<List<IWordFrequency>> topWordsAsync(int n) {
        return CompletableFuture.completedFuture(topWords(n));
    }

    @Override
    @Async
    public List<String> longestWords(int n) {
        var wordsArray = this.extractWords(this.getTextAsString());

        var alphabeticalOrderList = Arrays.stream(wordsArray)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        return alphabeticalOrderList.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length)
                        .reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<List<String>> longestWordsAsync(int n) {
        return CompletableFuture.completedFuture(longestWords(n));
    }

    @Override
    @Async
    public long numberOfWords() {
        var wordsArray = this.extractWords(this.getTextAsString());
        return wordsArray.length;
    }

    @Async
    public CompletableFuture<Long> numberOfWordsAsync() {
        return CompletableFuture.completedFuture(numberOfWords());
    }

    @Override
    @Async
    public long numberOfLines() {
        if (this.textUrl == null)
            throw new InvalidOperationException("No text URL set for operation");

        long count = 0;

        try (InputStream inputStream = new URL(textUrl).openStream()) {
            var c = new byte[1024];

            var readChars = inputStream.read(c);
            if (readChars == -1) {
                return 0;
            }

            while (readChars == 1024) {
                for (int i = 0; i < 1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = inputStream.read(c);
            }

            // Counting remaining characters
            while (readChars != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }

                readChars = inputStream.read(c);
            }
        } catch (IOException e) {
            logger.error("Error while counting text's lines!", e);
        }

        return count;
    }

    @Async
    public CompletableFuture<Long> numberOfLinesAsync() {
        return CompletableFuture.completedFuture(numberOfLines());
    }

    private String getTextAsString() {
        if (this.textUrl == null)
            throw new InvalidOperationException("No text URL set for operation");

        String text = null;
        try (InputStream inputStream = new URL(this.textUrl).openStream()){
            text = inputStreamToString(inputStream);
        } catch (IOException e) {
            logger.error("Error while converting InputStream to String!", e);
        }

        return text;
    }

    private String inputStreamToString(InputStream stream) throws IOException {
        var bufferSize = 1024;
        var buffer = new char[bufferSize];
        var out = new StringBuilder();
        var in = new InputStreamReader(stream, StandardCharsets.UTF_8);

        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }

        return out.toString();
    }

    private String[] extractWords(String text) {
        return WORDS_REGEX_PATTERN.split(text);
    }

    /**
     * This method uses AtomicInteger as a thread safe way of incrementing
     * the number of occurrences of the words in a text. That way we can use
     * parallel processing provided by Java Stream APIs.
     */
    private ConcurrentSkipListMap<String, AtomicInteger> createFrequencyMap(String[] wordsArray) {
        var frequencyMap = new ConcurrentSkipListMap<String, AtomicInteger>();

        Stream.of(wordsArray)
                .parallel()
                .forEach(word -> {
                    if (word.length() > 0) {
                        frequencyMap.putIfAbsent(word, new AtomicInteger(1));
                        frequencyMap.get(word).incrementAndGet();
                    }
                });

        return frequencyMap;
    }

}
