package com.lastminute.recruitment.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.client.JsonWikiClient;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.PageParsingException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.io.File;
import java.io.IOException;

public class JsonWikiReader implements WikiReader {

    private final JsonWikiClient client;
    private final ObjectMapper objectMapper;

    public JsonWikiReader(final JsonWikiClient client,
                          final ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public WikiPage read(String link) {
        File file = getFile(link);

        try {
            return objectMapper.readValue(file, WikiPage.class);
        } catch (IOException e) {
            throw new PageParsingException("Unable to parse JSON file", e);
        }
    }

    private File getFile(String link) {
        try {
            String filePath = client.readJson(link);
            return new File(filePath);
        } catch (NullPointerException e) {
            throw new WikiPageNotFound(String.format("Wiki page does not exist: %s", link), e);
        }
    }
}
