package com.lastminute.recruitment.persistence;

import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiPageRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryWikiPageRepository implements WikiPageRepository {

    private long idCounter = 0;
    private final Map<Long, WikiPage> db = new HashMap<>();


    @Override
    public void save(WikiPage wikiPage) {
        db.put(idCounter++, wikiPage);
        System.out.println("WikiPages in DB:");
        db.keySet().forEach(wikiPageKey ->
                System.out.println(wikiPageKey + "->" + db.get(wikiPageKey))
        );
    }

}
