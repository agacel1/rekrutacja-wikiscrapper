package com.lastminute.recruitment.reader;

import com.lastminute.recruitment.client.HtmlWikiClient;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.PageParsingException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class HtmlWikiReader implements WikiReader {

    private final HtmlWikiClient client;

    public HtmlWikiReader(final HtmlWikiClient client) {
        this.client = client;
    }

    @Override
    public WikiPage read(String link) {
        Document doc = getDocument(link);
        String title = getTitle(doc);
        String content = getContent(doc);
        String selfLink = getSelfLink(doc);
        List<String> links = getLinks(doc);

        return new WikiPage(title, content, selfLink, links);
    }

    private Document getDocument(String link) {
        File file = getFile(link);

        try {
            return Jsoup.parse(file, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new PageParsingException("Unable to parse HTML file", e);
        }
    }

    private File getFile(String link) {
        try {
            String filePath = client.readHtml(link);
            return new File(filePath);
        } catch (NullPointerException e) {
            throw new WikiPageNotFound(String.format("Wiki page does not exist: %s", link), e);
        }
    }

    private String getTitle(Document doc) {
        return doc.title();
    }

    private String getContent(Document doc) {
        Element element = doc.selectFirst(HtmlElements.CONTENT_CLASS);

        return Optional.ofNullable(element)
            .map(Element::text)
            .orElse(null);
    }

    private String getSelfLink(Document doc) {
        Element firstElement = doc.selectFirst(HtmlElements.META_TAG);

        return Optional.ofNullable(firstElement)
            .map(element -> element.attr(HtmlElements.SELF_LINK_ATTRIBUTE))
            .orElse(null);
    }

    private List<String> getLinks(Document doc) {
        Elements elements = doc.select(HtmlElements.LINK_TAG);

        return elements.stream()
            .map(element -> element.attr(HtmlElements.HREF_ATTRIBUTE))
            .toList();
    }
}
