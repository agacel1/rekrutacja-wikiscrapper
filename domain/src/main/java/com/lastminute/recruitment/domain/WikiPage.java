package com.lastminute.recruitment.domain;

import java.util.List;

public record WikiPage(String title, String content, String selfLink, List<String> links) {
}
