package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.PageParsingException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WikiScrapperTest {

    @Mock
    private WikiReader wikiReader;
    @Mock
    private WikiPageRepository repository;
    @InjectMocks
    private WikiScrapper wikiScrapper;

    @Test
    void shouldReadPageWithNoLinks() {
        // given
        var link = "test-url";
        var page = getPage(link, Collections.emptyList());
        when(wikiReader.read(link)).thenReturn(page);

        // when
        wikiScrapper.read(link);

        // then
        verify(wikiReader, times(1)).read(anyString());
        verify(repository, times(1)).save(any(WikiPage.class));
    }

    @Test
    void shouldReadPageWithCyclicLinks() {
        // given
        var link = "test-url";
        var childLink1 = "test-url-1";
        var childLink2 = "test-url-2";
        var page = getPage(link, List.of(childLink1, childLink2));
        var childPage1 = getPage(childLink1, List.of(link, childLink2));
        var childPage2 = getPage(childLink2, List.of(link, childLink1));
        when(wikiReader.read(link)).thenReturn(page);
        when(wikiReader.read(childLink1)).thenReturn(childPage1);
        when(wikiReader.read(childLink2)).thenReturn(childPage2);

        // when
        wikiScrapper.read(link);

        // then
        verify(wikiReader, times(3)).read(anyString());
        verify(repository, times(3)).save(any(WikiPage.class));
    }

    @Test
    void shouldNotReadPageWithoutRoot() {
        // given
        var link = "test-url";
        var exceptionMessage = "Provided Wiki page does not exist";
        when(wikiReader.read(link)).thenThrow(new WikiPageNotFound(exceptionMessage));

        // when
        var exception = assertThrows(WikiPageNotFound.class, () -> wikiScrapper.read(link));

        // then
        assertEquals(exceptionMessage, exception.getMessage());
        verify(wikiReader, times(1)).read(link);
        verify(repository, never()).save(any(WikiPage.class));
    }

    @Test
    void shouldReadPageWhenChildLinkThrowsException() {
        // given
        var link = "test-url";
        var childLink = "test-url-2";
        var page = getPage(link, List.of(childLink));
        when(wikiReader.read(link)).thenReturn(page);
        when(wikiReader.read(childLink)).thenThrow(new PageParsingException("Unable to parse file"));

        // when
        wikiScrapper.read(link);

        // then
        verify(wikiReader, times(2)).read(anyString());
        verify(repository, times(1)).save(any(WikiPage.class));
    }

    @Test
    void shouldReadPageWithLinks() {
        // given
        var link = "test-url";
        var childLink = "test-url-2";
        var page = getPage(link, List.of(childLink));
        var childPage = getPage(childLink, Collections.emptyList());
        when(wikiReader.read(link)).thenReturn(page);
        when(wikiReader.read(childLink)).thenReturn(childPage);

        // when
        wikiScrapper.read(link);

        // then
        verify(wikiReader, times(2)).read(anyString());
        verify(repository, times(2)).save(any(WikiPage.class));
    }

    private WikiPage getPage(String link, List<String> links) {
        return new WikiPage("title", "content", link, links);
    }
}
