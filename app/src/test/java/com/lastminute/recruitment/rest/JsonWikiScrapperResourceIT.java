package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.WikiScrapperApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = WikiScrapperApplication.class)
@ActiveProfiles(value = "json")
class JsonWikiScrapperResourceIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldScrapPagesForGivenLink() {
        // given
        var requestBody = "\"http://wikiscrapper.test/site1\"";

        // when
        webTestClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/wiki/scrap")
                .build())
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue(requestBody)
            .exchange()
            // then
            .expectStatus().isOk();
    }

    @Test
    void shouldReturnBadRequestForIssueWithParsingWikiPage() {
        // given
        var requestBody = "\"http://wikiscrapper.test/site5\"";

        // when
        webTestClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/wiki/scrap")
                .build())
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue(requestBody)
            .exchange()
            // then
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Unable to parse JSON file");
    }

    @Test
    void shouldReturnNotFoundForNotExistingWikiPage() {
        // given
        var requestBody = "\"http://wikiscrapper.test/test-site\"";

        // when
        webTestClient
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/wiki/scrap")
                .build())
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue(requestBody)
            .exchange()
            // then
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Wiki page does not exist: \"http://wikiscrapper.test/test-site\"");
    }
}
