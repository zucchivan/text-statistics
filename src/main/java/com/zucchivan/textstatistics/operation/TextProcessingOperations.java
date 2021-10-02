package com.zucchivan.textstatistics.operation;

import com.zucchivan.textstatistics.exception.InvalidOperationException;
import com.zucchivan.textstatistics.model.IWordFrequency;
import com.zucchivan.textstatistics.model.WordFrequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextProcessingOperations implements ITextStatistics {

    private final Logger logger = LoggerFactory.getLogger(TextProcessingOperations.class);

    private String textUrl;

    public TextProcessingOperations(String textUrl) {
        this.textUrl = textUrl;
    }

    @Override
    public List<IWordFrequency> topWords(int n) {
        if (this.textUrl == null)
            throw new InvalidOperationException("No text URL set for operation");

        var text = getTextAsString();
        var wordsArray = extractWords(text);
        var frequencyMap = createFrequencyMap(wordsArray);

        List<IWordFrequency> result= frequencyMap.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, AtomicInteger>>comparingInt(i -> {
                        return i.getValue().get();
                    }).reversed())
                .limit(n)
                .map(m -> new WordFrequency(m.getKey(), m.getValue().get()))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<String> longestWords(int n) {
        return null;
    }

    @Override
    public long numberOfWords() {
        return 0;
    }

    @Override
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
        return text.split("[^\\w]+");
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
