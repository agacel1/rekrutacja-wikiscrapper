package com.lastminute.recruitment.domain;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
    }


    public void read(String link) {

    }

}
