package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.FileParseException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
    }

    public void read(String link) {
        var rootPage = wikiReader.read(link);
        var visitedLinks = new HashSet<String>();
        var wikiPageQueue = new LinkedList<WikiPage>();
        wikiPageQueue.add(rootPage);

        crawl(wikiPageQueue, visitedLinks);
    }

    private void crawl(Queue<WikiPage> wikiPageQueue, Set<String> visitedLinks) {
        if (wikiPageQueue.isEmpty()) {
            return;
        }

        var page = wikiPageQueue.poll();
        visitedLinks.add(page.selfLink());
        repository.save(page);

        page.links().stream()
            .filter(childLink -> linkIsNotVisited(visitedLinks, childLink))
            .forEach(childLink -> {
                try {
                    var childPage = wikiReader.read(childLink);
                    wikiPageQueue.add(childPage);
                    visitedLinks.add(childPage.selfLink());
                } catch (FileParseException e) {
                }
            });

        crawl(wikiPageQueue, visitedLinks);
    }

    private boolean linkIsNotVisited(Set<String> visitedLinks, String childLink) {
        return !visitedLinks.contains(childLink);
    }
}
