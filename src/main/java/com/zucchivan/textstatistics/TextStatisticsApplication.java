package com.zucchivan.textstatistics;

import com.zucchivan.textstatistics.operation.TextProcessingOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class TextStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextStatisticsApplication.class, args);
	}

}
