package com.zucchivan.textstatistics.operation;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TextProcessingOperations {

    public Map<String, AtomicInteger> proccessText(String url) {
        InputStream inputStream = null;
        String text = null;

        try {
            inputStream = new URL(url).openStream();
            text = inputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final var map = new ConcurrentSkipListMap<String, AtomicInteger>();
        var wordsArray = extractWords(text);

        for (int i = 0; i < wordsArray.length; i++) {
            final String key = wordsArray[i];

            if (key.length() > 0) {
                map.putIfAbsent(key, new AtomicInteger(1));
                map.get(key).incrementAndGet();
            }
        }

        return map;
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

    public String[] extractWords(String text) {
        //Arrays.sort(words);
        return text.split("[^\\w]+");
    }

    public StringTokenizer getStringTokenizer(char[] buffer) {
        //StringTokenizer st = new StringTokenizer(read);
        return null;
    }

}
